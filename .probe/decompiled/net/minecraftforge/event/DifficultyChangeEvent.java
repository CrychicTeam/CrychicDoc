package net.minecraftforge.event;

import net.minecraft.world.Difficulty;
import net.minecraftforge.eventbus.api.Event;

public class DifficultyChangeEvent extends Event {

    private final Difficulty difficulty;

    private final Difficulty oldDifficulty;

    public DifficultyChangeEvent(Difficulty difficulty, Difficulty oldDifficulty) {
        this.difficulty = difficulty;
        this.oldDifficulty = oldDifficulty;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public Difficulty getOldDifficulty() {
        return this.oldDifficulty;
    }
}