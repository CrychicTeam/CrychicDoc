---
authors: ['Wudji']
---

# 5 物品注册和属性修改

## 一、物品注册

### 1、事件监听

通过向`StartupEvents.registry`事件传入不同的参数，其可用于在启动脚本中注册游戏中的新元素，如方块、物品、声音、药效等，在这里，我们传入`"item"`来进行与物品有关的修改：

```js
StartupEvents.registry("item",event=>{
    // 脚本
})
```

显然，方块注册脚本应该放于StartupEvents脚本文件夹中。

### 2、事件方法

| 方法                               | 描述     | 返回值      |
| ---------------------------------- | -------- | ----------- |
| create(字符串 方块ID, 字符串 类型) | 注册物品 | ItemBuilder |
| create(字符串 方块ID)              | 注册物品 | ItemBuilder |

其中，“类型”参数支持以下字符串：

| 值            | 描述         | 备注               |
| ------------- | ------------ | ------------------ |
| "basic"       | 基础物品类型 | -                  |
| "sword"       | 剑类物品     | -                  |
| "pickaxe"     | 镐类物品     | -                  |
| "axe"         | 斧类物品     | -                  |
| "shovel"      | 锹类物品     | -                  |
| "hoe"         | 锄类物品     | -                  |
| "helmet"      | 头盔类物品   | 可以放于对应穿戴栏 |
| "chestplate"  | 胸甲类物品   | 可以放于对应穿戴栏 |
| "leggings"    | 护腿类物品   | 可以放于对应穿戴栏 |
| "boots"       | 靴子类物品   | 可以放于对应穿戴栏 |
| "music\_disc" | 唱片类物品   | 可以放于唱片机中   |

### 3、ItemBuilder

| 方法                                                                              | 描述               | 备注                         | 对应属性默认值 |
| --------------------------------------------------------------------------------- | ------------------ | ---------------------------- | -------------- |
| displayName(String 名称)                                                          | 设定物品名称       | -                            | -              |
| fireResistant(布尔值 是否抗火)                                                    | 设置物品是否抗火   | -                            | false          |
| maxDamage(整形 最大耐久值)                                                        | 设置物品耐久度     | 需求对应类型                 | -              |
| unstackable()                                                                     | 设置为无法堆叠     | -                            | false          |
| maxStackSize(整形 最大堆叠数)                                                     | 设置堆叠数量       | -                            | 64             |
| finishUsing(FinishUsingCallback 回调函数)                                         | 设置物品使用完成后 | -                            | -              |
| modifyAttribute(Attribute 属性类型,字符串 标识符, any 属性值, Operation 操作类型) | 修改Attribute      | \[1]                         | -              |
| tooltip(Component tooltip文本)                                                    | 设置物品tooltip    | 形如Component.blue("击退棒") | -              |
| glow(布尔值 v)                                                                    | 设置是否发光       | 这里指附魔光芒               | false          |
| food(Consumer\<FoodBuilder\_>)                                                    | 设置食物物品       | 见章节`6.2 食物`             | -              |
| rarity(Rarity 稀有度)                                                             | 设置物品稀有度     | \[2]                         | "common"       |
| useAnimation(UseAnim 使用动画)                                                    | 设置物品使用动画   | \[3]                         | null           |
| group(String 物品组)                                                              | 设置物品物品组     | -                            | -              |
| containerItem(ResourceLocation 物品)                                              | 设置物品容器       | 类似奶和桶的关系             | -              |
| burnTime(int 时间)                                                                | 设定燃烧时间       | 作为燃料                     | 0              |

\[1] 参数“标识符”应为任意UUID，操作类型可以为`"addition"`，`"multiply_total"`，`"multiply_base"`

\[2] 稀有度可以为`"uncommon"`、`"epic"`、`"rare"`、`"common"`

\[3] 使用动画可以为`"toot_horn"`、`"spear"`、`"eat"`、`"block"`、`"crossbow"`、`"spyglass"`、`"bow"`、 `"drink"`、`"custom"`、`"none"`

### 4、HandheldItemBuilder

借助ProbeJS不难看出，`SwordItemBuilder`、`PickaxeItemBuilder`、`AxeItemBuilder`、`ShovelItemBuilder`和`HoeItemBuilder`均为`HandheldItemBuilder`子类。

`HandheldItemBuilder`支持方法如下：

| 方法                                   | 描述             |
| -------------------------------------- | ---------------- |
| speedBaseline(float 基础挖掘速度)      | 设置基础挖掘速度 |
| speed(float 挖掘速度加成值)            | 每级效果加成     |
| attackDamageBaseline(float 基础伤害值) | 设置基础伤害值   |
| attackDamageBonus(float 伤害加成值)    | 每级效果加成     |
| modifyTier(Consumer)                   | 设置属性         |

MutableToolTier支持以下set方法：

| 方法                                       | 描述           | 备注           |
| ------------------------------------------ | -------------- | -------------- |
| setSpeed(float 挖掘速度加成值)             | 每级效果加成   | -              |
| setEnchantmentValue(int 值)                | 设置附魔值     | 限制附魔属性量 |
| setAttackDamageBonus(float 伤害加成值)     | 每级效果加成   | -              |
| setUses(int 值)                            | 设置使用次数   | -              |
| setLevel(int 值)                           | 设置等级       | -              |
| setRepairIngredient(Ingredient 修复用物品) | 设置修复用物品 | -              |

值得注意的是：若不进行设置，这些属性的默认值均为铁质工具。

### 5、ArmorItemBuilder

`ArmorItemBuilder`用于设置盔甲类型的物品

| 方法                          | 描述         |
| ----------------------------- | ------------ |
| tier(ArmorMaterial 等级)      | 设置盔甲等级 |
| modifyTier(Consumer 回调函数) | 设置属性     |

MutableArmorTier支持以下set方法：

| 方法                                       | 描述             | 备注                                    |
| ------------------------------------------ | ---------------- | --------------------------------------- |
| setDurabilityMultiplier(int 耐久倍率)      | 设置耐久损坏倍率 | -                                       |
| setToughness(float 抗性)                   | 设置伤害抗性     | -                                       |
| setKnockbackResistance(float 抗性)         | 设置击退抗性     | -                                       |
| setEnchantmentValue(int 值)                | 设置附魔值       | 限制附魔属性量                          |
| setEquipSound(SoundEvent 声音)             | 设置穿戴声音     | 形如`"ambient.basalt_deltas.additions"` |
| setRepairIngredient(Ingredient 修复用物品) | 设置修复用物品   | -                                       |
| setName(String 名称)                       | 设置名称         | -                                       |

值得注意的是：若不进行设置，这些属性的默认值均为铁质盔甲。

### 6、资源目录

对于物品注册来说，你需要将物品材质置于`assets\kubejs\textures\item\<方块ID>.png`，自定义模型（可选）放于`kubejs\assets\kubejs\models\item\<方块ID>.json`下。（即文件结构同资源包）

### 7、示例

```js
StartupEvents.registry("item",event =>{
    event.create("attack_base3","sword").attackDamageBaseline(5);

    event.create("ugly_helmet","helmet").fireResistant(true).modifyTier(tier => {
        tier.setRepairIngredient(Ingredient.of("#forge:ingots/netherite"))
    })

    event.create("wudji_pickaxe","pickaxe").burnTime(100).glow(true).displayName("奇怪的镐子")
})
```