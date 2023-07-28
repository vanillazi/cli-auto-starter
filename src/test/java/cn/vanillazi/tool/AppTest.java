package cn.vanillazi.tool;

import com.sun.jna.Platform;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    public void test(){
        System.getProperties().forEach((k,v)->{
            System.out.println(k+"-->"+v);
        });


    }

    @Test
    public void testOSAndArch(){
        var arch=System.getProperty("os.arch");
        var name=System.getProperty("os.name");
    }
}