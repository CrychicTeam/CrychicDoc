package dev.ftb.mods.ftbquests.events;

import dev.ftb.mods.ftbquests.net.DisplayCompletionToastMessage;
import dev.ftb.mods.ftbquests.quest.QuestObject;
import dev.ftb.mods.ftbquests.quest.TeamData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;

public final class QuestProgressEventData<T extends QuestObject> {

    private final Date time;

    private final TeamData teamData;

    private final T object;

    private final List<ServerPlayer> onlineMembers;

    private final List<ServerPlayer> notifiedPlayers;

    public QuestProgressEventData(Date date, TeamData teamData, T object, Collection<ServerPlayer> online, Collection<ServerPlayer> notified) {
        this.time = date;
        this.teamData = teamData;
        this.object = object;
        this.onlineMembers = new ArrayList(online);
        this.notifiedPlayers = new ArrayList(notified);
    }

    public void setStarted(long id) {
        this.teamData.setStarted(id, this.time);
    }

    public void setCompleted(long id) {
        this.teamData.setCompleted(id, this.time);
    }

    public void notifyPlayers(long id) {
        this.notifiedPlayers.forEach(player -> new DisplayCompletionToastMessage(id).sendTo(player));
    }

    public Date getTime() {
        return this.time;
    }

    public TeamData getTeamData() {
        return this.teamData;
    }

    public T getObject() {
        return this.object;
    }

    public List<ServerPlayer> getOnlineMembers() {
        return this.onlineMembers;
    }

    public List<ServerPlayer> getNotifiedPlayers() {
        return this.notifiedPlayers;
    }

    public <N extends QuestObject> QuestProgressEventData<N> withObject(N o) {
        return this.object == o ? this : new QuestProgressEventData<>(this.time, this.teamData, o, this.onlineMembers, this.notifiedPlayers);
    }
}