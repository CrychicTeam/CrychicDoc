package net.minecraft.world.scores;

import java.util.Comparator;
import javax.annotation.Nullable;

public class Score {

    public static final Comparator<Score> SCORE_COMPARATOR = (p_83396_, p_83397_) -> {
        if (p_83396_.getScore() > p_83397_.getScore()) {
            return 1;
        } else {
            return p_83396_.getScore() < p_83397_.getScore() ? -1 : p_83397_.getOwner().compareToIgnoreCase(p_83396_.getOwner());
        }
    };

    private final Scoreboard scoreboard;

    @Nullable
    private final Objective objective;

    private final String owner;

    private int count;

    private boolean locked;

    private boolean forceUpdate;

    public Score(Scoreboard scoreboard0, Objective objective1, String string2) {
        this.scoreboard = scoreboard0;
        this.objective = objective1;
        this.owner = string2;
        this.locked = true;
        this.forceUpdate = true;
    }

    public void add(int int0) {
        if (this.objective.getCriteria().isReadOnly()) {
            throw new IllegalStateException("Cannot modify read-only score");
        } else {
            this.setScore(this.getScore() + int0);
        }
    }

    public void increment() {
        this.add(1);
    }

    public int getScore() {
        return this.count;
    }

    public void reset() {
        this.setScore(0);
    }

    public void setScore(int int0) {
        int $$1 = this.count;
        this.count = int0;
        if ($$1 != int0 || this.forceUpdate) {
            this.forceUpdate = false;
            this.getScoreboard().onScoreChanged(this);
        }
    }

    @Nullable
    public Objective getObjective() {
        return this.objective;
    }

    public String getOwner() {
        return this.owner;
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setLocked(boolean boolean0) {
        this.locked = boolean0;
    }
}