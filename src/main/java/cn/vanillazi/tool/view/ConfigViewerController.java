package cn.vanillazi.tool.view;

import cn.vanillazi.commons.fx.view.BaseDialog;

import cn.vanillazi.tool.StartupItem;
import cn.vanillazi.tool.config.AppConfigs;
import cn.vanillazi.tool.config.ResourceBundles;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static cn.vanillazi.tool.constant.Constants.DEFAULT_CONF_PATH;
import static cn.vanillazi.tool.constant.Constants.ICON_PATH;

public class ConfigViewerController extends BaseDialog implements Initializable {
    private static final Logger logger= LoggerFactory.getLogger(ConfigViewerController.class.getName());
    @FXML
    protected StyleClassedTextArea area;
    @FXML
    protected MenuItem save;
    @FXML
    protected MenuItem insert;
    @FXML
    protected MenuItem format;
    @Override
    public void onInitUI() {
        stage.setTitle(ResourceBundles.config());
        stage.getIcons().add(new javafx.scene.image.Image(ICON_PATH));
        try {
            loadConfigFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        save.setOnAction(e->{
            var str=area.getText();
            try {
                Files.writeString(DEFAULT_CONF_PATH,str,StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        insert.setOnAction(e->{
            var str=area.getText();
            if(str.isEmpty()){
                var arrays=new ArrayList<StartupItem>();
                arrays.add(AppConfigs.newItem());
                str= AppConfigs.gson.toJson(arrays);
            }else{
                var arrays=new ArrayList<StartupItem>(AppConfigs.parseFromJson(str));
                arrays.add(AppConfigs.newItem());
                str= AppConfigs.gson.toJson(arrays);
            }
            area.replaceText(str);
        });
        format.setOnAction(actionEvent -> {
            var str=area.getText();
            if(!str.isEmpty()){
                var arrays=new ArrayList<StartupItem>(AppConfigs.parseFromJson(str));
                str= AppConfigs.gson.toJson(arrays);
            }
            area.replaceText(str);
        });
    }

    private void loadConfigFile() throws IOException {
        if(!DEFAULT_CONF_PATH.toFile().exists()){
            try {
                var parentFile=DEFAULT_CONF_PATH.toFile().getParentFile();
                if(!parentFile.exists()){
                    parentFile.mkdirs();
                }
                DEFAULT_CONF_PATH.toFile().createNewFile();
            } catch (IOException e) {
                logger.error(ResourceBundles.failedToCreateConfigurationFile(),e);
                throw new RuntimeException(e);
            }

        }
        var str=Files.readString(DEFAULT_CONF_PATH, StandardCharsets.UTF_8);
        area.append(str,"");
    }

    @Override
    protected void onWindowClose() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
