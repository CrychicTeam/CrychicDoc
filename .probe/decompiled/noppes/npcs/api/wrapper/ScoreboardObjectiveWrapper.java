package noppes.npcs.api.wrapper;

import java.util.Collection;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IScoreboardObjective;
import noppes.npcs.api.IScoreboardScore;

public class ScoreboardObjectiveWrapper implements IScoreboardObjective {

    private Objective objective;

    private Scoreboard board;

    protected ScoreboardObjectiveWrapper(Scoreboard board, Objective objective) {
        this.objective = objective;
        this.board = board;
    }

    @Override
    public String getName() {
        return this.objective.getName();
    }

    @Override
    public String getDisplayName() {
        return this.objective.getDisplayName().getString();
    }

    @Override
    public void setDisplayName(String name) {
        if (name.length() > 0 && name.length() <= 32) {
            this.objective.setDisplayName(Component.translatable(name));
        } else {
            throw new CustomNPCsException("Score objective display name must be between 1-32 characters: %s", name);
        }
    }

    @Override
    public String getCriteria() {
        return this.objective.getCriteria().getName();
    }

    @Override
    public boolean isReadyOnly() {
        return this.objective.getCriteria().isReadOnly();
    }

    @Override
    public IScoreboardScore[] getScores() {
        Collection<Score> list = this.board.getPlayerScores(this.objective);
        IScoreboardScore[] scores = new IScoreboardScore[list.size()];
        int i = 0;
        for (Score score : list) {
            scores[i] = new ScoreboardScoreWrapper(score);
            i++;
        }
        return scores;
    }

    @Override
    public IScoreboardScore getScore(String player) {
        return !this.hasScore(player) ? null : new ScoreboardScoreWrapper(this.board.getOrCreatePlayerScore(player, this.objective));
    }

    @Override
    public IScoreboardScore createScore(String player) {
        return new ScoreboardScoreWrapper(this.board.getOrCreatePlayerScore(player, this.objective));
    }

    @Override
    public void removeScore(String player) {
        this.board.resetPlayerScore(player, this.objective);
    }

    @Override
    public boolean hasScore(String player) {
        return this.board.hasPlayerScore(player, this.objective);
    }
}