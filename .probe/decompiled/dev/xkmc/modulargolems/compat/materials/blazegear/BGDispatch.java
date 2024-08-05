package dev.xkmc.modulargolems.compat.materials.blazegear;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.modulargolems.compat.materials.common.ModDispatch;
import dev.xkmc.modulargolems.init.ModularGolems;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BGDispatch extends ModDispatch {

    public static final String MODID = "blazegear";

    public BGDispatch() {
        BGCompatRegistry.register();
    }

    @Override
    public void genLang(RegistrateLangProvider pvd) {
        pvd.add("golem_material.blazegear.brimsteel", "Brimsteel");
    }

    @Override
    public void genRecipe(RegistrateRecipeProvider pvd) {
    }

    @Override
    public ConfigDataProvider getDataGen(DataGenerator gen) {
        return new BGConfigGen(gen);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void dispatchClientSetup() {
        DuplicateBlazeArmsLayer.registerLayer();
        ModularGolems.MOD_BUS.addListener(BGClientEvents::registerLayer);
    }
}