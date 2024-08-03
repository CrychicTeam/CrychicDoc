package harmonised.pmmo.client.events;

import harmonised.pmmo.client.gui.component.PMMOButton;
import harmonised.pmmo.config.Config;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "pmmo", bus = Bus.FORGE, value = { Dist.CLIENT })
public class ScreenHandler {

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof InventoryScreen inv && !Config.HIDE_SKILL_BUTTON.get()) {
            event.addListener(new PMMOButton(inv, inv.f_97735_ + 126, inv.f_96544_ / 2 - 22, 20, 18, 148, 0, 19));
        }
    }

    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Render.Post event) {
        if (event.getScreen() instanceof InventoryScreen inventory) {
            inventory.f_169369_.forEach(widget -> {
                if (widget instanceof PMMOButton button) {
                    button.m_264152_(inventory.f_97735_ + Config.SKILL_BUTTON_X.get(), inventory.f_96544_ / 2 + Config.SKILL_BUTTON_Y.get());
                }
            });
        }
    }
}