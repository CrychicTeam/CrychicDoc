---
title: 21 第一方块实体和数据保存
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 21 第一方块实体和数据保存 相关教程
image: ./covers/ce183a8df6f70b3784a4c207210a8e46651c8efd.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 第一个方块实体和其数据保存

## 本文大量参考该文章内容
https://boson.v2mcdev.com/tileentity/firsttileentity.html

在Minecraft中，方块实体（BlockEntity）是和方块（Block）紧密相关但又有所区别的概念。方块是构成游戏世界的基本单位，而方块实体则用于存储特定类型方块的数据。

**方块实体（BlockEntity）**

方块实体通常用于存储与单个方块相关联的数据，例如容器（如箱子）中的物品。每个方块实体都与游戏世界中的一个特定方块位置相关联。

**方块实体对应的方块**

我们先来看看方块实体对应的方块Block的类，如果一个方块具有方块实体，那么该方块应该继承BaseEntityBlock类，并实现该类的抽象方法。

BaseEntityBlock 是 Minecraft 中的一个抽象类，它继承自 Block 类并实现了 EntityBlock 接口。这个类为那些具有关联方块实体的方块提供了一些基础功能。

1.  构造方法：这个构造方法接受一个 BlockBehaviour.Properties 对象，该对象包含了一些影响方块行为的基本属性，如硬度、爆炸抗性等。
2. codec:这个方法必须由子类实现，并返回一个 MapCodec 对象，该对象用于读取和写入方块状态数据。MapCodec 是一种数据格式，用于在 Minecraft 的数据包中编码和解码数据。
3. getRenderShape:这个方法决定了方块的渲染方式。RenderShape.INVISIBLE 表示方块是隐形的，不会被渲染。子类可以重写这个方法来指定不同的渲染类型。
4. triggerEvent:当方块接收到事件时（例如，红石信号变化），这个方法会被调用。它首先调用父类的同名方法，然后检查是否存在关联的方块实体，如果有，则调用方块实体的 triggerEvent 方法。
5. getMenuProvider这个方法返回与方块关联的菜单提供者（如果有的话）。例如，如果方块是一个容器（如箱子），那么它将返回一个允许玩家打开容器界面的菜单提供者。
6. createTickerHelper：这个静态方法用于创建一个 BlockEntityTicker，它用于在服务器和客户端上以不同的方式更新方块实体。如果服务器和客户端的方块实体类型相同，它将返回一个 BlockEntityTicker，否则返回 null。

下面我们来看方块实体

对于方块实体我们需要继承BlockEntity类，BlockEntity 类是 Minecraft 中所有方块实体的基类。它负责管理与特定方块位置相关联的数据，并在方块被破坏或更新时保存和加载这些数据。

该类方法较多，我们这里说一下我们这次用到的方法，其他的方法大家自己研究下吧。

加载和保存数据（load 和 saveAdditional）:
public void load(CompoundTag pTag) { ... }
protected void saveAdditional(CompoundTag pTag) { ... }
这些方法用于从 NBT 标签加载和保存额外的数据。NBT 是一种用于存储 Minecraft 数据的格式。

更新（setChanged）:
public void setChanged() { ... }
当方块实体的数据发生变化时，调用此方法可以通知世界该方块实体已更改，让游戏知道在关闭的时候要保存调用保存方法。

了解了这些内容之后，我们来添加我们自己的方块和方块实体，我们要实现的是一个能够玩家点击后计数方块。这个计数应该被存储起来，并且退出游戏后读档保持原来的数值。

我们增加一个Block继承BaseEntityBlock

```java

public class RubyCounter extends BaseEntityBlock {
    public RubyCounter() {
        super(Properties.ofFullCopy(Blocks.STONE));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CounterBlockEntity(pPos,pState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide && pHand == InteractionHand.MAIN_HAND){
            var rubyBlockEntity = (CounterBlockEntity) pLevel.getBlockEntity(pPos);
            int counter = rubyBlockEntity.increase();
            pPlayer.sendSystemMessage(Component.literal("counter:" + counter));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }
}

```
构造方法就不说了。我们看下这个public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)方法，这是你继承BaseEntityBlock过后需要实现的一个方法，该方法需要返回一个新的BlockEntity的实例。这里我们返回的是CounterBlockEntity类的实例，这个类是我们对应方块的方块实体，CounterBlockEntity类是我们之后写的。

我们先略过InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) 方法，这里简单提一下该方法，玩家右键交互时候，会给获得方块实体，并将计数器+1，打印给玩家。

public RenderShape getRenderShape(BlockState pState) 我么重写了该方法返回RenderShape.MODEL，指出该方块使用模型渲染。

MapCodec<? extends BaseEntityBlock> codec()方法我们暂时返回null即可，同样是继承BaseEntityBlock后需要重写的方法。

接下来我们添加我们的CounterBlockEntity类，它需要继承BlockEntity类。

```java

public class CounterBlockEntity extends BlockEntity {
    private int counter = 0;
    public CounterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.RUBY_COUNTER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }
    public int increase(){
        counter ++;
        setChanged();
        return counter;
    }
}


```

