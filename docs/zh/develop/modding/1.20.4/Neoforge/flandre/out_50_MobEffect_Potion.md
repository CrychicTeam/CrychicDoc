---
title: 50 药水效果和药水
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 50 药水效果和药水 相关教程
image: ./covers/48d60f5288b3399f306b7392b030aa3e7f3b2767.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 药水效果和药水

类位置
java/net/flandre923/examplemod/potion/ModPotions.java
java/net/flandre923/examplemod/effect/ModEffects.java
java/net/flandre923/examplemod/effect/ExampleEffect.java

药水效果和附魔有些类似，仅仅定义了效果，具体的生效方法在具体的情景中，例如增加伤害的效果应该在对于的攻击的代码中检查是否有该效果，根据效果等级和效果进行攻击力的加成，当然也有一些药水效果是不需要这样的，可以直接生效，这一部分是例如生命恢复这样的效果和一部分是瞬间伤害这样的药水效果。

这次我们模仿原版的生命恢复的药水来看看具体怎么实现一个药水的效果和对于的药水。

大家应该注意到了我有意识的区分了药水效果和药水，在代码中这是分两类的， 游戏表现也确实是这样的，例如原版的漂浮效果是没有对应的药水效果的。

我们先来是添加药水效果

```java
// 原版的药水效果是这个MobEffect类来表示了，我们继承这个类，你可以去看看继承树寻找原版的是实现
public class ExampleEffect extends MobEffect {
    // 第一个是分类，表示药水效果是好的还是坏的，第二个是药水粒子效果的颜色是否正常
    protected ExampleEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }
    // 这里是具体的效果，这里我们模仿原版的生命恢复的效果，每5秒恢复2点生命值
    @Override
    public void applyEffectTick(LivingEntity pEntity, int pAmplifier) {
        super.applyEffectTick(pEntity, pAmplifier);
        if (pEntity.getHealth() < pEntity.getMaxHealth()) {
            pEntity.heal(2.0F);
        }
    }
    // 这里是判断是否应该应用效果的，这里我们每5秒应用一次效果
    @Override
    public boolean shouldApplyEffectTickThisTick(int tick, int amp) {
        if (tick % 5 == 0) {
            return true;
        }else{
            return false;
        }
    }
}


```
然后，要物品和方块一样，药水效果需要注册，我们在ModEffects类中注册一下，和其他的类似就不多说了。

```java

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOD_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, ExampleMod.MODID);
        // 第二个参数是颜色。自己选一个颜色。
        // 应该是用十进制表示的16进制吧，大家自己试试
    public static final Supplier<MobEffect> EXAMPLE_EFFECT = register("example_effect", ()->new ExampleEffect(MobEffectCategory.BENEFICIAL, 16262179));

    public static <T extends MobEffect> DeferredHolder<MobEffect, T> register(String name, Supplier<T> effect){
        return MOD_EFFECTS.register(name, effect);
    }
    public static void register(IEventBus eventBus){
        MOD_EFFECTS.register(eventBus);
    }
}
```
到这里你已经可以进入游戏中使用命令行的方式给你自己附加这个药水效果了。这个效果是每5Tick恢复2点生命值，不过你会发现你的效果的图标是一个黑紫贴图，具体的贴图的放的位置是:
resources/assets/examplemod/textures/mob_effect/example_effect.png  
注意的png的名称和你的注册的名称一致，examplemod改为你的modid

下面我注册我们的药水效果.和其他的类似就不过多介绍了。

```java

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, ExampleMod.MODID);
    // 这里的参数说一下，第一个是效果，第二个是持续时间tick，第三个是等级 0 是1级，1是2级。
    public static final DeferredHolder<Potion,Potion> EXAMPLE_POTION = register("example_potion", () -> new Potion("example_potion",new MobEffectInstance(ModEffects.EXAMPLE_EFFECT.get(),1200,1)));

    public static <T extends Potion> DeferredHolder<Potion,T> register(String name, Supplier<T> supplier){
        return POTIONS.register(name, supplier);
    }

    public static void register(IEventBus eventBus){
        POTIONS.register(eventBus);
    }
}


```

好了这样子药水也添加好了，可以进入游戏中查看了，至于药水合成表的添加就留给大家自己探索了。
