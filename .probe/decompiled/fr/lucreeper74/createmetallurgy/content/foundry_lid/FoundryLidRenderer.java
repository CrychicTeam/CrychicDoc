package fr.lucreeper74.createmetallurgy.content.foundry_lid;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import fr.lucreeper74.createmetallurgy.registries.CMPartialModels;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class FoundryLidRenderer extends SafeBlockEntityRenderer<FoundryLidBlockEntity> {

    public FoundryLidRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(FoundryLidBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        if ((Boolean) be.m_58900_().m_61143_(FoundryLidBlock.ON_FOUNDRY_BASIN)) {
            BlockState blockState = be.m_58900_();
            Direction facing = (Direction) blockState.m_61143_(BlockStateProperties.HORIZONTAL_FACING);
            float dialPivot = 0.359375F;
            ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) CachedBufferer.partial(CMPartialModels.THERMOMETER_GAUGE, blockState).centre()).rotateY((double) (-facing.toYRot()))).unCentre()).renderInto(ms, bufferSource.getBuffer(RenderType.solid()));
            ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) CachedBufferer.partial(AllPartialModels.BOILER_GAUGE_DIAL, blockState).centre()).rotateY((double) (-facing.toYRot()))).unCentre()).translate(0.0, (double) dialPivot, (double) dialPivot).rotateX((double) (be.gauge.getValue(partialTicks) * -90.0F))).translate(0.0, (double) (-dialPivot), (double) (-dialPivot)).renderInto(ms, bufferSource.getBuffer(RenderType.solid()));
        }
    }
}