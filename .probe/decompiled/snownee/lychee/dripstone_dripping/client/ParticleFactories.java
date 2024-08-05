package snownee.lychee.dripstone_dripping.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import snownee.lychee.dripstone_dripping.DripParticleHandler;
import snownee.lychee.dripstone_dripping.DripstoneRecipeMod;

public class ParticleFactories {

    private static void postParticle(DripParticle particle, BlockState blockState, ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        DripParticleHandler handler = DripstoneRecipeMod.getParticleHandler(level, blockState);
        if (handler != null) {
            int color = handler.getColor(level, blockState, x, y, z, velocityX, velocityY, velocityZ);
            float r = (float) (color >> 16 & 0xFF) / 255.0F;
            float g = (float) (color >> 8 & 0xFF) / 255.0F;
            float b = (float) (color & 0xFF) / 255.0F;
            particle.m_107253_(r, g, b);
            particle.isGlowing = handler.isGlowing(level, blockState);
        }
    }

    public static class Dripping implements ParticleProvider<BlockParticleOption> {

        protected final SpriteSet sprite;

        public Dripping(SpriteSet sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(BlockParticleOption defaultParticleType, ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            BlockParticleOption fallOption = new BlockParticleOption(DripstoneRecipeMod.DRIPSTONE_FALLING, defaultParticleType.getState());
            DripParticle particle = new DripParticle.DripHangParticle(level, x, y, z, Fluids.WATER, fallOption);
            particle.m_108335_(this.sprite);
            ParticleFactories.postParticle(particle, defaultParticleType.getState(), level, x, y, z, velocityX, velocityY, velocityZ);
            return particle;
        }
    }

    public static class Falling implements ParticleProvider<BlockParticleOption> {

        protected final SpriteSet sprite;

        public Falling(SpriteSet sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(BlockParticleOption defaultParticleType, ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            BlockParticleOption fallOption = new BlockParticleOption(DripstoneRecipeMod.DRIPSTONE_SPLASH, defaultParticleType.getState());
            DripParticle particle = new DripParticle.DripstoneFallAndLandParticle(level, x, y, z, Fluids.WATER, fallOption);
            particle.m_108335_(this.sprite);
            ParticleFactories.postParticle(particle, defaultParticleType.getState(), level, x, y, z, velocityX, velocityY, velocityZ);
            return particle;
        }
    }

    public static class Splash implements ParticleProvider<BlockParticleOption> {

        protected final SpriteSet sprite;

        public Splash(SpriteSet sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(BlockParticleOption defaultParticleType, ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            DripstoneSplashParticle particle = new DripstoneSplashParticle(level, x, y, z, velocityX, velocityY, velocityZ, Fluids.WATER);
            particle.m_108335_(this.sprite);
            ParticleFactories.postParticle(particle, defaultParticleType.getState(), level, x, y, z, velocityX, velocityY, velocityZ);
            return particle;
        }
    }
}