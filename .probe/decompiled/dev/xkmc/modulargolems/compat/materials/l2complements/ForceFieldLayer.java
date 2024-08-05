package dev.xkmc.modulargolems.compat.materials.l2complements;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ForceFieldLayer<T extends AbstractGolemEntity<T, ?>, M extends EntityModel<T>> extends EnergySwirlLayer<T, M> {

    private static final ResourceLocation WITHER_ARMOR_LOCATION = new ResourceLocation("textures/entity/wither/wither_armor.png");

    private final M model;

    public static void registerLayer() {
        AbstractGolemRenderer.LIST.add(ForceFieldLayer::new);
    }

    public ForceFieldLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
        this.model = pRenderer.getModel();
    }

    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, T e, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (e.getUpgrades().contains(LCCompatRegistry.FORCE_FIELD.get())) {
            super.render(pMatrixStack, pBuffer, pPackedLight, e, pLimbSwing, pLimbSwingAmount, pPartialTicks, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        }
    }

    @Override
    protected float xOffset(float t) {
        return Mth.cos(t * 0.02F) * 3.0F;
    }

    @Override
    protected ResourceLocation getTextureLocation() {
        return WITHER_ARMOR_LOCATION;
    }

    @Override
    protected M model() {
        return this.model;
    }
}