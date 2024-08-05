package dev.ftb.mods.ftbteams.data;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.ftb.mods.ftblibrary.snbt.SNBT;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import dev.ftb.mods.ftblibrary.util.TextComponentUtils;
import dev.ftb.mods.ftbteams.FTBTeams;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.api.TeamMessage;
import dev.ftb.mods.ftbteams.api.TeamRank;
import dev.ftb.mods.ftbteams.api.event.PlayerChangedTeamEvent;
import dev.ftb.mods.ftbteams.api.event.PlayerJoinedPartyTeamEvent;
import dev.ftb.mods.ftbteams.api.event.PlayerLeftPartyTeamEvent;
import dev.ftb.mods.ftbteams.api.event.TeamCreatedEvent;
import dev.ftb.mods.ftbteams.api.event.TeamEvent;
import dev.ftb.mods.ftbteams.api.event.TeamInfoEvent;
import dev.ftb.mods.ftbteams.api.event.TeamPropertiesChangedEvent;
import dev.ftb.mods.ftbteams.api.property.TeamProperty;
import dev.ftb.mods.ftbteams.api.property.TeamPropertyCollection;
import dev.ftb.mods.ftbteams.net.SendMessageResponseMessage;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractTeam extends AbstractTeamBase {

    protected final TeamManagerImpl manager;

    private boolean shouldSave;

    public AbstractTeam(TeamManagerImpl manager, UUID id) {
        super(id);
        this.manager = manager;
        this.properties.collectProperties();
    }

    @Override
    public void markDirty() {
        this.shouldSave = true;
        this.manager.nameMap = null;
    }

    public List<ServerPlayer> getOnlineRanked(TeamRank rank) {
        List<ServerPlayer> list = new ArrayList();
        for (UUID id : this.getPlayersByRank(rank).keySet()) {
            ServerPlayer player = FTBTUtils.getPlayerByUUID(this.manager.getServer(), id);
            if (player != null) {
                list.add(player);
            }
        }
        return list;
    }

    public List<ServerPlayer> getOnlineMembers() {
        return this.getOnlineRanked(TeamRank.MEMBER);
    }

    void onCreated(@Nullable ServerPlayer player, @NotNull UUID playerId) {
        if (player != null) {
            TeamEvent.CREATED.invoker().accept(new TeamCreatedEvent(this, player, playerId));
        }
        this.markDirty();
        this.manager.markDirty();
        this.manager.saveNow();
    }

    void updateCommands(ServerPlayer player) {
        player.m_20194_().getPlayerList().sendPlayerPermissionLevel(player);
    }

    void onPlayerChangeTeam(@Nullable Team prev, UUID player, @Nullable ServerPlayer p, boolean deleted) {
        TeamEvent.PLAYER_CHANGED.invoker().accept(new PlayerChangedTeamEvent(this, prev, player, p));
        if (prev instanceof PartyTeam && this instanceof PlayerTeam) {
            TeamEvent.PLAYER_LEFT_PARTY.invoker().accept(new PlayerLeftPartyTeamEvent(prev, (PlayerTeam) this, player, p, deleted));
        } else if (prev instanceof PlayerTeam && p != null) {
            TeamEvent.PLAYER_JOINED_PARTY.invoker().accept(new PlayerJoinedPartyTeamEvent(this, prev, p));
        }
        if (deleted && prev != null) {
            TeamEvent.DELETED.invoker().accept(new TeamEvent(prev));
        }
        if (p != null) {
            this.updateCommands(p);
        }
    }

    public SNBTCompoundTag serializeNBT() {
        SNBTCompoundTag tag = new SNBTCompoundTag();
        tag.m_128359_("id", this.getId().toString());
        tag.m_128359_("type", this.getType().getSerializedName());
        this.serializeExtraNBT(tag);
        SNBTCompoundTag ranksNBT = new SNBTCompoundTag();
        for (Entry<UUID, TeamRank> entry : this.ranks.entrySet()) {
            ranksNBT.m_128359_(((UUID) entry.getKey()).toString(), ((TeamRank) entry.getValue()).getSerializedName());
        }
        tag.m_128365_("ranks", ranksNBT);
        tag.m_128365_("properties", this.properties.write(new SNBTCompoundTag()));
        ListTag messageHistoryTag = new ListTag();
        for (TeamMessage msg : this.getMessageHistory()) {
            messageHistoryTag.add(TeamMessageImpl.toNBT(msg));
        }
        tag.m_128365_("message_history", messageHistoryTag);
        TeamEvent.SAVED.invoker().accept(new TeamEvent(this));
        tag.m_128365_("extra", this.extraData);
        return tag;
    }

    protected void serializeExtraNBT(CompoundTag tag) {
    }

    public void deserializeNBT(CompoundTag tag) {
        this.ranks.clear();
        CompoundTag ranksNBT = tag.getCompound("ranks");
        for (String s : ranksNBT.getAllKeys()) {
            this.ranks.put(UUID.fromString(s), TeamRank.NAME_MAP.get(ranksNBT.getString(s)));
        }
        this.properties.read(tag.getCompound("properties"));
        this.extraData = tag.getCompound("extra");
        this.messageHistory.clear();
        ListTag messageHistoryTag = tag.getList("message_history", 10);
        for (int i = 0; i < messageHistoryTag.size(); i++) {
            this.addMessage(TeamMessageImpl.fromNBT(messageHistoryTag.getCompound(i)));
        }
        TeamEvent.LOADED.invoker().accept(new TeamEvent(this));
    }

    public <T> int settings(CommandSourceStack source, TeamProperty<T> key, String value) {
        MutableComponent keyc = Component.translatable(key.getTranslationKey("ftbteamsconfig")).withStyle(ChatFormatting.YELLOW);
        if (value.isEmpty()) {
            Component valuec = Component.literal(key.toString(this.getProperty(key))).withStyle(ChatFormatting.AQUA);
            source.sendSuccess(() -> keyc.append(" is set to ").append(valuec), true);
        } else {
            Optional<T> optional = key.fromString(value);
            if (!optional.isPresent()) {
                source.sendFailure(Component.literal("Failed to parse value!"));
                return 0;
            }
            TeamPropertyCollection old = this.properties.copy();
            this.setProperty(key, (T) optional.get());
            Component valuec = Component.literal(value).withStyle(ChatFormatting.AQUA);
            source.sendSuccess(() -> Component.literal("Set ").append(keyc).append(" to ").append(valuec), true);
            TeamEvent.PROPERTIES_CHANGED.invoker().accept(new TeamPropertiesChangedEvent(this, old));
        }
        return 1;
    }

    public int declineInvitation(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        if (this.getRankForPlayer(player.m_20148_()) == TeamRank.INVITED) {
            this.ranks.put(player.m_20148_(), TeamRank.ALLY);
            source.sendSuccess(() -> Component.translatable("ftbteams.message.declined"), true);
            this.markDirty();
            this.manager.syncToAll(this);
        } else {
            FTBTeams.LOGGER.warn("ignore invitation decline for player {} to team {} (not invited)", player.m_20148_(), this.getId());
        }
        return 1;
    }

    @Override
    public List<Component> getTeamInfo() {
        List<Component> res = new ArrayList();
        res.add(Component.literal("== ").append(this.getName()).append(" ==").withStyle(ChatFormatting.BOLD));
        res.add(Component.translatable("ftbteams.info.id", Component.literal(this.getId().toString()).withStyle(ChatFormatting.YELLOW)));
        res.add(Component.translatable("ftbteams.info.short_id", Component.literal(this.getShortName()).withStyle(ChatFormatting.YELLOW)).append(" [" + this.getType().getSerializedName() + "]"));
        res.add(this.getOwner().equals(Util.NIL_UUID) ? Component.translatable("ftbteams.info.owner", Component.translatable("ftbteams.info.owner.none")) : Component.translatable("ftbteams.info.owner", this.manager.getPlayerName(this.getOwner())));
        res.add(Component.translatable("ftbteams.info.members"));
        if (this.getMembers().isEmpty()) {
            res.add(Component.literal("- ").append(Component.translatable("ftbteams.info.members.none")));
        } else {
            for (UUID member : this.getMembers()) {
                res.add(Component.literal("- ").append(this.manager.getPlayerName(member)));
            }
        }
        return res;
    }

    public int info(CommandSourceStack source) throws CommandSyntaxException {
        source.sendSuccess(Component::m_237119_, false);
        MutableComponent infoComponent = Component.literal("");
        infoComponent.getStyle().withBold(true);
        infoComponent.append("== ");
        infoComponent.append(this.getName());
        infoComponent.append(" ==");
        source.sendSuccess(() -> infoComponent, false);
        source.sendSuccess(() -> Component.translatable("ftbteams.info.id", Component.literal(this.getId().toString()).withStyle(ChatFormatting.YELLOW)), false);
        source.sendSuccess(() -> Component.translatable("ftbteams.info.short_id", Component.literal(this.getShortName()).withStyle(ChatFormatting.YELLOW)).append(" [" + this.getType().getSerializedName() + "]"), false);
        if (this.getOwner().equals(Util.NIL_UUID)) {
            source.sendSuccess(() -> Component.translatable("ftbteams.info.owner", Component.translatable("ftbteams.info.owner.none")), false);
        } else {
            source.sendSuccess(() -> Component.translatable("ftbteams.info.owner", this.manager.getPlayerName(this.getOwner())), false);
        }
        source.sendSuccess(() -> Component.translatable("ftbteams.info.members"), false);
        if (this.getMembers().isEmpty()) {
            source.sendSuccess(() -> Component.literal("- ").append(Component.translatable("ftbteams.info.members.none")), false);
        } else {
            for (UUID member : this.getMembers()) {
                source.sendSuccess(() -> Component.literal("- ").append(this.manager.getPlayerName(member)), false);
            }
        }
        TeamEvent.INFO.invoker().accept(new TeamInfoEvent(this, source));
        return 1;
    }

    @Override
    public UUID getOwner() {
        return Util.NIL_UUID;
    }

    @Override
    public void sendMessage(UUID senderId, String message) {
        this.sendMessage(senderId, TextComponentUtils.withLinks(message));
    }

    void sendMessage(UUID from, Component text) {
        this.addMessage(FTBTeamsAPI.api().createMessage(from, text));
        MutableComponent component = Component.literal("<");
        component.append(this.manager.getPlayerName(from));
        component.append(" @");
        component.append(this.getName());
        component.append("> ");
        component.append(text);
        for (ServerPlayer p : this.getOnlineMembers()) {
            p.displayClientMessage(component, false);
            new SendMessageResponseMessage(from, text).sendTo(p);
        }
        this.markDirty();
    }

    public void updatePropertiesFrom(TeamPropertyCollection newProperties) {
        TeamPropertyCollection oldProperties = this.properties.copy();
        this.properties.updateFrom(newProperties);
        TeamEvent.PROPERTIES_CHANGED.invoker().accept(new TeamPropertiesChangedEvent(this, oldProperties));
        this.markDirty();
    }

    void saveIfNeeded(Path directory) {
        if (this.shouldSave) {
            SNBT.write(directory.resolve(this.getType().getSerializedName() + "/" + this.getId() + ".snbt"), this.serializeNBT());
            this.shouldSave = false;
        }
    }
}