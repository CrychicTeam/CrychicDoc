---
title: 20 添加新的村民
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 20 添加新的村民 相关教程
image: ./covers/03c577cdc4bd410a1bdf32827f6442f89e1ad15e.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
#  添加新的村民职业


POI = POINT_OF_INTEREST 表示村民感兴趣的方块。兴趣点。

poiType代表了Minecraft世界中的一种特定类型的兴趣点，这些兴趣点通常是村民或其他NPC实体进行活动的地方。例如，铁匠铺、图书馆、农田等都是不同类型的兴趣点。

兴趣点类型定义了与该类型相关的方块状态（例如，铁匠铺的兴趣点类型将与铁砧和锻造台的方块状态相关联），以及该兴趣点的最大票数和有效范围。这些信息用于村民的AI，以便他们能够识别和前往这些地点进行工作或其他活动。

Registries.POINT_OF_INTEREST_TYPE是Minecraft中用于存储所有兴趣点类型的注册表。

VillagerProfession定义了村民的职业，每个职业都有其独特的行为和特征。例如，铁匠、农民、图书管理员等都是不同的村民职业。

村民职业决定了村民的穿着、交易行为、以及他们可能工作的兴趣点类型。每个职业都有一个与之相关的兴趣点类型，村民会在这些地方执行与职业相关的活动。

Registries.VILLAGER_PROFESSION是Minecraft中用于存储所有村民职业的注册表。




## 类介绍

### PoiTypes (net.minecraft.world.entity.ai.village.poi)

PoiType记录类（record class）

`public record PoiType(Set<BlockState> matchingStates, int maxTickets, int validRange) { ... }：`构造方法

matchingStates是一个Set<BlockState/>，表示与这个兴趣点类型匹配的方块状态集合。

maxTickets是一个整数，表示了该兴趣点最多的工作人数

validRange是一个整数，表示这个兴趣点的有效范围，


## PoiTypes (net.minecraft.world.entity.ai.village.poi)

原版中用于定义和注册游戏世界中的兴趣点（Point of Interest，简称POI）的类。

你可以在这里找到原版的兴趣点的内容，例如锻造台，堆肥桶等。

### VillagerProfession (net.minecraft.world.entity.npc)

表示村民的职业的类，原版村民职业的注册也在此类下。

`public record VillagerProfession(String name, Predicate<Holder<PoiType>> heldJobSite, Predicate<Holder<PoiType>> acquirableJobSite, ImmutableSet<Item> requestedItems, ImmutableSet<Block> secondaryPoi, @Nullable SoundEvent workSound) { ... }：`

name：村民职业的名称。

heldJobSite：一个Predicate，用于判断村民是否持有特定的兴趣点点类型（PoiType）

acquirableJobSite：一个Predicate，用于判断村民是否可以获取特定的兴趣点类型。

requestedItems - 定义了这个职业的村民可以捡起和使用物品。

secondaryPoi：一个不可变的集合，包含村民职业与之交互的兴趣点。

workSound：一个声音事件，表示村民在工作时的声音。

## 添加一个新的兴趣点

```java


public class ModVillagers {

    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, ExampleMod.MODID);

    public static final Supplier<PoiType> JUMPY_BLOCK_POI = POI_TYPES.register("ruby_block_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.RUBY_BLOCK.get().getStateDefinition().getPossibleStates()),
                    1, 1));


    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
    }

}
```

## 添加一个新的村民的职业


```java


public class ModVillagers {

    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(Registries.VILLAGER_PROFESSION, ExampleMod.MODID);

    public static final Predicate<Holder<PoiType>> IS_RUBY_BLOCK_POI = poiTypeHolder -> poiTypeHolder.value() == JUMPY_BLOCK_POI.get();

    public static final Supplier<VillagerProfession> JUMP_MASTER = VILLAGER_PROFESSIONS.register("ruby_master",
            () -> new VillagerProfession("ruby_master",IS_RUBY_BLOCK_POI,
                    IS_RUBY_BLOCK_POI, ImmutableSet.of(), ImmutableSet.of(),
                    SoundEvents.VILLAGER_WORK_ARMORER));


    public static void register(IEventBus eventBus) {
        VILLAGER_PROFESSIONS.register(eventBus);
    }

}
```

