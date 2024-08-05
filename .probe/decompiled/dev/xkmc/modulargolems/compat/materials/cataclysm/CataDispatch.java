package dev.xkmc.modulargolems.compat.materials.cataclysm;

import com.github.L_Ender.cataclysm.init.ModItems;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import dev.xkmc.modulargolems.compat.materials.common.ModDispatch;
import dev.xkmc.modulargolems.content.client.override.ModelOverride;
import dev.xkmc.modulargolems.content.client.override.ModelOverrides;
import dev.xkmc.modulargolems.init.data.RecipeGen;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CataDispatch extends ModDispatch {

    public static final String MODID = "cataclysm";

    public CataDispatch() {
        CataCompatRegistry.register();
    }

    @Override
    public void genLang(RegistrateLangProvider pvd) {
        pvd.add("golem_material.cataclysm.ignitium", "Ignitium");
        pvd.add("golem_material.cataclysm.witherite", "Witherite");
    }

    @Override
    public void genRecipe(RegistrateRecipeProvider pvd) {
        RecipeGen.<ShapelessRecipeBuilder>unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike) CataCompatRegistry.ENDER_GUARDIAN.get())::m_126132_, (Item) ModItems.GAUNTLET_OF_GUARD.get()).requires(GolemItems.EMPTY_UPGRADE).requires((ItemLike) ModItems.GAUNTLET_OF_GUARD.get()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "cataclysm"));
        RecipeGen.<ShapelessRecipeBuilder>unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike) CataCompatRegistry.LEVIATHAN.get())::m_126132_, (Item) ModItems.TIDAL_CLAWS.get()).requires(GolemItems.EMPTY_UPGRADE).requires((ItemLike) ModItems.TIDAL_CLAWS.get()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "cataclysm"));
        RecipeGen.<ShapelessRecipeBuilder>unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike) CataCompatRegistry.MONSTROSITY.get())::m_126132_, (Item) ModItems.INFERNAL_FORGE.get()).requires(GolemItems.EMPTY_UPGRADE).requires((ItemLike) ModItems.INFERNAL_FORGE.get()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "cataclysm"));
        RecipeGen.<ShapelessRecipeBuilder>unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike) CataCompatRegistry.ANCIENT_REMNANT.get())::m_126132_, (Item) ModItems.SANDSTORM_IN_A_BOTTLE.get()).requires(GolemItems.EMPTY_UPGRADE).requires((ItemLike) ModItems.SANDSTORM_IN_A_BOTTLE.get()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "cataclysm"));
    }

    @Override
    public ConfigDataProvider getDataGen(DataGenerator gen) {
        return new CataConfigGen(gen);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void dispatchClientSetup() {
        ModelOverrides.registerOverride(new ResourceLocation("cataclysm", "ignitium"), ModelOverride.texturePredicate(e -> e.m_21223_() <= e.m_21233_() / 2.0F ? "_soul" : ""));
    }
}