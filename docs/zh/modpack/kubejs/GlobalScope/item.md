---
layout: doc
title: Item类
---
## 前言

**`Item`** 用于进行与物品相关的操作。

## 前置知识

::: details **物品堆栈(ItemStack)**
例如接下来谈到的Item.of(...args)函数会返回一个ItemStack对象，在Minecraft中，玩家手上，箱子，创造模式物品栏中等你所能见到的物品均为ItemStack的实例。
:::
::: details **方法重载(Overload)**
重载是指同一个方法(函数)名可以有多个不同的实现。例如下文提到的Item.of(...args);就有四种。

1. 同名但参数不同：重载的方法必须有不同的参数列表。可以通过改变参数的类型、数量或顺序来实现重载。

2. 返回值不影响重载：返回值类型并不参与方法的重载。也就是说，两个方法即使返回值类型不同，如果它们的参数列表完全相同，也会导致编译错误。

3. 可读性提高：通过重载，可以使用相同名称的方法处理不同类型的数据，这样可以提高代码的可读性。

4. 编译时绑定：方法重载是在编译时通过静态类型检查决定调用哪个方法，与运行时绑定的动态多态性相对。

> [!NOTE] 总结
函数名相同但参数(类型，顺序，数量)不同的函数，且与返回值类型无关。\
同名不同参，返回值无关。
::: warning 注意
Kubejs（JavaScript）的方法重载不能这样实现。\
JavaScript（Kubejs）方法重载实现方式请查看(未完成，看到了请催一催)。
:::

## 常用函数

### Item.of(args)

**Item.of(args)共有4个重载方法。**

> Item.of(in_: Internal.ItemStack_): Internal.ItemStack;\
表示接收 **`Internal.ItemStack_`** 类型参数in_，返回一个 **`Internal.ItemStack`** 对象。

```js
// 表示1个铁镐物品堆栈
Item.of('minecraft:iron_pickaxe');
```

> Item.of(in_: Internal.ItemStack_, count: number, nbt: Internal.CompoundTag_): Internal.ItemStack;\
表示接收 **`Internal.ItemStack_`** 参数in_， **`number`** 类型参数count， **`Internal.CompoundTag_`** 类型参数nbt，返回count个具有nbt标签的 **`Internal.ItemStack`** 对象。

```js
// 表示1个消耗100点耐久的铁镐物品堆栈
Item.of('minecraft:iron_pickaxe', 1, '{Damage:100}');
```

> Item.of(in_: Internal.ItemStack_, count: number): Internal.ItemStack;\
表示接收 **`Internal.ItemStack_`** 参数in_， **`number`** 类型参数count，返回count个 **`Internal.ItemStack`** 对象。

```js
// 表示1个铁镐物品堆栈
Item.of('minecraft:iron_pickaxe', 1);
```

> Item.of(in_: Internal.ItemStack_, tag: Internal.CompoundTag_): Internal.ItemStack;\
表示接收 **`Internal.ItemStack_`** 参数in_， **`Internal.CompoundTag_`** 类型参数nbt，返回1个具有nbt标签的 **`Internal.ItemStack`** 对象。

```js
// 表示1个消耗100点耐久的铁镐物品堆栈
Item.of('minecraft:iron_pickaxe', '{Damage:100}');
```

::: details 要求参数不是Internal.ItemStack_类型吗?为什么传入的实际值是字符串呢?

1. 首先，并不是所有字符串都是被当做Internal.ItemStack_的，可以尝试一番，乱打的字符串在/reload后是会报错的，也就是说，是一些特定的字符串被定义为了Internal.ItemStack_，这个问题也就转变成了谁定义了这些特定字符串，为什么这些特定字符串可以当做Internal.ItemStack_传递。
2. 在Vscode打开你的游戏根目录(就像平时编写kubejs做的那样)，Ctrl+鼠标左键点击Item.of(...args);函数，可以看到这样一行of(in_: Internal.ItemStack_): Internal.ItemStack;
3. Ctrl+鼠标左键点击Internal.ItemStack_来到Internal.ItemStack_类型别名的定义部分。
4. 非常长，这里可以点击Alt+Z开启自动换行，不过我们的关注点是等号右边第二个类型别名Internal.Item_。
5. Ctrl+鼠标左键点击Internal.Item_打开它的类型别名定义，type Item_ = Item | Special.Item;
6. Item指代的是Item类的实例，因此我们的目的不是它，将注意力转向Special.Item并Ctrl+鼠标左键点击。
7. 在这里你可以发现，Special.Item是大量的物品id，而Special.Item是Internal.ItemStack_中的一个类型别名，因此你可以使用字符串来传递Internal.ItemStack_类型参数，且当你传递字符串后，kubejs内部会进行类型转换。这意味着你不仅可以使用字符串，你还可以使用Internal.ItemStack_中任何一个类型的参数来传递。

> [!NOTE] 总结
Special.Item内每一个字符串，游戏内都有与之对应的物品id。\
Kubejs内部会进行自动类型转换。
:::

::: details Internal.CompoundTag_类型别名定义中，并没有找到字符串或者对象字面量的类型声明，为什么依然可以被游戏识别为NBT？

1. 这里面依然有Kubejs内部的类型转换。
2. 打开Internal.CompoundTag类型声明，可以看到起其构造函数 constructor(arg0: Internal.Map_\<string, Internal.Tag\>); 也就是说它接收一个键值对，这与我们传递的参数具有形式上的相似性，出于经验，认为Kubejs内部进行了类型转换，此处没有再进行深究。
:::
