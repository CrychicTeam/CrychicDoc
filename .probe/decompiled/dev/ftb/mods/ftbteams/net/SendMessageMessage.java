package dev.ftb.mods.ftbteams.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class SendMessageMessage extends BaseC2SMessage {

    private final String text;

    SendMessageMessage(FriendlyByteBuf buffer) {
        this.text = buffer.readUtf(32767);
    }

    public SendMessageMessage(String s) {
        this.text = s.length() > 5000 ? s.substring(0, 5000) : s;
    }

    @Override
    public MessageType getType() {
        return FTBTeamsNet.SEND_MESSAGE;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.text, 32767);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        ServerPlayer player = (ServerPlayer) context.getPlayer();
        FTBTeamsAPI.api().getManager().getTeamForPlayer(player).ifPresent(team -> team.sendMessage(player.m_20148_(), this.text));
    }
}