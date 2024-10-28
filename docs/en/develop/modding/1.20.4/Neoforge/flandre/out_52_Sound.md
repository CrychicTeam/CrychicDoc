---
title: 52 声音
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 52 声音 相关教程
image: ./covers/69f2bc24f563756172f2e6256da455eecae40d52.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 参考

https://boson.v2mcdev.com/sounds/intro.html

## 音效

我们这次说怎么给添加音效，还是比较简单的,常规的注册声音的方法，和之前的类似，不不多赘述了。

```java

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, ExampleMod.MODID);

    public static final Supplier<SoundEvent> EXAMPLE_SOUND = register("example_sound", () -> SoundEvent.createFixedRangeEvent(new ResourceLocation(ExampleMod.MODID, "example_sound"),16));

    public static DeferredHolder<SoundEvent,SoundEvent> register(String name, Supplier<SoundEvent> supplier){
        return SOUNDS.register(name, supplier);

    }
    public static void register(IEventBus modBus){
        SOUNDS.register(modBus);
    }
}

```

下面需要创建对应的目录结构和json文件


```java
resources
├── META-INF
│   └── mods.toml
├── assets
│   └── modid
│       ├── blockstates
│       ├── lang
│       ├── models
│       ├── sounds // this 
│       ├── sounds.json // this 
│       └── textures
├── data
└── pack.mcmeta
```
来看看json的写法把。详细的字段的含义可以去看wiki，这里我们说几个有重要的字段。

https://minecraft.fandom.com/zh/wiki/Sounds.json

```json

{
  "example_sound": { // 这里的example_sound和的注册的名称是一致的。
    "category": "voice",
    "subtitle": "example_sound", 
    "sounds": [
      {
        "name": "examplemod:example_sound", // 这里的格式是 modid:声音的路径  位于sounds下面，其中声音文件必须是ogg
        "stream": true
      }
    ]
  }
}
```
声音放置的位置
```java
resources
├── META-INF
│   └── mods.toml
├── assets
│   └── modid
│       ├── blockstates
│       ├── lang
│       ├── models
│       ├── sounds
│       │   └── example_sound.ogg
│       ├── sounds.json
│       └── textures
├── data
└── pack.mcmeta
```

创建一个物品右键勃发这个声音

```java
public class ExampleSoundTestItem extends Item {
    public ExampleSoundTestItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(pLevel.isClientSide){
            pLevel.playSound(pPlayer,pPlayer.blockPosition(), ModSounds.EXAMPLE_SOUND.get(), SoundSource.AMBIENT,10f,1f);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}

```

关于ogg格式，你可以直接搜索MP3转ogg，会有网页工具的。

好了打开游戏试试吧。

