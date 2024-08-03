package software.bernie.example.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import org.joml.Vector3d;
import software.bernie.example.client.model.entity.BatModel;
import software.bernie.example.entity.BatEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class BatRenderer extends GeoEntityRenderer<BatEntity> {

    private int currentTick = -1;

    public BatRenderer(EntityRendererProvider.Context context) {
        super(context, new BatModel());
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    public void renderFinal(PoseStack poseStack, BatEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (this.currentTick < 0 || this.currentTick != animatable.f_19797_) {
            this.currentTick = animatable.f_19797_;
            this.model.getBone("leftear").ifPresent(ear -> {
                RandomSource rand = animatable.m_217043_();
                Vector3d earPos = ear.getWorldPosition();
                animatable.m_20193_().addParticle(ParticleTypes.PORTAL, earPos.x(), earPos.y(), earPos.z(), rand.nextDouble() - 0.5, -rand.nextDouble(), rand.nextDouble() - 0.5);
            });
        }
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}