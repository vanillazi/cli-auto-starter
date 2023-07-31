package cn.vanillazi.tool;

import cn.vanillazi.commons.fx.property.PropertyUtils;
import cn.vanillazi.commons.fx.util.TipUtils;
import cn.vanillazi.commons.fx.util.VersionUtils;
import cn.vanillazi.commons.fx.view.dialog.AboutDialog;
import cn.vanillazi.commons.fx.view.tray.MenuInfo;
import cn.vanillazi.commons.fx.view.tray.SystemTrayWindow;
import cn.vanillazi.commons.fx.view.tray.TrayWindowStarter;
import cn.vanillazi.tool.config.ResourceBundles;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class App {

    public static final String iconPath="file:"+new File("asset/logo.png").getAbsolutePath();
    private static final Logger logger=Logger.getLogger(App.class.getName());

    public static MenuInfo toMenuInfo(StartupItem si){
        var context=new CliExecutableContext(si);
        var mi=PropertyUtils.createPropertyClass(MenuInfo.class);
        mi.setName(si.getName());
        mi.setDisplayName(si.getName());
        mi.setTooltip(si.getDescription());
        //mi.setSelectable(true);
        mi.setTag(context);
        mi.setEventHandler(e->{
            if(context.isStarted()){
                mi.setDisplayName(mi.getName());
                context.stop();
            }else{
                mi.setDisplayName(mi.getName()+ResourceBundles.started());
                context.start();
            }
        });
        if(si.getAutoStart()){
            context.start();
            mi.setDisplayName(mi.getName()+ResourceBundles.started());
        }
        return mi;
    }

    public static void main(String[] args) throws AWTException, IOException {
        if(!SystemTray.isSupported()){
            logger.severe(ResourceBundles.theSystemTrayIsNotSupported());
            return;
        }
        Platform.setImplicitExit(false);
        var tray=SystemTray.getSystemTray();
        Image image=loadTrayIcon();
        var trayIcon=new TrayIcon(image);
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip(ResourceBundles.appName());
        tray.add(trayIcon);
        var menuInfos=new ArrayList<MenuInfo>();

        var sis=loadStartupItems();
        var menus=sis.stream().map(si->{
            var mi=toMenuInfo(si);
            menuInfos.add(mi);
            return mi;
        }).collect(Collectors.toList());
        var config= PropertyUtils.createPropertyClass(MenuInfo.class);
        config.setName("config");
        config.setDisplayName(ResourceBundles.config());
        config.setEventHandler(e->editConfigFile());
        var about= PropertyUtils.createPropertyClass(MenuInfo.class);
        about.setName("about");
        about.setDisplayName(ResourceBundles.about());
        about.setEventHandler(e->showAboutDialog());

        var exit=PropertyUtils.createPropertyClass(MenuInfo.class);
        exit.setName("exit");
        exit.setDisplayName(ResourceBundles.exit());
        exit.setEventHandler(e->{
                    menus.forEach(m->{
                        if(m.getTag() instanceof CliExecutableContext context){
                            context.stop();
                        }
                    });
                    System.exit(0);
                });
        menuInfos.add(config);
        menuInfos.add(about);
        menuInfos.add(exit);
        SystemTrayWindow.setSelectedExtraText(ResourceBundles.started());
        SystemTrayWindow.enableMultiSelectMode();
        SystemTrayWindow.setIconPath(iconPath);
        SystemTrayWindow.setMenuInfos(menuInfos);
        trayIcon.addMouseListener(new TrayWindowStarter(SystemTrayWindow.class));
    }

    private static void editConfigFile() {
        if(!path.toFile().exists()){
            try {
                FileUtils.forceMkdirParent(path.toFile());
                path.toFile().createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        try {
            Desktop.getDesktop().open(path.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void showAboutDialog() {

        var ad= VersionUtils.loadAbout();
        ad.setAppName(ResourceBundles.appName());
        ad.setCopyright(ResourceBundles.copyright());
        ad.setIconPath(iconPath);
        AboutDialog.newInstance(ad).show(null);
    }

    private static Image loadTrayIcon() {
        try {
            return ImageIO.read(new File("./asset/logo.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
   public static final Path path=Path.of("conf","app.json");
    private static List<StartupItem> loadStartupItems() throws IOException {
        if(!path.toFile().exists()){
            return Collections.emptyList();
        }
        var gson=new Gson();
        var json= Files.readString(path, StandardCharsets.UTF_8);
        var type=new TypeToken<List<StartupItem>>(){}.getType();
        if(json.isEmpty() || json.isEmpty()){
            return Collections.emptyList();
        }
        try {
            return gson.fromJson(json, type);
        }catch (Throwable e){
            e.printStackTrace();
            TipUtils.error(e.getMessage());
        }
        return Collections.emptyList();
    }
}
