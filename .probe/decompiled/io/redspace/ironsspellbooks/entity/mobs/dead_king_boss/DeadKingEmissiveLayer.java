package io.redspace.ironsspellbooks.entity.mobs.dead_king_boss;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class DeadKingEmissiveLayer extends GeoRenderLayer<AbstractSpellCastingMob> {

    public static final ResourceLocation TEXTURE_NORMAL = new ResourceLocation("irons_spellbooks", "textures/entity/dead_king/dead_king_glowing.png");

    public static final ResourceLocation TEXTURE_ENRAGED = new ResourceLocation("irons_spellbooks", "textures/entity/dead_king/dead_king_enraged_glowing.png");

    public DeadKingEmissiveLayer(GeoEntityRenderer renderer) {
        super(renderer);
    }

    public static ResourceLocation currentTexture(AbstractSpellCastingMob entity) {
        if (entity instanceof DeadKingBoss boss && boss.isPhase(DeadKingBoss.Phases.FinalPhase)) {
            return TEXTURE_ENRAGED;
        }
        return TEXTURE_NORMAL;
    }

    public static ResourceLocation currentModel(AbstractSpellCastingMob deadKingBoss) {
        return DeadKingModel.MODEL;
    }

    public static RenderType renderType(ResourceLocation resourceLocation) {
        return RenderType.energySwirl(resourceLocation, 0.0F, 0.0F);
    }

    public void render(PoseStack poseStack, AbstractSpellCastingMob animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (!(animatable instanceof DeadKingCorpseEntity) && !animatable.m_20145_()) {
            BakedGeoModel model = this.getGeoModel().getBakedModel(currentModel(animatable));
            poseStack.pushPose();
            renderType = renderType(currentTexture(animatable));
            VertexConsumer vertexconsumer = bufferSource.getBuffer(renderType);
            this.getRenderer().actuallyRender(poseStack, animatable, model, renderType, bufferSource, vertexconsumer, true, partialTick, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }
    }
}