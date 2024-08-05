package dev.xkmc.l2library.init.events;

import dev.xkmc.l2library.capability.entity.GeneralCapabilityHolder;
import dev.xkmc.l2library.capability.player.PlayerCapabilityHolder;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "l2library", bus = Bus.FORGE)
public class BaseCapabilityEvents {

    @SubscribeEvent
    public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        for (GeneralCapabilityHolder<?, ?> holder : GeneralCapabilityHolder.INTERNAL_MAP.values()) {
            Entity e = event.getObject();
            if (holder.entity_class.isInstance(e) && ((GeneralCapabilityHolder<Object, ?>) holder).shouldHaveCap((ICapabilityProvider) Wrappers.cast(e))) {
                event.addCapability(holder.id, ((GeneralCapabilityHolder<Object, ?>) holder).generateSerializer((ICapabilityProvider) Wrappers.cast(e)));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.m_6084_() && event.phase == TickEvent.Phase.END) {
            for (PlayerCapabilityHolder<?> holder : PlayerCapabilityHolder.INTERNAL_MAP.values()) {
                holder.get(event.player).tick();
            }
        }
    }

    @SubscribeEvent
    public static void onServerPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer e = (ServerPlayer) event.getEntity();
        if (e != null) {
            for (PlayerCapabilityHolder<?> holder : PlayerCapabilityHolder.INTERNAL_MAP.values()) {
                holder.network.toClientSyncAll(e);
                holder.network.toTracking(e);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        for (PlayerCapabilityHolder<?> holder : PlayerCapabilityHolder.INTERNAL_MAP.values()) {
            CompoundTag tag0 = TagCodec.toTag(new CompoundTag(), holder.get(event.getOriginal()));
            assert tag0 != null;
            Wrappers.run(() -> TagCodec.fromTag(tag0, holder.holder_class, holder.get(event.getEntity()), f -> true));
            holder.get(event.getEntity()).onClone(event.isWasDeath());
            ServerPlayer e = (ServerPlayer) event.getEntity();
            holder.network.toClientSyncClone(e);
            holder.network.toTracking(e);
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        for (PlayerCapabilityHolder<?> holder : PlayerCapabilityHolder.INTERNAL_MAP.values()) {
            if (event.getTarget() instanceof ServerPlayer e) {
                holder.network.startTracking((ServerPlayer) event.getEntity(), e);
            }
        }
    }
}