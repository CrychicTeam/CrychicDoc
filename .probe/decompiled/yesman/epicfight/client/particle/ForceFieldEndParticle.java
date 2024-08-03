package yesman.epicfight.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.renderer.LightningRenderHelper;

@OnlyIn(Dist.CLIENT)
public class ForceFieldEndParticle extends Particle {

    private boolean init;

    protected ForceFieldEndParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.f_107225_ = 10;
        Minecraft mc = Minecraft.getInstance();
        mc.particleEngine.add(new DustParticle.ExpansiveMetaParticle(level, x, y, z, 6.0, 80));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return EpicFightParticleRenderTypes.LIGHTNING;
    }

    @Override
    public void render(VertexConsumer vertexBuilder, Camera camera, float parttialTick) {
        PoseStack poseStack = new PoseStack();
        Vec3 vec3 = camera.getPosition();
        float f = (float) (Mth.lerp((double) parttialTick, this.f_107209_, this.f_107212_) - vec3.x());
        float f1 = (float) (Mth.lerp((double) parttialTick, this.f_107210_, this.f_107213_) - vec3.y());
        float f2 = (float) (Mth.lerp((double) parttialTick, this.f_107211_, this.f_107214_) - vec3.z());
        poseStack.translate(f, f1, f2);
        if (this.f_107224_ > 0) {
            float progression = ((float) this.f_107224_ + parttialTick) / (float) this.f_107225_;
            LightningRenderHelper.renderFlashingLight(vertexBuilder, poseStack, 255, 0, 255, 15, 1.0F, progression);
        }
        if (!this.init) {
            ClientEngine.getInstance().renderEngine.getOverlayManager().flickering("flickering", 0.05F, 1.2F);
            this.init = true;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ForceFieldEndParticle(level, x, y, z);
        }
    }
}