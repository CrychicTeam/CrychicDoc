package com.mna.particles.types.movers;

import com.mna.api.particles.IParticleMoveType;
import com.mna.particles.base.MAParticleBase;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class ParticleOrbitMover implements IParticleMoveType {

    private Vec3 center;

    private double forward;

    private double up;

    private double radius;

    private double flare = -1.0;

    public ParticleOrbitMover() {
        this.center = new Vec3(0.0, 0.0, 0.0);
    }

    public ParticleOrbitMover(double sx, double sy, double sz, double forward, double up, double radius) {
        this.center = new Vec3(sx, sy, sz);
        this.forward = forward;
        this.up = up;
        this.radius = radius;
    }

    public ParticleOrbitMover(double sx, double sy, double sz, double forward, double up, double radius, double flare) {
        this(sx, sy, sz, forward, up, radius);
        this.flare = flare;
    }

    @Override
    public void serialize(FriendlyByteBuf buffer) {
        buffer.writeDouble(this.center.x);
        buffer.writeDouble(this.center.y);
        buffer.writeDouble(this.center.z);
        buffer.writeDouble(this.forward);
        buffer.writeDouble(this.up);
        buffer.writeDouble(this.radius);
        buffer.writeDouble(this.flare);
    }

    @Override
    public String serialize() {
        return "OrbitMover:" + this.center.x + ":" + this.center.y + ":" + this.center.z + ":" + this.forward + ":" + this.up + ":" + this.radius + ":" + this.flare;
    }

    @Override
    public IParticleMoveType deserialize(FriendlyByteBuf buffer) {
        this.center = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        this.forward = buffer.readDouble();
        this.up = buffer.readDouble();
        this.radius = buffer.readDouble();
        this.flare = buffer.readDouble();
        return this;
    }

    @Override
    public void deserialize(String string) {
        if (string.startsWith("OrbitMover")) {
            String[] parts = string.split(":");
            this.center = new Vec3(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
            this.forward = Double.parseDouble(parts[4]);
            this.up = Double.parseDouble(parts[5]);
            this.radius = Double.parseDouble(parts[6]);
            if (parts.length > 6) {
                this.flare = Double.parseDouble(parts[7]);
            }
        }
    }

    @Override
    public void configureParticle(TextureSheetParticle particle) {
        if (particle instanceof MAParticleBase) {
            ((MAParticleBase) particle).setMoveOrbit(this.center.x, this.center.y, this.center.z, this.forward, this.up, this.radius, this.flare);
        }
    }

    @Override
    public int getId() {
        return 2;
    }
}