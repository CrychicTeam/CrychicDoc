package io.redspace.ironsspellbooks.particle;

import io.redspace.ironsspellbooks.api.util.Utils;
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

public class PortalFrameParticle extends TextureSheetParticle {

    float pathWidth;

    float pathHeight;

    float yRad;

    float rotSpeed;

    float rot;

    final Vec3 origin;

    public PortalFrameParticle(ClientLevel level, double xCoord, double yCoord, double zCoord, double pathWidth, double pathHeight, double yDegrees) {
        super(level, xCoord, yCoord, zCoord, pathWidth, pathHeight, yDegrees);
        this.origin = new Vec3(xCoord, yCoord, zCoord);
        this.pathWidth = (float) pathWidth * 0.5F;
        this.pathHeight = (float) pathHeight * 0.5F;
        this.yRad = (float) (yDegrees * (float) (Math.PI / 180.0));
        this.m_6569_(this.f_107223_.nextFloat() * 1.75F + 1.0F);
        this.f_107225_ = 40 + (int) (Math.random() * 45.0);
        this.f_107226_ = 0.0F;
        this.rotSpeed = (float) Utils.random.nextIntBetweenInclusive(5, 10) * 0.04F;
        this.rotSpeed = this.rotSpeed * this.rotSpeed * (float) (Utils.random.nextBoolean() ? -1 : 1);
        this.rot = Utils.random.nextFloat() * (float) (Math.PI * 2);
        this.f_107663_ = 0.0625F;
        float f = this.f_107223_.nextFloat() * 0.6F + 0.4F;
        this.f_107227_ = f * 0.9F;
        this.f_107228_ = f * 0.3F;
        this.f_107229_ = f;
        this.updatePos();
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            this.updatePos();
            this.pathWidth = this.pathWidth * (float) (1.0 + (double) (Utils.random.nextFloat() * 0.075F * this.rotSpeed));
            this.pathHeight = this.pathHeight * (float) (1.0 + (double) (Utils.random.nextFloat() * 0.075F * this.rotSpeed));
            this.f_107663_ = this.f_107663_ - this.rotSpeed * this.rotSpeed * 0.02F;
        }
    }

    private void updatePos() {
        this.f_107212_ = this.origin.x + (double) (Mth.cos(this.rot) * this.pathWidth * Mth.cos(this.yRad));
        this.f_107213_ = this.origin.y + (double) (Mth.sin(this.rot) * this.pathHeight);
        this.f_107214_ = this.origin.z + (double) (Mth.cos(this.rot) * this.pathWidth * Mth.sin(this.yRad));
        this.rot = (this.rotSpeed + this.rot) % (float) (Math.PI * 2);
        Vec3 speed = new Vec3(this.f_107212_ - this.f_107209_, this.f_107213_ - this.f_107210_, this.f_107214_ - this.f_107211_);
        this.m_107259_(this.m_107277_().move(speed.x, speed.y, speed.z));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ZapParticle.PARTICLE_EMISSIVE;
    }

    @Override
    public int getLightColor(float float0) {
        return 15728880;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            PortalFrameParticle particle = new PortalFrameParticle(level, x, y, z, dx, dy, dz);
            particle.m_108335_(this.sprites);
            return particle;
        }
    }
}