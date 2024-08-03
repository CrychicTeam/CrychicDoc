package dev.xkmc.l2hostility.compat.gateway;

import com.mojang.datafixers.util.Pair;
import dev.shadowsoffire.gateways.entity.GatewayEntity;
import dev.shadowsoffire.gateways.event.GateEvent.WaveEntitySpawned;
import dev.shadowsoffire.gateways.gate.GatewayRegistry;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.chunk.RegionalDifficultyModifier;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GatewayToEternityCompat {

    private static final ThreadLocal<Pair<GatewayEntity, WaveData>> CURRENT = new ThreadLocal();

    @SubscribeEvent
    public static void onSpawn(WaveEntitySpawned event) {
        Pair<GatewayEntity, WaveData> prev = (Pair<GatewayEntity, WaveData>) CURRENT.get();
        GatewayEntity gate = event.getEntity();
        int wave = event.getEntity().getWave();
        ResourceLocation rl = GatewayRegistry.INSTANCE.getKey(event.getEntity().getGateway());
        if (rl != null) {
            WaveId id = new WaveId(rl, wave);
            WaveData data;
            if (prev != null && gate == prev.getFirst() && ((WaveData) prev.getSecond()).id.equals(id)) {
                data = (WaveData) prev.getSecond();
            } else {
                CURRENT.set(Pair.of(gate, data = new WaveData(id)));
            }
            EntityConfig.Config config = L2Hostility.ENTITY.getMerged().get(event.getWaveEntity().m_6095_(), rl, WaveData.class, data);
            if (config != null) {
                initMob(event.getWaveEntity(), config);
            }
        }
    }

    private static void initMob(LivingEntity mob, EntityConfig.Config config) {
        if (MobTraitCap.HOLDER.isProper(mob)) {
            MobTraitCap cap = (MobTraitCap) MobTraitCap.HOLDER.get(mob);
            if (!mob.m_9236_().isClientSide() && !cap.isInitialized()) {
                Optional<ChunkDifficulty> opt = ChunkDifficulty.at(mob.m_9236_(), mob.m_20183_());
                if (opt.isPresent()) {
                    cap.setConfigCache(config);
                    cap.init(mob.m_9236_(), mob, (RegionalDifficultyModifier) opt.get());
                    cap.dropRate = LHConfig.COMMON.dropRateFromSpawner.get();
                }
            }
        }
    }
}