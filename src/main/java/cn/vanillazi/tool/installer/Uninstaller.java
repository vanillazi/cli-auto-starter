package cn.vanillazi.tool.installer;

import cn.vanillazi.commons.win32.ExecUtils;
import cn.vanillazi.commons.win32.StartupUtils;
import cn.vanillazi.commons.win32.desktop.DesktopShortcutCreator;
import cn.vanillazi.tool.constant.Constants;

import java.io.File;

public class Uninstaller implements ExecUtils.RunOnAdmin {

    public static void main(String[] args) {
        ExecUtils.launchInAdmin(Uninstaller.class, args, false);
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
