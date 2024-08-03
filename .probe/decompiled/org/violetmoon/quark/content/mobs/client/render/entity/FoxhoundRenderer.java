package org.violetmoon.quark.content.mobs.client.render.entity;

import java.util.UUID;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.client.handler.ModelHandler;
import org.violetmoon.quark.content.mobs.client.layer.FoxhoundCollarLayer;
import org.violetmoon.quark.content.mobs.client.model.FoxhoundModel;
import org.violetmoon.quark.content.mobs.entity.Foxhound;

public class FoxhoundRenderer extends MobRenderer<Foxhound, FoxhoundModel> {

    private static final ResourceLocation FOXHOUND_IDLE = new ResourceLocation("quark", "textures/model/entity/foxhound/red/idle.png");

    private static final ResourceLocation FOXHOUND_HOSTILE = new ResourceLocation("quark", "textures/model/entity/foxhound/red/hostile.png");

    private static final ResourceLocation FOXHOUND_SLEEPING = new ResourceLocation("quark", "textures/model/entity/foxhound/red/sleeping.png");

    private static final ResourceLocation SOULHOUND_IDLE = new ResourceLocation("quark", "textures/model/entity/foxhound/blue/idle.png");

    private static final ResourceLocation SOULHOUND_HOSTILE = new ResourceLocation("quark", "textures/model/entity/foxhound/blue/hostile.png");

    private static final ResourceLocation SOULHOUND_SLEEPING = new ResourceLocation("quark", "textures/model/entity/foxhound/blue/sleeping.png");

    private static final ResourceLocation BASALT_FOXHOUND_IDLE = new ResourceLocation("quark", "textures/model/entity/foxhound/black/idle.png");

    private static final ResourceLocation BASALT_FOXHOUND_HOSTILE = new ResourceLocation("quark", "textures/model/entity/foxhound/black/hostile.png");

    private static final ResourceLocation BASALT_FOXHOUND_SLEEPING = new ResourceLocation("quark", "textures/model/entity/foxhound/black/sleeping.png");

    private static final int SHINY_CHANCE = 256;

    public FoxhoundRenderer(EntityRendererProvider.Context context) {
        super(context, ModelHandler.model(ModelHandler.foxhound), 0.5F);
        this.m_115326_(new FoxhoundCollarLayer(this));
    }

    @NotNull
    public ResourceLocation getTextureLocation(@NotNull Foxhound entity) {
        if (entity.isBlue()) {
            return entity.m_5803_() ? SOULHOUND_SLEEPING : (entity.getRemainingPersistentAngerTime() > 0 ? SOULHOUND_HOSTILE : SOULHOUND_IDLE);
        } else {
            UUID id = entity.m_20148_();
            long most = id.getMostSignificantBits();
            if (most % 256L == 0L) {
                return entity.m_5803_() ? BASALT_FOXHOUND_SLEEPING : (entity.getRemainingPersistentAngerTime() > 0 ? BASALT_FOXHOUND_HOSTILE : BASALT_FOXHOUND_IDLE);
            } else {
                return entity.m_5803_() ? FOXHOUND_SLEEPING : (entity.getRemainingPersistentAngerTime() > 0 ? FOXHOUND_HOSTILE : FOXHOUND_IDLE);
            }
        }
    }
}