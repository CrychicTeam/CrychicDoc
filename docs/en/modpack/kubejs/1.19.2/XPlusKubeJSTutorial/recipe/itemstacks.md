# 2.1 物品的表示——ItemStack和Ingredient

**关于ProbeJS提示**

_**带有🔎符号的段落代表该段内容ProbeJS中有语言文档，或使用ProbeJS编写较为简便。**_

***

## 一、ItemStack

顾名思义，ItemStack可以代表一组物品。

🔎 以下为一些实例：

| **例子**                                                                                      | 解释                                                |
| --------------------------------------------------------------------------------------------- | --------------------------------------------------- |
| Item.of("minecraft:diamond")                                                                  | 1个铁锭                                             |
| Item.of("minecraft:diamond").withCount(6)                                                     | 4个铁锭                                             |
| Item.of("minecraft:iron\_ingot").withCount(5).withName("KubeJS魔改教程")                      | 5个名字为"KubeJS魔改教程"的铁锭                     |
| Item.of("minecraft:diamond\_sword").ignoreNBT()                                               | 忽略了NBT的钻石剑（多用于忽略物品耐久、附魔等属性） |
| Item.of("minecraft:enchanted\_book", {StoredEnchantments:\[{lvl:1,id:"minecraft:sweeping"}]}) | 横扫之刃I附魔书（直接添加NBT例子）                  |
| Item.of("minecraft:enchanted\_book").enchant("minecraft:sweeping", 1)                         | 横扫之刃I附魔书（使用函数添加NBT例子）              |
| Item.of(/create:.\*/)                                                                         | 所有机械动力物品（正则表达式）                      |
| ...                                                                                           | ...                                                 |

在KubeJS中，你可以直接使用物品ID来表示单个物品，例如`"minecraft:diamond_sword"`代表一把钻石剑。

你还可以在物品前加上倍数来表示物品个数，例如`"5x minecraft:cobblestone"`代表5个圆石。

## 二、Ingredient

Ingredient类似于一个集合，它能够代表其所包含的所有物品，包括但不限于tag、物品列表......

以下为一些实例：

| 例子                                                            | 意义                             |
| --------------------------------------------------------------- | -------------------------------- |
| Ingredient.of("#minecraft:logs")                                | 代表tag：#minecraft:logs         |
| Ingredient.matchAny\["minecraft:diamond", "@tinkersconstruct"]) | 获得一个包含当前筛选条件的物品组 |
| ...                                                             | ...                              |

`Ingredient.of()`可以接受很多类型的输入并返回对应的`Ingredient`，包括正则表达式，tag（如上例），`Ingredient[]`，模组名称（如`@xplus`）

另请参阅：[ItemStackJS (kubejs.com)](https://kubejs.com/wiki/kubejs/ItemStackJS/)、[IngredientJS (kubejs.com)](https://kubejs.com/wiki/kubejs/IngredientJS/)、[旧版本教程 2.1物品表示](https://www.mcbbs.net/thread-1207772-1-1.html)

***

我们将在后续章节中详细讲解ItemStack。