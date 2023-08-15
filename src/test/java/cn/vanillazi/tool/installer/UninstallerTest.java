package cn.vanillazi.tool.installer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UninstallerTest {

    @Test
    void removeLinuxDesktopFile() {
        Uninstaller.removeLinuxDesktopFile();
    }
}