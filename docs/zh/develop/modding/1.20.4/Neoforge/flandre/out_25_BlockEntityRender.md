---
title: 25 方块实体渲染
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 25 方块实体渲染 相关教程
image: ./covers/d2ac8a0065079277912f5e2df2d019b19d62d3d2.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 参考
本文参考了：
https://boson.v2mcdev.com/specialrender/ter.html

## BakedModel烘焙模型

在开始正式写代码之前，我们先要了解如何一个方块是如何渲染出来的
这次我们添加一个方块，利用方块实体渲染一个箱子和一个钻石物品在旁边，这是一个简单演示，当然利用这个你还可以实现更加复杂有趣的效果，就留给你自己探索了。

我们修改之前的frame框架的方块，给他添加一个方块实体。

```java

public class RubyFrame extends BaseEntityBlock implements SimpleWaterloggedBlock {
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

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RubyFrameBlockEntity(pPos,pState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
}
```
其中这两个方块是我们新添加的：

```java

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RubyFrameBlockEntity(pPos,pState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
```

相信大家已经能看懂了，就不介绍了。

我们来看方块实体的代码,这并没有什么内容，很简单。

```java
public class RubyFrameBlockEntity extends BlockEntity {
    public RubyFrameBlockEntity( BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.RUBY_FRAME_BLOCK_ENTITY.get(), pPos, pBlockState);
    }
}
```

重要的是这个RubyFrameBlockEntityRender类。这是一个我们自己定义的类，他就是方块实体渲染器。不过在介绍这个类之前我们先来介绍下这个类中用到的内容。

```java
public class RubyFrameBlockEntityRender implements BlockEntityRenderer<RubyFrameBlockEntity> {
    public RubyFrameBlockEntityRender(BlockEntityRendererProvider.Context pContext){

    }
    @Override
    public void render(RubyFrameBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();
        pPoseStack.translate(1,0,0);
        BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
        BlockState state = Blocks.CHEST.defaultBlockState();
        blockRenderDispatcher.renderSingleBlock(state,pPoseStack,pBuffer,pPackedLight,pPackedOverlay);
        pPoseStack.popPose();

        pPoseStack.pushPose();
        pPoseStack.translate(0,1,0);
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = new ItemStack(Items.DIAMOND);
        BakedModel bakedModel = itemRenderer.getModel(stack,pBlockEntity.getLevel(),null,0);
        itemRenderer.render(stack, ItemDisplayContext.FIXED,true,pPoseStack,pBuffer,pPackedLight,pPackedOverlay,bakedModel);
        pPoseStack.popPose();
    }
}
```

其中该类实现了BlockEntityRenderer接口，该接口是处理客户端渲染实体方块的方法，其中render方法是主要的方法，我们的渲染的逻辑要写在这里。

接下来我们看下pPoseStack类的作用，这里需要你有点计算机图形学的基础，如果你没有的话看看我之前的OpenGL的视频，或者自己去学下相关的内容。

PoseStack类用于管理pose堆栈，其中每个pose由pose本身的4x4矩阵和法线的3x3矩阵组成。
该类提供了几种方法来操作堆栈和pose：
- translate(double pX, double pY, double pZ)）和 translate(float pX, float pY, float pZ)：将堆栈中的最后一个姿势在x、y和z方向上平移给定的量。
- scale(float pX, float pY, float pZ)：在x、y和z方向上按给定的量缩放堆栈中的最后一个pose。
- mulPose（四元数pQuaternion）：将堆栈中的最后一个pose乘以给定的四元数。
- rotateRound（四元数p四元数，浮点pX，浮点pY，浮点pZ）：将堆栈中的最后一个pose围绕点（pX，pY，pZ）旋转给定的四元数。
- pushPose（）：将一个pose推到堆栈上，初始化为堆栈中最后一个pose的副本。
- popPose（）：从堆栈中移除最后一个pose。
- last（）：获取堆栈中的最后一个pose。
- clear（）：检查堆栈是否为null（即，它只包含初始pose）。
- setIdentity（）：将堆栈中的最后一个pose设置为单位矩阵。
- mulPoseMatrix（Matrix4f-pMatrix）：将堆栈中的最后一个pose乘以给定的4x4矩阵。
- Pose类是PoseStack的一个静态嵌套类，它包含姿势矩阵和法线矩阵。它提供了访问这些矩阵的方法。

说明白就是封装的对模型的顶点数据的变换矩阵。

下面我们来看BlockRenderDispatcher，这个我们在上一个教程中也使用过，我们通过他获得了相应的blockstate的model。

类BlockRenderDispatcher，负责在Minecraft中渲染block。它实现ResourceManagerReloadListener接口，表示它侦听资源管理器重载事件。

BlockRenderDispatcher类有几个字段：

