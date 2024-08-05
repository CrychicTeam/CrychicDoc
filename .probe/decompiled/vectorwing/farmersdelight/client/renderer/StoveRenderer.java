package vectorwing.farmersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.block.entity.StoveBlockEntity;

public class StoveRenderer implements BlockEntityRenderer<StoveBlockEntity> {

    public StoveRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void render(StoveBlockEntity stoveEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
        Direction direction = ((Direction) stoveEntity.m_58900_().m_61143_(StoveBlock.FACING)).getOpposite();
        ItemStackHandler inventory = stoveEntity.getInventory();
        int posLong = (int) stoveEntity.m_58899_().asLong();
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stoveStack = inventory.getStackInSlot(i);
            if (!stoveStack.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0.5, 1.02, 0.5);
                float f = -direction.toYRot();
                poseStack.mulPose(Axis.YP.rotationDegrees(f));
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                Vec2 itemOffset = stoveEntity.getStoveItemOffset(i);
                poseStack.translate((double) itemOffset.x, (double) itemOffset.y, 0.0);
                poseStack.scale(0.375F, 0.375F, 0.375F);
                if (stoveEntity.m_58904_() != null) {
                    Minecraft.getInstance().getItemRenderer().renderStatic(stoveStack, ItemDisplayContext.FIXED, LevelRenderer.getLightColor(stoveEntity.m_58904_(), stoveEntity.m_58899_().above()), combinedOverlayIn, poseStack, buffer, stoveEntity.m_58904_(), posLong + i);
                }
                poseStack.popPose();
            }
        }
    }
}