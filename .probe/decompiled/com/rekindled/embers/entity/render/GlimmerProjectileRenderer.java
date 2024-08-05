package com.rekindled.embers.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rekindled.embers.entity.GlimmerProjectileEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GlimmerProjectileRenderer extends EntityRenderer<GlimmerProjectileEntity> {

    public GlimmerProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    public void render(GlimmerProjectileEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
    }

    public ResourceLocation getTextureLocation(GlimmerProjectileEntity pEntity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}