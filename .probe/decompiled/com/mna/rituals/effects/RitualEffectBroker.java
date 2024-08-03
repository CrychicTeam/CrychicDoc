package com.mna.rituals.effects;

import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.api.sound.SFX;
import com.mna.entities.faction.Broker;
import com.mojang.math.Axis;
import java.util.HashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class RitualEffectBroker extends RitualEffect {

    HashMap<BlockPos, Vector3f> effectWindDirections = new HashMap();

    public RitualEffectBroker(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        if (context.getLevel().isClientSide()) {
            return true;
        } else {
            Broker entity = new Broker(context.getLevel());
            entity.m_6034_((double) context.getCenter().m_123341_() + 0.5, (double) context.getCenter().m_123342_(), (double) context.getCenter().m_123343_() + 0.5);
            entity.m_35891_(12000);
            context.getLevel().m_7967_(entity);
            context.getLevel().playSound(null, (double) context.getCenter().m_123341_(), (double) context.getCenter().m_123342_(), (double) context.getCenter().m_123343_(), SFX.Event.Ritual.IRON_BELL, SoundSource.AMBIENT, 1.0F, 1.0F);
            this.effectWindDirections.remove(context.getCenter());
            return true;
        }
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 20;
    }

    @Override
    public boolean spawnRitualParticles(IRitualContext context) {
        RandomSource rand = context.getLevel().getRandom();
        Vector3f wind = (Vector3f) this.effectWindDirections.getOrDefault(context.getCenter(), new Vector3f(rand.nextFloat() - 0.5F, 0.0F, rand.nextFloat() - 0.5F));
        wind.rotate(Axis.YP.rotationDegrees((float) (rand.nextInt(4) - 2)));
        for (int i = 0; i < 5; i++) {
            BlockPos block = context.getCenter().offset(-3 + rand.nextInt(7), 0, -3 + rand.nextInt(7));
            Vec3 pos = new Vec3((double) block.m_123341_() + rand.nextDouble(), (double) ((float) block.m_123342_() + rand.nextFloat()), (double) block.m_123343_() + rand.nextDouble());
            context.getLevel().addParticle(new MAParticleType(ParticleInit.EARTH.get()), pos.x(), pos.y(), pos.z(), 0.04F, 0.32F, 0.03F);
        }
        for (int i = 0; i < 75; i++) {
            BlockPos block = context.getCenter().offset(-3 + rand.nextInt(7), 0, -3 + rand.nextInt(7));
            Vec3 pos = new Vec3((double) block.m_123341_() + rand.nextDouble(), (double) block.m_123342_(), (double) block.m_123343_() + rand.nextDouble());
            context.getLevel().addParticle(new MAParticleType(ParticleInit.FLAME.get()), pos.x(), pos.y(), pos.z(), (double) (wind.x() * 0.25F), (double) (wind.y() * 0.25F), (double) (wind.z() * 0.25F));
        }
        if (rand.nextFloat() < 0.1F) {
            BlockPos block = context.getCenter().offset(-3 + rand.nextInt(7), 2, -3 + rand.nextInt(7));
            Vec3 pos = new Vec3((double) block.m_123341_() + rand.nextDouble(), (double) ((float) block.m_123342_() + rand.nextFloat() * 2.0F), (double) block.m_123343_() + rand.nextDouble());
            for (int i = 0; i < 50; i++) {
                context.getLevel().addParticle(new MAParticleType(ParticleInit.ARCANE_RANDOM.get()), pos.x(), pos.y(), pos.z(), 0.05F, 0.05F, 0.05F);
            }
        }
        this.effectWindDirections.put(context.getCenter(), wind);
        return true;
    }
}