package team.lodestar.lodestone.systems.particle.world.type;

import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;

public class LodestoneWorldParticleType extends AbstractLodestoneParticleType<WorldParticleOptions> {

    public static class Factory implements ParticleProvider<WorldParticleOptions> {

        private final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Nullable
        public Particle createParticle(WorldParticleOptions data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
            return new LodestoneWorldParticle(world, data, (ParticleEngine.MutableSpriteSet) this.sprite, x, y, z, mx, my, mz);
        }
    }
}