package net.minecraft.client.renderer.entity.layers;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Map;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.IronGolem;

public class IronGolemCrackinessLayer extends RenderLayer<IronGolem, IronGolemModel<IronGolem>> {

    private static final Map<IronGolem.Crackiness, ResourceLocation> resourceLocations = ImmutableMap.of(IronGolem.Crackiness.LOW, new ResourceLocation("textures/entity/iron_golem/iron_golem_crackiness_low.png"), IronGolem.Crackiness.MEDIUM, new ResourceLocation("textures/entity/iron_golem/iron_golem_crackiness_medium.png"), IronGolem.Crackiness.HIGH, new ResourceLocation("textures/entity/iron_golem/iron_golem_crackiness_high.png"));

    public IronGolemCrackinessLayer(RenderLayerParent<IronGolem, IronGolemModel<IronGolem>> renderLayerParentIronGolemIronGolemModelIronGolem0) {
        super(renderLayerParentIronGolemIronGolemModelIronGolem0);
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, IronGolem ironGolem3, float float4, float float5, float float6, float float7, float float8, float float9) {
        if (!ironGolem3.m_20145_()) {
            IronGolem.Crackiness $$10 = ironGolem3.getCrackiness();
            if ($$10 != IronGolem.Crackiness.NONE) {
                ResourceLocation $$11 = (ResourceLocation) resourceLocations.get($$10);
                m_117376_(this.m_117386_(), $$11, poseStack0, multiBufferSource1, int2, ironGolem3, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}