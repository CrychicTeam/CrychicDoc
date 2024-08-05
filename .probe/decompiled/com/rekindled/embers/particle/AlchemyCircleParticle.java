package com.rekindled.embers.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.rekindled.embers.render.EmbersRenderTypes;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class AlchemyCircleParticle extends TextureSheetParticle {

    public float rotScale = 0.02F;

    public static final Quaternionf QUAT = Axis.XP.rotationDegrees(90.0F);

    public AlchemyCircleParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, AlchemyCircleParticleOptions pOptions) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.f_107219_ = false;
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
        this.f_107227_ = pOptions.getColor().x();
        this.f_107228_ = pOptions.getColor().y();
        this.f_107229_ = pOptions.getColor().z();
        this.f_107231_ = this.rotScale;
        this.f_107663_ = pOptions.getScale();
        this.f_107225_ = pOptions.getLifetime();
    }

    @Override
    public float getQuadSize(float pScaleFactor) {
        if (this.f_107224_ < 20) {
            return this.f_107663_ * (float) Math.sin((double) ((float) this.f_107224_ + pScaleFactor) * Math.PI / 40.0);
        } else {
            return this.f_107224_ > this.f_107225_ - 20 ? this.f_107663_ - this.f_107663_ * (float) Math.sin((double) ((float) (this.f_107224_ + 20 - this.f_107225_) + pScaleFactor) * Math.PI / 40.0) : this.f_107663_;
        }
    }

    @Override
    public void tick() {
        super.m_5989_();
        if (this.f_107224_ < 20) {
            this.f_107230_ = (float) Math.sin((double) this.f_107224_ * Math.PI / 40.0);
        }
        if (this.f_107224_ > this.f_107225_ - 20) {
            this.f_107230_ = 1.0F - (float) Math.sin((double) (this.f_107224_ + 20 - this.f_107225_) * Math.PI / 40.0);
        }
        this.f_107204_ = this.f_107231_;
        this.f_107231_ = this.f_107231_ + this.rotScale;
    }

    @Override
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        Vec3 vec3 = pRenderInfo.getPosition();
        float f = (float) (Mth.lerp((double) pPartialTicks, this.f_107209_, this.f_107212_) - vec3.x());
        float f1 = (float) (Mth.lerp((double) pPartialTicks, this.f_107210_, this.f_107213_) - vec3.y());
        float f2 = (float) (Mth.lerp((double) pPartialTicks, this.f_107211_, this.f_107214_) - vec3.z());
        Quaternionf quaternion;
        if (this.f_107231_ == 0.0F) {
            quaternion = QUAT;
        } else {
            quaternion = new Quaternionf(QUAT);
            quaternion.rotateZ(Mth.lerp(pPartialTicks, this.f_107204_, this.f_107231_));
        }
        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f1.rotate(quaternion);
        Vector3f[] avector3f = new Vector3f[] { new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F) };
        float f4 = this.getQuadSize(pPartialTicks);
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
        int j = this.getLightColor(pPartialTicks);
        pBuffer.vertex((double) avector3f[0].x(), (double) avector3f[0].y(), (double) avector3f[0].z()).uv(f8, f6).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        pBuffer.vertex((double) avector3f[1].x(), (double) avector3f[1].y(), (double) avector3f[1].z()).uv(f8, f5).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        pBuffer.vertex((double) avector3f[2].x(), (double) avector3f[2].y(), (double) avector3f[2].z()).uv(f7, f5).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        pBuffer.vertex((double) avector3f[3].x(), (double) avector3f[3].y(), (double) avector3f[3].z()).uv(f7, f6).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        pBuffer.vertex((double) avector3f[3].x(), (double) avector3f[3].y(), (double) avector3f[3].z()).uv(f7, f6).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        pBuffer.vertex((double) avector3f[2].x(), (double) avector3f[2].y(), (double) avector3f[2].z()).uv(f7, f5).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        pBuffer.vertex((double) avector3f[1].x(), (double) avector3f[1].y(), (double) avector3f[1].z()).uv(f8, f5).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        pBuffer.vertex((double) avector3f[0].x(), (double) avector3f[0].y(), (double) avector3f[0].z()).uv(f8, f6).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
    }

    @Override
    protected int getLightColor(float partialTicks) {
        return 15728880;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return EmbersRenderTypes.PARTICLE_SHEET_ADDITIVE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider.Sprite<AlchemyCircleParticleOptions> {

        public TextureSheetParticle createParticle(AlchemyCircleParticleOptions pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new AlchemyCircleParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, pType);
        }
    }
}