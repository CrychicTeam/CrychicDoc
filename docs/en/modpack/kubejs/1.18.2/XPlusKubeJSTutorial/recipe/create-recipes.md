# 2.1 新建配方及物品表示

***

## 一、物品的表示方法——IngredientJS和ItemstackJS

KubeJS提供了IngredientJS和ItemstackJS用于表示物品。比如，标签`Ingredient.of("#minecraft:logs")`就是Ingredient，`Item.of('minecraft:iron_ingot')`就是一个Itemstack

其中Ingredient多用于匹配物品。你可以使用`Ingredient.matchAny("条件")`来获得一个包含当前筛选条件的物品组，其中条件可以为物品ID（`minecraft:diamond`），mod注册名（`@tinkersconstruct`），标签（`#minecraft:logs`）

Itemstack主要用于表示准确的物品组，ItemstackJS可使用的属性/函数如下：

| 属性               | 功能               | 返回值             |
| ---------------- | ---------------- | --------------- |
| id               | 返回该物品组的id        | 字符串             |
| tags             | 返回该物品组的tags      | Collection tags |
| count            | 设置/返回该物品组中物品数量   | int             |
| block            | 返回当前物品是否为方块      | 布尔值             |
| nbt              | 返回当前物品组的nbt      | CompoundTag     |
| nbtString        | 返回字符串形式的nbt      | 字符串             |
| name             | 返回当前物品组的名称       | Text            |
| enchantments     | 返回当前物品组的附魔       | MapJS           |
| harvestSpeed     | 返回当前物品组的破坏速度     | 浮点型             |
| itemGroup        | 返回当前物品组在创造物品栏的位置 | 字符串             |
| vanillaPredicate | 获取当前物品谓词(?)      | Predicate       |
| empty            | 返回当前物品组是否为空      | 布尔值             |

| 函数                                                                                 | 功能                          | 返回值              |
| ---------------------------------------------------------------------------------- | --------------------------- | ---------------- |
| hasTag(tag)                                                                        | 判断物品是否有指定tag                | 布尔值              |
| **withCount(整形 数量)**                                                               | **返回一个指定数量的物品组**            | **ItemStackJS**  |
| hasNBT()                                                                           | 判断是否具有NBT                   | 布尔值              |
| removeNBT()                                                                        | 移除物品的NBT                    | void             |
| **withNBT(CompoundTag nbt)**                                                       | **返回具有指定NBT的物品组**           | **ItemStackJS**  |
| **withName(Text 名称)**                                                              | **返回一个具有指定名称的物品组**          | **ItemStackJS**  |
| strongEquals(any)                                                                  | 将当前物品组与给定内容对比(同时比较数量等)      | 布尔值              |
| hasEnchantment(字符串 附魔ID, 整形 等级)                                                    | 判断当前物品组是否有给定的附魔             | 布尔值              |
| **enchant(字符串 附魔ID, 整形 等级)**                                                       | **返回一个添加了指定附魔的物品组**         | **ItemStackJS**  |
| **enchant(MapJS 附魔内容)**                                                            | **返回一个添加了指定附魔的物品组**         | **ItemStackJS**  |
| **ignoreNBT()**                                                                    | **返回忽略的NBT的IngredientJS**   | **IngredientJS** |
| **weakNBT()**                                                                      | **返回部分忽略的NBT的IngredientJS** | **IngredientJS** |
| areItemsEqual(ItemStackJS 对比对象)                                                    | 将当前物品组与给定物品组对比              | 布尔值              |
| isNBTEqual(ItemStackJS 对比对象)                                                       | 将当前物品组与给定物品组的NBT对比          | 布尔值              |
| getHarvestLevel(ToolType 工具类型, nullable PlayerJS 玩家, nullable BlockContainerJS 方块) | 返回其挖掘等级                     | 整形               |
| getHarvestSpeed(nullable BlockContainerJS 方块)                                      | 返回其挖掘速度                     | 浮点型              |

配方修改需要用到的大都是上表加粗的内容，剩余的内容大都等到第15章判断物品状态时才会用到。

估计你看到上面这些东西得有点麻了，不要慌，让我们看几个例子

