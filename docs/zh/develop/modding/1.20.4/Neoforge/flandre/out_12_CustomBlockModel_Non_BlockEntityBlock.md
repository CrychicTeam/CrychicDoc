---
title: 12 自定义方块模型和非实体方块
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 12 自定义方块模型和非实体方块 相关教程
image: ./covers/33bba4aee6ecbb1717f43c720138144e1b1de251.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 本文参考了改内容

https://boson.v2mcdev.com/block/nonesoildblock.html


## 自定义方块模型和非实体方块

该代码复制了原版的台阶的代码

```java

public class RubyFrame extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final VoxelShape SHAPE = Block.box(0,0,0,16,18,16);
    public RubyFrame(){
        super(Properties.ofFullCopy(Blocks.STONE).strength(5).noOcclusion());
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(false)));
    }


    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(WATERLOGGED);
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        BlockState blockstate = pContext.getLevel().getBlockState(blockpos);
        if (blockstate.is(this)) {
            return blockstate.setValue(WATERLOGGED, Boolean.valueOf(false));
        } else {
            FluidState fluidstate = pContext.getLevel().getFluidState(blockpos);
            BlockState blockstate1 = this.defaultBlockState()
                    .setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
            return blockstate1;
        }
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        switch(pType) {
            case LAND:
                return false;
            case WATER:
                return pLevel.getFluidState(pPos).is(FluidTags.WATER);
            case AIR:
                return false;
            default:
                return false;
        }
    }



}


```

在Minecraft中，SimpleWaterloggedBlock接口允许一个方块在放置时检查其位置是否有水，并相应地设置其状态。如果一个方块实现了这个接口，它可以决定是否被水覆盖，这为方块的行为增加了额外的维度。

noOcclusion() 方法是从 BlockBehaviour.Properties 中调用的一个方法，当你调用它时，它告诉Minecraft这个方块不会阻挡任何光线的传播。也就是说，这个方块对光线的阻挡效果会被忽略，光线可以穿过这个方块。

SHAPE 是一个 VoxelShape 类型的静态常量，它定义了方块的体积形状（碰撞箱）。

getShape 方法返回一个 VoxelShape 对象，这个对象定义了方块在给定 BlockState 下的形状。这个方法通常用于确定碰撞箱、视锥剔除（frustum culling）以及与方块交互时的形状检测。

getStateForPlacement 方法在玩家尝试放置方块时被调用。它用于确定方块在被放置时的初始状态。这个方法可以根据放置的上下文（如放置位置是否有水）来返回不同的 BlockState。

updateShape 方法在方块周围的环境发生变化时被调用，例如，当相邻的方块被移除或放置时。这个方法允许方块在状态改变时更新其形状或者其他属性。在 RubyFrame 类中，这个方法还检查了 WATERLOGGED 状态，如果方块被设定为水浸状态，它将安排水流体进行刻（tick）处理。

getFluidState 方法返回一个 FluidState 对象，表示方块在给定 BlockState 下的流体状态。如果一个方块是水浸的，这个方法会返回代表水状态的 FluidState。

isPathfindable 方法用于确定生物是否可以将方块视为路径的一部分。它根据不同的路径计算类型（如陆地、水域、空中）来返回不同的结果。在 RubyFrame 类中，这个方法确保了如果方块被水覆盖，那么它对于水域路径来说是可通行的。


## 注册方块

```java
    public static final Supplier<Block> RUBY_FRAME = registerBlock("ruby_frame", RubyFrame::new);

```

## 通过BlockBeanch创建自己的方块模型

Blockbench 是一款开源的3D建模软件，主要用于创建Minecraft和其他类似游戏的模型。它提供了一个用户友好的界面，允许用户通过拖放和调整参数来设计模型，

具体的教程可以去在B战搜索学习，这里仅做简单的介绍。然后可以保存json文件放到指定的目录的位置即可。

## 添加模型和材质

将用BlockBench的做的json文件和png图片放到之前我们说过的位置model和texture下

```java

├─assets
│  ├─examplemod
│  │  ├─blockstates
│  │  ├─models
│  │  │  ├─block
│  │  │  └─item
│  │  └─textures
│  │      ├─block
│  │      └─item

```


## 生成blockstate和物品模型

```java


    @Override
    protected void registerStatesAndModels() {
        this.simpleBlockWithItem(ModBlocks.RUBY_BLOCK.get(),cubeAll(ModBlocks.RUBY_BLOCK.get()));
        this.propertyBlock(ModBlocks.LAMP_BLOCK.get());
        this.addWithHaveModel(ModBlocks.RUBY_FRAME.get(),"ruby_frame");
    }

    public void addWithHaveModel(Block block,String name){
        var model_path = models().getExistingFile(new ResourceLocation(ExampleMod.MODID,name));
        var model = new ConfiguredModel(model_path);
        getVariantBuilder(block).partialState().setModels(model);
        simpleBlockItem(block,model_path);
    }
```