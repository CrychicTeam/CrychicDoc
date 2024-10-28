---
title: 26 物品特殊渲染
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 26 物品特殊渲染 相关教程
image: ./covers/dcdf7f7ee8c35900ba6ffaf9bfcb8bf7d0c2f339.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 参考
https://boson.v2mcdev.com/specialrender/ister.html

## 物品特殊渲染

你在模组中经常会看到一些具有动画的物品，我们这次就介绍下如何实现一个简单的具有动画效果的物品。使用的是BlockEntityWithoutLevelRenderer这样的一个类。

首先还是添加一个物品

```java
public class WrenchItem extends Item {
    public WrenchItem() {
        super(new Properties());
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new WrenchItemRenderer();
            }
        });
    }
}
```

这个代码中你可能会有一部分不知道怎么回事，我们之后来讲，首先我们先来看怎么构造这样的一个类WrenchItemRenderer

```java

public class WrenchItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static int degree = 0;
    public WrenchItemRenderer(){
        super(null,null);
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        if(degree==360){
            degree=0;
        }
        degree++;

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        BakedModel bakedModel = itemRenderer.getModel(pStack,null,null,1);
        pPoseStack.pushPose();
        pPoseStack.translate(0.5F, 0.5F, 0.5F);
        float xOffset = -1 / 32f;
        float zOffset = 0;
        pPoseStack.translate(-xOffset, 0, -zOffset);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(degree));
        pPoseStack.translate(xOffset, 0, zOffset);
        itemRenderer.render(pStack, ItemDisplayContext.NONE, false, pPoseStack, pBuffer, pPackedLight, pPackedOverlay, bakedModel);
        pPoseStack.popPose();
    }
}
```

可以看到这样的一个render是BlockEntityWithoutLevelRenderer类是实现的，那么我么先看看BlockEntityWithoutLevelRenderer类，再来看我们写的这个类。

BlockEntityWithoutLevelRenderer用于实现在Minecraft游戏中渲染不需要block的实体渲染器。BlockEntityWithoutLevelRenderer类实现了ResourceManagerReloadListener接口，用于在资源管理器重新加载时，重新加载模型和材质等资源。

在类中声明了几个静态变量，例如SHULKER_BOXES数组，用于存储不同颜色的箱子实体，DEFAULT_SHULKER_BOX变量，用于存储默认的箱子实体，以及几个实体对象，例如chest、trappedChest、enderChest等，用于渲染不同类型的实体。
renderByItem()方法是在渲染物品时调用的方法，用于渲染方块实体。在该方法中，首先获取物品栈的物品对象，根据该物品的类型，选择不同的渲染方式。例如，如果物品是盾牌，则调用盾牌模型渲染方法；如果物品是三叉戟，则调用三叉戟模型渲染方法；如果物品是一个方块，则根据方块的类型和状态，选择相应的实体对象进行渲染。

实际上我们的动画的物品是同样是通过方块实体渲染来实现的，我们要重写方法就是这个renderByItem()方法。

好了，我们来看我们的这个render吧。

```java
    private static int degree = 0;
```

这里声明了一个字段，这个字段是用于计算物品旋转的情况的。

我们重写了renderByItem方法，以下是内容：

```java
  if(degree==360){
            degree=0;
        }
        degree++;

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        BakedModel bakedModel = itemRenderer.getModel(pStack,null,null,0);
        pPoseStack.pushPose();
        pPoseStack.translate(0.5F, 0.5F, 0.5F);
        float xOffset = -1 / 32f;
        float zOffset = 0;
        pPoseStack.translate(-xOffset, 0, -zOffset);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(degree));
        pPoseStack.translate(xOffset, 0, zOffset);
        itemRenderer.render(pStack, ItemDisplayContext.NONE, false, pPoseStack, pBuffer, pPackedLight, pPackedOverlay, bakedModel);
        pPoseStack.popPose();
```
我们做了这些操作。首先判断了角度，如果是360就重置，否则就自增。然后我们获得了ItemRenderer，通过ItemRenderer获得了物品的烘焙模型BakedModel，之后我们调整移动这个模型这个模型，并让他沿着Y轴缓慢旋转

