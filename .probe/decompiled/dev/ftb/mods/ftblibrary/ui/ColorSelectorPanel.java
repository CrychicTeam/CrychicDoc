package dev.ftb.mods.ftblibrary.ui;

import com.google.common.primitives.Ints;
import dev.ftb.mods.ftblibrary.FTBLibrary;
import dev.ftb.mods.ftblibrary.config.ColorConfig;
import dev.ftb.mods.ftblibrary.config.ConfigCallback;
import dev.ftb.mods.ftblibrary.config.FTBLibraryClientConfig;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TextComponentUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;

public class ColorSelectorPanel extends ModalPanel {

    private static final Icon WHEEL = Icon.getIcon(FTBLibrary.rl("textures/gui/rgbcolorwheel.png"));

    private static final MutableComponent ARGB = Component.literal("ARGB");

    private static final MutableComponent RGB = Component.literal("RGB");

    private final ColorConfig config;

    private final ConfigCallback callback;

    private final ColorSelectorPanel.BrightnessButton bButton;

    private final ColorSelectorPanel.HueSaturationButton hsButton;

    private final ColorSelectorPanel.AlphaButton aButton;

    private final ColorSelectorPanel.RGBTextBox rgbBox;

    private final Button acceptBtn;

    private final Button cancelBtn;

    private final ColorSelectorPanel.PaletteSelectorButton presetBtn;

    private final List<ColorSelectorPanel.PaletteButton> paletteButtons = new ArrayList();

    private final float[] hsb = new float[3];

    private boolean allowAlphaEdit = false;

    private static String curPalette = "chat";

    private static final Map<String, List<Integer>> PRESETS = new LinkedHashMap();

    public ColorSelectorPanel(Panel panel, ColorConfig config, ConfigCallback callback) {
        super(panel);
        this.config = config;
        this.callback = callback;
        this.setSize(224, 138);
        this.bButton = new ColorSelectorPanel.BrightnessButton();
        this.hsButton = new ColorSelectorPanel.HueSaturationButton();
        this.aButton = new ColorSelectorPanel.AlphaButton();
        this.rgbBox = new ColorSelectorPanel.RGBTextBox();
        this.presetBtn = new ColorSelectorPanel.PaletteSelectorButton();
        this.acceptBtn = SimpleTextButton.accept(this, mb -> this.done(true), Component.translatable("gui.accept"), TextComponentUtils.hotkeyTooltip("⇧ + Enter"));
        this.cancelBtn = SimpleTextButton.cancel(this, mb -> this.done(false), Component.translatable("gui.cancel"), TextComponentUtils.hotkeyTooltip("ESC"));
        for (int i = 0; i < 16; i++) {
            this.paletteButtons.add(new ColorSelectorPanel.PaletteButton());
        }
        this.updateHSB(config.getValue());
        this.selectPalette(curPalette);
    }

    public static ColorSelectorPanel popupAtMouse(BaseScreen gui, ColorConfig config, ConfigCallback callback) {
        ColorSelectorPanel selector = new ColorSelectorPanel(gui, config, callback);
        selector.setAllowAlphaEdit(config.isAllowAlphaEdit());
        int absX = Math.min(gui.getMouseX(), gui.getScreen().getGuiScaledWidth() - selector.width - 10);
        int absY = Math.min(gui.getMouseY(), gui.getScreen().getGuiScaledHeight() - selector.height - 10);
        selector.setPos(absX - selector.getParent().getX(), absY - selector.getParent().getY());
        gui.pushModalPanel(selector);
        return selector;
    }

    public void setAllowAlphaEdit(boolean allowAlphaEdit) {
        this.allowAlphaEdit = allowAlphaEdit;
    }

    @Override
    public void addWidgets() {
        this.addAll(List.of(this.bButton, this.hsButton, this.aButton, this.rgbBox, this.presetBtn, this.acceptBtn, this.cancelBtn));
        this.addAll(this.paletteButtons);
    }

    @Override
    public void alignWidgets() {
        this.bButton.setPosAndSize(5, 5, 16, 128);
        this.hsButton.setPosAndSize(26, 5, 96, 96);
        this.aButton.setPosAndSize(26, 106, 100, 27);
        this.rgbBox.setPosAndSize(159, 5, 60, 16);
        this.presetBtn.setPosAndSize(130, 26, 90, 16);
        this.acceptBtn.setPosAndSize(177, 113, 20, 20);
        this.cancelBtn.setPosAndSize(199, 113, 20, 20);
        for (int i = 0; i < this.paletteButtons.size(); i++) {
            int x = i % 4;
            int y = i / 4;
            ((ColorSelectorPanel.PaletteButton) this.paletteButtons.get(i)).setPosAndSize(132 + x * 12, 45 + y * 12, 10, 10);
        }
    }

