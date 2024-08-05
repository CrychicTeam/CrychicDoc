package net.minecraft.client.renderer.debug;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class StructureRenderer implements DebugRenderer.SimpleDebugRenderer {

    private final Minecraft minecraft;

    private final Map<DimensionType, Map<String, BoundingBox>> postMainBoxes = Maps.newIdentityHashMap();

    private final Map<DimensionType, Map<String, BoundingBox>> postPiecesBoxes = Maps.newIdentityHashMap();

    private final Map<DimensionType, Map<String, Boolean>> startPiecesMap = Maps.newIdentityHashMap();

    private static final int MAX_RENDER_DIST = 500;

    public StructureRenderer(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, double double2, double double3, double double4) {
        Camera $$5 = this.minecraft.gameRenderer.getMainCamera();
        LevelAccessor $$6 = this.minecraft.level;
        DimensionType $$7 = $$6.m_6042_();
        BlockPos $$8 = BlockPos.containing($$5.getPosition().x, 0.0, $$5.getPosition().z);
        VertexConsumer $$9 = multiBufferSource1.getBuffer(RenderType.lines());
        if (this.postMainBoxes.containsKey($$7)) {
            for (BoundingBox $$10 : ((Map) this.postMainBoxes.get($$7)).values()) {
                if ($$8.m_123314_($$10.getCenter(), 500.0)) {
                    LevelRenderer.renderLineBox(poseStack0, $$9, (double) $$10.minX() - double2, (double) $$10.minY() - double3, (double) $$10.minZ() - double4, (double) ($$10.maxX() + 1) - double2, (double) ($$10.maxY() + 1) - double3, (double) ($$10.maxZ() + 1) - double4, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                }
            }
        }
        if (this.postPiecesBoxes.containsKey($$7)) {
            for (Entry<String, BoundingBox> $$11 : ((Map) this.postPiecesBoxes.get($$7)).entrySet()) {
                String $$12 = (String) $$11.getKey();
                BoundingBox $$13 = (BoundingBox) $$11.getValue();
                Boolean $$14 = (Boolean) ((Map) this.startPiecesMap.get($$7)).get($$12);
                if ($$8.m_123314_($$13.getCenter(), 500.0)) {
                    if ($$14) {
                        LevelRenderer.renderLineBox(poseStack0, $$9, (double) $$13.minX() - double2, (double) $$13.minY() - double3, (double) $$13.minZ() - double4, (double) ($$13.maxX() + 1) - double2, (double) ($$13.maxY() + 1) - double3, (double) ($$13.maxZ() + 1) - double4, 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, 1.0F, 0.0F);
                    } else {
                        LevelRenderer.renderLineBox(poseStack0, $$9, (double) $$13.minX() - double2, (double) $$13.minY() - double3, (double) $$13.minZ() - double4, (double) ($$13.maxX() + 1) - double2, (double) ($$13.maxY() + 1) - double3, (double) ($$13.maxZ() + 1) - double4, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F);
                    }
                }
            }
        }
    }

    public void addBoundingBox(BoundingBox boundingBox0, List<BoundingBox> listBoundingBox1, List<Boolean> listBoolean2, DimensionType dimensionType3) {
        if (!this.postMainBoxes.containsKey(dimensionType3)) {
            this.postMainBoxes.put(dimensionType3, Maps.newHashMap());
        }
        if (!this.postPiecesBoxes.containsKey(dimensionType3)) {
            this.postPiecesBoxes.put(dimensionType3, Maps.newHashMap());
            this.startPiecesMap.put(dimensionType3, Maps.newHashMap());
        }
        ((Map) this.postMainBoxes.get(dimensionType3)).put(boundingBox0.toString(), boundingBox0);
        for (int $$4 = 0; $$4 < listBoundingBox1.size(); $$4++) {
            BoundingBox $$5 = (BoundingBox) listBoundingBox1.get($$4);
            Boolean $$6 = (Boolean) listBoolean2.get($$4);
            ((Map) this.postPiecesBoxes.get(dimensionType3)).put($$5.toString(), $$5);
            ((Map) this.startPiecesMap.get(dimensionType3)).put($$5.toString(), $$6);
        }
    }

    @Override
    public void clear() {
        this.postMainBoxes.clear();
        this.postPiecesBoxes.clear();
        this.startPiecesMap.clear();
    }
}