package com.github.alexmodguy.alexscaves.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.Consumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class NuclearSirenSonarParticle extends TextureSheetParticle {

    private float xRot;

    private float yRot;

    private float fadeR;

    private float fadeG;

    private float fadeB;

    protected NuclearSirenSonarParticle(ClientLevel world, double x, double y, double z, float xRot, float yRot) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
        this.m_107250_(0.4F, 0.4F);
        this.m_107253_(1.0F, 1.0F, 1.0F);
        this.f_107225_ = 8;
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        this.f_107663_ = 0.4F;
        this.f_172258_ = 1.0F;
        this.xRot = xRot;
        this.yRot = yRot;
    }

    public void setFadeColor(int i) {
        this.fadeR = (float) ((i & 0xFF0000) >> 16) / 255.0F;
        this.fadeG = (float) ((i & 0xFF00) >> 8) / 255.0F;
        this.fadeB = (float) ((i & 0xFF) >> 0) / 255.0F;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        float f = ((float) this.f_107224_ - (float) (this.f_107225_ / 2)) / (float) this.f_107225_;
        float f1 = (float) this.f_107224_ / (float) this.f_107225_;
        float f2 = 1.0F - 0.1F * f1;
        this.f_172258_ = 1.0F - 0.65F * f1;
        if (this.f_107224_ > this.f_107225_ / 2) {
            this.m_107271_(1.0F - f * 2.0F);
        }
        this.f_107227_ = this.f_107227_ + (this.fadeR - this.f_107227_) * 0.1F;
        this.f_107228_ = this.f_107228_ + (this.fadeG - this.f_107228_) * 0.1F;
        this.f_107229_ = this.f_107229_ + (this.fadeB - this.f_107229_) * 0.1F;
        Vec3 motionVec = new Vec3(0.0, 0.0, 0.055F).xRot((float) Math.toRadians((double) this.xRot)).yRot(-((float) Math.toRadians((double) this.yRot)));
        this.f_107215_ = this.f_107215_ + motionVec.x * (double) f2;
        this.f_107216_ = this.f_107216_ + motionVec.y * (double) f2;
        this.f_107217_ = this.f_107217_ + motionVec.z * (double) f2;
        this.f_107219_ = this.f_107224_ > 3;
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            this.f_107215_ = this.f_107215_ * (double) this.f_172258_;
            this.f_107216_ = this.f_107216_ * (double) this.f_172258_;
            this.f_107217_ = this.f_107217_ * (double) this.f_172258_;
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public int getLightColor(float partialTicks) {
        return 240;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        return this.f_107663_ * Mth.clamp(((float) this.f_107224_ + scaleFactor) / (float) this.f_107225_, 0.0F, 1.0F) * 2.0F;
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        this.renderSignal(vertexConsumer, camera, partialTick, quaternionf -> quaternionf.rotateY(-((float) Math.toRadians((double) this.yRot))).rotateX(-((float) Math.toRadians((double) this.xRot))));
        this.renderSignal(vertexConsumer, camera, partialTick, quaternionf -> quaternionf.rotateY((float) -Math.PI - (float) Math.toRadians((double) this.yRot)).rotateX((float) Math.toRadians((double) this.xRot)));
    }

    private void renderSignal(VertexConsumer consumer, Camera camera, float partialTicks, Consumer<Quaternionf> rots) {
        Vec3 vec3 = camera.getPosition();
        float f = (float) (Mth.lerp((double) partialTicks, this.f_107209_, this.f_107212_) - vec3.x());
        float f1 = (float) (Mth.lerp((double) partialTicks, this.f_107210_, this.f_107213_) - vec3.y());
        float f2 = (float) (Mth.lerp((double) partialTicks, this.f_107211_, this.f_107214_) - vec3.z());
        Vector3f vector3f = new Vector3f(0.5F, 0.5F, 0.5F).normalize();
        Quaternionf quaternionf = new Quaternionf().setAngleAxis(0.0F, vector3f.x(), vector3f.y(), vector3f.z());
        rots.accept(quaternionf);
        Vector3f[] avector3f = new Vector3f[] { new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F) };
        float f3 = this.getQuadSize(partialTicks);
        for (int i = 0; i < 4; i++) {
            Vector3f vector3f1 = avector3f[i];
            vector3f1.rotate(quaternionf);
            vector3f1.mul(f3);
            vector3f1.add(f, f1, f2);
        }
        float f6 = this.m_5970_();
        float f7 = this.m_5952_();
        float f4 = this.m_5951_();
        float f5 = this.m_5950_();
        int j = this.getLightColor(partialTicks);
        consumer.vertex((double) avector3f[0].x(), (double) avector3f[0].y(), (double) avector3f[0].z()).uv(f7, f5).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        consumer.vertex((double) avector3f[1].x(), (double) avector3f[1].y(), (double) avector3f[1].z()).uv(f7, f4).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        consumer.vertex((double) avector3f[2].x(), (double) avector3f[2].y(), (double) avector3f[2].z()).uv(f6, f4).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        consumer.vertex((double) avector3f[3].x(), (double) avector3f[3].y(), (double) avector3f[3].z()).uv(f6, f5).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            NuclearSirenSonarParticle particle = new NuclearSirenSonarParticle(worldIn, x, y, z, (float) xSpeed, (float) ySpeed);
            particle.m_108335_(this.spriteSet);
            particle.setFadeColor(60928);
            return particle;
        }
    }
}