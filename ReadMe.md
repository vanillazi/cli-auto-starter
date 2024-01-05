# 命令行启动器
Windows,Ubuntu,Fedora下命令行程序自启动托盘程序
## 构建
### Windows
```powershell
mvn package -PdistWin -DwithJre=true
```
### Ubuntu
```shell
mvn package -PdistLinux -DwithJre=true
```
## 安装
### Windows
```powershell
cd ${安装目录}
./run.cn.vanillazi.tool.installer.Installer.bat
```
### Ubuntu
```shell
cd ${安装目录}
./run.cn.vanillazi.tool.installer.Installer.sh
```
## 卸载
### Windows
```powershell
cd ${安装目录}
./run.cn.vanillazi.tool.installer.Uninstaller.bat
```
### Ubuntu
```shell
cd ${安装目录}
./run.cn.vanillazi.tool.installer.Uninstaller.sh
```

## 参考
1. [JReleaser Github Reference](https://jreleaser.org/guide/latest/reference/release/github.html)