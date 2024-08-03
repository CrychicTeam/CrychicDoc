package com.github.einjerjar.mc.keymap.client.gui.widgets;

import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeymapRegistry;
import com.github.einjerjar.mc.keymap.keys.layout.KeyData;
import com.github.einjerjar.mc.keymap.keys.sources.KeymappingNotifier;
import com.github.einjerjar.mc.keymap.keys.wrappers.keys.KeyHolder;
import com.github.einjerjar.mc.widgets.EWidget;
import com.github.einjerjar.mc.widgets.utils.ColorGroups;
import com.github.einjerjar.mc.widgets.utils.ColorSet;
import com.github.einjerjar.mc.widgets.utils.Styles;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class KeyWidget extends EWidget implements KeymappingNotifier.KeybindingRegistrySubscriber {

    protected KeyData key;

    protected InputConstants.Key mcKey;

    protected EWidget.SimpleWidgetAction<KeyWidget> onClick;

    protected KeyWidget.SpecialKeyWidgetAction onSpecialClick;

    protected boolean selected = false;

    protected Component text;

    protected boolean hasComplex = false;

    public KeyWidget(KeyData key, int x, int y, int w, int h) {
        super(x, y, w, h);
        this._init(key);
    }

    protected void _init(KeyData key) {
        this.key = key;
        this.text = Component.literal(key.name());
        this.mcKey = this.getMCKey(key);
        this.tooltips = new ArrayList();
        this.tooltips.add(this.mcKey.getDisplayName());
        if (key.code() == -2) {
            this.allowRightClick = true;
            for (int i = 0; i < 10; i++) {
                KeymappingNotifier.subscribe(i, this);
            }
        } else {
            KeymappingNotifier.subscribe(key.code(), this);
        }
        this.updateTooltips();
    }

    public void destroy() {
        if (this.key.code() == -2) {
            for (int i = 0; i < 10; i++) {
                KeymappingNotifier.unsubscribe(i, this);
            }
        } else {
            KeymappingNotifier.unsubscribe(this.key.code(), this);
        }
    }

    public boolean isNormal() {
        return this.key.code() != -2;
    }

    public boolean updateTooltipForOtherMouseKeys() {
        this.tooltips.add(Component.literal(this.text.getString()).withStyle(Styles.header()));
        List<Component> boundKeys = new ArrayList();
        for (int i = 0; i < 10; i++) {
            if (KeymappingNotifier.keys().containsKey(i)) {
                for (KeyHolder k : KeymappingNotifier.keys().get(i)) {
                    boundKeys.add(Component.literal(String.format("[%s] %s", i, k.getTranslatedName().getString())));
                }
            }
        }
        int size = boundKeys.size();
        this.color(ColorGroups.WHITE);
        if (size > 0) {
            this.color(ColorGroups.GREEN);
            this.tooltips.add(Component.literal("--------------------").withStyle(Styles.muted()));
            this.tooltips.addAll(boundKeys);
        }
        if (this.selected) {
            this.color(ColorGroups.YELLOW);
        }
        return true;
    }

    public boolean updateSpecialTooltip() {
        return this.key.code() == -2 ? this.updateTooltipForOtherMouseKeys() : false;
    }

    protected void updateDebugTooltips() {
        if (KeymapConfig.instance().debug()) {
            this.tooltips.add(Component.literal("--------------------").withStyle(Styles.muted()));
            this.tooltips.add(Component.literal(String.format("Code: %d", this.key.code())).withStyle(Styles.yellow()));
            this.tooltips.add(Component.literal(String.format("Mouse?: %b", this.key.mouse())).withStyle(Styles.yellow()));
            this.tooltips.add(Component.literal(String.format("Name: %s", this.key.name())).withStyle(Styles.yellow()));
        }
    }

    public void updateNormalTooltip() {
        this.tooltips.add(Component.literal(String.format("(%s) ", this.text.getString())).withStyle(Styles.yellow()).append(Component.literal(this.mcKey.getDisplayName().getString()).withStyle(Styles.headerBold())));
        this.hasComplex = KeymapRegistry.containsKey(this.key.code());
        if (KeymappingNotifier.keys().containsKey(this.key.code())) {
            List<KeyHolder> holders = List.copyOf(KeymappingNotifier.keys().get(this.key.code()));
            int size = holders.size();
            switch(size) {
                case 0:
                    this.color(ColorGroups.WHITE);
                    break;
                case 1:
                    this.color(ColorGroups.GREEN);
                    break;
                default:
                    this.color(ColorGroups.RED);
            }
            if (size > 0) {
                this.tooltips.add(Component.literal("--------------------").withStyle(Styles.muted()));
                for (KeyHolder k : holders) {
                    this.tooltips.add(k.getTranslatedName());
                }
            }
        } else {
            this.color(ColorGroups.WHITE);
        }
        if (this.hasComplex) {
            List<KeyMapping> m = KeymapRegistry.getMappings(this.key.code());
            if (m.isEmpty() && this.tooltips.size() == 1) {
                this.tooltips.add(Component.literal("--------------------").withStyle(Styles.muted()));
            }
            for (KeyMapping km : m) {
                this.tooltips.add(Component.translatable(km.getName()));
            }
        }
        if (this.selected) {
            this.color(ColorGroups.YELLOW);
        }
    }

    @Override
    public void updateTooltips() {
        this.tooltips.clear();
        if (!this.updateSpecialTooltip()) {
            this.updateNormalTooltip();
        }
        this.updateDebugTooltips();
    }

    protected InputConstants.Key getMCKey(KeyData key) {
        return key.mouse() ? InputConstants.Type.MOUSE.getOrCreate(key.code()) : InputConstants.Type.KEYSYM.getOrCreate(key.code());
    }

    @Override
    public boolean onMouseReleased(boolean inside, double mouseX, double mouseY, int button) {
        if (this.isNormal()) {
            if (this.onClick != null) {
                this.onClick.run(this);
                return true;
            }
        } else if (this.onSpecialClick != null) {
            this.onSpecialClick.run(this, button);
            return true;
        }
        return false;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        ColorSet colors = this.colorVariant();
        this.drawBg(guiGraphics, colors.bg());
        if (this.hasComplex) {
            guiGraphics.fill(this.right() - 6, this.bottom() - 6, this.right(), this.bottom(), -256);
        }
        this.drawOutline(guiGraphics, colors.border());
        guiGraphics.drawCenteredString(this.font, this.text, this.midX(), this.midY() - 9 / 2 + 1, colors.text());
    }

    @Override
    public void keybindingRegistryUpdated(boolean selected) {
        this.selected = selected;
        this.updateTooltips();
    }

    public KeyData key() {
        return this.key;
    }

    public InputConstants.Key mcKey() {
        return this.mcKey;
    }

    public KeyWidget onClick(EWidget.SimpleWidgetAction<KeyWidget> onClick) {
        this.onClick = onClick;
        return this;
    }

    public KeyWidget onSpecialClick(KeyWidget.SpecialKeyWidgetAction onSpecialClick) {
        this.onSpecialClick = onSpecialClick;
        return this;
    }

    public boolean selected() {
        return this.selected;
    }

    public KeyWidget selected(boolean selected) {
        this.selected = selected;
        return this;
    }

    public interface SpecialKeyWidgetAction {

        void run(KeyWidget var1, int var2);
    }
}