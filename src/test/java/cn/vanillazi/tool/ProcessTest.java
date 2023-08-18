package cn.vanillazi.tool;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ProcessTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        var p=new ProcessBuilder()
                .command("ps","x")
                .inheritIO()
                .start();
        p.waitFor(60, TimeUnit.SECONDS);
    }
}
