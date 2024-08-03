package dev.xkmc.modulargolems.compat.jei;

import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.modulargolems.compat.curio.CurioCompatRegistry;
import dev.xkmc.modulargolems.content.config.GolemMaterial;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.core.GolemType;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.content.item.upgrade.UpgradeItem;
import dev.xkmc.modulargolems.content.menu.config.ToggleGolemConfigScreen;
import dev.xkmc.modulargolems.content.menu.equipment.EquipmentsScreen;
import dev.xkmc.modulargolems.content.menu.filter.ItemConfigScreen;
import dev.xkmc.modulargolems.content.menu.path.PathConfigScreen;
import dev.xkmc.modulargolems.content.menu.tabs.ITabScreen;
import dev.xkmc.modulargolems.content.menu.target.TargetConfigScreen;
import dev.xkmc.modulargolems.content.recipe.GolemAssembleRecipe;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.handlers.IGuiProperties;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.MinecraftForge;

@JeiPlugin
public class GolemJEIPlugin implements IModPlugin {

    public static final ResourceLocation ID = new ResourceLocation("modulargolems", "main");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        for (Item item : GolemPart.LIST) {
            registration.registerSubtypeInterpreter(item, GolemJEIPlugin::partSubtype);
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        GolemMaterialConfig config = GolemMaterialConfig.get();
        List<IJeiAnvilRecipe> recipes = new ArrayList();
        addPartCraftRecipes(recipes, config, registration.getVanillaRecipeFactory());
        addRepairRecipes(recipes, config, registration.getVanillaRecipeFactory());
        addUpgradeRecipes(recipes, config, registration.getVanillaRecipeFactory());
        registration.addRecipes(RecipeTypes.ANVIL, recipes);
        MinecraftForge.EVENT_BUS.post(new CustomRecipeEvent(registration));
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory().addCategoryExtension(GolemAssembleRecipe.class, GolemAssemblyExtension::new);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGhostIngredientHandler(ItemConfigScreen.class, new ItemFilterHandler());
        registration.addGuiScreenHandler(EquipmentsScreen.class, GolemJEIPlugin::create);
        registration.addGuiScreenHandler(ToggleGolemConfigScreen.class, GolemJEIPlugin::create);
        registration.addGuiScreenHandler(ItemConfigScreen.class, GolemJEIPlugin::create);
        registration.addGuiScreenHandler(TargetConfigScreen.class, GolemJEIPlugin::create);
        registration.addGuiScreenHandler(PathConfigScreen.class, GolemJEIPlugin::create);
        CurioCompatRegistry.onJEIRegistry(e -> registration.addGuiScreenHandler((Class<Screen>) Wrappers.cast(e), x$0 -> create((ITabScreen) x$0)));
    }

    @Nullable
    public static IGuiProperties create(ITabScreen screen) {
        if (screen.screenWidth() > 0 && screen.screenHeight() > 0) {
            int x = screen.getGuiLeft();
            int y = screen.getGuiTop();
            int width = screen.getXSize() + 32;
            int height = screen.getYSize();
            return width > 0 && height > 0 ? new GuiProperties(screen.asScreen().getClass(), x, y, width, height, screen.screenWidth(), screen.screenHeight()) : null;
        } else {
            return null;
        }
    }

    private static String partSubtype(ItemStack stack, UidContext ctx) {
        return ((ResourceLocation) GolemPart.getMaterial(stack).orElse(GolemMaterial.EMPTY)).toString();
    }

    private static void addPartCraftRecipes(List<IJeiAnvilRecipe> recipes, GolemMaterialConfig config, IVanillaRecipeFactory factory) {
        for (ResourceLocation mat : config.getAllMaterials()) {
            ItemStack[] arr = ((Ingredient) config.ingredients.get(mat)).getItems();
            boolean special = false;
            for (ItemStack stack : arr) {
                if (stack.is(MGTagGen.SPECIAL_CRAFT)) {
                    special = true;
                    break;
                }
            }
            if (!special) {
                for (GolemPart<?, ?> item : GolemPart.LIST) {
                    List<ItemStack> list = new ArrayList();
                    for (ItemStack stackx : arr) {
                        list.add(new ItemStack(stackx.getItem(), item.count));
                    }
                    recipes.add(factory.createAnvilRecipe(new ItemStack(item), list, List.of(GolemPart.setMaterial(new ItemStack(item), mat))));
                }
            }
        }
    }

