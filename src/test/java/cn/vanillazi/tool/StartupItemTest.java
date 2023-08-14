package cn.vanillazi.tool;

import com.google.gson.GsonBuilder;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class StartupItemTest {

    public static void main(String[] args) {
        var si=new StartupItem();
        si.setArgs(Arrays.asList("test","123"));
        si.setName("test");
        si.setExecutable("a:/test.exe");
        si.setDescription("just for test");
        si.setAutoStart(false);
        var sis=Arrays.asList(si);
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(sis));
    }
}