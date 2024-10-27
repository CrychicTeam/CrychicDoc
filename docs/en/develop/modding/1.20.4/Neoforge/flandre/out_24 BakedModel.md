---
title: 24 烘焙模型
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 24 烘焙模型 相关教程
image: ./covers/209819cd236988806684c93079f865dd36bb9dcb.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 参考

https://boson.v2mcdev.com/specialrender/ibakedmodel.html

## BakedModel烘焙模型

在开始正式写代码之前，我们先要了解如何一个方块是如何渲染出来的

Minecraft本身会读取json和材质文件将其转换成IModel接口的实例，然后IModel进行了「Bake（烘焙）」处理，变成了IBakedModel，这个IBakedModel会被放入BlockRendererDispatcher中，当游戏需要时会直接从BlockRendererDispatcher取出IBakedModel进行渲染。至于什么是「Bake」，Bake基本上是对模型的材质进行光照计算等操作，让它变成可以直接被GPU渲染的东西。

> 以上内容来自https://boson.v2mcdev.com/specialrender/ibakedmodel.html

这次我们会效仿boson教程的内容做一个隐藏方块出来。再此之前我们还是先来了解下相关的类和我们应该怎么操作。

IBakedModelExtension接口

这是一个对BakedModel接口拓展的接口，由neoforge提供的。包含了获得模型的面片（quads）、是否使用环境遮蔽（ambient occlusion）、应用变换、获取粒子图标、获取渲染类型等。

getQuads 方法用于获取模型的面片，如果渲染类型为空，则返回所有的面片；useAmbientOcclusion 方法用于判断是否使用环境遮蔽；applyTransform 方法用于应用变换；getModelData 方法用于获取模型数据；getParticleIcon 方法用于获取粒子图标；getRenderTypes 方法用于获取渲染类型；getRenderPasses 方法用于获取用于渲染物品的模型列表。

BakedModel接口

这个接口用于表示 Minecraft 中已经预先处理好的模型，

这个接口中定义了一些方法，用于获取模型的面片、是否使用环境遮蔽、是否是 3D GUI、是否使用方块光源、是否使用自定义渲染器、获取粒子图标、获取变换、获取覆盖器等。

在说了这些接口方法的内容之后，我们来说一下我们的要添加的隐藏方块，该方块在本方块下面有方块的时候会模仿下方方块的模型，如果没有则显示自己的模型。

所以我们还是先添加一个方块，这个就和之前一样不过多介绍了。

```java
public class HiddenBlock extends Block {
    public HiddenBlock(){
        super(Properties.ofFullCopy(Blocks.STONE).noOcclusion());
    }
}

```

接下来我们再看自己的烘焙模型的代码:

```java

public class HiddenBlockModel implements BakedModel {
    BakedModel defaultModel;
    public static ModelProperty<BlockState> COPIED_BLOCK = new ModelProperty<>();

    public HiddenBlockModel(BakedModel existingModel){
        this.defaultModel = existingModel;
    }
    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType) {
        BakedModel renderModel = defaultModel;
        if (data.has(COPIED_BLOCK)) {
            BlockState copiedBlock = data.get(COPIED_BLOCK);
            if (copiedBlock != null) {
                Minecraft mc = Minecraft.getInstance();
                BlockRenderDispatcher blockRendererDispatcher = mc.getBlockRenderer();
                renderModel = blockRendererDispatcher.getBlockModel(copiedBlock);
            }
        }
        return renderModel.getQuads(state,side,rand,data,renderType);
    }

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData modelData) {
        BlockState downBlockState = level.getBlockState(pos.below());
        ModelData modelDataMap = modelData.derive().with(COPIED_BLOCK,null).build();

        if(downBlockState.getBlock() == Blocks.AIR || downBlockState.getBlock() == ModBlocks.HIDDEN_BLOCK.get()){
            return modelDataMap;
        }

        return modelDataMap.derive().with(COPIED_BLOCK,downBlockState).build();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pDirection, RandomSource pRandom) {
        throw new AssertionError("IBakedModel::getQuads should never be called, only IForgeBakedModel::getQuads");
    }

    @Override
    public boolean useAmbientOcclusion() {
        return defaultModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return defaultModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return defaultModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return defaultModel.isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return defaultModel.getParticleIcon();
    }

    @Override
    public ItemOverrides getOverrides() {
        return defaultModel.getOverrides();
    }
}

```

