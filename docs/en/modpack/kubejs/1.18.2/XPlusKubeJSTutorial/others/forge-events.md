# 19 Forge事件监听

***

KubeJS提供了很多事件，但相较于Forge依旧不够全面。因此，KubeJS提供了 `onForgeEvent` 用于监听 Forge 事件。

**一、准备工作**

为便于编写事件，你需要先在你的电脑上部署Forge开发环境。

* 下载并安装[IntelliJ IDEA](https://www.jetbrains.com/idea/)
*   下载Forge MDK

    ![](https://m1.miaomc.cn/uploads/20220924\_632f2636b1fef.png)
* 右键解压后的文件夹，选择"Open Folder as lntelliJ IDEA Project"，在弹出的窗口中选择"Trust Project"，之后IDEA应该会自动配置开发环境。该过程会从Mojang，MinecraftForge等站点下载资源，可能速度较慢。
  * 若下载多次出现问题可以尝试使用[离线开发包](https://www.mcbbs.net/thread-896542-1-1.html)。
  * 尝试[设置代--理](https://fmltutor.ustc-zzzz.net/1.1-%E9%85%8D%E7%BD%AE%E4%BD%A0%E7%9A%84%E5%B7%A5%E4%BD%9C%E7%8E%AF%E5%A2%83.html#%E9%85%8D%E7%BD%AE%E5%B7%A5%E4%BD%9C%E7%8E%AF%E5%A2%83)
* 安装[Minecraft Development](https://plugins.jetbrains.com/plugin/8327-minecraft-development)插件。

**二、监听事件**

你可以在IDEA中按下`Ctrl` + `N`快捷键搜索Forge事件，所有`net.minecraftforge.event`下的事件均可被监听，以监听玩家改变维度事件为例：

```
onForgeEvent("net.minecraftforge.event.entity.player.PlayerEvent$PlayerChangedDimensionEvent", event=>{
    console.info("PlayerChangedDimensionEvent 触发")
})
```

**Forge事件下的所有方法都可以被调用**，例如此处获取玩家从哪里来又到哪里去：

```
onForgeEvent("net.minecraftforge.event.entity.player.PlayerEvent$PlayerChangedDimensionEvent", event=>{
    console.info(event.getFrom())
    console.info(event.getTo())
})
```

此时触发事件，日志中会打印出世界的RegistryKey（形如`" ResourceKey[minecraft:dimension / minecraft:the_nether]"`），如果我们只想要获取世界ID，应该调用RegistryKey下的`getLocation()`方法。

\*\*当调用Minecraft类中的方法等内容时，你应该使用其SRG Name而不是IDE中看到的名称。\*\*在IDEA中右键方法，选择"Get SRG Name"即可快捷获取方法的SRG Name。（也可通过[网站查询](https://linkie.shedaniel.me/mappings)）

通过查询可以获得RegistryKey下的`getLocation()`对应的SRG Name是`func_240901_a_()`：

```
onForgeEvent("net.minecraftforge.event.entity.player.PlayerEvent$PlayerChangedDimensionEvent", event=>{
    console.info(event.getFrom().func_240901_a_())// getLocation()
    console.info(event.getTo().func_240901_a_())
})
// 返回 minecraft:the_nether 和 minecraft:overworld
```

现在我们要将该消息发送给玩家，根据上文同理可得：

```
onForgeEvent("net.minecraftforge.event.entity.player.PlayerEvent$PlayerChangedDimensionEvent", event=>{
    // PlayerEntity.func_146105_b -> PlayerEntity.sendStatusMessage
    event.player.func_146105_b("你来到了" + event.getTo().func_240901_a_(), false)
})
```

**KubeJS添加了方法`.asKJS()`，通过该方法你可以将部分支持的Minecraft类转换为KubeJS中等价的方法（比如Player和ItemStack等），便于编写事件。**

则上述代码可改写为：

```
onForgeEvent("net.minecraftforge.event.entity.player.PlayerEvent$PlayerChangedDimensionEvent", event=>{
    event.player.asKJS().tell("你来到了" + event.getTo().func_240901_a_())
})
```

通过直接监听Forge事件可以解决一些KubeJS中的老大难问题，比如监听玩家重生事件或者读取键盘输入等。
