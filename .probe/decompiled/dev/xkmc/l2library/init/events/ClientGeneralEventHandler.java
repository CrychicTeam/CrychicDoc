package dev.xkmc.l2library.init.events;

import dev.xkmc.l2library.capability.player.PlayerCapabilityHolder;
import dev.xkmc.l2library.util.raytrace.EntityTarget;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "l2library", bus = Bus.FORGE)
public class ClientGeneralEventHandler {

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            for (EntityTarget target : EntityTarget.LIST) {
                target.tickRender();
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(ClientPlayerNetworkEvent.Clone event) {
        for (PlayerCapabilityHolder<?> holder : PlayerCapabilityHolder.INTERNAL_MAP.values()) {
            CompoundTag tag0 = holder.getCache(event.getOldPlayer());
            Wrappers.run(() -> TagCodec.fromTag(tag0, holder.holder_class, holder.get(event.getNewPlayer()), f -> true));
            holder.get(event.getNewPlayer());
        }
    }
}