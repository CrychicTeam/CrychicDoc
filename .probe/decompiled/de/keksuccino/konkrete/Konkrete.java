package de.keksuccino.konkrete;

import de.keksuccino.konkrete.gui.content.AdvancedButtonHandler;
import de.keksuccino.konkrete.gui.content.handling.AdvancedWidgetsHandler;
import de.keksuccino.konkrete.gui.screens.popup.PopupHandler;
import de.keksuccino.konkrete.input.KeyboardHandler;
import de.keksuccino.konkrete.input.MouseInput;
import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.konkrete.rendering.CurrentScreenHandler;
import de.keksuccino.konkrete.sound.SoundHandler;
import java.io.File;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("konkrete")
public class Konkrete {

    public static final String VERSION = "1.8.0";

    public static Logger LOGGER = LogManager.getLogger();

    public static boolean isOptifineLoaded = false;

    public Konkrete() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            PopupHandler.init();
            AdvancedWidgetsHandler.init();
            KeyboardHandler.init();
            MouseInput.init();
            CurrentScreenHandler.init();
            AdvancedButtonHandler.init();
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
            try {
                Class.forName("optifine.Installer");
                isOptifineLoaded = true;
                LOGGER.info("[KONKRETE] OptiFine detected!");
            } catch (ClassNotFoundException var2) {
            }
        }
        LOGGER.info("[KONKRETE] Successfully initialized!");
        LOGGER.info("[KONKRETE] Server-side libs ready to use!");
    }

    private void onClientSetup(FMLClientSetupEvent e) {
        SoundHandler.init();
        SoundHandler.updateVolume();
        initLocals();
        LOGGER.info("[KONKRETE] Client-side libs ready to use!");
        PostLoadingHandler.runPostLoadingEvents();
    }

    private static void initLocals() {
        File f = new File("config/konkrete/locals");
        if (!f.exists()) {
            f.mkdirs();
        }
        Locals.copyLocalsFileToDir(new ResourceLocation("keksuccino", "konkretelocals/en_us.local"), "en_us", f.getPath());
        Locals.copyLocalsFileToDir(new ResourceLocation("keksuccino", "konkretelocals/de_de.local"), "de_de", f.getPath());
        Locals.copyLocalsFileToDir(new ResourceLocation("keksuccino", "konkretelocals/pl_pl.local"), "pl_pl", f.getPath());
        Locals.copyLocalsFileToDir(new ResourceLocation("keksuccino", "konkretelocals/pt_br.local"), "pt_br", f.getPath());
        Locals.getLocalsFromDir(f.getPath());
    }

    public static void addPostLoadingEvent(String modid, Runnable event) {
        PostLoadingHandler.addEvent(modid, event);
    }
}