package dev.xkmc.modulargolems.compat.materials.common;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import java.util.function.BiFunction;
import javax.annotation.Nullable;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class ModDispatch {

    protected abstract void genLang(RegistrateLangProvider var1);

    public abstract void genRecipe(RegistrateRecipeProvider var1);

    @Nullable
    public abstract ConfigDataProvider getDataGen(DataGenerator var1);

    public static <T> T safeUpgrade(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
        return (T) func.apply("has_" + pvd.safeName(item), DataIngredient.items((Item) GolemItems.EMPTY_UPGRADE.get(), new Item[0]).getCritereon(pvd));
    }

    @OnlyIn(Dist.CLIENT)
    public void dispatchClientSetup() {
    }

    public void lateRegister() {
    }
}