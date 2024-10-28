---
title: 29 第一个实体
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 29 第一个实体 相关教程
image: ./covers/8aeac5a0016d893c1a62e2531d794fa19a47fa61.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 参考

https://boson.v2mcdev.com/entity/scratchentity.html
植物魔法

## 实体
这此我们来看看怎么从零添加一个实体，游戏中的实体不仅仅是各种生物也包含了其他的内容，比如船，矿车，末影水晶都是实体。
实体是一个很重要的概念，模组的很多内容需要通过实体实现。下面我们介绍一下有关的内容。

在我的世界中你要做一个实体需要处理三个内容，
- 一个是处理实体的逻辑的类Entity实体类
- 另一个是处理渲染的渲染类EntityRenderer，觉得实体怎么渲染
- 还有一个是实体的模型类EntityModel，觉得实体模型

这次我们做一个实体，实体的模型是通过blockbench制作的， 详细可以见看blockbench教程。这里会简单介绍下。

这个实体的逻辑是给数据自增，并完成和客户端的数据同步。

我们先来实现Entity类。

```java
public class FlyingSwordEntity extends Entity {
    //LOGGER的Logger对象，用于记录日志信息。
    private static final Logger LOGGER = LogUtils.getLogger();
    //COUNTER的实体数据访问器，用于存储实体的计数器数据。
    private static final EntityDataAccessor<Integer> COUNTER = SynchedEntityData.defineId(FlyingSwordEntity.class, EntityDataSerializers.INT);
    @Override
    public void tick() {
        //检查当前是否为客户端，如果是，则从实体数据中获取计数器数据并记录日志信息。如果不是客户端，则从实体数据中获取计数器数据，记录日志信息，并将计数器数据加1。最后，调用父类的tick()方法。
        // 说的明白一些就是服务器将计数+1，然后进行数据的同步，在客户端打印出来。
        if(this.level().isClientSide){
            Integer i = this.entityData.get(COUNTER);
            LOGGER.info(i.toString());
        }
        if(!this.level().isClientSide){
            LOGGER.info(this.entityData.get(COUNTER).toString());
            this.entityData.set(COUNTER,this.entityData.get(COUNTER)+1);
        }
        super.tick();
    }
    //构造方法
    public FlyingSwordEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    //defineSynchedData()：该方法用于定义实体的同步数据，在该方法中，将COUNTER实体数据访问器初始化为0。
    @Override
    protected void defineSynchedData() {
        this.entityData.define(COUNTER, 0);

    }
    //readAdditionalSaveData()：该方法用于从NBT标签中读取额外的保存数据，在该方法中，从NBT标签中读取计数器数据，并保存到实体数据中。
    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.entityData.set(COUNTER,pCompound.getInt("counter"));
    }
    //addAdditionalSaveData()：该方法用于向NBT标签中添加额外的保存数据，在该方法中，将计数器数据保存到NBT标签中。
    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putInt("counter",this.entityData.get(COUNTER));
    }

}

```
在代码的注解中对每个代码进行了介绍，不过这里还需要对一些内容进行详细的讲解首先是COUNTER，所有客户端和服务端需要同步的数据都需要用这样的形式进行定义。泛型的类型是你数据的类型。第一个参数是实体类，第二个参数是类型，在EntityDataSerializers中找到，EntityDataSerializers定义的类型已经足够你使用了。
```java
    private static final EntityDataAccessor<Integer> COUNTER = SynchedEntityData.defineId(FlyingSwordEntity.class, EntityDataSerializers.INT);

```

定义两端同步的数据还需要进行初始化，这这里
```java
    @Override
    protected void defineSynchedData() {
        this.entityData.define(COUNTER, 0);

    }
```

下面我们来看Model，你可以直接通过blockbench做模型做完之后导出对应的java文件，然后把其中的内容放在这里。我们对其中的内容做一定的解释。

