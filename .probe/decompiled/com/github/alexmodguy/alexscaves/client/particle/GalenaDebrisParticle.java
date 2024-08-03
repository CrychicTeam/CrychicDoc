package com.github.alexmodguy.alexscaves.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;

public class GalenaDebrisParticle extends TextureSheetParticle {

    private boolean fromRoof;

    private float rotSpeed;

    private double hoverY;

    private int hoverTime = 0;

    public GalenaDebrisParticle(ClientLevel world, double x, double y, double z, boolean fromRoof, SpriteSet sprites) {
        super(world, x, y, z);
        this.m_108335_(sprites);
        this.fromRoof = fromRoof;
        this.f_107226_ = fromRoof ? 0.001F : -0.001F;
        this.f_107225_ = 200 + this.f_107223_.nextInt(200);
        this.f_107663_ = 0.3F;
        this.rotSpeed = ((float) Math.random() - 0.5F) * 0.2F;
        this.f_107231_ = (float) Math.random() * (float) (Math.PI * 2);
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.f_107204_ = this.f_107231_;
        this.f_107231_ = this.f_107231_ + (float) Math.PI * this.rotSpeed * 2.0F;
        if (this.f_107218_) {
            this.f_107204_ = this.f_107231_ = 0.0F;
        }
        double targetY = Math.sin((double) ((float) this.f_107224_ * 0.2F)) * 0.01F;
        this.f_107216_ += targetY;
        int timeLeft = this.f_107225_ - this.f_107224_;
        if (timeLeft < 20) {
            this.f_107663_ = 0.3F * (float) timeLeft / 20.0F;
        }
        this.f_107224_++;
        if (this.f_107224_ < this.f_107225_ && (this.f_107210_ != this.f_107213_ || this.f_107224_ <= 3)) {
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            this.f_107216_ = this.f_107216_ - (double) this.f_107226_;
        } else {
            this.m_107274_();
        }
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTick) {
        super.m_5744_(consumer, camera, partialTick);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            boolean riseOrFall = worldIn.f_46441_.nextBoolean();
            Vec3 startPos = MagneticCavesAmbientParticle.getStartPosition(worldIn, riseOrFall, x, y, z);
            return new GalenaDebrisParticle(worldIn, x, startPos.y, z, riseOrFall, this.spriteSet);
        }
    }
}