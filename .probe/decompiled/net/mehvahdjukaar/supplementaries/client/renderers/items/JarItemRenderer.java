package net.mehvahdjukaar.supplementaries.client.renderers.items;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.concurrent.atomic.AtomicInteger;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidRegistry;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.supplementaries.client.renderers.VertexUtils;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.JarBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class JarItemRenderer extends CageItemRenderer {

    private static final RandomSource RAND = RandomSource.createNewThreadLocalInstance();

    @Override
    public void renderContent(CompoundTag tag, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        super.renderContent(tag, transformType, poseStack, buffer, light, overlay);
        if (tag.contains("MobHolder") || tag.contains("BucketHolder")) {
            CompoundTag com = tag.getCompound("BucketHolder");
            if (com.isEmpty()) {
                com = tag.getCompound("MobHolder");
            }
            if (com.contains("FishTexture")) {
                int fishTexture = com.getInt("FishTexture");
                if (fishTexture >= 0) {
                    poseStack.pushPose();
                    poseStack.translate(0.5, 0.3125, 0.5);
                    poseStack.mulPose(RotHlpr.YN45);
                    poseStack.scale(1.5F, 1.5F, 1.5F);
                    VertexUtils.renderFish(buffer, poseStack, 0.0F, 0.0F, fishTexture, light);
                    poseStack.popPose();
                }
                if (com.contains("Fluid")) {
                    Holder<SoftFluid> holder = SoftFluidRegistry.getHolder(new ResourceLocation(com.getString("Fluid")));
                    if (holder != null) {
                        SoftFluid s = holder.value();
                        JarBlockTileRenderer.renderFluid(0.75F, s.getTintColor(), 0, s.getStillTexture(), poseStack, buffer, light, overlay);
                    }
                }
            }
        }
        if (tag.contains("FluidHolder")) {
            CompoundTag comx = tag.getCompound("FluidHolder");
            SoftFluidStack fluidStack = SoftFluidStack.load(comx);
            int count = fluidStack.getCount();
            if (count != 0) {
                int color = fluidStack.getFlowingColor(Minecraft.getInstance().level, null);
                JarBlockTileRenderer.renderFluid(getHeight((float) count, 1.0F), color, 0, fluidStack.fluid().getStillTexture(), poseStack, buffer, light, overlay);
            }
        }
        if (tag.contains("Items")) {
            ListTag items = tag.getList("Items", 10);
            AtomicInteger i = new AtomicInteger();
            RAND.setSeed(420L);
            JarBlockTileRenderer.renderCookies(poseStack, buffer, RAND, light, overlay, () -> {
                int j = i.getAndIncrement();
                return j < items.size() ? ItemStack.of(items.getCompound(j)) : ItemStack.EMPTY;
            });
        }
    }

    private static float getHeight(float count, float maxHeight) {
        return maxHeight * count / (float) ((Integer) CommonConfigs.Functional.JAR_CAPACITY.get()).intValue();
    }
}