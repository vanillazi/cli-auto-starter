package cn.vanillazi.tool.installer;

import cn.vanillazi.commons.win32.ExecUtils;
import cn.vanillazi.commons.win32.StartupUtils;
import cn.vanillazi.commons.win32.desktop.DesktopShortcutCreator;
import cn.vanillazi.tool.constant.Constants;
import com.sun.jna.Platform;

import java.io.File;

public class Uninstaller implements ExecUtils.RunOnAdmin {

    public static void main(String[] args) {
        if(Platform.isWindows()) {
            ExecUtils.launchInAdmin(Uninstaller.class, args, false);
        }else if(Platform.isLinux()){
            removeLinuxDesktopFile();
        }
    }

    protected static void removeLinuxDesktopFile(){
        var df=new File(Installer.LINUX_USER_DESKTOP_FILE_PATH);
        if(df.exists()){
            df.delete();
        }
    }
    @Override
    public void runOnAdmin(String[] args) {
        var shortcutPath=DesktopShortcutCreator.buildDesktopShortcutPath(Constants.APP_ID);
        var shortcutFile=new File(shortcutPath);
        if(shortcutFile.exists()) {
            shortcutFile.delete();
        }
        StartupUtils.remove(Constants.APP_ID);
    }
}
