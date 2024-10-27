---
title: 30 第一个生物
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 30 第一个生物 相关教程
image: ./covers/69f2bc24f563756172f2e6256da455eecae40d52.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 参考
https://boson.v2mcdev.com/entity/scratchentity.html
## 第一个动物以及AI

上一次我们从entity类出发创建了一个实体，不过大多时候是不需要从entity类出发创建实体的，你可以看到entitty的继承关系中，会有很多的子类，这些子类才是更多我们会利用到的，
这次我们还是利用entity子类Animal类创建一个动物实体，并为他设置一个AI/Goal，模型用blockbench创建。

什么是Goal/AI，其实就是生物的目的或者说是行为逻辑，例如牛会被小麦吸引等等。

什么是属性就是，这里的属性就是生物的血量，移动速度，需要单独配置并注册。

我们先看怎么写我们的这个animal entity类。

```java

public class FirstAnimal extends Animal {
    protected FirstAnimal(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
   // 重写了父类的registerGoals方法，该方法用于注册动物的行为目标Goal。
    @Override
    protected void registerGoals() {
        // 这里添加的是我们自己的Goal
        this.goalSelector.addGoal(0,new MyGoal(this));
    }

    // 定义了一个静态方法createAttributes，用于创建和配置动物的属性。
    // 这个方法返回一个AttributeSupplier.Builder对象，用于构建属性。
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0);
    }
    // 重写了父类的getBreedOffspring方法，该方法用于生成动物的繁殖后代。
    // 我们这里不需要繁衍的生物，所以直接返回null即可。
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }
}
```

动物AI

```java

public class MyGoal extends Goal {
    public final FirstAnimal firstAnimal;
    public MyGoal(FirstAnimal firstAnimal){
        this.firstAnimal = firstAnimal;
    }
      // 重写了Goal类中的canUse方法，该方法用于判断是否应当执行这个行为目标。
      // 这里返回true 表示一直执行。
      // 逻辑很简单就是给最近的玩家添加一个饥饿的效果。
    @Override
    public boolean canUse() {
        Level level = this.firstAnimal.level();
        if(!level.isClientSide){
            Player nearestPlayer = level.getNearestPlayer(this.firstAnimal, 10);
            if(nearestPlayer!=null){
                nearestPlayer.addEffect(new MobEffectInstance(MobEffects.HUNGER, 3 * 20, 3));
            }

        }
        return true;
    }
}

```

动物模型

```java
public class FirstAnimalModel extends EntityModel<FirstAnimal> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ExampleMod.MODID, "first_animal"), "main");
    private final ModelPart bb_main;

    public FirstAnimalModel(ModelPart root){
        this.bb_main = root.getChild("bb_main");

    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -20.0F, -8.0F, 16.0F, 11.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
    @Override
    public void setupAnim(FirstAnimal pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        bb_main.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
    }
}

```

动物渲染，这里我们使用了MobRenderer，相对于EntityRenderer多了一些渲染影子的内容内容，看构造函数，

```java

public class FirstAnimalRenderer extends MobRenderer<FirstAnimal, FirstAnimalModel> {
    public FirstAnimalRenderer(EntityRendererProvider.Context pContext) {
        // 第一个参数我们将EntityRendererProvider。Context传入，
        // 第二个参数是model，传入我们的model，model要求一个参数是modelpart，这里通过Context获得即可和之前一样。
        // 第三个参数是影子的大小，这里传入1f半径。
        super(pContext, new FirstAnimalModel(pContext.bakeLayer(FirstAnimalModel.LAYER_LOCATION)), 1f);
    }

    @Override
    public ResourceLocation getTextureLocation(FirstAnimal pEntity) {
        return new ResourceLocation(ExampleMod.MODID,"textures/entity/first_animal.png");
    }
}

```

生物属性的注册，我们通过EntityAttributeCreationEvent进行注册，

```java
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBus{
        @SubscribeEvent
        public static void setupAttributes(EntityAttributeCreationEvent event) {
            // 第一个参数是你的entityType，第二个参数是你的AttributeSupplier，通过我们写的Builder获得。
            event.put(ModEntityTypes.FIRST_ANIMAL.get(), FirstAnimal.createAttributes().build());
        }

    }
```

照例的实体的render注册。
```java
    @SubscribeEvent
    public static void onClientEvent(FMLClientSetupEvent event){
        event.enqueueWork(()->{
            EntityRenderers.register(ModEntityTypes.FIRST_ANIMAL.get(), FirstAnimalRenderer::new);
        });

    }
/// modelLayer注册。和之前一样。

        @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions evt) {
        evt.registerLayerDefinition(FirstAnimalModel.LAYER_LOCATION,FirstAnimalModel::createBodyLayer);
    }

```

对于实体的注册就自己实现下把。

记得把你的纹理放到对应的位置上去，这样才能生效。