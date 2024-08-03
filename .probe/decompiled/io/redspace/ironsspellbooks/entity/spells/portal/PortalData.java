package io.redspace.ironsspellbooks.entity.spells.portal;

import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.util.NBT;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class PortalData implements ICastDataSerializable {

    public PortalPos globalPos1;

    public UUID portalEntityId1;

    public PortalPos globalPos2;

    public UUID portalEntityId2;

    public int ticksToLive;

    public void setPortalDuration(int ticksToLive) {
        this.ticksToLive = ticksToLive;
    }

    public Optional<PortalPos> getConnectedPortalPos(UUID portalId) {
        if (this.portalEntityId1.equals(portalId)) {
            return Optional.of(this.globalPos2);
        } else {
            return this.portalEntityId2.equals(portalId) ? Optional.of(this.globalPos1) : Optional.empty();
        }
    }

    public UUID getConnectedPortalUUID(UUID portalId) {
        if (this.portalEntityId1.equals(portalId)) {
            return this.portalEntityId2;
        } else {
            return this.portalEntityId2.equals(portalId) ? this.portalEntityId1 : null;
        }
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buffer) {
        buffer.writeLong((long) this.ticksToLive);
        if (this.globalPos1 != null && this.portalEntityId1 != null) {
            buffer.writeBoolean(true);
            this.writePortalPosToBuffer(buffer, this.globalPos1);
            buffer.writeUUID(this.portalEntityId1);
            if (this.globalPos2 != null && this.portalEntityId2 != null) {
                buffer.writeBoolean(true);
                this.writePortalPosToBuffer(buffer, this.globalPos2);
                buffer.writeUUID(this.portalEntityId2);
            } else {
                buffer.writeBoolean(false);
            }
        } else {
            buffer.writeBoolean(false);
        }
    }

    private void writePortalPosToBuffer(FriendlyByteBuf buffer, PortalPos pos) {
        buffer.writeResourceKey(pos.dimension());
        Vec3 vec3 = pos.pos();
        buffer.writeInt((int) (vec3.x * 10.0));
        buffer.writeInt((int) (vec3.y * 10.0));
        buffer.writeInt((int) (vec3.z * 10.0));
        buffer.writeFloat(pos.rotation());
    }

    private PortalPos readPortalPosFromBuffer(FriendlyByteBuf buffer) {
        return PortalPos.of(buffer.readResourceKey(Registries.DIMENSION), new Vec3((double) buffer.readInt() / 10.0, (double) buffer.readInt() / 10.0, (double) buffer.readInt() / 10.0), buffer.readFloat());
    }

    @Override
    public void readFromBuffer(FriendlyByteBuf buffer) {
        this.ticksToLive = buffer.readInt();
        if (buffer.readBoolean()) {
            this.globalPos1 = this.readPortalPosFromBuffer(buffer);
            this.portalEntityId1 = buffer.readUUID();
            if (buffer.readBoolean()) {
                this.globalPos2 = this.readPortalPosFromBuffer(buffer);
                this.portalEntityId2 = buffer.readUUID();
            }
        }
    }

    @Override
    public void reset() {
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("ticksToLive", this.ticksToLive);
        if (this.globalPos1 != null) {
            tag.put("gp1", NBT.writePortalPos(this.globalPos1));
            tag.putUUID("pe1", this.portalEntityId1);
            if (this.globalPos2 != null) {
                tag.put("gp2", NBT.writePortalPos(this.globalPos2));
                tag.putUUID("pe2", this.portalEntityId2);
            }
        }
        return tag;
    }

    public void deserializeNBT(CompoundTag compoundTag) {
        this.ticksToLive = compoundTag.getInt("ticksToLive");
        if (compoundTag.contains("gp1") && compoundTag.contains("pe1")) {
            this.globalPos1 = NBT.readPortalPos(compoundTag.getCompound("gp1"));
            this.portalEntityId1 = compoundTag.getUUID("pe1");
            if (compoundTag.contains("gp2") && compoundTag.contains("pe2")) {
                this.globalPos2 = NBT.readPortalPos(compoundTag.getCompound("gp2"));
                this.portalEntityId2 = compoundTag.getUUID("pe2");
            }
        }
    }
}