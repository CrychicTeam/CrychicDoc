package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class MessageMyrmexSettings {

    public int queenID;

    public boolean reproduces;

    public boolean deleteRoom;

    public long roomToDelete;

    public MessageMyrmexSettings(int queenID, boolean repoduces, boolean deleteRoom, long roomToDelete) {
        this.queenID = queenID;
        this.reproduces = repoduces;
        this.deleteRoom = deleteRoom;
        this.roomToDelete = roomToDelete;
    }

    public static MessageMyrmexSettings read(FriendlyByteBuf buf) {
        return new MessageMyrmexSettings(buf.readInt(), buf.readBoolean(), buf.readBoolean(), buf.readLong());
    }

    public static void write(MessageMyrmexSettings message, FriendlyByteBuf buf) {
        buf.writeInt(message.queenID);
        buf.writeBoolean(message.reproduces);
        buf.writeBoolean(message.deleteRoom);
        buf.writeLong(message.roomToDelete);
    }

    public static class Handler {

        public static void handle(MessageMyrmexSettings message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = (NetworkEvent.Context) contextSupplier.get();
            context.enqueueWork(() -> {
                Player player = context.getSender();
                if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                    player = IceAndFire.PROXY.getClientSidePlayer();
                }
                if (player != null && player.m_9236_().getEntity(message.queenID) instanceof EntityMyrmexBase myrmex) {
                    MyrmexHive hive = myrmex.getHive();
                    if (hive != null) {
                        hive.reproduces = message.reproduces;
                        if (message.deleteRoom) {
                            hive.removeRoom(BlockPos.of(message.roomToDelete));
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}