---
title: 32 构建能力
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 32 构建能力 相关教程
image: ./covers/ce183a8df6f70b3784a4c207210a8e46651c8efd.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 参考

https://boson.v2mcdev.com/capability/capabilityfromscratch.html
https://docs.neoforged.net/docs/datastorage/capabilities

## 能力介绍，构建能力

我们先来介绍一下能系统，能力系统是NeoForge增加一套系统，不是原版就有的，能力系统用于处理一些给游戏物品，方块，实体等添加新的功能的内容，例如给一些机器添加处理流体，处理物品，处理能量这样的功能，我们可以说给该方块或者物品添加了这样的能力。

能力系统主要和这样的几个类有关，
- 创建实体的能力的EntityCapability (net.neoforged.neoforge.capabilities)类，
- 创建方块和方块实体能力的BlockCapability (net.neoforged.neoforge.capabilities)类
- 创建物品能力的ItemCapability (net.neoforged.neoforge.capabilities)类
- NeoForg提供的能力的实例的Capabilities类，
- 给方块，物品，实体添加能力的类CapabilityHooks。
- 以及ICapabilityProvider提供能力的接口

对于能力和provider这里直接引用了boson的讲解内容：

> 假设，你是一个投资商，想要投资建一栋商务楼。一般情况下，你就会去找一个建筑方案提供商。如果这个建筑方案提供商拿不出这个建筑方案，你可能就不建了，如果这个建筑方案提供商拿的出建筑方案，你就可以从这个方法中获取一些信息或者直接按照这个方案建筑商务楼。

我们先来看几个用于来创建Capability的类吧。只给出简单的介绍。

BaseCapability 是几个Capability的基类实现Capability的通用代码。
EntityCapability 提供了获得你提供能力的灵活方便的方法。之后我们介绍怎么获得。

我们对其中的几个创建Capbility的方法说一下功能，之后我们会用到。

<T, C/> EntityCapability<T, C/> create(ResourceLocation name, Class<T/> typeClass, Class<C/> contextClass) 创建新的实体功能，或者获取它（如果已存在）。第一个参数是你能力的name，第二参数是你的能力的接口API功能，第三个是附加的上下文。

<T/> EntityCapability<T, Void/> createVoid(ResourceLocation name, Class<T/> typeClass) 使用Void创建Capability，表示不需要上下文功能。

 <T/> EntityCapability<T, @Nullable Direction/> createSided(ResourceLocation name, Class<T/> typeClass)创建具有可为 null 的方向上下文的新实体功能，或者如果已存在则获取它。 侧面通常是访问实体的侧面，如果未知或不是特定侧面，则为 null。使用可为 null 的方向上下文创建新的实体功能，或者如果已存在则获取它。 该端通常是访问实体的端，如果定未知或不是特端，则为 null。

这几个方法在其他的几个BlockCapability，ItemCapability中是一样的。就不介绍了。

BlockCapability 提供对位于世界中的 T 类型对象的灵活访问。查询block和blockentity能力

ItemCapability 提供对项目堆栈中类型 T 的对象的灵活访问。

T 类型就是你的能力的接口API。

下面我们来看NeoForge为我们提供了什么样子的能力，怎么使用上面提到的类创建能力的。

```java
// 这里我们就使用EnergyStorage这个介绍了，其他大同小异。
/**
* Capability 类提供了 NeoForge 定义的能力，供 modder 直接引用。
  * 它包含不同类型功能的内部类，例如EnergyStorage、FluidHandler和ItemHandler。
 */
public final class Capabilities {
/**
      * EnergyStorage内部类定义了与能量存储相关的功能。
      */
    public static final class EnergyStorage {
/**
          * BLOCK 功能代表块的能量存储，支持侧面访问。
          * 它是使用 BlockCapability.createSided() 方法创建的，并使用 IEnergyStorage 接口。
          */
        public static final BlockCapability<IEnergyStorage, @Nullable Direction> BLOCK = BlockCapability.createSided(create("energy"), IEnergyStorage.class);
/**
          * 实体能力代表实体的能量存储，支持侧面访问。
          * 它是使用 EntityCapability.createSided() 方法创建的，并使用 IEnergyStorage 接口。
          */
        public static final EntityCapability<IEnergyStorage, @Nullable Direction> ENTITY = EntityCapability.createSided(create("energy"), IEnergyStorage.class);
/**
          * ITEM能力代表物品的能量存储，没有任何特定的侧面。
          * 它是使用 ItemCapability.createVoid() 方法创建的，并使用 IEnergyStorage 接口。
          */
        public static final ItemCapability<IEnergyStorage, Void> ITEM = ItemCapability.createVoid(create("energy"), IEnergyStorage.class);
    }
// 流体
    public static final class FluidHandler {
    }
// 处理物品的输入输出
    public static final class ItemHandler {

    }
}
```

