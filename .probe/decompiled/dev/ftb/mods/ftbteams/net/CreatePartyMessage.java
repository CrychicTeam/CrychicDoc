package dev.ftb.mods.ftbteams.net;

import com.mojang.authlib.GameProfile;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbteams.FTBTeamsAPIImpl;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.data.PlayerTeam;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class CreatePartyMessage extends BaseC2SMessage {

    private final String name;

    private final String description;

    private final int color;

    private final Set<GameProfile> invited;

    CreatePartyMessage(FriendlyByteBuf buffer) {
        this.name = buffer.readUtf(32767);
        this.description = buffer.readUtf(32767);
        this.color = buffer.readInt();
        int s = buffer.readVarInt();
        this.invited = new HashSet(s);
        for (int i = 0; i < s; i++) {
            this.invited.add(new GameProfile(buffer.readUUID(), buffer.readUtf(32767)));
        }
    }

    public CreatePartyMessage(String n, String d, int c, Set<GameProfile> i) {
        this.name = n;
        this.description = d;
        this.color = c;
        this.invited = i;
    }

    @Override
    public MessageType getType() {
        return FTBTeamsNet.CREATE_PARTY;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.name, 32767);
        buffer.writeUtf(this.description, 32767);
        buffer.writeInt(this.color);
        buffer.writeVarInt(this.invited.size());
        for (GameProfile p : this.invited) {
            buffer.writeUUID(p.getId());
            buffer.writeUtf(p.getName(), 32767);
        }
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        ServerPlayer player = (ServerPlayer) context.getPlayer();
        FTBTeamsAPI.api().getManager().getTeamForPlayer(player).ifPresent(team -> {
            if (FTBTeamsAPIImpl.INSTANCE.isPartyCreationFromAPIOnly()) {
                player.displayClientMessage(Component.translatable("ftbteams.party_api_only").withStyle(ChatFormatting.RED), false);
            } else if (team instanceof PlayerTeam playerTeam) {
                playerTeam.createParty(player.m_20148_(), player, this.name, this.description, this.color, this.invited);
            }
        });
    }
}