package club.iananderson.seasonhud.client.gui.screens;

import club.iananderson.seasonhud.client.SeasonHUDClient;
import club.iananderson.seasonhud.client.gui.components.boxes.ColorEditBox;
import club.iananderson.seasonhud.client.gui.components.buttons.DefaultColorButton;
import club.iananderson.seasonhud.client.gui.components.buttons.MenuButton;
import club.iananderson.seasonhud.client.gui.components.sliders.BlueSlider;
import club.iananderson.seasonhud.client.gui.components.sliders.GreenSlider;
import club.iananderson.seasonhud.client.gui.components.sliders.RedSlider;
import club.iananderson.seasonhud.client.gui.components.sliders.RgbSlider;
import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.impl.seasons.SeasonList;
import club.iananderson.seasonhud.platform.Services;
import club.iananderson.seasonhud.util.Rgb;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ColorScreen extends Screen {

    public static final int WIDGET_PADDING = 6;

    private static final int MENU_PADDING_FULL = 25;

    private static final int BUTTON_WIDTH = 150;

    private static final int BUTTON_HEIGHT = 20;

    private static final Component TITLE = Component.translatable("menu.seasonhud.color.title");

    private static final Component ENABLE_SEASON_NAME_COLOR = Component.translatable("menu.seasonhud.color.button.enableSeasonNameColor");

    private static final ColorScreen instance = new ColorScreen(SeasonHUDScreen.getInstance());

    public static MenuButton doneButton;

    private final Screen lastScreen;

    private final List<ColorEditBox> seasonBoxes = new ArrayList();

    private final List<AbstractWidget> widgets = new ArrayList();

    private MenuButton cancelButton;

    private CycleButton<Boolean> seasonNameColorButton;

    private int x;

    private int y;

    public ColorScreen(Screen screen) {
        super(TITLE);
        this.lastScreen = screen;
        this.widgets.toArray().clone();
    }

    public static void open(Screen screen) {
        SeasonHUDClient.mc.setScreen(new ColorScreen(screen));
    }

    private static EnumSet<SeasonList> seasonListSet() {
        EnumSet<SeasonList> set = SeasonList.seasons.clone();
        if (!Config.showTropicalSeason.get() || !Services.PLATFORM.getPlatformName().equals("Forge")) {
            set.remove(SeasonList.DRY);
            set.remove(SeasonList.WET);
        }
        return set;
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    public int getWidth() {
        return SeasonHUDClient.mc.getWindow().getGuiScaledWidth();
    }

    public int getHeight() {
        return SeasonHUDClient.mc.getWindow().getGuiScaledHeight();
    }

    @Override
    public void tick() {
        this.seasonBoxes.forEach(EditBox::m_94120_);
        super.tick();
    }

    private void onDone() {
        this.seasonBoxes.forEach(seasonBoxes -> {
            if (Integer.parseInt(seasonBoxes.m_94155_()) != seasonBoxes.getColor()) {
                seasonBoxes.save();
            }
        });
        SeasonHUDClient.mc.setScreen(this.lastScreen);
    }

    private void onCancel() {
        SeasonHUDClient.mc.setScreen(this.lastScreen);
    }

    public int getBoxWidth() {
        int widgetCount = seasonListSet().size();
        int widgetTotalSize = 86 * widgetCount;
        int scaledWidth = this.getWidth();
        int boxWidth;
        if (scaledWidth < widgetTotalSize) {
            boxWidth = 60;
        } else {
            boxWidth = 80;
        }
        return boxWidth;
    }

    private List<AbstractWidget> seasonWidget(int x, int y, SeasonList season) {
        List<AbstractWidget> seasonWidgetList = new ArrayList();
        ColorEditBox colorBox = new ColorEditBox(this.f_96547_, x, y, this.getBoxWidth(), 20, season);
        this.seasonBoxes.add(colorBox);
        seasonWidgetList.add(colorBox);
        y += colorBox.m_93694_() + 6;
        x--;
        y += 20 + RgbSlider.SLIDER_PADDING;
        RedSlider redSlider = new RedSlider(x, y, colorBox);
        seasonWidgetList.add(redSlider);
        y += redSlider.m_93694_() + RgbSlider.SLIDER_PADDING;
        GreenSlider greenSlider = new GreenSlider(x, y, colorBox);
        seasonWidgetList.add(greenSlider);
        y += greenSlider.m_93694_() + RgbSlider.SLIDER_PADDING;
        BlueSlider blueSlider = new BlueSlider(x, y, colorBox);
        seasonWidgetList.add(blueSlider);
        y -= greenSlider.m_93694_() + redSlider.m_93694_() + RgbSlider.SLIDER_PADDING + 20 + RgbSlider.SLIDER_PADDING;
        seasonWidgetList.add(new DefaultColorButton(x, y, season, colorBox, button -> {
            int defaultColorInt = season.getDefaultColor();
            if (colorBox.getNewColor() != defaultColorInt) {
                redSlider.setSliderValue(defaultColorInt);
                greenSlider.setSliderValue(defaultColorInt);
                blueSlider.setSliderValue(defaultColorInt);
                colorBox.m_94144_(String.valueOf(defaultColorInt));
                Rgb.setRgb(season, defaultColorInt);
            }
        }));
        return seasonWidgetList;
    }

    @Override
    public void init() {
        this.widgets.clear();
        int scaledWidth = this.getWidth();
        int leftButtonX = this.getWidth() / 2 - 156;
        int rightButtonX = this.getWidth() / 2 + 6;
        int widgetWidth = this.getBoxWidth() + 6;
        int totalWidgetWidth = seasonListSet().size() * widgetWidth - 6;
        this.x = scaledWidth / 2 - totalWidgetWidth / 2;
        this.y = 71;
        seasonListSet().forEach(season -> {
            this.widgets.addAll(this.seasonWidget(this.x, this.y, season));
            this.x += widgetWidth;
        });
        this.seasonNameColorButton = CycleButton.onOffBuilder(Config.enableSeasonNameColor.get()).create(leftButtonX, 25, 150, 20, ENABLE_SEASON_NAME_COLOR, (b, enableColor) -> Config.setEnableSeasonNameColor(enableColor));
        this.widgets.add(this.seasonNameColorButton);
        doneButton = new MenuButton(leftButtonX, this.getHeight() - 20 - 6, MenuButton.MenuButtons.DONE, press -> this.onDone());
        this.widgets.add(doneButton);
        this.cancelButton = new MenuButton(rightButtonX, this.getHeight() - 20 - 6, MenuButton.MenuButtons.CANCEL, press -> this.onCancel());
        this.widgets.add(this.cancelButton);
        this.widgets.forEach(x$0 -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(x$0);
        });
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(graphics);
        graphics.drawCenteredString(this.f_96547_, TITLE, this.getWidth() / 2, 6, 16777215);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }
}