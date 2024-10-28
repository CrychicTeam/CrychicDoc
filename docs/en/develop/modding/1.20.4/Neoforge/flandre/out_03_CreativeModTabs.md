---
title: 03 创造模式物品栏
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 03 创造模式物品栏 相关教程
image: ./covers/018535be9026ee118030fe659322cc3091f29ddc.jpg
category: Minecraft1_20_4_NeoForge_Tutorial
draft: false
authors: ['Flandre923']
---
# 自定义创造模式物品栏
现在添加一个创造物品栏和添加一个物品类似，同样你需要创建一个CreativeModeTab注册器，以使用注册器注册一个CreativeModeTab。然后添加的IEventBus总线中。

添加的包路径如下

```java
package net.flandre923.examplemod.item;

public class ModCreativeTab {
// 对应的注册器
   public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ExampleMod.MODID);
// 这个string是鼠标移动到tab上的显示的内容。
   public static final String EXAMPLE_MOD_TAB_STRING = "creativetab.example_tab";
// 添加一个tab，title标题，icon显示图标，displayItem是指tab中添加的内容，这里传入一个lammabd表达式，通过poutput添加物品，这里添加了我们的ruby
   public static final Supplier<CreativeModeTab> EXAMPLE_TAB  = CREATIVE_MODE_TABS.register("example_tab",() -> CreativeModeTab.builder()
           .withTabsBefore(CreativeModeTabs.COMBAT)
           .title(Component.translatable(EXAMPLE_MOD_TAB_STRING))
           .icon(()->ModItems.RUBY.get().getDefaultInstance())
           .displayItems((pParameters, pOutput) -> {
               pOutput.accept(ModItems.RUBY.get());
           })
           .build());

```

## 记得在总线上注册
```java
public static void register(IEventBus eventBus){
       CREATIVE_MODE_TABS.register(eventBus);
   }
}
```

添加多个tab类似上面的步骤

