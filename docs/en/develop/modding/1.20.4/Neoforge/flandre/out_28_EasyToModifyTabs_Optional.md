---
title: 28 便利添加创造模式物品栏【可选】
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 28 便利添加创造模式物品栏【可选】 相关教程
image: ./covers/1e4c4846a507a4bc124776a32a4cd1b13b9b73f0.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 便捷的方式将物品添加到创造模式物品栏[可选]

想必大家已经添加物品然后在手动添加到物品栏已经很烦了，这里给出一种简单的解决方法，当你注册物品的时候，直接添加到物品栏中去。

修改ModdItem类如下

这并没有做什么额外的事情，只是创建一个map，然后写了一个register方法。注册调用register方法，将返回的supplierItem翻入到ITEMS_SUPPLIER中去，然后将supplierItem返回。

```java
public class ModItems {
    public static final List<Supplier<Item>> ITEMS_SUPPLIER = new ArrayList<>();

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, ExampleMod.MODID);

    public static final Supplier<Item> MESSAGE_ITEM = register("message_item", MessageItem::new);


    public static Supplier<Item> register(String name, Supplier<Item> supplier){
        Supplier<Item> supplierItem =  ITEMS.register(name,supplier);
        ITEMS_SUPPLIER.add(supplierItem);
        return supplierItem;
    }

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
```

对于ModBlock类同样需要进行调整。我们修改了registerBlockItem方法不直接使用ITEMS了，而是转用ModItems.register方法，这样就可以将blockitem也添加到ITEMS_SUPPLIER中去了。

```java

    public static void registerBlockItem(String name, Supplier<Block> block, Item.Properties properties){
        ModItems.register(name, () -> new BlockItem(block.get(), properties));
    }
```

最后我们的创造模式物品栏中遍历这个LIST，将对应的ITEM添加到其中。

```java
    public static final Supplier<CreativeModeTab> EXAMPLE_TAB  = CREATIVE_MODE_TABS.register("example_tab",() -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .title(Component.translatable(EXAMPLE_MOD_TAB_STRING))
            .icon(()->ModItems.RUBY.get().getDefaultInstance())
            .displayItems((pParameters, pOutput) -> {
                ModItems.ITEMS_SUPPLIER.forEach(item->pOutput.accept(item.get()));
            })
            .build());
```