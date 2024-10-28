---
title: 23 方块实体内置的数据同步
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 23 方块实体内置的数据同步 相关教程
image: ./covers/a8e4dc1a735d0df9414275e16cd2e766e8abb3df.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 参考
本文章参考了：
https://boson.v2mcdev.com/tileentity/datasync.html

## 方块实体数据同步
这次我们来看方块实体的数据同步，这里说的数据同步是指客户端和服务端之间的数据同步，我们主要的游戏逻辑写在了服务端，不过有的时候客户端的一些操作依赖于数据，这时候我们需要将数据在客户端和服务端进行同步。这里我们说一种用于服务端和客户端的同步方法，这种同步的方法是方块实体提供的。

我们需要这样的一个逻辑，我们在服务端执行一些计数的操作，当计数完成之后，我们要给客户端知道我们计数完成了，并播放僵尸的叫声。

我们还是从添加方块开始：

```java


public class ZombieBlock extends BaseEntityBlock {
    public ZombieBlock() {
        super(Properties.ofFullCopy(Blocks.STONE));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ZombieBlockEntity(pPos,pState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? createTickerHelper(pBlockEntityType, ModBlockEntities.ZOMBIE_BLOCK_ENTITY.get(),ZombieBlockEntity::clientTick) : createTickerHelper(pBlockEntityType, ModBlockEntities.ZOMBIE_BLOCK_ENTITY.get(), ZombieBlockEntity::serverTick);
    }

}
```

这里值得说的还是getticker方法

```java

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? createTickerHelper(pBlockEntityType, ModBlockEntities.ZOMBIE_BLOCK_ENTITY.get(),ZombieBlockEntity::clientTick) : createTickerHelper(pBlockEntityType, ModBlockEntities.ZOMBIE_BLOCK_ENTITY.get(), ZombieBlockEntity::serverTick);
    }

}
```
理解和之前一样，判断是否是客户端，如果是客户端返回clientick的方法引用，如果是服务端返回servertick的方法引用。使用creatickerhelper的帮助方法。

我们来看方块实体类

```java

public class ZombieBlockEntity extends BlockEntity {
    private static final  int MAX_TIME = 5 * 20;
    private boolean flag = false;
    private int timer = 0;

    public ZombieBlockEntity( BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ZOMBIE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }
// getUpdatePacket是服务端发送数据包用的方法，
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
//而onDataPacket才是客户端接受数据包的方法。
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(pkt.getTag());
    }
//
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compoundNBT = super.getUpdateTag();
        compoundNBT.putBoolean("flag", flag);
        return compoundNBT;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        flag = tag.getBoolean("flag");
    }
    public static void clientTick(Level pLevel, BlockPos pPos, BlockState pState, ZombieBlockEntity pBlockEntity) {
        if(pLevel.isClientSide && pBlockEntity.flag){
            var player =  pLevel.getNearestPlayer(pPos.getX(),pPos.getY(),pPos.getZ(),10,false);
            pLevel.playSound(player,pPos, SoundEvents.ZOMBIE_AMBIENT, SoundSource.AMBIENT,1f,1f);
            pBlockEntity.flag = false;
        }
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, ZombieBlockEntity pBlockEntity) {
        if(!pLevel.isClientSide){
            if(pBlockEntity.timer >= MAX_TIME){
                pBlockEntity.flag = true;
                pLevel.sendBlockUpdated(pPos,pLevel.getBlockState(pPos),pLevel.getBlockState(pPos),3);
                pBlockEntity.timer = 0;
            }
            pBlockEntity.timer ++;
        }
    }
}

```

我们有这样的几个方法：

```java
// getUpdatePacket是服务端发送数据包用的方法，
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
//而onDataPacket才是客户端接受数据包的方法。
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(pkt.getTag());
    }
//
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compoundNBT = super.getUpdateTag();
        compoundNBT.putBoolean("flag", flag);
        return compoundNBT;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        flag = tag.getBoolean("flag");
    }
```
这几个方法就用于数据同步的。
而其中的getUpdatePacket方法是服务端给客户端发送一个数据包的方法。这个数据包中的数据就是getUpdateTag方法返回的nbt。
onDataPacket方法是客户端处理数据包的方法。他处理的内容就是调用了handleUpdateTag方法，将flag从nbt读出来赋值给了flag。

对于getUpdateTag和handleUpdateTag需要说的一点是这两个方法是区块被载入时候调用的。为了让实体方块能在区块加载时候同步数据你可以实现getUpdateTag和handleUpdateTag方法。

如果你需要在你需要的时候触发数据同步，你需要调用pLevel.sendBlockUpdated(pPos,pLevel.getBlockState(pPos),pLevel.getBlockState(pPos),3);方法，最后一个参数的flag数值可以看之前的教程，有说到。

那么我们在看看这个客户端的tick方法和服务端的tick方法

```java
    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, ZombieBlockEntity pBlockEntity) {
        if(!pLevel.isClientSide){
            if(pBlockEntity.timer >= MAX_TIME){
                pBlockEntity.flag = true;
                pLevel.sendBlockUpdated(pPos,pLevel.getBlockState(pPos),pLevel.getBlockState(pPos),3);
                pBlockEntity.timer = 0;
            }
            pBlockEntity.timer ++;
        }
    }
```
服务端的tick方法的主要作用就是计数，当计数完成时候，修改flag并触发数据同步，清空计数器。

```java
        if(pLevel.isClientSide && pBlockEntity.flag){
            var player =  pLevel.getNearestPlayer(pPos.getX(),pPos.getY(),pPos.getZ(),10,false);
            pLevel.playSound(player,pPos, SoundEvents.ZOMBIE_AMBIENT, SoundSource.AMBIENT,1f,1f);
            pBlockEntity.flag = false;
        }
```

客户端的tick方法就是检查flag标志位有没有被更新，如果被更新了，就播放僵尸的声音，然后重置flag标志。

对方块和方块实体的注册及其相关的内容就不过度赘述了， 可以直接看源码。

源码在Kook


