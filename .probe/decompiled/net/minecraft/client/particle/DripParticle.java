package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class DripParticle extends TextureSheetParticle {

    private final Fluid type;

    protected boolean isGlowing;

    DripParticle(ClientLevel clientLevel0, double double1, double double2, double double3, Fluid fluid4) {
        super(clientLevel0, double1, double2, double3);
        this.m_107250_(0.01F, 0.01F);
        this.f_107226_ = 0.06F;
        this.type = fluid4;
    }

    protected Fluid getType() {
        return this.type;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public int getLightColor(float float0) {
        return this.isGlowing ? 240 : super.m_6355_(float0);
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        this.preMoveUpdate();
        if (!this.f_107220_) {
            this.f_107216_ = this.f_107216_ - (double) this.f_107226_;
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            this.postMoveUpdate();
            if (!this.f_107220_) {
                this.f_107215_ *= 0.98F;
                this.f_107216_ *= 0.98F;
                this.f_107217_ *= 0.98F;
                if (this.type != Fluids.EMPTY) {
                    BlockPos $$0 = BlockPos.containing(this.f_107212_, this.f_107213_, this.f_107214_);
                    FluidState $$1 = this.f_107208_.m_6425_($$0);
                    if ($$1.getType() == this.type && this.f_107213_ < (double) ((float) $$0.m_123342_() + $$1.getHeight(this.f_107208_, $$0))) {
                        this.m_107274_();
                    }
                }
            }
        }
    }

    protected void preMoveUpdate() {
        if (this.f_107225_-- <= 0) {
            this.m_107274_();
        }
    }

    protected void postMoveUpdate() {
    }

    public static TextureSheetParticle createWaterHangParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
        DripParticle $$8 = new DripParticle.DripHangParticle(clientLevel1, double2, double3, double4, Fluids.WATER, ParticleTypes.FALLING_WATER);
        $$8.m_107253_(0.2F, 0.3F, 1.0F);
        return $$8;
    }

    public static TextureSheetParticle createWaterFallParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
        DripParticle $$8 = new DripParticle.FallAndLandParticle(clientLevel1, double2, double3, double4, Fluids.WATER, ParticleTypes.SPLASH);
        $$8.m_107253_(0.2F, 0.3F, 1.0F);
        return $$8;
    }

    public static TextureSheetParticle createLavaHangParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
        return new DripParticle.CoolingDripHangParticle(clientLevel1, double2, double3, double4, Fluids.LAVA, ParticleTypes.FALLING_LAVA);
    }

    public static TextureSheetParticle createLavaFallParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
        DripParticle $$8 = new DripParticle.FallAndLandParticle(clientLevel1, double2, double3, double4, Fluids.LAVA, ParticleTypes.LANDING_LAVA);
        $$8.m_107253_(1.0F, 0.2857143F, 0.083333336F);
        return $$8;
    }

    public static TextureSheetParticle createLavaLandParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
        DripParticle $$8 = new DripParticle.DripLandParticle(clientLevel1, double2, double3, double4, Fluids.LAVA);
        $$8.m_107253_(1.0F, 0.2857143F, 0.083333336F);
        return $$8;
    }

    public static TextureSheetParticle createHoneyHangParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
        DripParticle.DripHangParticle $$8 = new DripParticle.DripHangParticle(clientLevel1, double2, double3, double4, Fluids.EMPTY, ParticleTypes.FALLING_HONEY);
        $$8.f_107226_ *= 0.01F;
        $$8.f_107225_ = 100;
        $$8.m_107253_(0.622F, 0.508F, 0.082F);
        return $$8;
    }

    public static TextureSheetParticle createHoneyFallParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
        DripParticle $$8 = new DripParticle.HoneyFallAndLandParticle(clientLevel1, double2, double3, double4, Fluids.EMPTY, ParticleTypes.LANDING_HONEY);
        $$8.f_107226_ = 0.01F;
        $$8.m_107253_(0.582F, 0.448F, 0.082F);
        return $$8;
    }

    public static TextureSheetParticle createHoneyLandParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
        DripParticle $$8 = new DripParticle.DripLandParticle(clientLevel1, double2, double3, double4, Fluids.EMPTY);
        $$8.f_107225_ = (int) (128.0 / (Math.random() * 0.8 + 0.2));
        $$8.m_107253_(0.522F, 0.408F, 0.082F);
        return $$8;
    }

    public static TextureSheetParticle createDripstoneWaterHangParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
        DripParticle $$8 = new DripParticle.DripHangParticle(clientLevel1, double2, double3, double4, Fluids.WATER, ParticleTypes.FALLING_DRIPSTONE_WATER);
        $$8.m_107253_(0.2F, 0.3F, 1.0F);
        return $$8;
    }

    public static TextureSheetParticle createDripstoneWaterFallParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
        DripParticle $$8 = new DripParticle.DripstoneFallAndLandParticle(clientLevel1, double2, double3, double4, Fluids.WATER, ParticleTypes.SPLASH);
        $$8.m_107253_(0.2F, 0.3F, 1.0F);
        return $$8;
    }

    public static TextureSheetParticle createDripstoneLavaHangParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
        return new DripParticle.CoolingDripHangParticle(clientLevel1, double2, double3, double4, Fluids.LAVA, ParticleTypes.FALLING_DRIPSTONE_LAVA);
    }

    public static TextureSheetParticle createDripstoneLavaFallParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
        DripParticle $$8 = new DripParticle.DripstoneFallAndLandParticle(clientLevel1, double2, double3, double4, Fluids.LAVA, ParticleTypes.LANDING_LAVA);
        $$8.m_107253_(1.0F, 0.2857143F, 0.083333336F);
        return $$8;
    }

    public static TextureSheetParticle createNectarFallParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
        DripParticle $$8 = new DripParticle.FallingParticle(clientLevel1, double2, double3, double4, Fluids.EMPTY);
        $$8.f_107225_ = (int) (16.0 / (Math.random() * 0.8 + 0.2));
        $$8.f_107226_ = 0.007F;
        $$8.m_107253_(0.92F, 0.782F, 0.72F);
        return $$8;
    }

    public static TextureSheetParticle createSporeBlossomFallParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
        int $$8 = (int) (64.0F / Mth.randomBetween(clientLevel1.m_213780_(), 0.1F, 0.9F));
        DripParticle $$9 = new DripParticle.FallingParticle(clientLevel1, double2, double3, double4, Fluids.EMPTY, $$8);
        $$9.f_107226_ = 0.005F;
        $$9.m_107253_(0.32F, 0.5F, 0.22F);
        return $$9;
    }

    public static TextureSheetParticle createObsidianTearHangParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
        DripParticle.DripHangParticle $$8 = new DripParticle.DripHangParticle(clientLevel1, double2, double3, double4, Fluids.EMPTY, ParticleTypes.FALLING_OBSIDIAN_TEAR);
        $$8.f_106048_ = true;
        $$8.f_107226_ *= 0.01F;
        $$8.f_107225_ = 100;
        $$8.m_107253_(0.51171875F, 0.03125F, 0.890625F);
        return $$8;
    }

    public static TextureSheetParticle createObsidianTearFallParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
        DripParticle $$8 = new DripParticle.FallAndLandParticle(clientLevel1, double2, double3, double4, Fluids.EMPTY, ParticleTypes.LANDING_OBSIDIAN_TEAR);
        $$8.isGlowing = true;
        $$8.f_107226_ = 0.01F;
        $$8.m_107253_(0.51171875F, 0.03125F, 0.890625F);
        return $$8;
    }

    public static TextureSheetParticle createObsidianTearLandParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
        DripParticle $$8 = new DripParticle.DripLandParticle(clientLevel1, double2, double3, double4, Fluids.EMPTY);
        $$8.isGlowing = true;
        $$8.f_107225_ = (int) (28.0 / (Math.random() * 0.8 + 0.2));
        $$8.m_107253_(0.51171875F, 0.03125F, 0.890625F);
        return $$8;
    }

    static class CoolingDripHangParticle extends DripParticle.DripHangParticle {

        CoolingDripHangParticle(ClientLevel clientLevel0, double double1, double double2, double double3, Fluid fluid4, ParticleOptions particleOptions5) {
            super(clientLevel0, double1, double2, double3, fluid4, particleOptions5);
        }

        @Override
        protected void preMoveUpdate() {
            this.f_107227_ = 1.0F;
            this.f_107228_ = 16.0F / (float) (40 - this.f_107225_ + 16);
            this.f_107229_ = 4.0F / (float) (40 - this.f_107225_ + 8);
            super.preMoveUpdate();
        }
    }

    static class DripHangParticle extends DripParticle {

        private final ParticleOptions fallingParticle;

        DripHangParticle(ClientLevel clientLevel0, double double1, double double2, double double3, Fluid fluid4, ParticleOptions particleOptions5) {
            super(clientLevel0, double1, double2, double3, fluid4);
            this.fallingParticle = particleOptions5;
            this.f_107226_ *= 0.02F;
            this.f_107225_ = 40;
        }

        @Override
        protected void preMoveUpdate() {
            if (this.f_107225_-- <= 0) {
                this.m_107274_();
                this.f_107208_.addParticle(this.fallingParticle, this.f_107212_, this.f_107213_, this.f_107214_, this.f_107215_, this.f_107216_, this.f_107217_);
            }
        }

        @Override
        protected void postMoveUpdate() {
            this.f_107215_ *= 0.02;
            this.f_107216_ *= 0.02;
            this.f_107217_ *= 0.02;
        }
    }

    static class DripLandParticle extends DripParticle {

        DripLandParticle(ClientLevel clientLevel0, double double1, double double2, double double3, Fluid fluid4) {
            super(clientLevel0, double1, double2, double3, fluid4);
            this.f_107225_ = (int) (16.0 / (Math.random() * 0.8 + 0.2));
        }
    }

    static class DripstoneFallAndLandParticle extends DripParticle.FallAndLandParticle {

        DripstoneFallAndLandParticle(ClientLevel clientLevel0, double double1, double double2, double double3, Fluid fluid4, ParticleOptions particleOptions5) {
            super(clientLevel0, double1, double2, double3, fluid4, particleOptions5);
        }

        @Override
        protected void postMoveUpdate() {
            if (this.f_107218_) {
                this.m_107274_();
                this.f_107208_.addParticle(this.f_106114_, this.f_107212_, this.f_107213_, this.f_107214_, 0.0, 0.0, 0.0);
                SoundEvent $$0 = this.m_171928_() == Fluids.LAVA ? SoundEvents.POINTED_DRIPSTONE_DRIP_LAVA : SoundEvents.POINTED_DRIPSTONE_DRIP_WATER;
                float $$1 = Mth.randomBetween(this.f_107223_, 0.3F, 1.0F);
                this.f_107208_.playLocalSound(this.f_107212_, this.f_107213_, this.f_107214_, $$0, SoundSource.BLOCKS, $$1, 1.0F, false);
            }
        }
    }

    static class FallAndLandParticle extends DripParticle.FallingParticle {

        protected final ParticleOptions landParticle;

        FallAndLandParticle(ClientLevel clientLevel0, double double1, double double2, double double3, Fluid fluid4, ParticleOptions particleOptions5) {
            super(clientLevel0, double1, double2, double3, fluid4);
            this.landParticle = particleOptions5;
        }

        @Override
        protected void postMoveUpdate() {
            if (this.f_107218_) {
                this.m_107274_();
                this.f_107208_.addParticle(this.landParticle, this.f_107212_, this.f_107213_, this.f_107214_, 0.0, 0.0, 0.0);
            }
        }
    }

    static class FallingParticle extends DripParticle {

        FallingParticle(ClientLevel clientLevel0, double double1, double double2, double double3, Fluid fluid4) {
            this(clientLevel0, double1, double2, double3, fluid4, (int) (64.0 / (Math.random() * 0.8 + 0.2)));
        }

        FallingParticle(ClientLevel clientLevel0, double double1, double double2, double double3, Fluid fluid4, int int5) {
            super(clientLevel0, double1, double2, double3, fluid4);
            this.f_107225_ = int5;
        }

        @Override
        protected void postMoveUpdate() {
            if (this.f_107218_) {
                this.m_107274_();
            }
        }
    }

    static class HoneyFallAndLandParticle extends DripParticle.FallAndLandParticle {

        HoneyFallAndLandParticle(ClientLevel clientLevel0, double double1, double double2, double double3, Fluid fluid4, ParticleOptions particleOptions5) {
            super(clientLevel0, double1, double2, double3, fluid4, particleOptions5);
        }

        @Override
        protected void postMoveUpdate() {
            if (this.f_107218_) {
                this.m_107274_();
                this.f_107208_.addParticle(this.f_106114_, this.f_107212_, this.f_107213_, this.f_107214_, 0.0, 0.0, 0.0);
                float $$0 = Mth.randomBetween(this.f_107223_, 0.3F, 1.0F);
                this.f_107208_.playLocalSound(this.f_107212_, this.f_107213_, this.f_107214_, SoundEvents.BEEHIVE_DRIP, SoundSource.BLOCKS, $$0, 1.0F, false);
            }
        }
    }
}