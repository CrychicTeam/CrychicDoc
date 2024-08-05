package net.minecraft.network.protocol.game;

import javax.annotation.Nullable;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ClientboundPlayerLookAtPacket implements Packet<ClientGamePacketListener> {

    private final double x;

    private final double y;

    private final double z;

    private final int entity;

    private final EntityAnchorArgument.Anchor fromAnchor;

    private final EntityAnchorArgument.Anchor toAnchor;

    private final boolean atEntity;

    public ClientboundPlayerLookAtPacket(EntityAnchorArgument.Anchor entityAnchorArgumentAnchor0, double double1, double double2, double double3) {
        this.fromAnchor = entityAnchorArgumentAnchor0;
        this.x = double1;
        this.y = double2;
        this.z = double3;
        this.entity = 0;
        this.atEntity = false;
        this.toAnchor = null;
    }

    public ClientboundPlayerLookAtPacket(EntityAnchorArgument.Anchor entityAnchorArgumentAnchor0, Entity entity1, EntityAnchorArgument.Anchor entityAnchorArgumentAnchor2) {
        this.fromAnchor = entityAnchorArgumentAnchor0;
        this.entity = entity1.getId();
        this.toAnchor = entityAnchorArgumentAnchor2;
        Vec3 $$3 = entityAnchorArgumentAnchor2.apply(entity1);
        this.x = $$3.x;
        this.y = $$3.y;
        this.z = $$3.z;
        this.atEntity = true;
    }

    public ClientboundPlayerLookAtPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.fromAnchor = friendlyByteBuf0.readEnum(EntityAnchorArgument.Anchor.class);
        this.x = friendlyByteBuf0.readDouble();
        this.y = friendlyByteBuf0.readDouble();
        this.z = friendlyByteBuf0.readDouble();
        this.atEntity = friendlyByteBuf0.readBoolean();
        if (this.atEntity) {
            this.entity = friendlyByteBuf0.readVarInt();
            this.toAnchor = friendlyByteBuf0.readEnum(EntityAnchorArgument.Anchor.class);
        } else {
            this.entity = 0;
            this.toAnchor = null;
        }
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeEnum(this.fromAnchor);
        friendlyByteBuf0.writeDouble(this.x);
        friendlyByteBuf0.writeDouble(this.y);
        friendlyByteBuf0.writeDouble(this.z);
        friendlyByteBuf0.writeBoolean(this.atEntity);
        if (this.atEntity) {
            friendlyByteBuf0.writeVarInt(this.entity);
            friendlyByteBuf0.writeEnum(this.toAnchor);
        }
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleLookAt(this);
    }

    public EntityAnchorArgument.Anchor getFromAnchor() {
        return this.fromAnchor;
    }

    @Nullable
    public Vec3 getPosition(Level level0) {
        if (this.atEntity) {
            Entity $$1 = level0.getEntity(this.entity);
            return $$1 == null ? new Vec3(this.x, this.y, this.z) : this.toAnchor.apply($$1);
        } else {
            return new Vec3(this.x, this.y, this.z);
        }
    }
}