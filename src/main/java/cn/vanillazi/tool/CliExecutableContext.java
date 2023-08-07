package cn.vanillazi.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class CliExecutableContext implements Runnable{

    private Logger logger;

    private StartupItem startupItem;
    private CommandProcessListener commandProcessListener;

    private volatile Thread thread;
    private Process process;
    public CliExecutableContext(StartupItem startupItem,CommandProcessListener commandProcessListener) {
        this.startupItem = startupItem;
        this.commandProcessListener=commandProcessListener;
        logger= LoggerFactory.getLogger(CliExecutableContext.class.getName()+"."+startupItem.getName());
    }

    public StartupItem getStartupItem() {
        return startupItem;
    }

    public void start(){
        if(thread!=null){
            return;
        }
        thread=new Thread(this);
        thread.start();
    }

    public void stop(){
        if(thread!=null) {
            process.destroyForcibly();
            thread.interrupt();
            thread = null;
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
            process=new ProcessBuilder()
                    .command(cmd)
                    .redirectOutput(ProcessBuilder.Redirect.PIPE)
                    .redirectError(ProcessBuilder.Redirect.PIPE)
                    .start();
            commandProcessListener.onStarted();
            streamToLog(process.getErrorStream(),true);
            streamToLog(process.getInputStream(),false);
        } catch (IOException e) {
            logger.error("",e);
            commandProcessListener.onError("",e);
        }
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            logger.error("",e);
        }
        thread=null;
        commandProcessListener.onFinished();
    }

    public void streamToLog(InputStream in,boolean error){
        Scanner scanner=new Scanner(in);
        while (scanner.hasNext()){
            var line=scanner.nextLine();
            if(error) {
                logger.error(line);
            }else{
                logger.info(line);
            }
        }
    }

}
