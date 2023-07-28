# 命令行启动器
Windows下命令行程序自启动托盘程序
## 构建
```shell
mvn package -Pdist
```
## 安装
```powershell
cd ${安装目录}
./run.cn.vanillazi.tool.installer.Installer.bat
```
## 卸载
```powershell
cd ${安装目录}
./run.cn.vanillazi.tool.installer.Uninstaller.bat
```