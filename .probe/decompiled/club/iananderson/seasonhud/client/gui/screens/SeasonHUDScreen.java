package club.iananderson.seasonhud.client.gui.screens;

import club.iananderson.seasonhud.client.SeasonHUDClient;
import club.iananderson.seasonhud.client.gui.components.buttons.MenuButton;
import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.config.Location;
import club.iananderson.seasonhud.config.ShowDay;
import club.iananderson.seasonhud.platform.Services;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class SeasonHUDScreen extends Screen {

    private static final int MENU_PADDING_FULL = 50;

    private static final int PADDING = 4;

    private static final int BUTTON_WIDTH = 180;

    private static final int BUTTON_HEIGHT = 20;

    private static final Component TITLE = Component.translatable("menu.seasonhud.title");

    private static final Component JOURNEYMAP = Component.translatable("menu.seasonhud.journeymap");

    private static final SeasonHUDScreen instance = new SeasonHUDScreen();

    public SeasonHUDScreen() {
        super(TITLE);
    }

    public static SeasonHUDScreen getInstance() {
        return instance;
    }

    public static void open() {
        SeasonHUDClient.mc.setScreen(getInstance());
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    private void onDone() {
        SeasonHUDClient.mc.options.save();
        SeasonHUDClient.mc.setScreen(null);
    }

    @Override
    public void render(@NotNull GuiGraphics stack, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(stack);
        stack.drawCenteredString(this.f_96547_, TITLE, this.f_96543_ / 2, 4, 16777215);
        if (Services.PLATFORM.isModLoaded("journeymap")) {
            stack.drawCenteredString(this.f_96547_, JOURNEYMAP, this.f_96543_ / 2, 194 - (9 + 4), 16777215);
        }
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void init() {
        super.init();
        boolean isForge = Services.PLATFORM.getPlatformName().equals("Forge");
        int leftButtonX = this.f_96543_ / 2 - 184;
        int rightButtonX = this.f_96543_ / 2 + 4;
        int buttonStartY = 50;
        int yOffset = 24;
        MenuButton doneButton = new MenuButton(this.f_96543_ / 2 - 90, this.f_96544_ - 20 - 4, 180, 20, MenuButton.MenuButtons.DONE, press -> this.onDone());
        int row = 0;
        CycleButton<Boolean> enableModButton = CycleButton.onOffBuilder(Config.enableMod.get()).create(leftButtonX, buttonStartY + row * yOffset, 180, 20, Component.translatable("menu.seasonhud.button.enableMod"), (b, Off) -> Config.setEnableMod(Off));
        MenuButton seasonhudColors = new MenuButton(rightButtonX, buttonStartY + row * yOffset, 180, 20, MenuButton.MenuButtons.COLORS, press -> ColorScreen.open(this));
        CycleButton<Location> hudLocationButton = CycleButton.<Location>builder(Location::getLocationName).withValues(Location.TOP_LEFT, Location.TOP_CENTER, Location.TOP_RIGHT, Location.BOTTOM_LEFT, Location.BOTTOM_RIGHT).withInitialValue(Config.hudLocation.get()).create(leftButtonX, buttonStartY + ++row * yOffset, 180, 20, Component.translatable("menu.seasonhud.button.hudLocation"), (b, location) -> Config.setHudLocation(location));
        CycleButton<Boolean> showTropicalSeasonButton = CycleButton.onOffBuilder(Config.showTropicalSeason.get()).create(rightButtonX, buttonStartY + row * yOffset, 180, 20, Component.translatable("menu.seasonhud.button.showTropicalSeason"), (b, Off) -> Config.setShowTropicalSeason(Off));
        row++;
        ShowDay[] showDayValuesForge = new ShowDay[] { ShowDay.NONE, ShowDay.SHOW_DAY, ShowDay.SHOW_WITH_TOTAL_DAYS };
        ShowDay[] showDayValuesFabric = new ShowDay[] { ShowDay.NONE, ShowDay.SHOW_DAY, ShowDay.SHOW_WITH_TOTAL_DAYS, ShowDay.SHOW_WITH_MONTH };
        CycleButton<ShowDay> showDayButton = CycleButton.<ShowDay>builder(ShowDay::getDayDisplayName).withValues(isForge ? showDayValuesForge : showDayValuesFabric).withInitialValue(Config.showDay.get()).create(leftButtonX, buttonStartY + row * yOffset, 180, 20, Component.translatable("menu.seasonhud.button.showDay"), (b, showDay) -> Config.setShowDay(showDay));
        CycleButton<Boolean> showSubSeasonButton = CycleButton.onOffBuilder(Config.showSubSeason.get()).create(rightButtonX, buttonStartY + row * yOffset, 180, 20, Component.translatable("menu.seasonhud.button.showSubSeason"), (b, Off) -> Config.setShowSubSeason(Off));
        CycleButton<Boolean> needCalendarButton = CycleButton.onOffBuilder(Config.needCalendar.get()).create(leftButtonX, buttonStartY + ++row * yOffset, 180, 20, Component.translatable("menu.seasonhud.button.needCalendar"), (b, Off) -> Config.setNeedCalendar(Off));
        CycleButton<Boolean> enableMinimapIntegrationButton = CycleButton.onOffBuilder(Config.enableMinimapIntegration.get()).create(rightButtonX, buttonStartY + row * yOffset, 180, 20, Component.translatable("menu.seasonhud.button.enableMinimapIntegration"), (b, Off) -> Config.setEnableMinimapIntegration(Off));
        CycleButton<Boolean> showMinimapHiddenButton = CycleButton.onOffBuilder(Config.showDefaultWhenMinimapHidden.get()).create(leftButtonX, buttonStartY + ++row * yOffset, 180, 20, Component.translatable("menu.seasonhud.button.showMinimapHidden"), (b, Off) -> Config.setShowDefaultWhenMinimapHidden(Off));
        if (Services.PLATFORM.isModLoaded("journeymap")) {
            row += 2;
            CycleButton<Boolean> journeyMapAboveMapButton = CycleButton.onOffBuilder(Config.journeyMapAboveMap.get()).create(leftButtonX, buttonStartY + row * yOffset, 180, 20, Component.translatable("menu.seasonhud.button.journeyMapAboveMap"), (b, Off) -> Config.setJourneyMapAboveMap(Off));
            CycleButton<Boolean> journeyMapMacOSButton = CycleButton.onOffBuilder(Config.journeyMapMacOS.get()).create(rightButtonX, buttonStartY + row * yOffset, 180, 20, Component.translatable("menu.seasonhud.button.journeyMapMacOS"), (b, Off) -> Config.setJourneyMapMacOS(Off));
            this.m_142416_(journeyMapAboveMapButton);
            this.m_142416_(journeyMapMacOSButton);
        }
        this.m_142416_(enableModButton);
        this.m_142416_(hudLocationButton);
        this.m_142416_(showTropicalSeasonButton);
        this.m_142416_(showSubSeasonButton);
        this.m_142416_(showDayButton);
        this.m_142416_(needCalendarButton);
        this.m_142416_(enableMinimapIntegrationButton);
        this.m_142416_(showMinimapHiddenButton);
        this.m_142416_(seasonhudColors);
        this.m_142416_(doneButton);
    }
}