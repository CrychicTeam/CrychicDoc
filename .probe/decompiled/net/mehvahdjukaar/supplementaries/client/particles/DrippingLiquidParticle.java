package net.mehvahdjukaar.supplementaries.client.particles;

import net.mehvahdjukaar.supplementaries.reg.ModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class DrippingLiquidParticle extends TextureSheetParticle {

    private final Fluid fluid;

    private DrippingLiquidParticle(ClientLevel world, double x, double y, double z, Fluid fluid) {
        super(world, x, y, z);
        this.m_107250_(0.01F, 0.01F);
        this.f_107226_ = 0.06F;
        this.fluid = fluid;
        this.f_107226_ *= 0.02F;
        this.f_107225_ = 40;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        this.ageParticle();
        if (!this.f_107220_) {
            this.f_107216_ = this.f_107216_ - (double) this.f_107226_;
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            this.updateMotion();
            if (!this.f_107220_) {
                this.f_107215_ *= 0.98F;
                this.f_107216_ *= 0.98F;
                this.f_107217_ *= 0.98F;
                BlockPos blockpos = BlockPos.containing(this.f_107212_, this.f_107213_, this.f_107214_);
                FluidState fluidstate = this.f_107208_.m_6425_(blockpos);
                if (fluidstate.getType() == this.fluid && this.f_107213_ < (double) ((float) blockpos.m_123342_() + fluidstate.getHeight(this.f_107208_, blockpos))) {
                    this.m_107274_();
                }
            }
        }
    }

    protected void ageParticle() {
        if (this.f_107225_-- <= 0) {
            this.m_107274_();
            this.f_107208_.addParticle((ParticleOptions) ModParticles.FALLING_LIQUID.get(), this.f_107212_, this.f_107213_, this.f_107214_, (double) this.f_107227_, (double) this.f_107228_, (double) this.f_107229_);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    protected void updateMotion() {
        this.f_107215_ *= 0.02;
        this.f_107216_ *= 0.02;
        this.f_107217_ *= 0.02;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        protected final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double r, double g, double b) {
            DrippingLiquidParticle drippingLiquidParticle = new DrippingLiquidParticle(worldIn, x, y, z, Fluids.WATER);
            drippingLiquidParticle.m_107253_((float) r, (float) g, (float) b);
            drippingLiquidParticle.m_108335_(this.spriteSet);
            return drippingLiquidParticle;
        }
    }
}