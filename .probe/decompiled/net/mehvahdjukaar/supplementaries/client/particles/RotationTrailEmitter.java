package net.mehvahdjukaar.supplementaries.client.particles;

import net.mehvahdjukaar.supplementaries.reg.ModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;

public class RotationTrailEmitter extends NoRenderParticle {

    private final double radius;

    private final int ccw;

    private final Direction axis;

    private int timeSinceStart;

    private RotationTrailEmitter(ClientLevel world, double x, double y, double z, Direction axis, double radius, double velocity) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.radius = radius;
        this.axis = axis;
        this.ccw = velocity > 0.0 ? -1 : 1;
    }

    @Override
    public void tick() {
        int maximumTime = 6;
        for (int i = 0; i < 4; i++) {
            this.f_107208_.addParticle((ParticleOptions) ModParticles.ROTATION_TRAIL.get(), this.f_107212_, this.f_107213_, this.f_107214_, (double) (this.axis.get3DDataValue() * this.ccw), this.radius, (double) ((float) (i * 90 + 45) + (float) (this.ccw * this.timeSinceStart) * 11.0F));
        }
        this.timeSinceStart++;
        if (this.timeSinceStart == maximumTime) {
            this.m_107274_();
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        public Factory(SpriteSet sprite) {
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel world, double centerX, double centerY, double centerZ, double direction, double radius, double angularVelocity) {
            Direction dir = Direction.from3DDataValue((int) direction);
            if (dir == Direction.DOWN) {
                angularVelocity *= -1.0;
                dir = dir.getOpposite();
            }
            return new RotationTrailEmitter(world, centerX, centerY, centerZ, dir, radius, angularVelocity);
        }
    }
}