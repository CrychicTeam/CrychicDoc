package dev.xkmc.l2backpack.events;

import dev.xkmc.l2backpack.content.common.ContentTransfer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = "l2backpack", bus = Bus.FORGE)
public class LoadContainerEvents {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void playerLeftClick(@NotNull PlayerInteractEvent.LeftClickBlock event) {
        if (event.getEntity().m_6144_() && event.getItemStack().getItem() instanceof ContentTransfer.Quad load) {
            if (!event.getLevel().isClientSide()) {
                ContentTransfer.leftClick(load, event.getLevel(), event.getPos(), event.getItemStack(), event.getEntity());
            }
            event.setCanceled(true);
        }
    }
}