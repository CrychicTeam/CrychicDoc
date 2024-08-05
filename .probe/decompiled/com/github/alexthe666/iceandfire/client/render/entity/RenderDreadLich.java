package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDreadLich;
import com.github.alexthe666.iceandfire.client.model.util.HideableLayer;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerGenericGlowing;
import com.github.alexthe666.iceandfire.entity.EntityDreadLich;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class RenderDreadLich extends MobRenderer<EntityDreadLich, ModelDreadLich> {

    public static final ResourceLocation TEXTURE_EYES = new ResourceLocation("iceandfire:textures/models/dread/dread_lich_eyes.png");

    public static final ResourceLocation TEXTURE_0 = new ResourceLocation("iceandfire:textures/models/dread/dread_lich_0.png");

    public static final ResourceLocation TEXTURE_1 = new ResourceLocation("iceandfire:textures/models/dread/dread_lich_1.png");

    public static final ResourceLocation TEXTURE_2 = new ResourceLocation("iceandfire:textures/models/dread/dread_lich_2.png");

    public static final ResourceLocation TEXTURE_3 = new ResourceLocation("iceandfire:textures/models/dread/dread_lich_3.png");

    public static final ResourceLocation TEXTURE_4 = new ResourceLocation("iceandfire:textures/models/dread/dread_lich_4.png");

    public final HideableLayer<EntityDreadLich, ModelDreadLich, ItemInHandLayer<EntityDreadLich, ModelDreadLich>> itemLayer;

    public RenderDreadLich(EntityRendererProvider.Context context) {
        super(context, new ModelDreadLich(0.0F), 0.6F);
        this.m_115326_(new LayerGenericGlowing(this, TEXTURE_EYES));
        this.itemLayer = new HideableLayer<>(new ItemInHandLayer<>(this, context.getItemInHandRenderer()), this);
        this.m_115326_(this.itemLayer);
    }

    protected void scale(EntityDreadLich entity, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.95F, 0.95F, 0.95F);
        if (entity.getAnimation() == ((ModelDreadLich) this.m_7200_()).getSpawnAnimation()) {
            this.itemLayer.hidden = entity.getAnimationTick() <= ((ModelDreadLich) this.m_7200_()).getSpawnAnimation().getDuration() - 10;
        } else {
            this.itemLayer.hidden = false;
        }
    }

    @Nullable
    public ResourceLocation getTextureLocation(EntityDreadLich entity) {
        switch(entity.getVariant()) {
            case 1:
                return TEXTURE_1;
            case 2:
                return TEXTURE_2;
            case 3:
                return TEXTURE_3;
            case 4:
                return TEXTURE_4;
            default:
                return TEXTURE_0;
        }
    }
}