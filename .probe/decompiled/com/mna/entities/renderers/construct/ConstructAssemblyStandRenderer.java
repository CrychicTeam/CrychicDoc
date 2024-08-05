package com.mna.entities.renderers.construct;

import com.mna.ManaAndArtifice;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ItemConstructPart;
import com.mna.entities.constructs.ConstructAssemblyStand;
import com.mna.entities.constructs.animated.Construct;
import com.mna.entities.models.constructs.ConstructAssemblyStandModel;
import com.mna.entities.models.constructs.ConstructModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import javax.annotation.Nullable;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class ConstructAssemblyStandRenderer extends LivingEntityRenderer<ConstructAssemblyStand, ConstructAssemblyStandModel> {

    public static final ResourceLocation DEFAULT_SKIN_LOCATION = new ResourceLocation("textures/entity/armorstand/wood.png");

    public ConstructAssemblyStandRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new ConstructAssemblyStandModel(entityRendererProviderContext0.bakeLayer(ModelLayers.ARMOR_STAND)), 0.0F);
    }

    public void render(ConstructAssemblyStand stand, float float0, float partialTick, PoseStack stack, MultiBufferSource bufferSource, int combinedLight) {
        super.render(stand, float0, partialTick, stack, bufferSource, combinedLight);
        stack.pushPose();
        stack.mulPose(Axis.YP.rotationDegrees(180.0F));
        stack.mulPose(Axis.YP.rotationDegrees(180.0F - stand.m_146908_()));
        stack.translate(0.0F, 0.0F, 0.2F);
        try {
            Construct dummy = ManaAndArtifice.instance.proxy.getDummyConstructForRender();
            ConstructRenderer geoRenderer = ConstructRenderer.instance;
            ConstructModel model = ConstructRenderer.constructModel;
            model.getBakedModel(model.getModelResource(dummy));
            for (ConstructMaterial matl : stand.getComposition()) {
                model.resetMutexVisibility();
                model.setActiveMaterial(matl);
                dummy.getConstructData().resetParts();
                for (ItemConstructPart part : stand.getPartsForMaterial(matl)) {
                    dummy.getConstructData().setPart(new ItemStack(part));
                    model.setMutexVisibility(part.getSlot(), part.getModelTypeMutex());
                }
                stack.pushPose();
                geoRenderer.render(dummy, 0.0F, partialTick, stack, bufferSource, combinedLight);
                stack.popPose();
            }
        } catch (Exception var18) {
        }
        stack.popPose();
    }

    public ResourceLocation getTextureLocation(ConstructAssemblyStand stand) {
        return DEFAULT_SKIN_LOCATION;
    }

    protected void setupRotations(ConstructAssemblyStand stand, PoseStack stack, float bob, float yRotLerp, float xRotLerp) {
        stack.mulPose(Axis.YP.rotationDegrees(180.0F - stand.m_146908_()));
        float f = (float) (stand.m_9236_().getGameTime() - stand.lastHit) + xRotLerp;
        if (f < 5.0F) {
            stack.mulPose(Axis.YP.rotationDegrees(Mth.sin(f / 1.5F * (float) Math.PI) * 3.0F));
        }
    }

    protected boolean shouldShowName(ConstructAssemblyStand constructAssemblyStand0) {
        return false;
    }

    @Nullable
    protected RenderType getRenderType(ConstructAssemblyStand stand, boolean boolean0, boolean boolean1, boolean boolean2) {
        ResourceLocation resourcelocation = this.getTextureLocation(stand);
        if (boolean1) {
            return RenderType.entityTranslucent(resourcelocation, false);
        } else {
            return boolean0 ? RenderType.entityCutoutNoCull(resourcelocation, false) : null;
        }
    }
}