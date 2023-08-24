# 命令行启动器
Windows,Ubuntu下命令行程序自启动托盘程序
## 构建
### Windows
```powershell
mvn package -PdistWin
```
### Ubuntu
```shell
mvn package -PdistLinux
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