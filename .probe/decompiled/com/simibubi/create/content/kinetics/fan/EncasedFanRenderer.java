package com.simibubi.create.content.kinetics.fan;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class EncasedFanRenderer extends KineticBlockEntityRenderer<EncasedFanBlockEntity> {

    public EncasedFanRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected void renderSafe(EncasedFanBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!Backend.canUseInstancing(be.m_58904_())) {
            Direction direction = (Direction) be.m_58900_().m_61143_(BlockStateProperties.FACING);
            VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());
            int lightBehind = LevelRenderer.getLightColor(be.m_58904_(), be.m_58899_().relative(direction.getOpposite()));
            int lightInFront = LevelRenderer.getLightColor(be.m_58904_(), be.m_58899_().relative(direction));
            SuperByteBuffer shaftHalf = CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, be.m_58900_(), direction.getOpposite());
            SuperByteBuffer fanInner = CachedBufferer.partialFacing(AllPartialModels.ENCASED_FAN_INNER, be.m_58900_(), direction.getOpposite());
            float time = AnimationTickHolder.getRenderTime(be.m_58904_());
            float speed = be.getSpeed() * 5.0F;
            if (speed > 0.0F) {
                speed = Mth.clamp(speed, 80.0F, 1280.0F);
            }
            if (speed < 0.0F) {
                speed = Mth.clamp(speed, -1280.0F, -80.0F);
            }
            float angle = time * speed * 3.0F / 10.0F % 360.0F;
            angle = angle / 180.0F * (float) Math.PI;
            standardKineticRotationTransform(shaftHalf, be, lightBehind).renderInto(ms, vb);
            kineticRotationTransform(fanInner, be, direction.getAxis(), angle, lightInFront).renderInto(ms, vb);
        }
    }
}