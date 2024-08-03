package com.simibubi.create.content.contraptions.sync;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

public class ContraptionSeatMappingPacket extends SimplePacketBase {

    private Map<UUID, Integer> mapping;

    private int entityID;

    private int dismountedID;

    public ContraptionSeatMappingPacket(int entityID, Map<UUID, Integer> mapping) {
        this(entityID, mapping, -1);
    }

    public ContraptionSeatMappingPacket(int entityID, Map<UUID, Integer> mapping, int dismountedID) {
        this.entityID = entityID;
        this.mapping = mapping;
        this.dismountedID = dismountedID;
    }

    public ContraptionSeatMappingPacket(FriendlyByteBuf buffer) {
        this.entityID = buffer.readInt();
        this.dismountedID = buffer.readInt();
        this.mapping = new HashMap();
        short size = buffer.readShort();
        for (int i = 0; i < size; i++) {
            this.mapping.put(buffer.readUUID(), Integer.valueOf(buffer.readShort()));
        }
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityID);
        buffer.writeInt(this.dismountedID);
        buffer.writeShort(this.mapping.size());
        this.mapping.forEach((k, v) -> {
            buffer.writeUUID(k);
            buffer.writeShort(v);
        });
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level.getEntity(this.entityID) instanceof AbstractContraptionEntity contraptionEntity) {
                if (this.dismountedID != -1) {
                    Entity dismountedByID = Minecraft.getInstance().level.getEntity(this.dismountedID);
                    if (Minecraft.getInstance().player != dismountedByID) {
                        return;
                    }
                    Vec3 transformedVector = contraptionEntity.getPassengerPosition(dismountedByID, 1.0F);
                    if (transformedVector != null) {
                        dismountedByID.getPersistentData().put("ContraptionDismountLocation", VecHelper.writeNBT(transformedVector));
                    }
                }
                contraptionEntity.getContraption().setSeatMapping(this.mapping);
            }
        });
        return true;
    }
}