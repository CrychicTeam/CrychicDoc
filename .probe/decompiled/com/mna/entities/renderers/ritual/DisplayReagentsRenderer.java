package com.mna.entities.renderers.ritual;

import com.mna.api.tools.MATags;
import com.mna.entities.utility.DisplayReagents;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.joml.Quaternionf;

public class DisplayReagentsRenderer extends EntityRenderer<DisplayReagents> {

    private final ItemRenderer itemRenderer;

    private final Minecraft mc = Minecraft.getInstance();

    public DisplayReagentsRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = this.mc.getItemRenderer();
    }

    public ResourceLocation getTextureLocation(DisplayReagents entity) {
        return null;
    }

    public void render(DisplayReagents entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        List<ResourceLocation> reagents = entityIn.getResourceLocations();
        float displayItemWidth = 0.0625F;
        float offset = (float) reagents.size() * displayItemWidth;
        Quaternionf cameraRotation = this.f_114476_.cameraOrientation();
        Quaternionf portalRotation = new Quaternionf(0.0F, cameraRotation.y(), 0.0F, cameraRotation.w());
        int multiItemIndex = entityIn.getAge() / 40;
        int count = 0;
        float scaleFactor = 0.25F;
        for (ResourceLocation location : reagents) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(offset * (float) reagents.size() / 2.0F - displayItemWidth * 2.0F, 1.0F, 0.0F);
            matrixStackIn.translate(-offset * (float) count, 0.0F, 0.0F);
            matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
            matrixStackIn.pushPose();
            matrixStackIn.mulPose(portalRotation);
            List<Item> item = MATags.smartLookupItem(location);
            ItemStack renderStack;
            if (item.size() == 1) {
                renderStack = new ItemStack((ItemLike) item.get(0));
            } else {
                renderStack = new ItemStack((ItemLike) item.get(multiItemIndex % item.size()));
            }
            if (renderStack != null) {
                this.itemRenderer.renderStatic(renderStack, ItemDisplayContext.FIXED, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, this.mc.level, 0);
            }
            matrixStackIn.popPose();
            this.m_7649_(entityIn, Component.literal(count + 1 + ""), matrixStackIn, bufferIn, packedLightIn);
            matrixStackIn.popPose();
            count++;
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }
}