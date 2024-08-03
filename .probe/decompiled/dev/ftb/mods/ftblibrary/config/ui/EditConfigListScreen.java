package dev.ftb.mods.ftblibrary.config.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.ftb.mods.ftblibrary.config.ConfigCallback;
import dev.ftb.mods.ftblibrary.config.ConfigValue;
import dev.ftb.mods.ftblibrary.config.ListConfig;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.icon.MutableColor4I;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleButton;
import dev.ftb.mods.ftblibrary.ui.TextField;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.ui.misc.AbstractThreePanelScreen;
import dev.ftb.mods.ftblibrary.util.TextComponentUtils;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class EditConfigListScreen<E, CV extends ConfigValue<E>> extends AbstractThreePanelScreen<EditConfigListScreen<E, CV>.ConfigPanel> {

    private final ListConfig<E, CV> listConfig;

    private final ConfigCallback callback;

    private final List<E> localValues;

    private final int widestElement;

    private final Component title;

    private final EditConfigListScreen<E, CV>.ButtonAddValue addButton;

    boolean changed = false;

    public EditConfigListScreen(ListConfig<E, CV> listConfig, ConfigCallback callback) {
        this.listConfig = listConfig;
        this.callback = callback;
        this.localValues = new ArrayList((Collection) listConfig.getValue());
        this.title = Component.literal(listConfig.getName()).withStyle(ChatFormatting.BOLD);
        this.addButton = new EditConfigListScreen.ButtonAddValue(this.topPanel);
        this.widestElement = Math.max(this.getTheme().getStringWidth(this.title) + 25, (Integer) listConfig.getValue().stream().map(item -> this.getTheme().getStringWidth(listConfig.getType().getStringForGUI((E) item))).max(Integer::compareTo).orElse(176));
    }

    @Override
    public boolean onInit() {
        int maxH = (int) ((float) this.getScreen().getGuiScaledHeight() * 0.8F);
        int maxW = (int) ((float) this.getScreen().getGuiScaledWidth() * 0.9F);
        this.setHeight(Mth.clamp(this.localValues.size() * 12 + this.getTopPanelHeight() + this.bottomPanel.height, 176, maxH));
        this.setWidth(Mth.clamp(this.widestElement + 20, 176, maxW));
        return true;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected void doAccept() {
        if (this.changed) {
            this.listConfig.getValue().clear();
            this.listConfig.getValue().addAll(this.localValues);
        }
        this.callback.save(this.changed);
    }

    @Override
    protected int getTopPanelHeight() {
        return 20;
    }

    @Override
    protected Panel createTopPanel() {
        return new EditConfigListScreen.CustomTopPanel();
    }

    protected EditConfigListScreen<E, CV>.ConfigPanel createMainPanel() {
        return new EditConfigListScreen.ConfigPanel();
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
        this.callback.save(false);
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
        } else if (key.is(260)) {
            this.addButton.onClicked(MouseButton.LEFT);
            return true;
        } else {
            return key.is(261) ? (Boolean) this.mainPanel.getHoveredDeletable().map(d -> {
                d.deleteItem();
                return true;
            }).orElse(super.keyPressed(key)) : false;
        }
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    public class ButtonAddValue extends SimpleButton implements EditStringConfigOverlay.PosProvider {

        public ButtonAddValue(Panel panel) {
            super(panel, Component.translatable("gui.add"), Icons.ADD, (btn, mb) -> {
            });
        }

        @Override
        public void addMouseOverText(TooltipList list) {
            list.translate("gui.add");
            list.styledString("[Ins]", ChatFormatting.GRAY);
        }

        @Override
        public void onClicked(MouseButton button) {
            this.playClickSound();
            CV listType = EditConfigListScreen.this.listConfig.getType();
            listType.setValue(listType.getDefaultValue() == null ? null : listType.copy(listType.getDefaultValue()));
            listType.onClicked(this, button, accepted -> {
                if (accepted) {
                    EditConfigListScreen.this.localValues.add(listType.getValue());
                    EditConfigListScreen.this.changed = true;
                }
                this.openGui();
            });
        }

        @Override
        public EditStringConfigOverlay.PosProvider.Offset getOverlayOffset() {
            return new EditStringConfigOverlay.PosProvider.Offset(-this.getGui().width / 2, 20);
        }
    }

    public class ButtonConfigValue extends Button implements EditConfigListScreen.Deletable, EditStringConfigOverlay.PosProvider {

        private static final Component DEL_BUTTON_TXT = Component.literal("[").withStyle(ChatFormatting.RED).append(Component.literal("X").withStyle(ChatFormatting.GOLD)).append(Component.literal("]").withStyle(ChatFormatting.RED));

        public final int index;

        public ButtonConfigValue(int index) {
            super(EditConfigListScreen.this.mainPanel);
            this.index = index;
            this.setHeight(12);
        }

        @Override
        public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            boolean mouseOver = this.getMouseY() >= 20 && this.isMouseOver();
            MutableColor4I textCol = EditConfigListScreen.this.listConfig.getType().getColor((E) EditConfigListScreen.this.localValues.get(this.index)).mutable();
            textCol.setAlpha(255);
            if (mouseOver) {
                textCol.addBrightness(60);
                Color4I.WHITE.withAlpha(33).draw(graphics, x, y, w, h);
                if (this.getMouseX() >= x + w - 19) {
                    Color4I.WHITE.withAlpha(33).draw(graphics, x + w - 19, y, 19, h);
                }
            }
            theme.drawString(graphics, this.getGui().getTheme().trimStringToWidth(EditConfigListScreen.this.listConfig.getType().getStringForGUI((E) EditConfigListScreen.this.localValues.get(this.index)), this.width), x + 4, y + 2, textCol, 0);
            if (mouseOver) {
                theme.drawString(graphics, DEL_BUTTON_TXT, x + w - 16, y + 2, Color4I.WHITE, 0);
            }
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }

        @Override
        public void onClicked(MouseButton button) {
            this.playClickSound();
            if (this.getMouseX() >= this.getX() + this.width - 19) {
                if (EditConfigListScreen.this.listConfig.getCanEdit()) {
                    this.deleteItem();
                }
            } else {
                EditConfigListScreen.this.listConfig.getType().setValue((E) EditConfigListScreen.this.localValues.get(this.index));
                EditConfigListScreen.this.listConfig.getType().onClicked(this, button, accepted -> {
                    if (accepted) {
                        EditConfigListScreen.this.localValues.set(this.index, EditConfigListScreen.this.listConfig.getType().getValue());
                        EditConfigListScreen.this.changed = true;
                    }
                    this.openGui();
                });
            }
        }

        @Override
        public void addMouseOverText(TooltipList l) {
            if (this.getMouseX() >= this.getX() + this.width - 19) {
                l.translate("selectServer.delete");
                l.add(TextComponentUtils.hotkeyTooltip("Del"));
            } else {
                EditConfigListScreen.this.listConfig.getType().setValue((E) EditConfigListScreen.this.localValues.get(this.index));
                EditConfigListScreen.this.listConfig.getType().addInfo(l);
            }
        }

        @Override
        public void deleteItem() {
            EditConfigListScreen.this.localValues.remove(this.index);
            EditConfigListScreen.this.changed = true;
            this.parent.refreshWidgets();
        }

        @Override
        public EditStringConfigOverlay.PosProvider.Offset getOverlayOffset() {
            return new EditStringConfigOverlay.PosProvider.Offset(0, 0);
        }
    }

    public class ConfigPanel extends Panel {

        public ConfigPanel() {
            super(EditConfigListScreen.this);
        }

        @Override
        public void addWidgets() {
            for (int i = 0; i < EditConfigListScreen.this.localValues.size(); i++) {
                this.add(EditConfigListScreen.this.new ButtonConfigValue(i));
            }
        }

        @Override
        public void alignWidgets() {
            for (Widget w : this.widgets) {
                w.setX(2);
                w.setWidth(this.width - 4);
            }
            this.align(WidgetLayout.VERTICAL);
        }

        public Optional<EditConfigListScreen.Deletable> getHoveredDeletable() {
            return this.getWidgets().stream().filter(w -> w.isMouseOver() && w instanceof EditConfigListScreen.Deletable).map(w -> (EditConfigListScreen.Deletable) w).findFirst();
        }
    }

    private class CustomTopPanel extends AbstractThreePanelScreen<EditConfigListScreen<E, CV>.ConfigPanel>.TopPanel {

        private final TextField titleLabel = new TextField(this).setText(this.getTitle());

        @Override
        public void addWidgets() {
            this.titleLabel.addFlags(32);
            this.add(this.titleLabel);
            if (EditConfigListScreen.this.listConfig.getCanEdit()) {
                this.add(EditConfigListScreen.this.addButton);
            }
        }

        @Override
        public void alignWidgets() {
            this.titleLabel.setPosAndSize(4, 0, this.titleLabel.width, this.height);
            EditConfigListScreen.this.addButton.setPosAndSize(this.width - 18, 1, 16, 16);
        }

        @Override
        public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            super.draw(graphics, theme, x, y, w, h);
            theme.drawString(graphics, this.getGui().getTitle(), x + 6, y + 6, 2);
        }
    }

    @FunctionalInterface
    public interface Deletable {

        void deleteItem();
    }
}