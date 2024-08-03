package snownee.kiwi.customization.placement;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import snownee.kiwi.util.NotNullByDefault;
import snownee.kiwi.util.VoxelUtil;

@NotNullByDefault
public class PlaceDebugRenderer implements DebugRenderer.SimpleDebugRenderer {

    private static final PlaceDebugRenderer INSTANCE = new PlaceDebugRenderer();

    private List<PlaceDebugRenderer.SlotRenderInstance> slots = List.of();

    private long lastUpdateTime;

    public static PlaceDebugRenderer getInstance() {
        return INSTANCE;
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, double pCamX, double pCamY, double pCamZ) {
        Minecraft mc = Minecraft.getInstance();
        if (!mc.isPaused()) {
            long millis = Util.getMillis();
            if (millis - this.lastUpdateTime > 300L) {
                this.lastUpdateTime = millis;
                Entity entity = mc.gameRenderer.getMainCamera().getEntity();
                Level level = entity.level();
                this.slots = BlockPos.betweenClosedStream(entity.getBoundingBox().inflate(4.0)).map(BlockPos::m_7949_).flatMap(pos -> {
                    BlockState blockState = level.getBlockState(pos);
                    return Direction.stream().flatMap(side -> PlaceSlot.find(blockState, side).stream().map(slot -> PlaceDebugRenderer.SlotRenderInstance.create(slot, pos, side)));
                }).toList();
            }
            VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.lines());
            for (PlaceDebugRenderer.SlotRenderInstance instance : this.slots) {
                float r = (float) (instance.color >> 16 & 0xFF) / 255.0F;
                float g = (float) (instance.color >> 8 & 0xFF) / 255.0F;
                float b = (float) (instance.color & 0xFF) / 255.0F;
                LevelRenderer.renderVoxelShape(pPoseStack, vertexconsumer, instance.shape, (double) instance.pos.m_123341_() - pCamX, (double) instance.pos.m_123342_() - pCamY, (double) instance.pos.m_123343_() - pCamZ, r, g, b, 1.0F, true);
            }
        }
    }

    private static record SlotRenderInstance(PlaceSlot slot, BlockPos pos, Direction side, VoxelShape shape, int color) {

        private static final VoxelShape SHAPE_DOWN = Block.box(6.0, -0.5, 6.0, 10.0, 0.5, 10.0);

        private static final VoxelShape[] SHAPES = (VoxelShape[]) Direction.stream().map(side -> VoxelUtil.rotate(SHAPE_DOWN, side)).toArray(VoxelShape[]::new);

        private static PlaceDebugRenderer.SlotRenderInstance create(PlaceSlot slot, BlockPos pos, Direction side) {
            String tag = slot.primaryTag();
            int color = 16777215;
            if (tag.endsWith("side")) {
                color = 16755370;
            } else if (tag.endsWith("front") || tag.endsWith("top")) {
                color = 11206570;
            } else if (tag.endsWith("back") || tag.endsWith("bottom")) {
                color = 11184895;
            }
            return new PlaceDebugRenderer.SlotRenderInstance(slot, pos, side, SHAPES[side.ordinal()], color);
        }
    }
}