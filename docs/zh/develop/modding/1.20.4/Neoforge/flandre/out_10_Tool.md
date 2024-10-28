---
title: 10 工具
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 10 工具 相关教程
image: ./covers/d1a82a6a5dcb945c5753e847c86c6e1bccd85f80.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 工具


```java

public class RubyPickaxe extends PickaxeItem {
    public RubyPickaxe() {
        super(ModItemTiers.RUBY,2,-3f,new Properties());
    }
}

```

通过这一行代码，RubyPickaxe 类继承了 PickaxeItem 类的功能，并将其属性设置为与 RUBY 等级相符的值。这使得 RubyPickaxe 成为一个具有特定耐久度、速度和攻击力的镐类物品。


注册物品

```java
    public static final Supplier<Item> RUBY_PICKAXE = ITEMS.register("ruby_pickaxe", RubyPickaxe::new);

```

添加到创造模式物品栏

```java

                pOutput.accept(ModItems.RUBY_PICKAXE.get());
```

添加物品模型

```java
        this.basicItem(ModItems.RUBY_PICKAXE.get());
```

添加贴图

自己网上找了个贴图，故略

## pickaxe类

在 PickaxeItem 类中，构造函数调用 super((float)pAttackDamageModifier, pAttackSpeedModifier, pTier, BlockTags.MINEABLE_WITH_PICKAXE, pProperties); 这一行代码的作用是初始化 PickaxeItem 类，并设置其属性

- (float)pAttackDamageModifier：这转换了 pAttackDamageModifier 参数为浮点数，用于设置镐的攻击伤害。
- pAttackSpeedModifier：这表示镐的攻击速度。攻击速度是一个负值，因为较低的值意味着更快的攻击速度。
- pTier：这表示镐的等级，与 RUBY 常量的等级相同。
- BlockTags.MINEABLE_WITH_PICKAXE：这指定哪些方块可以用镐挖掘。
- pProperties：这包含了一些额外的属性，如可堆叠性等

## DiggerItem类

DiggerItem 类是 Minecraft 中的一个扩展类，它继承自 TieredItem 类并实现了 Vanishable 接口。这个类代表一个挖掘工具类物品，并定义了它的属性和行为。

- blocks：一个 TagKey<Block/>，表示这个工具可以挖掘的方块集合。
- speed：工具的工作速度。
- attackDamageBaseline：工具的基本攻击伤害值。

构造函数

DiggerItem(float pAttackDamageModifier, float pAttackSpeedModifier, Tier pTier, TagKey<Block/> pBlocks, Item.Properties pProperties)：初始化 DiggerItem 类。

- getDestroySpeed(ItemStack pStack, BlockState pState)：返回工具破坏特定方块的速度。
- hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker)：处理工具伤害敌人的逻辑。
- mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving)：处理使用工具挖掘方块的逻辑。
- getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot)：获取工具的默认属性修改器。
- getAttackDamage()：返回工具的基本攻击伤害值。
- isCorrectToolForDrops(BlockState pBlock)：检查工具是否是挖掘给定方块的正确工具。
- isCorrectToolForDrops(ItemStack stack, BlockState state)：一个重写方法，用于检查工具是否是挖掘给定方块的正确工具，考虑了物品堆叠。
