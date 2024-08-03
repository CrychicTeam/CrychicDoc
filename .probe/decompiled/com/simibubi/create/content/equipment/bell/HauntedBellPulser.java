package com.simibubi.create.content.equipment.bell;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPackets;
import com.simibubi.create.foundation.utility.IntAttached;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.PacketDistributor;

@EventBusSubscriber
public class HauntedBellPulser {

    public static final int DISTANCE = 3;

    public static final int RECHARGE_TICKS = 8;

    public static final int WARMUP_TICKS = 10;

    public static final Cache<UUID, IntAttached<Entity>> WARMUP = CacheBuilder.newBuilder().expireAfterAccess(250L, TimeUnit.MILLISECONDS).build();

    @SubscribeEvent
    public static void hauntedBellCreatesPulse(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (event.side == LogicalSide.SERVER) {
                if (!event.player.isSpectator()) {
                    if (event.player.m_21093_(AllBlocks.HAUNTED_BELL::isIn)) {
                        Entity player = event.player;
                        boolean firstPulse = false;
                        try {
                            IntAttached<Entity> ticker = (IntAttached<Entity>) WARMUP.get(player.getUUID(), () -> IntAttached.with(10, player));
                            firstPulse = ticker.getFirst() == 1;
                            ticker.decrement();
                            if (!ticker.isOrBelowZero()) {
                                return;
                            }
                        } catch (ExecutionException var5) {
                        }
                        long gameTime = player.level().getGameTime();
                        if (firstPulse || gameTime % 8L != 0L) {
                            sendPulse(player.level(), event.player.m_20183_(), 3, false);
                        }
                    }
                }
            }
        }
    }

    public static void sendPulse(Level world, BlockPos pos, int distance, boolean canOverlap) {
        LevelChunk chunk = world.getChunkAt(pos);
        AllPackets.getChannel().send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), new SoulPulseEffectPacket(pos, distance, canOverlap));
    }
}