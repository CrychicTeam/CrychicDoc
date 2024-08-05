package com.mna.particles;

import com.mna.ManaAndArtifice;
import com.mna.api.particles.ParticleInit;
import com.mna.particles.bolt.FXLightningBolt;
import com.mna.particles.bolt.FXWraithBolt;
import com.mna.particles.trail.FXTrail;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ParticleClientInit {

    @SubscribeEvent
    public static void onRegisterParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleInit.SPARKLE_BEZIER_POINT.get(), FXSparkle.FXSparkleBezierPointFactory::new);
        event.registerSpriteSet(ParticleInit.SPARKLE_GRAVITY.get(), FXSparkle.FXSparkleGravityFactory::new);
        event.registerSpriteSet(ParticleInit.SPARKLE_LERP_POINT.get(), FXSparkle.FXSparkleLerpPointFactory::new);
        event.registerSpriteSet(ParticleInit.SPARKLE_RANDOM.get(), FXSparkle.FXSparkleRandomFactory::new);
        event.registerSpriteSet(ParticleInit.SPARKLE_VELOCITY.get(), FXSparkle.FXSparkleVelocityFactory::new);
        event.registerSpriteSet(ParticleInit.SPARKLE_STATIONARY.get(), FXSparkle.FXSparkleStationaryFactory::new);
        event.registerSpriteSet(ParticleInit.BLUE_SPARKLE_STATIONARY.get(), FXBlueSparkle.FXBlueSparkleStationaryFactory::new);
        event.registerSpriteSet(ParticleInit.BLUE_SPARKLE_GRAVITY.get(), FXBlueSparkle.FXBlueSparkleGravityFactory::new);
        event.registerSpriteSet(ParticleInit.BLUE_SPARKLE_VELOCITY.get(), FXBlueSparkle.FXBlueSparkleVelocityFactory::new);
        event.registerSpriteSet(ParticleInit.BLUE_SPARKLE_SPHERE_ORBIT.get(), FXBlueSparkle.FXBlueSparkleSphereOrbitFactory::new);
        event.registerSpriteSet(ParticleInit.BLUE_SPARKLE_ORBIT.get(), FXBlueSparkle.FXBlueSparkleOrbitFactory::new);
        event.registerSpriteSet(ParticleInit.LIGHT_VELOCITY.get(), FXLight.FXLightVelocity::new);
        event.registerSpriteSet(ParticleInit.BLUE_FLAME.get(), FXBlueFlame.FXBlueFlameRandomFactory::new);
        event.registerSpriteSet(ParticleInit.FLAME.get(), FXFlame.FXFlameFactory::new);
        event.registerSpriteSet(ParticleInit.FLAME_LERP.get(), FXFlame.FXFlameLerpFactory::new);
        event.registerSpriteSet(ParticleInit.FLAME_ORBIT.get(), FXFlame.FXFlameOrbitFactory::new);
        event.registerSpriteSet(ParticleInit.HELLFIRE.get(), FXFlame.FXHellfireFactory::new);
        event.registerSpriteSet(ParticleInit.FROST.get(), FXFrost.FXFrostFactory::new);
        event.registerSpriteSet(ParticleInit.FROST_LERP.get(), FXFrost.FXFrostLerpFactory::new);
        event.registerSpriteSet(ParticleInit.MIST.get(), FXFrost.FXMistFactory::new);
        event.registerSpriteSet(ParticleInit.WATER.get(), FXWater.FXWaterFactory::new);
        event.registerSpriteSet(ParticleInit.WATER_LERP.get(), FXWater.FXWaterLerpFactory::new);
        event.registerSpriteSet(ParticleInit.ENDER.get(), FXEnder.FXEnderBezier::new);
        event.registerSpriteSet(ParticleInit.ENDER_VELOCITY.get(), FXEnder.FXEnderVelocity::new);
        event.registerSpriteSet(ParticleInit.ARCANE.get(), FXArcane.FXArcaneFactory::new);
        event.registerSpriteSet(ParticleInit.ARCANE_LERP.get(), FXArcane.FXArcaneStretchLerp::new);
        event.registerSpriteSet(ParticleInit.ARCANE_RANDOM.get(), FXArcane.FXArcaneRandomFactory::new);
        event.registerSpriteSet(ParticleInit.ARCANE_MAGELIGHT.get(), FXArcane.FXArcaneMagelight::new);
        event.registerSpriteSet(ParticleInit.AIR_VELOCITY.get(), FXAir.FXAirVelocity::new);
        event.registerSpriteSet(ParticleInit.AIR_ORBIT.get(), FXAir.FXAirOrbit::new);
        event.registerSpriteSet(ParticleInit.AIR_LERP.get(), FXAir.FXAirLerp::new);
        event.registerSpriteSet(ParticleInit.EARTH.get(), FXEarth.FXEarthFactory::new);
        event.registerSpriteSet(ParticleInit.DUST.get(), FXDust.FXDustFactory::new);
        event.registerSpriteSet(ParticleInit.DUST_LERP.get(), FXDust.FXDustLerpFactory::new);
        event.registerSpriteSet(ParticleInit.GLOW.get(), FXGlow.FXGlowFactory::new);
        event.registerSpriteSet(ParticleInit.SOUL.get(), FXSoul.FXSoulFactory::new);
        event.registerSpriteSet(ParticleInit.BONE.get(), FXBone.FXBoneFactory::new);
        event.registerSpriteSet(ParticleInit.BONE_ORBIT.get(), FXBone.FXBoneOrbitFactory::new);
        event.registerSpriteSet(ParticleInit.DRIP.get(), FXDrip.FXDripFactory::new);
        event.registerSpriteSet(ParticleInit.ITEM.get(), FXItem.Factory::new);
        event.registerSpriteSet(ParticleInit.HEART.get(), FXHeart.FXHeartFactory::new);
        event.registerSpriteSet(ParticleInit.ENCHANT.get(), FXEnchant.FXEnchantFactory::new);
        event.registerSpriteSet(ParticleInit.COZY_SMOKE.get(), FXCozySmoke.FXCozySmokeFactory::new);
        event.registerSpriteSet(ParticleInit.LIGHTNING_BOLT.get(), FXLightningBolt.FXLightningBoltFactory::new);
        event.registerSpriteSet(ParticleInit.WRAITH_BOLT.get(), FXWraithBolt.FXWraithBoltFactory::new);
        event.registerSpriteSet(ParticleInit.TRAIL.get(), FXTrail.FXTrailFollowEntityFactory::new);
        event.registerSpriteSet(ParticleInit.TRAIL_VELOCITY.get(), FXTrail.FXTrailVelocityFactory::new);
        event.registerSpriteSet(ParticleInit.TRAIL_ORBIT.get(), FXTrail.FXTrailOrbitFactory::new);
        event.registerSpriteSet(ParticleInit.TRAIL_SPHERE_ORBIT.get(), FXTrail.FXTrailSphereOrbitFactory::new);
        event.registerSpriteSet(ParticleInit.TRAIL_BEZIER.get(), FXTrail.FXTrailBezierFactory::new);
        ManaAndArtifice.LOGGER.info("M&A -> Particle Factories Registered");
    }
}