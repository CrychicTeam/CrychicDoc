package io.github.lightman314.lightmanscurrency.client.renderer.blockentity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.lightman314.lightmanscurrency.client.resourcepacks.data.item_trader.ItemPositionBlockManager;
import io.github.lightman314.lightmanscurrency.client.resourcepacks.data.item_trader.ItemPositionData;
import io.github.lightman314.lightmanscurrency.common.blockentity.AuctionStandBlockEntity;
import java.util.List;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class AuctionStandBlockEntityRenderer implements BlockEntityRenderer<AuctionStandBlockEntity> {

    private final ItemRenderer itemRenderer;

    public AuctionStandBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(@NotNull AuctionStandBlockEntity blockEntity, float partialTicks, @NotNull PoseStack pose, @NotNull MultiBufferSource buffer, int lightLevel, int id) {
        ImmutableList<ItemStack> displayItems = AuctionStandBlockEntity.getDisplayItems();
        if (displayItems.size() >= 1) {
            pose.pushPose();
            BlockState state = blockEntity.m_58900_();
            ItemPositionData data = ItemPositionBlockManager.getDataForBlock(state);
            List<Vector3f> positions = data.getPositions(state, 0);
            if (positions.size() != 0) {
                Vector3f pos = (Vector3f) positions.get(0);
                pose.translate(pos.x, pos.y, pos.z);
                for (Quaternionf r : data.getRotation(state, 0, partialTicks)) {
                    pose.mulPose(r);
                }
                float scale = data.getScale(0);
                pose.scale(scale, scale, scale);
                if (displayItems.size() < 2) {
                    this.itemRenderer.renderStatic((ItemStack) displayItems.get(0), ItemDisplayContext.FIXED, lightLevel, OverlayTexture.NO_OVERLAY, pose, buffer, blockEntity.m_58904_(), id);
                } else {
                    pose.pushPose();
                    pose.translate(-0.55F, 0.0F, 0.0F);
                    this.itemRenderer.renderStatic((ItemStack) displayItems.get(0), ItemDisplayContext.FIXED, lightLevel, OverlayTexture.NO_OVERLAY, pose, buffer, blockEntity.m_58904_(), id);
                    pose.popPose();
                    pose.pushPose();
                    pose.translate(0.55F, 0.0F, 0.0F);
                    this.itemRenderer.renderStatic((ItemStack) displayItems.get(1), ItemDisplayContext.FIXED, lightLevel, OverlayTexture.NO_OVERLAY, pose, buffer, blockEntity.m_58904_(), id);
                    pose.popPose();
                }
                pose.popPose();
            }
        }
    }
}