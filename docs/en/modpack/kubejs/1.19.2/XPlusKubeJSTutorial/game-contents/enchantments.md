# 7.附魔添加

***

## 一、事件监听

通过向`StartupEvents.registry`事件传入不同的参数，其可用于在启动脚本中注册游戏中的新元素，如方块、物品、声音、药效等，在这里，我们传入`"enchantment"`来进行与附魔有关的修改：

```js
StartupEvents.registry("enchantment",event => {
    // 在此进行修改
})
```

## 二、事件方法

| 方法                               | 描述     | 返回值             |
| ---------------------------------- | -------- | ------------------ |
| create(字符串 附魔ID, 字符串 类型) | 注册物品 | EnchantmentBuilder |
| create(字符串 附魔ID)              | 注册物品 | EnchantmentBuilder |

其中"类型"参数默认情况下只支持`"basic"`类型。

## 三、EnchantmentBuilderf

| 方法                                                | 描述                             | 备注                     | 对应属性默认值 |
| --------------------------------------------------- | -------------------------------- | ------------------------ | -------------- |
| category(EnchantmentCategory 附魔类型)              | 设置附魔支持的物品类型           | \[1]                     | "digger"       |
| rarity(Rarity 稀有度)                               | 设置附魔稀有度                   | -                        | Rarity.COMMON  |
| damageProtection(DamageProtectionFunction 伤害保护) | 设置伤害保护                     | 使用方法见下             | null           |
| damageBonus(DamageBonusFunction 伤害加成)           | 设置伤害加成                     | 使用方法见下             | null           |
| maxLevel(整形 等级)                                 | 设置附魔最大等级                 | -                        | 1              |
| minLevel(整形 等级)                                 | 设置附魔最小等级                 | -                        | 1              |
| maxCost(Int2IntFunction 设置花费)                   | 设置最大花费                     | 使用方法见下             | null           |
| minCost(Int2IntFunction 设置花费)                   | 设置最小花费                     | 使用方法见下             | null           |
| postHurt(PostFunction 受伤后事件)                   | 设置受伤后事件                   | 使用方法见下             | null           |
| postAttack(PostFunction 攻击后事件)                 | 设置攻击后事件                   | 使用方法见下             | null           |
| canEnchant(Object2BooleanFunction 物品检查)         | 检查附魔条件                     | 使用方法见下             | null           |
| undiscoverable()                                    | 隐形附魔                         | -                        | -              |
| curse()                                             | 设置附魔类型为诅咒附魔           | -                        | -              |
| untradeable()                                       | 设置附魔无法被村民卖出           | -                        | -              |
| uncommon()                                          | 设置物品稀有度为UNCOMMON         | 效果同rarity方法，下同   | -              |
| rare()                                              | 设置物品稀有度为RARE             | -                        | -              |
| veryRare()                                          | 设置物品稀有度为VERY\_RARE       | -                        | -              |
| vanishable()                                        | 允许将这类附魔附加于可消耗物品上 | 效果同category方法，下同 | -              |
| crossbow()                                          | 允许将这类附魔附加于弩上         | -                        | -              |
| trident()                                           | 允许将这类附魔附加于三叉戟上     | -                        | -              |
| armor()                                             | 允许将这类附魔附加于护甲上       | -                        | -              |
| armorHead()                                         | 允许将这类附魔附加于头盔上       | -                        | -              |
| armorChest()                                        | 允许将这类附魔附加于胸甲上       | -                        | -              |
| armorLegs()                                         | 允许将这类附魔附加于护腿上       | -                        | -              |
| armorFeet()                                         | 允许将这类附魔附加于靴子上       | -                        | -              |
| wearable()                                          | 允许将这类附魔附加于可穿戴物品上 | -                        | -              |
| weapon()                                            | 允许将这类附魔附加于武器上       | -                        | -              |
| bow()                                               | 允许将这类附魔附加于弓箭上       | -                        | -              |
| fishingRod()                                        | 允许将这类附魔附加于钓鱼竿上     | -                        | -              |
| breakable()                                         | 允许将这类附魔附加于可破坏物品上 | -                        | -              |
| .displayName(字符串 显示名称)                       | 设置附魔显示名称                 | -                        | -              |

\[1]

| EnchantmentCategory值 | 描述         |
| --------------------- | ------------ |
| "basic"               | 基础类       |
| "armor\_head"         | 头盔类       |
| "armor\_chest"        | 胸甲类       |
| "armor\_legs"         | 护腿类       |
| "armor\_feet"         | 靴子类       |
| "armor"               | 护甲类       |
| "weapon"              | 武器类       |
| "digger"              | 挖掘工具类   |
| "bow"                 | 弓类         |
| "crossbow"            | 弩类         |
| "fishing\_rod"        | 钓鱼竿类     |
| "trident"             | 三叉戟类     |
| "breakable"           | 可破坏物品类 |
| "vanishable"          | 消耗物品类   |
| "wearable"            | 可穿戴类     |

## 四：示例

```js
StartupEvents.registry("enchantment",event => {

    // 正面附魔：水刃。可应用于武器上。当攻击对象类型为"WATER"时，给予伤害加成（level * 2），如果不是那么降低造成的伤害（level * -1）
    event.create("hydro_enchantment").weapon().maxLevel(5).damageBonus((level,mobType)=>{

        // 检查怪物类型
        if (mobType == "WATER"){

            // 返回加成计算结果
            return level * 2;

        }else{

            // 返回加成计算结果
            return level * -1;

        }
    }).displayName("水刃");

    // 正面附魔：冰冻。可应用于木棍上。当使用附魔冰冻的木棍攻击时，给予受击目标缓慢药水效果。该效果具有冷却。
    event.create("cryo_enchantment").canEnchant((itemStack)=>{
        // 形参为要检查的itemStack
        
        // 设置该附魔只能应用于木棍上
        if (itemStack.id == "minecraft:stick"){

            // 返回检查结果（通过）
            return true;

        };

        // 返回检查结果（未通过）
        return false;

    }).maxLevel(1).untradeable().postAttack((source,target,level)=>{
        /* 
            三个形参分别为：
            LivingEntity 攻击源（附魔持有者）
            Entity 受击目标
            整形 附魔等级
        */

        // 如果受击目标存活，那么给予其缓慢药水效果，并为发出者添加物品冷却。
        if (target.isLiving()){
            
            // 为受击目标添加药水效果
            target.potionEffects.add("minecraft:slowness",3000);

            // 为攻击源添加物品冷却
            source.addItemCooldown(source.getMainHandItem(),3000);
        }
    }).displayName("冰冻");

    // 负面附魔：风化。可应用于盔甲上。当装备者受到伤害时，根据附魔等级给予其漂浮药水效果，并治疗攻击者。
    event.create("anemo_enchantment").armor().maxLevel(5).curse().postHurt((target,attacker,level)=>{
        /* 
            三个形参分别为：
            LivingEntity 受击者（附魔持有者）
            Entity 攻击源
            整形 附魔等级
        */

        // 如果附魔持有者存活，那么根据附魔等级给予其漂浮药水效果。
        if (target.isLiving()){

            // 为附魔持有者添加药水效果
            target.potionEffects.add("minecraft:levitation",2000 * level);
        }

        // 如果攻击者存活，那么根据附魔等级治疗攻击者。
        if (attacker.isLiving()){

            // 治疗攻击者
            attacker.heal(level)
        }
    }).displayName("风化");
})
```