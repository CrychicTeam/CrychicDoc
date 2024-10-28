---
title: 11 装备
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 11 装备 相关教程
image: ./covers/7798cca5464edd8c7d9dbf16226840800803137c.jpg
category: Minecraft1_20_4_NeoForge_Tutorial
draft: false
authors: ['Flandre923']
---
# 装备

```java


public enum ModArmorMaterial implements ArmorMaterial {

    RUBY("ruby", 33, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 3);
        map.put(ArmorItem.Type.LEGGINGS, 6);
        map.put(ArmorItem.Type.CHESTPLATE, 8);
        map.put(ArmorItem.Type.HELMET, 3);
    }), 10, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0F, 0.0F, () -> Ingredient.of(ModItems.RUBY.get()));

    private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 13);
        map.put(ArmorItem.Type.LEGGINGS, 15);
        map.put(ArmorItem.Type.CHESTPLATE, 16);
        map.put(ArmorItem.Type.HELMET, 11);
    });
    private final String name;
    private final int durabilityMultiplier;
    private final EnumMap<ArmorItem.Type, Integer> protectionFunctionForType;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    private ModArmorMaterial(
            String pName,
            int pDurabilityMultiplier,
            EnumMap<ArmorItem.Type, Integer> pProtectionFunctionForType,
            int pEnchantmentValue,
            SoundEvent pSound,
            float pToughness,
            float pKnockbackResistance,
            Supplier<Ingredient> pRepairIngredient
    ) {
        this.name = pName;
        this.durabilityMultiplier = pDurabilityMultiplier;
        this.protectionFunctionForType = pProtectionFunctionForType;
        this.enchantmentValue = pEnchantmentValue;
        this.sound = pSound;
        this.toughness = pToughness;
        this.knockbackResistance = pKnockbackResistance;
        this.repairIngredient = new LazyLoadedValue<>(pRepairIngredient);
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type pType) {
        return HEALTH_FUNCTION_FOR_TYPE.get(pType) * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type pType) {
        return this.protectionFunctionForType.get(pType);
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.sound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    /**
     * Gets the percentage of knockback resistance provided by armor of the material.
     */
    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

}
```

ModArmorMaterial 是一个枚举类，它实现了 ArmorMaterial 接口，用于表示Minecraft中自定义盔甲材料的属性。每个枚举常量代表一种不同的盔甲材料，具有不同的属性值。

name：盔甲材料的名称。
durabilityMultiplier：耐久度乘数，用于计算每件盔甲的耐久度。
protectionFunctionForType：一个 EnumMap，包含了不同盔甲类型（如靴子、裤子、胸甲、头盔）的防御值。
enchantmentValue：盔甲可以附魔的等级。
sound：装备盔甲时播放的声音事件。
toughness：盔甲的韧性，是对护甲值效果受伤害降低的防护
knockbackResistance：盔甲提供的抗击退效果的百分比。
repairIngredient：一个 LazyLoadedValue 对象，用于存储修理该材料盔甲所需的材料。


注册物品

```java
    public static final Supplier<Item> RUBY_HELMET = ITEMS.register("ruby_helmet", () -> new ArmorItem(ModArmorMaterial.RUBY, ArmorItem.Type.HELMET, (new Item.Properties())));
    public static final Supplier<Item> RUBY_CHESTPLATE = ITEMS.register("ruby_chestplate", () -> new ArmorItem(ModArmorMaterial.RUBY, ArmorItem.Type.CHESTPLATE, (new Item.Properties())));
    public static final Supplier<Item> RUBY_LEGGINGS = ITEMS.register("ruby_leggings", () -> new ArmorItem(ModArmorMaterial.RUBY, ArmorItem.Type.LEGGINGS, (new Item.Properties())));
    public static final Supplier<Item> RUBY_BOOTS = ITEMS.register("ruby_boots", () -> new ArmorItem(ModArmorMaterial.RUBY, ArmorItem.Type.BOOTS, (new Item.Properties())));
 
```

添加到创造模式物品栏

```java

    public static final Supplier<CreativeModeTab> EXAMPLE_TAB  = CREATIVE_MODE_TABS.register("example_tab",() -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .title(Component.translatable(EXAMPLE_MOD_TAB_STRING))
            .icon(()->ModItems.RUBY.get().getDefaultInstance())
            .displayItems((pParameters, pOutput) -> {
                pOutput.accept(ModItems.RUBY.get());
                pOutput.accept(ModItems.MAGIC_INGOT.get());
                pOutput.accept(ModItems.RUBY_APPLE.get());
                pOutput.accept(ModItems.RUBY_SWORD.get());
                pOutput.accept(ModItems.RUBY_PICKAXE.get());
                pOutput.accept(ModItems.RUBY_CHESTPLATE.get());
                pOutput.accept(ModItems.RUBY_HELMET.get());
                pOutput.accept(ModItems.RUBY_LEGGINGS.get());
                pOutput.accept(ModItems.RUBY_BOOTS.get());
                pOutput.accept(ModBlocks.RUBY_BLOCK.get());
                pOutput.accept(ModBlocks.LAMP_BLOCK.get());
            })
            .build());

```

## 添加材质和模型

```java

        this.basicItem(ModItems.RUBY_HELMET.get());
        this.basicItem(ModItems.RUBY_CHESTPLATE.get());
        this.basicItem(ModItems.RUBY_LEGGINGS.get());
        this.basicItem(ModItems.RUBY_BOOTS.get());
```

材质我直接使用了原版的钻石套装改了一下。你可以直接使用自己的材质

## 添加人物穿上盔甲的材质

因为Minecraft将所有的盔甲材质写死在minecraft这个域下，所以我们得创建相对应的目录，创建如下的目录

```
├── assets
│   ├── examplemod
│   └── minecraft
│       └── textures
│           └── models
│               └── armor


```

命名格式为：盔甲材料_layer_1和盔甲材料_layer_2

你之前写的ModArmorMaterial枚举的name 的名称

## ArmorItem类

ArmorItem 类是 Minecraft  中的一个扩展类，它继承自 Item 类并实现了 Equipable 接口。这个类代表一个盔甲类物品，并定义了它的属性和行为。


- type：表示盔甲类型（如头盔、胸甲、裤子、靴子）。
- defense：盔甲的基础防御值。
- toughness：盔甲的韧性，是对护甲值效果受伤害降低的防护
- knockbackResistance：盔甲提供的抗击退效果的百分比。
- material：盔甲的材料，决定了盔甲的耐久度、防御力、韧性、附魔等级等属性。
- defaultModifiers：一个 Multimap 对象，包含了默认的属性修改器，用于增强盔甲的防御力和韧性。

方法

- getType()：返回盔甲类型。
- getEnchantmentValue()：返回盔甲可以附魔的等级。
- getMaterial()：返回盔甲材料。
- isValidRepairItem(ItemStack pToRepair, ItemStack pRepair)：检查盔甲是否可以被修复。
- use(Level pLevel, Player pPlayer, InteractionHand pHand)：处理盔甲的右键点击行为。
- getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot)：获取盔甲的默认属性修改器。
- getDefense()：返回盔甲的基础防御值。
- getToughness()：返回盔甲的韧性。
- getEquipmentSlot()：返回盔甲的装备槽位。
- getEquipSound()：返回装备盔甲时播放的声音事件。