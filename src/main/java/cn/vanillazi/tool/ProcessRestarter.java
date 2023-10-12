package cn.vanillazi.tool;

import cn.vanillazi.commons.win32.SystemProperties;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProcessRestarter {

    private static final Logger logger=Logger.getLogger(ProcessRestarter.class.getName());

    public static void restart() throws IOException {
        var commands=extractCommands();
        String workDir = SystemProperties.getCurrentPath();
        logger.info(String.join(" ", commands));
        new ProcessBuilder()
                .command(commands)
                .directory(new File(workDir))
                .start();
        System.exit(0);
    }

    protected static List<String> extractCommands(){
        String javaCommands = SystemProperties.getMainClass();
        String executableFile = SystemProperties.getJavaPath();
        var commands=new ArrayList<String>();
        commands.add(executableFile);
        commands.add("-classpath");
        commands.add(SystemProperties.getJavaExeClassPath());
        if(javaCommands.contains(" ")){
            var items=javaCommands.split(" ");
            for(var item:items){
                item=item.trim();
                if(item.isEmpty()){
                    continue;
                }
                commands.add(item);
            }
        }else {
            commands.add(javaCommands);
        }
        return commands;
    }
}