这里我们看下我们所说的能力的API是什么，我们来看看IEnergyStorage接口

```java
 // 其实可以看到接口就是定义了一组能量处理的行为，也就是能量的行为。
public interface IEnergyStorage {
    int receiveEnergy(int maxReceive, boolean simulate);
    int extractEnergy(int maxExtract, boolean simulate);
    int getEnergyStored();
    int getMaxEnergyStored();
    boolean canExtract();
    boolean canReceive();
}

```

下面我们来看怎么给这些物品添加能力的。主要的行为在CapabilityHooks这个类，我们就调两个介绍，其他自己看或者去文档中看。

```java
// 给当个方块添加能力，这里是给漏斗的方块实体添加物品输入输出的能力。
    // 为漏斗(Hopper)注册自定义的物品处理器
event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityType.HOPPER, (hopper, side) -> {
 // 使用自定义的漏斗包装器,以遵守冷却时间
    return new VanillaHopperItemHandler(hopper);
});

// 给多个方块实体添加处理物品输入输出的能力
// 定义支持面向(sided)访问的原版容器列表
var sidedVanillaContainers = List.of(
    BlockEntityType.BLAST_FURNACE,
    BlockEntityType.BREWING_STAND,
    BlockEntityType.FURNACE,
    BlockEntityType.SMOKER);
 // 为每个支持面向访问的原版容器注册物品处理器
for (var type : sidedVanillaContainers) {
    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type, (sidedContainer, side) -> {
                  // 如果没有指定面向,则使用普通的库存包装器(InvWrapper)
            // 否则,使用支持面向访问的库存包装器(SidedInvWrapper)
        return side == null ? new InvWrapper(sidedContainer) : new SidedInvWrapper(sidedContainer, side);
    });
}
```

其中的InvWrapper和SidedInvWrapper是对ItemHandler能力接口的实现类。其中的这个
```java
(sidedContainer, side) -> {
                  // 如果没有指定面向,则使用普通的库存包装器(InvWrapper)
            // 否则,使用支持面向访问的库存包装器(SidedInvWrapper)
        return side == null ? new InvWrapper(sidedContainer) : new SidedInvWrapper(sidedContainer, side);
    }
```
就是capabilityProvider
```java
// 这是registerBlockEntity方法的方法签名，可以看到第三个参数就是provider，其中是一个方法，所以你可以用lammbd方式返回。
    <T, C, BE extends BlockEntity> void registerBlockEntity(BlockCapability<T, C> capability, BlockEntityType<BE> blockEntityType, ICapabilityProvider<? super BE, C, T> provider) 
```

我想我们已经将Neoforge提供的内容大概解释清楚了，现在我们用这套系统，添加一个自己的能力，这个能力用在方块实体上，并且没有什么特殊的功能，仅仅获得方块实体的能力，调用能力的方法。

我们建立2个方块，2个方块放在一起时候，上面方块获得下面方块我们提供给他的能力，并执行能力提供的功能。

我们定义我们能力的行为的API接口。这是必要的，其他的模组要兼容你的能力，你提供接口是十分方便的。

```java
package net.flandre923.examplemod.capability;

import net.minecraft.core.BlockPos;

public interface ISimpleCapability {
    public void getString(BlockPos pos);
}

```

这是我们接口的实现类。功能也很简单，就是传入pos打印信息。

```java
package net.flandre923.examplemod.capability.impl;

import com.mojang.logging.LogUtils;
import net.flandre923.examplemod.capability.ISimpleCapability;
import net.minecraft.core.BlockPos;
import org.slf4j.Logger;

public class SimpleCapability implements ISimpleCapability {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final String str;
    public SimpleCapability(final String str){
        this.str = str;
    }

    @Override
    public void getString(BlockPos pos) {
        LOGGER.info(pos.toString() + ":::" + str);
    }
}

```

