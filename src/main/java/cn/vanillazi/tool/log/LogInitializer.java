package cn.vanillazi.tool.log;

import java.util.concurrent.SubmissionPublisher;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LogInitializer {

    public static SubmissionPublisher<LogRecord> publisher=new SubmissionPublisher<>();
    public static void init(){
        var root=Logger.getLogger("");
        root.addHandler(new Handler() {
            @Override
            public void publish(LogRecord record) {
                publisher.submit(record);
            }

            @Override
            public void flush() {}

            @Override
            public void close() throws SecurityException {}
        });
    }
}
