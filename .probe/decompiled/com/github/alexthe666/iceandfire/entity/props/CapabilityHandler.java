package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber
public class CapabilityHandler {

    public static final Capability<EntityData> ENTITY_DATA_CAPABILITY = CapabilityManager.get(new CapabilityToken<EntityData>() {
    });

    public static final ResourceLocation ENTITY_DATA = new ResourceLocation("iceandfire", "entity_data");

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            event.addCapability(ENTITY_DATA, new EntityDataProvider());
        }
    }

    @SubscribeEvent
    public static void handleInitialSync(EntityJoinLevelEvent event) {
        syncEntityData(event.getEntity());
    }

    @SubscribeEvent
    public static void removeCachedEntry(EntityLeaveLevelEvent event) {
        EntityDataProvider.removeCachedEntry(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof LivingEntity target && event.getEntity() instanceof ServerPlayer serverPlayer) {
            EntityDataProvider.getCapability(target).ifPresent(data -> IceAndFire.sendMSGToPlayer(new SyncEntityData(target.m_19879_(), data.serialize()), serverPlayer));
        }
    }

    @SubscribeEvent
    public static void tickData(LivingEvent.LivingTickEvent event) {
        EntityDataProvider.getCapability(event.getEntity()).ifPresent(data -> data.tick(event.getEntity()));
    }

    public static void syncEntityData(Entity entity) {
        if (!entity.level().isClientSide() && entity instanceof LivingEntity) {
            if (entity instanceof ServerPlayer serverPlayer) {
                EntityDataProvider.getCapability(entity).ifPresent(data -> IceAndFire.NETWORK_WRAPPER.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> serverPlayer), new SyncEntityData(entity.getId(), data.serialize())));
            } else {
                EntityDataProvider.getCapability(entity).ifPresent(data -> IceAndFire.NETWORK_WRAPPER.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new SyncEntityData(entity.getId(), data.serialize())));
            }
        }
    }

    @Nullable
    public static Player getLocalPlayer() {
        return IceAndFire.PROXY.getClientSidePlayer();
    }
}