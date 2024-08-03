package com.mna.effects.particles;

import com.mna.ManaAndArtifice;
import com.mna.api.affinity.Affinity;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.capabilities.entity.MAPFX;
import com.mna.capabilities.entity.MAPFXProvider;
import com.mna.entities.boss.DemonLord;
import com.mna.particles.types.movers.ParticleOrbitMover;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EffectParticleSpawner {

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        Level world = entity.m_9236_();
        entity.getCapability(MAPFXProvider.MAPFX).ifPresent(pfx -> {
            pfx.requestSync(entity);
            if (pfx.getFlag(entity, MAPFX.Flag.FIRE_SHIELD) || pfx.getFlag(entity, MAPFX.Flag.LIVING_BOMB)) {
                int amount = (int) (entity.m_20206_() * 5.0F);
                MAParticleType type = new MAParticleType(ParticleInit.FLAME_ORBIT.get());
                if (entity instanceof DemonLord) {
                    type.setColor(Affinity.HELLFIRE.getColor()[0], Affinity.HELLFIRE.getColor()[1], Affinity.HELLFIRE.getColor()[2]);
                    type.setScale(0.075F);
                }
                for (int i = 0; i < amount; i++) {
                    world.addParticle(type, entity.m_20185_(), entity.m_20186_() + (double) (entity.m_20206_() / 3.0F * 2.0F) * Math.random(), entity.m_20189_(), 0.1F, 0.01F + Math.random() * 0.02F, 1.0 + Math.random());
                }
            }
            if (pfx.getFlag(entity, MAPFX.Flag.MANA_SHIELD)) {
                for (int i = 0; i < 3; i++) {
                    double angle = Math.random() * 360.0 / 180.0 * Math.PI;
                    Vec3 pos = new Vec3(entity.m_20185_() + Math.cos(angle), entity.m_20186_() + 1.9 + Math.random() * 0.2, entity.m_20189_() + Math.sin(angle));
                    world.addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_GRAVITY.get()), pos.x, pos.y, pos.z, 0.0, 0.0, 0.0);
                }
            }
            if (pfx.getFlag(entity, MAPFX.Flag.BRIARTHORN_BARRIER)) {
                for (int i = 0; i < 3; i++) {
                    double angle = Math.random() * 360.0 / 180.0 * Math.PI;
                    Vec3 pos = new Vec3(entity.m_20185_() + Math.cos(angle), entity.m_20186_() + 0.9 + Math.random() * 0.2, entity.m_20189_() + Math.sin(angle));
                    world.addParticle(new MAParticleType(ParticleInit.EARTH.get()), pos.x, pos.y, pos.z, 0.4392157F, 0.22352941F, 0.078431375F);
                }
            }
            if (pfx.getFlag(entity, MAPFX.Flag.MIST_FORM) && (entity != ManaAndArtifice.instance.proxy.getClientPlayer() || Minecraft.getInstance().gameRenderer.getMainCamera().isDetached())) {
                for (int i = 0; i < 3; i++) {
                    world.addParticle(new MAParticleType(ParticleInit.MIST.get()), entity.m_20185_(), entity.m_20186_() + (double) (entity.m_20206_() / 2.0F), entity.m_20189_(), (Math.random() - 0.5) * 0.1F, (Math.random() - 0.5) * 0.1F, (Math.random() - 0.5) * 0.1F);
                }
            }
            if (pfx.getFlag(entity, MAPFX.Flag.AURA_OF_FROST) && !pfx.getFlag(entity, MAPFX.Flag.MIST_FORM)) {
                for (int i = 0; i < 3; i++) {
                    world.addParticle(new MAParticleType(ParticleInit.FROST.get()).setMover(new ParticleOrbitMover(entity.m_20185_(), entity.m_20186_() + 0.05F, entity.m_20189_(), 0.05F, 0.01F, 1.0 + Math.random() * 2.0)), entity.m_20185_(), entity.m_20186_() + 0.05F, entity.m_20189_(), 0.0, Math.random() * 0.05F, 0.0);
                }
                for (int i = 0; i < 3; i++) {
                    world.addParticle(new MAParticleType(ParticleInit.FROST.get()), entity.m_20185_() - 3.0 + Math.random() * 6.0, entity.m_20186_() + 0.05F, entity.m_20189_() - 3.0 + Math.random() * 6.0, 0.0, Math.random() * 0.01F, 0.0);
                }
            }
            if (pfx.getFlag(entity, MAPFX.Flag.CIRCLE_OF_POWER)) {
                world.addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_VELOCITY.get()).setMaxAge(5), entity.m_20185_() - 0.5 + Math.random(), entity.m_20186_() + Math.random() / 2.0, entity.m_20189_() - 0.5 + Math.random(), 0.0, 0.17F, 0.0);
            }
            if (pfx.getFlag(entity, MAPFX.Flag.WIND_WALL)) {
                for (int i = 0; i < 10; i++) {
                    world.addParticle(new MAParticleType(ParticleInit.AIR_ORBIT.get()).setScale(0.05F).setColor(10, 10, 10), entity.m_20185_(), entity.m_20186_(), entity.m_20189_(), 0.1F, 0.04F + Math.random() * 0.04F, 1.2F);
                }
            }
            if (pfx.getFlag(entity, MAPFX.Flag.SOAKED) && Math.random() < 0.5 && !entity.m_20072_()) {
                world.addParticle(ParticleTypes.FALLING_WATER, entity.m_20185_() - 0.5 + Math.random(), entity.m_20186_() + Math.random() * (double) entity.m_20206_(), entity.m_20189_() - 0.5 + Math.random(), 0.0, 0.0, 0.0);
            }
        });
    }

    @SubscribeEvent
    public static void onLivingRenderPre(RenderLivingEvent.Pre<?, ?> event) {
        event.getEntity().getCapability(MAPFXProvider.MAPFX).ifPresent(pfx -> {
            if (pfx.getFlag(event.getEntity(), MAPFX.Flag.CANCEL_RENDER)) {
                event.setCanceled(true);
            }
        });
    }
}