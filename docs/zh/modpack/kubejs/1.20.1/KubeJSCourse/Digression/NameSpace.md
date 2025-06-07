---
authors:
  - Gu-meng
editor: Gu-meng
---
# 什么是命名空间
来自[McWiki](https://zh.minecraft.wiki/w/%E5%91%BD%E5%90%8D%E7%A9%BA%E9%97%B4ID?variant=zh-cn)的解释

命名空间ID(Namespaced identifier，或译为命名空间标识符)、资源路径(Resource location、资源标识符(Resource identifier)或命名空间字符串(Namespaced string)是用来指明和识别游戏中特定对象而又能避免潜在的歧义和冲突的一种方式。
# 命名空间一般用来做什么
命名空间ID在游戏内部为资源路径(Resource location)，主要由以下两部分组成：

命名空间Namespace)：一个字符串，用于标识资源的唯一性。游戏的默认命名空间为minecraft

路径(Path)：一个字符串。在数据包中，一般也反应了该资源的文件路径。有时也仅仅作为一种标识名。

在模组里命名空间又被常称为modid，所以modid也是指命名空间，也是用于指向对应的资源文件夹命名的(关于叫做命名空间还是modid取决于个人习惯)

## 在注册物品时有什么用
```js
StartupEvents.registry("item", (event) => {
    event.create("meng:my_item", "basic")
})
```
上面是一个最简单的物品注册，第一个参数传入的就是我们的物品id

物品id可以看到他被 `:` 进行分开了,`:` 前面是命名空间，用于游戏去寻找资源位置的，而后面才是物品id

命名空间决定了材质的存放位置,物品id决定了材质的命名

# 命名空间和id的定义规则
命名空间和id只能接受以下的字符
- 数字`0123456789`
- 小写字母`abcdefghijklmnopqrstuvwxyz`
- 下划线`_`
- 连字符号`-`
- 点`.`

如果游戏内报错信息为`Non [a-z0-9/._-] xxxxx `说明你的id或者命名空间里有着不包含上面的规则

这里有一个特殊的的符号`/`，他无法在命名空间里进行使用，但是可以使用在id的位置，这个时候我们一般叫他为资源路径,用于划分目录

# ModID查看
`Fabric` 的 `Modid` 在 `ModFile.jar/fabric.mod.json`\
`Forge` 或 `NeoForge` 的 `Modid` 在 `ModFile.jar/META-INF/mods.toml`
