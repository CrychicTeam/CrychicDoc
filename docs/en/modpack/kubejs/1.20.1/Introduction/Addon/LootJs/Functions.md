# LootJs

LootJs 是一个`KubeJS`附属模组，它为`KubeJS`对于原版战利品列表修改进行了更方便的操作
`KubeJS`本身自带的修改 Loot 的方法过于繁琐，若要修改关于:

-   方块
-   实体
-   战利品列表

内的 LootTable

推荐使用`LootJs`来实现

:::v-info
该篇教程源自于 Github 的官方 Wiki

适用于 Minecraft 1.19.2/1.20.1
:::

## 简介

战利品 Functions 可以帮助你在战利品掉落之前对其进行修改

如果你曾经修改过原版的战利品表，那么你大概会熟悉到这个概念

### 战利品随机附魔

enchantRandomly()

`enchantRandomly()`会随机给战利品掉落物附上随机的附魔

下面的示例代码中会给掉落的战利品添加一个随机附魔

注意：掉落物必须是装备或者工具，不然无法进行附魔

```js
LootJS.modifiers((event) => {
    event.addBlockLootModifier("minecraft:emerald_block").pool((p) => {
        p.addLoot("minecraft:diamond_axe");
        p.enchantRandomly();
    });
});
```

### 战利品特定等级附魔

enchantWithLevels(NumberProvider)

大致相当于使用附魔台进行附魔，只是指定了随机的等级

例如下列的`enchantWithLevels([2,4])`中填入了一个数组，那么钻石镐将会进行 2 到 4 级之间的随机等级附魔

```js
LootJS.modifiers((event) => {
    event.addBlockLootModifier("minecraft:emerald_block").pool((p) => {
        p.addLoot("minecraft:diamond_pickaxe");
        p.enchantWithLevels([2, 4]);
    });
});
```

### 抢夺附魔奖励

applyLootingBonus(NumberProvider)

下面的示例代码中，根据抢夺附魔随机给战利品增加数量

`NumberProvider`可以指定一个范围，例如`[1, 3]`，那么掉落的战利品会根据每级抢夺附魔将随机增加1到3个额外的战利品掉落

```js
LootJS.modifiers((event) => {
    event.addBlockLootModifier("minecraft:emerald_block").pool((p) => {
        p.addLoot("minecraft:emerald");
        p.applyLootingBonus([1, 3]);
    });
});
```

### 二项分布战利品

applyLootingBonus(NumberProvider)

根据击杀者手持物品的掠夺附魔等级以及概率来设置战利品掉落物数量

`applyBinomialDistributionBonus()`语句的第一个参数是附魔 ID，第二个参数是触发随机掉落概率，第三个参数是最大额外掉落数量

如果手持物品有对应附魔，那么掉落的战利品将会有 1 到 3 个，概率为 20%

```js
LootJS.modifiers((event) => {
    event.addBlockLootModifier("minecraft:emerald_block").pool((p) => {
        p.addLoot("minecraft:emerald");
        p.applyBinomialDistributionBonus("minecraft:fortune", 0.2, 3);
    });
});
```

### 附魔加成战利品

applyOreBonus(enchantment)

`applyOreBonus()`语句传入的参数是附魔 ID，这个方法用于调整物品堆叠大小，基于矿石掉落的加成

根据官方 Wiki 的介绍`bonus = random(enchantmentLevel + 2) + 1`，可以看出额外战利品的数量是随机的，范围从 1 到(附魔等级+3)

例如:

-   如果玩家使用无附魔工具：额外掉落范围为 1-3 个（0+2+1 到 0+2+1）
-   如果玩家使用时运 III 工具：额外掉落范围为 1-6 个（3+2+1 到 3+2+1）

```js
LootJS.modifiers((event) => {
    event.addBlockLootModifier("minecraft:emerald_block").pool((p) => {
        p.addLoot("minecraft:emerald");
        p.applyOreBonus("minecraft:fortune");
    });
});
```

### 乘数战利品

applyBonus(enchantment, multiplier)

`applyBonus()`语句的传参为附魔 ID，乘数，这个方法会根据给定的乘数来调整物品掉落的数量

下面的代码修改了绿宝石块的掉落物，使用 `"minecraft:fortune"` 作为附魔类型，表示这个加成效果模拟了时运附魔，乘数 `3` 意味着每级幸运附魔都会将掉落物数量增加 3 倍

具体示例：

-   假设玩家使用无附魔工具：正常掉落 1 个绿宝石
-   使用时运 I 工具：可能掉落 1 + (1 \* 3) = 4 个绿宝石
-   使用时运 II 工具：可能掉落 1 + (2 \* 3) = 7 个绿宝石
-   使用时运 III 工具：可能掉落 1 + (3 \* 3) = 10 个绿宝石

```js
LootJS.modifiers((event) => {
    event.addBlockLootModifier("minecraft:emerald_block").pool((p) => {
        p.addLoot("minecraft:emerald");
        p.applyBonus("minecraft:fortune", 3);
    });
});
```

### 爆炸损耗战利品

simulateExplosionDecay()

下面的示例代码中，给掉落的 emerald 添加了 50 个，然后使用 `simulateExplosionDecay()` 来模拟爆炸损耗，掉落的 emerald 数量会根据爆炸来进行减少，例如绿宝石块被爆炸破坏，实际掉落的绿宝石数量可能会少于 50 个

```js
LootJS.modifiers((event) => {
    event.addBlockLootModifier("minecraft:emerald_block").pool((p) => {
        p.addLoot(Item.of("minecraft:emerald", 50));
        p.simulateExplosionDecay();
    });
});
```

### 熟食战利品

smeltLoot()

