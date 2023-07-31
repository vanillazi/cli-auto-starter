package cn.vanillazi.tool;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class CliExecutableContext implements Runnable{

    private static final Logger logger= Logger.getLogger(CliExecutableContext.class.getName());

    private StartupItem startupItem;
    private CommandProcessListener commandProcessListener;

    private volatile Thread thread;
    private Process process;
    public CliExecutableContext(StartupItem startupItem,CommandProcessListener commandProcessListener) {
        this.startupItem = startupItem;
        this.commandProcessListener=commandProcessListener;
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
            IOUtils.copy(process.getErrorStream(),System.out);
            IOUtils.copy(process.getInputStream(),System.out);
        } catch (IOException e) {
            e.printStackTrace();
            logger.severe(e.getMessage());
            commandProcessListener.onError("",e);
        }
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.severe(e.getMessage());
        }
        thread=null;
        commandProcessListener.onFinished();
    }
}