    @Override
    public boolean keyPressed(Key key) {
        if (key.esc()) {
            this.done(false);
            return true;
        } else if (key.enter() && BaseScreen.isShiftKeyDown()) {
            this.done(true);
            return true;
        } else {
            return super.keyPressed(key);
        }
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        theme.drawContextMenuBackground(graphics, x - 1, y - 1, w + 2, h + 2);
        Color4I.GRAY.withAlpha(40).draw(graphics, x + 130, y + 43, 50, 50);
    }

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        super.draw(graphics, theme, x, y, w, h);
        theme.drawString(graphics, this.allowAlphaEdit ? ARGB : RGB, x + 157 - theme.getStringWidth(ARGB), y + 9);
    }

    private void done(boolean accept) {
        if (accept) {
            List<Integer> l = (List<Integer>) PRESETS.computeIfAbsent("recent", k -> new ArrayList());
            if (!l.contains(this.config.getValue().rgba())) {
                l.add(this.config.getValue().rgba());
                if (l.size() > 16) {
                    l.remove(0);
                }
                FTBLibraryClientConfig.RECENT.set(Ints.toArray(l));
                FTBLibraryClientConfig.save();
            }
        }
        this.callback.save(accept);
        this.getGui().popModalPanel();
    }

    private void setColor(Color4I newColor) {
        if (this.config.setCurrentValue(newColor)) {
            this.rgbBox.setTextFromColor(this.config.getValue());
        }
    }

    private void updateHSB(Color4I newColor) {
        Color4I.RGBtoHSB(newColor.redi(), newColor.greeni(), newColor.bluei(), this.hsb);
    }

    private void selectPalette(String paletteName) {
        if (PRESETS.containsKey(paletteName)) {
            curPalette = paletteName;
            this.presetBtn.setTitle(getPaletteName(curPalette).append(" ▼"));
            List<Integer> cols = (List<Integer>) PRESETS.get(paletteName);
            this.paletteButtons.forEach(b -> b.setIcon(Color4I.empty()));
            for (int i = 0; i < this.paletteButtons.size() && i < cols.size(); i++) {
                ((ColorSelectorPanel.PaletteButton) this.paletteButtons.get(i)).setIcon(Color4I.rgba((Integer) cols.get(i)));
            }
        }
    }

    private static MutableComponent getPaletteName(String palette) {
        return Component.translatable("ftblibrary.palette." + palette);
    }

    private static void setupPalettes() {
        PRESETS.put("chat", (List) Util.make(new ArrayList(), l -> Arrays.stream(ChatFormatting.values()).filter(ChatFormatting::m_126664_).map(ChatFormatting::m_126665_).forEach(e -> l.add(e | 0xFF000000))));
        PRESETS.put("dye", (List) Util.make(new ArrayList(), l -> Arrays.stream(DyeColor.values()).map(DyeColor::m_41071_).forEach(e -> l.add(e | 0xFF000000))));
        PRESETS.put("nord", List.of(-13749184, -12893614, -12366754, -11774358, -2564375, -1709584, -1249292, -7357253, -7814960, -8281663, -10583636, -4234902, -3111056, -1324149, -6046068, -4944211));
        PRESETS.put("reds", List.of(-11137779, -10743792, -9502720, -7077888, -3997439, -65536));
        PRESETS.put("greens", List.of(-14789071, -11757014, -8996325, -9913511, -5972478, -5447801));
        PRESETS.put("blues", List.of(-16776961, -16759553, -16750849, -13399809, -11162881, -8925953));
        PRESETS.put("recent", (List) Util.make(new ArrayList(), l -> Arrays.stream(FTBLibraryClientConfig.RECENT.get()).forEach(l::add)));
    }

    static {
        setupPalettes();
    }

    private class AlphaButton extends SimpleButton {

        public AlphaButton() {
            super(ColorSelectorPanel.this, Component.empty(), Color4I.empty(), (b, m) -> {
            });
        }

        @Override
        public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            GuiHelper.drawHollowRect(graphics, x, y, w, h, Color4I.BLACK, false);
            if (ColorSelectorPanel.this.allowAlphaEdit) {
                if (ColorSelectorPanel.this.config.getValue().alphai() < 255) {
                    GuiHelper.pushScissor(this.getScreen(), x, y, w, h);
                    for (int i = 0; i < w; i += 10) {
                        for (int j = 0; j < h; j += 10) {
                            Color4I c = (i + j) / 10 % 2 == 0 ? Color4I.WHITE : Color4I.GRAY;
                            c.draw(graphics, x + i, y + j, 10, 10);
                        }
                    }
                    GuiHelper.popScissor(this.getScreen());
                }
                ColorSelectorPanel.this.config.getValue().draw(graphics, x, y, w, h);
                int xVal = x + (w - 1) * ColorSelectorPanel.this.config.getValue().alphai() / 255;
                Color4I.BLACK.draw(graphics, xVal, y - 2, 3, this.height + 4);
                Color4I.GRAY.draw(graphics, xVal + 1, y - 1, 1, this.height + 2);
            } else {
                ColorSelectorPanel.this.config.getValue().draw(graphics, x, y, w, h);
            }
        }

        @Override
        public void onClicked(MouseButton button) {
            if (ColorSelectorPanel.this.allowAlphaEdit) {
                this.adjustToMouseX();
            }
        }

        @Override
        public boolean mouseDragged(int button, double dragX, double dragY) {
            if (ColorSelectorPanel.this.allowAlphaEdit && this.isMouseOver()) {
                this.adjustToMouseX();
                return true;
            } else {
                return false;
            }
        }

        private void adjustToMouseX() {
            int xVal = this.getWidth() - 1 - (this.getMouseX() - this.getX());
            int newA = 255 - Mth.clamp(xVal * 255 / this.getWidth(), 0, 255);
            ColorSelectorPanel.this.setColor(Color4I.rgb(Color4I.HSBtoRGB(ColorSelectorPanel.this.hsb[0], ColorSelectorPanel.this.hsb[1], ColorSelectorPanel.this.hsb[2])).withAlpha(newA));
        }
    }

    private class BrightnessButton extends SimpleButton {

        public BrightnessButton() {
            super(ColorSelectorPanel.this, Component.empty(), Color4I.empty(), (b, m) -> {
            });
        }

        @Override
        public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            Color4I bg = Color4I.rgb(Color4I.HSBtoRGB(ColorSelectorPanel.this.hsb[0], ColorSelectorPanel.this.hsb[1], 1.0F));
            GuiHelper.drawGradientRect(graphics, x, y, w, h, bg, Color4I.BLACK);
            GuiHelper.drawHollowRect(graphics, x, y, w, h, Color4I.BLACK, false);
        }

        @Override
        public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            super.draw(graphics, theme, x, y, w, h);
            int yVal = (int) ((float) y + (float) h * (1.0F - ColorSelectorPanel.this.hsb[2]));
            Color4I.BLACK.draw(graphics, x - 2, yVal, this.width + 4, 3);
            Color4I.GRAY.draw(graphics, x - 1, yVal + 1, this.width + 2, 1);
        }

        @Override
        public void onClicked(MouseButton button) {
            this.adjustToMouseY();
        }

        @Override
        public boolean mouseDragged(int button, double dragX, double dragY) {
            if (this.isMouseOver) {
                this.adjustToMouseY();
                return true;
            } else {
                return false;
            }
        }

        private void adjustToMouseY() {
            int yVal = this.getHeight() - 1 - (this.getMouseY() - this.getY());
            float newB = Mth.clamp((float) yVal / ((float) this.height - 1.0F), 0.0F, 1.0F);
            ColorSelectorPanel.this.hsb[2] = newB;
            ColorSelectorPanel.this.setColor(Color4I.rgb(Color4I.HSBtoRGB(ColorSelectorPanel.this.hsb[0], ColorSelectorPanel.this.hsb[1], ColorSelectorPanel.this.hsb[2])).withAlpha(ColorSelectorPanel.this.config.getValue().alphai()));
        }
    }

    private class HueSaturationButton extends SimpleButton {

        public HueSaturationButton() {
            super(ColorSelectorPanel.this, Component.empty(), Color4I.empty(), (b, m) -> {
            });
        }

        @Override
        public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            ColorSelectorPanel.WHEEL.draw(graphics, x, y, w, h);
        }

        @Override
        public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            super.draw(graphics, theme, x, y, w, h);
            int xc = this.getWidth() / 2;
            int yc = this.getHeight() / 2;
            double rad = (Math.PI / 2) - (double) ColorSelectorPanel.this.hsb[0] * Math.PI * 2.0;
            int dx = (int) ((double) xc + (double) ((float) xc * ColorSelectorPanel.this.hsb[1]) * Math.cos(rad));
            int dy = (int) ((double) yc - (double) ((float) yc * ColorSelectorPanel.this.hsb[1]) * Math.sin(rad));
            graphics.pose().pushPose();
            graphics.pose().translate((float) x, (float) y, 0.0F);
            Color4I.BLACK.draw(graphics, dx - 1, dy - 5, 3, 11);
            Color4I.BLACK.draw(graphics, dx - 5, dy - 1, 11, 3);
            Color4I.GRAY.draw(graphics, dx, dy - 4, 1, 9);
            Color4I.GRAY.draw(graphics, dx - 4, dy, 9, 1);
            graphics.pose().popPose();
        }

        @Override
        public void onClicked(MouseButton button) {
            this.adjustToMouseXY();
        }

        private boolean adjustToMouseXY() {
            int xc = this.getWidth() / 2;
            int yc = this.getHeight() / 2;
            int xVal = this.getMouseX() - this.getX();
            int yVal = this.getHeight() - 1 - (this.getMouseY() - this.getY());
            int dx = xVal - xc;
            int dy = yVal - yc;
            int dSq = dx * dx + dy * dy;
            if (dSq < xc * xc) {
                double a = flippedAtan2((double) dy, (double) dx);
                ColorSelectorPanel.this.hsb[0] = (float) (a / (Math.PI * 2));
                ColorSelectorPanel.this.hsb[1] = (float) Math.sqrt((double) ((float) dSq / (float) (xc * xc)));
                ColorSelectorPanel.this.setColor(Color4I.rgb(Color4I.HSBtoRGB(ColorSelectorPanel.this.hsb[0], ColorSelectorPanel.this.hsb[1], ColorSelectorPanel.this.hsb[2])).withAlpha(ColorSelectorPanel.this.config.getValue().alphai()));
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean mouseDragged(int button, double dragX, double dragY) {
            return this.adjustToMouseXY();
        }

        private static double flippedAtan2(double y, double x) {
            double angle = Math.atan2(y, x);
            double flippedAngle = (Math.PI / 2) - angle;
            return flippedAngle >= 0.0 ? flippedAngle : flippedAngle + (Math.PI * 2);
        }
    }

    private class PaletteButton extends SimpleButton {

        public PaletteButton() {
            super(ColorSelectorPanel.this, Component.empty(), Color4I.empty(), (b, mb) -> ((ColorSelectorPanel.PaletteButton) b).applyColor());
        }

        @Override
        public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            if (this.icon instanceof Color4I col && !col.isEmpty()) {
                col.draw(graphics, x, y, w, h);
                Color4I shade = col.addBrightness(-0.15F);
                shade.draw(graphics, x, y + h - 1, w, 1);
                shade.draw(graphics, x + w - 1, y, 1, h);
            }
        }

        private void applyColor() {
            if (this.icon instanceof Color4I col && !col.isEmpty()) {
                ColorSelectorPanel.this.setColor(col);
                ColorSelectorPanel.this.updateHSB(col);
            }
        }
    }

    private class PaletteSelectorButton extends SimpleTextButton {

        public PaletteSelectorButton() {
            super(ColorSelectorPanel.this, ColorSelectorPanel.getPaletteName(ColorSelectorPanel.curPalette).append(" ▼"), Color4I.empty());
        }

        @Override
        public void onClicked(MouseButton button) {
            List<ContextMenuItem> items = new ArrayList();
            ColorSelectorPanel.PRESETS.forEach((k, v) -> items.add(new ContextMenuItem(ColorSelectorPanel.getPaletteName(k), Color4I.empty(), b -> ColorSelectorPanel.this.selectPalette(k))));
            this.getGui().openContextMenu(items);
        }
    }

    private class RGBTextBox extends TextBox {

        private static final Pattern HEX = Pattern.compile("^[0-9a-fA-F]{1,8}$");

        public RGBTextBox() {
            super(ColorSelectorPanel.this);
            this.setTextFromColor(ColorSelectorPanel.this.config.getValue());
            this.setFilter(s -> {
                if (s.isEmpty()) {
                    return true;
                } else {
                    if (s.startsWith("#")) {
                        s = s.substring(1);
                    }
                    return s.isEmpty() || HEX.matcher(s).matches();
                }
            });
        }

        private void setTextFromColor(Color4I color) {
            if (ColorSelectorPanel.this.allowAlphaEdit) {
                this.setText(String.format("#%08x", color.rgba()));
            } else {
                this.setText(String.format("#%06x", color.rgb()));
            }
        }

        @Override
        public void onEnterPressed() {
            String s = this.getText();
            if (s.startsWith("#")) {
                s = s.substring(1);
            }
            if (s.length() == 6 || !ColorSelectorPanel.this.allowAlphaEdit) {
                s = "FF" + s;
            }
            try {
                int col = Integer.parseUnsignedInt(s, 16);
                ColorSelectorPanel.this.setColor(ColorSelectorPanel.this.allowAlphaEdit ? Color4I.rgba(col) : Color4I.rgb(col));
                ColorSelectorPanel.this.updateHSB(ColorSelectorPanel.this.config.getValue());
            } catch (NumberFormatException var3) {
            }
        }
    }
}