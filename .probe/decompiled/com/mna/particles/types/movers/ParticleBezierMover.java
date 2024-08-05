package com.mna.particles.types.movers;

import com.mna.api.particles.IParticleMoveType;
import com.mna.particles.base.MAParticleBase;
import com.mna.tools.math.Vector3;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class ParticleBezierMover implements IParticleMoveType {

    private Vector3 start;

    private Vector3 end;

    private Vector3 controlA;

    private Vector3 controlB;

    public ParticleBezierMover() {
        this.start = new Vector3(0.0, 0.0, 0.0);
        this.end = new Vector3(0.0, 0.0, 0.0);
        this.controlA = new Vector3(0.0, 0.0, 0.0);
        this.controlB = new Vector3(0.0, 0.0, 0.0);
    }

    public ParticleBezierMover(Vec3 start, Vec3 end) {
        this.start = new Vector3(start);
        this.end = new Vector3(end);
    }

    public ParticleBezierMover(Vec3 start, Vec3 end, Vec3 controlA, Vec3 controlB) {
        this.start = new Vector3(start);
        this.end = new Vector3(end);
        this.controlA = new Vector3(controlA);
        this.controlB = new Vector3(controlB);
    }

    @Override
    public void serialize(FriendlyByteBuf buffer) {
        buffer.writeDouble((double) this.start.x);
        buffer.writeDouble((double) this.start.y);
        buffer.writeDouble((double) this.start.z);
        buffer.writeDouble((double) this.end.x);
        buffer.writeDouble((double) this.end.y);
        buffer.writeDouble((double) this.end.z);
        if (this.controlA != null && this.controlB != null) {
            buffer.writeBoolean(true);
            buffer.writeDouble((double) this.controlA.x);
            buffer.writeDouble((double) this.controlA.y);
            buffer.writeDouble((double) this.controlA.z);
            buffer.writeDouble((double) this.controlB.x);
            buffer.writeDouble((double) this.controlB.y);
            buffer.writeDouble((double) this.controlB.z);
        } else {
            buffer.writeBoolean(false);
        }
    }

    @Override
    public String serialize() {
        return this.controlA != null && this.controlB != null ? "BezierMover:" + this.start.x + ":" + this.start.y + ":" + this.start.z + ":" + this.end.x + ":" + this.end.y + ":" + this.end.z + ":true:" + this.controlA.x + ":" + this.controlA.y + ":" + this.controlA.z + ":" + this.controlB.x + ":" + this.controlB.y + ":" + this.controlB.z : "BezierMover:" + this.start.x + ":" + this.start.y + ":" + this.start.z + ":" + this.end.x + ":" + this.end.y + ":" + this.end.z + ":false";
    }

    @Override
    public IParticleMoveType deserialize(FriendlyByteBuf buffer) {
        this.start = new Vector3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        this.end = new Vector3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        if (buffer.readBoolean()) {
            this.controlA = new Vector3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
            this.controlB = new Vector3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        }
        return this;
    }

    @Override
    public void deserialize(String string) {
        if (string.startsWith("VelocityMover")) {
            String[] parts = string.split(":");
            this.start = new Vector3(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
            this.end = new Vector3(Double.parseDouble(parts[4]), Double.parseDouble(parts[5]), Double.parseDouble(parts[6]));
            if (Boolean.parseBoolean(parts[7])) {
                this.start = new Vector3(Double.parseDouble(parts[8]), Double.parseDouble(parts[9]), Double.parseDouble(parts[10]));
                this.end = new Vector3(Double.parseDouble(parts[11]), Double.parseDouble(parts[12]), Double.parseDouble(parts[13]));
            }
        }
    }

    @Override
    public void configureParticle(TextureSheetParticle particle) {
        if (particle instanceof MAParticleBase) {
            if (this.controlA != null && this.controlB != null) {
                ((MAParticleBase) particle).setMoveBezier(this.start, this.end, this.controlA, this.controlB);
            } else {
                ((MAParticleBase) particle).setMoveBezier(this.start, this.end);
            }
        }
    }

    @Override
    public int getId() {
        return 3;
    }
}