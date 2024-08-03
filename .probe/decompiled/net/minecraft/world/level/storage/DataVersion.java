package net.minecraft.world.level.storage;

public class DataVersion {

    private final int version;

    private final String series;

    public static String MAIN_SERIES = "main";

    public DataVersion(int int0) {
        this(int0, MAIN_SERIES);
    }

    public DataVersion(int int0, String string1) {
        this.version = int0;
        this.series = string1;
    }

    public boolean isSideSeries() {
        return !this.series.equals(MAIN_SERIES);
    }

    public String getSeries() {
        return this.series;
    }

    public int getVersion() {
        return this.version;
    }

    public boolean isCompatible(DataVersion dataVersion0) {
        return this.getSeries().equals(dataVersion0.getSeries());
    }
}