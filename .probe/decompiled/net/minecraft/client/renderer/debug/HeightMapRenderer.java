package net.minecraft.client.renderer.debug;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import org.joml.Vector3f;

public class HeightMapRenderer implements DebugRenderer.SimpleDebugRenderer {

    private final Minecraft minecraft;

    private static final int CHUNK_DIST = 2;

    private static final float BOX_HEIGHT = 0.09375F;

    public HeightMapRenderer(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, double double2, double double3, double double4) {
        LevelAccessor $$5 = this.minecraft.level;
        VertexConsumer $$6 = multiBufferSource1.getBuffer(RenderType.debugFilledBox());
        BlockPos $$7 = BlockPos.containing(double2, 0.0, double4);
        for (int $$8 = -2; $$8 <= 2; $$8++) {
            for (int $$9 = -2; $$9 <= 2; $$9++) {
                ChunkAccess $$10 = $$5.m_46865_($$7.offset($$8 * 16, 0, $$9 * 16));
                for (Entry<Heightmap.Types, Heightmap> $$11 : $$10.getHeightmaps()) {
                    Heightmap.Types $$12 = (Heightmap.Types) $$11.getKey();
                    ChunkPos $$13 = $$10.getPos();
                    Vector3f $$14 = this.getColor($$12);
                    for (int $$15 = 0; $$15 < 16; $$15++) {
                        for (int $$16 = 0; $$16 < 16; $$16++) {
                            int $$17 = SectionPos.sectionToBlockCoord($$13.x, $$15);
                            int $$18 = SectionPos.sectionToBlockCoord($$13.z, $$16);
                            float $$19 = (float) ((double) ((float) $$5.m_6924_($$12, $$17, $$18) + (float) $$12.ordinal() * 0.09375F) - double3);
                            LevelRenderer.addChainedFilledBoxVertices(poseStack0, $$6, (double) ((float) $$17 + 0.25F) - double2, (double) $$19, (double) ((float) $$18 + 0.25F) - double4, (double) ((float) $$17 + 0.75F) - double2, (double) ($$19 + 0.09375F), (double) ((float) $$18 + 0.75F) - double4, $$14.x(), $$14.y(), $$14.z(), 1.0F);
                        }
                    }
                }
            }
        }
    }

    private Vector3f getColor(Heightmap.Types heightmapTypes0) {
        return switch(heightmapTypes0) {
            case WORLD_SURFACE_WG ->
                new Vector3f(1.0F, 1.0F, 0.0F);
            case OCEAN_FLOOR_WG ->
                new Vector3f(1.0F, 0.0F, 1.0F);
            case WORLD_SURFACE ->
                new Vector3f(0.0F, 0.7F, 0.0F);
            case OCEAN_FLOOR ->
                new Vector3f(0.0F, 0.0F, 0.5F);
            case MOTION_BLOCKING ->
                new Vector3f(0.0F, 0.3F, 0.3F);
            case MOTION_BLOCKING_NO_LEAVES ->
                new Vector3f(0.0F, 0.5F, 0.5F);
        };
    }
}