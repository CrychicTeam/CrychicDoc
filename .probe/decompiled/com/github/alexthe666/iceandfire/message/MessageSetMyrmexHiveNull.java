package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class MessageSetMyrmexHiveNull {

    public static MessageSetMyrmexHiveNull read(FriendlyByteBuf buf) {
        return new MessageSetMyrmexHiveNull();
    }

    public static void write(MessageSetMyrmexHiveNull message, FriendlyByteBuf buf) {
    }

    public static class Handler {

        public static void handle(MessageSetMyrmexHiveNull message, Supplier<NetworkEvent.Context> context) {
            ((NetworkEvent.Context) context.get()).setPacketHandled(true);
            Player player = ((NetworkEvent.Context) context.get()).getSender();
            if (player != null) {
                IceAndFire.PROXY.setReferencedHive(null);
            }
        }
    }
}