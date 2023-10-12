package cn.vanillazi.tool;

import cn.vanillazi.commons.fx.property.PropertyUtils;

import cn.vanillazi.commons.fx.view.ViewUtils;
import cn.vanillazi.commons.fx.view.dialog.AboutDialog;

import cn.vanillazi.commons.tray.SystemTrayBuilder;
import cn.vanillazi.commons.tray.base.MenuInfo;
import cn.vanillazi.commons.tray.base.SystemTrayConfig;
import cn.vanillazi.tool.config.AppConfigs;
import cn.vanillazi.tool.config.ResourceBundles;

import cn.vanillazi.tool.constant.Constants;
import cn.vanillazi.tool.log.LogInitializer;
import cn.vanillazi.tool.view.ConfigViewerController;
import cn.vanillazi.tool.view.LogViewerController;

import javafx.application.Application;
import javafx.application.Platform;

import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.vanillazi.tool.constant.Constants.*;

public class App {

    public static class FXInitializer extends Application{

        @Override
        public void start(Stage primaryStage) throws Exception {
            onInitUI();
        }
    }

    private static final Logger logger= LoggerFactory.getLogger(App.class.getName());

    static {
        LogInitializer.init();
    }

    public static void main(String[] args) {
        Application.launch(FXInitializer.class,args);
    }

    public static MenuInfo toMenuInfo(StartupItem startupItem){
        var mi=PropertyUtils.createPropertyClass(MenuInfo.class);
        var context=new ProcessContext(startupItem, new CommandProcessListener() {
            @Override
            public void onStarted() {
                Platform.runLater(()->mi.setDisplayName(mi.getName() + ResourceBundles.started()));
            }

            @Override
            public void onFinished() {
                Platform.runLater(()->mi.setDisplayName(mi.getName()));
            }

            @Override
            public void onError(String msg, Throwable cause) {
                Platform.runLater(()->mi.setDisplayName(mi.getName()));
            }
        });
        mi.setName(startupItem.getName());
        mi.setDisplayName(startupItem.getName());
        mi.setTooltip(startupItem.getDescription());
        mi.setTag(context);
        mi.setEventHandler(e->{
            if(context.isStarted()){
                context.stop();
            }else{
                context.start();
            }
        });
        return mi;
    }

    private static List<MenuInfo> menuItemStarters;

    public static List<MenuInfo> getMenuItemStarters() {
        return menuItemStarters;
    }

    public static SystemTrayConfig systemTrayConfig;
    private static void onInitUI() throws IOException, AWTException {

        Platform.setImplicitExit(false);

        var startupItems=loadStartupItems();
        menuItemStarters=startupItems.stream().map(App::toMenuInfo).toList();
        var trayMenuInfos = new ArrayList<>(menuItemStarters);

        var config= PropertyUtils.createPropertyClass(MenuInfo.class);
        config.setName(ResourceBundles.config());
        config.setDisplayName(ResourceBundles.config());
        config.setEventHandler(e->editConfigFile());

        var about= PropertyUtils.createPropertyClass(MenuInfo.class);
        about.setName(ResourceBundles.about());
        about.setDisplayName(ResourceBundles.about());
        about.setEventHandler(e->showAboutDialog());

        var exit=PropertyUtils.createPropertyClass(MenuInfo.class);
        exit.setName(ResourceBundles.exit());
        exit.setDisplayName(ResourceBundles.exit());
        exit.setEventHandler(e->{
            stopAllRunningCliProcess();
            System.exit(0);
        });

        var restarter=PropertyUtils.createPropertyClass(MenuInfo.class);
        restarter.setName(ResourceBundles.restart());
        restarter.setDisplayName(ResourceBundles.restart());
        restarter.setEventHandler(e->{
            stopAllRunningCliProcess();
            try {
                ProcessRestarter.restart();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        var log=PropertyUtils.createPropertyClass(MenuInfo.class);
        log.setName(ResourceBundles.log());
        log.setDisplayName(ResourceBundles.log());
        log.setEventHandler(e->ViewUtils.loadView(LogViewerController.class).show());

        trayMenuInfos.add(config);
        trayMenuInfos.add(log);
        trayMenuInfos.add(restarter);
        trayMenuInfos.add(about);
        trayMenuInfos.add(exit);
        Image image=loadTrayIcon();
        systemTrayConfig= SystemTrayBuilder.newBuilder()
                .withImage(image)
                .withIconPath(DEFAULT_LOGO_FILE.getAbsolutePath())
                .withMenuInfos(trayMenuInfos)
                .withToolTip(ResourceBundles.appName())
                .withSelectedExtraText(ResourceBundles.started())
                .enableMultiSelectMode()
                .build();
        autoStart();
    }

    public static void autoStart(){
        menuItemStarters.forEach(m->{
            if(m.getTag() instanceof ProcessContext context && context.getStartupItem().getAutoStart()){
                context.start();
            }
        });
    }

    public static void stopAllRunningCliProcess(){
        menuItemStarters.forEach(m->{
            if(m.getTag() instanceof ProcessContext context){
                context.stop();
            }
        });
    }

    private static void editConfigFile() {
        ViewUtils.loadView(ConfigViewerController.class,null,ResourceBundles.getResourceBundle()).show();
    }

    private static void showAboutDialog() {
        var about= new AboutDialog.About();
        about.setAppName(ResourceBundles.appName());
        about.setCopyright(ResourceBundles.copyright());
        about.setIconPath(ICON_PATH);
        AboutDialog.newInstance(about).show(null);
    }

    private static Image loadTrayIcon() {
        try {
            return ImageIO.read(DEFAULT_LOGO_FILE);
        } catch (IOException e) {
            logger.error(ResourceBundles.failedToLoadLogoFile(),e);
            throw new RuntimeException(e);
        }
    }

    private static List<StartupItem> loadStartupItems() throws IOException {
        if(!DEFAULT_CONF_PATH.toFile().exists()){
            return Collections.emptyList();
        }
        var json= Files.readString(DEFAULT_CONF_PATH, StandardCharsets.UTF_8);
        return AppConfigs.parseFromJson(json);
    }
}