注意：此方法仅针对于可以掉落可使用物品的生物，以及该物品可以被篝火，烟熏炉，熔炉等方块烧制为熟食

下面的示例代码中使用`functions()`方法来应用`ItemFilter`，针对鸡肉使用`smeltLoot()`这个方法，你将看到的是

鸡死亡时，有概率掉落熟鸡肉而不是生的鸡肉

```js
LootJS.modifiers((event) => {
    event
        .addEntityLootModifier("minecraft:chicken")
        .functions(Item.of("chicken"), (f) => {
            f.smeltLoot();
        });
});
```

### 耐久损耗战利品

damage(NumberProvider)

`damage()`方法传入的参数是一个数组，用来指定掉落物耐久损耗的随机范围

下面的示例代码中添加了`minecraft:netherite_sword`来作为掉落物，然后使用`damage()`方法来指定掉落物耐久损耗的随机范围，

掉落的`minecraft:netheritee_sword`会损耗一定的耐久，范围在 30%-90%之间

注意：该方法只适用于携带耐久的物品

```js
LootJS.modifiers((event) => {
    event.addBlockLootModifier("minecraft:emerald_block").pool((p) => {
        p.addLoot("minecraft:netherite_sword");
        p.damage([0.3, 0.9]);
    });
});
```

### 药水战利品

addPotion(potion)

下面的示例代码中，添加了药水掉落，掉落的药水为`poison`，也就是剧毒药水

可惜的是`addPotion()`方法只有一个参数`potion`，因此你只能填入药水的 ID，并且不能设置该药水的等级以及持续的时间

```js
LootJS.modifiers((event) => {
    event.addBlockLootModifier("minecraft:emerald_block").pool((p) => {
        p.addLoot("minecraft:potion");
        p.addPotion("poison");
    });
});
```

### 战利品数量限制

limitCount(NumberProvider, NumberProvider)

`limitCount()`语句传入的是两个数组，第一个是最小范围，第二个是最大范围

下面的示例代码中：

-   [5, 10] 设置最小值范围：掉落的钻石数量最少为 5 到 10 个（具体数值在这个范围内随机）
-   [30, 64] 设置最大值范围：掉落的钻石数量最多为 30 到 64 个（具体数值在这个范围内随机）

```js
LootJS.modifiers((event) => {
    event.addBlockLootModifier("minecraft:emerald_block").pool((p) => {
        p.addLoot("minecraft:diamond");
        p.limitCount([5, 10], [30, 64]);
    });
});
```

### 战利品属性修改

addAttributes(callback)

`addAttributes()`语句的传参为回调函数，回调函数中可以添加多个属性，如果使用的是`simple`，则使用的是默认的装备槽

下面的示例代码中：

-   `generic.max_health`属性，表示最大生命值，值为 1，表示为 1 点
-   `generic.max_health`属性，表示最大生命值，值为 2，表示为 2 点，且有 99%的概率为其随机加成

更多的装备属性可以使用`ProbeJs`进行查看

```js
LootJS.modifiers((event) => {
    event.addBlockLootModifier("minecraft:emerald_block").pool((p) => {
        p.addLoot("minecraft:diamond_sword");
        p.addAttributes((attributes) => {
            attributes.simple("generic.max_health", 1);
            attributes.simple(0.99, "generic.max_health", 2);
        });
    });
});
```

### 战利品说明

addLore(...components)

为战利品添加`Lora`说明，其实质就是一个`tooltip`

```js
LootJS.modifiers((event) => {
    event.addBlockLootModifier("minecraft:emerald_block").pool((p) => {
        p.addLoot("");
        p.addLore(["This is a lore", Text.red("This is a red lore")]);
    });
});
```

### 替换战利品说明

replaceLore(...components)

替换现有战利品的文本说明

```js
LootJS.modifiers((event) => {
    event.addBlockLootModifier("minecraft:emerald_block").pool((p) => {
        p.addLoot("");
        p.replaceLore(Text.of("Some lore text for your item"));
    });
});
```

### 战利品名称设置

setName(component)

可以设置掉落的战利品名称

```js
LootJS.modifiers((event) => {
    event.addBlockLootModifier("minecraft:emerald_block").pool((p) => {
        p.addLoot("minecraft:emerald");
        p.setName(Text.blue("This is a blue name"));
    });
});
```

### 战利品添加 NBT

addNBT(nbt)

为掉落的战利品添加 NBT，具体对应的 NBT 可以去 Minecraft Wiki 进行查看，[Minecraft Wiki](https://minecraft.wiki/w/Tutorials/Command_NBT_tags)

```js
LootJS.modifiers((event) => {
    event.addBlockLootModifier("minecraft:emerald_block").pool((p) => {
        p.addLoot("minecraft:iron_sword");
        p.addNBT({ Damage: 20 });
    });
});
```

### 函数

functions(ItemFilter, callback)

可用于将一个或多个函数应用于特定的`ItemFilter`，由于函数将应用于当前池中的所有项目，因此可以使用 `this` 或 `pools` 来确定它们的范围

```js
LootJS.modifiers((event) => {
    event
        .addEntityLootModifier("minecraft:chicken")
        .functions(ItemFilter.FOOD, (f) => {
            f.smeltLoot();
        });
});
```

### 自定义函数

customFunction(json)

可用于通过 json 添加修改后的函数，在官方的示例代码中使用 `irons_spellbooks:randomize_spell` 并且仅适用于武器。

```js
LootJS.modifiers((event) => {
    event.addLootTableModifier(/.*/).functions(ItemFilter.WEAPON, (f) => {
        f.customFunction({
            function: "irons_spellbooks:randomize_spell",
            quality: {
                min: 0.5,
                max: 1,
            },
        });
    });
});
```
