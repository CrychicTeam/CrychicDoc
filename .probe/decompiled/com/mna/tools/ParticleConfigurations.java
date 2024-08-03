package com.mna.tools;

import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.particles.types.movers.ParticleBezierMover;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ParticleConfigurations {

    public static void ArcaneParticleBurst(Level level, Vec3 srcPoint) {
        Random rnd = new Random();
        for (int i = 0; i < 150; i++) {
            level.addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_GRAVITY.get()), srcPoint.x(), srcPoint.y(), srcPoint.z(), -0.5 + Math.random(), 0.01, -0.5 + Math.random());
        }
        for (int i = 0; i < 50; i++) {
            Vec3 lightPoint = new Vec3(srcPoint.x() - 0.2F + (double) (rnd.nextFloat() * 0.4F), srcPoint.y() - 0.3F, srcPoint.z() - 0.2F + (double) (rnd.nextFloat() * 0.4F));
            level.addParticle(new MAParticleType(ParticleInit.LIGHT_VELOCITY.get()), lightPoint.x(), lightPoint.y(), lightPoint.z(), 0.0, 0.01, 0.0);
        }
    }

    public static void ItemPullParticle(Level level, Vec3 origin, CompoundTag fromServer) {
        Vec3 destination = Vec3.upFromBottomCenterOf(BlockPos.of(fromServer.getLong("destination")), 2.5);
        Vec3 delta = destination.subtract(origin);
        for (int i = 0; i < 10; i++) {
            Vec3 control_a = origin.add(delta.scale(0.25 + Math.random() * 0.2)).add(0.0, 2.0, 0.0);
            Vec3 control_b = origin.add(delta.scale(0.6 + Math.random() * 0.2)).add(0.0, 2.0, 0.0);
            level.addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_VELOCITY.get()).setMover(new ParticleBezierMover(origin, destination, control_a, control_b)).setMaxAge(10 + (int) (Math.random() * 10.0)).setScale(0.1F), origin.x(), origin.y(), origin.z(), 0.0, 0.0, 0.0);
        }
    }
}