# LootJs

此教程为 `LootJs` Minecraft 1.21 NeoForge 版本教程

若想访问 `1.19.2/1.20.1` 的教程，请移步[LootJs](https://docs.mihono.cn/zh/modpack/kubejs/1.20.1/Introduction/Addon/LootJs/LootJs)此篇文档进行查看

:::v-info

该篇教程源自于 LootJs 官方网站，

适用于 Minecraft 1.21 及更高版本

:::

点我查看=> [LootJs 官方教程网站，适用于 1.21+](https://docs.almostreliable.com/lootjs/)<=点我查看

`LootJs` 是 `NeoForge` 的一个 `mod`，它允许您使用`KubeJs`修改战利品表

它不附带任何预设的战利品修改，需要玩家自行编写JavaScript脚本进行修改，相当于这是一个整合包制作者使用的模组

该 Mod 由两个主要事件组成，并且都运行在`Server`：

-   `LootJS.lootTables()`
-   `LootJS.modifiers()`

注意：所有包含 LootJs 的脚本，必须放置在 `server_scripts` 内，编辑脚本的时候，我们建议你使用`VSCode`，并在游戏安装 `ProbeJs`模组以及在`VSCode`安装`ProbeJs`插件以进行辅助编写
