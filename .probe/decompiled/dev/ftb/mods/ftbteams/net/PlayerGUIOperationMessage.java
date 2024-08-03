package dev.ftb.mods.ftbteams.net;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbteams.FTBTeams;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.TeamRank;
import dev.ftb.mods.ftbteams.api.client.KnownClientPlayer;
import dev.ftb.mods.ftbteams.data.PartyTeam;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class PlayerGUIOperationMessage extends BaseC2SMessage {

    private final PlayerGUIOperationMessage.Operation op;

    private final List<UUID> targets;

    public PlayerGUIOperationMessage(PlayerGUIOperationMessage.Operation op, UUID target) {
        this.op = op;
        this.targets = List.of(target);
    }

    public PlayerGUIOperationMessage(PlayerGUIOperationMessage.Operation op, Collection<GameProfile> targets) {
        this.op = op;
        this.targets = targets.stream().map(GameProfile::getId).toList();
    }

    public PlayerGUIOperationMessage(FriendlyByteBuf buf) {
        this.op = buf.readEnum(PlayerGUIOperationMessage.Operation.class);
        this.targets = new ArrayList();
        int n = buf.readVarInt();
        for (int i = 0; i < n; i++) {
            this.targets.add(buf.readUUID());
        }
    }

    @Override
    public MessageType getType() {
        return FTBTeamsNet.PLAYER_GUI_OPERATION;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(this.op);
        buf.writeVarInt(this.targets.size());
        this.targets.forEach(buf::m_130077_);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
            UUID var4 = context.getPlayer().m_20148_();
            FTBTeamsAPI.api().getManager().getTeamForPlayerID(var4).ifPresent(team -> {
                if (team instanceof PartyTeam partyTeam) {
                    TeamRank senderRank = partyTeam.getRankForPlayer(serverPlayer.m_20148_());
                    this.targets.forEach(target -> this.processTarget(serverPlayer, senderRank, partyTeam, target));
                }
            });
        }
    }

    private void processTarget(ServerPlayer sourcePlayer, TeamRank senderRank, PartyTeam partyTeam, UUID targetId) {
        if (!this.op.requireSameTeam() || FTBTeamsAPI.api().getManager().arePlayersInSameTeam(sourcePlayer.m_20148_(), targetId)) {
            TeamRank targetRank = partyTeam.getRankForPlayer(targetId);
            FTBTeams.LOGGER.debug("received teams operation msg {} from {} (rank {}), team {}, target {} (rank {})", this.op, sourcePlayer.m_20148_(), senderRank, partyTeam.getName().getString(), targetId, targetRank);
            try {
                List<GameProfile> targetProfile = List.of(new GameProfile(targetId, null));
                switch(this.op) {
                    case KICK:
                        if (senderRank.getPower() > targetRank.getPower()) {
                            partyTeam.kick(sourcePlayer.m_20203_(), targetProfile);
                        }
                        break;
                    case PROMOTE:
                        if (senderRank.isAtLeast(TeamRank.OWNER) && targetRank.isAtLeast(TeamRank.MEMBER)) {
                            partyTeam.promote(sourcePlayer, targetProfile);
                        }
                        break;
                    case DEMOTE:
                        if (senderRank.isAtLeast(TeamRank.OWNER) && targetRank.isAtLeast(TeamRank.OFFICER)) {
                            partyTeam.demote(sourcePlayer, targetProfile);
                        }
                        break;
                    case TRANSFER_OWNER:
                        if (senderRank.isAtLeast(TeamRank.OWNER)) {
                            ServerPlayer p = sourcePlayer.m_20194_().getPlayerList().getPlayer(targetId);
                            if (p != null) {
                                partyTeam.transferOwnership(sourcePlayer.m_20203_(), p.m_36316_());
                            }
                        }
                        break;
                    case LEAVE:
                        partyTeam.leave(sourcePlayer.m_20148_());
                        break;
                    case INVITE:
                        if (senderRank.isAtLeast(TeamRank.OFFICER)) {
                            ServerPlayer p = sourcePlayer.m_20194_().getPlayerList().getPlayer(targetId);
                            if (p != null) {
                                partyTeam.invite(sourcePlayer, List.of(p.m_36316_()));
                            }
                        }
                        break;
                    case ADD_ALLY:
                        if (senderRank.isAtLeast(TeamRank.OFFICER) && targetRank.isAtLeast(TeamRank.NONE)) {
                            partyTeam.addAlly(sourcePlayer.m_20203_(), targetProfile);
                        }
                        break;
                    case REMOVE_ALLY:
                        if (senderRank.isAtLeast(TeamRank.OFFICER) && targetRank.isAtLeast(TeamRank.ALLY)) {
                            partyTeam.removeAlly(sourcePlayer.m_20203_(), targetProfile);
                        }
                }
            } catch (CommandSyntaxException var8) {
                sourcePlayer.displayClientMessage(Component.literal(var8.getMessage()).withStyle(ChatFormatting.RED), false);
            }
        }
    }

    public static enum Operation {

        PROMOTE(true),
        DEMOTE(true),
        LEAVE(true),
        KICK(true),
        TRANSFER_OWNER(true),
        INVITE(false),
        ADD_ALLY(false),
        REMOVE_ALLY(false);

        private final boolean requireSameTeam;

        private Operation(boolean requireSameTeam) {
            this.requireSameTeam = requireSameTeam;
        }

        boolean requireSameTeam() {
            return this.requireSameTeam;
        }

        public void sendMessage(KnownClientPlayer target) {
            new PlayerGUIOperationMessage(this, target.id()).sendToServer();
        }
    }
}