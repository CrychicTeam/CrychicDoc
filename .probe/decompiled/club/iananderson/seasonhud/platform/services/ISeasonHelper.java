package club.iananderson.seasonhud.platform.services;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public interface ISeasonHelper {

    boolean isTropicalSeason();

    boolean isSeasonTiedWithSystemTime();

    String getCurrentSeasonState();

    String getSeasonFileName();

    int getDate();

    int seasonDuration();

    Item calendar();

    int findCuriosCalendar(Player var1, Item var2);
}