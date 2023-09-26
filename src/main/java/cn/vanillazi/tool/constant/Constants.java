package cn.vanillazi.tool.constant;

import java.io.File;
import java.nio.file.Path;

public class Constants {

    public static final File DEFAULT_LOGO_FILE =new File("./asset/logo.png");

    public static final String ICON_PATH ="file:"+DEFAULT_LOGO_FILE.getAbsolutePath();
    public static final String APP_ID= "cli-auto-starter";

    public static final Path DEFAULT_CONF_PATH =Path.of(System.getProperty("user.home"),"."+APP_ID,"app.json");
}
