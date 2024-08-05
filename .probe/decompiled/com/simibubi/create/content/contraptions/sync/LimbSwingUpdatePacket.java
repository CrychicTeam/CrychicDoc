package com.simibubi.create.content.contraptions.sync;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

public class LimbSwingUpdatePacket extends SimplePacketBase {

    private int entityId;

    private Vec3 position;

    private float limbSwing;

    public LimbSwingUpdatePacket(int entityId, Vec3 position, float limbSwing) {
        this.entityId = entityId;
        this.position = position;
        this.limbSwing = limbSwing;
    }

    public LimbSwingUpdatePacket(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.position = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        this.limbSwing = buffer.readFloat();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeDouble(this.position.x);
        buffer.writeDouble(this.position.y);
        buffer.writeDouble(this.position.z);
        buffer.writeFloat(this.limbSwing);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ClientLevel world = Minecraft.getInstance().level;
            if (world != null) {
                Entity entity = world.getEntity(this.entityId);
                if (entity != null) {
                    CompoundTag data = entity.getPersistentData();
                    data.putInt("LastOverrideLimbSwingUpdate", 0);
                    data.putFloat("OverrideLimbSwing", this.limbSwing);
                    entity.lerpTo(this.position.x, this.position.y, this.position.z, entity.getYRot(), entity.getXRot(), 2, false);
                }
            }
        });
        return true;
    }
}