我们实现了BakedModel接口,自然要实现接口的方法

```java

public class HiddenBlockModel implements BakedModel {

```

这里我们定义了两个字段，一个是烘焙模型，另一个是ModelProperty，其中第一个字段，用于存储我们默认情况游戏帮我们生成的模型，也就是我们平常直接使用的模型，包括cubeall那个。而为了讲解第二个定义的字段，我们就需要说一下ModelProperty和ModelData

```java
    BakedModel defaultModel;
    public static ModelProperty<BlockState> COPIED_BLOCK = new ModelProperty<>();
```

我们先看ModelProperty，这个类继承于Predicate接口，Predicate接口表示一个函数式接口，用于表示对某个对象的判断。ModelProperty类有两个构造函数，一个是无参构造函数，它会创建一个ModelProperty对象，其内部predicate是一个总是返回true的函数；另一个是有参构造函数，它会创建一个ModelProperty对象，其内部predicate是输入的Predicate函数。

这个ModelProperty类被设计用于ModelData中，ModelData是一个接口，用于存储方块模型的数据。ModelProperty可以用于在ModelData对象中存储一些属性，并可选地对这些属性进行判断和校验。

我们再来看ModelData，它是方块模型数据的容器类，用于存储和传递方块模型的数据。ModelData类有一个成员变量properties，是一个映射表，用于存储多个ModelProperty和其对应的值。ModelData类有一个构造函数，接收一个映射表作为输入参数，并将其保存在成员变量properties中。

- getProperties：该方法返回当前ModelData对象中所有ModelProperty的集合。
- has：该方法判断当前ModelData对象中是否包含指定的ModelProperty。
- get：该方法获取当前ModelData对象中指定的ModelProperty对应的值，返回值为泛型T。
- derive：该方法返回一个Builder对象，用于创建一个新的ModelData对象，并可选地从当前ModelData对象中复制一些ModelProperty和值。
- builder：该方法返回一个新的Builder对象，用于创建一个空的ModelData对象。

ModelData类中还定义了一个内部类Builder，用于构建ModelData对象。

Builder类提供了一个方法with，用于在构建ModelData时，向当前Builder对象中添加一个ModelProperty和其对应的值。在添加之前，会对值进行判断，如果值不满足ModelProperty的要求，则抛出异常。

Builder类还提供了一个方法build，用于构建一个新的ModelData对象，并返回。 这个ModelData对象是一个不可变对象，其properties是一个只读映射表，并且构建之后不能修改。

好了到此为止，我们就可以知道我们的ModelProperty是什么东西了。我们结合下面的代码一起看。

对于getQuads方法有两个方法，对于我们方块模型获得数据来说，我们会用到这个
```java

    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType) {

```
对于另一个方法，如果调用了，就直接抛出错误。
```java
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pDirection, RandomSource pRandom) {
        throw new AssertionError("IBakedModel::getQuads should never be called, only IForgeBakedModel::getQuads");
    }
```

我们会注意到getQuads方法会用使用到ModelData这个参数，而这个参数传入的数据就是
```java
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData modelData) {

```

这个方法传递的。所以我们先来看这个getModelData方法实现。
```java
        BlockState downBlockState = level.getBlockState(pos.below());
        ModelData modelDataMap = modelData.derive().with(COPIED_BLOCK,null).build();

        if(downBlockState.getBlock() == Blocks.AIR || downBlockState.getBlock() == ModBlocks.HIDDEN_BLOCK.get()){
            return modelDataMap;
        }

        return modelDataMap.derive().with(COPIED_BLOCK,downBlockState).build();
```
可以看到在该方法中，我们首先获得了当前方块的下面方块的blockstate，之后构建一个modeldata，复制了原modeldata的数据，并添加了COPIED_BLOCK这个ModelProperty其数值为null，然后判断该方块下面方块是不是空气方块或则是我们隐藏方块，如果是的话，就返回这个modeldata，其中的COPIED_BLOCK的字段是null。如果不是，我们就返回COPIED_BLOCK字段是下面方块的blockstate，之后我们通过这个blockstate获得对应方块状态的model。

