package harmonised.pmmo.setup;

import harmonised.pmmo.client.gui.IndicatorsOverlayGUI;
import harmonised.pmmo.client.gui.TutorialOverlayGUI;
import harmonised.pmmo.client.gui.XPOverlayGUI;
import harmonised.pmmo.setup.datagen.LangProvider;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "pmmo", bus = Bus.MOD, value = { Dist.CLIENT })
public class ClientSetup {

    public static final KeyMapping SHOW_VEIN = new KeyMapping(LangProvider.KEYBIND_SHOWVEIN.key(), 258, LangProvider.KEYBIND_CATEGORY.key());

    public static final KeyMapping ADD_VEIN = new KeyMapping(LangProvider.KEYBIND_ADDVEIN.key(), 91, LangProvider.KEYBIND_CATEGORY.key());

    public static final KeyMapping SUB_VEIN = new KeyMapping(LangProvider.KEYBIND_SUBVEIN.key(), 93, LangProvider.KEYBIND_CATEGORY.key());

    public static final KeyMapping CYCLE_VEIN = new KeyMapping(LangProvider.KEYBIND_VEINCYCLE.key(), 39, LangProvider.KEYBIND_CATEGORY.key());

    public static final KeyMapping SHOW_LIST = new KeyMapping(LangProvider.KEYBIND_SHOWLIST.key(), 342, LangProvider.KEYBIND_CATEGORY.key());

    public static final KeyMapping VEIN_KEY = new KeyMapping(LangProvider.KEYBIND_VEIN.key(), 96, LangProvider.KEYBIND_CATEGORY.key());

    public static final KeyMapping OPEN_MENU = new KeyMapping(LangProvider.KEYBIND_OPENMENU.key(), 80, LangProvider.KEYBIND_CATEGORY.key());

    @SubscribeEvent
    public static void init(RegisterKeyMappingsEvent event) {
        event.register(SHOW_VEIN);
        event.register(ADD_VEIN);
        event.register(SUB_VEIN);
        event.register(CYCLE_VEIN);
        event.register(SHOW_LIST);
        event.register(VEIN_KEY);
        event.register(OPEN_MENU);
    }

    @SubscribeEvent
    public static void registerOverlay(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("stats_overlay", new XPOverlayGUI());
        event.registerAboveAll("tutorial", new TutorialOverlayGUI());
        event.registerAbove(new ResourceLocation("crosshair"), "overlay_icons", new IndicatorsOverlayGUI());
    }
}