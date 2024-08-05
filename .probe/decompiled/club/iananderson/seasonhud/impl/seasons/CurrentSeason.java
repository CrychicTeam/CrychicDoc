package club.iananderson.seasonhud.impl.seasons;

import club.iananderson.seasonhud.Common;
import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.config.ShowDay;
import club.iananderson.seasonhud.platform.Services;
import java.time.LocalDateTime;
import java.util.ArrayList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public class CurrentSeason {

    public static String getSeasonStateLower() {
        return Services.SEASON.getCurrentSeasonState().toLowerCase();
    }

    public static String getSeasonIcon(String seasonFileName) {
        for (SeasonList season : SeasonList.values()) {
            if (season.getFileName().equals(seasonFileName)) {
                return season.getIconChar();
            }
        }
        return null;
    }

    public static int getTextColor(String seasonFileName) {
        for (SeasonList season : SeasonList.values()) {
            if (season.getFileName().equals(seasonFileName)) {
                return season.getSeasonColor();
            }
        }
        return 16777215;
    }

    public static ArrayList<Component> getSeasonHudName() {
        ArrayList<Component> text = new ArrayList();
        ShowDay showDay = Config.showDay.get();
        String fileName = Services.SEASON.getSeasonFileName();
        Style SEASON_FORMAT = Style.EMPTY;
        if (Config.enableSeasonNameColor.get()) {
            SEASON_FORMAT = Style.EMPTY.withColor(getTextColor(fileName));
        }
        switch(showDay) {
            case NONE:
                text.add(Component.translatable("desc.seasonhud.icon", getSeasonIcon(fileName)).withStyle(Common.SEASON_STYLE));
                text.add(Component.translatable("desc.seasonhud.summary", Component.translatable("desc.seasonhud." + getSeasonStateLower())).withStyle(SEASON_FORMAT));
                break;
            case SHOW_DAY:
                text.add(Component.translatable("desc.seasonhud.icon", getSeasonIcon(fileName)).withStyle(Common.SEASON_STYLE));
                text.add(Component.translatable("desc.seasonhud.detailed", Component.translatable("desc.seasonhud." + getSeasonStateLower()), Services.SEASON.getDate()).withStyle(SEASON_FORMAT));
                break;
            case SHOW_WITH_TOTAL_DAYS:
                text.add(Component.translatable("desc.seasonhud.icon", getSeasonIcon(fileName)).withStyle(Common.SEASON_STYLE));
                text.add(Component.translatable("desc.seasonhud.detailed.total", Component.translatable("desc.seasonhud." + getSeasonStateLower()), Services.SEASON.getDate(), Services.SEASON.seasonDuration()).withStyle(SEASON_FORMAT));
                break;
            case SHOW_WITH_MONTH:
                text.add(Component.translatable("desc.seasonhud.icon", getSeasonIcon(fileName)).withStyle(Common.SEASON_STYLE));
                if (Services.SEASON.isSeasonTiedWithSystemTime()) {
                    String currentMonth = LocalDateTime.now().getMonth().name().toLowerCase();
                    text.add(Component.translatable("desc.seasonhud.month", Component.translatable("desc.seasonhud." + getSeasonStateLower()), Component.translatable("desc.seasonhud." + currentMonth), Services.SEASON.getDate()).withStyle(SEASON_FORMAT));
                } else {
                    text.add(Component.translatable("desc.seasonhud.detailed", Component.translatable("desc.seasonhud." + getSeasonStateLower()), Services.SEASON.getDate()).withStyle(SEASON_FORMAT));
                }
        }
        return text;
    }
}