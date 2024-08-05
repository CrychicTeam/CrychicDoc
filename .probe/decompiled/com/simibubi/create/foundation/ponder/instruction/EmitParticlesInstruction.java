package com.simibubi.create.foundation.ponder.instruction;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.PonderWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.phys.Vec3;

public class EmitParticlesInstruction extends TickingInstruction {

    private Vec3 anchor;

    private EmitParticlesInstruction.Emitter emitter;

    private float runsPerTick;

    public EmitParticlesInstruction(Vec3 anchor, EmitParticlesInstruction.Emitter emitter, float runsPerTick, int ticks) {
        super(false, ticks);
        this.anchor = anchor;
        this.emitter = emitter;
        this.runsPerTick = runsPerTick;
    }

    @Override
    public void tick(PonderScene scene) {
        super.tick(scene);
        int runs = (int) this.runsPerTick;
        if (Create.RANDOM.nextFloat() < this.runsPerTick - (float) runs) {
            runs++;
        }
        for (int i = 0; i < runs; i++) {
            this.emitter.create(scene.getWorld(), this.anchor.x, this.anchor.y, this.anchor.z);
        }
    }

    @FunctionalInterface
    public interface Emitter {

        static <T extends ParticleOptions> EmitParticlesInstruction.Emitter simple(T data, Vec3 motion) {
            return (w, x, y, z) -> w.addParticle(data, x, y, z, motion.x, motion.y, motion.z);
        }

        static <T extends ParticleOptions> EmitParticlesInstruction.Emitter withinBlockSpace(T data, Vec3 motion) {
            return (w, x, y, z) -> w.addParticle(data, Math.floor(x) + (double) Create.RANDOM.nextFloat(), Math.floor(y) + (double) Create.RANDOM.nextFloat(), Math.floor(z) + (double) Create.RANDOM.nextFloat(), motion.x, motion.y, motion.z);
        }

        static ParticleEngine paticleManager() {
            return Minecraft.getInstance().particleEngine;
        }

        void create(PonderWorld var1, double var2, double var4, double var6);
    }
}