package cn.vanillazi.tool;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.ArrayList;

public class CliExecutableContext implements Runnable{

    private StartupItem startupItem;

    private volatile Thread thread;
    private Process process;
    public CliExecutableContext(StartupItem startupItem) {
        this.startupItem = startupItem;
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
            IOUtils.copy(process.getErrorStream(),System.out);
            IOUtils.copy(process.getInputStream(),System.out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        thread=null;
    }
}
