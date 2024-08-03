package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class TrackingEmitter extends NoRenderParticle {

    private final Entity entity;

    private int life;

    private final int lifeTime;

    private final ParticleOptions particleType;

    public TrackingEmitter(ClientLevel clientLevel0, Entity entity1, ParticleOptions particleOptions2) {
        this(clientLevel0, entity1, particleOptions2, 3);
    }

    public TrackingEmitter(ClientLevel clientLevel0, Entity entity1, ParticleOptions particleOptions2, int int3) {
        this(clientLevel0, entity1, particleOptions2, int3, entity1.getDeltaMovement());
    }

    private TrackingEmitter(ClientLevel clientLevel0, Entity entity1, ParticleOptions particleOptions2, int int3, Vec3 vec4) {
        super(clientLevel0, entity1.getX(), entity1.getY(0.5), entity1.getZ(), vec4.x, vec4.y, vec4.z);
        this.entity = entity1;
        this.lifeTime = int3;
        this.particleType = particleOptions2;
        this.tick();
    }

    @Override
    public void tick() {
        for (int $$0 = 0; $$0 < 16; $$0++) {
            double $$1 = (double) (this.f_107223_.nextFloat() * 2.0F - 1.0F);
            double $$2 = (double) (this.f_107223_.nextFloat() * 2.0F - 1.0F);
            double $$3 = (double) (this.f_107223_.nextFloat() * 2.0F - 1.0F);
            if (!($$1 * $$1 + $$2 * $$2 + $$3 * $$3 > 1.0)) {
                double $$4 = this.entity.getX($$1 / 4.0);
                double $$5 = this.entity.getY(0.5 + $$2 / 4.0);
                double $$6 = this.entity.getZ($$3 / 4.0);
                this.f_107208_.addParticle(this.particleType, false, $$4, $$5, $$6, $$1, $$2 + 0.2, $$3);
            }
        }
        this.life++;
        if (this.life >= this.lifeTime) {
            this.m_107274_();
        }
    }
}