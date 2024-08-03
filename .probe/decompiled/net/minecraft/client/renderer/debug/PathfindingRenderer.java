package net.minecraft.client.renderer.debug;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Locale;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;

public class PathfindingRenderer implements DebugRenderer.SimpleDebugRenderer {

    private final Map<Integer, Path> pathMap = Maps.newHashMap();

    private final Map<Integer, Float> pathMaxDist = Maps.newHashMap();

    private final Map<Integer, Long> creationMap = Maps.newHashMap();

    private static final long TIMEOUT = 5000L;

    private static final float MAX_RENDER_DIST = 80.0F;

    private static final boolean SHOW_OPEN_CLOSED = true;

    private static final boolean SHOW_OPEN_CLOSED_COST_MALUS = false;

    private static final boolean SHOW_OPEN_CLOSED_NODE_TYPE_WITH_TEXT = false;

    private static final boolean SHOW_OPEN_CLOSED_NODE_TYPE_WITH_BOX = true;

    private static final boolean SHOW_GROUND_LABELS = true;

    private static final float TEXT_SCALE = 0.02F;

    public void addPath(int int0, Path path1, float float2) {
        this.pathMap.put(int0, path1);
        this.creationMap.put(int0, Util.getMillis());
        this.pathMaxDist.put(int0, float2);
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, double double2, double double3, double double4) {
        if (!this.pathMap.isEmpty()) {
            long $$5 = Util.getMillis();
            for (Integer $$6 : this.pathMap.keySet()) {
                Path $$7 = (Path) this.pathMap.get($$6);
                float $$8 = (Float) this.pathMaxDist.get($$6);
                renderPath(poseStack0, multiBufferSource1, $$7, $$8, true, true, double2, double3, double4);
            }
            for (Integer $$9 : (Integer[]) this.creationMap.keySet().toArray(new Integer[0])) {
                if ($$5 - (Long) this.creationMap.get($$9) > 5000L) {
                    this.pathMap.remove($$9);
                    this.creationMap.remove($$9);
                }
            }
        }
    }

