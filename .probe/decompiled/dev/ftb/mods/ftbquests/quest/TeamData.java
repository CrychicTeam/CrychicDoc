package dev.ftb.mods.ftbquests.quest;

import com.mojang.util.UUIDTypeAdapter;
import dev.ftb.mods.ftblibrary.snbt.SNBT;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import dev.ftb.mods.ftbquests.FTBQuests;
import dev.ftb.mods.ftbquests.api.FTBQuestsAPI;
import dev.ftb.mods.ftbquests.events.QuestProgressEventData;
import dev.ftb.mods.ftbquests.net.ClaimRewardResponseMessage;
import dev.ftb.mods.ftbquests.net.ObjectCompletedMessage;
import dev.ftb.mods.ftbquests.net.ObjectCompletedResetMessage;
import dev.ftb.mods.ftbquests.net.ObjectStartedMessage;
import dev.ftb.mods.ftbquests.net.ObjectStartedResetMessage;
import dev.ftb.mods.ftbquests.net.ResetRewardMessage;
import dev.ftb.mods.ftbquests.net.SyncEditingModeMessage;
import dev.ftb.mods.ftbquests.net.SyncLockMessage;
import dev.ftb.mods.ftbquests.net.SyncRewardBlockingMessage;
import dev.ftb.mods.ftbquests.net.TogglePinnedResponseMessage;
import dev.ftb.mods.ftbquests.net.UpdateTaskProgressMessage;
import dev.ftb.mods.ftbquests.quest.reward.Reward;
import dev.ftb.mods.ftbquests.quest.reward.RewardAutoClaim;
import dev.ftb.mods.ftbquests.quest.reward.RewardClaimType;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.ftb.mods.ftbquests.util.FTBQuestsInventoryListener;
import dev.ftb.mods.ftbquests.util.QuestKey;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2LongMap.Entry;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class TeamData {

    public static final int VERSION = 1;

    public static final int AUTO_PIN_ID = 1;

    private static final byte BOOL_UNKNOWN = -1;

    private static final byte BOOL_FALSE = 0;

    private static final byte BOOL_TRUE = 1;

    private static final Comparator<it.unimi.dsi.fastutil.longs.Long2LongMap.Entry> LONG2LONG_COMPARATOR = (e1, e2) -> Long.compareUnsigned(e1.getLongValue(), e2.getLongValue());

    private static final Comparator<Entry<QuestKey>> OBJECT2LONG_COMPARATOR = (e1, e2) -> Long.compareUnsigned(e1.getLongValue(), e2.getLongValue());

    private final UUID teamId;

    private final BaseQuestFile file;

    private String name;

    private boolean shouldSave;

    private boolean locked;

    private boolean rewardsBlocked;

    private final Long2LongOpenHashMap taskProgress;

    private final Object2LongOpenHashMap<QuestKey> claimedRewards;

    private final Long2LongOpenHashMap started;

    private final Long2LongOpenHashMap completed;

    private final Object2ObjectOpenHashMap<UUID, TeamData.PerPlayerData> perPlayerData;

    private Long2ByteOpenHashMap areDependenciesCompleteCache;

    private Object2ByteOpenHashMap<QuestKey> unclaimedRewardsCache;

    public TeamData(UUID teamId, BaseQuestFile file) {
        this(teamId, file, "");
    }

    public TeamData(UUID teamId, BaseQuestFile file, String name) {
        this.teamId = teamId;
        this.file = file;
        this.name = name;
        this.shouldSave = false;
        this.taskProgress = new Long2LongOpenHashMap();
        this.taskProgress.defaultReturnValue(0L);
        this.claimedRewards = new Object2LongOpenHashMap();
        this.claimedRewards.defaultReturnValue(0L);
        this.started = new Long2LongOpenHashMap();
        this.started.defaultReturnValue(0L);
        this.completed = new Long2LongOpenHashMap();
        this.completed.defaultReturnValue(0L);
        this.perPlayerData = new Object2ObjectOpenHashMap();
    }

    public UUID getTeamId() {
        return this.teamId;
    }

    public BaseQuestFile getFile() {
        return this.file;
    }

    public static TeamData get(Player player) {
        return FTBQuestsAPI.api().getQuestFile(player.m_20193_().isClientSide()).getOrCreateTeamData(player);
    }

    public void markDirty() {
        this.shouldSave = true;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (!this.name.equals(name)) {
            this.name = name;
            this.markDirty();
        }
    }

    public void saveIfChanged() {
        if (this.shouldSave && this.file instanceof ServerQuestFile sqf) {
            Path path = sqf.server.getWorldPath(ServerQuestFile.FTBQUESTS_DATA);
            SNBT.write(path.resolve(this.teamId + ".snbt"), this.serializeNBT());
            this.shouldSave = false;
        }
    }

    public String toString() {
        return this.name.isEmpty() ? this.teamId.toString() : this.name;
    }

    public long getProgress(long taskId) {
        return this.taskProgress.get(taskId);
    }

    public long getProgress(Task task) {
        return this.getProgress(task.id);
    }

    public Optional<Date> getStartedTime(long questId) {
        long when = this.started.get(questId);
        return when == 0L ? Optional.empty() : Optional.of(new Date(when));
    }

    public boolean setStarted(long questId, @Nullable Date time) {
        if (!this.locked) {
            if (time == null) {
                if (this.started.remove(questId) >= 0L) {
                    this.clearCachedProgress();
                    this.markDirty();
                    if (this.file.isServerSide()) {
                        new ObjectStartedResetMessage(this.teamId, questId).sendTo(this.getOnlineMembers());
                    }
                    return true;
                }
            } else if (this.started.put(questId, time.getTime()) == 0L) {
                this.clearCachedProgress();
                this.markDirty();
                if (this.file.isServerSide()) {
                    new ObjectStartedMessage(this.teamId, questId).sendTo(this.getOnlineMembers());
                }
                return true;
            }
        }
        return false;
    }

    public Optional<Date> getCompletedTime(long questId) {
        long when = this.completed.get(questId);
        return when == 0L ? Optional.empty() : Optional.of(new Date(when));
    }

    public boolean setCompleted(long id, @Nullable Date time) {
        if (this.locked) {
            return false;
        } else {
            if (time == null) {
                if (this.completed.remove(id) >= 0L) {
                    this.clearCachedProgress();
                    this.markDirty();
                    if (this.file.isServerSide()) {
                        new ObjectCompletedResetMessage(this.teamId, id).sendTo(this.getOnlineMembers());
                    }
                    return true;
                }
            } else if (this.completed.put(id, time.getTime()) == 0L) {
                this.clearCachedProgress();
                this.markDirty();
                if (this.file.isServerSide()) {
                    new ObjectCompletedMessage(this.teamId, id).sendTo(this.getOnlineMembers());
                }
                return true;
            }
            return false;
        }
    }

    public Optional<Date> getRewardClaimTime(UUID player, Reward reward) {
        QuestKey key = QuestKey.forReward(player, reward);
        long t = this.claimedRewards.getLong(key);
        return t == 0L ? Optional.empty() : Optional.of(new Date(t));
    }

    public boolean areRewardsBlocked() {
        return this.rewardsBlocked;
    }

    public boolean isRewardBlocked(Reward reward) {
        return this.areRewardsBlocked() && !reward.ignoreRewardBlocking() && !reward.getQuest().ignoreRewardBlocking();
    }

    public boolean setRewardsBlocked(boolean rewardsBlocked) {
        if (rewardsBlocked != this.rewardsBlocked) {
            this.rewardsBlocked = rewardsBlocked;
            this.clearCachedProgress();
            this.markDirty();
            if (this.file.isServerSide()) {
                new SyncRewardBlockingMessage(this.teamId, rewardsBlocked).sendTo(this.getOnlineMembers());
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean isRewardClaimed(UUID player, Reward reward) {
        return this.getRewardClaimTime(player, reward).isPresent();
    }

    public boolean hasUnclaimedRewards(UUID player, QuestObject object) {
        if (this.unclaimedRewardsCache == null) {
            this.unclaimedRewardsCache = new Object2ByteOpenHashMap();
            this.unclaimedRewardsCache.defaultReturnValue((byte) -1);
        }
        QuestKey key = QuestKey.create(player, object.id);
        byte b = this.unclaimedRewardsCache.getByte(key);
        if (b == -1) {
            b = (byte) (object.hasUnclaimedRewardsRaw(this, player) ? 1 : 0);
            this.unclaimedRewardsCache.put(key, b);
        }
        return b == 1;
    }

    public boolean claimReward(UUID player, Reward reward, long date) {
        if (!this.locked && !this.isRewardBlocked(reward)) {
            QuestKey key = QuestKey.forReward(player, reward);
            if (!this.claimedRewards.containsKey(key)) {
                this.claimedRewards.put(key, date);
                this.clearCachedProgress();
                this.markDirty();
                if (this.file.isServerSide()) {
                    new ClaimRewardResponseMessage(this.teamId, player, reward.id).sendTo(this.getOnlineMembers());
                }
                reward.getQuest().checkRepeatable(this, player);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void deleteReward(Reward reward) {
        if (!this.locked && this.claimedRewards.object2LongEntrySet().removeIf(e -> ((QuestKey) e.getKey()).getId() == reward.id)) {
            this.clearCachedProgress();
            this.markDirty();
        }
    }

    public boolean resetReward(UUID player, Reward reward) {
        if (!this.locked && this.claimedRewards.removeLong(QuestKey.forReward(player, reward)) != 0L) {
            this.clearCachedProgress();
            this.markDirty();
            if (this.file.isServerSide()) {
                new ResetRewardMessage(this.teamId, player, reward.id).sendTo(this.getOnlineMembers());
            }
            return true;
        } else {
            return false;
        }
    }

    public void clearCachedProgress() {
        this.areDependenciesCompleteCache = null;
        this.unclaimedRewardsCache = null;
    }

    public SNBTCompoundTag serializeNBT() {
        SNBTCompoundTag nbt = new SNBTCompoundTag();
        nbt.m_128405_("version", 1);
        nbt.m_128359_("uuid", UUIDTypeAdapter.fromUUID(this.teamId));
        nbt.m_128359_("name", this.name);
        nbt.putBoolean("lock", this.locked);
        nbt.putBoolean("rewards_blocked", this.rewardsBlocked);
        SNBTCompoundTag taskProgressNBT = new SNBTCompoundTag();
        ObjectIterator startedNBT = this.taskProgress.long2LongEntrySet().iterator();
        while (startedNBT.hasNext()) {
            it.unimi.dsi.fastutil.longs.Long2LongMap.Entry entry = (it.unimi.dsi.fastutil.longs.Long2LongMap.Entry) startedNBT.next();
            if (entry.getLongValue() <= 2147483647L) {
                taskProgressNBT.m_128405_(QuestObjectBase.getCodeString(entry.getLongKey()), (int) entry.getLongValue());
            } else {
                taskProgressNBT.m_128356_(QuestObjectBase.getCodeString(entry.getLongKey()), entry.getLongValue());
            }
        }
        nbt.m_128365_("task_progress", taskProgressNBT);
        SNBTCompoundTag startedNBTx = new SNBTCompoundTag();
        for (it.unimi.dsi.fastutil.longs.Long2LongMap.Entry entry : this.started.long2LongEntrySet().stream().sorted(LONG2LONG_COMPARATOR).toList()) {
            startedNBTx.m_128356_(QuestObjectBase.getCodeString(entry.getLongKey()), entry.getLongValue());
        }
        nbt.m_128365_("started", startedNBTx);
        SNBTCompoundTag completedNBT = new SNBTCompoundTag();
        for (it.unimi.dsi.fastutil.longs.Long2LongMap.Entry entry : this.completed.long2LongEntrySet().stream().sorted(LONG2LONG_COMPARATOR).toList()) {
            completedNBT.m_128356_(QuestObjectBase.getCodeString(entry.getLongKey()), entry.getLongValue());
        }
        nbt.m_128365_("completed", completedNBT);
        SNBTCompoundTag claimedRewardsNBT = new SNBTCompoundTag();
        for (Entry<QuestKey> entry : this.claimedRewards.object2LongEntrySet().stream().sorted(OBJECT2LONG_COMPARATOR).toList()) {
            claimedRewardsNBT.m_128356_(((QuestKey) entry.getKey()).toString(), entry.getLongValue());
        }
        nbt.m_128365_("claimed_rewards", claimedRewardsNBT);
        CompoundTag ppdTag = new CompoundTag();
        this.perPlayerData.forEach((id, ppd) -> {
            if (!ppd.hasDefaultValues()) {
                ppdTag.put(UUIDTypeAdapter.fromUUID(id), ppd.writeNBT());
            }
        });
        nbt.m_128365_("player_data", ppdTag);
        return nbt;
    }

    public void deserializeNBT(SNBTCompoundTag nbt) {
        int fileVersion = nbt.m_128451_("version");
        if (fileVersion != 1) {
            this.markDirty();
        }
        this.name = nbt.m_128461_("name");
        this.locked = nbt.m_128471_("lock");
        this.rewardsBlocked = nbt.m_128471_("rewards_blocked");
        this.taskProgress.clear();
        this.claimedRewards.clear();
        this.perPlayerData.clear();
        CompoundTag claimedRewardsNBT = nbt.getCompound("claimed_rewards");
        for (String s : claimedRewardsNBT.getAllKeys()) {
            this.claimedRewards.put(QuestKey.fromString(s), claimedRewardsNBT.getLong(s));
        }
        CompoundTag taskProgressNBT = nbt.getCompound("task_progress");
        for (String s : taskProgressNBT.getAllKeys()) {
            this.taskProgress.put(this.file.getID(s), taskProgressNBT.getLong(s));
        }
        CompoundTag startedNBT = nbt.getCompound("started");
        for (String s : startedNBT.getAllKeys()) {
            this.started.put(this.file.getID(s), startedNBT.getLong(s));
        }
        CompoundTag completedNBT = nbt.getCompound("completed");
        for (String s : completedNBT.getAllKeys()) {
            this.completed.put(this.file.getID(s), completedNBT.getLong(s));
        }
        CompoundTag ppdTag = nbt.getCompound("player_data");
        for (String key : ppdTag.getAllKeys()) {
            try {
                UUID id = UUIDTypeAdapter.fromString(key);
                this.perPlayerData.put(id, TeamData.PerPlayerData.fromNBT(ppdTag.getCompound(key), this.file));
            } catch (IllegalArgumentException var11) {
                FTBQuests.LOGGER.error("ignoring invalid player ID {} while loading per-player data for team {}", key, this.teamId);
            }
        }
    }

    public void write(FriendlyByteBuf buffer, boolean self) {
        buffer.writeUtf(this.name, 32767);
        buffer.writeVarInt(this.taskProgress.size());
        ObjectIterator now = this.taskProgress.long2LongEntrySet().iterator();
        while (now.hasNext()) {
            it.unimi.dsi.fastutil.longs.Long2LongMap.Entry entry = (it.unimi.dsi.fastutil.longs.Long2LongMap.Entry) now.next();
            buffer.writeLong(entry.getLongKey());
            buffer.writeVarLong(entry.getLongValue());
        }
        long nowx = System.currentTimeMillis();
        buffer.writeVarInt(this.started.size());
        ObjectIterator var5 = this.started.long2LongEntrySet().iterator();
        while (var5.hasNext()) {
            it.unimi.dsi.fastutil.longs.Long2LongMap.Entry entry = (it.unimi.dsi.fastutil.longs.Long2LongMap.Entry) var5.next();
            buffer.writeLong(entry.getLongKey());
            buffer.writeVarLong(nowx - entry.getLongValue());
        }
        buffer.writeVarInt(this.completed.size());
        var5 = this.completed.long2LongEntrySet().iterator();
        while (var5.hasNext()) {
            it.unimi.dsi.fastutil.longs.Long2LongMap.Entry entry = (it.unimi.dsi.fastutil.longs.Long2LongMap.Entry) var5.next();
            buffer.writeLong(entry.getLongKey());
            buffer.writeVarLong(nowx - entry.getLongValue());
        }
        buffer.writeBoolean(this.locked);
        buffer.writeBoolean(this.rewardsBlocked);
        if (self) {
            buffer.writeVarInt(this.claimedRewards.size());
            var5 = this.claimedRewards.object2LongEntrySet().iterator();
            while (var5.hasNext()) {
                Entry<QuestKey> entry = (Entry<QuestKey>) var5.next();
                ((QuestKey) entry.getKey()).toNetwork(buffer);
                buffer.writeVarLong(nowx - entry.getLongValue());
            }
            buffer.writeVarInt(this.perPlayerData.size());
            this.perPlayerData.forEach((id, ppd) -> {
                buffer.writeUUID(id);
                ppd.writeNet(buffer);
            });
        }
    }

    public void read(FriendlyByteBuf buffer, boolean self) {
        this.name = buffer.readUtf(32767);
        this.taskProgress.clear();
        int ts = buffer.readVarInt();
        for (int i = 0; i < ts; i++) {
            this.taskProgress.put(buffer.readLong(), buffer.readVarLong());
        }
        long now = System.currentTimeMillis();
        this.started.clear();
        int ss = buffer.readVarInt();
        for (int i = 0; i < ss; i++) {
            this.started.put(buffer.readLong(), now - buffer.readVarLong());
        }
        this.completed.clear();
        int cs = buffer.readVarInt();
        for (int i = 0; i < cs; i++) {
            this.completed.put(buffer.readLong(), now - buffer.readVarLong());
        }
        this.locked = buffer.readBoolean();
        this.rewardsBlocked = buffer.readBoolean();
        this.claimedRewards.clear();
        this.perPlayerData.clear();
        if (self) {
            int claimedRewardCount = buffer.readVarInt();
            for (int i = 0; i < claimedRewardCount; i++) {
                QuestKey key = QuestKey.fromNetwork(buffer);
                this.claimedRewards.put(key, now - buffer.readVarLong());
            }
            int ppdCount = buffer.readVarInt();
            for (int i = 0; i < ppdCount; i++) {
                UUID id = buffer.readUUID();
                this.perPlayerData.put(id, TeamData.PerPlayerData.fromNet(buffer));
            }
        }
    }

    public int getRelativeProgress(QuestObject object) {
        if (this.isCompleted(object)) {
            return 100;
        } else {
            return !this.isStarted(object) ? 0 : object.getRelativeProgressFromChildren(this);
        }
    }

    public boolean isStarted(QuestObject object) {
        return this.started.containsKey(object.id);
    }

    public boolean isCompleted(QuestObject object) {
        return this.completed.containsKey(object.id);
    }

    public boolean areDependenciesComplete(Quest quest) {
        if (!quest.hasDependencies()) {
            return true;
        } else {
            if (this.areDependenciesCompleteCache == null) {
                this.areDependenciesCompleteCache = new Long2ByteOpenHashMap();
                this.areDependenciesCompleteCache.defaultReturnValue((byte) -1);
            }
            byte res = this.areDependenciesCompleteCache.get(quest.id);
            if (res == -1) {
                res = (byte) (quest.areDependenciesComplete(this) ? 1 : 0);
                this.areDependenciesCompleteCache.put(quest.id, res);
            }
            return res == 1;
        }
    }

    public boolean canStartTasks(Quest quest) {
        return quest.getProgressionMode() == ProgressionMode.FLEXIBLE || this.areDependenciesComplete(quest);
    }

    public void claimReward(ServerPlayer player, Reward reward, boolean notify) {
        if (this.claimReward(player.m_20148_(), reward, System.currentTimeMillis())) {
            reward.claim(player, notify);
        }
    }

    public Collection<ServerPlayer> getOnlineMembers() {
        return (Collection<ServerPlayer>) FTBTeamsAPI.api().getManager().getTeamByID(this.teamId).map(Team::getOnlineMembers).orElse(List.of());
    }

    public void checkAutoCompletion(Quest quest) {
        if (!quest.getRewards().isEmpty() && this.isCompleted(quest)) {
            Collection<ServerPlayer> online = null;
            for (Reward reward : quest.getRewards()) {
                RewardAutoClaim auto = reward.getAutoClaimType();
                if (auto != RewardAutoClaim.DISABLED) {
                    if (online == null) {
                        online = this.getOnlineMembers();
                        if (online.isEmpty()) {
                            return;
                        }
                    }
                    for (ServerPlayer player : online) {
                        this.claimReward(player, reward, auto == RewardAutoClaim.ENABLED);
                    }
                }
            }
        }
    }

    public RewardClaimType getClaimType(UUID player, Reward reward) {
        boolean r = this.isRewardClaimed(player, reward);
        if (r) {
            return RewardClaimType.CLAIMED;
        } else {
            return this.isCompleted(reward.getQuest()) ? RewardClaimType.CAN_CLAIM : RewardClaimType.CANT_CLAIM;
        }
    }

    public void resetProgress(Task task) {
        if (this.taskProgress.remove(task.id) > 0L) {
            this.markDirty();
        }
    }

    public final void setProgress(Task task, long progress) {
        if (!this.locked) {
            long maxProgress = task.getMaxProgress();
            progress = Math.max(0L, Math.min(progress, maxProgress));
            long prevProgress = this.getProgress(task);
            if (prevProgress != progress || progress == 0L && this.isStarted(task)) {
                if (progress == 0L) {
                    this.taskProgress.remove(task.id);
                    this.started.remove(task.id);
                    this.completed.remove(task.id);
                } else {
                    this.taskProgress.put(task.id, progress);
                }
                this.clearCachedProgress();
                if (this.file.isServerSide()) {
                    Date now = new Date();
                    Collection<ServerPlayer> onlineMembers = this.getOnlineMembers();
                    new UpdateTaskProgressMessage(this, task.id, progress).sendTo(onlineMembers);
                    if (prevProgress == 0L) {
                        task.onStarted(new QuestProgressEventData<>(now, this, task, onlineMembers, Collections.emptyList()));
                    }
                    if (progress >= maxProgress && this.areDependenciesComplete(task.getQuest())) {
                        this.markTaskCompleted(task);
                    }
                }
                this.markDirty();
            }
        }
    }

    public void markTaskCompleted(Task task) {
        if (!this.isCompleted(task)) {
            Collection<ServerPlayer> onlineMembers = this.getOnlineMembers();
            Collection<ServerPlayer> notifiedPlayers;
            if (!task.getQuest().getChapter().isAlwaysInvisible() && QuestObjectBase.shouldSendNotifications()) {
                notifiedPlayers = onlineMembers;
            } else {
                notifiedPlayers = List.of();
            }
            task.onCompleted(new QuestProgressEventData<>(new Date(), this, task, onlineMembers, notifiedPlayers));
            for (ServerPlayer player : onlineMembers) {
                FTBQuestsInventoryListener.detect(player, ItemStack.EMPTY, task.id);
            }
            if (this.isCompleted(task.getQuest())) {
                this.perPlayerData.values().forEach(data -> data.pinnedQuests.remove(task.getQuest().id));
                this.markDirty();
                new TogglePinnedResponseMessage(task.getQuest().id, false).sendTo(onlineMembers);
            }
        }
    }

    public final void addProgress(Task task, long progress) {
        this.setProgress(task, this.getProgress(task) + progress);
    }

    public boolean isLocked() {
        return this.locked;
    }

    public boolean setLocked(boolean newLocked) {
        if (this.locked != newLocked) {
            this.locked = newLocked;
            this.clearCachedProgress();
            this.markDirty();
            if (this.file.isServerSide()) {
                new SyncLockMessage(this.teamId, this.locked).sendTo(this.getOnlineMembers());
            }
            return true;
        } else {
            return false;
        }
    }

    public void mergeData(TeamData from) {
        from.taskProgress.forEach((id, data) -> this.taskProgress.mergeLong(id, data, Long::max));
        from.started.forEach((id, data) -> this.started.mergeLong(id, data, (oldVal, newVal) -> oldVal));
        from.completed.forEach((id, data) -> this.completed.mergeLong(id, data, (oldVal, newVal) -> oldVal));
        from.claimedRewards.forEach((id, data) -> this.claimedRewards.mergeLong(id, data, (oldVal, newVal) -> oldVal));
        from.perPlayerData.forEach((id, data) -> this.perPlayerData.merge(id, data, (oldVal, newVal) -> oldVal));
    }

    public void mergeClaimedRewards(TeamData from) {
        from.claimedRewards.forEach((questKey, data) -> {
            if (questKey.uuid().equals(this.teamId)) {
                this.claimedRewards.put(questKey, data);
            }
        });
    }

    public void copyData(TeamData from) {
        this.locked = from.locked;
        this.taskProgress.putAll(from.taskProgress);
        this.claimedRewards.putAll(from.claimedRewards);
        this.started.putAll(from.started);
        this.completed.putAll(from.completed);
        this.perPlayerData.putAll(from.perPlayerData);
        this.rewardsBlocked = from.rewardsBlocked;
    }

    private Optional<TeamData.PerPlayerData> getOrCreatePlayerData(Player player) {
        if (!this.perPlayerData.containsKey(player.m_20148_()) && this.file.isPlayerOnTeam(player, this)) {
            this.perPlayerData.put(player.m_20148_(), new TeamData.PerPlayerData());
        }
        return Optional.ofNullable((TeamData.PerPlayerData) this.perPlayerData.get(player.m_20148_()));
    }

    public boolean getCanEdit(Player player) {
        return (Boolean) this.getOrCreatePlayerData(player).map(d -> d.canEdit).orElse(false);
    }

    public boolean setCanEdit(Player player, boolean newCanEdit) {
        return (Boolean) this.getOrCreatePlayerData(player).map(playerData -> {
            if (playerData.canEdit != newCanEdit) {
                playerData.canEdit = newCanEdit;
                this.clearCachedProgress();
                this.markDirty();
                if (this.file.isServerSide() && player instanceof ServerPlayer sp) {
                    new SyncEditingModeMessage(this.teamId, newCanEdit).sendTo(sp);
                }
                return true;
            } else {
                return false;
            }
        }).orElse(false);
    }

    public boolean isQuestPinned(Player player, long id) {
        return (Boolean) this.getOrCreatePlayerData(player).map(playerData -> playerData.pinnedQuests.contains(id)).orElse(false);
    }

    public void setQuestPinned(Player player, long id, boolean pinned) {
        this.getOrCreatePlayerData(player).ifPresent(playerData -> {
            if (pinned ? playerData.pinnedQuests.add(id) : playerData.pinnedQuests.remove(id)) {
                this.markDirty();
            }
        });
    }

    public void setChapterPinned(Player player, boolean pinned) {
        this.getOrCreatePlayerData(player).ifPresent(playerData -> {
            if (playerData.chapterPinned != pinned) {
                playerData.chapterPinned = pinned;
                this.markDirty();
            }
        });
    }

    public boolean isChapterPinned(Player player) {
        return (Boolean) this.getOrCreatePlayerData(player).map(playerData -> playerData.chapterPinned).orElse(false);
    }

    public LongSet getPinnedQuestIds(Player player) {
        return (LongSet) this.getOrCreatePlayerData(player).map(playerData -> playerData.pinnedQuests).orElse(LongSet.of());
    }

    private static class PerPlayerData {

        private boolean canEdit;

        private boolean autoPin;

        private boolean chapterPinned;

        private final LongSet pinnedQuests;

        PerPlayerData() {
            this.canEdit = this.autoPin = this.chapterPinned = false;
            this.pinnedQuests = new LongOpenHashSet();
        }

        PerPlayerData(boolean canEdit, boolean autoPin, boolean chapterPinned, LongSet pinnedQuests) {
            this.canEdit = canEdit;
            this.autoPin = autoPin;
            this.chapterPinned = chapterPinned;
            this.pinnedQuests = pinnedQuests;
        }

        public boolean hasDefaultValues() {
            return !this.canEdit && !this.autoPin && !this.chapterPinned && this.pinnedQuests.isEmpty();
        }

        public static TeamData.PerPlayerData fromNBT(CompoundTag nbt, BaseQuestFile file) {
            boolean canEdit = nbt.getBoolean("can_edit");
            boolean autoPin = nbt.getBoolean("auto_pin");
            boolean chapterPinned = nbt.getBoolean("chapter_pinned");
            LongSet pq = (LongSet) nbt.getList("pinned_quests", 8).stream().map(tag -> file.getID(tag.getAsString())).collect(Collectors.toCollection(LongOpenHashSet::new));
            return new TeamData.PerPlayerData(canEdit, autoPin, chapterPinned, pq);
        }

        public static TeamData.PerPlayerData fromNet(FriendlyByteBuf buffer) {
            TeamData.PerPlayerData ppd = new TeamData.PerPlayerData();
            ppd.canEdit = buffer.readBoolean();
            ppd.autoPin = buffer.readBoolean();
            ppd.chapterPinned = buffer.readBoolean();
            int pinnedCount = buffer.readVarInt();
            for (int i = 0; i < pinnedCount; i++) {
                ppd.pinnedQuests.add(buffer.readLong());
            }
            return ppd;
        }

        public CompoundTag writeNBT() {
            CompoundTag nbt = new CompoundTag();
            if (this.canEdit) {
                nbt.putBoolean("can_edit", true);
            }
            if (this.autoPin) {
                nbt.putBoolean("auto_pin", true);
            }
            if (this.chapterPinned) {
                nbt.putBoolean("chapter_pinned", true);
            }
            if (!this.pinnedQuests.isEmpty()) {
                long[] pinnedQuestsArray = this.pinnedQuests.toLongArray();
                Arrays.sort(pinnedQuestsArray);
                ListTag pinnedQuestsNBT = new ListTag();
                for (long l : pinnedQuestsArray) {
                    pinnedQuestsNBT.add(StringTag.valueOf(QuestObjectBase.getCodeString(l)));
                }
                nbt.put("pinned_quests", pinnedQuestsNBT);
            }
            return nbt;
        }

        public void writeNet(FriendlyByteBuf buffer) {
            buffer.writeBoolean(this.canEdit);
            buffer.writeBoolean(this.autoPin);
            buffer.writeBoolean(this.chapterPinned);
            buffer.writeVarInt(this.pinnedQuests.size());
            LongIterator var2 = this.pinnedQuests.iterator();
            while (var2.hasNext()) {
                long reward = (Long) var2.next();
                buffer.writeLong(reward);
            }
        }
    }
}