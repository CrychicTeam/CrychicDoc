package team.lodestar.lodestone.systems.particle.screen;

import net.minecraft.client.multiplayer.ClientLevel;
import team.lodestar.lodestone.systems.particle.screen.base.ScreenParticle;

public class ScreenParticleType<T extends ScreenParticleOptions> {

    public ScreenParticleType.ParticleProvider<T> provider;

    public interface ParticleProvider<T extends ScreenParticleOptions> {

        ScreenParticle createParticle(ClientLevel var1, T var2, double var3, double var5, double var7, double var9);
    }
}