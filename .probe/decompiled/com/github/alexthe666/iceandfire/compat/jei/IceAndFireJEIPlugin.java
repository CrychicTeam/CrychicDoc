package com.github.alexthe666.iceandfire.compat.jei;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.compat.jei.firedragonforge.FireDragonForgeCategory;
import com.github.alexthe666.iceandfire.compat.jei.icedragonforge.IceDragonForgeCategory;
import com.github.alexthe666.iceandfire.compat.jei.lightningdragonforge.LightningDragonForgeCategory;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
import com.github.alexthe666.iceandfire.recipe.IafRecipeRegistry;
import java.util.List;
import java.util.stream.Collectors;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class IceAndFireJEIPlugin implements IModPlugin {

    public static final ResourceLocation MOD = new ResourceLocation("iceandfire:iceandfire");

    public static final RecipeType<DragonForgeRecipe> FIRE_DRAGON_FORGE_RECIPE_TYPE = RecipeType.create("minecraft", "firedragonforge", DragonForgeRecipe.class);

    public static final RecipeType<DragonForgeRecipe> ICE_DRAGON_FORGE_RECIPE_TYPE = RecipeType.create("minecraft", "icedragonforge", DragonForgeRecipe.class);

    public static final RecipeType<DragonForgeRecipe> LIGHTNING_DRAGON_FORGE_RECIPE_TYPE = RecipeType.create("minecraft", "lightningdragonforge", DragonForgeRecipe.class);

    public static final ResourceLocation FIRE_DRAGON_FORGE_ID = new ResourceLocation("iceandfire:fire_dragon_forge");

    public static final ResourceLocation ICE_DRAGON_FORGE_ID = new ResourceLocation("iceandfire:ice_dragon_forge");

    public static final ResourceLocation LIGHTNING_DRAGON_FORGE_ID = new ResourceLocation("iceandfire:lightning_dragon_forge");

    private void addDescription(IRecipeRegistration registry, ItemStack itemStack) {
        registry.addIngredientInfo(itemStack, VanillaTypes.ITEM_STACK, Component.translatable(itemStack.getDescriptionId() + ".jei_desc"));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        List<DragonForgeRecipe> forgeRecipeList = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(IafRecipeRegistry.DRAGON_FORGE_TYPE.get());
        List<DragonForgeRecipe> fire = (List<DragonForgeRecipe>) forgeRecipeList.stream().filter(item -> item.getDragonType().equals("fire")).collect(Collectors.toList());
        List<DragonForgeRecipe> ice = (List<DragonForgeRecipe>) forgeRecipeList.stream().filter(item -> item.getDragonType().equals("ice")).collect(Collectors.toList());
        List<DragonForgeRecipe> lightning = (List<DragonForgeRecipe>) forgeRecipeList.stream().filter(item -> item.getDragonType().equals("lightning")).collect(Collectors.toList());
        registry.addRecipes(FIRE_DRAGON_FORGE_RECIPE_TYPE, fire);
        registry.addRecipes(ICE_DRAGON_FORGE_RECIPE_TYPE, ice);
        registry.addRecipes(LIGHTNING_DRAGON_FORGE_RECIPE_TYPE, lightning);
        this.addDescription(registry, new ItemStack(IafItemRegistry.FIRE_DRAGON_BLOOD.get()));
        this.addDescription(registry, new ItemStack(IafItemRegistry.ICE_DRAGON_BLOOD.get()));
        this.addDescription(registry, new ItemStack(IafItemRegistry.LIGHTNING_DRAGON_BLOOD.get()));
        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_RED.get()));
        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_BRONZE.get()));
        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_GRAY.get()));
        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_GREEN.get()));
        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_BLUE.get()));
        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_WHITE.get()));
        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_SAPPHIRE.get()));
        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_SILVER.get()));
        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_ELECTRIC.get()));
        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_AMYTHEST.get()));
        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_COPPER.get()));
        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_BLACK.get()));
        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGON_SKULL_FIRE.get()));
        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGON_SKULL_ICE.get()));
        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGON_SKULL_LIGHTNING.get()));
        this.addDescription(registry, new ItemStack(IafItemRegistry.FIRE_STEW.get()));
        this.addDescription(registry, new ItemStack(IafItemRegistry.FROST_STEW.get()));
        for (EnumSkullType skull : EnumSkullType.values()) {
            this.addDescription(registry, new ItemStack(skull.skull_item.get()));
        }
        registry.addIngredientInfo(IafItemRegistry.PATTERN_FIRE.get().m_7968_(), VanillaTypes.ITEM_STACK, Component.translatable("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_ICE.get().m_7968_(), VanillaTypes.ITEM_STACK, Component.translatable("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_LIGHTNING.get().m_7968_(), VanillaTypes.ITEM_STACK, Component.translatable("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_FIRE_HEAD.get().m_7968_(), VanillaTypes.ITEM_STACK, Component.translatable("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_ICE_HEAD.get().m_7968_(), VanillaTypes.ITEM_STACK, Component.translatable("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_LIGHTNING_HEAD.get().m_7968_(), VanillaTypes.ITEM_STACK, Component.translatable("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_AMPHITHERE.get().m_7968_(), VanillaTypes.ITEM_STACK, Component.translatable("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_BIRD.get().m_7968_(), VanillaTypes.ITEM_STACK, Component.translatable("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_EYE.get().m_7968_(), VanillaTypes.ITEM_STACK, Component.translatable("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_FAE.get().m_7968_(), VanillaTypes.ITEM_STACK, Component.translatable("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_FEATHER.get().m_7968_(), VanillaTypes.ITEM_STACK, Component.translatable("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_GORGON.get().m_7968_(), VanillaTypes.ITEM_STACK, Component.translatable("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_HIPPOCAMPUS.get().m_7968_(), VanillaTypes.ITEM_STACK, Component.translatable("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_HIPPOGRYPH_HEAD.get().m_7968_(), VanillaTypes.ITEM_STACK, Component.translatable("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_MERMAID.get().m_7968_(), VanillaTypes.ITEM_STACK, Component.translatable("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_SEA_SERPENT.get().m_7968_(), VanillaTypes.ITEM_STACK, Component.translatable("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_TROLL.get().m_7968_(), VanillaTypes.ITEM_STACK, Component.translatable("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_WEEZER.get().m_7968_(), VanillaTypes.ITEM_STACK, Component.translatable("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_DREAD.get().m_7968_(), VanillaTypes.ITEM_STACK, Component.translatable("item.iceandfire.custom_banner.jei_desc"));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new FireDragonForgeCategory());
        registry.addRecipeCategories(new IceDragonForgeCategory());
        registry.addRecipeCategories(new LightningDragonForgeCategory());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(IafBlockRegistry.DRAGONFORGE_FIRE_CORE.get()), FIRE_DRAGON_FORGE_RECIPE_TYPE);
        registry.addRecipeCatalyst(new ItemStack(IafBlockRegistry.DRAGONFORGE_ICE_CORE.get()), ICE_DRAGON_FORGE_RECIPE_TYPE);
        registry.addRecipeCatalyst(new ItemStack(IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE.get()), LIGHTNING_DRAGON_FORGE_RECIPE_TYPE);
    }

    @NotNull
    @Override
    public ResourceLocation getPluginUid() {
        return MOD;
    }
}