## 将你的兴趣点添加到json文件中

acquirable_job_site这个JSON文件的作用是定义哪些兴趣点（Point of Interest，POI）是村民可以获取并从事工作的地点。

 创建这样的目录

 ```

│  └─minecraft
│      └─tags
│          ├─blocks
│          ├─painting_variant
│          └─point_of_interest_type

```

在└─point_of_interest_type文件夹下创建 acquirable_job_site.json

在json文件中加入如下的内容：

```json
{
  "values": [
    "examplemod:ruby_block_poi"
  ]
}
```

## 给你的村民职业的材质图片

在你的材质文件夹下创建如下目录，将图片放在目录中

```
├─entity
│  └─villager
│      └─profession
│              ruby_master.png
```

图片的内容怎么摆放可以去看原版的图片的内容。

## 怎么给村民添加交易列表

VillagerTrades类表示了村民交易相关的内容，你可以在这里找到原版村民相关的交易的内容。包括了不同的交易的等级可以或的物品等相关的内容。

对于某个特定职业的村民来说，他的交易列表这样的存在的。

Int2ObjectMap<List<VillagerTrades.ItemListing/>>用于管理村民的交易（VillagerTrades），其中每个整数键代表一个特定的交易等级，而与之关联的对象数组则包含了一组交易清单（ItemListing）。

ItemListing是一个接口，用于生成与村民交易相关的MerchantOffer对象。这个接口通常用于自定义村民的交易行为
该接口要求实现如下的方法。
`MerchantOffer getOffer(Entity pTrader, RandomSource pRandom);`
pTrader: 这是一个Entity类型的参数，代表交易的另一方，通常是村民。
pRandom: 这是一个RandomSource类型的参数，用于生成随机数，以确定是否以及如何提供一个交易。

MerchantOffer 它代表了一个商人（如村民）的交易提议。这个类包含了交易的所有必要信息，如交易的输入物品、输出物品、交易次数、经验值奖励等。
`MerchantOffer(ItemStack pBaseCostA, ItemStack pResult, int pMaxUses, int pXp, float pPriceMultiplier) `
- baseCostA: 交易输入物品。
- result: 交易成功后的输出物品。
- maxUses: 交易的最大使用次数。
- pXp: 交易经验
- pPriceMultiplier: 价格乘数，用于调整交易价格。


到此为止，我们就明白了，我们想添加新的交易，其实就是给特定的职业的村民的交易的Int2ObjectMap中，获得对应等级的List<VillagerTrades.ItemListing/>。往这个List中加入一个实现了ItemListing接口的类的实例。该接口要求实现getOffer方法，返回一个MerchantOffer类的实例。



## 给村民添加交易

```java

public class ModEvent {
    @Mod.EventBusSubscriber(modid = ExampleMod.MODID)
    public static class ForgeEvents{

        @SubscribeEvent
        public static void addCustomTrades(VillagerTradesEvent event) {
            if(event.getType() == VillagerProfession.TOOLSMITH) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(ModItems.MAGIC_INGOT.get(), 1);
                int villagerLevel = 1;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 2),
                        stack,10,8,0.02F));
            }
        }
    }
}
```

## 给自己村民添加

```java


public class ModEvent {
    @Mod.EventBusSubscriber(modid = ExampleMod.MODID)
    public static class ForgeEvents{

        @SubscribeEvent
        public static void addCustomTrades(VillagerTradesEvent event) {

            if(event.getType() == ModVillagers.RUBY_MASTER.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(ModItems.RUBY.get(), 15);
                int villagerLevel = 1;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemStack(Items.EMERALD, 5),
                        stack,10,8,0.02F));
            }
        }
    }
}
```