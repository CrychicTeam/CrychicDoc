---
title: 08 食物
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 08 食物 相关教程
image: ./covers/007e7658995cde57ba10c1cec4f27441fb30b0cd.jpg
category: Minecraft1_20_4_NeoForge_Tutorial
draft: false
authors: ['Flandre923']
---
# 食物

```java


public class RubyApple extends Item {
    private static final FoodProperties FOOD_PROPERTIES = new FoodProperties.Builder()
            .saturationMod(10)
            .nutrition(20)
            .effect(()-> new MobEffectInstance(MobEffects.POISON,3*20,1),1)
            .build();
    public RubyApple() {
        super(new Properties().food(FOOD_PROPERTIES));
    }


}

```

FoodProperties 类用于定义食物的这些特性。在 RubyApple 类中，FOOD_PROPERTIES 是一个静态常量，它使用 FoodProperties.Builder 类来构建食物的特性。

FoodProperties.Builder 是一个用于构建 FoodProperties 实例的帮助类。它提供了一系列的方法来设置食物的属性。

这行代码设置了食物的饱和度修改值。饱和度是食物满足饥饿程度的指标。这个值越高，食物就越能减少饥饿条下降的速度。在这里，饱和度被设置为 10。

这行代码设置了食物的营养值。营养值决定了食物能恢复多少饥饿值。在这个例子中，营养值被设置为 20，这意味着吃下一个 RubyApple 可以恢复 20 点饥饿值。

这行代码为食物添加了一个效果。MobEffectInstance 是效果实例类，它用于创建具体的效果对象。这里使用了 lambda 表达式来创建一个 MobEffectInstance，它表示玩家在食用 RubyApple 后会获得中毒效果。

- MobEffects.POISON：这是中毒效果的常量引用。

- 3*20：效果的持续时间，以游戏刻（ticks）为单位。在这个例子中，效果持续 60 秒（3 分钟）。

- 1：效果的水平，或强度。中毒效果的强度通常从 0 开始，但这里设置为 1，这可能是因为某些效果可以有不同的级别。

最后，build() 方法被调用以创建 FoodProperties 对象。这个对象包含了之前设置的所有食物属性。


注册物品


```java
    public static final Supplier<Item> RUBY_APPLE = ITEMS.register("ruby_apple", RubyApple::new);

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
                pOutput.accept(ModBlocks.RUBY_BLOCK.get());
                pOutput.accept(ModBlocks.LAMP_BLOCK.get());
            })
            .build());

```


添加物品模型

```java

    @Override
    protected void registerModels() {
        this.basicItem(ModItems.RUBY.get());
        this.basicItem(ModItems.RUBY_APPLE.get());
        this.magicIngotModel(getResourceLocation(ModItems.MAGIC_INGOT.get()));
    }

```

添加贴图

![alt text](ruby_apple.png)

## FoodProperties 类

FoodProperties 类是 Minecraft  中用于定义食物属性的一个类。它包含了一系列的属性和方法，用于设置食物的营养值、饱和度、是否为肉类、是否可以总是食用、是否为快速食物，以及食物可能产生的效果。

- nutrition：食物提供的营养值，决定了食物能恢复多少饥饿值。
- saturationModifier：食物的饱和度修改值，决定了食物减少饥饿条下降速度的能力。
- isMeat：一个布尔值，表示食物是否为肉类。
- canAlwaysEat：一个布尔值，表示即使玩家已经饱了，是否仍然可以食用该食物。
- fastFood：一个布尔值，表示食物是否可以快速食用（即不需要长时间咀嚼）。
- effects：一个列表，包含食物可能给予玩家的效果及其概


## Builder类

Builder 类
FoodProperties.Builder 是一个内部静态类，用于构建 FoodProperties 实例。它提供了一系列的链式方法来设置食物的属性。


- nutrition(int pNutrition)：设置食物的营养值。
- saturationMod(float pSaturationModifier)：设置食物的饱和度修改值。
- meat()：将食物设置为肉类。
- alwaysEat()：设置玩家总是可以食用该食物。
- fast()：设置食物为快速食用。
- effect(java.util.function.Supplier<MobEffectInstance/> effectIn, float probability)：添加食物可能给予玩家的效果及其概率。
- effect(MobEffectInstance pEffect, float pProbability)：已弃用的方法，用于添加食物可能给予玩家的效果及其概率。
- build()：使用设置的食物属性创建并返回一个 FoodProperties 实例。