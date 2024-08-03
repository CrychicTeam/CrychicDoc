package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.CarriedBlockLayer;
import net.minecraft.client.renderer.entity.layers.EnderEyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class EndermanRenderer extends MobRenderer<EnderMan, EndermanModel<EnderMan>> {

    private static final ResourceLocation ENDERMAN_LOCATION = new ResourceLocation("textures/entity/enderman/enderman.png");

    private final RandomSource random = RandomSource.create();

    public EndermanRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new EndermanModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.ENDERMAN)), 0.5F);
        this.m_115326_(new EnderEyesLayer<>(this));
        this.m_115326_(new CarriedBlockLayer(this, entityRendererProviderContext0.getBlockRenderDispatcher()));
    }

    public void render(EnderMan enderMan0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        BlockState $$6 = enderMan0.getCarriedBlock();
        EndermanModel<EnderMan> $$7 = (EndermanModel<EnderMan>) this.m_7200_();
        $$7.carrying = $$6 != null;
        $$7.creepy = enderMan0.isCreepy();
        super.render(enderMan0, float1, float2, poseStack3, multiBufferSource4, int5);
    }

    public Vec3 getRenderOffset(EnderMan enderMan0, float float1) {
        if (enderMan0.isCreepy()) {
            double $$2 = 0.02;
            return new Vec3(this.random.nextGaussian() * 0.02, 0.0, this.random.nextGaussian() * 0.02);
        } else {
            return super.m_7860_(enderMan0, float1);
        }
    }

    public ResourceLocation getTextureLocation(EnderMan enderMan0) {
        return ENDERMAN_LOCATION;
    }
}