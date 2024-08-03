package com.simibubi.create.content.contraptions.pulley;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractPulleyRenderer<T extends KineticBlockEntity> extends KineticBlockEntityRenderer<T> {

    private PartialModel halfRope;

    private PartialModel halfMagnet;

    public AbstractPulleyRenderer(BlockEntityRendererProvider.Context context, PartialModel halfRope, PartialModel halfMagnet) {
        super(context);
        this.halfRope = halfRope;
        this.halfMagnet = halfMagnet;
    }

    public boolean shouldRenderOffScreen(T p_188185_1_) {
        return true;
    }

    @Override
    protected void renderSafe(T be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!Backend.canUseInstancing(be.m_58904_())) {
            super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
            float offset = this.getOffset(be, partialTicks);
            boolean running = this.isRunning(be);
            Direction.Axis rotationAxis = ((IRotate) be.m_58900_().m_60734_()).getRotationAxis(be.m_58900_());
            kineticRotationTransform(this.getRotatedCoil(be), be, rotationAxis, AngleHelper.rad((double) (offset * 180.0F)), light).renderInto(ms, buffer.getBuffer(RenderType.solid()));
            Level world = be.m_58904_();
            BlockState blockState = be.m_58900_();
            BlockPos pos = be.m_58899_();
            SuperByteBuffer halfMagnet = CachedBufferer.partial(this.halfMagnet, blockState);
            SuperByteBuffer halfRope = CachedBufferer.partial(this.halfRope, blockState);
            SuperByteBuffer magnet = this.renderMagnet(be);
            SuperByteBuffer rope = this.renderRope(be);
            VertexConsumer vb = buffer.getBuffer(RenderType.solid());
            if (running || offset == 0.0F) {
                renderAt(world, offset > 0.25F ? magnet : halfMagnet, offset, pos, ms, vb);
            }
            float f = offset % 1.0F;
            if (offset > 0.75F && (f < 0.25F || f > 0.75F)) {
                renderAt(world, halfRope, f > 0.75F ? f - 1.0F : f, pos, ms, vb);
            }
            if (running) {
                for (int i = 0; (float) i < offset - 1.25F; i++) {
                    renderAt(world, rope, offset - (float) i - 1.0F, pos, ms, vb);
                }
            }
        }
    }

    public static void renderAt(LevelAccessor world, SuperByteBuffer partial, float offset, BlockPos pulleyPos, PoseStack ms, VertexConsumer buffer) {
        BlockPos actualPos = pulleyPos.below((int) offset);
        int light = LevelRenderer.getLightColor(world, world.m_8055_(actualPos), actualPos);
        partial.translate(0.0, (double) (-offset), 0.0).light(light).renderInto(ms, buffer);
    }

    protected abstract Direction.Axis getShaftAxis(T var1);

    protected abstract PartialModel getCoil();

    protected abstract SuperByteBuffer renderRope(T var1);

    protected abstract SuperByteBuffer renderMagnet(T var1);

    protected abstract float getOffset(T var1, float var2);

    protected abstract boolean isRunning(T var1);

    @Override
    protected BlockState getRenderedBlockState(T be) {
        return shaft(this.getShaftAxis(be));
    }

    protected SuperByteBuffer getRotatedCoil(T be) {
        BlockState blockState = be.m_58900_();
        return CachedBufferer.partialFacing(this.getCoil(), blockState, Direction.get(Direction.AxisDirection.POSITIVE, this.getShaftAxis(be)));
    }

    @Override
    public int getViewDistance() {
        return 256;
    }
}