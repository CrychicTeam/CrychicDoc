package com.github.alexmodguy.alexscaves.client.particle;

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

public class AmberMonolithParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    private Vec3 target;

    private float initialDistance = 0.0F;

    public AmberMonolithParticle(ClientLevel level, double x, double y, double z, double toX, double toY, double toZ, SpriteSet spriteSet) {
        super(level, x, y, z, 0.0, 0.0, 0.0);
        this.f_172258_ = 0.96F;
        this.f_107226_ = 0.1F;
        this.f_172259_ = true;
        this.sprites = spriteSet;
        this.f_107212_ = x;
        this.f_107213_ = y;
        this.f_107214_ = z;
        this.f_107216_ = 0.0;
        this.f_107215_ = 0.0;
        this.f_107217_ = 0.0;
        this.f_107663_ *= 1.125F;
        this.f_107225_ = (int) (20.0 / (Math.random() * 0.8 + 0.2));
        this.f_107219_ = false;
        this.target = new Vec3(toX, toY, toZ);
        this.m_108339_(spriteSet);
        this.initialDistance = (float) this.target.subtract(x, y, z).length();
        this.f_107227_ = 1.0F;
        this.f_107228_ = 0.69F;
        this.f_107229_ = 0.12F;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public int getLightColor(float partialTicks) {
        Vec3 to = this.target.subtract(this.f_107212_, this.f_107213_, this.f_107214_);
        float glowBy = (float) (to.length() / (double) this.initialDistance);
        int i = super.m_6355_(partialTicks);
        int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        j += (int) (glowBy * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }
        return j | k << 16;
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.m_108339_(this.sprites);
        this.m_107271_(Mth.lerp(0.05F, this.f_107230_, 1.0F));
        Vec3 to = this.target.subtract(this.f_107212_, this.f_107213_, this.f_107214_);
        if (to.length() > 1.0) {
            to = to.normalize();
        } else {
            this.m_107274_();
        }
        this.f_107215_ = this.f_107215_ + to.x * 0.05F;
        this.f_107216_ = this.f_107216_ + to.y * 0.05F;
        this.f_107217_ = this.f_107217_ + to.z * 0.05F;
        this.f_107227_ = Math.min(1.0F, this.f_107227_ + 0.03F);
        this.f_107228_ = Math.min(1.0F, this.f_107228_ + 0.03F);
        this.f_107229_ = Math.min(1.0F, this.f_107229_ + 0.03F);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Factory(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new AmberMonolithParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.sprite);
        }
    }
}