package com.github.alexmodguy.alexscaves.client.particle;

import com.github.alexmodguy.alexscaves.client.render.entity.ForsakenRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.ForsakenEntity;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ForsakenSonarParticle extends TextureSheetParticle {

    private final int forsakenId;

    private float xRot;

    private float yRot;

    private float fadeR;

    private float fadeG;

    private float fadeB;

    private boolean massive;

    private boolean passedTarget;

    protected ForsakenSonarParticle(ClientLevel world, double x, double y, double z, int entityId, float xRot, float yRot, boolean massive) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
        this.m_107250_(massive ? 2.99F : 0.9F, 0.99F);
        this.m_107253_(1.0F, 1.0F, 1.0F);
        this.forsakenId = entityId;
        this.massive = massive;
        this.f_107225_ = 15;
        this.setInMouthPos(1.0F);
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        this.f_107663_ = massive ? 3.0F + world.f_46441_.nextFloat() * 0.5F : 1.0F + world.f_46441_.nextFloat() * 0.3F;
        this.f_172258_ = 1.0F;
        this.xRot = xRot;
        this.yRot = yRot;
        this.setFadeColor(15073280);
        this.angleTowardsTarget();
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
        this.angleTowardsTarget();
        this.f_107227_ = this.f_107227_ + (this.fadeR - this.f_107227_) * 0.1F;
        this.f_107228_ = this.f_107228_ + (this.fadeG - this.f_107228_) * 0.1F;
        this.f_107229_ = this.f_107229_ + (this.fadeB - this.f_107229_) * 0.1F;
        Vec3 motionVec = new Vec3(0.0, 0.0, this.massive ? -0.2F : 0.5).xRot((float) Math.toRadians((double) this.xRot)).yRot(-((float) Math.toRadians((double) this.yRot)));
        this.f_107215_ = this.f_107215_ + motionVec.x * (double) f2;
        this.f_107216_ = this.f_107216_ + motionVec.y * (double) f2;
        this.f_107217_ = this.f_107217_ + motionVec.z * (double) f2;
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            this.f_107215_ = this.f_107215_ * (double) this.f_172258_;
            this.f_107216_ = this.f_107216_ * (double) this.f_172258_;
            this.f_107217_ = this.f_107217_ * (double) this.f_172258_;
        }
    }

    public void setInMouthPos(float partialTick) {
        if (this.forsakenId != -1 && this.f_107208_.getEntity(this.forsakenId) instanceof ForsakenEntity entity) {
            Vec3 mouthPos = ForsakenRenderer.getMouthPositionFor(this.forsakenId);
            if (mouthPos != null) {
                Vec3 translate = mouthPos.add(new Vec3(0.0, this.massive ? 0.75 : 0.0, 0.0)).yRot((float) (Math.PI - (double) (entity.f_20883_ * (float) (Math.PI / 180.0))));
                this.m_107264_(entity.m_20185_() + translate.x, entity.m_20186_() + translate.y, entity.m_20189_() + translate.z);
            }
            if (!this.massive) {
                Entity target = entity.getSonarTarget();
                if (target == null) {
                    this.f_107225_ = 40;
                } else {
                    this.f_107225_ = 15 + Math.min(15, (int) Math.ceil((double) (entity.m_20270_(target) * 3.0F)));
                }
            }
        }
    }

    public void angleTowardsTarget() {
        if (!this.massive && this.forsakenId != -1 && !this.passedTarget && this.f_107208_.getEntity(this.forsakenId) instanceof ForsakenEntity forsakenEntity) {
            Entity target = forsakenEntity.getSonarTarget();
            if (target != null) {
                Vec3 vector3d1 = target.getEyePosition().subtract(this.f_107212_, this.f_107213_, this.f_107214_);
                if (vector3d1.length() < 2.0) {
                    this.passedTarget = true;
                }
                this.yRot = -((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180.0F / (float) Math.PI);
                this.xRot = (float) (Mth.atan2(vector3d1.y, vector3d1.horizontalDistance()) * 180.0F / (float) Math.PI);
            }
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
        float f = this.massive ? 4.0F : 2.0F;
        float f1 = this.massive ? 1.0F : 1.5F;
        return this.f_107663_ * Mth.clamp(((float) this.f_107224_ + scaleFactor) * f1 / (float) this.f_107225_, 0.0F, 1.0F) * f;
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
            ForsakenSonarParticle particle = new ForsakenSonarParticle(worldIn, x, y, z, (int) xSpeed, (float) ySpeed, (float) zSpeed, false);
            particle.m_108335_(this.spriteSet);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class LargeFactory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public LargeFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ForsakenSonarParticle particle = new ForsakenSonarParticle(worldIn, x, y, z, (int) xSpeed, (float) ySpeed, (float) zSpeed, true);
            particle.m_108335_(this.spriteSet);
            return particle;
        }
    }
}