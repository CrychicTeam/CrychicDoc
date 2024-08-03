package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.ScoreboardSaveData;

public class ServerScoreboard extends Scoreboard {

    private final MinecraftServer server;

    private final Set<Objective> trackedObjectives = Sets.newHashSet();

    private final List<Runnable> dirtyListeners = Lists.newArrayList();

    public ServerScoreboard(MinecraftServer minecraftServer0) {
        this.server = minecraftServer0;
    }

    @Override
    public void onScoreChanged(Score score0) {
        super.onScoreChanged(score0);
        if (this.trackedObjectives.contains(score0.getObjective())) {
            this.server.getPlayerList().broadcastAll(new ClientboundSetScorePacket(ServerScoreboard.Method.CHANGE, score0.getObjective().getName(), score0.getOwner(), score0.getScore()));
        }
        this.setDirty();
    }

    @Override
    public void onPlayerRemoved(String string0) {
        super.onPlayerRemoved(string0);
        this.server.getPlayerList().broadcastAll(new ClientboundSetScorePacket(ServerScoreboard.Method.REMOVE, null, string0, 0));
        this.setDirty();
    }

    @Override
    public void onPlayerScoreRemoved(String string0, Objective objective1) {
        super.onPlayerScoreRemoved(string0, objective1);
        if (this.trackedObjectives.contains(objective1)) {
            this.server.getPlayerList().broadcastAll(new ClientboundSetScorePacket(ServerScoreboard.Method.REMOVE, objective1.getName(), string0, 0));
        }
        this.setDirty();
    }

    @Override
    public void setDisplayObjective(int int0, @Nullable Objective objective1) {
        Objective $$2 = this.m_83416_(int0);
        super.setDisplayObjective(int0, objective1);
        if ($$2 != objective1 && $$2 != null) {
            if (this.getObjectiveDisplaySlotCount($$2) > 0) {
                this.server.getPlayerList().broadcastAll(new ClientboundSetDisplayObjectivePacket(int0, objective1));
            } else {
                this.stopTrackingObjective($$2);
            }
        }
        if (objective1 != null) {
            if (this.trackedObjectives.contains(objective1)) {
                this.server.getPlayerList().broadcastAll(new ClientboundSetDisplayObjectivePacket(int0, objective1));
            } else {
                this.startTrackingObjective(objective1);
            }
        }
        this.setDirty();
    }

    @Override
    public boolean addPlayerToTeam(String string0, PlayerTeam playerTeam1) {
        if (super.addPlayerToTeam(string0, playerTeam1)) {
            this.server.getPlayerList().broadcastAll(ClientboundSetPlayerTeamPacket.createPlayerPacket(playerTeam1, string0, ClientboundSetPlayerTeamPacket.Action.ADD));
            this.setDirty();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void removePlayerFromTeam(String string0, PlayerTeam playerTeam1) {
        super.removePlayerFromTeam(string0, playerTeam1);
        this.server.getPlayerList().broadcastAll(ClientboundSetPlayerTeamPacket.createPlayerPacket(playerTeam1, string0, ClientboundSetPlayerTeamPacket.Action.REMOVE));
        this.setDirty();
    }

    @Override
    public void onObjectiveAdded(Objective objective0) {
        super.onObjectiveAdded(objective0);
        this.setDirty();
    }

    @Override
    public void onObjectiveChanged(Objective objective0) {
        super.onObjectiveChanged(objective0);
        if (this.trackedObjectives.contains(objective0)) {
            this.server.getPlayerList().broadcastAll(new ClientboundSetObjectivePacket(objective0, 2));
        }
        this.setDirty();
    }

    @Override
    public void onObjectiveRemoved(Objective objective0) {
        super.onObjectiveRemoved(objective0);
        if (this.trackedObjectives.contains(objective0)) {
            this.stopTrackingObjective(objective0);
        }
        this.setDirty();
    }

    @Override
    public void onTeamAdded(PlayerTeam playerTeam0) {
        super.onTeamAdded(playerTeam0);
        this.server.getPlayerList().broadcastAll(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(playerTeam0, true));
        this.setDirty();
    }

    @Override
    public void onTeamChanged(PlayerTeam playerTeam0) {
        super.onTeamChanged(playerTeam0);
        this.server.getPlayerList().broadcastAll(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(playerTeam0, false));
        this.setDirty();
    }

    @Override
    public void onTeamRemoved(PlayerTeam playerTeam0) {
        super.onTeamRemoved(playerTeam0);
        this.server.getPlayerList().broadcastAll(ClientboundSetPlayerTeamPacket.createRemovePacket(playerTeam0));
        this.setDirty();
    }

    public void addDirtyListener(Runnable runnable0) {
        this.dirtyListeners.add(runnable0);
    }

    protected void setDirty() {
        for (Runnable $$0 : this.dirtyListeners) {
            $$0.run();
        }
    }

    public List<Packet<?>> getStartTrackingPackets(Objective objective0) {
        List<Packet<?>> $$1 = Lists.newArrayList();
        $$1.add(new ClientboundSetObjectivePacket(objective0, 0));
        for (int $$2 = 0; $$2 < 19; $$2++) {
            if (this.m_83416_($$2) == objective0) {
                $$1.add(new ClientboundSetDisplayObjectivePacket($$2, objective0));
            }
        }
        for (Score $$3 : this.m_83498_(objective0)) {
            $$1.add(new ClientboundSetScorePacket(ServerScoreboard.Method.CHANGE, $$3.getObjective().getName(), $$3.getOwner(), $$3.getScore()));
        }
        return $$1;
    }

    public void startTrackingObjective(Objective objective0) {
        List<Packet<?>> $$1 = this.getStartTrackingPackets(objective0);
        for (ServerPlayer $$2 : this.server.getPlayerList().getPlayers()) {
            for (Packet<?> $$3 : $$1) {
                $$2.connection.send($$3);
            }
        }
        this.trackedObjectives.add(objective0);
    }

    public List<Packet<?>> getStopTrackingPackets(Objective objective0) {
        List<Packet<?>> $$1 = Lists.newArrayList();
        $$1.add(new ClientboundSetObjectivePacket(objective0, 1));
        for (int $$2 = 0; $$2 < 19; $$2++) {
            if (this.m_83416_($$2) == objective0) {
                $$1.add(new ClientboundSetDisplayObjectivePacket($$2, objective0));
            }
        }
        return $$1;
    }

    public void stopTrackingObjective(Objective objective0) {
        List<Packet<?>> $$1 = this.getStopTrackingPackets(objective0);
        for (ServerPlayer $$2 : this.server.getPlayerList().getPlayers()) {
            for (Packet<?> $$3 : $$1) {
                $$2.connection.send($$3);
            }
        }
        this.trackedObjectives.remove(objective0);
    }

    public int getObjectiveDisplaySlotCount(Objective objective0) {
        int $$1 = 0;
        for (int $$2 = 0; $$2 < 19; $$2++) {
            if (this.m_83416_($$2) == objective0) {
                $$1++;
            }
        }
        return $$1;
    }

    public ScoreboardSaveData createData() {
        ScoreboardSaveData $$0 = new ScoreboardSaveData(this);
        this.addDirtyListener($$0::m_77762_);
        return $$0;
    }

    public ScoreboardSaveData createData(CompoundTag compoundTag0) {
        return this.createData().load(compoundTag0);
    }

    public static enum Method {

        CHANGE, REMOVE
    }
}