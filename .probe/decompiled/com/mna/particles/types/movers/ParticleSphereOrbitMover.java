package com.mna.particles.types.movers;

import com.mna.api.particles.IParticleMoveType;
import com.mna.particles.base.MAParticleBase;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class ParticleSphereOrbitMover implements IParticleMoveType {

    private Vec3 center;

    private double forward;

    private double tilt;

    private double radius;

    public ParticleSphereOrbitMover() {
        this.center = new Vec3(0.0, 0.0, 0.0);
    }

    public ParticleSphereOrbitMover(double sx, double sy, double sz, double forward, double tilt, double radius) {
        this.center = new Vec3(sx, sy, sz);
        this.forward = forward;
        this.tilt = tilt;
        this.radius = radius;
    }

    @Override
    public void serialize(FriendlyByteBuf buffer) {
        buffer.writeDouble(this.center.x);
        buffer.writeDouble(this.center.y);
        buffer.writeDouble(this.center.z);
        buffer.writeDouble(this.forward);
        buffer.writeDouble(this.tilt);
        buffer.writeDouble(this.radius);
    }

    @Override
    public String serialize() {
        return "OrbitMover:" + this.center.x + ":" + this.center.y + ":" + this.center.z + ":" + this.forward + ":" + this.tilt + ":" + this.radius;
    }

    @Override
    public IParticleMoveType deserialize(FriendlyByteBuf buffer) {
        this.center = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        this.forward = buffer.readDouble();
        this.tilt = buffer.readDouble();
        this.radius = buffer.readDouble();
        return this;
    }

    @Override
    public void deserialize(String string) {
        if (string.startsWith("SphereOrbitMover")) {
            String[] parts = string.split(":");
            this.center = new Vec3(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
            this.forward = Double.parseDouble(parts[4]);
            this.tilt = Double.parseDouble(parts[5]);
            this.radius = Double.parseDouble(parts[6]);
        }
    }

    @Override
    public void configureParticle(TextureSheetParticle particle) {
        if (particle instanceof MAParticleBase) {
            ((MAParticleBase) particle).setMoveSphereOrbit(this.center.x, this.center.y, this.center.z, this.forward, this.tilt, this.radius);
        }
    }

    @Override
    public int getId() {
        return 2;
    }
}