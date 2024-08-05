package com.mna.entities.renderers.faction;

import com.mna.api.tools.RLoc;
import com.mna.entities.faction.util.FactionWar;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class FactionWarRenderer extends EntityRenderer<FactionWar> {

    private static final ResourceLocation texture = RLoc.create("");

    public FactionWarRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    public void render(FactionWar pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
    }

    public ResourceLocation getTextureLocation(FactionWar pEntity) {
        return texture;
    }
}