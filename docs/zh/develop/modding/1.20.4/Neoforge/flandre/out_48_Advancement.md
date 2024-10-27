---
title: 48 成就
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 48 成就 相关教程
image: ./covers/d2ac8a0065079277912f5e2df2d019b19d62d3d2.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 参考
https://github.dev/VazkiiMods/Botania
## 成就/进度
进度或者说成就，就是游戏里面那些成就，我们这次学习怎么添加成就，这就也是使用的json数据作为驱动的，其中一些原版的检验的条件是你自己的可以直接使用的， 不过我们这里添加一个自己的检验条件。

我们添加这样的一个进度，当你使用我们之前创建的的在维度等级下存储数据的方块时候，就会弹出这个成就。

在开始之前我们说以下，我们写的是一类的触发器即我们上文说的检验条件。这个触发器类似你写的规则，比如，我要添加一系列的成就，不过这些成就都是使用了某个物品就可以触发对应的使用物品成就，所以我们没必要为所有的物品都编写这样的触发器，而是写一个通用的触发器，那么既然这样的我们应该怎么判断是那个物品呢？这里就是使用了上文的JSON驱动了，你需要把对应的匹配的物品写入到JSON中，然后判断传入的物品和JSON序列化读入的物品是否一致就会是否完成这个成就了。

所以我们写的是一系列的规则，原版也正是这样的设计的。不过这里的例子直接采用了硬编码的方式写死了这个成就，对于json驱动的方式可以去看看原版的，这里介绍了大致的流程。

```java
// 进度的触发器，完成自己的触发器，需要你继承SimpleCriterionTrigger类，原版的触发器也同样的继承于这个类的。
// 泛型是一个内部类，这个内部类，应该继承SimpleCriterionTrigger.SimpleInstance的内部类，用来存储实例的。
// 你可以通过继承关系看到原版的触发器
public class GiveAppleTrigger extends SimpleCriterionTrigger<GiveAppleTrigger.Instance> {
    // 这里的trigger就是我们的判断的成就是否完成的条件
    // 如果你的触发器很复杂，需要判断多个条件，比如物品，位置，方块，等等
    // 看有这样写trigger(player,pos,block,item)
    // 之后在this.trigger(player，第二个参数书写你的判断条件，这里要求传入一个lammbda表达式，返回true或者false，true代表触发，false代表不触发。)
    public void trigger(ServerPlayer pPlayer) {
        this.trigger(pPlayer,instance -> true);
    }

    // 我们这里需要返回一个对应的触发情况，也就是读取的json时候的内容是什么。
    // 我这里的json仅仅存储了player这个信息， 但是我们并不需要去处理player相关的内容，所以这里返回的是一个空的Optional。
    // 关于一些其他的判断可以去看看原版的Criterion是怎么写的。
    // 例如如果你要判断是玩家捡起了苹果这个物品，那么你应该这里创建的Instance包含物品，传入对应的苹果。
    // 而于我们为什么可以使用createCriterion这个方法，其实是SimpleCriterionTrigger实现了对应的接口，大家可以去查看源码
    // 下面可以看一个三叉戟成就的例子在下面。
    public static Criterion<GiveAppleTrigger.Instance> giveAppleBlock() {
        return InitTrigger.GIVE_RUBY_APPLE.get().createCriterion(new GiveAppleTrigger.Instance(Optional.empty()));
    }
    // 返回对应的codec，用于反序列化json用的。
    @Override
    public Codec<Instance> codec() {
        return Instance.CODEC;
    }
    // 对应的Instance类
    public static class Instance implements SimpleCriterionTrigger.SimpleInstance {
        // 存储player
        private Optional<ContextAwarePredicate> player;
        public Instance(Optional<ContextAwarePredicate> player) {
            super();
            ///
            this.player = player;
        }

        public static final Codec<GiveAppleTrigger.Instance> CODEC = RecordCodecBuilder.create(
            // 这里的内容对应的你的json中的存储字段，详细看原版的内容
                p_312304_ -> p_312304_.group(
                                ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(GiveAppleTrigger.Instance::player)
                        )
                        .apply(p_312304_, GiveAppleTrigger.Instance::new)
        );
        // 实现impleCriterionTrigger.SimpleInstance该接口要求实现的方法。
        @Override
        public Optional<ContextAwarePredicate> player() {
            return this.player;
        }
    }
}


```

