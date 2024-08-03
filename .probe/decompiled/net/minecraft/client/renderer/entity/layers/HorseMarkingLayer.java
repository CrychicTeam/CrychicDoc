package net.minecraft.client.renderer.entity.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Markings;

public class HorseMarkingLayer extends RenderLayer<Horse, HorseModel<Horse>> {

    private static final Map<Markings, ResourceLocation> LOCATION_BY_MARKINGS = Util.make(Maps.newEnumMap(Markings.class), p_117069_ -> {
        p_117069_.put(Markings.NONE, null);
        p_117069_.put(Markings.WHITE, new ResourceLocation("textures/entity/horse/horse_markings_white.png"));
        p_117069_.put(Markings.WHITE_FIELD, new ResourceLocation("textures/entity/horse/horse_markings_whitefield.png"));
        p_117069_.put(Markings.WHITE_DOTS, new ResourceLocation("textures/entity/horse/horse_markings_whitedots.png"));
        p_117069_.put(Markings.BLACK_DOTS, new ResourceLocation("textures/entity/horse/horse_markings_blackdots.png"));
    });

    public HorseMarkingLayer(RenderLayerParent<Horse, HorseModel<Horse>> renderLayerParentHorseHorseModelHorse0) {
        super(renderLayerParentHorseHorseModelHorse0);
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, Horse horse3, float float4, float float5, float float6, float float7, float float8, float float9) {
        ResourceLocation $$10 = (ResourceLocation) LOCATION_BY_MARKINGS.get(horse3.getMarkings());
        if ($$10 != null && !horse3.m_20145_()) {
            VertexConsumer $$11 = multiBufferSource1.getBuffer(RenderType.entityTranslucent($$10));
            ((HorseModel) this.m_117386_()).m_7695_(poseStack0, $$11, int2, LivingEntityRenderer.getOverlayCoords(horse3, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}