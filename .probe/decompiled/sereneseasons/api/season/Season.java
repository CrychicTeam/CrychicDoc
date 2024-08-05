package sereneseasons.api.season;

public enum Season {

    SPRING, SUMMER, AUTUMN, WINTER;

    public static enum SubSeason implements ISeasonColorProvider {

        EARLY_SPRING(Season.SPRING, 7831687, 0.85F, 7307663, 0.85F, 8821352),
        MID_SPRING(Season.SPRING, 6783639, 5211823, 7254659),
        LATE_SPRING(Season.SPRING, 7307663, 6259871, 7646835),
        EARLY_SUMMER(Season.SUMMER, 7831687, 7307663, 8039012),
        MID_SUMMER(Season.SUMMER, 16777215, 16777215, 8431445),
        LATE_SUMMER(Season.SUMMER, 8877943, 10444639, 10003787),
        EARLY_AUTUMN(Season.AUTUMN, 9400175, 12861504, 11641922),
        MID_AUTUMN(Season.AUTUMN, 10444639, 15671585, 14852657),
        LATE_AUTUMN(Season.AUTUMN, 11489103, 0.85F, 14364720, 0.85F, 13208117),
        EARLY_WINTER(Season.WINTER, 11489103, 0.6F, 14364720, 0.6F, 11629115),
        MID_WINTER(Season.WINTER, 11489103, 0.45F, 14364720, 0.45F, 10519117),
        LATE_WINTER(Season.WINTER, 9339265, 0.6F, 10842224, 0.6F, 9409119);

        public static final Season.SubSeason[] VALUES = values();

        private Season season;

        private int grassOverlay;

        private float grassSaturationMultiplier;

        private int foliageOverlay;

        private float foliageSaturationMultiplier;

        private int birchColor;

        private SubSeason(Season season, int grassColour, float grassSaturation, int foliageColour, float foliageSaturation, int birchColor) {
            this.season = season;
            this.grassOverlay = grassColour;
            this.grassSaturationMultiplier = grassSaturation;
            this.foliageOverlay = foliageColour;
            this.foliageSaturationMultiplier = foliageSaturation;
            this.birchColor = birchColor;
        }

        private SubSeason(Season season, int grassColour, int foliageColour, int birchColor) {
            this(season, grassColour, -1.0F, foliageColour, -1.0F, birchColor);
        }

        public Season getSeason() {
            return this.season;
        }

        @Override
        public int getGrassOverlay() {
            return this.grassOverlay;
        }

        @Override
        public float getGrassSaturationMultiplier() {
            return this.grassSaturationMultiplier;
        }

        @Override
        public int getFoliageOverlay() {
            return this.foliageOverlay;
        }

        @Override
        public float getFoliageSaturationMultiplier() {
            return this.foliageSaturationMultiplier;
        }

        @Override
        public int getBirchColor() {
            return this.birchColor;
        }
    }

    public static enum TropicalSeason implements ISeasonColorProvider {

        EARLY_DRY(16777215, 16777215, 8431445),
        MID_DRY(10847848, 0.8F, 12027516, 0.95F, 10003787),
        LATE_DRY(9337709, 0.9F, 10521478, 0.975F, 8431445),
        EARLY_WET(7703690, 7507089, 8431445),
        MID_WET(5538692, 2398382, 7777388),
        LATE_WET(6654345, 5146771, 8431445);

        public static final Season.TropicalSeason[] VALUES = values();

        private int grassOverlay;

        private float grassSaturationMultiplier;

        private int foliageOverlay;

        private float foliageSaturationMultiplier;

        private int birchColor;

        private TropicalSeason(int grassColour, float grassSaturation, int foliageColour, float foliageSaturation, int birchColor) {
            this.grassOverlay = grassColour;
            this.grassSaturationMultiplier = grassSaturation;
            this.foliageOverlay = foliageColour;
            this.foliageSaturationMultiplier = foliageSaturation;
            this.birchColor = birchColor;
        }

        private TropicalSeason(int grassColour, int foliageColour, int birchColor) {
            this(grassColour, -1.0F, foliageColour, -1.0F, birchColor);
        }

        @Override
        public int getGrassOverlay() {
            return this.grassOverlay;
        }

        @Override
        public float getGrassSaturationMultiplier() {
            return this.grassSaturationMultiplier;
        }

        @Override
        public int getFoliageOverlay() {
            return this.foliageOverlay;
        }

        @Override
        public float getFoliageSaturationMultiplier() {
            return this.foliageSaturationMultiplier;
        }

        @Override
        public int getBirchColor() {
            return this.birchColor;
        }
    }
}