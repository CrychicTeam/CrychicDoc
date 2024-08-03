package io.redspace.ironsspellbooks.registries;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PotionRegistry {

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, "irons_spellbooks");

    public static final RegistryObject<Potion> INSTANT_MANA_ONE = POTIONS.register("instant_mana_one", () -> new Potion("mana", new MobEffectInstance(MobEffectRegistry.INSTANT_MANA.get())));

    public static final RegistryObject<Potion> INSTANT_MANA_TWO = POTIONS.register("instant_mana_two", () -> new Potion("mana", new MobEffectInstance(MobEffectRegistry.INSTANT_MANA.get(), 0, 1)));

    public static final RegistryObject<Potion> INSTANT_MANA_THREE = POTIONS.register("instant_mana_three", () -> new Potion("mana", new MobEffectInstance(MobEffectRegistry.INSTANT_MANA.get(), 0, 2)));

    public static final RegistryObject<Potion> INSTANT_MANA_FOUR = POTIONS.register("instant_mana_four", () -> new Potion("mana", new MobEffectInstance(MobEffectRegistry.INSTANT_MANA.get(), 0, 3)));

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
        eventBus.addListener(PotionRegistry::addRecipes);
    }

    public static void addRecipes(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PotionBrewing.addMix(Potions.AWKWARD, ItemRegistry.ARCANE_ESSENCE.get(), INSTANT_MANA_ONE.get());
            PotionBrewing.addMix(INSTANT_MANA_ONE.get(), Items.GLOWSTONE_DUST, INSTANT_MANA_TWO.get());
            PotionBrewing.addMix(INSTANT_MANA_TWO.get(), Items.AMETHYST_SHARD, INSTANT_MANA_THREE.get());
            PotionBrewing.addMix(INSTANT_MANA_THREE.get(), Items.AMETHYST_CLUSTER, INSTANT_MANA_FOUR.get());
        });
    }

    public static void addContainerMix(Item pFrom, Item pIngredient, Item pTo) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.RuntimeException: Constructor net/minecraft/world/item/alchemy/PotionBrewing$Mix.<init>(Lnet/minecraftforge/registries/IForgeRegistry;Ljava/lang/Object;Lnet/minecraft/world/item/crafting/Ingredient;Ljava/lang/Object;)V not found
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.ExprUtil.getSyntheticParametersMask(ExprUtil.java:49)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.appendParamList(InvocationExprent.java:957)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.NewExprent.toJava(NewExprent.java:460)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.getCastedExprent(ExprProcessor.java:1018)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.appendParamList(InvocationExprent.java:1153)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.toJava(InvocationExprent.java:902)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.listToJava(ExprProcessor.java:895)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.BasicBlockStatement.toJava(BasicBlockStatement.java:90)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.RootStatement.toJava(RootStatement.java:36)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.ClassWriter.writeMethod(ClassWriter.java:1283)
        //
        // Bytecode:
        // 00: getstatic net/minecraft/world/item/alchemy/PotionBrewing.f_43495_ Ljava/util/List;
        // 03: new net/minecraft/world/item/alchemy/PotionBrewing$Mix
        // 06: dup
        // 07: getstatic net/minecraftforge/registries/ForgeRegistries.ITEMS Lnet/minecraftforge/registries/IForgeRegistry;
        // 0a: aload 0
        // 0b: bipush 1
        // 0c: anewarray 96
        // 0f: dup
        // 10: bipush 0
        // 11: aload 1
        // 12: aastore
        // 13: invokestatic net/minecraft/world/item/crafting/Ingredient.m_43929_ ([Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/crafting/Ingredient;
        // 16: aload 2
        // 17: invokespecial net/minecraft/world/item/alchemy/PotionBrewing$Mix.<init> (Lnet/minecraftforge/registries/IForgeRegistry;Ljava/lang/Object;Lnet/minecraft/world/item/crafting/Ingredient;Ljava/lang/Object;)V
        // 1a: invokeinterface java/util/List.add (Ljava/lang/Object;)Z 2
        // 1f: pop
        // 20: return
    }
}