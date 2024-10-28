---
title: 40 流体
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 40 流体 相关教程
image: ./covers/db5375b0efcd53976a3fe5d47fbd8f3b3216e0f8.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 参考
https://boson.v2mcdev.com/fluid/firstfluid.html
## 流体

这次我们来说怎么创建流体。

在游戏中你看到的流体一把是在世界中存在的流动的和源头的两中，不过还有就是机器中处理的，桶这样的形势存在的。

当在桶内装的时候，是一个BucketItem的类，而在世界中不断的流动的就是一种特殊的方块，你可以尝试删除某个 流体的贴图你会发发现都是黑紫块了。

说明流体在世界中就是一种特殊的方块，不过这种特殊的方块有着类似方块和方块实体的这样的特殊关系，方块和对应的流体关联起来。

对于流体有两种状态我们需要创建这两种装填的流体，流动的flow，和源头source以及流体的type，这三个类是我们需要处理的，以及将创建的流体和对于的方块做出关联。

我们先来看下流体的type怎么处理，由于原版的fluidtype直接使用不方便，这里就给他包裹一层，然后我们使用我们创建的这个basefluidtype创建流体的type。

下面我们来看看这个类都提供了什么功能，和流体的那些属性有关。

```java
// 用于定义了流体类型
public class BaseFluidType extends FluidType {
    // 定义了源source的纹理图片，流动的纹理图片，以及流体覆盖层的图片（指的是颜色，例如水的蓝色纹理，岩浆的红色纹理，你可以到原版对应的位置看看是什么图片就知道了）
    private final ResourceLocation stillTexture;
    private final ResourceLocation flowingTexture;
    private final ResourceLocation overlayTexture;
    // 流体的着色颜色
    private final int tintColor;
    // 从流体中看外面的雾的颜色
    private final Vector3f fogColor;

    //构造函数
    public BaseFluidType(final ResourceLocation stillTexture, final ResourceLocation flowingTexture, final ResourceLocation overlayTexture,
                         final int tintColor, final Vector3f fogColor, final Properties properties) {
        super(properties);
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        this.overlayTexture = overlayTexture;
        this.tintColor = tintColor;
        this.fogColor = fogColor;
    }
    // 对应的get函数
    public ResourceLocation getStillTexture() {
        return stillTexture;
    }

    public ResourceLocation getFlowingTexture() {
        return flowingTexture;
    }

    public int getTintColor() {
        return tintColor;
    }

    public ResourceLocation getOverlayTexture() {
        return overlayTexture;
    }

    public Vector3f getFogColor() {
        return fogColor;
    }

    // 对于我们的几个纹理，如果如果想生效的话，就需要重写这个方法，在对于的方法将我们的RL的资源定位的图片返回。
    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return stillTexture;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return flowingTexture;
            }

            @Override
            public @Nullable ResourceLocation getOverlayTexture() {
                return overlayTexture;
            }

            @Override
            public int getTintColor() {
                return tintColor;
            }

            // 修改从流体中看雾的颜色
            @Override
            public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level,
                                                    int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                return fogColor;
            }
            // 液体中的能见度 或者 说雾的范围
            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick,
                                        float nearDistance, float farDistance, FogShape shape) {
                RenderSystem.setShaderFogStart(1f);
                RenderSystem.setShaderFogEnd(6f); // distance when the fog starts
            }
        });
    }

}

```

好了我们可以看到流体类型fluidType类和流体的颜色，贴图等属性有关。

我们接下来看怎么使用BaseFluidType创建我们的流体类型。并注册到总线。

```java

public class ModFluidType {
    // 图片的位置，这里的source和flow，直接使用的原版的，所以没有第一个参数传入modid。
    public static final ResourceLocation WATER_STILL_RL = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = new ResourceLocation("block/water_flow");
    // 这里的流体的overlay的图片使用是自己的图片，直接原版的water修改的。
    public static final ResourceLocation MY_FLUID_RL = new ResourceLocation(ExampleMod.MODID, "misc/my_fluid");

    // 怎么获得流体类型FluidType的注册器，有一点特殊，不是直接在Registires类下，而是在NeoForgeRegistries.Keys下
    // 这应该是因为原版并没有流体类型FluidType这样的概念。
    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, ExampleMod.MODID);

    // 我们看到使用了register这个方法，这个方法是我们自己写的。
    // 对于register在下面介绍，我们来看参数
    // 第一参数name，没什么好说的，第二个参数是对流体的类型的属性进行一些设置  FluidType.Properties这个类就是对属性的一些设置。
    // create返回一个properties实例，lightlevel设置亮度等级2，density设置密度，viscosity设置粘度，sound设置流体声音。
    // 这些数值是直接复制的水的，对于其中的一些具体的效果，就自己调试看看效果把。也可以大家弹幕评论补充
    public static final Supplier<FluidType> MY_FLUID_TYPE = register("my_fluid",
            FluidType.Properties.create().lightLevel(2).density(15).viscosity(5).sound(SoundAction.get("drink"),
                    SoundEvents.HONEY_DRINK));

    // 这个是我们自己写的注册的方法
    // 并没有什么特殊的，直接返回了new baseFludType的supplier方法。
    private static Supplier<FluidType> register(String name, FluidType.Properties properties) {
        return FLUID_TYPES.register(name, () -> new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, MY_FLUID_RL,
                0xA1E038D0, new Vector3f(224f / 255f, 56f / 255f, 208f / 255f), properties));
    }
    // 记得注册到总线
    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }

}

```

