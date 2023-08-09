package cn.vanillazi.tool.installer;

import cn.vanillazi.commons.win32.ExecUtils;
import cn.vanillazi.commons.win32.StartupUtils;
import cn.vanillazi.commons.win32.desktop.DesktopShortcutCreator;
import cn.vanillazi.tool.config.ResourceBundles;
import cn.vanillazi.tool.constant.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class Installer implements ExecUtils.RunOnAdmin {

    private static final Logger logger= LoggerFactory.getLogger(Installer.class);

    public static void main(String[] args) {
        ExecUtils.launchInAdmin(Installer.class,args,false);
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