| 例子                                                                                            | 意义                          |
| --------------------------------------------------------------------------------------------- | --------------------------- |
| Item.of("minecraft:iron\_ingot").withCount(5)                                                 | 5个铁锭                        |
| Item.of("minecraft:iron\_ingot").withCount(5).withName("KubeJS魔改教程")                          | 5个名字为"KubeJS魔改教程"的铁锭        |
| Item.of("minecraft:diamond\_sword").ignoreNBT()                                               | 忽略了NBT的钻石剑(长用于忽略物品耐久、附魔等属性) |
| Item.of("minecraft:enchanted\_book", {StoredEnchantments:\[{lvl:1,id:"minecraft:sweeping"}]}) | 横扫之刃I附魔书(直接添加NBT例子)         |
| Item.of("minecraft:enchanted\_book").enchant("minecraft:sweeping", 1)                         | 横扫之刃I附魔书(使用函数添加NBT例子)       |
| Item.of(/create:.\*/)                                                                         | 所有机械动力物品(正则表达式)             |

显然，上面的方法表示物品非常麻烦。KubeJS提供了一些**简写方法**：

你可以使用命名空间ID直接表示一个物品，如`"minecraft:iron_ingot"`

你可以直接使用标签的字符串表示这一标签下的使用物品，如`"#minecraft:logs"`

你可以在命名空间前加上倍数来表示物品个数，如`"5x minecraft:iron_ingot"`表示五个铁锭

当然，你也可以直接使用正则表达式

你可以使用指令/kubejs hand来快速获取手持物品信息，详见1.1 基础代码格式和常用指令

## 二、配方的添加

**1、有序配方添加**

语句：`event.shaped(输出物品 , 输入物品)`

例子：用8个海绵合成3个石头

```
onEvent('recipes', event => { // 监听recipes事件
        // 主体修改内容
        event.shaped('3x minecraft:stone', [
                'SSS',
                'S S',
                'SSS'
          ], {
                S: 'minecraft:sponge'
          })
})
```

![](https://i1.mcobj.com/imgb/u18prz/20240225\_65db06a132e01.png)

可以看到，KubeJS采用了类似[原版的格式](https://zhangshenxing.gitee.io/vanillamodtutorial/#%E6%9C%89%E5%BA%8F%E5%90%88%E6%88%90)来修改配方，如果你的配方不需要三行的话，直接留空即可

_**注：下文为了表述简洁将省略onEvent部分**_

**2、无序配方添加**

语句：`event.shapeless(输出物品 , 输入物品)`

例子：用1个石头和1个带萤石粉标签的物品合成4个圆石

```
event.shapeless('4x minecraft:cobblestone', ['minecraft:stone', '#forge:dusts/glowstone'])
```

**3、切石机、熔炉、高炉、烟熏炉、锻造台配方的添加**

```
  // 添加切石机配方：用1个minecraft:golden_apple合成4个minecraft:apple
  event.stonecutting('4x minecraft:apple'/* 输出物品 */, 'minecraft:golden_apple'/* 输入物品 */)

  // 添加切石机配方：用1个minecraft:golden_apple合成2个minecraft:carrot
  event.stonecutting('2x minecraft:carrot'/* 输出物品 */, 'minecraft:golden_apple'/* 输入物品 */)

  // 添加熔炉配方：用1个minecraft:golden_apple合成2个minecraft:carrot
  // (event.recipes.minecraft.smelting的缩写)
  event.smelting('2x minecraft:carrot'/* 输出物品 */, 'minecraft:golden_apple'/* 输入物品 */)
  
  // 添加高炉，营火和烟熏炉的配方(与上文类似)
  event.blasting('3x minecraft:apple', 'minecraft:golden_apple')
  
  // 添加锻造台配方，将后2个物品合并成第一个物品 (将minecraft:gold_ingot和minecraft:apple合成为minecraft:golden_apple)
  event.smithing('minecraft:golden_apple', 'minecraft:apple', 'minecraft:gold_ingot')
```

**4、NBT和配方ID**

带NBT的配方修改

```
  event.shaped('minecraft:book', [
    'CCC',
    'WGL',
    'CCC'
  ], {
    C: '#forge:cobblestone',
    L: Item.of('minecraft:enchanted_book', {StoredEnchantments:[{lvl:1,id:"minecraft:sweeping"}]}),
    // 尽管格式是相同的，但是对于附魔来说，你还可以将其简写成如下形式：
    W: Item.of('minecraft:enchanted_book').enchant('minecraft:respiration', 2),
    G: '#forge:glass'
  })
```

mc中所有的配方都有一个随机的ID，但以下配方被指定了一个唯一的静态ID。这个功能对于编写Patchouli手册比较有用

```
event.smelting('minecraft:golden_apple', 'minecraft:carrot').id('wudjimodpack:wudji_first_recipe_id'/* 配方ID */)
```
