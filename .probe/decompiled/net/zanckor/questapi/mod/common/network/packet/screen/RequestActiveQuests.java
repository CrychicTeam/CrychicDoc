package net.zanckor.questapi.mod.common.network.packet.screen;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.zanckor.questapi.mod.common.network.SendQuestPacket;
import net.zanckor.questapi.mod.common.network.packet.quest.ActiveQuestList;

public class RequestActiveQuests {

    public RequestActiveQuests() {
    }

    public RequestActiveQuests(FriendlyByteBuf buffer) {
    }

    public void encodeBuffer(FriendlyByteBuf buffer) {
    }

    public static void handler(RequestActiveQuests msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            Player player = ((NetworkEvent.Context) ctx.get()).getSender();
            SendQuestPacket.TO_CLIENT(player, new ActiveQuestList(player.m_20148_()));
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}