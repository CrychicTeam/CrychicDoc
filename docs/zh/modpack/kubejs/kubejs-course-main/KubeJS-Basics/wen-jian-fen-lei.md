## 认识KubeJS的文件夹
kjs在第一次运行后，会在版本文件夹下创建一个自己的魔改文件夹 **`kubejs`** ，在该文件夹里有\
**`assets`**\
**`data`** \
**`client_scripts`** \
**`config`** \
**`server_scripts`**\
**`startup_scripts`**\
这些文件夹，下面会介绍这些文件夹所存放的类型

**`assets`** 是存放材质的，比如lang文件或者物品贴图和模型等都在该文件夹内进行更改
> **KubeJS 6不建议使用KubeJS内置资源包或数据包, KubeJS 6的资源加载优先级比资源包都低, 别人加个资源包就会覆盖掉了, 当然你要是说加资源包是玩家的自由那我无话可说**

> **这边顺便打个广告, 全局资源包建议使用[Json Things](https://www.mcmod.cn/class/7734.html) Mod进行内置**

**`data`** 是写数据包的，KubeJS支持直接写全局数据包，让创建世界时默认加载该数据
> **和上面的一样, 建议使用别的Mod进行内置**

**`config`** 是存放配置文件的，可以自己定义一些配置文件丢里面
> **只是说KubeJS自己所提供的配置文件, 例如图标和窗口标题的修改等**

**`client_scripts`** 客户端代码，除了逻辑处理代码可以写在客户端里，比如游戏内的物品渲染等，使用F3+T时重载, 同时可以用`kubejs reload client_scripts`指令进行脚本的重载(一般用于PonderJS等Mod)

**`server_scripts`** 服务端运行的代码，在大部分逻辑处理代码都是由服务端运行，所以大部分代码写在该文件夹内就可以，使用`reload`时重载

**`startup_scripts`** 在游戏加载时运行的代码，比如添加物品、方块流体等 在游戏内可以使用`kubejs reload startup_scripts`指令进行重载(**注册新东西不能热重载, 只能重启游戏**)