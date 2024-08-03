package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerSeaSerpentAncient;
import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class RenderSeaSerpent extends MobRenderer<EntitySeaSerpent, AdvancedEntityModel<EntitySeaSerpent>> {

    public static final ResourceLocation TEXTURE_BLUE = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_blue.png");

    public static final ResourceLocation TEXTURE_BLUE_BLINK = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_blue_blink.png");

    public static final ResourceLocation TEXTURE_BRONZE = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_bronze.png");

    public static final ResourceLocation TEXTURE_BRONZE_BLINK = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_bronze_blink.png");

    public static final ResourceLocation TEXTURE_DARKBLUE = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_darkblue.png");

    public static final ResourceLocation TEXTURE_DARKBLUE_BLINK = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_darkblue_blink.png");

    public static final ResourceLocation TEXTURE_GREEN = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_green.png");

    public static final ResourceLocation TEXTURE_GREEN_BLINK = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_green_blink.png");

    public static final ResourceLocation TEXTURE_PURPLE = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_purple.png");

    public static final ResourceLocation TEXTURE_PURPLE_BLINK = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_purple_blink.png");

    public static final ResourceLocation TEXTURE_RED = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_red.png");

    public static final ResourceLocation TEXTURE_RED_BLINK = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_red_blink.png");

    public static final ResourceLocation TEXTURE_TEAL = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_teal.png");

    public static final ResourceLocation TEXTURE_TEAL_BLINK = new ResourceLocation("iceandfire:textures/models/seaserpent/seaserpent_teal_blink.png");

    public RenderSeaSerpent(EntityRendererProvider.Context context, AdvancedEntityModel<EntitySeaSerpent> model) {
        super(context, model, 1.6F);
        this.f_115291_.add(new LayerSeaSerpentAncient(this));
    }

    protected void scale(EntitySeaSerpent entity, PoseStack matrixStackIn, float partialTickTime) {
        this.f_114477_ = entity.getSeaSerpentScale();
        matrixStackIn.scale(this.f_114477_, this.f_114477_, this.f_114477_);
    }

    @NotNull
    public ResourceLocation getTextureLocation(EntitySeaSerpent serpent) {
        switch(serpent.getVariant()) {
            case 0:
                if (serpent.isBlinking()) {
                    return TEXTURE_BLUE_BLINK;
                }
                return TEXTURE_BLUE;
            case 1:
                if (serpent.isBlinking()) {
                    return TEXTURE_BRONZE_BLINK;
                }
                return TEXTURE_BRONZE;
            case 2:
                if (serpent.isBlinking()) {
                    return TEXTURE_DARKBLUE_BLINK;
                }
                return TEXTURE_DARKBLUE;
            case 3:
                if (serpent.isBlinking()) {
                    return TEXTURE_GREEN_BLINK;
                }
                return TEXTURE_GREEN;
            case 4:
                if (serpent.isBlinking()) {
                    return TEXTURE_PURPLE_BLINK;
                }
                return TEXTURE_PURPLE;
            case 5:
                if (serpent.isBlinking()) {
                    return TEXTURE_RED_BLINK;
                }
                return TEXTURE_RED;
            case 6:
                if (serpent.isBlinking()) {
                    return TEXTURE_TEAL_BLINK;
                }
                return TEXTURE_TEAL;
            default:
                return TEXTURE_BLUE;
        }
    }
}