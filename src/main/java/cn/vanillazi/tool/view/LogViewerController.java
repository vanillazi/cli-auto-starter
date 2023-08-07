package cn.vanillazi.tool.view;

import cn.vanillazi.commons.fx.view.BaseDialog;
import cn.vanillazi.tool.CliExecutableContext;
import cn.vanillazi.tool.log.LogInitializer;
import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.fxml.FXML;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LogViewerController extends BaseDialog {

    @FXML
    protected StyleClassedTextArea area;

    @Override
    public void onInitUI() {
        area.setAutoHeight(true);
        LogInitializer.logDispatcher.register(this);
    }

    public static final int prefixIndex=CliExecutableContext.class.getName().length()+1;

    @Subscribe
    public void onLog(LogRecord record){
        Platform.runLater(()->{
            var cliName=record.getLoggerName().substring(prefixIndex);
            area.append(cliName+":","bold");
            if(record.getLevel().intValue()> Level.WARNING.intValue()) {
                area.append(record.getMessage() + "\n", "red");
            }else{
                area.append(record.getMessage() + "\n", "");
            }
        });
    }

    @Override
    protected void onWindowClose() {
        LogInitializer.logDispatcher.unregister(this);
    }
}
