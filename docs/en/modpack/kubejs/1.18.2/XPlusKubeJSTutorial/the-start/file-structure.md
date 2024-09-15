# 1.2 文件结构

***

在正确安装KubeJS并启动过一次游戏后，你可以在版本根目录下找到kubejs这个文件夹：

```
kubejs
├─assets
│  └─kubejs
│      └─textures
│          ├─block
│          └─item
├─client_scripts
├─config
├─data
├─exported
│  └─tags
├─server_scripts
└─startup_scripts
```

以下为各个目录的功能(详见目录下`README.txt`)：

* `assets` 文件夹和资源包的功能基本相同，你可以在这里的对应目录下放自定义方块、物品的纹理、模型等，也可以当做一个全局资源包加载器（类似于OpenLoader）
* `config` 中包括对KubeJS的一些配置选项
* `data` 文件夹和数据包功能基本相同，类似于全局数据包加载器（类似于OpenLoader）
* `client_scripts` 中为客户端资源被加载时加载脚本
  * （如 `client.tick` 等被标记为Client和Client Startup的事件）（可使用 `F3` + `T` 重载）
* `server_scripts` 中为服务端资源被加载时加载的脚本
  * （如 `recipes` 等被标记为Server和Server Startup的事件）（可使用游戏内命令 `/reload` 重载）
* `startup_scripts` 中为启动时就被加载的脚本
  * （如 `item.modification` 等被标记Startup的事件）（**部分内容**可使用游戏内命令 `/kubejs reload_startup_scripts` 重载）
