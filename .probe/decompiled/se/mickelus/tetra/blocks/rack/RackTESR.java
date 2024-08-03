package se.mickelus.tetra.blocks.rack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import se.mickelus.tetra.items.modular.impl.ModularBladedItem;
import se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldItem;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class RackTESR implements BlockEntityRenderer<RackTile> {

    private final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

    public RackTESR(BlockEntityRendererProvider.Context context) {
    }

    public void render(RackTile tile, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        tile.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            Direction direction = (Direction) tile.m_58900_().m_61143_(RackBlock.facingProp);
            Direction itemDirection = direction.getCounterClockWise();
            matrixStack.pushPose();
            matrixStack.translate(0.5 - (double) direction.getStepX() * 0.36, 0.7, 0.5 - (double) direction.getStepZ() * 0.36);
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack itemStack = handler.getStackInSlot(i);
                if (!itemStack.isEmpty()) {
                    matrixStack.pushPose();
                    matrixStack.translate((double) itemDirection.getStepX() * ((double) i - 0.5), 0.0, (double) itemDirection.getStepZ() * ((double) i - 0.5));
                    matrixStack.mulPose(direction.getRotation());
                    this.renderItemStack(tile, itemStack, matrixStack, buffer, combinedLight, combinedOverlay);
                    matrixStack.popPose();
                }
            }
            matrixStack.popPose();
        });
    }

    private void renderItemStack(RackTile tile, ItemStack itemStack, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (itemStack != null && !itemStack.isEmpty()) {
            int renderId = (int) tile.m_58899_().asLong();
            BakedModel model = this.itemRenderer.getModel(itemStack, tile.m_58904_(), null, combinedLight);
            matrixStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            if (itemStack.getItem() instanceof ModularShieldItem) {
                matrixStack.translate(-0.25, 0.0, 0.16);
                matrixStack.scale(2.0F, 2.0F, 2.0F);
            } else if (itemStack.getItem() instanceof ModularBladedItem || itemStack.getItem() instanceof SwordItem) {
                matrixStack.translate(0.0, -0.2, 0.0);
                matrixStack.mulPose(Axis.ZP.rotationDegrees(135.0F));
            } else if (itemStack.getItem() instanceof ModularCrossbowItem || itemStack.getItem() instanceof CrossbowItem) {
                matrixStack.translate(0.0, -0.2, 0.0);
                matrixStack.mulPose(Axis.ZP.rotationDegrees(225.0F));
            } else if (model.isGui3d()) {
                matrixStack.mulPose(Axis.ZP.rotationDegrees(-45.0F));
            } else {
                matrixStack.mulPose(Axis.ZP.rotationDegrees(-45.0F));
            }
            Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, matrixStack, buffer, tile.m_58904_(), renderId);
        }
    }
}