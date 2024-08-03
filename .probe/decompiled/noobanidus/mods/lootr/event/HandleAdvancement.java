package noobanidus.mods.lootr.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import noobanidus.mods.lootr.init.ModAdvancements;

@EventBusSubscriber(modid = "lootr")
public class HandleAdvancement {

    @SubscribeEvent
    public static void onAdvancement(AdvancementEvent event) {
        if (!event.getEntity().m_9236_().isClientSide()) {
            ModAdvancements.ADVANCEMENT_PREDICATE.trigger((ServerPlayer) event.getEntity(), event.getAdvancement().getId());
        }
    }
}