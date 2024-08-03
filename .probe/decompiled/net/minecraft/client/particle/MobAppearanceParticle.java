package net.minecraft.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.GuardianModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ElderGuardianRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class MobAppearanceParticle extends Particle {

    private final Model model;

    private final RenderType renderType = RenderType.entityTranslucent(ElderGuardianRenderer.GUARDIAN_ELDER_LOCATION);

    MobAppearanceParticle(ClientLevel clientLevel0, double double1, double double2, double double3) {
        super(clientLevel0, double1, double2, double3);
        this.model = new GuardianModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.ELDER_GUARDIAN));
        this.f_107226_ = 0.0F;
        this.f_107225_ = 30;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @Override
    public void render(VertexConsumer vertexConsumer0, Camera camera1, float float2) {
        float $$3 = ((float) this.f_107224_ + float2) / (float) this.f_107225_;
        float $$4 = 0.05F + 0.5F * Mth.sin($$3 * (float) Math.PI);
        PoseStack $$5 = new PoseStack();
        $$5.mulPose(camera1.rotation());
        $$5.mulPose(Axis.XP.rotationDegrees(150.0F * $$3 - 60.0F));
        $$5.scale(-1.0F, -1.0F, 1.0F);
        $$5.translate(0.0F, -1.101F, 1.5F);
        MultiBufferSource.BufferSource $$6 = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer $$7 = $$6.getBuffer(this.renderType);
        this.model.renderToBuffer($$5, $$7, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, $$4);
        $$6.endBatch();
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            return new MobAppearanceParticle(clientLevel1, double2, double3, double4);
        }
    }
}