好了我们定义了render但是我们的render现在并没有工作，我们还需要在烘焙模型中开启使用BlockEntityWithoutLevelRenderer,并且和物品进行绑定.

我们先定义我们的烘焙模型，并替换原本的烘焙模型，这些操作和上一个教程类似。


```java

public class WrenchBakeModel implements BakedModel {
    private BakedModel existingModel;

    public WrenchBakeModel(BakedModel existingModel) {
        this.existingModel = existingModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pDirection, RandomSource pRandom) {
        return this.existingModel.getQuads(pState, pDirection, pRandom);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType) {
        throw new AssertionError("IForgeBakedModel::getQuads should never be called, only IForgeBakedModel::getQuads");
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.existingModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.existingModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return this.existingModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }



    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.existingModel.getParticleIcon();
    }

    @Override
    public ItemOverrides getOverrides() {
        return this.existingModel.getOverrides();
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext transformType, PoseStack poseStack, boolean applyLeftHandTransform) {
        if (transformType == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND){
            return this;
        }
        return this.existingModel.applyTransform(transformType,poseStack,applyLeftHandTransform);
    }
}
```

第一个需要说的是这里,对于物品获得对于的顶点数据，是getQuads(@Nullable BlockState pState, @Nullable Direction pDirection, RandomSource pRandom) 方法，而 getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType)方法是不会生效的，所以这里我们直接抛出一个异常。

```java
@Override
    public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pDirection, RandomSource pRandom) {
        return this.existingModel.getQuads(pState, pDirection, pRandom);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType) {
        throw new AssertionError("IForgeBakedModel::getQuads should never be called, only IForgeBakedModel::getQuads");
    }

```
第二个要说就是这个, 这个表示了我们的模型要使用自己定义的渲染，而不是使用默认的渲染。

```java
    @Override
    public boolean isCustomRenderer() {
        return true;
    }

```

第三个就是这里，这里的意识是说在在不同的ItemDisplayContext下返回的烘焙模型不一样，这里我们在玩家的手持的时候返回我们这个模型，也是就是自定义渲染的这个模型，而且时候返回默认的模型。

```java

    @Override
    public BakedModel applyTransform(ItemDisplayContext transformType, PoseStack poseStack, boolean applyLeftHandTransform) {
        if (transformType == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND){
            return this;
        }
        return this.existingModel.applyTransform(transformType,poseStack,applyLeftHandTransform);
    }```

使用我们的模型替换掉原本的烘焙模型

```java

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
    public static class ModEventBus{
        @SubscribeEvent
        public static void onModelBaked(ModelEvent.ModifyBakingResult event){
            // wrench item model
            Map<ResourceLocation, BakedModel> modelRegistry = event.getModels();
            ModelResourceLocation location = new ModelResourceLocation(BuiltInRegistries.ITEM.getKey(ModItems.WRENCH_ITEM.get()), "inventory");
            BakedModel existingModel = modelRegistry.get(location);
            if (existingModel == null) {
                throw new RuntimeException("Did not find Obsidian Hidden in registry");
            } else if (existingModel instanceof WrenchBakeModel) {
                throw new RuntimeException("Tried to replaceObsidian Hidden twice");
            } else {
                WrenchBakeModel obsidianWrenchBakedModel = new WrenchBakeModel(existingModel);
                event.getModels().put(location, obsidianWrenchBakedModel);
            }
        }
    }
```

和之前的内容大致相同，这里就不多加赘述了。

好了回到我们最开始的那个物品的类的中，我可以解释那段代码了。

```java
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new WrenchItemRenderer();
            }
        });
    }
```

public BlockEntityWithoutLevelRenderer getCustomRenderer(): 这个方法返回一个自定义的渲染器，用于在客户端渲染 WrenchItem 对象。代码中创建了一个 WrenchItemRenderer 对象并返回。