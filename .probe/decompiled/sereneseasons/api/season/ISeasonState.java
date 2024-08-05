package sereneseasons.api.season;

public interface ISeasonState {

    int getDayDuration();

    int getSubSeasonDuration();

    int getSeasonDuration();

    int getCycleDuration();

    int getSeasonCycleTicks();

    int getDay();

    Season.SubSeason getSubSeason();

    Season getSeason();

    Season.TropicalSeason getTropicalSeason();
}