在让我们看看getQuads方法,该方法会返回对应的blockstate的模型数据，
```java
        BakedModel renderModel = defaultModel;
        if (data.has(COPIED_BLOCK)) {
            BlockState copiedBlock = data.get(COPIED_BLOCK);
            if (copiedBlock != null) {
                Minecraft mc = Minecraft.getInstance();
                BlockRenderDispatcher blockRendererDispatcher = mc.getBlockRenderer();
                renderModel = blockRendererDispatcher.getBlockModel(copiedBlock);
            }
        }
        return renderModel.getQuads(state,side,rand,data,renderType);
```
我们还是获得该模型本身的模型，之后判断modeldata中我们添加的字段是不是null，如果是null，那么我们直接返回默认的数据，如果不是null我们获得对应的该字段的blockstate，通过blockRendererDispatcher获得对应blockstate的模型数据，设置rendermoodel，然后返回该model的数据。

对于其他的几个方法我们直接调用了默认模型的方法，对于他们都是什么作用在之前讲解BakeModel时候已经说过了，自己翻看。

好了我们目前只是定义了这个烘焙模型，怎么使用它呢？我们需要将原本的烘焙模型替换我们写的这个烘焙模型，万幸的是neoforge为我们提供了对应的事件。

```java

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
    public static class ModEventBus{
        @SubscribeEvent
        public static void onModelBaked(ModelEvent.ModifyBakingResult event){
            for(BlockState blockstate: ModBlocks.HIDDEN_BLOCK.get().getStateDefinition().getPossibleStates()){
                ModelResourceLocation modelResourceLocation = BlockModelShaper.stateToModelLocation(blockstate);
                BakedModel existingModel = event.getModels().get(modelResourceLocation);
                if(existingModel==null){
                    throw new RuntimeException("Did not find Obsidian Hidden in registry");
                }else if (existingModel instanceof HiddenBlockModel) {
                    throw new RuntimeException("Tried to replaceObsidian Hidden twice");
                }else {
                    HiddenBlockModel obsidianHiddenBlockModel = new HiddenBlockModel(existingModel);
                    event.getModels().put(modelResourceLocation, obsidianHiddenBlockModel);
                }
            }
        }
    }
```
由于烘焙模型替换事件是在游戏启动时候发生的， 所以我们使用的是MOD总线，你也可以在事件的定义的注解中看到关于事件应该写在总线上，也说明了是不是CLIENT,这里是CLIENT，所以我们写了value=Dist.CLIENT保证只在客户端加载。

ModBlocks.HIDDEN_BLOCK.get().getStateDefinition().getPossibleStates()，迭代了所有HIDDEN_BLOCK的blockstate，获得blockstate的modelResourceLocation。 通过modelResourceLocation和event.getModels().get方法获得对应的Blockstate的bakedmodel，之后我们做了判断如果为null，则报错说明没有找到这个HIDDEN_BLOCK的方块的默认模型，如果找到了，我们还判断existingModel这个模型是不是HiddenBlockModel我们创建烘焙模型的实例化如果是则说明这个模型替换了两遍，当这些都没问题，我们就创建一个自己的HIDDEN_BLOCK_MODEL模型并通过event.getModels().put(modelResourceLocation, obsidianHiddenBlockModel);替换掉原来modelResourceLocation定位模型。

> 我相信经过前几期的教程应该可以自己看懂modelResourceLocation的作用了，其实就是blockstate模型的资源定位

> 对于这里的event.getModels() 返回的是Map<ResourceLocation, BakedModel>这样的MAP，这个MAP维护了ResourceLocation和对应的BakedModel的键值对。

好了对于方块的模型，材质，注册，以及创造模式物品栏的添就留给观众了，你也可以直接查看源码。

你进入游戏将当前方块放置在另一个方块上时候就会模仿他的模型了，不过要注意的是这只是一个演示讲解的例子，可能会有一些bug。

