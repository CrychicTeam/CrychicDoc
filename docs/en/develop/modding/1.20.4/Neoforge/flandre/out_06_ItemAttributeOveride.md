---
title: 06 物品属性覆盖
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 06 物品属性覆盖 相关教程
image: ./covers/d2ac8a0065079277912f5e2df2d019b19d62d3d2.jpg
category: Minecraft1_20_4_NeoForge_Tutorial
draft: false
authors: ['Flandre923']
---
# 物品属性覆盖

在Minecraft中，物品模型和属性可以通过JSON文件进行自定义。物品属性覆盖是一种机制，允许开发者根据物品的不同状态或条件来改变其模型或属性。例如，原版的弓在不同的拉动状态下会显示不同的模型。

例如原版的弓

bow.json

```json
{ 
    ……省略内容……
    "overrides": [
        {
            "predicate": {
                "pulling": 1
            },
            "model": "item/bow_pulling_0"
        },
        {
            "predicate": {
                "pulling": 1,
                "pull": 0.65
            },
            "model": "item/bow_pulling_1"
        },
        {
            "predicate": {
                "pulling": 1,
                "pull": 0.9
            },
            "model": "item/bow_pulling_2"
        }
    ]
}
```
- overrides: 这是一个数组，用于列出所有的属性覆盖。每个元素都是一个对象，定义了一组条件和一个或多个要应用的属性更改。

- predicate: 这是一个对象，用于定义触发属性覆盖的条件。它包含了一组键值对，每个键都是一个属性名，值是该属性应满足的条件。

- pulling: 这是一个布尔值属性，用于判断弓是否正在被拉动。在上述例子中，pulling: 1 表示弓正在被拉动。

- pull: 这是一个0到1之间的数值属性，表示弓被拉动的程度。值越接近1，表示弓被拉得越满。

- model: 这是一个字符串属性，指定了当满足predicate中定义的条件时，应该使用哪个模型文件。模型文件通常位于assets/<namespace/>/models/<model/>.json。

## 添加item 

```java
public class MagicIngot extends Item {
    public MagicIngot() {
        super(new Properties());
    }



}
```
在上面的Java代码中，我们创建了一个名为MagicIngot的新物品类。

## 注册item

```java

    public static final Supplier<Item> MAGIC_INGOT = ITEMS.register("magic_ingot", MagicIngot::new);

```

## 添加到物品栏

```java
    public static final Supplier<CreativeModeTab> EXAMPLE_TAB  = CREATIVE_MODE_TABS.register("example_tab",() -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .title(Component.translatable(EXAMPLE_MOD_TAB_STRING))
            .icon(()->ModItems.RUBY.get().getDefaultInstance())
            .displayItems((pParameters, pOutput) -> {
                pOutput.accept(ModItems.RUBY.get());
                pOutput.accept(ModItems.MAGIC_INGOT.get());
                pOutput.accept(ModBlocks.RUBY_BLOCK.get());
                pOutput.accept(ModBlocks.LAMP_BLOCK.get());
            })
            .build());
```

## 添加物品属性覆盖

```java

@Mod.EventBusSubscriber(modid = ExampleMod.MODID,bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class PropertyRegistry {

    @SubscribeEvent
    public static void propertyOverrideRegistry(FMLClientSetupEvent event){
        event.enqueueWork(()->{
            ItemProperties.register(ModItems.MAGIC_INGOT.get(),new ResourceLocation(ExampleMod.MODID,"size"),(itemStack,level,livingEntity,num)->{
                return itemStack.getCount();
            });
        });
    }
}

```

在Neoforge开发中，ItemProperties.register 方法用于注册一个自定义的物品属性。这个方法允许开发者根据特定的条件动态地改变物品的属性，这些属性可以用于模型的不同渲染或者其他逻辑。

第三个参数要求传入一个lammbd的表达式，参数itemstack,clientlevel，livingentity，和int数值。

上述代码中，我们为MAGIC_INGOT物品注册了一个名为size的新属性。这个属性的值由一个lambda表达式定义，它简单地返回了itemStack.getCount()，即物品堆叠的数量。这意味着size属性将反映物品堆叠的数量。

这个自定义属性可以在物品的模型文件中引用，并用于根据物品的数量来改变物品的渲染。例如，可以创建不同的模型文件来表示不同数量的物品，然后在模型文件中使用属性覆盖来根据size属性的值选择不同的模型。

## 添加json

这里是堆叠的数量大于等于16时候返回金锭，小于16时候返回铁定

```json

{
  "parent": "item/generated",
  "textures": {
    "layer0": "item/iron_ingot"
  },
  "overrides": [
    {
      "predicate": {
        "examplemod:size": 16
      },
      "model": "item/gold_ingot"
    }
  ]
}

```
## 参考

https://boson.v2mcdev.com/item/item-property-override.html