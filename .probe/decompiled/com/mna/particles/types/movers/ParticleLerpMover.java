package com.mna.particles.types.movers;

import com.mna.api.particles.IParticleMoveType;
import com.mna.particles.base.MAParticleBase;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class ParticleLerpMover implements IParticleMoveType {

    private Vec3 start;

    private Vec3 end;

    public ParticleLerpMover() {
        this.start = new Vec3(0.0, 0.0, 0.0);
        this.end = new Vec3(0.0, 0.0, 0.0);
    }

    public ParticleLerpMover(double sx, double sy, double sz, double ex, double ey, double ez) {
        this.start = new Vec3(sx, sy, sz);
        this.end = new Vec3(ex, ey, ez);
    }

    @Override
    public void serialize(FriendlyByteBuf buffer) {
        buffer.writeDouble(this.start.x);
        buffer.writeDouble(this.start.y);
        buffer.writeDouble(this.start.z);
        buffer.writeDouble(this.end.x);
        buffer.writeDouble(this.end.y);
        buffer.writeDouble(this.end.z);
    }

    @Override
    public String serialize() {
        return "LerpMover:" + this.start.x + ":" + this.start.y + ":" + this.start.z + ":" + this.end.x + ":" + this.end.y + ":" + this.end.z;
    }

    @Override
    public IParticleMoveType deserialize(FriendlyByteBuf buffer) {
        this.start = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        this.end = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        return this;
    }

    @Override
    public void deserialize(String string) {
        if (string.startsWith("VelocityMover")) {
            String[] parts = string.split(":");
            this.start = new Vec3(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
            this.end = new Vec3(Double.parseDouble(parts[4]), Double.parseDouble(parts[5]), Double.parseDouble(parts[6]));
        }
    }

    @Override
    public void configureParticle(TextureSheetParticle particle) {
        if (particle instanceof MAParticleBase) {
            ((MAParticleBase) particle).setMoveLerp(this.start.x, this.start.y, this.start.z, this.end.x, this.end.y, this.end.z);
        }
    }

    @Override
    public int getId() {
        return 1;
    }
}