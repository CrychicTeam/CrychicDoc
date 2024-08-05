package dev.xkmc.l2library.init.events;

import dev.xkmc.l2library.base.effects.EffectToClient;
import dev.xkmc.l2library.init.L2Library;
import dev.xkmc.l2library.init.data.L2TagGen;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(modid = "l2library", bus = Bus.FORGE)
public class EffectSyncEvents {

    @Deprecated
    public static final Set<MobEffect> TRACKED = new HashSet();

    public static boolean tracked(MobEffect eff) {
        return TRACKED.contains(eff) || ForgeRegistries.MOB_EFFECTS.tags().getTag(L2TagGen.TRACKED_EFFECTS).contains(eff);
    }

    @SubscribeEvent
    public static void onPotionAddedEvent(MobEffectEvent.Added event) {
        if (tracked(event.getEffectInstance().getEffect())) {
            onEffectAppear(event.getEffectInstance().getEffect(), event.getEntity(), event.getEffectInstance().getAmplifier());
        }
    }

    @SubscribeEvent
    public static void onPotionRemoveEvent(MobEffectEvent.Remove event) {
        if (event.getEffectInstance() != null && tracked(event.getEffectInstance().getEffect())) {
            onEffectDisappear(event.getEffectInstance().getEffect(), event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onPotionExpiryEvent(MobEffectEvent.Expired event) {
        if (event.getEffectInstance() != null && tracked(event.getEffectInstance().getEffect())) {
            onEffectDisappear(event.getEffectInstance().getEffect(), event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof LivingEntity le) {
            for (MobEffect eff : le.getActiveEffectsMap().keySet()) {
                if (tracked(eff)) {
                    onEffectAppear(eff, le, ((MobEffectInstance) le.getActiveEffectsMap().get(eff)).getAmplifier());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerStopTracking(PlayerEvent.StopTracking event) {
        if (event.getTarget() instanceof LivingEntity le) {
            for (MobEffect eff : le.getActiveEffectsMap().keySet()) {
                if (tracked(eff)) {
                    onEffectDisappear(eff, le);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onServerPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer e = (ServerPlayer) event.getEntity();
        if (e != null) {
            for (MobEffect eff : e.m_21221_().keySet()) {
                if (tracked(eff)) {
                    onEffectAppear(eff, e, ((MobEffectInstance) e.m_21221_().get(eff)).getAmplifier());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onServerPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerPlayer e = (ServerPlayer) event.getEntity();
        if (e != null) {
            for (MobEffect eff : e.m_21221_().keySet()) {
                if (tracked(eff)) {
                    onEffectDisappear(eff, e);
                }
            }
        }
    }

    private static void onEffectAppear(MobEffect eff, LivingEntity e, int lv) {
        if (!e.m_9236_().isClientSide()) {
            L2Library.PACKET_HANDLER.toTrackingPlayers(new EffectToClient(e.m_19879_(), eff, true, lv), e);
        }
    }

    private static void onEffectDisappear(MobEffect eff, LivingEntity e) {
        if (!e.m_9236_().isClientSide()) {
            L2Library.PACKET_HANDLER.toTrackingPlayers(new EffectToClient(e.m_19879_(), eff, false, 0), e);
        }
    }
}