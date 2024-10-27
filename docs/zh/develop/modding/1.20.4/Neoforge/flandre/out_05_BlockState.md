---
title: 05 方块状态
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 05 方块状态 相关教程
image: ./covers/f20a4ca39c07b2976273ef86714d297256424048.jpg
category: Minecraft1_20_4_NeoForge_Tutorial
draft: false
---
# 方块状态


```java

package net.flandre923.examplemod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class LampBlock extends Block {
    public static final BooleanProperty LIT = BooleanProperty.create("lit");

    public LampBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.defaultBlockState().setValue(LIT,false));

    }



    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide && pHand == InteractionHand.MAIN_HAND){
            pLevel.setBlock(pPos,pState.cycle(LIT),3);
        }
        return super.use(pState,pLevel,pPos,pPlayer,pHand,pHit);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LIT);
    }
}

```
在这里，我们创建了一个名为LIT的布尔属性，用于表示方块是否被点亮。这个属性将会被添加到方块的BlockState中，用于存储和检查方块是否被点亮的状态。

```java

    public static final BooleanProperty LIT = BooleanProperty.create("lit");

```

这个方法用于定义方块的状态。在这个方法中，我们将LIT属性添加到方块的状态定义中。这样，方块的状态就可以包含LIT属性，用于存储和检查方块是否被点亮的状态。

```java
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LIT);
    }

```

在构造器中，我们首先调用父类的构造器，然后使用registerDefaultState方法设置方块的默认状态。在这里，我们将LIT属性设置为false，表示方块默认是不被点亮的。这样，每次创建方块时，都会使用这个默认状态。

```java

    public LampBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.defaultBlockState().setValue(LIT,false));

    }

```

## 注册方块和物品

```java
    public static final Supplier<Block> LAMP_BLOCK = registerBlock("lamp_block",()->new LampBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(6f).requiresCorrectToolForDrops()
            .lightLevel(state->state.getValue(LampBlock.LIT)?15:0)));

```
## 添加到物品栏

```java
    public static final Supplier<CreativeModeTab> EXAMPLE_TAB  = CREATIVE_MODE_TABS.register("example_tab",() -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .title(Component.translatable(EXAMPLE_MOD_TAB_STRING))
            .icon(()->ModItems.RUBY.get().getDefaultInstance())
            .displayItems((pParameters, pOutput) -> {
                pOutput.accept(ModItems.RUBY.get());
                pOutput.accept(ModItems.MAGIC_INGOT.get());
                pOutput.accept(ModBlocks.RUBY_BLOCK.get());
                pOutput.accept(ModBlocks.LAMP_BLOCK.get());
            })
            .build());

```

## 添加blockstate的json

lamp_block.json

```java
{
  "variants": {
    "lit=false": {
      "model": "examplemod:block/lamp_off"
    },
    "lit=true": {
      "model": "examplemod:block/lamp_on"
    }
  }
}

```

## 添加model

lamp_off.json

```json

{
  "parent": "minecraft:block/cube_all",
  "textures": {
    "all": "examplemod:block/zircon_lamp_off"
  }
}

```

```json

{
  "parent": "minecraft:block/cube_all",
  "textures": {
    "all": "examplemod:block/zircon_lamp_on"
  }
}
```

## 添加贴图

 这里用了两张原版的贴图

![alt text](zircon_lamp_off.png) 

![alt text](zircon_lamp_on.png)


## 更新方块中的 flag 位

```java


    /**
     * Sets a block state into this world.Flags are as follows:
     * 1 will notify neighboring blocks through {@link net.minecraft.world.level.block.state.BlockBehaviour$BlockStateBase#neighborChanged neighborChanged} updates.
     * 2 will send the change to clients.
     * 4 will prevent the block from being re-rendered.
     * 8 will force any re-renders to run on the main thread instead
     * 16 will prevent neighbor reactions (e.g. fences connecting, observers pulsing).
     * 32 will prevent neighbor reactions from spawning drops.
     * 64 will signify the block is being moved.
     * Flags can be OR-ed
     */
    @Override
    public boolean setBlock(BlockPos pPos, BlockState pNewState, int pFlags) {
        return this.setBlock(pPos, pNewState, pFlags, 512);
    }


```
注释中提到的标志（flags）可以通过位运算（OR操作，使用|符号）来组合使用，以便根据需要启用多种行为。以下是各个标志的含义：
1：通知相邻的方块通过neighborChanged更新。这意味着当方块状态改变时，周围的方块将会收到一个通知，可能会触发一些相邻方块的更新逻辑，例如红石电路的变化。
2：将更改发送给客户端。在多人游戏中，这个标志用于确保所有玩家都能看到方块状态的变化。
4：阻止方块重新渲染。如果设置了此标志，方块的视觉表现不会立即更新，这对于性能优化可能很有用。
8：强制任何重新渲染在主线程上运行。这通常用于确保渲染操作在正确的线程上执行，以避免潜在的线程安全问题。
16：阻止相邻方块反应。这意味着方块的更新不会触发相邻方块的任何反应，例如观察者方块的脉冲。
32：阻止相邻方块反应产生掉落物。如果设置了此标志，即使方块因为邻居的更新而被破坏，也不会产生掉落物。
64：表示方块正在被移动。这可能用于处理方块移动的逻辑，例如活塞推动方块。
通过将这些标志组合使用，开发者可以精确控制方块状态变化时的行为，以满足特定的游戏逻辑需求。例如，如果只是想在服务器内部更改方块状态而不需要通知客户端或触发其他更新，可以不设置任何标志。如果需要确保客户端看到变化并允许正常的邻居更新，可以设置2和1标志

## 原版的带状态的方块
RedstoneLampBlock 原版的红石灯方块。
