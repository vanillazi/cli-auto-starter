package cn.vanillazi.tool.log;

import com.google.common.eventbus.EventBus;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LogInitializer {

    public static EventBus logDispatcher=new EventBus();

    public static void init(){
        var root=Logger.getLogger("");
        root.addHandler(new Handler() {
            @Override
            public void publish(LogRecord record) {
                logDispatcher.post(record);
            }

            @Override
            public void flush() {}

            @Override
            public void close() throws SecurityException {}
        });
    }
}
