package fuzs.puzzleslib.impl.init;

import fuzs.puzzleslib.api.init.v2.PotionBrewingRegistry;
import fuzs.puzzleslib.mixin.accessor.PotionBrewingForgeAccessor;
import java.util.Objects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;

public final class PotionBrewingRegistryForge implements PotionBrewingRegistry {

    @Override
    public void registerContainerRecipe(PotionItem from, Ingredient ingredient, PotionItem to) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.RuntimeException: Constructor net/minecraft/world/item/alchemy/PotionBrewing$Mix.<init>(Lnet/minecraftforge/registries/IForgeRegistry;Ljava/lang/Object;Lnet/minecraft/world/item/crafting/Ingredient;Ljava/lang/Object;)V not found
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.ExprUtil.getSyntheticParametersMask(ExprUtil.java:49)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.appendParamList(InvocationExprent.java:957)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.NewExprent.toJava(NewExprent.java:460)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.getCastedExprent(ExprProcessor.java:1018)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.AssignmentExprent.toJava(AssignmentExprent.java:154)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.listToJava(ExprProcessor.java:895)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.BasicBlockStatement.toJava(BasicBlockStatement.java:90)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.RootStatement.toJava(RootStatement.java:36)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.ClassWriter.writeMethod(ClassWriter.java:1283)
        //
        // Bytecode:
        // 00: aload 1
        // 01: ldc "from item is null"
        // 03: invokestatic java/util/Objects.requireNonNull (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
        // 06: pop
        // 07: aload 2
        // 08: ldc "ingredient is null"
        // 0a: invokestatic java/util/Objects.requireNonNull (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
        // 0d: pop
        // 0e: aload 3
        // 0f: ldc "to item is null"
        // 11: invokestatic java/util/Objects.requireNonNull (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
        // 14: pop
        // 15: new net/minecraft/world/item/alchemy/PotionBrewing$Mix
        // 18: dup
        // 19: getstatic net/minecraftforge/registries/ForgeRegistries.ITEMS Lnet/minecraftforge/registries/IForgeRegistry;
        // 1c: aload 1
        // 1d: aload 2
        // 1e: aload 3
        // 1f: invokespecial net/minecraft/world/item/alchemy/PotionBrewing$Mix.<init> (Lnet/minecraftforge/registries/IForgeRegistry;Ljava/lang/Object;Lnet/minecraft/world/item/crafting/Ingredient;Ljava/lang/Object;)V
        // 22: astore 4
        // 24: invokestatic fuzs/puzzleslib/mixin/accessor/PotionBrewingForgeAccessor.puzzleslib$getContainerMixes ()Ljava/util/List;
        // 27: aload 4
        // 29: invokeinterface java/util/List.add (Ljava/lang/Object;)Z 2
        // 2e: pop
        // 2f: return
    }

    @Override
    public void registerPotionContainer(PotionItem container) {
        Objects.requireNonNull(container, "container item is null");
        Ingredient ingredient = Ingredient.of(container);
        PotionBrewingForgeAccessor.puzzleslib$getAllowedContainers().add(ingredient);
    }

    @Override
    public void registerPotionRecipe(Potion from, Ingredient ingredient, Potion to) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.RuntimeException: Constructor net/minecraft/world/item/alchemy/PotionBrewing$Mix.<init>(Lnet/minecraftforge/registries/IForgeRegistry;Ljava/lang/Object;Lnet/minecraft/world/item/crafting/Ingredient;Ljava/lang/Object;)V not found
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.ExprUtil.getSyntheticParametersMask(ExprUtil.java:49)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.appendParamList(InvocationExprent.java:957)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.NewExprent.toJava(NewExprent.java:460)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.getCastedExprent(ExprProcessor.java:1018)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.AssignmentExprent.toJava(AssignmentExprent.java:154)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.listToJava(ExprProcessor.java:895)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.BasicBlockStatement.toJava(BasicBlockStatement.java:90)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.RootStatement.toJava(RootStatement.java:36)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.ClassWriter.writeMethod(ClassWriter.java:1283)
        //
        // Bytecode:
        // 00: aload 1
        // 01: ldc "from potion is null"
        // 03: invokestatic java/util/Objects.requireNonNull (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
        // 06: pop
        // 07: aload 2
        // 08: ldc "ingredient is null"
        // 0a: invokestatic java/util/Objects.requireNonNull (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
        // 0d: pop
        // 0e: aload 3
        // 0f: ldc "to potion is null"
        // 11: invokestatic java/util/Objects.requireNonNull (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
        // 14: pop
        // 15: new net/minecraft/world/item/alchemy/PotionBrewing$Mix
        // 18: dup
        // 19: getstatic net/minecraftforge/registries/ForgeRegistries.POTIONS Lnet/minecraftforge/registries/IForgeRegistry;
        // 1c: aload 1
        // 1d: aload 2
        // 1e: aload 3
        // 1f: invokespecial net/minecraft/world/item/alchemy/PotionBrewing$Mix.<init> (Lnet/minecraftforge/registries/IForgeRegistry;Ljava/lang/Object;Lnet/minecraft/world/item/crafting/Ingredient;Ljava/lang/Object;)V
        // 22: astore 4
        // 24: invokestatic fuzs/puzzleslib/mixin/accessor/PotionBrewingForgeAccessor.puzzleslib$getPotionMixes ()Ljava/util/List;
        // 27: aload 4
        // 29: invokeinterface java/util/List.add (Ljava/lang/Object;)Z 2
        // 2e: pop
        // 2f: return
    }
}