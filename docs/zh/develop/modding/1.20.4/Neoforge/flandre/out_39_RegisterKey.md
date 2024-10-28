---
title: 39 注册热键
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 39 注册热键 相关教程
image: ./covers/33bba4aee6ecbb1717f43c720138144e1b1de251.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 注册热键

这次我们来说怎么给我们的mod添加一个按键，按下按键处理按键事件。

不过提前要说明的一点就是按键的注册和触发的所有内容都是在客户端的，所以如果要实现一些逻辑操作，需要发包处理。参考网路的内容。不要在客户端写逻辑，因为这是没有用的，数据并不能同步到服务端。所以还是请注意。

这次我们就讲注册热键和怎么处理热键按下，这里就简单给客户端的玩家发送一个信息，这个信息并不是所有人能看到的。所以是展示用的处理。

我们先来了解几个类，帮助我们理解我们的代码。

首先是KeyMapping类。通过这个 KeyBinding 类，你可以方便地定义和管理模组中的自定义按键绑定。

```java
    // 你可以通过这个构造方法来创建对应的按键，其中参数的含义是：
    // 自定义按键指定一个类别描述 description
    // 通过指定冲突上下文，你可以定义按键在什么情况下可以被触发。 IKeyConflictContext
    // inputType 键盘还是鼠标
    // keyCode 哪一个按键
    // category 按键在那个分类中。
    public KeyMapping(String description, net.neoforged.neoforge.client.settings.IKeyConflictContext keyConflictContext, final InputConstants.Type inputType, final int keyCode, String category) {
         this(description, keyConflictContext, inputType.getOrCreate(keyCode), category);
    }

```
当键盘按下了某个输入的按键时候触发的事件。在客户端的Forge总线上。我们在这里处理按键按下的逻辑

```java
public static class Key extends InputEvent
```

该事件是用于用户注册自定义按键的事件，不可取消，没有结果，在客户端的MOD总线上触发。

```java
public class RegisterKeyMappingsEvent extends Event implements IModBusEvent
```

好了介绍完了我们来看我们的代码把。

```java
//
public class KeyBinding {
    // 我们添加一个我们自己的分类
    public static final String KEY_CATEGORY_EXAMPLE_MOD = "key.category.example_mod";
    // 我们添加一个按键的描述，是可以被语言化文件处理的。
    public static final String KEY_DRINK_WATER = "key.example_mod.drink_water";

    // 使用KeyMapping创建一个我们自己的热键
    // 参数含义已经在上面介绍了。
    public static final KeyMapping DRINKING_KEY = new KeyMapping(KEY_CATEGORY_EXAMPLE_MOD, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_O,KEY_DRINK_WATER);

}
```

注册热键

```java

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ModClientEventHandler {
    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(KeyBinding.DRINKING_KEY);
    }
}

```

添加热键按下的逻辑

```java
@Mod.EventBusSubscriber(modid = ExampleMod.MODID,value = Dist.CLIENT)
public class ForgeClientEventHandler {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        // 如果我们的热键被按下了，那么就给玩家发送一个消息press the key
        // 注意这里是客户端发送的，所以并不是所有玩家都能看到的。
        if(KeyBinding.DRINKING_KEY.consumeClick()){
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Press the key!"));
        }
    }

}

```

到游戏中按下o键看看把