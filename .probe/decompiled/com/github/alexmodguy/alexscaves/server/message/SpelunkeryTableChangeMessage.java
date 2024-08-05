package com.github.alexmodguy.alexscaves.server.message;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.inventory.SpelunkeryTableMenu;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class SpelunkeryTableChangeMessage {

    public boolean pass;

    public SpelunkeryTableChangeMessage(boolean pass) {
        this.pass = pass;
    }

    public SpelunkeryTableChangeMessage() {
    }

    public static SpelunkeryTableChangeMessage read(FriendlyByteBuf buf) {
        return new SpelunkeryTableChangeMessage(buf.readBoolean());
    }

    public static void write(SpelunkeryTableChangeMessage message, FriendlyByteBuf buf) {
        buf.writeBoolean(message.pass);
    }

    public static void handle(SpelunkeryTableChangeMessage message, Supplier<NetworkEvent.Context> context) {
        ((NetworkEvent.Context) context.get()).setPacketHandled(true);
        Player player = ((NetworkEvent.Context) context.get()).getSender();
        if (((NetworkEvent.Context) context.get()).getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            player = AlexsCaves.PROXY.getClientSidePlayer();
        }
        if (player != null && player.containerMenu instanceof SpelunkeryTableMenu tableMenu) {
            tableMenu.onMessageFromScreen(player, message.pass);
        }
    }
}