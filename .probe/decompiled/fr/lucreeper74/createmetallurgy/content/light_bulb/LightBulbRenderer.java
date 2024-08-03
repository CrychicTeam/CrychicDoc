package fr.lucreeper74.createmetallurgy.content.light_bulb;

import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkBlock;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.RenderTypes;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import fr.lucreeper74.createmetallurgy.content.light_bulb.network.address.NetworkAddressRenderer;
import fr.lucreeper74.createmetallurgy.registries.CMPartialModels;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class LightBulbRenderer extends SafeBlockEntityRenderer<LightBulbBlockEntity> {

    public LightBulbRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(LightBulbBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        NetworkAddressRenderer.renderOnBlockEntity(be, ms, buffer, light, overlay);
        float glowValue = be.glow.getValue(partialTicks);
        int color = (int) (14.0F * glowValue);
        float size = 1.0F + 0.033333335F * glowValue;
        BlockState blockState = be.m_58900_();
        TransformStack msr = TransformStack.cast(ms);
        Direction face = (Direction) blockState.m_61145_(DisplayLinkBlock.f_52588_).orElse(Direction.UP);
        if (face.getAxis().isHorizontal()) {
            face = face.getOpposite();
        }
        ms.pushPose();
        ((TransformStack) ((TransformStack) ((TransformStack) msr.centre()).rotateY((double) AngleHelper.horizontalAngle(face))).rotateX((double) (-AngleHelper.verticalAngle(face) - 90.0F))).unCentre();
        if (glowValue > 0.125F) {
            ((SuperByteBuffer) CachedBufferer.partial(CMPartialModels.BULB_INNER_GLOW, be.m_58900_()).light(light).color(color, color, color, 255).disableDiffuse().translate(0.5, 0.5625, 0.5).scale(size)).translate(-0.5, -0.5625, -0.5).renderInto(ms, buffer.getBuffer(RenderTypes.getAdditive()));
            CachedBufferer.partial((PartialModel) CMPartialModels.BULB_TUBES.get(be.getColor()), blockState).light(light).renderInto(ms, buffer.getBuffer(RenderType.translucent()));
            CachedBufferer.partial((PartialModel) CMPartialModels.BULB_TUBES_GLOW.get(be.getColor()), blockState).light(light).color(color, color, color, 255).disableDiffuse().renderInto(ms, buffer.getBuffer(RenderTypes.getAdditive()));
        } else {
            CachedBufferer.partial((PartialModel) CMPartialModels.BULB_TUBES.get(be.getColor()), blockState).light(light).renderInto(ms, buffer.getBuffer(RenderType.translucent()));
        }
        ms.popPose();
    }
}