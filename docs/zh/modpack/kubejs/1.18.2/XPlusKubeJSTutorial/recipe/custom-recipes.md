---
authors: ['Wudji']
---

# 2.3 非标准配方修改

***

本节将介绍非标准配方的修改，包括非工作台配方、修改输入物品状态等（如原版中蛋糕的合成方式）

## 例子1：修改机械动力中粉碎轮的合成配方

**注意！KubeJS现已有机械动力的拓展mod，无需使用该方法修改！**

**机械动力的修改教程详见本教程12.3部分**

```
event.custom({
    type: 'create:crushing',//指定合成方式为粉碎轮
    ingredients: [
      Ingredient.of('minecraft:oak_sapling').toJson()//输入内容
    ],
    results: [//这里的 results(包括所有类似的位置的双引号都是可加可不加的)
      Item.of('minecraft:apple').toResultJson(),//100%输出苹果
      Item.of('minecraft:carrot').withChance(0.5).toResultJson()//50%输出苹果
    ],
    processingTime: 100 //所用时间
  })
//若上述配方使用Json格式添加（即原版数据包格式）
{
  "type": "create:crushing",
  "ingredients": [
    {
      "tag": "minecraft:oak_sapling"
    }
  ],
  "results": [
    {
      "item": "minecraft:apple",
      "count": 1
    },
    {
      "item": "minecraft:carrot",
      "chance": 0.5
    }
  ],
  "processingTime": 100
}
```

如果你使用自定义的配方格式，你必须使用类似于原版Json数据包的格式，也就是必须带有`"type": "mod:recipe_id"`！（比如上文中提到的`"type": "create:crushing"`）. 通过这种方式，你可以为使用原版配方系统的任何配方处理器添加配方，即使它不兼容KubeJS. KubeJS提供的简写格式为：

`{item: '物品注册名', count: 数量}` → `Item.of('物品注册名', 数量).toResultJson()` `{item: '物品注册名'} / {tag: '标签名'}` →`Ingredient.of('物品注册名').toJson()` 和 `Ingredient.of('#标签名').toJson()`

~~实际上不就是Json套壳吗~~

下面我们再看一个例子

## 例子2：为Extended Crafting添加配方

[合成拓展 (Extended Crafting) 介绍(mcmod)](https://www.mcmod.cn/class/1602.html)

```
event.custom({
    type: 'extendedcrafting:shaped_table',
    tier: 4,
	pattern: [
    	"XXXXXXXXX",
    	"X       X",
    	"X       X",
	    "X       X",
    	"X       X",
	    "X       X",
    	"X       X",
	    "X       X",
    	"XXXXXXXXX"
  ],
  key: {
  	X: [Ingredient.of('#forge:ingots/gold').toJson()],//标签的使用
  },
  	result: [Ingredient.of('minecraft:apple').toJson()]
  })
//上述配方使用数据包修改：
{
  "type": "extendedcrafting:shaped_table",
  "pattern": [
    "XXXXXXXXX",
    "X       X",
    "X       X",
    "X       X",
    "X       X",
    "X       X",
    "X       X",
    "X       X",
    "XXXXXXXXX"
  ],
  "key": {
    "X": {
      "tag": "forge:ingots/gold"
    }
  },
  "result": {
    "item": "minecraft:apple"
  }
}
```

## 例子3：修改输入物品状态

以下是一些内置的函数

| 功能                        | 函数格式                                                   |
| ------------------------- | ------------------------------------------------------ |
| 为输入物品减去耐久                 | .damageIngredient(`要修改的物品(输入过滤器)`, `减去的耐久值(整形)`)       |
| 替换输入物品(比如桶)               | .replaceIngredient(`要替换的物品(输入过滤器)`, `替换的物品(物品组)`)      |
| 保持输入物品不变                  | .keepIngredient(`要保留的物品(输入过滤器)`)                       |
| 自定义事件(Server StartUp脚本注册) | .customIngredientAction(`要操作的物品(输入过滤器)`, 自定义事件ID(字符串)) |

其中，所谓的输入过滤器可接受如下类型的内容

| 内容          | 示例                                                                  |
| ----------- | ------------------------------------------------------------------- |
| ItemStackJS | 'minecraft:dirt', Item.of('minecraft:diamond\_sword').ignoreNBT() 等 |
| 合成输入索引      | 整形，如0，1，2......                                                     |
| 对象          | {item: 'something', index: 0}                                       |

你可以在整个配方修改脚本的最后加上它们来实现你想要的效果，比如

```
onEvent('recipes', event => {
  	event.shapeless(/*配方脚本*/).damageItem(Item.of('minecraft:diamond_sword').ignoreNBT())
})
```

下面是几个例子

```
onEvent('recipes', event => {
	//用钻石剑切西瓜
  	event.shapeless('9x minecraft:melon_slice', [ //无序合成，合成输出: 9个西瓜片
		Item.of('minecraft:diamond_sword').ignoreNBT(), //输入一个忽略NBT的钻石剑
		'minecraft:minecraft:melon' // 其他输入内容
	]).damageItem(Item.of('minecraft:diamond_sword').ignoreNBT()) // 降低钻石剑耐久1点(必须忽略NBT)
  
    // 使用两个钻石剑合成kubejs:example_block. 合成后索引为1的钻石剑掉一点耐久并保留第二个钻石剑.
	event.shaped('kubejs:example_block', [
		'SD ',
		'D S'
	], {
		S: Item.of('minecraft:diamond_sword').ignoreNBT(),
		D: 'minecraft:dirt'
	}).damageIngredient(0).keepIngredient('minecraft:diamond_sword')//叠加使用多个函数

    // 使用两个钻石剑合成kubejs:example_block. 合成后钻石剑被替换为石剑
	event.shapeless('kubejs:example_block', [
		Item.of('minecraft:diamond_sword').ignoreNBT(),
		'minecraft:stone',
		Item.of('minecraft:diamond_sword').ignoreNBT(),
		'minecraft:stone'
	]).replaceIngredient('minecraft:diamond_sword', 'minecraft:stone_sword')

    // 使用沙子，骨粉，土方块和水瓶合成陶土. 合成后，水瓶被玻璃瓶所替代
	event.shapeless('minecraft:clay', [
		'minecraft:sand',
		'minecraft:bone_meal',
		'minecraft:dirt',
		Item.of('minecraft:potion', {Potion: "minecraft:water"})
	]).replaceIngredient({item: Item.of('minecraft:potion', {Potion: "minecraft:water"})}, 'minecraft:glass_bottle')
})
```
