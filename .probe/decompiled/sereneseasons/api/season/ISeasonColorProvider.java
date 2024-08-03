package sereneseasons.api.season;

public interface ISeasonColorProvider {

    int getGrassOverlay();

    float getGrassSaturationMultiplier();

    int getFoliageOverlay();

    float getFoliageSaturationMultiplier();

    int getBirchColor();
}