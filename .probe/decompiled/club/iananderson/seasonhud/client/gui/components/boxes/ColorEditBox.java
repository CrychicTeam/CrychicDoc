package club.iananderson.seasonhud.client.gui.components.boxes;

import club.iananderson.seasonhud.Common;
import club.iananderson.seasonhud.client.SeasonHUDClient;
import club.iananderson.seasonhud.client.gui.screens.ColorScreen;
import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.impl.seasons.SeasonList;
import club.iananderson.seasonhud.platform.Services;
import club.iananderson.seasonhud.util.Rgb;
import java.util.EnumSet;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;

public class ColorEditBox extends EditBox {

    private static final int PADDING = 4;

    private final SeasonList boxSeason;

    private final int seasonColor;

    private int newSeasonColor;

    public ColorEditBox(Font font, int x, int y, int width, int height, SeasonList season) {
        super(font, x, y, width, height, season.getSeasonName());
        this.boxSeason = season;
        this.seasonColor = season.getSeasonColor();
        this.newSeasonColor = this.seasonColor;
        this.m_94199_(8);
        this.m_94144_(String.valueOf(this.seasonColor));
        this.m_94151_(colorString -> {
            if (this.validate(colorString)) {
                this.m_94202_(16777215);
                int colorInt = Integer.parseInt(colorString);
                if (colorInt != this.newSeasonColor) {
                    this.newSeasonColor = colorInt;
                    this.m_94144_(colorString);
                }
                ColorScreen.doneButton.f_93623_ = true;
            } else {
                this.m_94202_(16733525);
                ColorScreen.doneButton.f_93623_ = false;
            }
        });
    }

    private static EnumSet<SeasonList> seasonListSet() {
        EnumSet<SeasonList> set = SeasonList.seasons.clone();
        if (!Config.showTropicalSeason.get() || !Services.PLATFORM.getPlatformName().equals("Forge")) {
            set.remove(SeasonList.DRY);
            set.remove(SeasonList.WET);
        }
        return set;
    }

    private boolean inBounds(int color) {
        int minColor = 0;
        int maxColor = 16777215;
        return color >= minColor && color <= maxColor;
    }

    public boolean validate(String colorString) {
        try {
            int colorInt = Integer.parseInt(colorString);
            return this.inBounds(colorInt);
        } catch (NumberFormatException var3) {
            return false;
        }
    }

    public void save() {
        Rgb.setRgb(this.boxSeason, this.newSeasonColor);
        this.boxSeason.setColor(this.newSeasonColor);
    }

    public int getColor() {
        return this.seasonColor;
    }

    public int getNewColor() {
        return this.newSeasonColor;
    }

    public SeasonList getSeason() {
        return this.boxSeason;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        String seasonIcon = this.boxSeason.getIconChar();
        String seasonFileName = this.boxSeason.getFileName();
        boolean enableSeasonNameColor = Config.enableSeasonNameColor.get();
        this.m_94186_(enableSeasonNameColor);
        Style SEASON_FORMAT = Style.EMPTY;
        if (enableSeasonNameColor) {
            SEASON_FORMAT = Style.EMPTY.withColor(this.newSeasonColor);
        }
        Component icon = Component.translatable("desc.seasonhud.icon", seasonIcon).withStyle(Common.SEASON_STYLE);
        Component season = Component.translatable("desc.seasonhud.summary", Component.translatable("desc.seasonhud." + seasonFileName)).withStyle(SEASON_FORMAT);
        int widgetTotalSize = 86 * seasonListSet().size();
        int scaledWidth = SeasonHUDClient.mc.getWindow().getGuiScaledWidth();
        if (this.boxSeason == SeasonList.DRY && scaledWidth < widgetTotalSize) {
            season = Component.translatable("menu.seasonhud.color.editbox.dryColor").withStyle(SEASON_FORMAT);
        }
        if (this.boxSeason == SeasonList.WET && scaledWidth < widgetTotalSize) {
            season = Component.translatable("menu.seasonhud.color.editbox.wetColor").withStyle(SEASON_FORMAT);
        }
        MutableComponent seasonCombined = Component.translatable("desc.seasonhud.combined", icon, season);
        graphics.pose().pushPose();
        float scale = 1.0F;
        if (SeasonHUDClient.mc.font.width(seasonCombined) > this.m_5711_() - 4) {
            scale = ((float) this.m_5711_() - 4.0F) / (float) SeasonHUDClient.mc.font.width(seasonCombined);
        }
        graphics.pose().scale(scale, scale, 1.0F);
        graphics.drawCenteredString(SeasonHUDClient.mc.font, seasonCombined, (int) (((double) this.m_252754_() + (double) this.m_5711_() / 2.0) / (double) scale), (int) (((float) this.m_252907_() - 9.0F * scale - 4.0F) / scale), 16777215);
        graphics.pose().popPose();
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
    }
}