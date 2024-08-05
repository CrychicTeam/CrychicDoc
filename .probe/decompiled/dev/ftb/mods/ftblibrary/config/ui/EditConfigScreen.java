package dev.ftb.mods.ftblibrary.config.ui;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.ConfigValue;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.icon.MutableColor4I;
import dev.ftb.mods.ftblibrary.math.Bits;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleButton;
import dev.ftb.mods.ftblibrary.ui.TextField;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.VerticalSpaceWidget;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.ui.misc.AbstractThreePanelScreen;
import dev.ftb.mods.ftblibrary.util.TextComponentUtils;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.mutable.MutableInt;

public class EditConfigScreen extends AbstractThreePanelScreen<EditConfigScreen.ConfigPanel> {

    private final ConfigGroup group;

    private final Component title;

    private final List<Widget> allConfigButtons;

    private final Button buttonCollapseAll;

    private final Button buttonExpandAll;

    private int groupSize = 0;

    private boolean autoclose = false;

    private int widestKey = 0;

    private int widestValue = 0;

    private boolean changed = false;

    public EditConfigScreen(ConfigGroup configGroup) {
        this.group = configGroup;
        this.title = configGroup.getName().copy().withStyle(ChatFormatting.BOLD);
        this.allConfigButtons = new ArrayList();
        List<ConfigValue<?>> list = new ArrayList();
        this.collectAllConfigValues(this.group, list);
        if (!list.isEmpty()) {
            list.sort(null);
            EditConfigScreen.ConfigGroupButton group = null;
            for (ConfigValue<?> value : list) {
                if (group == null || group.group != value.getGroup()) {
                    this.allConfigButtons.add(new VerticalSpaceWidget(this.mainPanel, 4));
                    group = new EditConfigScreen.ConfigGroupButton(this.mainPanel, value.getGroup());
                    this.allConfigButtons.add(group);
                    this.groupSize++;
                }
                EditConfigScreen.ConfigEntryButton<?> btn = new EditConfigScreen.ConfigEntryButton<>(this.mainPanel, group, value);
                this.allConfigButtons.add(btn);
            }
            if (this.groupSize == 1) {
                this.allConfigButtons.remove(group);
            }
        }
        this.buttonExpandAll = new SimpleButton(this.topPanel, List.of(Component.translatable("gui.expand_all"), TextComponentUtils.hotkeyTooltip("="), TextComponentUtils.hotkeyTooltip("+")), Icons.UP, (widget, button) -> this.toggleAll(false));
        this.buttonCollapseAll = new SimpleButton(this.topPanel, List.of(Component.translatable("gui.collapse_all"), TextComponentUtils.hotkeyTooltip("-")), Icons.DOWN, (widget, button) -> this.toggleAll(true));
    }

    private void toggleAll(boolean collapsed) {
        for (Widget w : this.allConfigButtons) {
            if (w instanceof EditConfigScreen.ConfigGroupButton cgb) {
                cgb.setCollapsed(collapsed);
            }
        }
        this.scrollBar.setValue(0.0);
        this.getGui().refreshWidgets();
    }

    private void collectAllConfigValues(ConfigGroup group, List<ConfigValue<?>> list) {
        list.addAll(group.getValues());
        for (ConfigGroup subgroup : group.getSubgroups()) {
            this.collectAllConfigValues(subgroup, list);
        }
    }

    @Override
    public boolean onInit() {
        this.widestKey = this.widestValue = 0;
        MutableInt widestGroup = new MutableInt(0);
        MutableInt cfgHeight = new MutableInt(0);
        this.allConfigButtons.forEach(w -> {
            if (w instanceof EditConfigScreen.ConfigEntryButton<?> eb) {
                this.widestKey = Math.max(this.widestKey, this.getTheme().getFont().width(eb.keyText));
                this.widestValue = Math.max(this.widestValue, this.getTheme().getFont().width(eb.getValueStr()));
            } else if (w instanceof EditConfigScreen.ConfigGroupButton gb) {
                widestGroup.setValue(Math.max(widestGroup.intValue(), this.getTheme().getStringWidth(gb.title)));
            }
            cfgHeight.add(w.height + 2);
        });
        this.setHeight(Mth.clamp(cfgHeight.intValue() + this.getTopPanelHeight() + 25, 100, (int) ((float) this.getScreen().getGuiScaledHeight() * 0.9F)));
        this.setWidth(Mth.clamp(Math.max(this.widestKey + this.widestValue, widestGroup.intValue()) + 50, 176, (int) ((float) this.getScreen().getGuiScaledWidth() * 0.9F)));
        return true;
    }

