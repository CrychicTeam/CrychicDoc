package dev.xkmc.modulargolems.compat.jei;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.modulargolems.content.config.GolemMaterial;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.content.recipe.GolemAssembleRecipe;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record GolemAssemblyExtension(GolemAssembleRecipe recipe) implements ICraftingCategoryExtension {

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
        Optional<IFocus<ItemStack>> out = focuses.getItemStackFocuses(RecipeIngredientRole.OUTPUT).findAny();
        if (out.isPresent()) {
            ItemStack outStack = (ItemStack) ((IFocus) out.get()).getTypedValue().getIngredient();
            this.setRecipeSpecial(builder, craftingGridHelper, outStack);
        } else {
            this.setRecipeAll(builder, craftingGridHelper, focuses);
        }
    }

    private void setRecipeAll(IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.NullPointerException: Cannot invoke "org.jetbrains.java.decompiler.struct.gen.VarType.isGeneric()" because "newRet" is null
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.InvocationExprent.getInferredExprType(InvocationExprent.java:634)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.DoStatement.toJava(DoStatement.java:146)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.jmpWrapper(ExprProcessor.java:833)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.SequenceStatement.toJava(SequenceStatement.java:107)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.jmpWrapper(ExprProcessor.java:833)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.DoStatement.toJava(DoStatement.java:148)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.jmpWrapper(ExprProcessor.java:833)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.SequenceStatement.toJava(SequenceStatement.java:107)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.jmpWrapper(ExprProcessor.java:833)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.IfStatement.toJava(IfStatement.java:241)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.jmpWrapper(ExprProcessor.java:833)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.SequenceStatement.toJava(SequenceStatement.java:107)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.RootStatement.toJava(RootStatement.java:36)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.ClassWriter.writeMethod(ClassWriter.java:1283)
        //
        // Bytecode:
        // 000: new java/util/ArrayList
        // 003: dup
        // 004: invokespecial java/util/ArrayList.<init> ()V
        // 007: astore 4
        // 009: aload 0
        // 00a: getfield dev/xkmc/modulargolems/compat/jei/GolemAssemblyExtension.recipe Ldev/xkmc/modulargolems/content/recipe/GolemAssembleRecipe;
        // 00d: invokevirtual dev/xkmc/modulargolems/content/recipe/GolemAssembleRecipe.m_7527_ ()Lnet/minecraft/core/NonNullList;
        // 010: invokevirtual net/minecraft/core/NonNullList.iterator ()Ljava/util/Iterator;
        // 013: astore 5
        // 015: aload 5
        // 017: invokeinterface java/util/Iterator.hasNext ()Z 1
        // 01c: ifeq 0b7
        // 01f: aload 5
        // 021: invokeinterface java/util/Iterator.next ()Ljava/lang/Object; 1
        // 026: checkcast net/minecraft/world/item/crafting/Ingredient
        // 029: astore 6
        // 02b: aload 6
        // 02d: invokevirtual net/minecraft/world/item/crafting/Ingredient.m_43908_ ()[Lnet/minecraft/world/item/ItemStack;
        // 030: astore 7
        // 032: aload 7
        // 034: arraylength
        // 035: bipush 1
        // 036: if_icmpne 0a7
        // 039: aload 7
        // 03b: bipush 0
        // 03c: aaload
        // 03d: invokevirtual net/minecraft/world/item/ItemStack.m_41720_ ()Lnet/minecraft/world/item/Item;
        // 040: astore 9
        // 042: aload 9
        // 044: instanceof dev/xkmc/modulargolems/content/item/golem/GolemPart
        // 047: ifeq 0a7
        // 04a: aload 9
        // 04c: checkcast dev/xkmc/modulargolems/content/item/golem/GolemPart
        // 04f: astore 8
        // 051: new java/util/ArrayList
        // 054: dup
        // 055: invokespecial java/util/ArrayList.<init> ()V
        // 058: astore 9
        // 05a: invokestatic dev/xkmc/modulargolems/content/config/GolemMaterialConfig.get ()Ldev/xkmc/modulargolems/content/config/GolemMaterialConfig;
        // 05d: invokevirtual dev/xkmc/modulargolems/content/config/GolemMaterialConfig.getAllMaterials ()Ljava/util/Set;
        // 060: invokeinterface java/util/Set.iterator ()Ljava/util/Iterator; 1
        // 065: astore 10
        // 067: aload 10
        // 069: invokeinterface java/util/Iterator.hasNext ()Z 1
        // 06e: ifeq 09a
        // 071: aload 10
        // 073: invokeinterface java/util/Iterator.next ()Ljava/lang/Object; 1
        // 078: checkcast net/minecraft/resources/ResourceLocation
        // 07b: astore 11
        // 07d: new net/minecraft/world/item/ItemStack
        // 080: dup
        // 081: aload 8
        // 083: invokespecial net/minecraft/world/item/ItemStack.<init> (Lnet/minecraft/world/level/ItemLike;)V
        // 086: astore 12
        // 088: aload 9
        // 08a: aload 12
        // 08c: aload 11
        // 08e: invokestatic dev/xkmc/modulargolems/content/item/golem/GolemPart.setMaterial (Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/world/item/ItemStack;
        // 091: invokeinterface java/util/List.add (Ljava/lang/Object;)Z 2
        // 096: pop
        // 097: goto 067
        // 09a: aload 4
        // 09c: aload 9
        // 09e: invokeinterface java/util/List.add (Ljava/lang/Object;)Z 2
        // 0a3: pop
        // 0a4: goto 0b4
        // 0a7: aload 4
        // 0a9: aload 7
        // 0ab: invokestatic java/util/List.of ([Ljava/lang/Object;)Ljava/util/List;
        // 0ae: invokeinterface java/util/List.add (Ljava/lang/Object;)Z 2
        // 0b3: pop
        // 0b4: goto 015
        // 0b7: aload 0
        // 0b8: getfield dev/xkmc/modulargolems/compat/jei/GolemAssemblyExtension.recipe Ldev/xkmc/modulargolems/content/recipe/GolemAssembleRecipe;
        // 0bb: invokestatic dev/xkmc/l2library/util/Proxy.getClientWorld ()Lnet/minecraft/client/multiplayer/ClientLevel;
        // 0be: invokevirtual net/minecraft/client/multiplayer/ClientLevel.m_9598_ ()Lnet/minecraft/core/RegistryAccess;
        // 0c1: invokevirtual dev/xkmc/modulargolems/content/recipe/GolemAssembleRecipe.m_8043_ (Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;
        // 0c4: astore 5
        // 0c6: new java/util/ArrayList
        // 0c9: dup
        // 0ca: invokespecial java/util/ArrayList.<init> ()V
        // 0cd: astore 6
        // 0cf: aload 5
        // 0d1: invokevirtual net/minecraft/world/item/ItemStack.m_41720_ ()Lnet/minecraft/world/item/Item;
        // 0d4: astore 8
        // 0d6: aload 8
        // 0d8: instanceof dev/xkmc/modulargolems/content/item/golem/GolemHolder
        // 0db: ifeq 1e2
        // 0de: aload 8
        // 0e0: checkcast dev/xkmc/modulargolems/content/item/golem/GolemHolder
        // 0e3: astore 7
        // 0e5: aconst_null
        // 0e6: astore 8
        // 0e8: aload 3
        // 0e9: getstatic mezz/jei/api/recipe/RecipeIngredientRole.INPUT Lmezz/jei/api/recipe/RecipeIngredientRole;
        // 0ec: invokeinterface mezz/jei/api/recipe/IFocusGroup.getItemStackFocuses (Lmezz/jei/api/recipe/RecipeIngredientRole;)Ljava/util/stream/Stream; 2
        // 0f1: invokeinterface java/util/stream/Stream.findAny ()Ljava/util/Optional; 1
        // 0f6: astore 9
        // 0f8: aload 9
        // 0fa: invokevirtual java/util/Optional.isPresent ()Z
        // 0fd: ifeq 14b
        // 100: aload 9
        // 102: invokevirtual java/util/Optional.get ()Ljava/lang/Object;
        // 105: checkcast mezz/jei/api/recipe/IFocus
        // 108: invokeinterface mezz/jei/api/recipe/IFocus.getTypedValue ()Lmezz/jei/api/ingredients/ITypedIngredient; 1
        // 10d: invokeinterface mezz/jei/api/ingredients/ITypedIngredient.getIngredient ()Ljava/lang/Object; 1
        // 112: checkcast net/minecraft/world/item/ItemStack
        // 115: astore 10
        // 117: aload 10
        // 119: invokevirtual net/minecraft/world/item/ItemStack.m_41720_ ()Lnet/minecraft/world/item/Item;
        // 11c: astore 12
        // 11e: aload 12
        // 120: instanceof dev/xkmc/modulargolems/content/item/golem/GolemPart
        // 123: ifeq 14b
        // 126: aload 12
        // 128: checkcast dev/xkmc/modulargolems/content/item/golem/GolemPart
        // 12b: astore 11
        // 12d: aload 10
        // 12f: invokestatic dev/xkmc/modulargolems/content/item/golem/GolemPart.getMaterial (Lnet/minecraft/world/item/ItemStack;)Ljava/util/Optional;
        // 132: astore 12
        // 134: aload 12
        // 136: invokevirtual java/util/Optional.isPresent ()Z
        // 139: ifeq 14b
        // 13c: aload 11
        // 13e: aload 12
        // 140: invokevirtual java/util/Optional.get ()Ljava/lang/Object;
        // 143: checkcast net/minecraft/resources/ResourceLocation
        // 146: invokestatic com/mojang/datafixers/util/Pair.of (Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        // 149: astore 8
        // 14b: invokestatic dev/xkmc/modulargolems/content/config/GolemMaterialConfig.get ()Ldev/xkmc/modulargolems/content/config/GolemMaterialConfig;
        // 14e: invokevirtual dev/xkmc/modulargolems/content/config/GolemMaterialConfig.getAllMaterials ()Ljava/util/Set;
        // 151: invokeinterface java/util/Set.iterator ()Ljava/util/Iterator; 1
        // 156: astore 10
        // 158: aload 10
        // 15a: invokeinterface java/util/Iterator.hasNext ()Z 1
        // 15f: ifeq 1df
        // 162: aload 10
        // 164: invokeinterface java/util/Iterator.next ()Ljava/lang/Object; 1
        // 169: checkcast net/minecraft/resources/ResourceLocation
        // 16c: astore 11
        // 16e: new net/minecraft/world/item/ItemStack
        // 171: dup
        // 172: aload 7
        // 174: invokespecial net/minecraft/world/item/ItemStack.<init> (Lnet/minecraft/world/level/ItemLike;)V
        // 177: astore 12
        // 179: aload 7
        // 17b: invokevirtual dev/xkmc/modulargolems/content/item/golem/GolemHolder.getEntityType ()Ldev/xkmc/modulargolems/content/core/GolemType;
        // 17e: invokevirtual dev/xkmc/modulargolems/content/core/GolemType.values ()[Ldev/xkmc/modulargolems/content/core/IGolemPart;
        // 181: astore 13
        // 183: aload 13
        // 185: arraylength
        // 186: istore 14
        // 188: bipush 0
        // 189: istore 15
        // 18b: iload 15
        // 18d: iload 14
        // 18f: if_icmpge 1d2
        // 192: aload 13
        // 194: iload 15
        // 196: aaload
        // 197: astore 16
        // 199: aload 16
        // 19b: invokeinterface dev/xkmc/modulargolems/content/core/IGolemPart.toItem ()Ldev/xkmc/modulargolems/content/item/golem/GolemPart; 1
        // 1a0: astore 17
        // 1a2: aload 8
        // 1a4: ifnull 1c3
        // 1a7: aload 8
        // 1a9: invokevirtual com/mojang/datafixers/util/Pair.getFirst ()Ljava/lang/Object;
        // 1ac: aload 17
        // 1ae: if_acmpne 1c3
        // 1b1: aload 12
        // 1b3: aload 17
        // 1b5: aload 8
        // 1b7: invokevirtual com/mojang/datafixers/util/Pair.getSecond ()Ljava/lang/Object;
        // 1ba: checkcast net/minecraft/resources/ResourceLocation
        // 1bd: invokestatic dev/xkmc/modulargolems/content/item/golem/GolemHolder.addMaterial (Lnet/minecraft/world/item/ItemStack;Ldev/xkmc/modulargolems/content/item/golem/GolemPart;Lnet/minecraft/resources/ResourceLocation;)V
        // 1c0: goto 1cc
        // 1c3: aload 12
        // 1c5: aload 17
        // 1c7: aload 11
        // 1c9: invokestatic dev/xkmc/modulargolems/content/item/golem/GolemHolder.addMaterial (Lnet/minecraft/world/item/ItemStack;Ldev/xkmc/modulargolems/content/item/golem/GolemPart;Lnet/minecraft/resources/ResourceLocation;)V
        // 1cc: iinc 15 1
        // 1cf: goto 18b
        // 1d2: aload 6
        // 1d4: aload 12
        // 1d6: invokeinterface java/util/List.add (Ljava/lang/Object;)Z 2
        // 1db: pop
        // 1dc: goto 158
        // 1df: goto 1ec
        // 1e2: aload 6
        // 1e4: aload 5
        // 1e6: invokeinterface java/util/List.add (Ljava/lang/Object;)Z 2
        // 1eb: pop
        // 1ec: aload 0
        // 1ed: invokevirtual dev/xkmc/modulargolems/compat/jei/GolemAssemblyExtension.getWidth ()I
        // 1f0: istore 7
        // 1f2: aload 0
        // 1f3: invokevirtual dev/xkmc/modulargolems/compat/jei/GolemAssemblyExtension.getHeight ()I
        // 1f6: istore 8
        // 1f8: aload 2
        // 1f9: aload 1
        // 1fa: aload 6
        // 1fc: invokeinterface mezz/jei/api/gui/ingredient/ICraftingGridHelper.createAndSetOutputs (Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Ljava/util/List;)Lmezz/jei/api/gui/builder/IRecipeSlotBuilder; 3
        // 201: pop
        // 202: aload 2
        // 203: aload 1
        // 204: aload 4
        // 206: iload 7
        // 208: iload 8
        // 20a: invokeinterface mezz/jei/api/gui/ingredient/ICraftingGridHelper.createAndSetInputs (Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Ljava/util/List;II)Ljava/util/List; 5
        // 20f: pop
        // 210: return
    }

    private void setRecipeSpecial(IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, ItemStack focusResult) {
        ArrayList<GolemMaterial> mats = GolemHolder.getMaterial(focusResult);
        List<List<ItemStack>> inputs = new ArrayList();
        int ind = 0;
        for (Ingredient ing : this.recipe.m_7527_()) {
            ItemStack[] stacks = ing.getItems();
            if (stacks.length == 1 && stacks[0].getItem() instanceof GolemPart<?, ?> part) {
                GolemMaterial mat = (GolemMaterial) mats.get(ind++);
                inputs.add(List.of(GolemPart.setMaterial(new ItemStack(part), mat.id())));
            } else {
                inputs.add(List.of(stacks));
            }
        }
        int width = this.getWidth();
        int height = this.getHeight();
        craftingGridHelper.createAndSetOutputs(builder, List.of(focusResult));
        craftingGridHelper.createAndSetInputs(builder, inputs, width, height);
    }

    @Override
    public int getWidth() {
        return this.recipe.m_44220_();
    }

    @Override
    public int getHeight() {
        return this.recipe.m_44221_();
    }
}