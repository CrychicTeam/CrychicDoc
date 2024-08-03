package com.simibubi.create.content.kinetics.fan;

import com.simibubi.create.content.kinetics.fan.processing.AllFanProcessingTypes;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.foundation.utility.VecHelper;
import javax.annotation.Nonnull;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public class AirFlowParticle extends SimpleAnimatedParticle {

    private final IAirCurrentSource source;

    private final AirFlowParticle.Access access = new AirFlowParticle.Access();

    protected AirFlowParticle(ClientLevel world, IAirCurrentSource source, double x, double y, double z, SpriteSet sprite) {
        super(world, x, y, z, sprite, world.f_46441_.nextFloat() * 0.5F);
        this.source = source;
        this.f_107663_ *= 0.75F;
        this.f_107225_ = 40;
        this.f_107219_ = false;
        this.selectSprite(7);
        Vec3 offset = VecHelper.offsetRandomly(Vec3.ZERO, this.f_107223_, 0.25F);
        this.m_107264_(x + offset.x, y + offset.y, z + offset.z);
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        this.m_107657_(15658734);
        this.m_107271_(0.25F);
    }

    @Nonnull
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        if (this.source != null && !this.source.isSourceRemoved()) {
            this.f_107209_ = this.f_107212_;
            this.f_107210_ = this.f_107213_;
            this.f_107211_ = this.f_107214_;
            if (this.f_107224_++ >= this.f_107225_) {
                this.m_107274_();
            } else {
                AirCurrent airCurrent = this.source.getAirCurrent();
                if (airCurrent == null || !airCurrent.bounds.inflate(0.25).contains(this.f_107212_, this.f_107213_, this.f_107214_)) {
                    this.m_107274_();
                    return;
                }
                Vec3 directionVec = Vec3.atLowerCornerOf(airCurrent.direction.getNormal());
                Vec3 motion = directionVec.scale(0.125);
                if (!this.source.getAirCurrent().pushing) {
                    motion = motion.scale(-1.0);
                }
                double distance = new Vec3(this.f_107212_, this.f_107213_, this.f_107214_).subtract(VecHelper.getCenterOf(this.source.getAirCurrentPos())).multiply(directionVec).length() - 0.5;
                if (distance > (double) (airCurrent.maxDistance + 1.0F) || distance < -0.25) {
                    this.m_107274_();
                    return;
                }
                motion = motion.scale((double) airCurrent.maxDistance - (distance - 1.0)).scale(0.5);
                FanProcessingType type = this.getType(distance);
                if (type == AllFanProcessingTypes.NONE) {
                    this.m_107657_(15658734);
                    this.m_107271_(0.25F);
                    this.selectSprite((int) Mth.clamp(distance / (double) airCurrent.maxDistance * 8.0 + (double) this.f_107223_.nextInt(4), 0.0, 7.0));
                } else {
                    type.morphAirFlow(this.access, this.f_107223_);
                    this.selectSprite(this.f_107223_.nextInt(3));
                }
                this.f_107215_ = motion.x;
                this.f_107216_ = motion.y;
                this.f_107217_ = motion.z;
                if (this.f_107218_) {
                    this.f_107215_ *= 0.7;
                    this.f_107217_ *= 0.7;
                }
                this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            }
        } else {
            this.m_107274_();
        }
    }

    private FanProcessingType getType(double distance) {
        return (FanProcessingType) (this.source.getAirCurrent() == null ? AllFanProcessingTypes.NONE : this.source.getAirCurrent().getTypeAt((float) distance));
    }

    @Override
    public int getLightColor(float partialTick) {
        BlockPos blockpos = BlockPos.containing(this.f_107212_, this.f_107213_, this.f_107214_);
        return this.f_107208_.m_46749_(blockpos) ? LevelRenderer.getLightColor(this.f_107208_, blockpos) : 0;
    }

    private void selectSprite(int index) {
        this.m_108337_(this.f_107644_.get(index, 8));
    }

    private class Access implements FanProcessingType.AirFlowParticleAccess {

        @Override
        public void setColor(int color) {
            AirFlowParticle.this.m_107657_(color);
        }

        @Override
        public void setAlpha(float alpha) {
            AirFlowParticle.this.m_107271_(alpha);
        }

        @Override
        public void spawnExtraParticle(ParticleOptions options, float speedMultiplier) {
            AirFlowParticle.this.f_107208_.addParticle(options, AirFlowParticle.this.f_107212_, AirFlowParticle.this.f_107213_, AirFlowParticle.this.f_107214_, AirFlowParticle.this.f_107215_ * (double) speedMultiplier, AirFlowParticle.this.f_107216_ * (double) speedMultiplier, AirFlowParticle.this.f_107217_ * (double) speedMultiplier);
        }
    }

    public static class Factory implements ParticleProvider<AirFlowParticleData> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet animatedSprite) {
            this.spriteSet = animatedSprite;
        }

        public Particle createParticle(AirFlowParticleData data, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            BlockEntity be = worldIn.m_7702_(new BlockPos(data.posX, data.posY, data.posZ));
            if (!(be instanceof IAirCurrentSource)) {
                be = null;
            }
            return new AirFlowParticle(worldIn, (IAirCurrentSource) be, x, y, z, this.spriteSet);
        }
    }
}