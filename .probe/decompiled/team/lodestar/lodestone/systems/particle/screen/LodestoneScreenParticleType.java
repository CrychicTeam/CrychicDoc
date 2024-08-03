package team.lodestar.lodestone.systems.particle.screen;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.SpriteSet;
import team.lodestar.lodestone.systems.particle.screen.base.ScreenParticle;

public class LodestoneScreenParticleType extends ScreenParticleType<ScreenParticleOptions> {

    public static class Factory implements ScreenParticleType.ParticleProvider<ScreenParticleOptions> {

        public final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public ScreenParticle createParticle(ClientLevel pLevel, ScreenParticleOptions options, double x, double y, double pXSpeed, double pYSpeed) {
            return new GenericScreenParticle(pLevel, options, (ParticleEngine.MutableSpriteSet) this.sprite, x, y, pXSpeed, pYSpeed);
        }
    }
}