好了，流体类型说完了，我们来看怎么处理流体了，包含了流体的source和flow，其实source和flow都是继承FlowingFluid类的。不过Neoforge为我们提供了BaseFlowingFluid类以及Source和Flowing子类帮助我们创建对应的实例。

```java

public class ModFluids {
    // 流体注册器
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, ExampleMod.MODID);

    // 注册对应流体的source和flow，使用NeoForge提供的BaseFlowingFluid来注册
    // 其中source和flow都需要填入一个参数，这个参数是流体的属性,在下面定义
    public static Supplier<FlowingFluid> MY_SOURCE_FLUID_BLOCK = FLUIDS.register("my_fluid", () -> new BaseFlowingFluid.Source(ModFluids.MY_FLUID_PROPERTIES));
    public static Supplier<FlowingFluid> MY_FLOWING_FLUID_BLOCK = FLUIDS.register("my_fluid_flow", () -> new BaseFlowingFluid.Flowing(ModFluids.MY_FLUID_PROPERTIES));
    // 定义流体的属性
    // 这个流体的属性要传入的内容比较多，我们挨个介绍，我们使用了BaseFlowingFluid的Properties内部类创建对应的Properties，其中第一个参数是对应的流体的类体类型FluidType，然后第二个参数是对应的source流体，第三个参数是flow流体，都是我们刚刚写过的，看起来比较绕，大家自己理清下关系。
    // 通过bucket这个设置流体和对应的流体桶的绑定，等会我们注册这个bucketitem
    // 通过block绑定对应的流体和方块的绑定，这个方块等会我们注册。
    // slopeFindDistance寻找斜坡的距离
    // levelDecreasePerBlock 每个方块流体的减少量。
    // 后两个数据是用于流体的流动的，主要是斜坡时候优先流，不会扩散。
    // 以及流体最多能流多远，例如原版的水是8格
    // 可以自己调试这几个数值试试，也可以去wiki看看具体的含义。
    private static final BaseFlowingFluid.Properties MY_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(ModFluidType.MY_FLUID_TYPE, ModFluids.MY_SOURCE_FLUID_BLOCK, ModFluids.MY_FLOWING_FLUID_BLOCK).bucket(ModItems.MY_FLUID_BUCKET).slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.MY_FLUID_BLOCK);
    // 别忘记注册到总线
    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
```
下面我们来看对应的流体的方块和物品。

先看方块。

```java
// 注册方块，不过我们没有使用我么自己的那个方法，而是直接使用BLOCK的register方法，主要是我们不需要提供对应的item。因为我们还要注册对应的bucketitem。
    public static final Supplier<LiquidBlock> MY_FLUID_BLOCK = BLOCKS.register("my_fluid_block",
            ()->new LiquidBlock(ModFluids.MY_SOURCE_FLUID_BLOCK,BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));

```

流体桶

```java
// 流体桶，craftRemainder表示合成之后保留桶
    public static final Supplier<Item> MY_FLUID_BUCKET = register("my_fluid_bucket", ()->new BucketItem(ModFluids.MY_SOURCE_FLUID_BLOCK,new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

```

流体的渲染，由于我们的流体是一个半透明的材质，所以指定流体的渲染为：translucent

对于source和flow都需要指定。

```java
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ModClientEventHandler {
    @SubscribeEvent
    public static void onClientEvent(FMLClientSetupEvent event){
        event.enqueueWork(()->{
            //fluid
            ItemBlockRenderTypes.setRenderLayer(ModFluids.MY_SOURCE_FLUID_BLOCK.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.MY_FLOWING_FLUID_BLOCK.get(), RenderType.translucent());

        });

    }
```

好了，别忘记了注册到总线，然后你就可以进入游戏中看看了。
