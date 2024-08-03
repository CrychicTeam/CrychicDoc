package dev.xkmc.l2complements.content.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpecialSpriteRenderer<T extends Entity & ItemSupplier & ISizedItemEntity> extends EntityRenderer<T> {

    private final ItemRenderer itemRenderer;

    private final boolean fullBright;

    public SpecialSpriteRenderer(EntityRendererProvider.Context manager, ItemRenderer itemRenderer, boolean bright) {
        super(manager);
        this.itemRenderer = itemRenderer;
        this.fullBright = bright;
    }

    @Override
    protected int getBlockLightLevel(T entity, BlockPos pos) {
        return this.fullBright ? 15 : super.getBlockLightLevel(entity, pos);
    }

    @Override
    public void render(T entity, float yRot, float partial, PoseStack matrix, MultiBufferSource buffer, int light) {
        if (entity.tickCount >= 2 || !(this.f_114476_.camera.getEntity().distanceToSqr(entity) < 12.25)) {
            matrix.pushPose();
            float size = entity.getSize();
            matrix.scale(size, size, size);
            matrix.mulPose(this.f_114476_.cameraOrientation());
            matrix.mulPose(Axis.YP.rotationDegrees(180.0F));
            this.itemRenderer.renderStatic(entity.getItem(), ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, matrix, buffer, entity.level(), 0);
            matrix.popPose();
            super.render(entity, yRot, partial, matrix, buffer, light);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Entity p_110775_1_) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}