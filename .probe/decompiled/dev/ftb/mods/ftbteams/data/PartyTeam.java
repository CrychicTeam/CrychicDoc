package dev.ftb.mods.ftbteams.data;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.api.TeamRank;
import dev.ftb.mods.ftbteams.api.event.PlayerTransferredTeamOwnershipEvent;
import dev.ftb.mods.ftbteams.api.event.TeamAllyEvent;
import dev.ftb.mods.ftbteams.api.event.TeamEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

public class PartyTeam extends AbstractTeam {

    UUID owner = Util.NIL_UUID;

    public PartyTeam(TeamManagerImpl manager, UUID id) {
        super(manager, id);
    }

    @Override
    public TeamType getType() {
        return TeamType.PARTY;
    }

    @Override
    protected void serializeExtraNBT(CompoundTag tag) {
        tag.putString("owner", this.owner.toString());
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        super.deserializeNBT(tag);
        this.owner = UUID.fromString(tag.getString("owner"));
    }

    @Override
    public TeamRank getRankForPlayer(UUID playerId) {
        return this.owner.equals(playerId) ? TeamRank.OWNER : super.getRankForPlayer(playerId);
    }

    public boolean isOwner(UUID profile) {
        return this.owner.equals(profile);
    }

    @Override
    public UUID getOwner() {
        return this.owner;
    }

    @Override
    public boolean isPartyTeam() {
        return true;
    }

    public int join(ServerPlayer player) throws CommandSyntaxException {
        Team oldTeam = (Team) this.manager.getTeamForPlayer(player).orElseThrow(() -> TeamArgument.TEAM_NOT_FOUND.create(player.m_20148_()));
        if (oldTeam instanceof PlayerTeam playerTeam) {
            UUID id = player.m_20148_();
            playerTeam.setEffectiveTeam(this);
            this.ranks.put(id, TeamRank.MEMBER);
            this.sendMessage(Util.NIL_UUID, Component.translatable("ftbteams.message.joined", player.m_7755_()).withStyle(ChatFormatting.GREEN));
            this.markDirty();
            playerTeam.ranks.remove(id);
            playerTeam.markDirty();
            playerTeam.updatePresence();
            this.manager.syncToAll(this, oldTeam);
            this.onPlayerChangeTeam(oldTeam, id, player, false);
            return 1;
        } else {
            throw TeamArgument.ALREADY_IN_PARTY.create();
        }
    }

    public int invite(ServerPlayer inviter, Collection<GameProfile> profiles) throws CommandSyntaxException {
        if (!FTBTUtils.canPlayerUseCommand(inviter, "ftbteams.party.invite")) {
            throw TeamArgument.NO_PERMISSION.create();
        } else {
            for (GameProfile profile : profiles) {
                FTBTeamsAPI.api().getManager().getTeamForPlayerID(profile.getId()).ifPresent(team -> {
                    if (!(team instanceof PartyTeam)) {
                        this.ranks.put(profile.getId(), TeamRank.INVITED);
                        this.markDirty();
                        this.sendMessage(inviter.m_20148_(), Component.translatable("ftbteams.message.invited", Component.literal(profile.getName()).withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GREEN));
                        ServerPlayer invitee = FTBTUtils.getPlayerByUUID(this.manager.getServer(), profile.getId());
                        if (invitee != null && !invitee.m_20148_().equals(inviter.m_20148_())) {
                            invitee.displayClientMessage(Component.translatable("ftbteams.message.invite_sent", inviter.m_7755_().copy().withStyle(ChatFormatting.YELLOW)), false);
                            Component acceptButton = this.makeInviteButton("ftbteams.accept", ChatFormatting.GREEN, "/ftbteams party join " + this.getShortName());
                            Component declineButton = this.makeInviteButton("ftbteams.decline", ChatFormatting.RED, "/ftbteams party decline " + this.getShortName());
                            invitee.displayClientMessage(Component.literal("[").append(acceptButton).append("] [").append(declineButton).append("]"), false);
                        }
                    }
                });
            }
            return 1;
        }
    }

