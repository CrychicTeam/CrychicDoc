---
title: 38 HUD
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 38 HUD 相关教程
image: ./covers/b66cf820118f07ab1cc387a075cd13b5f19e67d4.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 参考
https://boson.v2mcdev.com/gui/hud.html

## HUD

我们定义ExampleHud的类，实现了IGuiOverlay接口，用于在游戏中显示HUD，这里使用BOSON的内容，讲解下HUD是什么：

> HUD或者又称为inGameGui，指的是你在游戏中看到的类似于经验条、准星之类的东西。

由于注册时候要求返回一个实现了IGuiOverlay接口的实例，这里使用了单例模式，返回这个实例，或者你可以使用对应的lammbda表达式。

其中重要的就是render方法，该方法绘制对应的内容. 使用GuiGraphics类进行绘制。该类提供了各种绘制内容的方法，在之前的教程中应该已经见到了。

```java

public class ExampleHud implements IGuiOverlay {
     // 定义一个静态常量 hud，用于存储 ExampleHud 的单例对象
    private static final ExampleHud hud = new ExampleHud();
     // 定义一个 ResourceLocation 对象，用于存储 HUD 纹理的路径
    private final ResourceLocation HUD = new ResourceLocation(ExampleMod.MODID, "textures/gui/hud.png");

    public ExampleHud() {
    }

    @Override
    public void render(ExtendedGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
         // 检查玩家主手持有的物品是否为 ModItems.RUBY_APPLE，如果不是则直接返回
        if (minecraft.player.getMainHandItem().getItem()!= ModItems.RUBY_APPLE.get())
            return;
        guiGraphics.setColor(1,1,1,1);
        // 在屏幕中央上方绘制 HUD 纹理，宽高为 32x32 像素
        //参数是含义，x坐标，y坐标，u，v坐标，宽高，uv宽高。
        guiGraphics.blit(HUD,screenWidth/2-16,screenHeight/2-64,0,0,32,32,32,32);
    }
    // 提供一个静态方法用于获取 ExampleHud 的单例对象
    public static ExampleHud getInstance() {
        return hud;
    }
}
```


```java
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ClientEventHandler {
    //// 在 RegisterGuiOverlaysEvent 事件中注册自定义的 HUD 覆盖
    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        // 使用 event.registerAboveAll 方法将 ExampleHud 的实例注册为最高优先级的 HUD 覆盖
        // 第一个参数是一个 ResourceLocation 对象，用于指定 HUD 的唯一标识符
        // 第二个参数是 ExampleHud 的单例对象，通过 ExampleHud.getInstance() 获取
        event.registerAboveAll(new ResourceLocation(ExampleMod.MODID,"example_hud"), ExampleHud.getInstance());
    }

}

```