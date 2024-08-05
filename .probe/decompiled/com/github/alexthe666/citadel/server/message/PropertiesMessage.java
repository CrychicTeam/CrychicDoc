package com.github.alexthe666.citadel.server.message;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class PropertiesMessage {

    private String propertyID;

    private CompoundTag compound;

    private int entityID;

    public PropertiesMessage(String propertyID, CompoundTag compound, int entityID) {
        this.propertyID = propertyID;
        this.compound = compound;
        this.entityID = entityID;
    }

    public static void write(PropertiesMessage message, FriendlyByteBuf packetBuffer) {
        PacketBufferUtils.writeUTF8String(packetBuffer, message.propertyID);
        PacketBufferUtils.writeTag(packetBuffer, message.compound);
        packetBuffer.writeInt(message.entityID);
    }

    public static PropertiesMessage read(FriendlyByteBuf packetBuffer) {
        return new PropertiesMessage(PacketBufferUtils.readUTF8String(packetBuffer), PacketBufferUtils.readTag(packetBuffer), packetBuffer.readInt());
    }

    public static class Handler {

        public static void handle(PropertiesMessage message, Supplier<NetworkEvent.Context> context) {
            ((NetworkEvent.Context) context.get()).setPacketHandled(true);
            ((NetworkEvent.Context) context.get()).enqueueWork(() -> {
                if (((NetworkEvent.Context) context.get()).getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                    Citadel.PROXY.handlePropertiesPacket(message.propertyID, message.compound, message.entityID);
                } else {
                    Entity e = ((NetworkEvent.Context) context.get()).getSender().m_9236_().getEntity(message.entityID);
                    if (e instanceof LivingEntity && (message.propertyID.equals("CitadelPatreonConfig") || message.propertyID.equals("CitadelTagUpdate"))) {
                        CitadelEntityData.setCitadelTag((LivingEntity) e, message.compound);
                    }
                }
            });
        }
    }
}