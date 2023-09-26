package cn.vanillazi.tool;

import org.junit.jupiter.api.Test;

public class ProcessRestarterTest {

    public static void main(String[] args) {

        new ProcessRestarterTest().extractCommands();

    }

    @Test
    public void extractCommands(){
        var cmds=ProcessRestarter.extractCommands();
        System.out.println(String.join(",",cmds));
    }

    @Test
    public void testSystemProperties(){
        System.getProperties().forEach((k,v)->{
            System.out.println(k+"-->"+v);
        });
    }

}
