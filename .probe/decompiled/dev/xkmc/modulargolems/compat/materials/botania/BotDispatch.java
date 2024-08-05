package dev.xkmc.modulargolems.compat.materials.botania;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.modulargolems.compat.materials.common.ModDispatch;
import net.minecraft.data.DataGenerator;

public class BotDispatch extends ModDispatch {

    public static final String MODID = "botania";

    public BotDispatch() {
        BotCompatRegistry.register();
    }

    @Override
    public void genLang(RegistrateLangProvider pvd) {
        pvd.add("golem_material.botania.manasteel", "Manasteel");
        pvd.add("golem_material.botania.terrasteel", "Terrasteel");
        pvd.add("golem_material.botania.elementium", "Elementium");
    }

    @Override
    public void genRecipe(RegistrateRecipeProvider pvd) {
    }

    @Override
    public ConfigDataProvider getDataGen(DataGenerator gen) {
        return new BotConfigGen(gen);
    }
}