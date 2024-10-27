---
title: 22  实体方块tick
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 22  实体方块tick 相关教程
image: ./covers/b66cf820118f07ab1cc387a075cd13b5f19e67d4.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 参考
本文章大量参考了：
https://boson.v2mcdev.com/tileentity/itickabletileentity.html

## 实体方块tick

这次我们看一个实体方块中重要的概念。

使用到的类还是上次教程的几个类，我们就不再重复介绍了。

直接直入主题

照旧添加一个新的方块，并且继承BaseEntityBlock类

```java


public class HelloBlock extends BaseEntityBlock {
    public HelloBlock() {
        super(Properties.ofFullCopy(Blocks.STONE));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new HelloBlockEntity(pPos,pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : createTickerHelper(pBlockEntityType, ModBlockEntities.HELLO_BLOCK_ENTITY.get(), HelloBlockEntity::serverTick);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

}

```

其中我们要说的就是这个方法:

```java

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : createTickerHelper(pBlockEntityType, ModBlockEntities.HELLO_BLOCK_ENTITY.get(), HelloBlockEntity::serverTick);
    }
```

该方法于返回一个BlockEntityTicker，BlockEntityTicker是一个函数式接口，即你需要返回一个参数是如下的函数。

```java

@FunctionalInterface
public interface BlockEntityTicker<T extends BlockEntity> {
    void tick(Level pLevel, BlockPos pPos, BlockState pState, T pBlockEntity);
}

```
getTicker方法主要的作用就是返回一个BlockEntityTicker，这个BlockEntityTicker用于处理该方块实体的tick时间更新的回调方法。所以你要在tick中处理你要处理的内容，该内容回调执行，

这里我们通过了一个createTickerHelper的辅助函数返回了对应的这个函数式接口BlockEntityTicker。

这里要说的一点就是这个tick的回调是分客户端和服务端的，一般游戏逻辑应该写在服务端的。所以这里看到我们对于isclientside做了判断，如果是客户端的tick则直接返回null，服务端返回了servertick方法的应用。

```java
        return pLevel.isClientSide ? null : createTickerHelper(pBlockEntityType, ModBlockEntities.HELLO_BLOCK_ENTITY.get(), HelloBlockEntity::serverTick);
```

至于tick方法写了什么内容，我们把它写在了方块实体中，主要是对方块实体的逻辑的处理。而我们这个方块实体的作用是在周围的玩家中距离该方块最近的那个玩家发送hello的消息。

我们来看看方块实体的内容吧。

```java


public class HelloBlockEntity extends BlockEntity  {
    private static final int MAX_TIME = 5 * 20;
    private int timer = 0;
    public HelloBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.HELLO_BLOCK_ENTITY.get(), pPos, pBlockState);
    }
    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, HelloBlockEntity pBlockEntity) {
        if(pLevel!=null && !pLevel.isClientSide){
            if(pBlockEntity.timer == HelloBlockEntity.MAX_TIME){
                Player nearestPlayer = pLevel.getNearestPlayer(pPos.getX(), pPos.getY(), pPos.getZ(), 10, false);
                Component component = Component.literal("hello");
                if(nearestPlayer!=null){
                    nearestPlayer.sendSystemMessage(component);
                }
                pBlockEntity.timer = 0;
            }
            pBlockEntity.timer++;
        }
    }

}

```

这两个字段是我们设置的，其中一个字段是我们的hello输出的间隔时间，游戏中1s是20tick，这里设置的就是5s。

第二个timer是我们用于计时的。

```java
    private static final int MAX_TIME = 5 * 20;
    private int timer = 0;
```

而对于我们的tick方法，会发现是静态的方法，所以我们通过方法中的blckentity的参数获得我们需要的变量。

```java

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, HelloBlockEntity pBlockEntity) {
        if(pLevel!=null && !pLevel.isClientSide){
            if(pBlockEntity.timer == HelloBlockEntity.MAX_TIME){
                Player nearestPlayer = pLevel.getNearestPlayer(pPos.getX(), pPos.getY(), pPos.getZ(), 10, false);
                Component component = Component.literal("hello");
                if(nearestPlayer!=null){
                    nearestPlayer.sendSystemMessage(component);
                }
                pBlockEntity.timer = 0;
            }
            pBlockEntity.timer++;
        }
    }
```

tick方法就是获得timer然后检查是否到达了计时，如果达到了，就获得最近的玩家，返送hello消息。重置计时器。
没有到达计时就计时++;

如果你看了上一个加成的话你会发现timer是没有保存到nbt中的，这意味这你退出游戏在进入是会导致timer重置的。

别忘记了注册你的方块实体和方块，以及添加到创造模式物品栏，材质，模型和翻译，相信大家已经会了怎么进行这些操作就不过度介绍了。

不了解了可以看源码和之前的教程。进入到游戏中你可以就可以看到给你打招呼了。

