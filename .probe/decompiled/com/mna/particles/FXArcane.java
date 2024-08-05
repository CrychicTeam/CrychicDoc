package com.mna.particles;

import com.mna.api.particles.MAParticleType;
import com.mna.particles.base.MAParticleBase;
import com.mna.tools.math.Vector3;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FXArcane extends MAParticleBase {

    private boolean stretchRender = false;

    private boolean shrink = false;

    public FXArcane(ClientLevel world, double x, double y, double z, SpriteSet sprite) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.f_107216_ *= 0.2F;
        this.f_107215_ = 0.0;
        this.f_107217_ = 0.0;
        this.colorTransitions.add(new Vector3(234.0, 95.0, 64.0));
        this.colorTransitions.add(new Vector3(203.0, 8.0, 112.0));
        this.colorTransitions.add(new Vector3(151.0, 25.0, 151.0));
        this.colorTransitions.add(new Vector3(77.0, 133.0, 207.0));
        this.m_107253_(((Vector3) this.colorTransitions.get(0)).x / 255.0F, ((Vector3) this.colorTransitions.get(0)).y / 255.0F, ((Vector3) this.colorTransitions.get(0)).z / 255.0F);
        this.f_107231_ = (float) (-45.0 + Math.random() * 90.0 / (Math.PI / 180.0));
        this.f_107204_ = this.f_107231_;
        this.f_107663_ *= 0.15F;
        this.m_107271_(0.0F);
        this.f_107225_ = 30 + (int) Math.ceil(Math.random() * 10.0);
        this.f_107219_ = false;
        super.f_108321_ = sprite.get((int) (Math.random() * 30.0), this.f_107225_);
    }

    private void setStretchRender() {
        this.stretchRender = true;
    }

    @Override
    public float getQuadSize(float partialTicks) {
        return !this.shrink ? this.f_107663_ * (1.0F + (float) this.f_107224_ / (float) this.f_107225_ * 3.0F) : this.f_107663_ - (float) this.f_107224_ / (float) this.f_107225_;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        this.f_107204_ = this.f_107231_;
        this.f_107231_ = (float) ((double) this.f_107231_ + Math.random() * Math.PI / 180.0);
        super.tick();
    }

    public Vector3 getPosition() {
        return new Vector3(this.f_107212_, this.f_107213_, this.f_107214_);
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 10485760;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        if (this.stretchRender) {
            this.renderStretched(buffer, renderInfo, partialTicks);
        } else {
            super.m_5744_(buffer, renderInfo, partialTicks);
        }
    }

    protected void renderStretched(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        Vec3 vector3d = renderInfo.getPosition();
        float f = (float) (Mth.lerp((double) partialTicks, this.f_107209_, this.f_107212_) - vector3d.x());
        float f1 = (float) (Mth.lerp((double) partialTicks, this.f_107210_, this.f_107213_) - vector3d.y());
        float f2 = (float) (Mth.lerp((double) partialTicks, this.f_107211_, this.f_107214_) - vector3d.z());
        Quaternionf quaternion;
        if (this.f_107231_ == 0.0F) {
            quaternion = renderInfo.rotation();
        } else {
            quaternion = new Quaternionf(renderInfo.rotation());
            float f3 = Mth.lerp(partialTicks, this.f_107204_, this.f_107231_);
            quaternion.mul(Axis.ZP.rotation(f3));
        }
        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f1.rotate(quaternion);
        Vector3f[] avector3f = new Vector3f[] { new Vector3f(-1.0F, -2.0F, 0.0F), new Vector3f(-1.0F, 2.0F, 0.0F), new Vector3f(1.0F, 2.0F, 0.0F), new Vector3f(1.0F, -2.0F, 0.0F) };
        float f4 = this.getQuadSize(partialTicks);
        for (int i = 0; i < 4; i++) {
            Vector3f vector3f = avector3f[i];
            vector3f.rotate(quaternion);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }
        float f7 = this.m_5970_();
        float f8 = this.m_5952_();
        float f5 = this.m_5951_();
        float f6 = this.m_5950_();
        int j = this.getLightColor(partialTicks);
        buffer.vertex((double) avector3f[0].x(), (double) avector3f[0].y(), (double) avector3f[0].z()).uv(f8, f6).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        buffer.vertex((double) avector3f[1].x(), (double) avector3f[1].y(), (double) avector3f[1].z()).uv(f8, f5).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        buffer.vertex((double) avector3f[2].x(), (double) avector3f[2].y(), (double) avector3f[2].z()).uv(f7, f5).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        buffer.vertex((double) avector3f[3].x(), (double) avector3f[3].y(), (double) avector3f[3].z()).uv(f7, f6).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXArcaneFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXArcaneFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXArcane particle = new FXArcane(worldIn, x, y, z, this.spriteSet);
            particle.setMoveVelocity(xSpeed, ySpeed, zSpeed, true);
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXArcaneMagelight extends MAParticleBase.FXParticleFactoryBase {

        public FXArcaneMagelight(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXArcane particle = new FXArcane(worldIn, x, y, z, this.spriteSet);
            particle.setMoveVelocity(xSpeed, ySpeed, zSpeed, true);
            particle.f_107225_ = 30;
            particle.shrink = true;
            particle.f_107663_ = 1.0F;
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXArcaneRandomFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXArcaneRandomFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXArcane particle = new FXArcane(worldIn, x, y, z, this.spriteSet);
            particle.setMoveRandomly(xSpeed, ySpeed, zSpeed);
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXArcaneStretchLerp extends MAParticleBase.FXParticleFactoryBase {

        public FXArcaneStretchLerp(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXArcane particle = new FXArcane(worldIn, x, y, z, this.spriteSet);
            particle.setMoveLerp(new Vector3(x, y, z), new Vector3(xSpeed, ySpeed, zSpeed));
            particle.setStretchRender();
            particle.f_107225_ = 15;
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }
}