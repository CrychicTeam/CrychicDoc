package sereneseasons.api.season;

import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;

public class SeasonChangedEvent<T> extends Event {

    private final Level level;

    private final T prevSeason;

    private final T newSeason;

    private SeasonChangedEvent(Level level, T prevSeason, T newSeason) {
        this.level = level;
        this.prevSeason = prevSeason;
        this.newSeason = newSeason;
    }

    public Level getLevel() {
        return this.level;
    }

    public T getPrevSeason() {
        return this.prevSeason;
    }

    public T getNewSeason() {
        return this.newSeason;
    }

    public static class Standard extends SeasonChangedEvent<Season.SubSeason> {

        public Standard(Level level, Season.SubSeason prevSeason, Season.SubSeason newSeason) {
            super(level, prevSeason, newSeason);
        }
    }

    public static class Tropical extends SeasonChangedEvent<Season.TropicalSeason> {

        public Tropical(Level level, Season.TropicalSeason prevSeason, Season.TropicalSeason newSeason) {
            super(level, prevSeason, newSeason);
        }
    }
}