package net.minecraft.client.renderer.debug;

import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

public class NeighborsUpdateRenderer implements DebugRenderer.SimpleDebugRenderer {

    private final Minecraft minecraft;

    private final Map<Long, Map<BlockPos, Integer>> lastUpdate = Maps.newTreeMap(Ordering.natural().reverse());

    NeighborsUpdateRenderer(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    public void addUpdate(long long0, BlockPos blockPos1) {
        Map<BlockPos, Integer> $$2 = (Map<BlockPos, Integer>) this.lastUpdate.computeIfAbsent(long0, p_113606_ -> Maps.newHashMap());
        int $$3 = (Integer) $$2.getOrDefault(blockPos1, 0);
        $$2.put(blockPos1, $$3 + 1);
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, double double2, double double3, double double4) {
        long $$5 = this.minecraft.level.m_46467_();
        int $$6 = 200;
        double $$7 = 0.0025;
        Set<BlockPos> $$8 = Sets.newHashSet();
        Map<BlockPos, Integer> $$9 = Maps.newHashMap();
        VertexConsumer $$10 = multiBufferSource1.getBuffer(RenderType.lines());
        Iterator<Entry<Long, Map<BlockPos, Integer>>> $$11 = this.lastUpdate.entrySet().iterator();
        while ($$11.hasNext()) {
            Entry<Long, Map<BlockPos, Integer>> $$12 = (Entry<Long, Map<BlockPos, Integer>>) $$11.next();
            Long $$13 = (Long) $$12.getKey();
            Map<BlockPos, Integer> $$14 = (Map<BlockPos, Integer>) $$12.getValue();
            long $$15 = $$5 - $$13;
            if ($$15 > 200L) {
                $$11.remove();
            } else {
                for (Entry<BlockPos, Integer> $$16 : $$14.entrySet()) {
                    BlockPos $$17 = (BlockPos) $$16.getKey();
                    Integer $$18 = (Integer) $$16.getValue();
                    if ($$8.add($$17)) {
                        AABB $$19 = new AABB(BlockPos.ZERO).inflate(0.002).deflate(0.0025 * (double) $$15).move((double) $$17.m_123341_(), (double) $$17.m_123342_(), (double) $$17.m_123343_()).move(-double2, -double3, -double4);
                        LevelRenderer.renderLineBox(poseStack0, $$10, $$19.minX, $$19.minY, $$19.minZ, $$19.maxX, $$19.maxY, $$19.maxZ, 1.0F, 1.0F, 1.0F, 1.0F);
                        $$9.put($$17, $$18);
                    }
                }
            }
        }
        for (Entry<BlockPos, Integer> $$20 : $$9.entrySet()) {
            BlockPos $$21 = (BlockPos) $$20.getKey();
            Integer $$22 = (Integer) $$20.getValue();
            DebugRenderer.renderFloatingText(poseStack0, multiBufferSource1, String.valueOf($$22), $$21.m_123341_(), $$21.m_123342_(), $$21.m_123343_(), -1);
        }
    }
}