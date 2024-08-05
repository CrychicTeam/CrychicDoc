package com.simibubi.create.content.equipment.armor;

import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class BacktankRenderer extends KineticBlockEntityRenderer<BacktankBlockEntity> {

    public BacktankRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected void renderSafe(BacktankBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        BlockState blockState = be.m_58900_();
        SuperByteBuffer cogs = CachedBufferer.partial(getCogsModel(blockState), blockState);
        ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) cogs.centre()).rotateY((double) (180.0F + AngleHelper.horizontalAngle((Direction) blockState.m_61143_(BacktankBlock.HORIZONTAL_FACING))))).unCentre()).translate(0.0, 0.40625, 0.6875).rotate(Direction.EAST, AngleHelper.rad((double) (be.getSpeed() / 4.0F * AnimationTickHolder.getRenderTime(be.m_58904_()) % 360.0F)))).translate(0.0, -0.40625, -0.6875);
        cogs.light(light).renderInto(ms, buffer.getBuffer(RenderType.solid()));
    }

    protected SuperByteBuffer getRotatedModel(BacktankBlockEntity be, BlockState state) {
        return CachedBufferer.partial(getShaftModel(state), state);
    }

    public static PartialModel getCogsModel(BlockState state) {
        return AllBlocks.NETHERITE_BACKTANK.has(state) ? AllPartialModels.NETHERITE_BACKTANK_COGS : AllPartialModels.COPPER_BACKTANK_COGS;
    }

    public static PartialModel getShaftModel(BlockState state) {
        return AllBlocks.NETHERITE_BACKTANK.has(state) ? AllPartialModels.NETHERITE_BACKTANK_SHAFT : AllPartialModels.COPPER_BACKTANK_SHAFT;
    }
}