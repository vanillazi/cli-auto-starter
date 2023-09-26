package cn.vanillazi.tool;

import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

    @Test
    public void testModify(){
        var si=new StartupItem();
        si.setExecutable("just for test");
        si.setName("test");
        System.out.println(si.digest());
        var si1=new StartupItem();
        si1.setExecutable("just for test");
        si1.setName("test");
        Assertions.assertEquals(si.digest(),si1.digest());
        si1.setWorkDirectory("/ttt");
        Assertions.assertNotEquals(si.digest(),si1.digest());
    }
}