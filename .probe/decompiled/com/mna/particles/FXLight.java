package com.mna.particles;

import com.mna.api.particles.MAParticleType;
import com.mna.particles.base.MAParticleBase;
import com.mna.particles.types.render.ParticleRenderTypes;
import com.mna.tools.math.Vector3;
import com.mojang.blaze3d.vertex.VertexConsumer;
import javax.annotation.Nonnull;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FXLight extends MAParticleBase {

    public FXLight(ClientLevel world, double x, double y, double z, SpriteSet sprite) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.f_107216_ = 0.0;
        this.f_107215_ = 0.0;
        this.f_107217_ = 0.0;
        this.m_107271_(0.0F);
        this.f_107227_ = 0.225F;
        this.f_107228_ = 0.245F;
        this.f_107229_ = 0.95F;
        this.f_107663_ *= 0.75F;
        this.f_107219_ = false;
        super.f_108321_ = sprite.get((int) (Math.random() * 30.0), this.f_107225_);
        this.maxAlpha = 0.2F;
        this.f_107225_ = 20;
    }

    @Override
    public float getQuadSize(float partialTicks) {
        return this.f_107663_;
    }

    @Override
    public void setMaxAlpha(float maxAlpha) {
        super.setMaxAlpha(maxAlpha * 0.2F);
    }

    @Nonnull
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderTypes.ADDITIVE;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        Vec3 vec3d = renderInfo.getPosition();
        float f = (float) (Mth.lerp((double) partialTicks, this.f_107209_, this.f_107212_) - vec3d.x());
        float f1 = (float) (Mth.lerp((double) partialTicks, this.f_107210_, this.f_107213_) - vec3d.y());
        float f2 = (float) (Mth.lerp((double) partialTicks, this.f_107211_, this.f_107214_) - vec3d.z());
        Vector3 myPos = new Vector3(this.f_107209_, this.f_107210_, this.f_107211_);
        Vector3 projected = new Vector3(this.f_107212_, this.f_107213_, this.f_107214_);
        Quaternionf quaternion = new Quaternionf(renderInfo.rotation());
        quaternion.mul(Vector3.LookAt(myPos, projected));
        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f1.rotate(quaternion);
        Vector3f[] avector3f = new Vector3f[] { new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F) };
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
        int j = this.m_6355_(partialTicks);
        buffer.vertex((double) avector3f[0].x(), (double) avector3f[0].y(), (double) avector3f[0].z()).uv(f8, f6).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        buffer.vertex((double) avector3f[1].x(), (double) avector3f[1].y(), (double) avector3f[1].z()).uv(f8, f5).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        buffer.vertex((double) avector3f[2].x(), (double) avector3f[2].y(), (double) avector3f[2].z()).uv(f7, f5).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        buffer.vertex((double) avector3f[3].x(), (double) avector3f[3].y(), (double) avector3f[3].z()).uv(f7, f6).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXLightVelocity extends MAParticleBase.FXParticleFactoryBase {

        public FXLightVelocity(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXLight particle = new FXLight(worldIn, x, y, z, this.spriteSet);
            particle.setMoveVelocity(xSpeed, ySpeed, zSpeed, false);
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }
}