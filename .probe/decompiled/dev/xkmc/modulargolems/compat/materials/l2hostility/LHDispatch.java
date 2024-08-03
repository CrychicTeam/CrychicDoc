package dev.xkmc.modulargolems.compat.materials.l2hostility;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2complements.init.materials.LCMats;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2hostility.content.traits.base.AttributeTrait;
import dev.xkmc.l2hostility.content.traits.base.SelfEffectTrait;
import dev.xkmc.l2hostility.content.traits.base.TargetEffectTrait;
import dev.xkmc.l2hostility.content.traits.common.AdaptingTrait;
import dev.xkmc.l2hostility.content.traits.common.ReflectTrait;
import dev.xkmc.l2hostility.content.traits.common.RegenTrait;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import dev.xkmc.modulargolems.compat.materials.common.ModDispatch;
import dev.xkmc.modulargolems.compat.materials.l2complements.LCCompatRegistry;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public class LHDispatch extends ModDispatch {

    public static final String MODID = "l2hostility";

    public LHDispatch() {
        LHCompatRegistry.register();
    }

    @Override
    public void genLang(RegistrateLangProvider pvd) {
    }

    @Override
    public void genRecipe(RegistrateRecipeProvider pvd) {
        RecipeCategory var10003 = RecipeCategory.MISC;
        safeUpgrade(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHCompatRegistry.CORE.get(), 1)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("CBC").pattern("BAB").pattern("CBC").define('A', (ItemLike) GolemItems.TALENTED.get()).define('B', (ItemLike) LHItems.CHAOS_INGOT.get()).define('C', ((AdaptingTrait) LHTraits.ADAPTIVE.get()).asItem()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "l2hostility"));
        var10003 = RecipeCategory.MISC;
        safeUpgrade(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHCompatRegistry.POTION.get(), 1)::m_126132_, ((TargetEffectTrait) LHTraits.CURSED.get()).asItem()).pattern("1B2").pattern("BAB").pattern("3B4").define('1', ((TargetEffectTrait) LHTraits.CURSED.get()).asItem()).define('2', ((TargetEffectTrait) LHTraits.SOUL_BURNER.get()).asItem()).define('3', ((TargetEffectTrait) LHTraits.SLOWNESS.get()).asItem()).define('4', ((TargetEffectTrait) LHTraits.WEAKNESS.get()).asItem()).define('A', (ItemLike) GolemItems.CAULDRON.get()).define('B', (ItemLike) LCItems.STORM_CORE.get()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "l2hostility"));
        var10003 = RecipeCategory.MISC;
        safeUpgrade(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHCompatRegistry.TANK.get(), 1)::m_126132_, ((AttributeTrait) LHTraits.TANK.get()).asItem()).pattern("CBC").pattern("BAB").pattern("CBC").define('A', (ItemLike) GolemItems.NETHERITE.get()).define('B', (ItemLike) LCItems.WARDEN_BONE_SHARD.get()).define('C', ((AttributeTrait) LHTraits.TANK.get()).asItem()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "l2hostility"));
        var10003 = RecipeCategory.MISC;
        safeUpgrade(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHCompatRegistry.SPEED.get(), 1)::m_126132_, ((AttributeTrait) LHTraits.SPEEDY.get()).asItem()).pattern("CBC").pattern("BAB").pattern("CBC").define('A', (ItemLike) LCCompatRegistry.SPEED_UP.get()).define('B', (ItemLike) LCItems.CAPTURED_WIND.get()).define('C', ((AttributeTrait) LHTraits.SPEEDY.get()).asItem()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "l2hostility"));
        var10003 = RecipeCategory.MISC;
        safeUpgrade(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHCompatRegistry.PROTECTION.get(), 1)::m_126132_, ((SelfEffectTrait) LHTraits.PROTECTION.get()).asItem()).pattern("CBC").pattern("BAB").pattern("CBC").define('A', (ItemLike) GolemItems.DIAMOND.get()).define('B', Items.SCUTE).define('C', ((SelfEffectTrait) LHTraits.PROTECTION.get()).asItem()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "l2hostility"));
        var10003 = RecipeCategory.MISC;
        safeUpgrade(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHCompatRegistry.REGEN.get(), 1)::m_126132_, ((RegenTrait) LHTraits.REGEN.get()).asItem()).pattern("CBC").pattern("BAB").pattern("CBC").define('A', (ItemLike) GolemItems.ENCHANTED_GOLD.get()).define('B', LCMats.TOTEMIC_GOLD.getIngot()).define('C', ((RegenTrait) LHTraits.REGEN.get()).asItem()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "l2hostility"));
        var10003 = RecipeCategory.MISC;
        safeUpgrade(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHCompatRegistry.REFLECTIVE.get(), 1)::m_126132_, ((ReflectTrait) LHTraits.REFLECT.get()).asItem()).pattern("CBC").pattern("BAB").pattern("CBC").define('A', (ItemLike) LCCompatRegistry.ATK_UP.get()).define('B', LCItems.EXPLOSION_SHARD).define('C', ((ReflectTrait) LHTraits.REFLECT.get()).asItem()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "l2hostility"));
    }

    @Override
    public ConfigDataProvider getDataGen(DataGenerator gen) {
        return new LHConfigGen(gen);
    }

    @Override
    public void dispatchClientSetup() {
    }
}