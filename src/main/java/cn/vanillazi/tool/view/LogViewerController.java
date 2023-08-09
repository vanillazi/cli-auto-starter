package cn.vanillazi.tool.view;

import cn.vanillazi.commons.fx.view.BaseDialog;
import cn.vanillazi.tool.CliExecutableContext;
import cn.vanillazi.tool.config.ResourceBundles;
import cn.vanillazi.tool.log.LogInitializer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.util.concurrent.Flow;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static cn.vanillazi.tool.constant.Constants.*;

public class LogViewerController extends BaseDialog implements Flow.Subscriber<LogRecord> {

    @FXML
    protected StyleClassedTextArea area;

    @Override
    public void onInitUI() {
        stage.setTitle(ResourceBundles.log());
        stage.getIcons().add(new javafx.scene.image.Image(ICON_PATH));
        area.prefWidthProperty().bind(stage.getScene().widthProperty());
        LogInitializer.publisher.subscribe(this);
    }
    public static final String PREFIX=CliExecutableContext.class.getName()+".";

    public void onLog(LogRecord record){
        Platform.runLater(()->{
            var cliName=record.getLoggerName().replace(PREFIX,"");
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
        subscription.cancel();
    }

    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription=subscription;
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(LogRecord item) {
        onLog(item);
    }

    @Override
    public void onError(Throwable throwable) {}

    @Override
    public void onComplete() {}
}
