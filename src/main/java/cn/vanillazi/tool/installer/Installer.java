package cn.vanillazi.tool.installer;

import cn.vanillazi.commons.win32.ExecUtils;
import cn.vanillazi.commons.win32.StartupUtils;
import cn.vanillazi.commons.win32.desktop.DesktopShortcutCreator;
import cn.vanillazi.tool.constant.Constants;

import java.io.File;
import java.io.IOException;

public class Installer implements ExecUtils.RunOnAdmin {

    public static void main(String[] args) {
        ExecUtils.launchInAdmin(Installer.class,args,false);
    }

    @Override
    public void runOnAdmin(String[] args) {
        var f=new File("");
        var exePath=f.getAbsolutePath()+File.separator+ Constants.APP_ID+".exe";
        try {
            DesktopShortcutCreator.createDesktopIcon(f.getAbsolutePath(),exePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        StartupUtils.set(Constants.APP_ID,exePath);
    }
}
