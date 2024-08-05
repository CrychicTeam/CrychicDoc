package com.github.alexmodguy.alexscaves.server.message;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.entity.util.KeybindUsingMount;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class MountedEntityKeyMessage {

    public int mountId;

    public int playerId;

    public int type;

    public MountedEntityKeyMessage(int mountId, int playerId, int type) {
        this.mountId = mountId;
        this.playerId = playerId;
        this.type = type;
    }

    public MountedEntityKeyMessage() {
    }

    public static MountedEntityKeyMessage read(FriendlyByteBuf buf) {
        return new MountedEntityKeyMessage(buf.readInt(), buf.readInt(), buf.readInt());
    }

    public static void write(MountedEntityKeyMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.mountId);
        buf.writeInt(message.playerId);
        buf.writeInt(message.type);
    }

    public static void handle(MountedEntityKeyMessage message, Supplier<NetworkEvent.Context> context) {
        ((NetworkEvent.Context) context.get()).enqueueWork(() -> {
            Player playerSided = ((NetworkEvent.Context) context.get()).getSender();
            if (((NetworkEvent.Context) context.get()).getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                playerSided = AlexsCaves.PROXY.getClientSidePlayer();
            }
            Entity parent = playerSided.m_9236_().getEntity(message.mountId);
            Entity keyPresser = playerSided.m_9236_().getEntity(message.playerId);
            if (keyPresser != null && parent instanceof KeybindUsingMount mount && keyPresser instanceof Player && keyPresser.isPassengerOfSameVehicle(parent)) {
                mount.onKeyPacket(keyPresser, message.type);
            }
        });
        ((NetworkEvent.Context) context.get()).setPacketHandled(true);
    }
}