该类的代码还是比较简单的。我们主要讲一下构造方法和setchanged方法。

```java
    public CounterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.RUBY_COUNTER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }
```

构造方法，要传入的是方块实体的类型type，pos，blockstate，其中pos，和blockstate我们直接传入即可。对于type我们需要注册对应的blockentitytype后，将我们注册的blockentitytype传入。

对于setChanged()方法我们讲过了，这里再重复一遍
更新（setChanged）:
public void setChanged() { ... }
当方块实体的数据发生变化时，调用此方法可以通知世界该方块实体已更改，让游戏知道在关闭的时候要保存调用保存方法。

下面我们说一下的ModBlockEntities.RUBY_COUNTER_BLOCK_ENTITY.get()的type的注册。

创建一个这样的类，用于注册我们的blockentity
```java

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ExampleMod.MODID);

    public static final Supplier<BlockEntityType<CounterBlockEntity>> RUBY_COUNTER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("ruby_counter_block_entity", () ->
                    BlockEntityType.Builder.of(CounterBlockEntity::new,
                            ModBlocks.RUBY_COUNTER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

}

```

其中BlockEntityType<?>中的？是指我们的可以注册各种继承了blockentity的类。

```java
 public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ExampleMod.MODID);
```
这里使用 BLOCK_ENTITIES 注册表来注册一个新的方块实体类型。register 方法接受一个字符串标识符和一个创建方块实体类型的工厂方法。工厂方法返回一个 BlockEntityType.Builder 对象，该对象定义了方块实体类型的构造函数和一个关联的方块。最后，使用 build(null) 方法来创建并返回方块实体类型。

在build里我们传入了一个null，其实这个地方可以填入一个叫做pDataType的实例，这个实例是用来做不同版本之前存档转换的。

```java
public static final Supplier<BlockEntityType<CounterBlockEntity>> RUBY_COUNTER_BLOCK_ENTITY =
        BLOCK_ENTITIES.register("ruby_counter_block_entity", () ->
                BlockEntityType.Builder.of(CounterBlockEntity::new,
                        ModBlocks.RUBY_COUNTER.get()).build(null));
```

现在我们回来看
```java

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide && pHand == InteractionHand.MAIN_HAND){
            var rubyBlockEntity = (CounterBlockEntity) pLevel.getBlockEntity(pPos);
            int counter = rubyBlockEntity.increase();
            pPlayer.sendSystemMessage(Component.translatable("counter:" + counter));
        }
        return InteractionResult.SUCCESS;
    }

    // 对应的json应该这样写。

     "message.neutrino.counter": "计数: %d"

    // 学过C语言的应该不陌生，这里的%d是指counter输出的位置，%d指明了输出的是一个整数。
```
我们看到我们判断了服务端和交互的手是MAIN手，然后获得了对应位置的方块实体，调用了方块实体的方法返回当前计数，并增加计数，然后给玩家发送信息。

如果你在这里想处理语言的国家化问题，请使用这样的内容：

```java
            pPlayer.sendSystemMessage(Component.translatable("modid.message.counter",counter));

    // 对应的json应该这样写。

     "message.neutrino.counter": "计数: %d"

    // 学过C语言的应该不陌生，这里的%d是指counter输出的位置，%d指明了输出的是一个整数。
```


别忘了将BLOCK_ENTITIES注册到IEventBus中。

```java
        ModBlockEntities.register(modEventBus);

```

别忘记了将你的方块添加到创造模式物品栏中，添加对应的材质和模型，我就偷懒了。

启动游戏你就能看到方块了。你右键可以增加计数。

并没结束，如果你退出游戏在进入游戏你会发发现你的计数清零了。我们希望这个数据能保存下来，然后在退出游戏和进入游戏并不会导致数据清零。

我们用到上文提到的。load和saveAdditional方法。

```java

public class CounterBlockEntity extends BlockEntity {
    private int counter = 0;

    public CounterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.RUBY_COUNTER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public int increase(){
        counter ++;
        setChanged();
        return counter;
    }

    @Override
    public void load(CompoundTag pTag) {
        counter = pTag.getInt("counter");
        super.load(pTag);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("counter",counter);
    }
}
```

在 Minecraft 中，NBT（Named Binary Tag）是一种用于存储游戏数据的格式。CompoundTag 是 NBT 的一种类型，用于存储一系列键值对的数据。在方块实体中，NBT 通常用于存储实体的状态和属性，以便在游戏的不同部分（如客户端和服务器之间）传输和保存。

load:当方块实体从 NBT 数据加载时，load 方法会被调用。在这个方法中，我们从 NBT 数据中读取键为 “counter” 的整数值，并将其赋值给实体的 counter 变量。super.load(pTag) 调用父类的 load 方法，允许父类处理其他可能存在的数据。

saveAdditional 当方块实体被保存到 NBT 数据时，saveAdditional 方法会被调用。在这个方法中，我们将实体的 counter 变量的值写入 NBT 数据，以键 “counter” 存储。super.saveAdditional(pTag) 调用父类的 saveAdditional 方法，允许父类保存其他可能存在的数据。

好了，你进入游戏中后在尝试就可以正常保存了。