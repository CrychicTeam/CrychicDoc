---
title: 09 武器
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 09 武器 相关教程
image: ./covers/7798cca5464edd8c7d9dbf16226840800803137c.jpg
category: Minecraft1_20_4_NeoForge_Tutorial
draft: false
authors: ['Flandre923']
---
# 武器

```java


public enum ModItemTiers implements Tier {
    RUBY(3,2000,10F,4F,30,() -> Ingredient.of(ModItems.RUBY.get()));

    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    private ModItemTiers(int pLevel, int pUses, float pSpeed, float pDamage, int pEnchantmentValue, Supplier<Ingredient> pRepairIngredient) {
        this.level = pLevel;
        this.uses = pUses;
        this.speed = pSpeed;
        this.damage = pDamage;
        this.enchantmentValue = pEnchantmentValue;
        this.repairIngredient = new LazyLoadedValue<>(pRepairIngredient);
    }


    @Override
    public int getUses() {
        return this.uses;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.damage;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

}

```
ModItemTiers 是一个枚举类，它实现了 Tier 接口，用于表示Minecraft中自定义工具的等级或品质。每个枚举常量代表一种不同的等级，具有不同的属性值。


添加一个物品

```java


public class RubySword extends SwordItem {

    public RubySword(){
        super(ModItemTiers.RUBY,3,-2.4f,new Item.Properties());
    }
}

```

3：这表示 RubySword 的攻击力
-2.4f：这表示 RubySword 的攻击速度。攻击速度是一个负值，越小攻击速度越慢例如斧头是-3.1f

注册

```java
    public static final Supplier<Item> RUBY_SWORD = ITEMS.register("ruby_sword", RubySword::new);

```
添加到创造模式物品栏

```java
                pOutput.accept(ModItems.RUBY_SWORD.get());

```
添加模型文件

```java
    @Override
    protected void registerModels() {
        this.basicItem(ModItems.RUBY.get());
        this.basicItem(ModItems.RUBY_APPLE.get());
        this.basicItem(ModItems.RUBY_SWORD.get());
        this.magicIngotModel(getResourceLocation(ModItems.MAGIC_INGOT.get()));
    }

```

添加贴图

自己找一个贴图即可。（略）

## Tiers枚举

Tiers 是一个枚举类，它实现了 Tier 接口，用于表示Minecraft中工具和装备的等级或品质。每个枚举常量代表一种不同的等级，具有不同的属性值。
属性

- level：表示工具或装备等级的整数值。
- uses：表示工具可以使用的次数。
- speed：表示工具的工作速度。
- damage：表示工具的攻击力加成。
- enchantmentValue：表示工具可以附魔的等级。
- repairIngredient：一个 LazyLoadedValue 对象，用于存储修理该等级工具所需的材料。

- WOOD：等级0，对应木制工具。
- STONE：等级1，对应石制工具。
- IRON：等级2，对应铁制工具。
- DIAMOND：等级3，对应钻石工具。
- GOLD：等级0，对应金制工具。
- NETHERITE：等级4，对应下界合金工具。

## SwordItem 类

SwordItem 类是 Minecraft 中的一个扩展类，它继承自 TieredItem 类并实现了 Vanishable 接口。这个类代表一个剑类物品，并定义了它的属性和行为。

SwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Item.Properties pProperties)：初始化 SwordItem 类。

- getDamage()：返回剑的攻击伤害值。
- canAttackBlock(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer)：检查玩家是否可以在创造模式外攻击方块。
- getDestroySpeed(ItemStack pStack, BlockState pState)：返回剑破坏特定方块的速度。
- hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker)：处理剑伤害敌人的逻辑。
- mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving)：处理使用剑挖掘方块的逻辑。
- isCorrectToolForDrops(BlockState pBlock)：检查剑是否是挖掘给定方块的正确工具。
- getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot)：获取剑的默认属性修改器。
- canPerformAction(ItemStack stack, net.neoforged.neoforge.common.ToolAction toolAction)：检查剑是否能执行给定的工具动作。