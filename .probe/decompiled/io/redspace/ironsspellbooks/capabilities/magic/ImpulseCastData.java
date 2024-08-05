package io.redspace.ironsspellbooks.capabilities.magic;

import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class ImpulseCastData implements ICastDataSerializable {

    public float x;

    public float y;

    public float z;

    public boolean hasImpulse;

    public ImpulseCastData() {
    }

    public ImpulseCastData(float x, float y, float z, boolean hasImpulse) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.hasImpulse = hasImpulse;
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buffer) {
        buffer.writeFloat(this.x);
        buffer.writeFloat(this.y);
        buffer.writeFloat(this.z);
        buffer.writeBoolean(this.hasImpulse);
    }

    @Override
    public void readFromBuffer(FriendlyByteBuf buffer) {
        this.x = buffer.readFloat();
        this.y = buffer.readFloat();
        this.z = buffer.readFloat();
        this.hasImpulse = buffer.readBoolean();
    }

    @Override
    public void reset() {
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("x", this.x);
        tag.putFloat("y", this.y);
        tag.putFloat("z", this.z);
        tag.putBoolean("i", this.hasImpulse);
        return tag;
    }

    public void deserializeNBT(CompoundTag compoundTag) {
        this.x = compoundTag.getFloat("x");
        this.y = compoundTag.getFloat("y");
        this.z = compoundTag.getFloat("z");
        this.hasImpulse = compoundTag.getBoolean("i");
    }
}