    public static void renderPath(PoseStack poseStack0, MultiBufferSource multiBufferSource1, Path path2, float float3, boolean boolean4, boolean boolean5, double double6, double double7, double double8) {
        renderPathLine(poseStack0, multiBufferSource1.getBuffer(RenderType.debugLineStrip(6.0)), path2, double6, double7, double8);
        BlockPos $$9 = path2.getTarget();
        if (distanceToCamera($$9, double6, double7, double8) <= 80.0F) {
            DebugRenderer.renderFilledBox(poseStack0, multiBufferSource1, new AABB((double) ((float) $$9.m_123341_() + 0.25F), (double) ((float) $$9.m_123342_() + 0.25F), (double) $$9.m_123343_() + 0.25, (double) ((float) $$9.m_123341_() + 0.75F), (double) ((float) $$9.m_123342_() + 0.75F), (double) ((float) $$9.m_123343_() + 0.75F)).move(-double6, -double7, -double8), 0.0F, 1.0F, 0.0F, 0.5F);
            for (int $$10 = 0; $$10 < path2.getNodeCount(); $$10++) {
                Node $$11 = path2.getNode($$10);
                if (distanceToCamera($$11.asBlockPos(), double6, double7, double8) <= 80.0F) {
                    float $$12 = $$10 == path2.getNextNodeIndex() ? 1.0F : 0.0F;
                    float $$13 = $$10 == path2.getNextNodeIndex() ? 0.0F : 1.0F;
                    DebugRenderer.renderFilledBox(poseStack0, multiBufferSource1, new AABB((double) ((float) $$11.x + 0.5F - float3), (double) ((float) $$11.y + 0.01F * (float) $$10), (double) ((float) $$11.z + 0.5F - float3), (double) ((float) $$11.x + 0.5F + float3), (double) ((float) $$11.y + 0.25F + 0.01F * (float) $$10), (double) ((float) $$11.z + 0.5F + float3)).move(-double6, -double7, -double8), $$12, 0.0F, $$13, 0.5F);
                }
            }
        }
        if (boolean4) {
            for (Node $$14 : path2.getClosedSet()) {
                if (distanceToCamera($$14.asBlockPos(), double6, double7, double8) <= 80.0F) {
                    DebugRenderer.renderFilledBox(poseStack0, multiBufferSource1, new AABB((double) ((float) $$14.x + 0.5F - float3 / 2.0F), (double) ((float) $$14.y + 0.01F), (double) ((float) $$14.z + 0.5F - float3 / 2.0F), (double) ((float) $$14.x + 0.5F + float3 / 2.0F), (double) $$14.y + 0.1, (double) ((float) $$14.z + 0.5F + float3 / 2.0F)).move(-double6, -double7, -double8), 1.0F, 0.8F, 0.8F, 0.5F);
                }
            }
            for (Node $$15 : path2.getOpenSet()) {
                if (distanceToCamera($$15.asBlockPos(), double6, double7, double8) <= 80.0F) {
                    DebugRenderer.renderFilledBox(poseStack0, multiBufferSource1, new AABB((double) ((float) $$15.x + 0.5F - float3 / 2.0F), (double) ((float) $$15.y + 0.01F), (double) ((float) $$15.z + 0.5F - float3 / 2.0F), (double) ((float) $$15.x + 0.5F + float3 / 2.0F), (double) $$15.y + 0.1, (double) ((float) $$15.z + 0.5F + float3 / 2.0F)).move(-double6, -double7, -double8), 0.8F, 1.0F, 1.0F, 0.5F);
                }
            }
        }
        if (boolean5) {
            for (int $$16 = 0; $$16 < path2.getNodeCount(); $$16++) {
                Node $$17 = path2.getNode($$16);
                if (distanceToCamera($$17.asBlockPos(), double6, double7, double8) <= 80.0F) {
                    DebugRenderer.renderFloatingText(poseStack0, multiBufferSource1, String.valueOf($$17.type), (double) $$17.x + 0.5, (double) $$17.y + 0.75, (double) $$17.z + 0.5, -1, 0.02F, true, 0.0F, true);
                    DebugRenderer.renderFloatingText(poseStack0, multiBufferSource1, String.format(Locale.ROOT, "%.2f", $$17.costMalus), (double) $$17.x + 0.5, (double) $$17.y + 0.25, (double) $$17.z + 0.5, -1, 0.02F, true, 0.0F, true);
                }
            }
        }
    }

    public static void renderPathLine(PoseStack poseStack0, VertexConsumer vertexConsumer1, Path path2, double double3, double double4, double double5) {
        for (int $$6 = 0; $$6 < path2.getNodeCount(); $$6++) {
            Node $$7 = path2.getNode($$6);
            if (!(distanceToCamera($$7.asBlockPos(), double3, double4, double5) > 80.0F)) {
                float $$8 = (float) $$6 / (float) path2.getNodeCount() * 0.33F;
                int $$9 = $$6 == 0 ? 0 : Mth.hsvToRgb($$8, 0.9F, 0.9F);
                int $$10 = $$9 >> 16 & 0xFF;
                int $$11 = $$9 >> 8 & 0xFF;
                int $$12 = $$9 & 0xFF;
                vertexConsumer1.vertex(poseStack0.last().pose(), (float) ((double) $$7.x - double3 + 0.5), (float) ((double) $$7.y - double4 + 0.5), (float) ((double) $$7.z - double5 + 0.5)).color($$10, $$11, $$12, 255).endVertex();
            }
        }
    }

    private static float distanceToCamera(BlockPos blockPos0, double double1, double double2, double double3) {
        return (float) (Math.abs((double) blockPos0.m_123341_() - double1) + Math.abs((double) blockPos0.m_123342_() - double2) + Math.abs((double) blockPos0.m_123343_() - double3));
    }
}