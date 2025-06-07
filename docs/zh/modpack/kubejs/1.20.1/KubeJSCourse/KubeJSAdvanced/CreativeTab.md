---
authors:
  - Gu-meng
editor: Gu-meng
---
# 创造物品栏
在创造模式下，玩家可以直接通过背包里的创造物品栏来选择拿去的物品类型或者一些模组会将物品放进自己的创造物品栏里，比如在原版内：活塞、按钮、拉杆等都是在红石的创造物品栏下方。

在本章中将会介绍如何去注册一个自己的创造物品栏和将物品添加挪动过去，以下代码都是在`startup_scripts`进行实现

## 注册创造物品栏
```js
StartupEvents.registry("creative_mode_tab", (event) => {
	// 注册创造物品栏，并给予创造物品栏id
	let tab = event.create("meng:items")
	// 设置创造物品栏的图标,注意这里的物品一定要是存在的
	tab.icon(() => Item.of("meng:hello_item"))
	// 设置创造物品栏的显示名称,这里用的是本地化的键(在物品提示章节有提起过使用)
	tab.displayName = Text.translatable("item_group.meng.items")
	//往物品栏里添加物品
	tab.content(() => [
		"meng:hello_item"
	])
})
```

## 修改创造物品栏
除了可以直接注册创造物品栏，然后将物品添加进去外，我们还可以直接修改创造物品栏
```js
//第一个值填写的是创造物品栏的id(kjs的创造物品栏id为kubejs:tab)
StartupEvents.modifyCreativeTab("meng:items", (event) => {
	// code
});
```
### 修改创造物品栏提供的方法
|              方法名              |         方法作用         |
| :------------------------------: | :----------------------: |
|     removeSearch(Ingredient)     |     匹配相符物品删除     |
|        setIcon(ItemStack)        |   设置创造物品栏的图标   |
| addBefore(ItemStack,ItemStack[]) |    在哪个物品之前添加    |
|    removeDisplay(Ingredient)     |            -             |
| addAfter(ItemStack,ItemStack[])  |    在哪个物品之后添加    |
|    setDisplayName(Component)     | 设置创造物品栏的显示名称 |
|          add(ItemStack)          |  添加物品进穿这个物品栏  |
|        remove(Ingredient)        |  将物品从创造物品栏删除  |

