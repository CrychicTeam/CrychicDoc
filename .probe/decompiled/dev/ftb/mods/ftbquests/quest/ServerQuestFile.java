package dev.ftb.mods.ftbquests.quest;

import com.mojang.util.UUIDTypeAdapter;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import dev.ftb.mods.ftblibrary.snbt.SNBT;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import dev.ftb.mods.ftbquests.FTBQuests;
import dev.ftb.mods.ftbquests.events.QuestProgressEventData;
import dev.ftb.mods.ftbquests.integration.PermissionsHelper;
import dev.ftb.mods.ftbquests.net.CreateOtherTeamDataMessage;
import dev.ftb.mods.ftbquests.net.DeleteObjectResponseMessage;
import dev.ftb.mods.ftbquests.net.MoveChapterGroupResponseMessage;
import dev.ftb.mods.ftbquests.net.SyncEditorPermissionMessage;
import dev.ftb.mods.ftbquests.net.SyncQuestsMessage;
import dev.ftb.mods.ftbquests.net.SyncTeamDataMessage;
import dev.ftb.mods.ftbquests.net.TeamDataChangedMessage;
import dev.ftb.mods.ftbquests.net.TeamDataUpdate;
import dev.ftb.mods.ftbquests.quest.reward.RewardType;
import dev.ftb.mods.ftbquests.quest.reward.RewardTypes;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import dev.ftb.mods.ftbquests.quest.task.TaskTypes;
import dev.ftb.mods.ftbquests.util.FTBQuestsInventoryListener;
import dev.ftb.mods.ftbquests.util.FileUtils;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.api.event.PlayerChangedTeamEvent;
import dev.ftb.mods.ftbteams.api.event.PlayerLoggedInAfterTeamEvent;
import dev.ftb.mods.ftbteams.api.event.TeamCreatedEvent;
import dev.ftb.mods.ftbteams.data.PartyTeam;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.LevelResource;

public class ServerQuestFile extends BaseQuestFile {

    public static final LevelResource FTBQUESTS_DATA = new LevelResource("ftbquests");

    public static ServerQuestFile INSTANCE;

    public final MinecraftServer server;

    private boolean shouldSave;

    private boolean isLoading;

    private Path folder;

    private ServerPlayer currentPlayer = null;

    public ServerQuestFile(MinecraftServer s) {
        this.server = s;
        this.shouldSave = false;
        this.isLoading = false;
        int taskTypeId = 0;
        for (TaskType type : TaskTypes.TYPES.values()) {
            type.internalId = ++taskTypeId;
            this.taskTypeIds.put(type.internalId, type);
        }
        int rewardTypeId = 0;
        for (RewardType type : RewardTypes.TYPES.values()) {
            type.intId = ++rewardTypeId;
            this.rewardTypeIds.put(type.intId, type);
        }
    }

    public void load() {
        this.folder = Platform.getConfigFolder().resolve("ftbquests/quests");
        if (Files.exists(this.folder, new LinkOption[0])) {
            FTBQuests.LOGGER.info("Loading quests from " + this.folder);
            this.isLoading = true;
            this.readDataFull(this.folder);
            this.isLoading = false;
        }
        Path path = this.server.getWorldPath(FTBQUESTS_DATA);
        if (Files.exists(path, new LinkOption[0])) {
            try {
                Stream<Path> s = Files.list(path);
                try {
                    s.filter(p -> p.getFileName().toString().contains("-") && p.getFileName().toString().endsWith(".snbt")).forEach(path1 -> {
                        SNBTCompoundTag nbt = SNBT.read(path1);
                        if (nbt != null) {
                            try {
                                UUID uuid = UUIDTypeAdapter.fromString(nbt.m_128461_("uuid"));
                                TeamData data = new TeamData(uuid, this);
                                this.addData(data, true);
                                data.deserializeNBT(nbt);
                            } catch (Exception var5x) {
                                var5x.printStackTrace();
                            }
                        }
                    });
                } catch (Throwable var6) {
                    if (s != null) {
                        try {
                            s.close();
                        } catch (Throwable var5) {
                            var6.addSuppressed(var5);
                        }
                    }
                    throw var6;
                }
                if (s != null) {
                    s.close();
                }
            } catch (Exception var7) {
                var7.printStackTrace();
            }
        }
    }

    @Override
    public Env getSide() {
        return Env.SERVER;
    }

    @Override
    public boolean isLoading() {
        return this.isLoading;
    }

    @Override
    public Path getFolder() {
        return this.folder;
    }