- blockModelShaper：用于获取给定块状态的模型的blockModelShaper对象。
- modelRenderer：用于渲染块模型的ModelBlockRenderer对象。
- blockEntityRenderer:用于渲染块实体的BlockEntityWithoutLevelRenderer对象。
- liquidBlockRenderer：用于渲染液体（例如，水、熔岩）的LiquidBlockRender对象。
- random：用于生成随机数的RandomSource对象。
- blockColors:blockColors对象，用于获取块的颜色。

该类提供了几种渲染块的方法：

- renderBreakingTexture（BlockState pState、BlockPos pPos、BlockAndTintGetter pLevel、PoseStack pPoseStac、VertexConsumer pConsumer）：渲染正在断开的块的纹理。
- renderBatched（BlockState pState、BlockPos pPos、BlockAndTintGetter pLevel、PoseStack pPoseStac、VertexConsumer pConsumer、boolean pCheckSides、RandomSource pRandom）：渲染一批block。
- renderLiquid（BlockPos pPos、BlockAndTintGetter pLevel、VertexConsumer pConsumer、BlockState pBlockState、FluidState pFluidState）：渲染液体block。
- renderSingleBlock（BlockState pState、PoseStack pPoseStac、MultiBufferSource pBufferSource、int pPackedLight、int pPackedOverlay）：渲染单个block。

该类还为其字段提供getter方法，以及在重新加载资源管理器时调用的onResourceManagerReload（ResourceManager-pResourceManager）方法。

那么我们再看看ItemRenderer方法

ItemRenderer的类，它是游戏负责渲染项目模型。该类实现了ResourceManagerReloadListener接口，这意味着它在重新加载游戏的资源管理器时侦听事件。

ItemRenderer类有几种用于在不同上下文中渲染项目的方法：
renderModelLists:渲染项目堆栈的四元模型列表。
render：在给定的显示上下文中渲染itemstack（例如，在玩家的手中、GUI插槽中、地面上）。这种方法可以处理各种特殊情况，例如使用自定义模型渲染三叉戟和望远镜，使用闪烁效果渲染魔法物品，以及使用自定义渲染器渲染物品。
getModel：返回itemstack的烘焙模型，同时考虑可能适用的任何模型override。
renderStatic:在静态上下文中渲染项堆栈（例如，在实体的固定项槽中）。
ItemRenderer类还实现了ResourceManagerReloadListener接口，这意味着当游戏的资源被重新加载时，它会得到通知。作为对资源重新加载事件的响应，ItemRenderer重建其项模型缓存。
最后，ItemRenderer类有几个常量和静态字段，它们与使用自定义模型或纹理（如三叉戟和spyglasse）渲染某些项目有关。它还有一个blockEntityRenderer字段，用于在块实体（如箱子、熔炉）作为项目保存时对其进行渲染。

会到我们的render方法中:

```java

        pPoseStack.pushPose();
        pPoseStack.translate(1,0,0);
        BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
        BlockState state = Blocks.CHEST.defaultBlockState();
        blockRenderDispatcher.renderSingleBlock(state,pPoseStack,pBuffer,pPackedLight,pPackedOverlay);
        pPoseStack.popPose();

        pPoseStack.pushPose();
        pPoseStack.translate(0,1,0);
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = new ItemStack(Items.DIAMOND);
        BakedModel bakedModel = itemRenderer.getModel(stack,pBlockEntity.getLevel(),null,0);
        itemRenderer.render(stack, ItemDisplayContext.FIXED,true,pPoseStack,pBuffer,pPackedLight,pPackedOverlay,bakedModel);
        pPoseStack.popPose();
```
对于posestack的使用，我们要先pushPose保存之前的变换的堆栈，防止污染了其他的变化。之后进行你的操作。首先将pose推送到矩阵堆栈上，并将其平移（1,0,0）。然后，它获得方块的渲染器和箱子的默认方块状态，并使用它们使用给定的矩阵堆栈和buffer在当前位置渲染单个block。
接下来，渲染方法将另一个pose推到矩阵堆栈上，并将其平移（0,1,0），然后，它获得item渲染器，并创建一个钻石的Itemstack，获得ItemStack的烘焙模型，并使用它们使用给定的矩阵堆栈和buffer在当前位置渲染itemstack。
最后，渲染方法从矩阵堆栈中弹出姿势，以恢复原始矩阵状态。
这就是我们这这个方法中做的事情，render方法每帧会调用一次，你可以通过一个变量让渲染的位置动起来从而实现一些动画效果，就留着大家自己研究了。

下面我们要给我们的方块实体绑定这个方块实体渲染器，

```java

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onClientEvent(FMLClientSetupEvent event){
        event.enqueueWork(()->{
            BlockEntityRenderers.register(ModBlockEntities.RUBY_FRAME_BLOCK_ENTITY.get(),RubyFrameBlockEntityRender::new);
        });
    }

}

```

记得注册你的方块和方块实体。

进入游戏你可以就可以看到渲染的物品和箱子方块了。