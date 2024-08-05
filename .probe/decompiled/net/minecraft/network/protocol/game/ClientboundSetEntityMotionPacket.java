package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ClientboundSetEntityMotionPacket implements Packet<ClientGamePacketListener> {

    private final int id;

    private final int xa;

    private final int ya;

    private final int za;

    public ClientboundSetEntityMotionPacket(Entity entity0) {
        this(entity0.getId(), entity0.getDeltaMovement());
    }

    public ClientboundSetEntityMotionPacket(int int0, Vec3 vec1) {
        this.id = int0;
        double $$2 = 3.9;
        double $$3 = Mth.clamp(vec1.x, -3.9, 3.9);
        double $$4 = Mth.clamp(vec1.y, -3.9, 3.9);
        double $$5 = Mth.clamp(vec1.z, -3.9, 3.9);
        this.xa = (int) ($$3 * 8000.0);
        this.ya = (int) ($$4 * 8000.0);
        this.za = (int) ($$5 * 8000.0);
    }

    public ClientboundSetEntityMotionPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.id = friendlyByteBuf0.readVarInt();
        this.xa = friendlyByteBuf0.readShort();
        this.ya = friendlyByteBuf0.readShort();
        this.za = friendlyByteBuf0.readShort();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.id);
        friendlyByteBuf0.writeShort(this.xa);
        friendlyByteBuf0.writeShort(this.ya);
        friendlyByteBuf0.writeShort(this.za);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleSetEntityMotion(this);
    }

    public int getId() {
        return this.id;
    }

    public int getXa() {
        return this.xa;
    }

    public int getYa() {
        return this.ya;
    }

    public int getZa() {
        return this.za;
    }
}