    private static void addRepairRecipes(List<IJeiAnvilRecipe> recipes, GolemMaterialConfig config, IVanillaRecipeFactory factory) {
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
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.DoStatement.toJava(DoStatement.java:148)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.jmpWrapper(ExprProcessor.java:833)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.SequenceStatement.toJava(SequenceStatement.java:107)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.stats.RootStatement.toJava(RootStatement.java:36)
        //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.ClassWriter.writeMethod(ClassWriter.java:1283)
        //
        // Bytecode:
        // 000: getstatic dev/xkmc/modulargolems/content/core/GolemType.GOLEM_TYPE_TO_ITEM Ljava/util/HashMap;
        // 003: invokevirtual java/util/HashMap.values ()Ljava/util/Collection;
        // 006: invokeinterface java/util/Collection.iterator ()Ljava/util/Iterator; 1
        // 00b: astore 3
        // 00c: aload 3
        // 00d: invokeinterface java/util/Iterator.hasNext ()Z 1
        // 012: ifeq 11c
        // 015: aload 3
        // 016: invokeinterface java/util/Iterator.next ()Ljava/lang/Object; 1
        // 01b: checkcast dev/xkmc/modulargolems/content/item/golem/GolemHolder
        // 01e: astore 4
        // 020: new java/util/ArrayList
        // 023: dup
        // 024: invokespecial java/util/ArrayList.<init> ()V
        // 027: astore 5
        // 029: new java/util/ArrayList
        // 02c: dup
        // 02d: invokespecial java/util/ArrayList.<init> ()V
        // 030: astore 6
        // 032: new java/util/ArrayList
        // 035: dup
        // 036: invokespecial java/util/ArrayList.<init> ()V
        // 039: astore 7
        // 03b: aload 1
        // 03c: invokevirtual dev/xkmc/modulargolems/content/config/GolemMaterialConfig.getAllMaterials ()Ljava/util/Set;
        // 03f: invokeinterface java/util/Set.iterator ()Ljava/util/Iterator; 1
        // 044: astore 8
        // 046: aload 8
        // 048: invokeinterface java/util/Iterator.hasNext ()Z 1
        // 04d: ifeq 106
        // 050: aload 8
        // 052: invokeinterface java/util/Iterator.next ()Ljava/lang/Object; 1
        // 057: checkcast net/minecraft/resources/ResourceLocation
        // 05a: astore 9
        // 05c: new net/minecraft/world/item/ItemStack
        // 05f: dup
        // 060: aload 4
        // 062: invokespecial net/minecraft/world/item/ItemStack.<init> (Lnet/minecraft/world/level/ItemLike;)V
        // 065: astore 10
        // 067: aload 4
        // 069: invokevirtual dev/xkmc/modulargolems/content/item/golem/GolemHolder.getEntityType ()Ldev/xkmc/modulargolems/content/core/GolemType;
        // 06c: invokevirtual dev/xkmc/modulargolems/content/core/GolemType.values ()[Ldev/xkmc/modulargolems/content/core/IGolemPart;
        // 06f: astore 11
        // 071: aload 11
        // 073: arraylength
        // 074: istore 12
        // 076: bipush 0
        // 077: istore 13
        // 079: iload 13
        // 07b: iload 12
        // 07d: if_icmpge 09b
        // 080: aload 11
        // 082: iload 13
        // 084: aaload
        // 085: astore 14
        // 087: aload 10
        // 089: aload 14
        // 08b: invokeinterface dev/xkmc/modulargolems/content/core/IGolemPart.toItem ()Ldev/xkmc/modulargolems/content/item/golem/GolemPart; 1
        // 090: aload 9
        // 092: invokestatic dev/xkmc/modulargolems/content/item/golem/GolemHolder.addMaterial (Lnet/minecraft/world/item/ItemStack;Ldev/xkmc/modulargolems/content/item/golem/GolemPart;Lnet/minecraft/resources/ResourceLocation;)V
        // 095: iinc 13 1
        // 098: goto 079
        // 09b: aload 10
        // 09d: invokevirtual net/minecraft/world/item/ItemStack.m_41777_ ()Lnet/minecraft/world/item/ItemStack;
        // 0a0: astore 11
        // 0a2: aload 11
        // 0a4: invokevirtual net/minecraft/world/item/ItemStack.m_41784_ ()Lnet/minecraft/nbt/CompoundTag;
        // 0a7: ldc_w "golem_display"
        // 0aa: ldc_w 0.75
        // 0ad: invokevirtual net/minecraft/nbt/CompoundTag.m_128350_ (Ljava/lang/String;F)V
        // 0b0: aload 5
        // 0b2: aload 11
        // 0b4: invokeinterface java/util/List.add (Ljava/lang/Object;)Z 2
        // 0b9: pop
        // 0ba: aload 1
        // 0bb: getfield dev/xkmc/modulargolems/content/config/GolemMaterialConfig.ingredients Ljava/util/HashMap;
        // 0be: aload 9
        // 0c0: invokevirtual java/util/HashMap.get (Ljava/lang/Object;)Ljava/lang/Object;
        // 0c3: checkcast net/minecraft/world/item/crafting/Ingredient
        // 0c6: invokevirtual net/minecraft/world/item/crafting/Ingredient.m_43908_ ()[Lnet/minecraft/world/item/ItemStack;
        // 0c9: astore 12
        // 0cb: aload 6
        // 0cd: new net/minecraft/world/item/ItemStack
        // 0d0: dup
        // 0d1: aload 12
        // 0d3: arraylength
        // 0d4: ifle 0e1
        // 0d7: aload 12
        // 0d9: bipush 0
        // 0da: aaload
        // 0db: invokevirtual net/minecraft/world/item/ItemStack.m_41720_ ()Lnet/minecraft/world/item/Item;
        // 0de: goto 0e4
        // 0e1: getstatic net/minecraft/world/item/Items.f_42127_ Lnet/minecraft/world/item/Item;
        // 0e4: invokespecial net/minecraft/world/item/ItemStack.<init> (Lnet/minecraft/world/level/ItemLike;)V
        // 0e7: invokeinterface java/util/List.add (Ljava/lang/Object;)Z 2
        // 0ec: pop
        // 0ed: aload 10
        // 0ef: invokevirtual net/minecraft/world/item/ItemStack.m_41784_ ()Lnet/minecraft/nbt/CompoundTag;
        // 0f2: ldc_w "golem_display"
        // 0f5: fconst_1
        // 0f6: invokevirtual net/minecraft/nbt/CompoundTag.m_128350_ (Ljava/lang/String;F)V
        // 0f9: aload 7
        // 0fb: aload 10
        // 0fd: invokeinterface java/util/List.add (Ljava/lang/Object;)Z 2
        // 102: pop
        // 103: goto 046
        // 106: aload 0
        // 107: aload 2
        // 108: aload 5
        // 10a: aload 6
        // 10c: aload 7
        // 10e: invokeinterface mezz/jei/api/recipe/vanilla/IVanillaRecipeFactory.createAnvilRecipe (Ljava/util/List;Ljava/util/List;Ljava/util/List;)Lmezz/jei/api/recipe/vanilla/IJeiAnvilRecipe; 4
        // 113: invokeinterface java/util/List.add (Ljava/lang/Object;)Z 2
        // 118: pop
        // 119: goto 00c
        // 11c: return
    }

    private static void addUpgradeRecipes(List<IJeiAnvilRecipe> recipes, GolemMaterialConfig config, IVanillaRecipeFactory factory) {
        for (UpgradeItem item : UpgradeItem.LIST) {
            List<ItemStack> input = new ArrayList();
            List<ItemStack> material = new ArrayList();
            List<ItemStack> result = new ArrayList();
            for (GolemHolder<?, ?> types : GolemType.GOLEM_TYPE_TO_ITEM.values()) {
                input.add(new ItemStack(types));
            }
            material.add(new ItemStack(item));
            for (GolemHolder<?, ?> types : GolemType.GOLEM_TYPE_TO_ITEM.values()) {
                result.add(GolemHolder.addUpgrade(new ItemStack(types), item));
            }
            recipes.add(factory.createAnvilRecipe(input, material, result));
        }
    }
}