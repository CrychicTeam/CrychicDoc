package io.github.lightman314.lightmanscurrency.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.IRotatableBlock;
import io.github.lightman314.lightmanscurrency.common.blockentity.trader.FreezerTraderBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blocks.traderblocks.FreezerBlock;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FreezerTraderBlockEntityRenderer implements BlockEntityRenderer<FreezerTraderBlockEntity> {

    public FreezerTraderBlockEntityRenderer(BlockEntityRendererProvider.Context ignored) {
    }

    public void render(@NotNull FreezerTraderBlockEntity tileEntity, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int lightLevel, int id) {
        ItemTraderBlockEntityRenderer.renderItems(tileEntity, partialTicks, poseStack, bufferSource, lightLevel, id);
        if (tileEntity.m_58900_().m_60734_() instanceof FreezerBlock freezerBlock) {
            poseStack.pushPose();
            Direction facing = freezerBlock.getFacing(tileEntity.m_58900_());
            Vector3f corner = IRotatableBlock.getOffsetVect(facing);
            Vector3f right = IRotatableBlock.getRightVect(facing);
            Vector3f forward = IRotatableBlock.getForwardVect(facing);
            Vector3f hinge = MathUtil.VectorAdd(corner, MathUtil.VectorMult(right, 0.96875F), MathUtil.VectorMult(forward, 0.21875F));
            Quaternionf rotation = MathUtil.fromAxisAngleDegree(MathUtil.getYP(), (float) facing.get2DDataValue() * -90.0F + 90.0F * tileEntity.getDoorAngle(partialTicks));
            poseStack.translate(hinge.x(), hinge.y(), hinge.z());
            poseStack.mulPose(rotation);
            Minecraft mc = Minecraft.getInstance();
            BakedModel model = mc.getModelManager().getModel(freezerBlock.getDoorModel());
            ItemRenderer itemRenderer = mc.getItemRenderer();
            itemRenderer.render(new ItemStack(freezerBlock), ItemDisplayContext.FIXED, false, poseStack, bufferSource, lightLevel, OverlayTexture.NO_OVERLAY, model);
            poseStack.popPose();
        }
    }
}