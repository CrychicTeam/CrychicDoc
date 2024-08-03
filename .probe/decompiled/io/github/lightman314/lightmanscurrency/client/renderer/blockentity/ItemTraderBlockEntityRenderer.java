package io.github.lightman314.lightmanscurrency.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.client.resourcepacks.data.item_trader.ItemPositionData;
import io.github.lightman314.lightmanscurrency.common.blockentity.trader.ItemTraderBlockEntity;
import io.github.lightman314.lightmanscurrency.common.traders.item.ItemTraderData;
import io.github.lightman314.lightmanscurrency.common.traders.item.tradedata.ItemTradeData;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@EventBusSubscriber({ Dist.CLIENT })
public class ItemTraderBlockEntityRenderer implements BlockEntityRenderer<ItemTraderBlockEntity> {

    private static long rotationTime = 0L;

    public ItemTraderBlockEntityRenderer(BlockEntityRendererProvider.Context ignored) {
    }

    public void render(@NotNull ItemTraderBlockEntity blockEntity, float partialTicks, @NotNull PoseStack pose, @NotNull MultiBufferSource buffer, int lightLevel, int id) {
        renderItems(blockEntity, partialTicks, pose, buffer, lightLevel, id);
    }

    public static List<ItemStack> GetRenderItems(ItemTradeData trade) {
        List<ItemStack> result = new ArrayList();
        for (int i = 0; i < 2; i++) {
            ItemStack item = trade.getSellItem(i);
            if (!item.isEmpty()) {
                result.add(item);
            }
        }
        return result;
    }

    public static void renderItems(ItemTraderBlockEntity blockEntity, float partialTicks, PoseStack pose, MultiBufferSource buffer, int lightLevel, int id) {
        try {
            if (!(blockEntity.getRawTraderData() instanceof ItemTraderData trader)) {
                return;
            }
            ItemPositionData positionData = blockEntity.GetRenderData();
            if (positionData.isEmpty()) {
                return;
            }
            int maxIndex = positionData.getEntryCount();
            int renderLimit = LCConfig.CLIENT.itemRenderLimit.get();
            BlockState state = blockEntity.m_58900_();
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            for (int tradeSlot = 0; tradeSlot < trader.getTradeCount() && tradeSlot < maxIndex; tradeSlot++) {
                ItemTradeData trade = trader.getTrade(tradeSlot);
                List<ItemStack> renderItems = GetRenderItems(trade);
                if (!renderItems.isEmpty()) {
                    List<Vector3f> positions = positionData.getPositions(state, tradeSlot);
                    List<Quaternionf> rotation = positionData.getRotation(state, tradeSlot, partialTicks);
                    float scale = positionData.getScale(tradeSlot);
                    for (int pos = 0; pos < renderLimit && pos < positions.size() && pos < trader.getTradeStock(tradeSlot); pos++) {
                        pose.pushPose();
                        Vector3f position = (Vector3f) positions.get(pos);
                        pose.translate(position.x(), position.y(), position.z());
                        for (Quaternionf rot : rotation) {
                            pose.mulPose(rot);
                        }
                        pose.scale(scale, scale, scale);
                        if (renderItems.size() > 1) {
                            pose.pushPose();
                            pose.translate(0.25, 0.25, 0.0);
                            pose.scale(0.5F, 0.5F, 0.5F);
                            itemRenderer.renderStatic((ItemStack) renderItems.get(0), ItemDisplayContext.FIXED, lightLevel, OverlayTexture.NO_OVERLAY, pose, buffer, blockEntity.m_58904_(), id);
                            pose.popPose();
                            pose.pushPose();
                            pose.translate(-0.25, -0.25, 0.001);
                            pose.scale(0.5F, 0.5F, 0.5F);
                            itemRenderer.renderStatic((ItemStack) renderItems.get(1), ItemDisplayContext.FIXED, lightLevel, OverlayTexture.NO_OVERLAY, pose, buffer, blockEntity.m_58904_(), id);
                            pose.popPose();
                        } else {
                            itemRenderer.renderStatic((ItemStack) renderItems.get(0), ItemDisplayContext.FIXED, lightLevel, OverlayTexture.NO_OVERLAY, pose, buffer, blockEntity.m_58904_(), id);
                        }
                        pose.popPose();
                    }
                }
            }
        } catch (Throwable var23) {
            LightmansCurrency.LogError("Error rendering an Item Trader!", var23);
        }
    }

    public static long getRotationTime() {
        return rotationTime;
    }

    public static Quaternionf getRotation(float partialTicks) {
        return new Quaternionf().fromAxisAngleDeg(new Vector3f(0.0F, 1.0F, 0.0F), ((float) getRotationTime() + partialTicks) * 2.0F);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            rotationTime++;
        }
    }
}