model 
```java

public class FlyingSwordModel extends EntityModel<FlyingSwordEntity> {
    //它用于标记模型的一个特定层。这里它被用来注册模型并指定资源位置和层级的名称。
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ExampleMod.MODID, "flying_sword_entity"), "main");
    //用来表示实体模型的主要部分
    private final ModelPart body;

    public FlyingSwordModel(ModelPart root){
        //调用root.getChild("body")获取到表示剑身体的模型部分，并赋值给body字段。
        this.body = root.getChild("body");
    }
    //静态方法createBodyLayer，该方法用于创建并定义飞行剑模型的结构和外观。
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        //这个构建器用于定义立方体的形状、位置和纹理。
        // -1.0F, -1.0F, -18.0F, 3.0F, 1.0F, 19.0F  前三个值是空间中的位置，然后三个值是方块的大小，
        // .texOffs(0, 20) 模型中一个方块的UV位置
        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -18.0F, 3.0F, 1.0F, 19.0F, new CubeDeformation(0.0F))
                .texOffs(0, 20).addBox(-3.0F, -2.0F, -1.0F, 7.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.0F, -1.0F, 1.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0F, 0.0F));
        // 方法返回一个LayerDefinition对象，这个对象包含了整个网格定义和纹理的尺寸。
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    //这个方法用于在每个动画帧上设置实体的动画和姿势。这里它将剑的旋转角度设定为与实体的动作参数相关联。
    @Override
    public void setupAnim(FlyingSwordEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        //置剑身体body的三维旋转角度，分别对应于X轴（pLimbSwing）、Y轴（pNetHeadYaw）和Z轴（pHeadPitch）的旋转。
        body.xRot = pLimbSwing;
        body.yRot = pNetHeadYaw;
        body.zRot = pHeadPitch;
    }
    //来控制模型的要怎么渲染的
    // 一般只需要调用模型的render方法即可
    @Override
    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        body.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
    }
}

```
下面是实体的renderer类，他继承于EntityRenderer，用来渲染我们的实体。


renderer
```java

public class FlyingSwordEntityRenderer extends EntityRenderer {
    // 存储我们的模型。
    private EntityModel<FlyingSwordEntity> flyingSwordModel;

    public FlyingSwordEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        //这里使用pContext.bakeLayer(FlyingSwordModel.LAYER_LOCATION)来准备ModelPart，这里的获得ModelPart是等会我们需要去注册的，通过LAYER_LOCATION注册我们的ModelPart
        flyingSwordModel = new FlyingSwordModel(pContext.bakeLayer(FlyingSwordModel.LAYER_LOCATION));
    }
        //这个方法返回一个ResourceLocation对象，指明了飞行剑实体的纹理文件位置。
    @Override
    public ResourceLocation getTextureLocation(Entity pEntity) {
        return new ResourceLocation(ExampleMod.MODID, "textures/entity/flying_sword_entity.png");
    }
    //重写了render方法，这个方法定义了实体在游戏中的渲染逻辑。
    @Override
    public void render(Entity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        //你的渲染应该在posh和pop之间，避免污染其他的渲染。
        pPoseStack.pushPose();
        // 绕y轴旋转45°
        pPoseStack.mulPose(Axis.YN.rotationDegrees(45));
        // 向下移动1格
        pPoseStack.translate(0,-1,0);
        // 构建顶点
        VertexConsumer buffer = pBuffer.getBuffer(this.flyingSwordModel.renderType(this.getTextureLocation(pEntity)));
        // 调用模型的render方法进行渲染，这里的OverlayTexture下有很多类型，自己选用。
        this.flyingSwordModel.renderToBuffer(pPoseStack,buffer,pPackedLight, OverlayTexture.NO_OVERLAY,1f,1f,1f,1f);
        pPoseStack.popPose();
    }
}

```

好了，三个类都写完了，不过我们还需要对他们进行注册，三个类都是需要注册的。 我们先来注册entity，这里对于的注册是entityType,比较简单。

不过我们还是需要说一下这个sized设置实体的碰撞体积，你进入游戏后按下f3+b就可以看到了。

```java

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, ExampleMod.MODID);
    public static final Supplier<EntityType<FlyingSwordEntity>> FLYING_SWORD_ENTITY = ENTITY_TYPES.register("flying_sword_entity", () -> EntityType.Builder.of(FlyingSwordEntity::new, MobCategory.MISC).sized(2, 0.5F).build("flying_sword_entity"));

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}

```
这里注册我们的modelpart也就是在render中通过context获得的，我们用到了这个RegisterLayerDefinitions事件，这个事件提供了registerLayerDefinition的方法，我们需要提供对于的r】LayerDefinition，而LayerDefinition是我们的模型createBodyLayer方法返回的，所以这传入该方法的方法引用即可。

```java
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions evt) {
        evt.registerLayerDefinition(FlyingSwordModel.LAYER_LOCATION, FlyingSwordModel::createBodyLayer);
    }
}
```
然后来注册我们的renderer，也是很简单，第一个参数是我们的实体，第二个参数是我们的renderer的构造方法的方法引用。传入即可。注意的默认传入的参数就是context

```java
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onClientEvent(FMLClientSetupEvent event){
        event.enqueueWork(()->{
            EntityRenderers.register(ModEntityTypes.FLYING_SWORD_ENTITY.get(), FlyingSwordEntityRenderer::new);
        });
    }
}

```

将你的贴图放在对于的位置下，然后就可以进入游戏看看了。
textures/entity/flying_sword_entity.png