这是三叉戟成就的一个例子，不过这里的触发器却是玩家攻击实体的一个触发器，不过三叉戟攻击实体确实算是玩家攻击实体的一个特例，所以这里的触发器不仅仅可以使用三叉戟你也可以指定其他的武器制作对应的成就。不过这里还是限制了三叉戟，我们可以看看是怎么限制的
```java
// 这个是返回对应的Criterion的方法，指定的类型是DamagePredicate，通过这个DamagePredicate进行限制是三叉戟的攻击，其他两个字段则不做限制，你同样可以限制第一个player字段表示只有某个人可以触发这个成就，或者限制第三个字段，表示攻击某个怪物触发这个成就，或者限制第二个和第三个表示怎么样的伤害某个生物触发。
        public static Criterion<PlayerHurtEntityTrigger.TriggerInstance> playerHurtEntityWithDamage(DamagePredicate.Builder pDamage) {
            return CriteriaTriggers.PLAYER_HURT_ENTITY
                .createCriterion(new PlayerHurtEntityTrigger.TriggerInstance(Optional.empty(), Optional.of(pDamage.build()), Optional.empty()));
        }

// 这里是对应的json的一部分，添加一个条件，就是对应的instance就是TriggerInstance，我们需要使用的是三叉戟才能触发这个成就，所以传入的DamagePredicate对其中的tag和direct进行了限制。
            .addCriterion(
                "shot_trident",
                PlayerHurtEntityTrigger.TriggerInstance.playerHurtEntityWithDamage(
                    DamagePredicate.Builder.damageInstance()
                        .type(
                            DamageSourcePredicate.Builder.damageType()
                                .tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE))
                                .direct(EntityPredicate.Builder.entity().of(EntityType.TRIDENT))
                        )
                )
            )

```

好了，然后关于触发器的注册的内容。

```java


public class InitTrigger {
    // 获得对应的注册器
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, ExampleMod.MODID);
    // 注册
    public static final Supplier<GiveAppleTrigger> GIVE_RUBY_APPLE = register("give_ruby_apple", GiveAppleTrigger::new);
    // 辅助的方法
    private static <T extends CriterionTrigger<?>> DeferredHolder<CriterionTrigger<?>, T> register(String id, Supplier<T> trigger) {
        return TRIGGERS.register(id, trigger);
    }
    // 别忘忘记注册到总线
    public static void register(IEventBus eventBus) {
        TRIGGERS.register(eventBus);
    }
}

```
当然并不是你写了对应的触发器就可以触发成就了，将你的触发器写在对应的触发的位置上，例如我这里希望是在使用我们之前添加的DataSaveBlock方块的时候触发这个成就，我就这样写。

```java

public class DataSaveBlock extends Block {
    public DataSaveBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide){

            if(mainHandItem.isEmpty()){

            }else{

                // ... 
                // 如果你的触发逻辑更加复杂你自然需要更多的参数，你看与这样修改你的trigger方法，并传入数据
                //trigger(pPlayer,pos,item,block.....) 
                InitTrigger.GIVE_RUBY_APPLE.get().trigger((ServerPlayer) pPlayer);
            }
        }
        return InteractionResult.SUCCESS;
    }
}

```
好了，我们去生成我们的对应触发器的一个json文件吧。

这里我们还是使用datagen的方式生成，当然你可以自己写json，就不演示了，相关的内容在wiki也有，可以自行查看。

