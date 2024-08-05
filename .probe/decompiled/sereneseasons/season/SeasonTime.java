package sereneseasons.season;

import com.google.common.base.Preconditions;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.Season;
import sereneseasons.config.ServerConfig;

public final class SeasonTime implements ISeasonState {

    public static final SeasonTime ZERO = new SeasonTime(0);

    public final int time;

    public SeasonTime(int time) {
        Preconditions.checkArgument(time >= 0, "Time cannot be negative!");
        this.time = time;
    }

    @Override
    public int getDayDuration() {
        return ServerConfig.dayDuration.get();
    }

    @Override
    public int getSubSeasonDuration() {
        return this.getDayDuration() * ServerConfig.subSeasonDuration.get();
    }

    @Override
    public int getSeasonDuration() {
        return this.getSubSeasonDuration() * 3;
    }

    @Override
    public int getCycleDuration() {
        return this.getSubSeasonDuration() * Season.SubSeason.VALUES.length;
    }

    @Override
    public int getSeasonCycleTicks() {
        return this.time;
    }

    @Override
    public int getDay() {
        return this.time / this.getDayDuration();
    }

    @Override
    public Season.SubSeason getSubSeason() {
        int index = this.time / this.getSubSeasonDuration() % Season.SubSeason.VALUES.length;
        return Season.SubSeason.VALUES[index];
    }

    @Override
    public Season getSeason() {
        return this.getSubSeason().getSeason();
    }

    @Override
    public Season.TropicalSeason getTropicalSeason() {
        int index = ((this.time / this.getSubSeasonDuration() + 11) / 2 + 5) % Season.TropicalSeason.VALUES.length;
        return Season.TropicalSeason.VALUES[index];
    }
}