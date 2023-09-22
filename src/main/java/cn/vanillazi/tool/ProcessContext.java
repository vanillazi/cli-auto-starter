package cn.vanillazi.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;

public class ProcessContext implements Runnable{

    private Logger logger;

    private StartupItem startupItem;
    private CommandProcessListener commandProcessListener;

    private volatile Thread thread;

    private volatile Thread readThread;

    private volatile  Thread readErrorThread;

    private Process process;
    public ProcessContext(StartupItem startupItem, CommandProcessListener commandProcessListener) {
        this.startupItem = startupItem;
        this.commandProcessListener=commandProcessListener;
        logger= LoggerFactory.getLogger(startupItem.getName());
    }

    public StartupItem getStartupItem() {
        return startupItem;
    }

    public void start(){
        if(thread!=null){
            return;
        }
        thread=new Thread(this,startupItem.getName());
        thread.start();
    }

    public void stop(){
        if(thread!=null) {
            if(process!=null) {
                process.destroyForcibly();
                process=null;
            }
            thread.interrupt();
            thread = null;
            if(readErrorThread!=null && readErrorThread.isAlive()){
                readErrorThread.interrupt();
            }
            readErrorThread=null;
            if(readThread!=null && readThread.isAlive()){
                readThread.interrupt();
            }
            readThread=null;
        }
    }

    public boolean isStarted(){
        return thread!=null;
    }

    @Override
    public void run() {
        try {
            var cmd=new ArrayList<String>();
            cmd.add(startupItem.getExecutable());
            cmd.addAll(startupItem.getArgs());
            var processBuilder=new ProcessBuilder()
                    .command(cmd)
                    .redirectOutput(ProcessBuilder.Redirect.PIPE)
                    .redirectError(ProcessBuilder.Redirect.PIPE);
            if(startupItem.getWorkDirectory()!=null){
                processBuilder.directory(new File(startupItem.getWorkDirectory()));
            }
            process=processBuilder.start();
            commandProcessListener.onStarted();
            readThread=new Thread(()->streamToLog(process.getInputStream(),false));
            readThread.setDaemon(true);
            readThread.start();

            readErrorThread=new Thread(()->streamToLog(process.getErrorStream(),true));
            readErrorThread.setDaemon(true);
            readErrorThread.start();
        } catch (Throwable e) {
            logger.error("",e);
            commandProcessListener.onError("",e);
        }
        if(process!=null) {
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }
        thread=null;
        commandProcessListener.onFinished();
    }

    public void streamToLog(InputStream in,boolean error){
            try(var lineReader=new BufferedReader(new InputStreamReader(in))){
                var line=lineReader.readLine();
                while (line!=null){
                    if(error) {
                        logger.error(line);
                    }else{
                        logger.info(line);
                    }
                    line=lineReader.readLine();
                };
            }catch (IOException e){

            }
    }

}
