package org.violetmoon.quark.content.mobs.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.client.handler.ModelHandler;
import org.violetmoon.quark.content.mobs.client.layer.shiba.ShibaCollarLayer;
import org.violetmoon.quark.content.mobs.client.layer.shiba.ShibaMouthItemLayer;
import org.violetmoon.quark.content.mobs.client.model.ShibaModel;
import org.violetmoon.quark.content.mobs.entity.Shiba;

public class ShibaRenderer extends MobRenderer<Shiba, ShibaModel> {

    private static final ResourceLocation[] SHIBA_BASES = new ResourceLocation[] { new ResourceLocation("quark", "textures/model/entity/shiba/shiba0.png"), new ResourceLocation("quark", "textures/model/entity/shiba/shiba1.png"), new ResourceLocation("quark", "textures/model/entity/shiba/shiba2.png") };

    private static final ResourceLocation SHIBA_RARE = new ResourceLocation("quark", "textures/model/entity/shiba/shiba_rare.png");

    private static final ResourceLocation SHIBA_DOGE = new ResourceLocation("quark", "textures/model/entity/shiba/shiba_doge.png");

    public ShibaRenderer(EntityRendererProvider.Context context) {
        super(context, ModelHandler.model(ModelHandler.shiba), 0.5F);
        this.m_115326_(new ShibaCollarLayer(this));
        this.m_115326_(new ShibaMouthItemLayer(this, context.getItemInHandRenderer()));
    }

    @NotNull
    public ResourceLocation getTextureLocation(Shiba entity) {
        if (entity.m_8077_() && entity.m_7770_().getString().trim().equalsIgnoreCase("doge")) {
            return SHIBA_DOGE;
        } else {
            long least = Math.abs(entity.m_20148_().getLeastSignificantBits());
            if (least % 200L == 0L) {
                return SHIBA_RARE;
            } else {
                int type = (int) (least % (long) SHIBA_BASES.length);
                return SHIBA_BASES[type];
            }
        }
    }
}