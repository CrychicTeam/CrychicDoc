package net.minecraft.network.protocol.game;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundLevelParticlesPacket implements Packet<ClientGamePacketListener> {

    private final double x;

    private final double y;

    private final double z;

    private final float xDist;

    private final float yDist;

    private final float zDist;

    private final float maxSpeed;

    private final int count;

    private final boolean overrideLimiter;

    private final ParticleOptions particle;

    public <T extends ParticleOptions> ClientboundLevelParticlesPacket(T t0, boolean boolean1, double double2, double double3, double double4, float float5, float float6, float float7, float float8, int int9) {
        this.particle = t0;
        this.overrideLimiter = boolean1;
        this.x = double2;
        this.y = double3;
        this.z = double4;
        this.xDist = float5;
        this.yDist = float6;
        this.zDist = float7;
        this.maxSpeed = float8;
        this.count = int9;
    }

    public ClientboundLevelParticlesPacket(FriendlyByteBuf friendlyByteBuf0) {
        ParticleType<?> $$1 = friendlyByteBuf0.readById(BuiltInRegistries.PARTICLE_TYPE);
        this.overrideLimiter = friendlyByteBuf0.readBoolean();
        this.x = friendlyByteBuf0.readDouble();
        this.y = friendlyByteBuf0.readDouble();
        this.z = friendlyByteBuf0.readDouble();
        this.xDist = friendlyByteBuf0.readFloat();
        this.yDist = friendlyByteBuf0.readFloat();
        this.zDist = friendlyByteBuf0.readFloat();
        this.maxSpeed = friendlyByteBuf0.readFloat();
        this.count = friendlyByteBuf0.readInt();
        this.particle = this.readParticle(friendlyByteBuf0, (ParticleType<ParticleOptions>) $$1);
    }

    private <T extends ParticleOptions> T readParticle(FriendlyByteBuf friendlyByteBuf0, ParticleType<T> particleTypeT1) {
        return particleTypeT1.getDeserializer().fromNetwork(particleTypeT1, friendlyByteBuf0);
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeId(BuiltInRegistries.PARTICLE_TYPE, this.particle.getType());
        friendlyByteBuf0.writeBoolean(this.overrideLimiter);
        friendlyByteBuf0.writeDouble(this.x);
        friendlyByteBuf0.writeDouble(this.y);
        friendlyByteBuf0.writeDouble(this.z);
        friendlyByteBuf0.writeFloat(this.xDist);
        friendlyByteBuf0.writeFloat(this.yDist);
        friendlyByteBuf0.writeFloat(this.zDist);
        friendlyByteBuf0.writeFloat(this.maxSpeed);
        friendlyByteBuf0.writeInt(this.count);
        this.particle.writeToNetwork(friendlyByteBuf0);
    }

    public boolean isOverrideLimiter() {
        return this.overrideLimiter;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getXDist() {
        return this.xDist;
    }

    public float getYDist() {
        return this.yDist;
    }

    public float getZDist() {
        return this.zDist;
    }

    public float getMaxSpeed() {
        return this.maxSpeed;
    }

    public int getCount() {
        return this.count;
    }

    public ParticleOptions getParticle() {
        return this.particle;
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleParticleEvent(this);
    }
}