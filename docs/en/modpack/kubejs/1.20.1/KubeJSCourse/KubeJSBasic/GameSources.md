# 全局资源调用

KubeJS中可以全局调用的游戏资源

| 关键字 | 名称 | 简介 | 相关链接 |
|:-----:|:----:|:---:|:-------:|
| Item | 物品 | 精准表达一个物品 | [✔](#物品item) |
| Ingredient | 物品标签 | 表达一种标签内的所有物品 | [✔](#物品标签ingredient) |
| Fluid | 流体 | 精准表达一个或一种流体 | - |

## 物品（Item）

| 调用方法 | 返回值 |
|:-------:|:-----:|
| Item.of(`物品类型(string)`) | 对应物品的ItemStack |
| Item.of(`物品类型(string)`, `物品数量(integer)`) | 对应物品对应数量的ItemStack |
| Item.of(`物品类型(string)`, `物品NBT(string)`) | 对应物品对应NBT的ItemStack |
| Item.of(`物品类型(string)`, `物品数量(integer)`, `物品NBT(string)`) | 对应物品对应数量对应NBT的ItemStack |
| Item.playerHead(`玩家正版游戏名(string)`) | 对应玩家名称的头颅ItemStack |

## 物品标签（Ingredient）

| 调用方法 | 返回值 |
|:-------:|:-----:|
| Ingredient.of(`标签命名空间`) | 对应标签的Ingredient |

**注** 标签命名空间用法:
* `#` `标签名` 表示标签，包含了拥有该标签的所有物品
* `@` `模组名` 表示模组，包含了该模组的所有物品
* `%` `创造标签页` 表示创造标签页分类，包含了该分页下的所有物品(或者叫做创造物品栏?)