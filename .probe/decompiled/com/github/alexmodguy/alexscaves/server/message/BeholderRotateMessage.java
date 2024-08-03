package com.github.alexmodguy.alexscaves.server.message;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.entity.item.BeholderEyeEntity;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

public class BeholderRotateMessage {

    public int beholderId;

    public float rotX;

    public float rotY;

    public BeholderRotateMessage(int beholderId, float rotX, float rotY) {
        this.beholderId = beholderId;
        this.rotX = rotX;
        this.rotY = rotY;
    }

    public BeholderRotateMessage() {
    }

    public static BeholderRotateMessage read(FriendlyByteBuf buf) {
        return new BeholderRotateMessage(buf.readInt(), buf.readFloat(), buf.readFloat());
    }

    public static void write(BeholderRotateMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.beholderId);
        buf.writeFloat(message.rotX);
        buf.writeFloat(message.rotY);
    }

    public static void handle(BeholderRotateMessage message, Supplier<NetworkEvent.Context> context) {
        ((NetworkEvent.Context) context.get()).enqueueWork(() -> {
            Player playerSided = ((NetworkEvent.Context) context.get()).getSender();
            if (((NetworkEvent.Context) context.get()).getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                playerSided = AlexsCaves.PROXY.getClientSidePlayer();
            }
            Level serverLevel = ServerLifecycleHooks.getCurrentServer().getLevel(playerSided.m_9236_().dimension());
            if (serverLevel.getEntity(message.beholderId) instanceof BeholderEyeEntity var5) {
                ;
            }
        });
        ((NetworkEvent.Context) context.get()).setPacketHandled(true);
    }
}