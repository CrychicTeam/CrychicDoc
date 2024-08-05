package net.minecraft.network.protocol.game;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

public class ClientboundAddEntityPacket implements Packet<ClientGamePacketListener> {

    private static final double MAGICAL_QUANTIZATION = 8000.0;

    private static final double LIMIT = 3.9;

    private final int id;

    private final UUID uuid;

    private final EntityType<?> type;

    private final double x;

    private final double y;

    private final double z;

    private final int xa;

    private final int ya;

    private final int za;

    private final byte xRot;

    private final byte yRot;

    private final byte yHeadRot;

    private final int data;

    public ClientboundAddEntityPacket(Entity entity0) {
        this(entity0, 0);
    }

    public ClientboundAddEntityPacket(Entity entity0, int int1) {
        this(entity0.getId(), entity0.getUUID(), entity0.getX(), entity0.getY(), entity0.getZ(), entity0.getXRot(), entity0.getYRot(), entity0.getType(), int1, entity0.getDeltaMovement(), (double) entity0.getYHeadRot());
    }

    public ClientboundAddEntityPacket(Entity entity0, int int1, BlockPos blockPos2) {
        this(entity0.getId(), entity0.getUUID(), (double) blockPos2.m_123341_(), (double) blockPos2.m_123342_(), (double) blockPos2.m_123343_(), entity0.getXRot(), entity0.getYRot(), entity0.getType(), int1, entity0.getDeltaMovement(), (double) entity0.getYHeadRot());
    }

    public ClientboundAddEntityPacket(int int0, UUID uUID1, double double2, double double3, double double4, float float5, float float6, EntityType<?> entityType7, int int8, Vec3 vec9, double double10) {
        this.id = int0;
        this.uuid = uUID1;
        this.x = double2;
        this.y = double3;
        this.z = double4;
        this.xRot = (byte) Mth.floor(float5 * 256.0F / 360.0F);
        this.yRot = (byte) Mth.floor(float6 * 256.0F / 360.0F);
        this.yHeadRot = (byte) Mth.floor(double10 * 256.0 / 360.0);
        this.type = entityType7;
        this.data = int8;
        this.xa = (int) (Mth.clamp(vec9.x, -3.9, 3.9) * 8000.0);
        this.ya = (int) (Mth.clamp(vec9.y, -3.9, 3.9) * 8000.0);
        this.za = (int) (Mth.clamp(vec9.z, -3.9, 3.9) * 8000.0);
    }

    public ClientboundAddEntityPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.id = friendlyByteBuf0.readVarInt();
        this.uuid = friendlyByteBuf0.readUUID();
        this.type = friendlyByteBuf0.readById(BuiltInRegistries.ENTITY_TYPE);
        this.x = friendlyByteBuf0.readDouble();
        this.y = friendlyByteBuf0.readDouble();
        this.z = friendlyByteBuf0.readDouble();
        this.xRot = friendlyByteBuf0.readByte();
        this.yRot = friendlyByteBuf0.readByte();
        this.yHeadRot = friendlyByteBuf0.readByte();
        this.data = friendlyByteBuf0.readVarInt();
        this.xa = friendlyByteBuf0.readShort();
        this.ya = friendlyByteBuf0.readShort();
        this.za = friendlyByteBuf0.readShort();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.id);
        friendlyByteBuf0.writeUUID(this.uuid);
        friendlyByteBuf0.writeId(BuiltInRegistries.ENTITY_TYPE, this.type);
        friendlyByteBuf0.writeDouble(this.x);
        friendlyByteBuf0.writeDouble(this.y);
        friendlyByteBuf0.writeDouble(this.z);
        friendlyByteBuf0.writeByte(this.xRot);
        friendlyByteBuf0.writeByte(this.yRot);
        friendlyByteBuf0.writeByte(this.yHeadRot);
        friendlyByteBuf0.writeVarInt(this.data);
        friendlyByteBuf0.writeShort(this.xa);
        friendlyByteBuf0.writeShort(this.ya);
        friendlyByteBuf0.writeShort(this.za);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleAddEntity(this);
    }

    public int getId() {
        return this.id;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public EntityType<?> getType() {
        return this.type;
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

    public double getXa() {
        return (double) this.xa / 8000.0;
    }

    public double getYa() {
        return (double) this.ya / 8000.0;
    }

    public double getZa() {
        return (double) this.za / 8000.0;
    }

    public float getXRot() {
        return (float) (this.xRot * 360) / 256.0F;
    }

    public float getYRot() {
        return (float) (this.yRot * 360) / 256.0F;
    }

    public float getYHeadRot() {
        return (float) (this.yHeadRot * 360) / 256.0F;
    }

    public int getData() {
        return this.data;
    }
}