package com.mna.particles.types.movers;

import com.mna.api.particles.IParticleMoveType;
import com.mna.particles.base.MAParticleBase;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class ParticleVelocityMover implements IParticleMoveType {

    private Vec3 velocity;

    private boolean decay;

    public ParticleVelocityMover() {
        this.velocity = new Vec3(0.0, 0.0, 0.0);
        this.decay = false;
    }

    public ParticleVelocityMover(double dx, double dy, double dz, boolean decay) {
        this.velocity = new Vec3(dx, dy, dz);
        this.decay = decay;
    }

    @Override
    public void serialize(FriendlyByteBuf buffer) {
        buffer.writeDouble(this.velocity.x);
        buffer.writeDouble(this.velocity.y);
        buffer.writeDouble(this.velocity.z);
        buffer.writeBoolean(this.decay);
    }

    @Override
    public String serialize() {
        return "VelocityMover:" + this.velocity.x + ":" + this.velocity.y + ":" + this.velocity.z + ":" + this.decay;
    }

    @Override
    public IParticleMoveType deserialize(FriendlyByteBuf buffer) {
        this.velocity = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        this.decay = buffer.readBoolean();
        return this;
    }

    @Override
    public void deserialize(String string) {
        if (string.startsWith("VelocityMover")) {
            String[] parts = string.split(":");
            this.velocity = new Vec3(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
            this.decay = Boolean.parseBoolean(parts[4]);
        }
    }

    @Override
    public void configureParticle(TextureSheetParticle particle) {
        if (particle instanceof MAParticleBase) {
            ((MAParticleBase) particle).setMoveVelocity(this.velocity.x, this.velocity.y, this.velocity.z, this.decay);
        }
    }

    @Override
    public int getId() {
        return 0;
    }
}