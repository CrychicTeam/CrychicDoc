package dev.ftb.mods.ftblibrary.ui.misc;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.NordTheme;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.PanelScrollBar;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;
import dev.ftb.mods.ftblibrary.ui.WidgetType;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import org.apache.commons.lang3.tuple.Pair;

public class KeyReferenceScreen extends BaseScreen {

    private final Panel textPanel;

    private final PanelScrollBar scrollBar;

    private final SimpleTextButton closeButton;

    private final String[] translationKeys;

    private static final int SCROLLBAR_WIDTH = 16;

    private static final int GUTTER_SIZE = 2;

    public KeyReferenceScreen(String... translationKeys) {
        this.translationKeys = translationKeys;
        this.textPanel = new KeyReferenceScreen.TextPanel(this);
        this.closeButton = new SimpleTextButton(this, Component.translatable("gui.close"), Icons.CLOSE) {

            @Override
            public void onClicked(MouseButton button) {
                KeyReferenceScreen.this.onBack();
            }
        };
        this.scrollBar = new PanelScrollBar(this, this.textPanel);
    }

    @Override
    public boolean onInit() {
        return this.setSizeProportional(0.75F, 0.8F);
    }

    @Override
    public Theme getTheme() {
        return NordTheme.THEME;
    }

    @Override
    public void addWidgets() {
        this.add(this.textPanel);
        this.add(this.scrollBar);
        this.add(this.closeButton);
    }

    @Override
    public void alignWidgets() {
        int textPanelWidth = this.getGui().width - 6 - 16;
        this.textPanel.setPosAndSize(2, 2, textPanelWidth, this.getGui().height - 4);
        this.textPanel.alignWidgets();
        this.scrollBar.setPosAndSize(this.getGui().width - 2 - 16, this.textPanel.getPosY(), 16, this.textPanel.getHeight());
        this.closeButton.setPosAndSize(this.width + 2, 0, 20, 20);
    }

    @Override
    public Component getTitle() {
        return Component.translatable("ftblibrary.gui.key_reference");
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        theme.drawGui(graphics, x, y, w, h, WidgetType.NORMAL);
        int w1 = theme.getStringWidth(this.getTitle());
        theme.drawString(graphics, this.getTitle(), x + (w - w1) / 2, y - theme.getFontHeight() - 1, Color4I.rgb(65535), 2);
    }

    protected void drawTextBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        theme.drawPanelBackground(graphics, x, y, w, h);
    }

    private static List<Pair<Component, Component>> buildText(String... translationKeys) {
        List<Pair<Component, Component>> res = new ArrayList();
        for (String translationKey : translationKeys) {
            for (String line : I18n.get(translationKey).split("\\n")) {
                String[] parts = line.split(";", 2);
                switch(parts.length) {
                    case 0:
                        res.add(Pair.of(Component.empty(), Component.empty()));
                        break;
                    case 1:
                        res.add(Pair.of(Component.literal(parts[0]).withStyle(ChatFormatting.YELLOW, ChatFormatting.UNDERLINE), Component.empty()));
                        break;
                    default:
                        res.add(Pair.of(Component.literal(parts[0]), Component.literal(parts[1]).withStyle(ChatFormatting.GRAY)));
                }
            }
            res.add(Pair.of(Component.empty(), Component.empty()));
        }
        return res;
    }

    private class TextPanel extends Panel {

        private final KeyReferenceScreen.TwoColumnList textWidget = new KeyReferenceScreen.TwoColumnList(this, KeyReferenceScreen.buildText(KeyReferenceScreen.this.translationKeys));

        public TextPanel(Panel panel) {
            super(panel);
        }

        @Override
        public void addWidgets() {
            this.add(this.textWidget);
        }

        @Override
        public void alignWidgets() {
            this.align(WidgetLayout.VERTICAL);
            this.textWidget.setPos(4, 2);
            this.textWidget.setWidth(this.width);
        }

        @Override
        public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            KeyReferenceScreen.this.drawTextBackground(graphics, theme, x, y, w, h);
        }
    }

    private static class TwoColumnList extends Widget {

        private final int widestL;

        private final List<Pair<Component, Component>> data;

        private final List<Pair<Component, FormattedCharSequence>> reflowed = new ArrayList();

        public TwoColumnList(Panel p, List<Pair<Component, Component>> data) {
            super(p);
            this.data = data;
            this.widestL = (Integer) data.stream().map(e -> this.getGui().getTheme().getStringWidth((FormattedText) e.getLeft())).max(Integer::compareTo).orElse(0);
        }

        @Override
        public void setWidth(int v) {
            super.setWidth(v);
            this.reflowText();
        }

        private void reflowText() {
            Theme theme = this.getGui().getTheme();
            int h = 0;
            int maxWidth = this.getParent().getWidth() - 4;
            this.reflowed.clear();
            for (Pair<Component, Component> entry : this.data) {
                if (((Component) entry.getRight()).getString().isEmpty()) {
                    this.reflowed.add(Pair.of((Component) entry.getLeft(), null));
                    h += theme.getFontHeight() + 3;
                } else {
                    List<FormattedCharSequence> l = theme.getFont().split((FormattedText) entry.getRight(), maxWidth - 10 - this.widestL);
                    if (!l.isEmpty()) {
                        this.reflowed.add(Pair.of((Component) entry.getLeft(), (FormattedCharSequence) l.get(0)));
                        for (int i = 1; i < l.size(); i++) {
                            this.reflowed.add(Pair.of(Component.empty(), (FormattedCharSequence) l.get(i)));
                        }
                        h += (theme.getFontHeight() + 1) * l.size();
                    }
                }
            }
            this.height = h;
        }

        @Override
        public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            int yPos = y;
            for (Pair<Component, FormattedCharSequence> entry : this.reflowed) {
                boolean header = entry.getRight() == null;
                int leftWidth = theme.getStringWidth((FormattedText) entry.getLeft());
                int xOff = header ? (this.width - leftWidth) / 2 : this.widestL - leftWidth - 2;
                theme.drawString(graphics, entry.getLeft(), x + xOff, yPos);
                if (!header) {
                    theme.drawString(graphics, entry.getRight(), x + this.widestL + 10, yPos);
                }
                yPos += theme.getFontHeight() + (header ? 3 : 1);
            }
        }
    }
}