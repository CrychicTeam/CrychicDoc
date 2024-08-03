package com.github.alexmodguy.alexscaves.client.particle;

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

public class FerrouslimeParticle extends TextureSheetParticle {

    private final int followEntityId;

    protected FerrouslimeParticle(ClientLevel world, double x, double y, double z, double followEntityId, SpriteSet spriteSet) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.f_107663_ = this.f_107663_ * (1.2F + world.f_46441_.nextFloat() * 0.5F);
        this.f_107219_ = true;
        this.followEntityId = (int) followEntityId;
        this.m_108335_(spriteSet);
        this.f_107225_ = (int) (Math.random() * 30.0) + 40;
        this.f_107227_ = 0.22F - world.f_46441_.nextFloat() * 0.05F;
        this.f_107228_ = 0.23F - world.f_46441_.nextFloat() * 0.05F;
        this.f_107229_ = 0.32F - world.f_46441_.nextFloat() * 0.05F;
        this.f_172258_ = 0.8F;
        this.f_107230_ = 0.5F + world.f_46441_.nextFloat() * 0.3F;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        return this.f_107663_ * Mth.clamp(((float) this.f_107224_ + scaleFactor) / (float) this.f_107225_ * 16.0F, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            Entity entity = this.f_107208_.getEntity(this.followEntityId);
            if (entity != null) {
                Vec3 vec3 = entity.getEyePosition().add(entity.getDeltaMovement());
                Vec3 movement = vec3.subtract(this.f_107212_, this.f_107213_, this.f_107214_).normalize().scale(0.05F);
                this.f_107215_ = movement.x;
                this.f_107216_ = this.f_107216_ + movement.y;
                this.f_107217_ = this.f_107217_ + movement.z;
            }
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

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FerrouslimeParticle(worldIn, x, y, z, xSpeed, this.spriteSet);
        }
    }
}