    private Component makeInviteButton(String xlate, ChatFormatting color, String command) {
        return Component.translatable(xlate).withStyle(Style.EMPTY.withColor(color).withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)));
    }

    public int kick(CommandSourceStack from, Collection<GameProfile> players) throws CommandSyntaxException {
        for (GameProfile player : players) {
            UUID id = player.getId();
            Team oldTeam = (Team) this.manager.getTeamForPlayerID(id).orElseThrow(TeamArgument.NOT_IN_PARTY::create);
            if (oldTeam != this) {
                throw TeamArgument.NOT_IN_PARTY.create();
            }
            if (this.isOwner(id)) {
                throw TeamArgument.CANT_KICK_OWNER.create();
            }
            PlayerTeam team = this.manager.getPersonalTeamForPlayerID(id);
            team.setEffectiveTeam(team);
            ServerPlayer playerToKick = FTBTUtils.getPlayerByUUID(this.manager.getServer(), id);
            team.ranks.put(id, TeamRank.OWNER);
            UUID fromId = from.getPlayer() != null ? from.getPlayer().m_20148_() : Util.NIL_UUID;
            this.sendMessage(fromId, Component.translatable("ftbteams.message.kicked", this.manager.getPlayerName(id).copy().withStyle(ChatFormatting.YELLOW), this.getName()).withStyle(ChatFormatting.GOLD));
            team.markDirty();
            this.ranks.remove(id);
            this.markDirty();
            team.updatePresence();
            this.manager.syncToAll(this, team);
            if (playerToKick != null) {
                playerToKick.displayClientMessage(Component.translatable("ftbteams.message.kicked", playerToKick.m_7755_().copy().withStyle(ChatFormatting.YELLOW), this.getName().copy().withStyle(ChatFormatting.AQUA)), false);
                this.updateCommands(playerToKick);
            }
            team.onPlayerChangeTeam(this, id, playerToKick, false);
        }
        return 1;
    }

    public int promote(ServerPlayer from, Collection<GameProfile> players) throws CommandSyntaxException {
        boolean changesMade = false;
        for (GameProfile player : players) {
            UUID id = player.getId();
            if (this.getRankForPlayer(id) != TeamRank.MEMBER) {
                throw TeamArgument.NOT_MEMBER.create(this.manager.getPlayerName(id), this.getName());
            }
            this.ranks.put(id, TeamRank.OFFICER);
            Component playerName = this.manager.getPlayerName(id).copy().withStyle(ChatFormatting.YELLOW);
            this.sendMessage(from.m_20148_(), Component.translatable("ftbteams.message.promoted", playerName).withStyle(ChatFormatting.GREEN));
            changesMade = true;
        }
        if (changesMade) {
            this.markDirty();
            this.manager.syncToAll(this);
        }
        return 1;
    }

    public int demote(ServerPlayer from, Collection<GameProfile> players) throws CommandSyntaxException {
        boolean changesMade = false;
        for (GameProfile player : players) {
            UUID id = player.getId();
            if (this.getRankForPlayer(id) != TeamRank.OFFICER) {
                throw TeamArgument.NOT_OFFICER.create(this.manager.getPlayerName(id), this.getName());
            }
            this.ranks.put(id, TeamRank.MEMBER);
            Component playerName = this.manager.getPlayerName(id).copy().withStyle(ChatFormatting.YELLOW);
            this.sendMessage(from.m_20148_(), Component.translatable("ftbteams.message.demoted", playerName).withStyle(ChatFormatting.GOLD));
            changesMade = true;
        }
        if (changesMade) {
            this.markDirty();
            this.manager.syncToAll(this);
        }
        return 1;
    }

    public int transferOwnership(CommandSourceStack from, Collection<GameProfile> toProfiles) throws CommandSyntaxException {
        return this.transferOwnership(from, (GameProfile) toProfiles.stream().findFirst().orElseThrow());
    }

    public int transferOwnership(CommandSourceStack from, GameProfile toProfile) throws CommandSyntaxException {
        UUID newOwnerID = toProfile.getId();
        if (!this.getMembers().contains(newOwnerID)) {
            throw TeamArgument.NOT_MEMBER.create(toProfile.toString(), this.getName());
        } else if (this.owner.equals(newOwnerID)) {
            from.sendSystemMessage(Component.literal("Already owner!").withStyle(ChatFormatting.RED));
            return 0;
        } else {
            this.ranks.put(this.owner, TeamRank.OFFICER);
            this.owner = newOwnerID;
            this.ranks.put(this.owner, TeamRank.OWNER);
            this.markDirty();
            ServerPlayer fromPlayer = from.getPlayer();
            if (fromPlayer != null) {
                this.updateCommands(fromPlayer);
            }
            ServerPlayer toPlayer = from.getServer().getPlayerList().getPlayer(newOwnerID);
            if (toPlayer != null) {
                TeamEvent.OWNERSHIP_TRANSFERRED.invoker().accept(new PlayerTransferredTeamOwnershipEvent(this, fromPlayer, toPlayer));
                this.updateCommands(toPlayer);
            } else {
                TeamEvent.OWNERSHIP_TRANSFERRED.invoker().accept(new PlayerTransferredTeamOwnershipEvent(this, fromPlayer, toProfile));
            }
            UUID fromId = fromPlayer == null ? Util.NIL_UUID : fromPlayer.m_20148_();
            Component msg = Component.translatable("ftbteams.message.transfer_owner", Component.literal(toProfile.getName()).withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GREEN);
            this.sendMessage(fromId, msg);
            if (fromPlayer == null) {
                from.sendSystemMessage(msg);
            }
            this.manager.syncToAll(this);
            return 1;
        }
    }

    public int leave(UUID id) throws CommandSyntaxException {
        ServerPlayer player = FTBTeamsAPI.api().getManager().getServer().getPlayerList().getPlayer(id);
        if (this.isOwner(id) && this.getMembers().size() > 1) {
            throw TeamArgument.OWNER_CANT_LEAVE.create();
        } else {
            PlayerTeam playerTeam = this.manager.getPersonalTeamForPlayerID(id);
            playerTeam.setEffectiveTeam(playerTeam);
            playerTeam.ranks.put(id, TeamRank.OWNER);
            String playerName = player == null ? id.toString() : player.m_36316_().getName();
            this.sendMessage(Util.NIL_UUID, Component.translatable("ftbteams.message.left_party", Component.literal(playerName).withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GOLD));
            playerTeam.markDirty();
            this.ranks.remove(id);
            this.manager.markDirty();
            boolean deletingTeam = false;
            if (this.getMembers().isEmpty()) {
                deletingTeam = true;
                this.invalidateTeam();
                this.manager.deleteTeam(this);
                this.manager.saveNow();
                this.manager.tryDeleteTeamFile(this.getId() + ".snbt", "party");
            }
            playerTeam.updatePresence();
            playerTeam.onPlayerChangeTeam(this, id, player, deletingTeam);
            this.manager.syncToAll(this, playerTeam);
            return 1;
        }
    }

    public int addAlly(CommandSourceStack source, Collection<GameProfile> players) throws CommandSyntaxException {
        if (source.getPlayer() != null && !FTBTUtils.canPlayerUseCommand(source.getPlayer(), "ftbteams.party.allies.add")) {
            throw TeamArgument.NO_PERMISSION.create();
        } else {
            UUID from = source.getEntity() == null ? Util.NIL_UUID : source.getEntity().getUUID();
            List<GameProfile> addedPlayers = new ArrayList();
            for (GameProfile player : players) {
                UUID id = player.getId();
                if (!this.isAllyOrBetter(id)) {
                    this.ranks.put(id, TeamRank.ALLY);
                    this.sendMessage(from, Component.translatable("ftbteams.message.add_ally", this.manager.getPlayerName(id).copy().withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GREEN));
                    addedPlayers.add(player);
                    ServerPlayer invitedPlayer = this.manager.getServer().getPlayerList().getPlayer(id);
                    if (invitedPlayer != null) {
                        invitedPlayer.displayClientMessage(Component.translatable("ftbteams.message.now_allied", this.getDisplayName()).withStyle(ChatFormatting.GREEN), false);
                    }
                }
            }
            if (!addedPlayers.isEmpty()) {
                this.markDirty();
                this.manager.syncToAll(this);
                TeamEvent.ADD_ALLY.invoker().accept(new TeamAllyEvent(this, addedPlayers, true));
                return 1;
            } else {
                return 0;
            }
        }
    }

    public int removeAlly(CommandSourceStack source, Collection<GameProfile> players) throws CommandSyntaxException {
        UUID from = source.getEntity() == null ? Util.NIL_UUID : source.getEntity().getUUID();
        List<GameProfile> removedPlayers = new ArrayList();
        for (GameProfile player : players) {
            UUID id = player.getId();
            if (this.isAllyOrBetter(id) && !this.isMember(id)) {
                this.ranks.remove(id);
                this.sendMessage(from, Component.translatable("ftbteams.message.remove_ally", this.manager.getPlayerName(id).copy().withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GOLD));
                removedPlayers.add(player);
                ServerPlayer removedPlayer = this.manager.getServer().getPlayerList().getPlayer(id);
                if (removedPlayer != null) {
                    removedPlayer.displayClientMessage(Component.translatable("ftbteams.message.no_longer_allied", this.getDisplayName()).withStyle(ChatFormatting.GOLD), false);
                }
            }
        }
        if (!removedPlayers.isEmpty()) {
            this.markDirty();
            this.manager.syncToAll(this);
            TeamEvent.REMOVE_ALLY.invoker().accept(new TeamAllyEvent(this, removedPlayers, false));
            return 1;
        } else {
            return 0;
        }
    }

    public int listAllies(CommandSourceStack source) {
        source.sendSuccess(() -> Component.literal("Allies:"), false);
        boolean any = false;
        for (Entry<UUID, TeamRank> entry : this.getPlayersByRank(TeamRank.ALLY).entrySet()) {
            if (!((TeamRank) entry.getValue()).isAtLeast(TeamRank.MEMBER)) {
                source.sendSuccess(() -> this.manager.getPlayerName((UUID) entry.getKey()), false);
                any = true;
            }
        }
        if (!any) {
            source.sendSuccess(() -> Component.literal("None"), false);
        }
        return 1;
    }

    public int forceDisband(CommandSourceStack from) throws CommandSyntaxException {
        Set<UUID> members = new HashSet(this.getMembers());
        members.remove(this.owner);
        this.kick(from, members.stream().map(id -> new GameProfile(id, null)).toList());
        this.leave(this.owner);
        from.sendSuccess(() -> Component.translatable("ftbteams.message.team_disbanded", this.getName(), this.getId()).withStyle(ChatFormatting.GOLD), false);
        return 1;
    }
}