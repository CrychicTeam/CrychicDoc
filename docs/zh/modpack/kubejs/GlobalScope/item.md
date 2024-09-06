---
layout: doc
title: 物品类(Item)
---

# Item类

**`Item`** 用于进行与物品相关的操作。

## 前置知识

### 物品堆栈(ItemStack)

例如接下来谈到的Item.of(...args)函数会返回一个ItemStack对象，在Minecraft中，玩家手上，箱子，创造模式物品栏中等你所能见到的物品均为ItemStack的实例。

### 方法重载(Overload)

重载是指同一个方法(函数)名可以有多个不同的实现。例如下文提到的Item.of(...args);就有四种。特点如下：

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

### 类型别名（Type Alias）

别名，指的是人或物除了自己本名以外的名字，而类型别名，则是指除了数据类型自身的类型名之外，新赋予的类型名称。\
例：假设苹果树、梨树，现在是我们有的两个对象的类型，你有一个“摘”的动作（函数）可以从“果树”类型对象上获得果实，苹果树与梨树都属于果树，或者说苹果树与梨树都是果树这个类型的子类型，你很容易明白，这个“摘”函数的参数类型写“果树”。\
但“摘”仅适用于果树吗？如果我把梨挂墙上能不能摘呢？很显然是能的，但这与前边定义的函数参数类型不符，“挂起来的梨”很显然不是果树，我们需要将所有可以被执行“摘”这个动作的类型归类到一个类型下，以便于使用类型提示。\
在Typescript中：type 果树_= 果树 | 挂起来的梨; 这样就是一个类型别名声明。在Kubejs使用过程中，会经常看见这样形式的type_类型别名。

### get与set属性

### readonly属性

## 所有函数

### Item.of(args)

**可以返回一个ItemStack实例，共有4个重载方法。**

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

**疑问，为什么Internal.ItemStack_类型的参数却传递了字符串？为什么Internal.CompoundTag_类的参数传递字符串或对象字面量?详情查看前置知识类型别名与下列对应类型别名解析。**

::: details Internal.ItemStack_类型解析

1. 在Vscode中打开你的游戏根目录(就像平时编写kubejs做的那样)，Ctrl+鼠标左键点击Item.of(...args);函数，可以看到这样一行of(in_: Internal.ItemStack_): Internal.ItemStack;
2. Ctrl+鼠标左键点击Internal.ItemStack_来到Internal.ItemStack_类型别名的定义部分。在这里看到Internal.ItemStack_类型别名下都有什么类型。
3. 它非常长，为了阅读方便，这里可以点击Alt+Z开启自动换行，不过请将关注点放在等号右边第二个类型别名Internal.Item_。
4. Ctrl+鼠标左键点击Internal.Item_打开它的类型别名定义，type Item_ = Item | Special.Item;
5. Item指代的是Minecraft的Item类的实例，不过现在将注意力转向Special.Item并Ctrl+鼠标左键点击。
6. 在这里你可以发现，Special.Item是大量的物品id，而Special.Item是Internal.ItemStack_中引用的一个类型，因此你可以使用字符串来传递Internal.ItemStack_类型参数。
7. 对于Internal.ItemStack_类型别名引用的其他类型，也可以使用，不过会不如字符串方便。

> [!NOTE] 总结
Special.Item内每一个字符串，游戏内都有与之对应的物品id。\
Kubejs内部会进行自动类型转换。
:::

::: details Internal.CompoundTag_类型解析

1. 打开Internal.CompoundTag_类型别名定义，它只引用了Internal.CompoundTag类型，也就是说Internal.CompoundTag_与Internal.CompoundTag是等价的，没有像Special.Item这样的方便类型供我们使用。
2. 对象字面量{key： value}或者字符串类型的对象字面量'{key： value}'，会经由Kubejs内部的类型转换成Internal.CompoundTag。
3. 打开Internal.CompoundTag类型声明，可以看到起其构造函数 constructor(arg0: Internal.Map_\<string, Internal.Tag\>); 也就是说它的实例化需要接收一个键值对，这与我们传递的参数具有形式上的相似性，出于经验，认为Kubejs内部进行了类型转换，此处没有再进行深究。
:::

### Item.withNBT(args)

**返回输入的ItemStack，并包含给定的NBT数据。**

> Item.withNBT(in_: Internal.ItemStack_, nbt: Internal.CompoundTag_): Internal.ItemStack;\
表示接收 **`Internal.ItemStack_`** 类型参数in_， **`Internal.CompoundTag_`** 类型参数nbt，返回带有指定NBT数据的ItemStack。

```js
// 一个带有'{test: 6}'NBT的草方块物品
Item.withNBT(Item.of('grass_block'), '{test: 6}');
```

### Item.getTypeToStackMap()

**获取一个包含了物品id和存放其对应物品堆栈的集合的键值对。**

> Item.getTypeToStackMap(): Internal.Map\<ResourceLocation, Internal.Collection\<Internal.ItemStack\>\>;\
无参数，返回一个Map键值对\<物品id，Set集合\>，这个Map是java.util.HashMap类型，这个Set集合是java.util.ImmutableCollections$Set类型，大小size为1，内含1个对应物品id的ItemStack实例。

```js
// 示例
PlayerEvents.chat(event => {
    const { message } = event;
    if (message !== 'test') return;
    Item.getTypeToStackMap().forEach(
    /**
     * 
     * @param {ResourceLocation} key 
     * @param {Internal.Set} value 
     */
        (key, value) => {
            if (key.namespace === 'minecraft') {
                console.log(`键值对值<${key},${value}>`);
                console.log(`键值对类型<${key.getClass()},${value.getClass()}>`);
            }
        }
    );
})
```

**它是一个Map（java.util.HashMap）其中键为物品id（ResourceLocation），值为Set（java.util.ImmutableCollections$Set）。**

**其中Set的size()为1，内含一个对应物品id的物品堆栈(ItemStack)。**



### Item.playerHeadFromSkinHash(args)

### Item.getEmpty()

### Item.getItem(args)

### Item.getVariants(args)

### Item.getList()

### Item.fireworks(args)

### Item.playerHead(args)

**返回玩家头颅ItemStack实例，具有两个方法重载。**

> Item.playerHead(name: string): Internal.ItemStack;\
接收string类型参数name，返回ItemStack。

```js

```

> Item.playerHead(uuid: Internal.UUID_, textureBase64: string): Internal.ItemStack;\
接收Internal.UUID_类型参数uuid，返回ItemStack。

### Item.isItem(args)

**判断传递的参数类型是否为ItemStack，返回布尔值。**

### Item.getId(args)

**从Item实例（并非ItemStack实例）获取其id。**

## 所有属性

### empty

### list

### typeList

### KJS_ARMOR_MODIFIER_UUID_PER_SLOT

### KJS_BASE_ATTACK_SPEED_UUID

### KJS_BASE_ATTACK_DAMAGE_UUID
