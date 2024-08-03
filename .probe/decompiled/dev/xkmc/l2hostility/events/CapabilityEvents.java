package dev.xkmc.l2hostility.events;

import dev.xkmc.l2hostility.content.capability.chunk.ChunkCapSyncToClient;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficultyCap;
import dev.xkmc.l2hostility.content.capability.chunk.RegionalDifficultyModifier;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "l2hostility", bus = Bus.FORGE)
public class CapabilityEvents {

    private static final Set<ChunkDifficulty> PENDING = new LinkedHashSet();

    @SubscribeEvent
    public static void onAttachChunkCapabilities(AttachCapabilitiesEvent<LevelChunk> event) {
        event.addCapability(new ResourceLocation("l2hostility", "difficulty"), new ChunkDifficultyCap(event.getObject()));
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof LivingEntity entity && event.getEntity() instanceof ServerPlayer player && MobTraitCap.HOLDER.isProper(entity)) {
            ((MobTraitCap) MobTraitCap.HOLDER.get(entity)).syncToPlayer(entity, player);
        }
    }

    private static boolean initMob(LivingEntity mob, MobSpawnType type) {
        if (MobTraitCap.HOLDER.isProper(mob)) {
            MobTraitCap cap = (MobTraitCap) MobTraitCap.HOLDER.get(mob);
            if (!mob.m_9236_().isClientSide() && !cap.isInitialized()) {
                Optional<ChunkDifficulty> opt = ChunkDifficulty.at(mob.m_9236_(), mob.m_20183_());
                if (opt.isPresent()) {
                    cap.init(mob.m_9236_(), mob, (RegionalDifficultyModifier) opt.get());
                    if (type == MobSpawnType.NATURAL && cap.shouldDiscard(mob)) {
                        return true;
                    }
                    if (type == MobSpawnType.SPAWNER) {
                        cap.dropRate = LHConfig.COMMON.dropRateFromSpawner.get();
                    }
                }
            }
        }
        return false;
    }

    @SubscribeEvent
    public static void onEntitySpawn(MobSpawnEvent.FinalizeSpawn event) {
        LivingEntity mob = event.getEntity();
        if (initMob(mob, event.getSpawnType())) {
            event.setSpawnCancelled(true);
        }
    }

    @SubscribeEvent
    public static void livingTickEvent(LivingEvent.LivingTickEvent event) {
        LivingEntity mob = event.getEntity();
        if (mob.f_19797_ % 10 == 0) {
            if (Float.isNaN(mob.getHealth())) {
                mob.setHealth(0.0F);
            }
            if (Float.isNaN(mob.getAbsorptionAmount())) {
                mob.setAbsorptionAmount(0.0F);
            }
        }
        if (mob.isAlive()) {
            mob.getCapability(MobTraitCap.CAPABILITY).ifPresent(e -> e.tick(mob));
        }
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        LivingEntity mob = event.getEntity();
        if (!mob.m_9236_().isClientSide()) {
            LivingEntity killer = event.getEntity().getKillCredit();
            Player player = null;
            if (killer instanceof Player pl) {
                player = pl;
            } else if (killer instanceof OwnableEntity own && own.getOwner() instanceof Player pl) {
                player = pl;
            }
            if (MobTraitCap.HOLDER.isProper(mob)) {
                MobTraitCap cap = (MobTraitCap) MobTraitCap.HOLDER.get(mob);
                if (killer != null) {
                    cap.onKilled(mob, player);
                }
                if (player != null) {
                    PlayerDifficulty playerDiff = PlayerDifficulty.HOLDER.get(player);
                    playerDiff.addKillCredit(cap);
                    LevelChunk chunk = mob.m_9236_().getChunkAt(mob.m_20183_());
                    LazyOptional<ChunkDifficulty> opt = chunk.getCapability(ChunkDifficulty.CAPABILITY);
                    if (opt.resolve().isPresent()) {
                        ((ChunkDifficulty) opt.resolve().get()).addKillHistory(player, mob, cap);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            for (ChunkDifficulty e : PENDING) {
                L2Hostility.toTrackingChunk(e.chunk, new ChunkCapSyncToClient(e));
            }
            PENDING.clear();
        }
    }

    @SubscribeEvent
    public static void onStartTrackingChunk(ChunkWatchEvent.Watch event) {
        Optional<ChunkDifficulty> opt = event.getChunk().getCapability(ChunkDifficulty.CAPABILITY).resolve();
        if (!opt.isEmpty()) {
            L2Hostility.HANDLER.toClientPlayer(new ChunkCapSyncToClient((ChunkDifficulty) opt.get()), event.getPlayer());
        }
    }

    public static void markDirty(ChunkDifficulty chunk) {
        PENDING.add(chunk);
    }
}