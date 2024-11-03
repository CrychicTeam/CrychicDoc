# LootJs

此教程为 `LootJs` Minecraft 1.21 NeoForge 版本教程

若想访问 `1.19.2/1.20.1` 的教程，请移步[LootJs](https://docs.mihono.cn/zh/modpack/kubejs/1.20.1/Introduction/Addon/LootJs/LootJs)此篇文档进行查看

:::v-info

该篇教程源自于 LootJs 官方网站，

适用于 Minecraft 1.21 及更高版本

:::

点我查看=> [LootJs 官方教程网站，适用于 1.21+](https://docs.almostreliable.com/lootjs/)<=点我查看

`LootJs` 是 `NeoForge` 的一个 `mod`，它允许您使用`KubeJs`修改战利品表

它不附带任何预设的战利品修改，需要玩家自行编写 JavaScript 脚本进行修改，相当于这是一个整合包制作者使用的模组

该 Mod 由两个主要事件组成，并且都运行在`Server`：

-   `LootJS.lootTables()`
-   `LootJS.modifiers()`

注意：所有包含 LootJs 的脚本，必须放置在 `server_scripts` 内，编辑脚本的时候，我们建议你使用`VSCode`，并在游戏安装 `ProbeJs`模组以及在`VSCode`安装`ProbeJs`插件以进行辅助编写

**对于新版的`LootJs`，此篇教程不做过多对于`LootJS.modifiers()`的教程描述，与 1.19.2/1.20.1 版本的对比，改动不是很多**

## 事件差异性

`LootJS.lootTables`和`LootJS.modifiers`

相较于 1.19.2/1.20.1 版本的`LootJs`模组，1.21 在此基础上新增了一个`LootJS.lootTables`用于更多的修改

### `LootJS.lootTables()`

此事件直接修改通过数据包加载的战利品表。这允许在不丢失有关滚动、条件、战利品功能等的任何信息的情况下更新战利品表。您可以遍历战利品表的不同部分并对其进行修改

由于是直接修改的类型，对于一些模组，可以显示所修改的配方，映射在`JEI`或者`REI`当中，例如 JEI 的附属模组`Just Enough Resources`，以及 REI 的附属模组`Roughly Enough Resources`

### LootJS.modifiers()

此事件中指定的修改将在战利品表滚动后动态调用。他们没有关于战利品表结构的任何信息。修饰符仅包含有关将要丢弃的项目的信息。该事件允许直接修改放置的项目

与 1.19.2/1.20.1 版本的`LootJs`一样，该方法是直接添加以及修改战利品，上述所说的可以修改原版数据包加载的战利品列表

### 附加信息

NeoForge 提供了 [Global Loot Modifier](https://docs.neoforged.net/docs/resources/server/loottables/glm/) 系统，它允许 Mod 在滚动特定的战利品表时动态添加战利品。此信息不存在于战利品表中，这意味着该事件无法跟踪有关它们的任何信息。相反，它们可以在 NeoForge 钩子之后运行时使用事件进行修改

