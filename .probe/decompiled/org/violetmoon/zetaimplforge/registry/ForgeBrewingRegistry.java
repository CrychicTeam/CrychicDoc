package org.violetmoon.zetaimplforge.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZCommonSetup;
import org.violetmoon.zeta.registry.BrewingRegistry;
import org.violetmoon.zetaimplforge.ForgeZeta;
import org.violetmoon.zetaimplforge.mixin.mixins.AccessorPotionBrewing;

public class ForgeBrewingRegistry extends BrewingRegistry {

    private List<ForgeBrewingRegistry.DelayedPotion> delayedPotions = new ArrayList();

    private boolean okToRegisterImmediately = false;

    public ForgeBrewingRegistry(ForgeZeta zeta) {
        super(zeta);
    }

    @Override
    public void addBrewingRecipe(Potion input, Supplier<Ingredient> reagentSupplier, Potion output) {
        ForgeBrewingRegistry.DelayedPotion d = new ForgeBrewingRegistry.DelayedPotion(input, reagentSupplier, output);
        if (this.okToRegisterImmediately) {
            d.register();
        } else {
            this.delayedPotions.add(d);
        }
    }

    @LoadEvent
    public void commonSetup(ZCommonSetup event) {
        event.enqueueWork(() -> {
            this.okToRegisterImmediately = true;
            this.delayedPotions.forEach(ForgeBrewingRegistry.DelayedPotion::register);
            this.delayedPotions = null;
        });
    }

    private static record DelayedPotion(Potion input, Supplier<Ingredient> reagentSupplier, Potion output) {

        void register() {
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
            // 00: invokestatic org/violetmoon/zetaimplforge/mixin/mixins/AccessorPotionBrewing.zeta$getPotionMixes ()Ljava/util/List;
            // 03: new net/minecraft/world/item/alchemy/PotionBrewing$Mix
            // 06: dup
            // 07: getstatic net/minecraftforge/registries/ForgeRegistries.POTIONS Lnet/minecraftforge/registries/IForgeRegistry;
            // 0a: aload 0
            // 0b: getfield org/violetmoon/zetaimplforge/registry/ForgeBrewingRegistry$DelayedPotion.input Lnet/minecraft/world/item/alchemy/Potion;
            // 0e: aload 0
            // 0f: getfield org/violetmoon/zetaimplforge/registry/ForgeBrewingRegistry$DelayedPotion.reagentSupplier Ljava/util/function/Supplier;
            // 12: invokeinterface java/util/function/Supplier.get ()Ljava/lang/Object; 1
            // 17: checkcast net/minecraft/world/item/crafting/Ingredient
            // 1a: aload 0
            // 1b: getfield org/violetmoon/zetaimplforge/registry/ForgeBrewingRegistry$DelayedPotion.output Lnet/minecraft/world/item/alchemy/Potion;
            // 1e: invokespecial net/minecraft/world/item/alchemy/PotionBrewing$Mix.<init> (Lnet/minecraftforge/registries/IForgeRegistry;Ljava/lang/Object;Lnet/minecraft/world/item/crafting/Ingredient;Ljava/lang/Object;)V
            // 21: invokeinterface java/util/List.add (Ljava/lang/Object;)Z 2
            // 26: pop
            // 27: return
        }
    }
}