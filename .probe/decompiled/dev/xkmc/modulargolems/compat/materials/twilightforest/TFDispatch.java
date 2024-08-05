package dev.xkmc.modulargolems.compat.materials.twilightforest;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import dev.xkmc.modulargolems.compat.materials.common.ModDispatch;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

public class TFDispatch extends ModDispatch {

    public static final String MODID = "twilightforest";

    public TFDispatch() {
        TFCompatRegistry.register();
    }

    @Override
    public void genLang(RegistrateLangProvider pvd) {
        pvd.add("golem_material.twilightforest.ironwood", "Ironwood");
        pvd.add("golem_material.twilightforest.steeleaf", "Steeleaf");
        pvd.add("golem_material.twilightforest.knightmetal", "Knightmetal");
        pvd.add("golem_material.twilightforest.fiery", "Fiery");
    }

    @Override
    public void genRecipe(RegistrateRecipeProvider pvd) {
        safeUpgrade(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) TFCompatRegistry.UP_CARMINITE.get())::m_126132_, (Item) TFItems.CARMINITE.get()).pattern("CAC").pattern("ABA").pattern("CAC").define('A', (ItemLike) TFItems.CARMINITE.get()).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).define('C', (ItemLike) TFBlocks.ENCASED_TOWERWOOD.get()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "twilightforest"));
        safeUpgrade(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) TFCompatRegistry.UP_FIERY.get())::m_126132_, (Item) TFItems.FIERY_INGOT.get()).pattern("CAC").pattern("ABA").pattern("CAC").define('A', (ItemLike) TFItems.FIERY_INGOT.get()).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).define('C', Items.BLAZE_POWDER).m_176498_(ConditionalRecipeWrapper.mod(pvd, "twilightforest"));
        safeUpgrade(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) TFCompatRegistry.UP_KNIGHTMETAL.get())::m_126132_, (Item) TFItems.KNIGHTMETAL_INGOT.get()).pattern("CAC").pattern("ABA").pattern("CAC").define('A', (ItemLike) TFItems.KNIGHTMETAL_INGOT.get()).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).define('C', (ItemLike) TFBlocks.HEDGE.get()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "twilightforest"));
        safeUpgrade(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) TFCompatRegistry.UP_STEELEAF.get())::m_126132_, (Item) TFItems.STEELEAF_INGOT.get()).pattern(" A ").pattern("ABA").pattern(" A ").define('A', (ItemLike) TFItems.STEELEAF_INGOT.get()).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "twilightforest"));
        safeUpgrade(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) TFCompatRegistry.UP_IRONWOOD.get())::m_126132_, (Item) TFItems.IRONWOOD_INGOT.get()).pattern(" A ").pattern("ABA").pattern(" A ").define('A', (ItemLike) TFItems.IRONWOOD_INGOT.get()).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "twilightforest"));
        safeUpgrade(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) TFCompatRegistry.UP_NAGA.get())::m_126132_, (Item) TFItems.NAGA_SCALE.get()).pattern(" A ").pattern("ABA").pattern(" A ").define('A', (ItemLike) TFItems.NAGA_SCALE.get()).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "twilightforest"));
    }

    @Override
    public ConfigDataProvider getDataGen(DataGenerator gen) {
        return new TFConfigGen(gen);
    }
}