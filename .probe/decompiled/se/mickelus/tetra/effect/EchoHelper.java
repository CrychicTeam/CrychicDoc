package se.mickelus.tetra.effect;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import se.mickelus.tetra.ServerScheduler;

@ParametersAreNonnullByDefault
public class EchoHelper {

    public static void echo(Player attacker, int delay, Runnable callback) {
        Vec3 origin = attacker.m_20182_();
        RandomSource rand = attacker.m_217043_();
        for (int i = 0; i < delay / 10; i++) {
            ServerScheduler.schedule(i * 10, () -> ((ServerLevel) attacker.m_9236_()).sendParticles(ParticleTypes.WITCH, origin.x + (double) attacker.m_20205_() * (rand.nextGaussian() - 0.5) * 0.5, origin.y + (double) (attacker.m_20206_() * rand.nextFloat()), origin.z + (double) attacker.m_20205_() * (rand.nextGaussian() - 0.5) * 0.5, 3, (double) rand.nextFloat() * 0.2, 0.2 + (double) rand.nextFloat() * 0.6, (double) rand.nextFloat() * 0.2, 0.0));
        }
        ServerScheduler.schedule(delay, () -> {
            Vec3 currentPos = attacker.m_20182_();
            attacker.m_6034_(origin.x, origin.y, origin.z);
            callback.run();
            attacker.m_6034_(currentPos.x, currentPos.y, currentPos.z);
        });
    }
}