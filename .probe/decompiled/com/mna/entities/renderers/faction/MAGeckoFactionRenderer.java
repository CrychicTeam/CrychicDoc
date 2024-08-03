package com.mna.entities.renderers.faction;

import com.mna.api.entities.IFactionEnemy;
import com.mna.entities.renderers.MAGeckoRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class MAGeckoFactionRenderer<T extends LivingEntity & IFactionEnemy<T> & GeoAnimatable> extends MAGeckoRenderer<T> {

    protected MAGeckoFactionRenderer(EntityRendererProvider.Context context, GeoModel<T> modelProvider) {
        super(context, modelProvider);
    }

    public void renderRecursively(PoseStack stack, T entity, GeoBone bone, RenderType renderType, MultiBufferSource multiBufferSource, VertexConsumer consumer, boolean arg6, float partialTicks, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        int tier = this.animatable.getTier();
        int idx = bone.getName().indexOf(95);
        bone.setHidden(false);
        if (idx > -1) {
            String preUnderscore = bone.getName().substring(0, idx);
            boolean t1 = preUnderscore.contains("T1");
            boolean t2 = preUnderscore.contains("T2");
            boolean t3 = preUnderscore.contains("T3");
            if (t1 || t2 || t3) {
                if (tier == 0 && !t1) {
                    return;
                }
                if (tier == 1 && !t2) {
                    return;
                }
                if (tier == 2 && !t3) {
                    return;
                }
            }
            if (this.hasEmissivePass) {
                boolean containsEmissive = preUnderscore.contains("EMISSIVE");
                if (this.emissivePass && !containsEmissive) {
                    bone.setHidden(true);
                    bone.setChildrenHidden(false);
                } else if (!this.emissivePass && containsEmissive) {
                    bone.setHidden(true);
                    bone.setChildrenHidden(false);
                }
            }
        } else if (this.emissivePass) {
            bone.setHidden(true);
            bone.setChildrenHidden(false);
        }
        super.renderRecursively(stack, entity, bone, renderType, multiBufferSource, consumer, arg6, green, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}