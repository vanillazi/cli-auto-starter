package cn.vanillazi.tool.installer;

import org.junit.jupiter.api.Test;

class InstallerTest {

    public static void main(String[] args) {
        System.getProperties().forEach((k,v)->{
            System.out.println(k+"-->"+v);
        });
    }

    @Test
    public void createLinuxDesktopContent(){
        var s=Installer.createLinuxDesktop();
        System.out.println(s);
    }

    @Test
    public void createLinuxDesktop(){
        Installer.createLinuxDesktopFile();
    }
}