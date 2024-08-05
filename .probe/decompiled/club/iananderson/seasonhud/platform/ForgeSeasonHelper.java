package club.iananderson.seasonhud.platform;

import club.iananderson.seasonhud.Common;
import club.iananderson.seasonhud.client.SeasonHUDClient;
import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.platform.services.ISeasonHelper;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import sereneseasons.api.SSItems;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.ServerConfig;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

public class ForgeSeasonHelper implements ISeasonHelper {

    @Override
    public boolean isTropicalSeason() {
        boolean showTropicalSeasons = Config.showTropicalSeason.get();
        boolean isInTropicalSeason = SeasonHelper.usesTropicalSeasons(((ClientLevel) Objects.requireNonNull(SeasonHUDClient.mc.level)).m_204166_(SeasonHUDClient.mc.player.m_20097_()));
        return showTropicalSeasons && isInTropicalSeason;
    }

    @Override
    public boolean isSeasonTiedWithSystemTime() {
        return false;
    }

    @Override
    public String getCurrentSeasonState() {
        if (this.isTropicalSeason()) {
            return Config.showSubSeason.get() ? SeasonHelper.getSeasonState((Level) Objects.requireNonNull(SeasonHUDClient.mc.level)).getTropicalSeason().toString() : SeasonHelper.getSeasonState((Level) Objects.requireNonNull(SeasonHUDClient.mc.level)).getTropicalSeason().toString().substring(SeasonHelper.getSeasonState((Level) Objects.requireNonNull(SeasonHUDClient.mc.level)).getTropicalSeason().toString().length() - 3);
        } else {
            return Config.showSubSeason.get() ? SeasonHelper.getSeasonState((Level) Objects.requireNonNull(SeasonHUDClient.mc.level)).getSubSeason().toString() : SeasonHelper.getSeasonState((Level) Objects.requireNonNull(SeasonHUDClient.mc.level)).getSeason().toString();
        }
    }

    @Override
    public String getSeasonFileName() {
        if (this.isTropicalSeason()) {
            return this.getCurrentSeasonState().toLowerCase().substring(this.getCurrentSeasonState().toLowerCase().length() - 3);
        } else {
            return Config.showSubSeason.get() ? SeasonHelper.getSeasonState((Level) Objects.requireNonNull(SeasonHUDClient.mc.level)).getSeason().toString().toLowerCase() : this.getCurrentSeasonState().toLowerCase();
        }
    }

    @Override
    public int getDate() {
        ISeasonState seasonState = SeasonHelper.getSeasonState((Level) Objects.requireNonNull(SeasonHUDClient.mc.level));
        int subSeasonDuration = ServerConfig.subSeasonDuration.get();
        int seasonDay = seasonState.getDay();
        int seasonDate = seasonDay % (subSeasonDuration * 3) + 1;
        int subDate = seasonDay % subSeasonDuration + 1;
        int subTropDate = (seasonDay + subSeasonDuration * 3) % (subSeasonDuration * 2) + 1;
        if (Services.SEASON.isTropicalSeason()) {
            if (!Config.showSubSeason.get()) {
                subTropDate = (seasonDay + subSeasonDuration * 3) % (subSeasonDuration * 6) + 1;
            }
            return subTropDate;
        } else {
            return Config.showSubSeason.get() ? subDate : seasonDate;
        }
    }

    @Override
    public int seasonDuration() {
        int seasonDuration = ServerConfig.subSeasonDuration.get() * 3;
        if (this.isTropicalSeason()) {
            seasonDuration *= 2;
        }
        if (Config.showSubSeason.get()) {
            seasonDuration /= 3;
        }
        return seasonDuration;
    }

    @Override
    public Item calendar() {
        return SSItems.CALENDAR.get();
    }

    @Override
    public int findCuriosCalendar(Player player, Item item) {
        if (Common.curiosLoaded()) {
            List<SlotResult> findCalendar = CuriosApi.getCuriosHelper().findCurios(player, item);
            return findCalendar.size();
        } else {
            return 0;
        }
    }
}