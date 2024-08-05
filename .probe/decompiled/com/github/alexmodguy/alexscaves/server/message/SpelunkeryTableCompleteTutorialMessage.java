package com.github.alexmodguy.alexscaves.server.message;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class SpelunkeryTableCompleteTutorialMessage {

    public boolean completedTutorial;

    public SpelunkeryTableCompleteTutorialMessage(boolean completedTutorial) {
        this.completedTutorial = completedTutorial;
    }

    public SpelunkeryTableCompleteTutorialMessage() {
    }

    public static SpelunkeryTableCompleteTutorialMessage read(FriendlyByteBuf buf) {
        return new SpelunkeryTableCompleteTutorialMessage(buf.readBoolean());
    }

    public static void write(SpelunkeryTableCompleteTutorialMessage message, FriendlyByteBuf buf) {
        buf.writeBoolean(message.completedTutorial);
    }

    public static void handle(SpelunkeryTableCompleteTutorialMessage message, Supplier<NetworkEvent.Context> context) {
        ((NetworkEvent.Context) context.get()).setPacketHandled(true);
        Player player = ((NetworkEvent.Context) context.get()).getSender();
        if (((NetworkEvent.Context) context.get()).getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            player = AlexsCaves.PROXY.getClientSidePlayer();
        }
        if (player != null) {
            AlexsCaves.PROXY.setSpelunkeryTutorialComplete(message.completedTutorial);
        }
    }
}