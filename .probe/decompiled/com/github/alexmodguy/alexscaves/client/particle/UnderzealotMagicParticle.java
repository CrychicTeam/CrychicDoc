package com.github.alexmodguy.alexscaves.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class UnderzealotMagicParticle extends TextureSheetParticle {

    private static final RandomSource RANDOM = RandomSource.create();

    private final SpriteSet sprites;

    private Vec3 target;

    public UnderzealotMagicParticle(ClientLevel level, double x, double y, double z, double toX, double toY, double toZ, SpriteSet spriteSet) {
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
        this.f_107663_ *= 1.15F;
        this.f_107225_ = (int) (10.0 / (Math.random() * 0.8 + 0.2));
        this.f_107219_ = false;
        this.target = new Vec3(toX, toY, toZ);
        this.m_108339_(spriteSet);
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
    public void tick() {
        super.m_5989_();
        this.m_108339_(this.sprites);
        this.m_107271_(Mth.lerp(0.05F, this.f_107230_, 1.0F));
        Vec3 to = this.target.subtract(this.f_107212_, this.f_107213_, this.f_107214_);
        if (to.length() > 1.0) {
            to = to.normalize();
        }
        this.f_107215_ = this.f_107215_ + to.x * 0.03F;
        this.f_107216_ = this.f_107216_ + to.y * 0.03F;
        this.f_107217_ = this.f_107217_ + to.z * 0.03F;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Factory(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            UnderzealotMagicParticle spellparticle = new UnderzealotMagicParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.sprite);
            float f = worldIn.f_46441_.nextFloat() * 0.15F + 0.85F;
            spellparticle.m_107253_(0.1F * f, 0.1F * f, 0.1F * f);
            return spellparticle;
        }
    }
}