    @Override
    public void deleteObject(long id) {
        QuestObjectBase object = this.getBase(id);
        if (object != null) {
            object.deleteChildren();
            object.deleteSelf();
            this.refreshIDMap();
            this.markDirty();
            object.getPath().ifPresent(path -> FileUtils.delete(this.getFolder().resolve(path).toFile()));
        }
        new DeleteObjectResponseMessage(id).sendToAll(this.server);
    }

    @Override
    public void markDirty() {
        this.shouldSave = true;
    }

    public void saveNow() {
        if (this.shouldSave) {
            this.writeDataFull(this.getFolder());
            this.shouldSave = false;
        }
        this.getAllTeamData().forEach(TeamData::saveIfChanged);
    }

    public void unload() {
        this.saveNow();
        this.deleteChildren();
        this.deleteSelf();
    }

    public ServerPlayer getCurrentPlayer() {
        return this.currentPlayer;
    }

    public void withPlayerContext(ServerPlayer player, Runnable toDo) {
        this.currentPlayer = player;
        try {
            toDo.run();
        } finally {
            this.currentPlayer = null;
        }
    }

    public void playerLoggedIn(PlayerLoggedInAfterTeamEvent event) {
        ServerPlayer player = event.getPlayer();
        TeamData data = this.getOrCreateTeamData(event.getTeam());
        new SyncQuestsMessage(this).sendTo(player);
        new SyncEditorPermissionMessage(PermissionsHelper.hasEditorPermission(player, false)).sendTo(player);
        player.f_36095_.m_38893_(new FTBQuestsInventoryListener(player));
        if (!data.isLocked()) {
            this.withPlayerContext(player, () -> this.forAllQuests(quest -> {
                if (!data.isCompleted(quest) && quest.isCompletedRaw(data)) {
                    quest.onCompleted(new QuestProgressEventData<>(new Date(), data, quest, data.getOnlineMembers(), Collections.singletonList(player)));
                }
                data.checkAutoCompletion(quest);
                if (data.canStartTasks(quest)) {
                    quest.getTasks().stream().filter(Task::checkOnLogin).forEach(task -> task.submitTask(data, player));
                }
            }));
        }
    }

    public void teamCreated(TeamCreatedEvent event) {
        UUID id = event.getTeam().getId();
        TeamData data = (TeamData) this.teamDataMap.computeIfAbsent(id, k -> {
            TeamData newTeamData = new TeamData(id, this);
            newTeamData.markDirty();
            return newTeamData;
        });
        data.setName(event.getTeam().getShortName());
        this.addData(data, false);
        if (event.getTeam() instanceof PartyTeam) {
            FTBTeamsAPI.api().getManager().getPlayerTeamForPlayerID(event.getCreator().m_20148_()).ifPresent(playerTeam -> {
                TeamData oldTeamData = this.getOrCreateTeamData(playerTeam);
                data.copyData(oldTeamData);
            });
        }
        TeamDataUpdate self = new TeamDataUpdate(data);
        new CreateOtherTeamDataMessage(self).sendToAll(this.server);
    }

    public void playerChangedTeam(PlayerChangedTeamEvent event) {
        event.getPreviousTeam().ifPresent(prevTeam -> {
            Team curTeam = event.getTeam();
            TeamData oldTeamData = this.getOrCreateTeamData(prevTeam);
            TeamData newTeamData = this.getOrCreateTeamData(curTeam);
            if (prevTeam.isPlayerTeam() && curTeam.isPartyTeam() && !curTeam.getOwner().equals(event.getPlayerId())) {
                newTeamData.mergeData(oldTeamData);
            } else if (prevTeam.isPartyTeam() && curTeam.isPlayerTeam()) {
                newTeamData.mergeClaimedRewards(oldTeamData);
            }
            new TeamDataChangedMessage(new TeamDataUpdate(oldTeamData), new TeamDataUpdate(newTeamData)).sendToAll(this.server);
            new SyncTeamDataMessage(newTeamData, true).sendTo(curTeam.getOnlineMembers());
        });
    }

    @Override
    public boolean isPlayerOnTeam(Player player, TeamData teamData) {
        return (Boolean) FTBTeamsAPI.api().getManager().getTeamForPlayerID(player.m_20148_()).map(team -> team.getTeamId().equals(teamData.getTeamId())).orElse(false);
    }

    @Override
    public boolean moveChapterGroup(long id, boolean movingUp) {
        if (super.moveChapterGroup(id, movingUp)) {
            this.markDirty();
            this.clearCachedData();
            new MoveChapterGroupResponseMessage(id, movingUp).sendToAll(this.server);
            return true;
        } else {
            return false;
        }
    }
}