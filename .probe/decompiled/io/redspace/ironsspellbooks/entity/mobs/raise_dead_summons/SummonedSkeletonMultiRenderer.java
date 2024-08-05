package io.redspace.ironsspellbooks.entity.mobs.raise_dead_summons;

import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.entity.mobs.HumanoidRenderer;
import io.redspace.ironsspellbooks.entity.mobs.SummonedSkeleton;
import io.redspace.ironsspellbooks.render.SpellTargetingLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;

public class SummonedSkeletonMultiRenderer extends HumanoidRenderer<SummonedSkeleton> {

    SkeletonRenderer vanillaRenderer;

    public static final ResourceLocation TEXTURE_ALT = new ResourceLocation("irons_spellbooks", "textures/entity/summoned_skeleton_alt.png");

    public SummonedSkeletonMultiRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SummonedSkeletonModel());
        this.vanillaRenderer = new SkeletonRenderer(pContext) {

            @Override
            public ResourceLocation getTextureLocation(AbstractSkeleton pEntity) {
                return SummonedSkeletonMultiRenderer.TEXTURE_ALT;
            }
        };
        this.vanillaRenderer.m_115326_(new SpellTargetingLayer.Vanilla<>(this.vanillaRenderer));
    }

    public void render(SummonedSkeleton entity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (entity.isAnimatingRise()) {
            super.m_7392_(entity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        } else {
            this.vanillaRenderer.m_7392_(entity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        }
    }
}