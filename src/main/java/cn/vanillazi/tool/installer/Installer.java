package cn.vanillazi.tool.installer;

import cn.vanillazi.commons.win32.ExecUtils;
import cn.vanillazi.commons.win32.StartupUtils;
import cn.vanillazi.commons.win32.desktop.DesktopShortcutCreator;
import cn.vanillazi.tool.config.ResourceBundles;
import cn.vanillazi.tool.constant.Constants;

import com.sun.jna.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class Installer implements ExecUtils.RunOnAdmin {

    private static final Logger logger= LoggerFactory.getLogger(Installer.class);

    public static void main(String[] args) {
        if(Platform.isWindows()) {
            ExecUtils.launchInAdmin(Installer.class, args, false);
        }else if(Platform.isLinux()){
            createLinuxDesktopFile();
        }
    }

    public static final String LINUX_USER_DESKTOP_DIR=System.getProperty("user.home")+ File.separator+".local/share/applications";
    public static final String LINUX_USER_DESKTOP_FILE_PATH=LINUX_USER_DESKTOP_DIR+"/"+Constants.APP_ID+".desktop";
    public static final String LINUX_USER_AUTO_START_DESKTOP_FILE_PATH=System.getProperty("user.home")+"/.config/autostart/"+Constants.APP_ID+".desktop";
    protected static void createLinuxDesktopFile() {
        var content=createLinuxDesktop();
        var file=new File(LINUX_USER_DESKTOP_DIR);
        if(file.exists() || file.mkdirs()){
            var desktopFile=new File(LINUX_USER_DESKTOP_FILE_PATH);
            if(desktopFile.exists()){
                throw new RuntimeException("the desktop file "+LINUX_USER_DESKTOP_FILE_PATH+" is existed");
            } else {
                try {
                    Files.writeString(desktopFile.toPath(),content, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
                } catch (IOException e) {
                    throw new RuntimeException("create desktop file "+LINUX_USER_DESKTOP_FILE_PATH+" error",e);
                }
            }
        }else{
            throw new RuntimeException("create the desktop dir:["+LINUX_USER_DESKTOP_FILE_PATH+"]  failed!");
        }

        var autoStartDesktopFile=new File(LINUX_USER_AUTO_START_DESKTOP_FILE_PATH);
        if(autoStartDesktopFile.exists()){
            throw new RuntimeException("the auto start desktop file "+LINUX_USER_AUTO_START_DESKTOP_FILE_PATH+" is existed");
        }else{
            try {
                var parentFile=autoStartDesktopFile.getParentFile();
                if(parentFile.exists() || parentFile.mkdirs()){
                    Files.writeString(autoStartDesktopFile.toPath(), content, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
                }else{
                    throw new RuntimeException("create the auto start desktop dir:["+parentFile.getAbsolutePath()+"]  failed!");
                }
            } catch (IOException e) {
                throw new RuntimeException("create auto start file "+LINUX_USER_AUTO_START_DESKTOP_FILE_PATH+" error",e);
            }
        }
    }

    public static String createLinuxDesktop(){
        String s= """
                [Desktop Entry]
                Path=%s
                Exec=%s/run-foreground.sh
                GenericName=%s
                Name=%s
                Terminal=false
                Type=Application
                Icon=%s/asset/logo.png
                """;
        var currentDirectory=new File("").getAbsolutePath();
        return s.formatted(currentDirectory,currentDirectory,ResourceBundles.appName(),ResourceBundles.appName(),currentDirectory);
    }

    @Override
    public void runOnAdmin(String[] args) {
        var currentDirectory=new File("").getAbsolutePath();
        var exePath=currentDirectory+File.separator+ Constants.APP_ID+".exe";
        try {
            DesktopShortcutCreator.createDesktopIcon(currentDirectory,exePath);
        } catch (IOException e) {
            logger.error(ResourceBundles.failedToCreateDesktopShortcut(),e);
            throw new RuntimeException(e);
        }
        StartupUtils.set(Constants.APP_ID,exePath);
    }
}
