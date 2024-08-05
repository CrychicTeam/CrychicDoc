package noppes.npcs.api.wrapper;

import net.minecraft.world.scores.Score;
import noppes.npcs.api.IScoreboardScore;

public class ScoreboardScoreWrapper implements IScoreboardScore {

    private Score score;

    public ScoreboardScoreWrapper(Score score) {
        this.score = score;
    }

    @Override
    public int getValue() {
        return this.score.getScore();
    }

    @Override
    public void setValue(int val) {
        this.score.setScore(val);
    }

    @Override
    public String getPlayerName() {
        return this.score.getOwner();
    }
}