```java


public class ModAdvancementProvider  {
    // 辅助方法 创建一个provider的实例
    // 其中如果你有多个成绩的列表，就在list中加
    // 例如原版的冒险，种地，等等
    public static net.minecraft.data.advancements.AdvancementProvider create(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        return new net.minecraft.data.advancements.AdvancementProvider(packOutput, lookupProvider, List.of(new ModAdvancements()));
    }
    // 每个subprovider中应该有一个root节点，这里我们就添加了一个root节点，
    // 下面也会有怎么指定父节点往下翻看
    // subprovider需要实现AdvancementSubProvider接口
    public static class ModAdvancements implements AdvancementSubProvider {
        // 实现对应的方法
        // 在这里写你的成就
        @Override
        public void generate(HolderLookup.Provider pRegistries, Consumer<AdvancementHolder> pWriter) {
            // AdvancementHolder是我们的成就的一个对象
            // Advancement.Builder.advancement()来获得一个设计工厂
            // 通过对应的方法设置
            // 和合成表类似
            AdvancementHolder giveAppleAdvancement = Advancement.Builder.advancement()
            // 显示的内容
            // 第一个是图标，成就提示时候的图片
            // 第二个是title。需要你去写对应的语言化json
            // 第三个是描述。需要你去写对应的语言化的json
            // 第四个是root节点特有的，是成就的背景图片，不给的话是紫黑块
            // 第五个是成就类型，这里是任务，其中还有目标和另一个，自己点源码看
                    .display(
                            ModItems.RUBY_APPLE.get(),
                            Component.translatable("advancements.adventure.ruby_apple.title"),
                            Component.translatable("advancements.adventure.ruby_apple.description"),
                            new ResourceLocation("textures/gui/advancements/backgrounds/adventure.png"),
                            AdvancementType.TASK,
                            true, // 显示提示
                            true, // 通知聊天栏
                            false // 是否隐藏
                    )
                    .addCriterion("use_level_data_block", GiveAppleTrigger.giveAppleBlock()) // 添加检查的条件，这里没有条件，关于限制看看上文提到的三叉戟 或者原版的例子
                    .save(pWriter,new ResourceLocation(ExampleMod.MODID,"main/give_ruby_apple").toString()); // save，
                    // 注意，需要传入的是一个string，我们有需要指定自己的命名空间，否则就到了原版的空间了。
                    // 其中最好针对不同的成就页面添加不同的字段，例如这里的mian
                    // 如果你有多个页面，例如你还有一个挑战的页面，这样写
                    // save(pwriter,new R...(modid,"challenge/xxxxxxxxx).tostring())
        }
    }
}

```

下面是原版的一个例子针对了如果是root的子节点怎么写。

```java
 AdvancementHolder advancementholder = Advancement.Builder.advancement()
            .display(
                Items.MAP,
                Component.translatable("advancements.adventure.root.title"),
                Component.translatable("advancements.adventure.root.description"),
                new ResourceLocation("textures/gui/advancements/backgrounds/adventure.png"),
                AdvancementType.TASK,
                false,
                false,
                false
            )
            .requirements(AdvancementRequirements.Strategy.OR)
            .addCriterion("killed_something", KilledTrigger.TriggerInstance.playerKilledEntity())
            .addCriterion("killed_by_something", KilledTrigger.TriggerInstance.entityKilledPlayer())
            .save(pWriter, "adventure/root");
        AdvancementHolder advancementholder1 = Advancement.Builder.advancement()
        /// 这里通过parent指定
            .parent(advancementholder) // 指定的是第一个。
            .display(
                Blocks.RED_BED,
                Component.translatable("advancements.adventure.sleep_in_bed.title"),
                Component.translatable("advancements.adventure.sleep_in_bed.description"),
                null,
                AdvancementType.TASK,
                true,
                true,
                false
            )
            .addCriterion("slept_in_bed", PlayerTrigger.TriggerInstance.sleptInBed())
            .save(pWriter, "adventure/sleep_in_bed");

```

生成的写方法
```java

        event.getGenerator().addProvider(
                event.includeServer(),
                (DataProvider.Factory<net.minecraft.data.advancements.AdvancementProvider>) pOutput -> ModAdvancementProvider.create(pOutput,lp)
        );
```

通过data task就可以生成对应的json文件了。

这里给出原版的一个生成json的类，大家自行查看其他的类。
VanillaAdventureAdvancements类，关于了原版的冒险的成就。

进入游戏测试下把。