package dev.xkmc.modulargolems.compat.materials.common;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.modulargolems.compat.materials.blazegear.BGDispatch;
import dev.xkmc.modulargolems.compat.materials.botania.BotDispatch;
import dev.xkmc.modulargolems.compat.materials.cataclysm.CataDispatch;
import dev.xkmc.modulargolems.compat.materials.create.CreateDispatch;
import dev.xkmc.modulargolems.compat.materials.l2complements.LCDispatch;
import dev.xkmc.modulargolems.compat.materials.l2hostility.LHDispatch;
import dev.xkmc.modulargolems.compat.materials.twilightforest.TFDispatch;
import dev.xkmc.modulargolems.compat.misc.CEICompat;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.ModList;

public abstract class CompatManager {

    public static final List<ModDispatch> LIST = new ArrayList();

    public static void register() {
        if (ModList.get().isLoaded("botania")) {
            LIST.add(new BotDispatch());
        }
        if (ModList.get().isLoaded("twilightforest")) {
            LIST.add(new TFDispatch());
        }
        if (ModList.get().isLoaded("create")) {
            LIST.add(new CreateDispatch());
        }
        if (ModList.get().isLoaded("l2complements")) {
            LIST.add(new LCDispatch());
        }
        if (ModList.get().isLoaded("blazegear")) {
            LIST.add(new BGDispatch());
        }
        if (ModList.get().isLoaded("l2hostility")) {
            LIST.add(new LHDispatch());
        }
        if (ModList.get().isLoaded("cataclysm")) {
            LIST.add(new CataDispatch());
        }
        if (ModList.get().isLoaded("create_enchantment_industry")) {
            CEICompat.register();
        }
    }

    public static void dispatchGenLang(RegistrateLangProvider pvd) {
        for (ModDispatch dispatch : LIST) {
            dispatch.genLang(pvd);
        }
    }

    public static void gatherData(GatherDataEvent event) {
        for (ModDispatch dispatch : LIST) {
            ConfigDataProvider gen = dispatch.getDataGen(event.getGenerator());
            if (gen != null) {
                event.getGenerator().addProvider(event.includeServer(), gen);
            }
        }
    }

    public static void dispatchGenRecipe(RegistrateRecipeProvider pvd) {
        for (ModDispatch dispatch : LIST) {
            dispatch.genRecipe(pvd);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void dispatchClientSetup() {
        for (ModDispatch dispatch : LIST) {
            dispatch.dispatchClientSetup();
        }
    }

    public static void lateRegister() {
        for (ModDispatch dispatch : LIST) {
            dispatch.lateRegister();
        }
    }
}