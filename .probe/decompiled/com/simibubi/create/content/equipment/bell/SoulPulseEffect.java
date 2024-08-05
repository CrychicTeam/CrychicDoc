package com.simibubi.create.content.equipment.bell;

import com.google.common.collect.Streams;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SoulPulseEffect {

    public static final int MAX_DISTANCE = 11;

    private static final List<List<BlockPos>> LAYERS = genLayers();

    private static final int WAITING_TICKS = 100;

    public static final int TICKS_PER_LAYER = 6;

    private int ticks;

    public final BlockPos pos;

    public final int distance;

    public final List<BlockPos> added;

    public SoulPulseEffect(BlockPos pos, int distance, boolean canOverlap) {
        this.ticks = 6 * distance;
        this.pos = pos;
        this.distance = distance;
        this.added = canOverlap ? null : new ArrayList();
    }

    public boolean finished() {
        return this.ticks <= -100;
    }

    public boolean canOverlap() {
        return this.added == null;
    }

    public List<BlockPos> tick(Level world) {
        if (this.finished()) {
            return null;
        } else {
            this.ticks--;
            if (this.ticks >= 0 && this.ticks % 6 == 0) {
                List<BlockPos> spawns = this.getPotentialSoulSpawns(world);
                while (spawns.isEmpty() && this.ticks > 0) {
                    this.ticks -= 6;
                    spawns.addAll(this.getPotentialSoulSpawns(world));
                }
                return spawns;
            } else {
                return null;
            }
        }
    }

    public int currentLayerIdx() {
        return this.distance - this.ticks / 6 - 1;
    }

    public List<BlockPos> getPotentialSoulSpawns(Level world) {
        return (List<BlockPos>) (world == null ? new ArrayList() : (List) getLayer(this.currentLayerIdx()).map(p -> p.offset(this.pos)).filter(p -> canSpawnSoulAt(world, p, true)).collect(Collectors.toList()));
    }

    public static boolean isDark(Level world, BlockPos at) {
        return world.m_45517_(LightLayer.BLOCK, at) < 1;
    }

    public static boolean canSpawnSoulAt(Level world, BlockPos at, boolean ignoreLight) {
        EntityType<?> dummy = EntityType.ZOMBIE;
        double dummyWidth = 0.2;
        double dummyHeight = 0.75;
        double w2 = dummyWidth / 2.0;
        return world != null && NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, world, at, dummy) && (ignoreLight || isDark(world, at)) && Streams.stream(world.m_186434_(null, new AABB((double) at.m_123341_() + 0.5 - w2, (double) at.m_123342_(), (double) at.m_123343_() + 0.5 - w2, (double) at.m_123341_() + 0.5 + w2, (double) at.m_123342_() + dummyHeight, (double) at.m_123343_() + 0.5 + w2))).allMatch(VoxelShape::m_83281_);
    }

    public void spawnParticles(Level world, BlockPos at) {
        if (world != null && world.isClientSide) {
            Vec3 p = Vec3.atLowerCornerOf(at);
            if (this.canOverlap()) {
                world.addAlwaysVisibleParticle((ParticleOptions) ((int) Math.round(VecHelper.getCenterOf(this.pos).distanceTo(VecHelper.getCenterOf(at))) >= this.distance ? new SoulParticle.PerimeterData() : new SoulParticle.ExpandingPerimeterData()), p.x + 0.5, p.y + 0.5, p.z + 0.5, 0.0, 0.0, 0.0);
            }
            if (isDark(world, at)) {
                world.addAlwaysVisibleParticle(new SoulParticle.Data(), p.x + 0.5, p.y + 0.5, p.z + 0.5, 0.0, 0.0, 0.0);
                world.addParticle(new SoulBaseParticle.Data(), p.x + 0.5, p.y + 0.01, p.z + 0.5, 0.0, 0.0, 0.0);
            }
        }
    }

    private static List<List<BlockPos>> genLayers() {
        List<List<BlockPos>> layers = new ArrayList();
        for (int i = 0; i < 11; i++) {
            layers.add(new ArrayList());
        }
        for (int x = 0; x < 11; x++) {
            for (int y = 0; y < 11; y++) {
                for (int z = 0; z < 11; z++) {
                    BlockPos candidate = new BlockPos(x, y, z);
                    int dist = (int) Math.round(Math.sqrt(candidate.m_123331_(BlockPos.ZERO)));
                    if (dist <= 11) {
                        if (dist <= 0) {
                            dist = 1;
                        }
                        List<BlockPos> layer = (List<BlockPos>) layers.get(dist - 1);
                        int start = layer.size();
                        int end = start + 1;
                        layer.add(candidate);
                        if (candidate.m_123341_() != 0) {
                            layer.add(new BlockPos(-candidate.m_123341_(), candidate.m_123342_(), candidate.m_123343_()));
                            end++;
                        }
                        if (candidate.m_123342_() != 0) {
                            for (int i = start; i < end; i++) {
                                BlockPos prev = (BlockPos) layer.get(i);
                                layer.add(new BlockPos(prev.m_123341_(), -prev.m_123342_(), prev.m_123343_()));
                            }
                            end += end - start;
                        }
                        if (candidate.m_123343_() != 0) {
                            for (int i = start; i < end; i++) {
                                BlockPos prev = (BlockPos) layer.get(i);
                                layer.add(new BlockPos(prev.m_123341_(), prev.m_123342_(), -prev.m_123343_()));
                            }
                        }
                    }
                }
            }
        }
        return layers;
    }

    public static Stream<BlockPos> getLayer(int idx) {
        return idx >= 0 && idx < 11 ? ((List) LAYERS.get(idx)).stream() : Stream.empty();
    }
}