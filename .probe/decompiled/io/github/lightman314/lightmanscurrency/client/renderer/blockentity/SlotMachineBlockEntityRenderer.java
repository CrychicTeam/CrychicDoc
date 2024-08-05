package io.github.lightman314.lightmanscurrency.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.IRotatableBlock;
import io.github.lightman314.lightmanscurrency.common.blockentity.trader.SlotMachineTraderBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blocks.traderblocks.SlotMachineBlock;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class SlotMachineBlockEntityRenderer implements BlockEntityRenderer<SlotMachineTraderBlockEntity> {

    public SlotMachineBlockEntityRenderer(BlockEntityRendererProvider.Context ignored) {
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    public void render(@NotNull SlotMachineTraderBlockEntity tileEntity, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int lightLevel, int overlay) {
        if (tileEntity.m_58900_().m_60734_() instanceof SlotMachineBlock block) {
            ResourceLocation lightModel = block.getLightModel();
            if (lightModel == null) {
                return;
            }
            poseStack.pushPose();
            Direction facing = block.getFacing(tileEntity.m_58900_());
            Vector3f corner = IRotatableBlock.getOffsetVect(facing);
            Vector3f right = IRotatableBlock.getRightVect(facing);
            Vector3f forward = IRotatableBlock.getForwardVect(facing);
            Vector3f offset = MathUtil.VectorAdd(corner, MathUtil.VectorMult(right, 0.5F), MathUtil.VectorMult(forward, 0.5F), new Vector3f(0.0F, 0.5F, 0.0F));
            poseStack.translate(offset.x, offset.y, offset.z);
            poseStack.mulPose(MathUtil.fromAxisAngleDegree(MathUtil.getYP(), (float) facing.get2DDataValue() * -90.0F));
            Minecraft mc = Minecraft.getInstance();
            BakedModel model = mc.getModelManager().getModel(lightModel);
            ItemRenderer itemRenderer = mc.getItemRenderer();
            itemRenderer.render(new ItemStack(block), ItemDisplayContext.FIXED, false, poseStack, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, model);
            poseStack.popPose();
        }
    }
}