---
authors:
  - Gu-meng
editor: Gu-meng
---
# 物品稀有度(Rarity)
**稀有度主要作用是用来影响物品名称在游戏内显示的颜色效果(如果物品单独设置了颜色则不受影响)**
## 稀有度的等级
在使用调用方法时候我们得注意自己使用的`ProbeJS`版本

如果是**7.0以上**请这样导包`const { $Rarity } = require("packages/net/minecraft/world/item/$Rarity")`

如果是**7.0以下**请这样导包`const $Rarity = Java.loadClass("net.minecraft.world.item.Rarity")`
|   等级   | 含义  | 对应颜色 |     调用方法     | 对应字符串 |
| :------: | :---: | :------: | :--------------: | :--------: |
|  Common  | 常见  |   白色   |  $Rarity.COMMON  |   common   |
| Uncommon | 罕见  |   黄色   | $Rarity.UNCOMMON |  uncommon  |
|   Rare   | 稀有  |   青色   |   $Rarity.RARE   |    rare    |
|   Epic   | 史诗  |  品红色  |   $Rarity.EPIC   |    epic    |

## 一些介绍
[介绍来自mcwiki](https://zh.minecraft.wiki/w/%E7%A8%80%E6%9C%89%E5%BA%A6?variant=zh-cn)
会显示稀有度的场景：
1. 在物品栏中选中物品，出现在提示框中的物品名称
2. 在快捷栏中选中物品，出现在快捷栏上方的物品名称
3. 在物品被包含在JSON文本消息中时显示的物品名称。这包括死亡界面中的死亡消息，聊天栏中的死亡消息，以及/tellraw和/give的文本

不会显示稀有度的场景：
1. 物品展示框显示的其中被命名的物品名称
2. 在物品栏中选中潜影盒时，提示框显示其内容物的物品名称
3. 统计屏幕中的物品名称
