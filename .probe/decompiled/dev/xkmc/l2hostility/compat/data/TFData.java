package dev.xkmc.l2hostility.compat.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.traits.base.AttributeTrait;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.content.traits.base.TargetEffectTrait;
import dev.xkmc.l2hostility.content.traits.common.ReflectTrait;
import dev.xkmc.l2hostility.content.traits.common.RegenTrait;
import dev.xkmc.l2hostility.init.data.LHConfigGen;
import dev.xkmc.l2hostility.init.data.RecipeGen;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItems;

public class TFData {

    public static void genRecipe(RegistrateRecipeProvider pvd) {
        RecipeCategory var10001 = RecipeCategory.MISC;
        RecipeGen.<ShapedRecipeBuilder>unlock(pvd, ShapedRecipeBuilder.shaped(var10001, (ItemLike) TFBlocks.NAGA_BOSS_SPAWNER.get(), 4)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("1I2").pattern("ITI").pattern("AIA").define('I', LHItems.CHAOS_INGOT).define('T', (ItemLike) TFItems.NAGA_TROPHY.get()).define('A', (ItemLike) TFItems.NAGA_SCALE.get()).define('1', ((AttributeTrait) LHTraits.SPEEDY.get()).m_5456_()).define('2', ((TargetEffectTrait) LHTraits.CURSED.get()).m_5456_()).save(ConditionalRecipeWrapper.mod(pvd, "twilightforest"), RecipeGen.getID((ItemLike) TFBlocks.NAGA_BOSS_SPAWNER.get()));
        var10001 = RecipeCategory.MISC;
        RecipeGen.<ShapedRecipeBuilder>unlock(pvd, ShapedRecipeBuilder.shaped(var10001, (ItemLike) TFBlocks.LICH_BOSS_SPAWNER.get(), 4)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("1I2").pattern("ITI").pattern("AIA").define('I', LHItems.CHAOS_INGOT).define('T', (ItemLike) TFItems.LICH_TROPHY.get()).define('A', Items.SKELETON_SKULL).define('1', ((TargetEffectTrait) LHTraits.WEAKNESS.get()).m_5456_()).define('2', ((TargetEffectTrait) LHTraits.CURSED.get()).m_5456_()).save(ConditionalRecipeWrapper.mod(pvd, "twilightforest"), RecipeGen.getID((ItemLike) TFBlocks.LICH_BOSS_SPAWNER.get()));
        var10001 = RecipeCategory.MISC;
        RecipeGen.<ShapedRecipeBuilder>unlock(pvd, ShapedRecipeBuilder.shaped(var10001, (ItemLike) TFBlocks.MINOSHROOM_BOSS_SPAWNER.get(), 4)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("1I2").pattern("ITI").pattern("AIA").define('I', LHItems.CHAOS_INGOT).define('T', (ItemLike) TFItems.MINOSHROOM_TROPHY.get()).define('A', (ItemLike) TFItems.RAW_MEEF.get()).define('1', ((AttributeTrait) LHTraits.TANK.get()).m_5456_()).define('2', ((TargetEffectTrait) LHTraits.CURSED.get()).m_5456_()).save(ConditionalRecipeWrapper.mod(pvd, "twilightforest"), RecipeGen.getID((ItemLike) TFBlocks.MINOSHROOM_BOSS_SPAWNER.get()));
        var10001 = RecipeCategory.MISC;
        RecipeGen.<ShapedRecipeBuilder>unlock(pvd, ShapedRecipeBuilder.shaped(var10001, (ItemLike) TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get(), 4)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("1I2").pattern("ITI").pattern("AIA").define('I', LHItems.CHAOS_INGOT).define('T', (ItemLike) TFItems.ALPHA_YETI_TROPHY.get()).define('A', (ItemLike) TFItems.ALPHA_YETI_FUR.get()).define('1', ((RegenTrait) LHTraits.REGEN.get()).m_5456_()).define('2', ((TargetEffectTrait) LHTraits.CURSED.get()).m_5456_()).save(ConditionalRecipeWrapper.mod(pvd, "twilightforest"), RecipeGen.getID((ItemLike) TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get()));
        var10001 = RecipeCategory.MISC;
        RecipeGen.<ShapedRecipeBuilder>unlock(pvd, ShapedRecipeBuilder.shaped(var10001, (ItemLike) TFBlocks.HYDRA_BOSS_SPAWNER.get(), 4)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("1I2").pattern("ITI").pattern("AIA").define('I', LHItems.CHAOS_INGOT).define('T', (ItemLike) TFItems.HYDRA_TROPHY.get()).define('A', (ItemLike) TFItems.FIERY_BLOOD.get()).define('1', ((TargetEffectTrait) LHTraits.SOUL_BURNER.get()).m_5456_()).define('2', ((TargetEffectTrait) LHTraits.CURSED.get()).m_5456_()).save(ConditionalRecipeWrapper.mod(pvd, "twilightforest"), RecipeGen.getID((ItemLike) TFBlocks.HYDRA_BOSS_SPAWNER.get()));
        var10001 = RecipeCategory.MISC;
        RecipeGen.<ShapedRecipeBuilder>unlock(pvd, ShapedRecipeBuilder.shaped(var10001, (ItemLike) TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get(), 4)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("1I2").pattern("ITI").pattern("AIA").define('I', LHItems.CHAOS_INGOT).define('T', (ItemLike) TFItems.KNIGHT_PHANTOM_TROPHY.get()).define('A', (ItemLike) TFItems.KNIGHTMETAL_INGOT.get()).define('1', ((ReflectTrait) LHTraits.REFLECT.get()).m_5456_()).define('2', ((TargetEffectTrait) LHTraits.CURSED.get()).m_5456_()).save(ConditionalRecipeWrapper.mod(pvd, "twilightforest"), RecipeGen.getID((ItemLike) TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get()));
        var10001 = RecipeCategory.MISC;
        RecipeGen.<ShapedRecipeBuilder>unlock(pvd, ShapedRecipeBuilder.shaped(var10001, (ItemLike) TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get(), 4)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("1I2").pattern("ITI").pattern("AIA").define('I', LHItems.CHAOS_INGOT).define('T', (ItemLike) TFItems.SNOW_QUEEN_TROPHY.get()).define('A', (ItemLike) TFItems.ICE_BOMB.get()).define('1', ((TargetEffectTrait) LHTraits.FREEZING.get()).m_5456_()).define('2', ((TargetEffectTrait) LHTraits.CURSED.get()).m_5456_()).save(ConditionalRecipeWrapper.mod(pvd, "twilightforest"), RecipeGen.getID((ItemLike) TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get()));
        var10001 = RecipeCategory.MISC;
        RecipeGen.<ShapedRecipeBuilder>unlock(pvd, ShapedRecipeBuilder.shaped(var10001, (ItemLike) TFBlocks.UR_GHAST_BOSS_SPAWNER.get(), 4)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("1I2").pattern("ITI").pattern("AIA").define('I', LHItems.CHAOS_INGOT).define('T', (ItemLike) TFItems.UR_GHAST_TROPHY.get()).define('A', (ItemLike) TFItems.FIERY_TEARS.get()).define('1', ((TargetEffectTrait) LHTraits.WITHER.get()).m_5456_()).define('2', ((TargetEffectTrait) LHTraits.CURSED.get()).m_5456_()).save(ConditionalRecipeWrapper.mod(pvd, "twilightforest"), RecipeGen.getID((ItemLike) TFBlocks.UR_GHAST_BOSS_SPAWNER.get()));
    }

    public static void genConfig(ConfigDataProvider.Collector collector) {
        LHConfigGen.addEntity(collector, 50, 20, TFEntities.NAGA, EntityConfig.trait((MobTrait) LHTraits.SPEEDY.get(), 1, 2), EntityConfig.trait((MobTrait) LHTraits.CURSED.get(), 0, 1));
        LHConfigGen.addEntity(collector, 100, 30, TFEntities.LICH, EntityConfig.trait((MobTrait) LHTraits.WEAKNESS.get(), 2, 3), EntityConfig.trait((MobTrait) LHTraits.CURSED.get(), 1, 1));
        LHConfigGen.addEntity(collector, 100, 30, TFEntities.MINOSHROOM, EntityConfig.trait((MobTrait) LHTraits.TANK.get(), 1, 3), EntityConfig.trait((MobTrait) LHTraits.CURSED.get(), 1, 1));
        LHConfigGen.addEntity(collector, 100, 30, TFEntities.ALPHA_YETI, EntityConfig.trait((MobTrait) LHTraits.REGEN.get(), 1, 2), EntityConfig.trait((MobTrait) LHTraits.CURSED.get(), 1, 1));
        LHConfigGen.addEntity(collector, 150, 50, TFEntities.HYDRA, EntityConfig.trait((MobTrait) LHTraits.SOUL_BURNER.get(), 1, 1), EntityConfig.trait((MobTrait) LHTraits.CURSED.get(), 1, 1));
        LHConfigGen.addEntity(collector, 150, 50, TFEntities.KNIGHT_PHANTOM, EntityConfig.trait((MobTrait) LHTraits.REFLECT.get(), 0, 1), EntityConfig.trait((MobTrait) LHTraits.CURSED.get(), 1, 1));
        LHConfigGen.addEntity(collector, 150, 50, TFEntities.SNOW_QUEEN, EntityConfig.trait((MobTrait) LHTraits.FREEZING.get(), 1, 1), EntityConfig.trait((MobTrait) LHTraits.CURSED.get(), 1, 1));
        LHConfigGen.addEntity(collector, 150, 50, TFEntities.UR_GHAST, EntityConfig.trait((MobTrait) LHTraits.WITHER.get(), 1, 1), EntityConfig.trait((MobTrait) LHTraits.CURSED.get(), 1, 1));
    }
}