package club.iananderson.seasonhud.config;

import net.minecraft.network.chat.Component;

public enum ShowDay {

    NONE(0, "none"), SHOW_DAY(1, "showDay"), SHOW_WITH_TOTAL_DAYS(2, "totalDays"), SHOW_WITH_MONTH(3, "showMonth");

    private final int idNum;

    private final String currentDayDisplay;

    private final Component dayDisplayName;

    private ShowDay(int id, String dayType) {
        this.idNum = id;
        this.currentDayDisplay = dayType;
        this.dayDisplayName = Component.translatable("showday.seasonhud." + dayType);
    }

    public int getId() {
        return this.idNum;
    }

    public String getDayDisplay() {
        return this.currentDayDisplay;
    }

    public Component getDayDisplayName() {
        return this.dayDisplayName;
    }
}