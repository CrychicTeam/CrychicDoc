package com.simibubi.create.foundation.particle;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class AirParticle extends SimpleAnimatedParticle {

    private float originX;

    private float originY;

    private float originZ;

    private float targetX;

    private float targetY;

    private float targetZ;

    private float drag;

    private float twirlRadius;

    private float twirlAngleOffset;

    private Direction.Axis twirlAxis;

    protected AirParticle(ClientLevel world, AirParticleData data, double x, double y, double z, double dx, double dy, double dz, SpriteSet sprite) {
        super(world, x, y, z, sprite, world.f_46441_.nextFloat() * 0.5F);
        this.f_107663_ *= 0.75F;
        this.f_107219_ = false;
        this.m_107264_(x, y, z);
        this.originX = (float) (this.f_107209_ = x);
        this.originY = (float) (this.f_107210_ = y);
        this.originZ = (float) (this.f_107211_ = z);
        this.targetX = (float) (x + dx);
        this.targetY = (float) (y + dy);
        this.targetZ = (float) (z + dz);
        this.drag = data.drag;
        this.twirlRadius = Create.RANDOM.nextFloat() / 6.0F;
        this.twirlAngleOffset = Create.RANDOM.nextFloat() * 360.0F;
        this.twirlAxis = Create.RANDOM.nextBoolean() ? Direction.Axis.X : Direction.Axis.Z;
        double length = new Vec3(dx, dy, dz).length();
        this.f_107225_ = Math.min((int) (length / (double) data.speed), 60);
        this.selectSprite(7);
        this.m_107271_(0.25F);
        if (length == 0.0) {
            this.m_107274_();
            this.m_107271_(0.0F);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            float progress = (float) Math.pow((double) ((float) this.f_107224_ / (float) this.f_107225_), (double) this.drag);
            float angle = (progress * 2.0F * 360.0F + this.twirlAngleOffset) % 360.0F;
            Vec3 twirl = VecHelper.rotate(new Vec3(0.0, (double) this.twirlRadius, 0.0), (double) angle, this.twirlAxis);
            float x = (float) ((double) Mth.lerp(progress, this.originX, this.targetX) + twirl.x);
            float y = (float) ((double) Mth.lerp(progress, this.originY, this.targetY) + twirl.y);
            float z = (float) ((double) Mth.lerp(progress, this.originZ, this.targetZ) + twirl.z);
            this.f_107215_ = (double) x - this.f_107212_;
            this.f_107216_ = (double) y - this.f_107213_;
            this.f_107217_ = (double) z - this.f_107214_;
            this.m_108339_(this.f_107644_);
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
        }
    }

    @Override
    public int getLightColor(float partialTick) {
        BlockPos blockpos = BlockPos.containing(this.f_107212_, this.f_107213_, this.f_107214_);
        return this.f_107208_.m_46749_(blockpos) ? LevelRenderer.getLightColor(this.f_107208_, blockpos) : 0;
    }

    private void selectSprite(int index) {
        this.m_108337_(this.f_107644_.get(index, 8));
    }

    public static class Factory implements ParticleProvider<AirParticleData> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet animatedSprite) {
            this.spriteSet = animatedSprite;
        }

        public Particle createParticle(AirParticleData data, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new AirParticle(worldIn, data, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}