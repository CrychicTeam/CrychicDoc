package noppes.npcs.api.wrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IScoreboard;
import noppes.npcs.api.IScoreboardObjective;
import noppes.npcs.api.IScoreboardTeam;

public class ScoreboardWrapper implements IScoreboard {

    private Scoreboard board;

    private MinecraftServer server;

    protected ScoreboardWrapper(MinecraftServer server) {
        this.server = server;
        this.board = server.getLevel(Level.OVERWORLD).getScoreboard();
    }

    @Override
    public IScoreboardObjective[] getObjectives() {
        List<Objective> collection = new ArrayList(this.board.getObjectives());
        IScoreboardObjective[] objectives = new IScoreboardObjective[collection.size()];
        for (int i = 0; i < collection.size(); i++) {
            objectives[i] = new ScoreboardObjectiveWrapper(this.board, (Objective) collection.get(i));
        }
        return objectives;
    }

    @Override
    public String[] getPlayerList() {
        Collection<String> collection = this.board.getObjectiveNames();
        return (String[]) collection.toArray(new String[collection.size()]);
    }

    @Override
    public IScoreboardObjective getObjective(String name) {
        Objective obj = this.board.getObjective(name);
        return obj == null ? null : new ScoreboardObjectiveWrapper(this.board, obj);
    }

    @Override
    public boolean hasObjective(String objective) {
        return this.board.getObjective(objective) != null;
    }

    @Override
    public void removeObjective(String objective) {
        Objective obj = this.board.getObjective(objective);
        if (obj != null) {
            this.board.removeObjective(obj);
        }
    }

    @Override
    public IScoreboardObjective addObjective(String objective, String criteria) {
        ObjectiveCriteria icriteria = (ObjectiveCriteria) ObjectiveCriteria.byName(criteria).orElse(null);
        if (icriteria == null) {
            throw new CustomNPCsException("Unknown score criteria: %s", criteria);
        } else if (objective.length() > 0 && objective.length() <= 16) {
            Objective obj = this.board.addObjective(objective, icriteria, Component.translatable(objective), ObjectiveCriteria.RenderType.INTEGER);
            return new ScoreboardObjectiveWrapper(this.board, obj);
        } else {
            throw new CustomNPCsException("Score objective must be between 1-16 characters: %s", objective);
        }
    }

    @Override
    public void setPlayerScore(String player, String objective, int score) {
        Objective objec = this.getObjectiveWithException(objective);
        if (!objec.getCriteria().isReadOnly() && score >= Integer.MIN_VALUE && score <= Integer.MAX_VALUE) {
            Score sco = this.board.getOrCreatePlayerScore(player, objec);
            sco.setScore(score);
        }
    }

    private Objective getObjectiveWithException(String objective) {
        Objective objec = this.board.getObjective(objective);
        if (objec == null) {
            throw new CustomNPCsException("Score objective does not exist: %s", objective);
        } else {
            return objec;
        }
    }

    @Override
    public int getPlayerScore(String player, String objective) {
        Objective objec = this.getObjectiveWithException(objective);
        return objec.getCriteria().isReadOnly() ? 0 : this.board.getOrCreatePlayerScore(player, objec).getScore();
    }

    @Override
    public boolean hasPlayerObjective(String player, String objective) {
        Objective objec = this.getObjectiveWithException(objective);
        return this.board.getPlayerScores(player).get(objec) != null;
    }

    @Override
    public void deletePlayerScore(String player, String objective) {
        Objective objec = this.getObjectiveWithException(objective);
        if (this.board.getPlayerScores(player).remove(objec) != null) {
            this.board.removePlayerFromTeam(player);
        }
    }

    @Override
    public IScoreboardTeam[] getTeams() {
        List<PlayerTeam> list = new ArrayList(this.board.getPlayerTeams());
        IScoreboardTeam[] teams = new IScoreboardTeam[list.size()];
        for (int i = 0; i < list.size(); i++) {
            teams[i] = new ScoreboardTeamWrapper((PlayerTeam) list.get(i), this.board);
        }
        return teams;
    }

    @Override
    public boolean hasTeam(String name) {
        return this.board.getPlayerTeam(name) != null;
    }

    @Override
    public IScoreboardTeam addTeam(String name) {
        if (this.hasTeam(name)) {
            throw new CustomNPCsException("Team %s already exists", name);
        } else {
            return new ScoreboardTeamWrapper(this.board.addPlayerTeam(name), this.board);
        }
    }

    @Override
    public IScoreboardTeam getTeam(String name) {
        PlayerTeam team = this.board.getPlayerTeam(name);
        return team == null ? null : new ScoreboardTeamWrapper(team, this.board);
    }

    @Override
    public void removeTeam(String name) {
        PlayerTeam team = this.board.getPlayerTeam(name);
        if (team != null) {
            this.board.removePlayerTeam(team);
        }
    }

    @Override
    public IScoreboardTeam getPlayerTeam(String player) {
        PlayerTeam team = this.board.getPlayersTeam(player);
        return team == null ? null : new ScoreboardTeamWrapper(team, this.board);
    }

    @Override
    public void removePlayerTeam(String player) {
        this.board.removePlayerFromTeam(player);
    }
}