    public EditConfigScreen setAutoclose(boolean autoclose) {
        this.autoclose = autoclose;
        return this;
    }

    @Override
    protected int getTopPanelHeight() {
        return 20;
    }

    @Override
    protected Panel createTopPanel() {
        return new EditConfigScreen.CustomTopPanel();
    }

    protected EditConfigScreen.ConfigPanel createMainPanel() {
        return new EditConfigScreen.ConfigPanel();
    }

    @Override
    protected void doAccept() {
        this.group.save(true);
        if (this.autoclose) {
            this.closeGui();
        }
    }

    @Override
    protected void doCancel() {
        if (this.changed) {
            this.openYesNo(Component.translatable("ftblibrary.unsaved_changes"), Component.empty(), this::reallyCancel);
        } else {
            this.reallyCancel();
        }
    }

    private void reallyCancel() {
        this.group.save(false);
        if (this.autoclose) {
            this.closeGui();
        }
    }

    @Override
    public boolean onClosedByKey(Key key) {
        if (super.onClosedByKey(key)) {
            this.doCancel();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean keyPressed(Key key) {
        if (super.keyPressed(key)) {
            return true;
        } else if ((key.is(257) || key.is(335)) && key.modifiers.shift()) {
            this.doAccept();
            return true;
        } else {
            if (key.is(334) || key.is(61)) {
                this.buttonExpandAll.onClicked(MouseButton.LEFT);
            } else if (key.is(45) || key.is(333)) {
                this.buttonCollapseAll.onClicked(MouseButton.LEFT);
            }
            return false;
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    private class ConfigEntryButton<T> extends Button implements EditStringConfigOverlay.PosProvider {

        private final EditConfigScreen.ConfigGroupButton groupButton;

        private final ConfigValue<T> configValue;

        private final Component keyText;

        public ConfigEntryButton(Panel panel, EditConfigScreen.ConfigGroupButton groupButton, ConfigValue<T> configValue) {
            super(panel);
            this.setHeight(EditConfigScreen.this.getTheme().getFontHeight() + 2);
            this.groupButton = groupButton;
            this.configValue = configValue;
            this.keyText = this.configValue.getCanEdit() ? Component.literal(this.configValue.getName()) : Component.literal(this.configValue.getName()).withStyle(ChatFormatting.GRAY);
        }

        @Override
        public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            theme.drawString(graphics, this.keyText, x + 5, y + 2, Bits.setFlag(0, 2, this.isMouseOver()));
            Component valueText = this.configValue.getStringForGUI(this.configValue.getValue());
            int maxLen = this.width - (EditConfigScreen.this.scrollBar.shouldDraw() ? EditConfigScreen.this.scrollBar.width : 0) - EditConfigScreen.this.widestKey;
            if (theme.getStringWidth(valueText) > maxLen) {
                valueText = Component.literal(theme.trimStringToWidth(valueText, maxLen).getString().trim() + "...");
            }
            MutableColor4I textCol = this.configValue.getColor().mutable();
            textCol.setAlpha(255);
            if (this.isMouseOver()) {
                textCol.addBrightness(60);
                Color4I.WHITE.withAlpha(33).draw(graphics, x, y, w, h);
            }
            Color4I.GRAY.withAlpha(33).draw(graphics, x + EditConfigScreen.this.widestKey + 10, y, 1, this.height);
            theme.drawString(graphics, valueText, x + EditConfigScreen.this.widestKey + 15, y + 2, textCol, 0);
        }

        @Override
        public void onClicked(MouseButton button) {
            if (this.getMouseY() >= 20) {
                this.playClickSound();
                this.configValue.onClicked(this, button, accepted -> {
                    if (accepted) {
                        EditConfigScreen.this.changed = true;
                    }
                    this.run();
                });
            }
        }

        @Override
        public void addMouseOverText(TooltipList list) {
            if (this.getMouseY() > 18) {
                list.add(this.keyText.copy().withStyle(ChatFormatting.UNDERLINE));
                String tooltip = this.configValue.getTooltip();
                if (!tooltip.isEmpty()) {
                    for (String s : tooltip.split("\n")) {
                        list.styledString(s, Style.EMPTY.withItalic(true).withColor(TextColor.fromLegacyFormat(ChatFormatting.GRAY)));
                    }
                }
                list.blankLine();
                this.configValue.addInfo(list);
            }
        }

        Component getValueStr() {
            return this.configValue.getStringForGUI(this.configValue.getValue());
        }

        @Override
        public EditStringConfigOverlay.PosProvider.Offset getOverlayOffset() {
            return new EditStringConfigOverlay.PosProvider.Offset(EditConfigScreen.this.widestKey + 12, -2);
        }
    }

    public static class ConfigGroupButton extends Button {

        private final ConfigGroup group;

        private final MutableComponent title;

        private final MutableComponent info;

        private boolean collapsed = false;

        public ConfigGroupButton(Panel panel, ConfigGroup g) {
            super(panel);
            this.setHeight(14);
            this.group = g;
            if (this.group.getParent() != null) {
                List<ConfigGroup> groups;
                for (groups = new ArrayList(); g.getParent() != null; g = g.getParent()) {
                    groups.add(g);
                }
                this.title = (MutableComponent) groups.stream().map(grp -> Component.translatable(grp.getNameKey()).withStyle(ChatFormatting.YELLOW)).reduce((g1, g2) -> g2.append(Component.literal(" → ").withStyle(ChatFormatting.GOLD)).append(g1)).orElse(Component.empty());
            } else {
                this.title = Component.translatable("stat.generalButton").withStyle(ChatFormatting.YELLOW);
            }
            String infoKey = this.group.getPath() + ".info";
            this.info = I18n.exists(infoKey) ? Component.translatable(infoKey) : null;
            this.setCollapsed(this.collapsed);
        }

        public void setCollapsed(boolean collapsed) {
            this.collapsed = collapsed;
            this.setTitle(Component.literal(this.collapsed ? "▶ " : "▼ ").withStyle(this.collapsed ? ChatFormatting.RED : ChatFormatting.GREEN).append(this.title));
        }

        @Override
        public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            theme.drawWidget(graphics, x, y, w, h, this.getWidgetType());
            theme.drawString(graphics, this.getTitle(), x + 3, y + 3);
            if (this.isMouseOver()) {
                Color4I.WHITE.withAlpha(33).draw(graphics, x, y, w, h);
            }
        }

        @Override
        public void addMouseOverText(TooltipList list) {
            if (this.info != null) {
                list.add(this.info);
            }
        }

        @Override
        public void onClicked(MouseButton button) {
            this.setCollapsed(!this.collapsed);
            this.getGui().refreshWidgets();
        }
    }

    public class ConfigPanel extends Panel {

        public ConfigPanel() {
            super(EditConfigScreen.this);
        }

        @Override
        public void addWidgets() {
            for (Widget w : EditConfigScreen.this.allConfigButtons) {
                if (!(w instanceof EditConfigScreen.ConfigEntryButton<?> cgb) || !cgb.groupButton.collapsed) {
                    this.add(w);
                }
            }
        }

        @Override
        public void alignWidgets() {
            EditConfigScreen.this.allConfigButtons.forEach(btn -> {
                btn.setX(1);
                btn.setWidth(this.width - 2);
            });
            this.align(WidgetLayout.VERTICAL);
        }
    }

    protected class CustomTopPanel extends AbstractThreePanelScreen<EditConfigScreen.ConfigPanel>.TopPanel {

        private final TextField titleLabel = new TextField(this);

        @Override
        public void addWidgets() {
            this.titleLabel.setText(this.getGui().getTitle());
            this.titleLabel.addFlags(32);
            this.add(this.titleLabel);
            if (EditConfigScreen.this.groupSize > 1) {
                this.add(EditConfigScreen.this.buttonExpandAll);
                this.add(EditConfigScreen.this.buttonCollapseAll);
            }
        }

        @Override
        public void alignWidgets() {
            this.titleLabel.setPosAndSize(4, 0, this.titleLabel.width, this.height);
            if (EditConfigScreen.this.groupSize > 1) {
                EditConfigScreen.this.buttonExpandAll.setPos(this.width - 18, 2);
                EditConfigScreen.this.buttonCollapseAll.setPos(this.width - 38, 2);
            }
        }
    }
}