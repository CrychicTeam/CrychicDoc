package net.mehvahdjukaar.supplementaries.client.particles;

import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;

public class RotationTrailParticle extends SimpleAnimatedParticle {

    public static final float SPEED = 11.0F;

    public static final int LIFE = 8;

    private final Vec3 axis;

    private final Vec3 origin;

    private final double radius;

    private float angularVelocity;

    private float currentAngle;

    private float fadeR;

    private float fadeG;

    private float fadeB;

    private static final float AL = 0.6F;

    private RotationTrailParticle(ClientLevel clientWorld, double x, double y, double z, Vec3 center, Vec3 rotationAxis, int ccw, double radius, double angle, SpriteSet sprite) {
        super(clientWorld, x, y, z, sprite, -5.0E-4F);
        this.origin = center;
        this.axis = rotationAxis;
        this.angularVelocity = (float) ccw * 11.0F * (float) (Math.PI / 180.0);
        this.radius = radius;
        this.currentAngle = (float) angle;
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
        this.m_107250_(0.01F, 0.01F);
        this.f_107663_ *= 0.625F;
        this.f_107225_ = 8;
        this.m_107657_((Integer) ClientConfigs.Particles.TURN_INITIAL_COLOR.get());
        this.setFadeColor((Integer) ClientConfigs.Particles.TURN_FADE_COLOR.get());
        this.m_108339_(sprite);
        this.f_107230_ = 0.6F;
        this.f_107219_ = false;
    }

    @Override
    public void tick() {
        super.tick();
        this.m_107271_(0.6F - (float) this.f_107224_ / (float) this.f_107225_ * 0.6F * 0.7F);
    }

    @Override
    public void setFadeColor(int pRgb) {
        this.fadeR = (float) ((pRgb & 0xFF0000) >> 16) / 255.0F;
        this.fadeG = (float) ((pRgb & 0xFF00) >> 8) / 255.0F;
        this.fadeB = (float) (pRgb & 0xFF) / 255.0F;
        super.setFadeColor(pRgb);
    }

    @Override
    public void move(double x, double y, double z) {
        this.f_107227_ = this.f_107227_ + (this.fadeR - this.f_107227_) * 0.1F;
        this.f_107228_ = this.f_107228_ + (this.fadeG - this.f_107228_) * 0.1F;
        this.f_107229_ = this.f_107229_ + (this.fadeB - this.f_107229_) * 0.1F;
        this.currentAngle = this.currentAngle + this.angularVelocity;
        Vec3 rot = new Vec3(this.radius, 0.0, 0.0).yRot(this.currentAngle);
        Vec3 newPos = MthUtils.changeBasisN(this.axis, rot).add(this.origin);
        this.angularVelocity = (float) ((double) this.angularVelocity * 0.75);
        super.m_6257_(newPos.x - this.f_107212_, newPos.y - this.f_107213_, newPos.z - this.f_107214_);
    }

    public static float increment(float age, int step) {
        return 11.0F * (float) step * (1.0F - ((float) step + 2.0F * (age - 1.0F)) / 16.0F);
    }

    @Override
    public int getLightColor(float pPartialTick) {
        BlockPos blockpos = BlockPos.containing(this.f_107212_, this.f_107213_, this.f_107214_);
        return this.f_107208_.m_46805_(blockpos) ? LevelRenderer.getLightColor(this.f_107208_, blockpos) : 0;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Factory(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel world, double centerX, double centerY, double centerZ, double direction, double radius, double initialAngle) {
            Vec3 center = new Vec3(centerX, centerY, centerZ);
            int ccw = 1;
            if (direction < 0.0) {
                ccw = -1;
                direction = -direction;
            }
            Direction dir = Direction.from3DDataValue((int) direction);
            float radAngle = (float) (initialAngle * (float) (Math.PI / 180.0));
            Vec3 axis = MthUtils.V3itoV3(dir.getNormal());
            Vec3 rot = new Vec3(radius, 0.0, 0.0).yRot(radAngle);
            Vec3 newPos = MthUtils.changeBasisN(axis, rot).add(center);
            return new RotationTrailParticle(world, newPos.x, newPos.y, newPos.z, center, axis, ccw, radius, (double) radAngle, this.sprites);
        }
    }
}