我们给方块实体创建这个能力。方块和方块实体的能力创建都是用的是这个BlockCapability类。参数含义，在之前的介绍时候已经讲解了。

```java
    public static final BlockCapability<ISimpleCapability,Void> SIMPLE_CAPABILITY_HANDLER =
            BlockCapability.createVoid(
                    new ResourceLocation(ExampleMod.MODID,"simple_capability_handler"),
                    ISimpleCapability.class
            );
```

给我们的方块实体添加这样的能力。第三个参数通过provider返回你的能力实现类。

```java

    @Mod.EventBusSubscriber(modid = ExampleMod.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBus{
        @SubscribeEvent
        private static void registerCapabilities(RegisterCapabilitiesEvent event) {
            event.registerBlockEntity(
                    ModCapabilities.SIMPLE_CAPABILITY_HANDLER,
                    ModBlockEntities.DOWN_BLOCK_BLOCK_ENTITY.get(),
                    (myBlockEntity, side) -> new SimpleCapability("hello")
            );
        }
    }
```

好了下面就是一些添加方块和方块实体的操作了。相信大家都会了，这里给出代码，不做讲解了。
Up方块

```java

public class UpBlock extends BaseEntityBlock {
    public UpBlock(){
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new UpBlockBlockEntity(pPos,pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : createTickerHelper(pBlockEntityType, ModBlockEntities.UP_BLOCK_BLOCK_ENTITY.get(), UpBlockBlockEntity::serverTick);
    }
}

```

Up方块实体，这里对tick方法讲解下，

```java

public class UpBlockBlockEntity extends BlockEntity {
    public UpBlockBlockEntity( BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.UP_BLOCK_BLOCK_ENTITY.get(), pPos, pBlockState);
    }
    /**
     * 在服务端进行 UpBlockBlockEntity 的 tick 更新。
     *
     * @param pLevel        方块实体所在的世界
     * @param pPos          方块实体的位置
     * @param pState        方块实体对应的方块状态
     * @param pBlockEntity  UpBlockBlockEntity 实例
     */
    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, UpBlockBlockEntity pBlockEntity) {
          // 检查世界是否存在且是服务端世界
        if (pLevel != null && !pLevel.isClientSide) {
            // 获取方块实体下方的位置
            BlockPos pos = pPos.below();
            // 获取下方位置的方块实体
            BlockEntity blockEntity = pLevel.getBlockEntity(pos);
            // 检查下方方块实体是否为 DownBlockBlockEntity
            if (blockEntity instanceof DownBlockBlockEntity) {
                // 获取 SIMPLE_CAPABILITY_HANDLER 能力
                ISimpleCapability simpleCapability = pLevel.getCapability(ModCapabilities.SIMPLE_CAPABILITY_HANDLER, pos, null);
                // 检查能力是否存在
                if (simpleCapability != null) {
                    // 调用 simpleCapability 的 getString 方法，传入当前方块实体的位置
                    simpleCapability.getString(pBlockEntity.getBlockPos());
                }
            }
        }
    }

}

```

Down方块

```java

public class DownBlock extends BaseEntityBlock {
    public DownBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));
    }


    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new DownBlockBlockEntity(pPos,pState);
    }
}

```
Down方块实体

```java

public class DownBlockBlockEntity extends BlockEntity {
    public DownBlockBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.DOWN_BLOCK_BLOCK_ENTITY.get(), pPos, pBlockState);
    }
}

```

方块实体注册

```java
    public static final Supplier<BlockEntityType<DownBlockBlockEntity>> DOWN_BLOCK_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("down_block_entity", () ->
                    BlockEntityType.Builder.of(DownBlockBlockEntity::new,
                            ModBlocks.DOWN_BLOCK.get()).build(null));
    public static final Supplier<BlockEntityType<UpBlockBlockEntity>> UP_BLOCK_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("up_block_entity", () ->
                    BlockEntityType.Builder.of(UpBlockBlockEntity::new,
                            ModBlocks.UP_BLOCK.get()).build(null));
```

方块注册

```java

    public static final Supplier<Block> DOWN_BLOCK = registerBlock("down_block",DownBlock::new);
    public static final Supplier<Block> UP_BLOCK = registerBlock("up_block",UpBlock::new);
```

好了进入游戏将up方块和down方块上下放在一起，看控制台就应该可以输出内容了.