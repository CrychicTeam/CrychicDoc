---
title: 34 能力提供附加
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 34 能力提供附加 相关教程
image: ./covers/75dbce6787e00ea0465fef1bd8923eab97ce98b9.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 参考
https://boson.v2mcdev.com/capability/attachcapabilityprovider.html

## 能力附加提供器

我们在之前已经讲解了关于Capability的知识，我们也使用了provider不过之前的使用，我们并没有直接处理provider，我们是通过一个lammbd来做的。现在我们要使用这个provider了。

这次的例子是给我们的玩家添加一个新的速度的能力，不过这个能力目前并不以什么实际效果哦，知识为了展示怎么样使用provider，以及怎么给实体添加能力，我们添加一个物品，将玩家的速度的能力等级显示出来。

首先我们来看能力的接口，没什么特别的行为，只有一个方法，要求返回对应的level，这个接口继承了INBTSerializable，代表这个能力需要实现序列化和反序列化的方法。

```java
public interface ISpeedUpCapability extends INBTSerializable<CompoundTag> {
    int getLevel();
}
```

这是能力的实现类，你应该注意到了我们实现了序列化接口用于将能力的level存储起来，不过这里并不能存起来，我们要在provider中调用这里的方法才行。

```java

/**
 SpeedUpCapability类实现了ISpeedUpCapability接口，用于表示加速能力。
 该类包含一个整数类型的级别（level）属性，表示加速能力的级别。
 */
public class SpeedUpCapability implements ISpeedUpCapability {
    private int level;
    /**
     构造函数，用于创建SpeedUpCapability对象。
     @param level 加速能力的级别
     */
    public SpeedUpCapability(int level) {
        this.level = level;
    }

    /**
     获取加速能力的级别。
     @return 加速能力的级别
     */
    @Override
    public int getLevel() {
        return level;
    }

    /**
     将加速能力序列化为NBT（Named Binary Tag）格式的数据。
     @return 包含加速能力级别的CompoundTag对象
     */
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundNBT = new CompoundTag();
        compoundNBT.putInt("level", this.level);
        return compoundNBT;
    }
    /**
     从NBT格式的数据中反序列化加速能力。
     @param nbt 包含加速能力级别的CompoundTag对象
     */
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.level = nbt.getInt("level");
    }

}

```

这里是创建我们的能力，和我们之前讲解的并没有什么区别。

```java
public class ModCapabilities {
    public static final EntityCapability<ISpeedUpCapability,Void> SPEED_CAPABILITY_HANDLER =
            EntityCapability.createVoid(new ResourceLocation(ExampleMod.MODID,"speed_capability_handler"),
                    ISpeedUpCapability.class);
}
```

这里是我们的provider实现类。我们还需要实现INBTSerializable接口，真正数据存储在这里。

```java

public class SpeedUpCapabilityProvider implements ICapabilityProvider<Player,Void,ISpeedUpCapability>, INBTSerializable<CompoundTag> {
    /**
    SpeedUpCapabilityProvider类实现了ICapabilityProvider和INBTSerializable接口，
    用于提供和管理玩家的加速能力。
    */
    private ISpeedUpCapability speedUpCapability;
    /**
    获取玩家的加速能力。
    @param entity 玩家实体
    @param context 上下文（未使用）
    @return 玩家的加速能力，如果不存在则创建一个新的加速能力
    */
    @Nullable
    @Override
    public ISpeedUpCapability getCapability(Player Entity, Void context) {
        return this.getOrCreateCapability();
    }
    /**
    获取或创建玩家的加速能力。
    @return 玩家的加速能力
    */
    @Nonnull
    ISpeedUpCapability getOrCreateCapability() {
        if (speedUpCapability == null) {
            Random random = new Random();
            this.speedUpCapability = new SpeedUpCapability(random.nextInt(99) + 1);
        }
        return this.speedUpCapability;
    }
    /**
    将玩家的加速能力序列化为NBT格式的数据。
    @return 包含玩家加速能力的CompoundTag对象
    */
    @Override
    public CompoundTag serializeNBT() {
        return this.getOrCreateCapability().serializeNBT();
    }
    /**
    从NBT格式的数据中反序列化玩家的加速能力。
    @param nbt 包含玩家加速能力的CompoundTag对象
    */
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.getOrCreateCapability().deserializeNBT(nbt);
    }
}
```

好了我们给玩家注册我们的能力了。和之前类似，我们给玩家提供了Speed能力，provider就是我们的那个SpeedUpCapabilityProvider

```java


    @Mod.EventBusSubscriber(modid = ExampleMod.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBus{

        @SubscribeEvent
        private static void registerCapabilities(RegisterCapabilitiesEvent event) {
            event.registerEntity(ModCapabilities.SPEED_CAPABILITY_HANDLER,
                    EntityType.PLAYER,
                    new SpeedUpCapabilityProvider());
        }
    }

```

下面我们来看怎么从实体上获得能力，以及使用

```java

public class SpeedUpShowItem extends Item {
    public SpeedUpShowItem() {
        super(new Properties());
    }
    // 很简单，从玩家身上获得能力，调用能力的方法，然后给玩家发送消息。
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide && pUsedHand == InteractionHand.MAIN_HAND) {
            ISpeedUpCapability capability = pPlayer.getCapability(ModCapabilities.SPEED_CAPABILITY_HANDLER);
            if(capability!=null){
                int level = capability.getLevel();
                pPlayer.sendSystemMessage(Component.literal("speed level:" + level));
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}

```

好了可以进入游戏拿到物品测试以下了。你也可以退出游戏从进入看看是否正常的level