package com.rekindled.embers.entity.render;

import com.rekindled.embers.entity.AncientGolemEntity;
import com.rekindled.embers.model.AncientGolemModel;
import com.rekindled.embers.model.AshenArmorModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AncientGolemRenderer extends MobRenderer<AncientGolemEntity, AncientGolemModel<AncientGolemEntity>> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("embers", "ancient_golem"), "main");

    public static final ResourceLocation TEXTURE = new ResourceLocation("embers", "textures/entity/golem.png");

    public AncientGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new AncientGolemModel<>(context.bakeLayer(LAYER_LOCATION)), 0.5F);
        this.m_115326_(new AncientGolemEyeLayer<>(this));
        AshenArmorModel.init(context);
    }

    public ResourceLocation getTextureLocation(AncientGolemEntity pEntity) {
        return TEXTURE;
    }
}