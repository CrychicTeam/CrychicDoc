package vectorwing.farmersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;

public class SkilletRenderer implements BlockEntityRenderer<SkilletBlockEntity> {

    private final Random random = new Random();

    public SkilletRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void render(SkilletBlockEntity skilletEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Direction direction = (Direction) skilletEntity.m_58900_().m_61143_(StoveBlock.FACING);
        IItemHandler inventory = skilletEntity.getInventory();
        int posLong = (int) skilletEntity.m_58899_().asLong();
        ItemStack stack = inventory.getStackInSlot(0);
        int seed = stack.isEmpty() ? 187 : Item.getId(stack.getItem()) + stack.getDamageValue();
        this.random.setSeed((long) seed);
        if (!stack.isEmpty()) {
            int itemRenderCount = this.getModelCount(stack);
            for (int i = 0; i < itemRenderCount; i++) {
                poseStack.pushPose();
                float xOffset = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                float zOffset = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                poseStack.translate(0.5 + (double) xOffset, 0.1 + 0.03 * (double) (i + 1), 0.5 + (double) zOffset);
                float degrees = -direction.toYRot();
                poseStack.mulPose(Axis.YP.rotationDegrees(degrees));
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                poseStack.scale(0.5F, 0.5F, 0.5F);
                if (skilletEntity.m_58904_() != null) {
                    Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, buffer, skilletEntity.m_58904_(), posLong);
                }
                poseStack.popPose();
            }
        }
    }

    protected int getModelCount(ItemStack stack) {
        if (stack.getCount() > 48) {
            return 5;
        } else if (stack.getCount() > 32) {
            return 4;
        } else if (stack.getCount() > 16) {
            return 3;
        } else {
            return stack.getCount() > 1 ? 2 : 1;
        }
    }
}