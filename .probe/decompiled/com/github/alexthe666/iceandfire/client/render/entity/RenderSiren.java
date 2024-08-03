package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelSiren;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class RenderSiren extends MobRenderer<EntitySiren, ModelSiren> {

    public static final ResourceLocation TEXTURE_0 = new ResourceLocation("iceandfire:textures/models/siren/siren_0.png");

    public static final ResourceLocation TEXTURE_0_AGGRESSIVE = new ResourceLocation("iceandfire:textures/models/siren/siren_0_aggressive.png");

    public static final ResourceLocation TEXTURE_1 = new ResourceLocation("iceandfire:textures/models/siren/siren_1.png");

    public static final ResourceLocation TEXTURE_1_AGGRESSIVE = new ResourceLocation("iceandfire:textures/models/siren/siren_1_aggressive.png");

    public static final ResourceLocation TEXTURE_2 = new ResourceLocation("iceandfire:textures/models/siren/siren_2.png");

    public static final ResourceLocation TEXTURE_2_AGGRESSIVE = new ResourceLocation("iceandfire:textures/models/siren/siren_2_aggressive.png");

    public RenderSiren(EntityRendererProvider.Context context) {
        super(context, new ModelSiren(), 0.8F);
    }

    public void scale(@NotNull EntitySiren LivingEntityIn, PoseStack stack, float partialTickTime) {
        stack.translate(0.0F, 0.0F, -0.5F);
    }

    @NotNull
    public ResourceLocation getTextureLocation(EntitySiren siren) {
        switch(siren.getHairColor()) {
            case 1:
                return siren.isAgressive() ? TEXTURE_1_AGGRESSIVE : TEXTURE_1;
            case 2:
                return siren.isAgressive() ? TEXTURE_2_AGGRESSIVE : TEXTURE_2;
            default:
                return siren.isAgressive() ? TEXTURE_0_AGGRESSIVE : TEXTURE_0;
        }
    }

    public static ResourceLocation getSirenOverlayTexture(int siren) {
        switch(siren) {
            case 1:
                return TEXTURE_1;
            case 2:
                return TEXTURE_2;
            default:
                return TEXTURE_0;
        }
    }
}