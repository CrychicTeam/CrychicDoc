package com.github.einjerjar.mc.keymap.client.gui.widgets;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.keys.layout.KeyData;
import com.github.einjerjar.mc.keymap.keys.layout.KeyRow;
import com.github.einjerjar.mc.widgets.EWidget;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

public class VirtualKeyboardWidget extends EWidget {

    protected final List<KeyWidget> childKeys = new ArrayList();

    protected final List<KeyRow> keys;

    protected int gap = 2;

    protected EWidget.SimpleWidgetAction<VirtualKeyboardWidget> onKeyClicked;

    protected VirtualKeyboardWidget.SpecialVKKeyClicked onSpecialKeyClicked;

    protected KeyWidget lastActionFrom;

    public VirtualKeyboardWidget(List<KeyRow> keys, int x, int y, int w, int h) {
        super(x, y, w, h);
        this.keys = keys;
        this.init();
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return false;
    }

    public VirtualKeyboardWidget destroy() {
        for (KeyWidget childKey : this.childKeys) {
            childKey.destroy();
        }
        return this;
    }

    protected void _onKeyClicked(KeyWidget source) {
        this.lastActionFrom = source;
        if (this.onKeyClicked != null) {
            this.onKeyClicked.run(this);
        }
        this.lastActionFrom = null;
    }

    protected void _onSpecialKeyClicked(KeyWidget source, int button) {
        if (source.isNormal()) {
            Keymap.logger().warn("False Special");
            this._onKeyClicked(source);
        } else {
            this.lastActionFrom = source;
            if (this.onSpecialKeyClicked != null) {
                this.onSpecialKeyClicked.run(this, source, button);
            }
            this.lastActionFrom = null;
        }
    }

    @Override
    protected void init() {
        int w = 16;
        int h = 16;
        int currentX = this.left();
        int currentY = this.top();
        int maxX = 0;
        for (KeyRow kk : this.keys) {
            for (KeyData k : kk.row()) {
                int ww = w + k.width();
                int hh = h + k.height();
                KeyWidget kw = new KeyWidget(k, currentX, currentY, ww, hh);
                kw.onClick(this::_onKeyClicked);
                kw.onSpecialClick(this::_onSpecialKeyClicked);
                this.childKeys.add(kw);
                currentX += this.gap + ww;
            }
            maxX = Math.max(currentX - this.gap, maxX);
            currentX = this.left();
            currentY += this.gap + h;
        }
        currentY -= this.gap;
        this.rect.w(maxX - this.left());
        this.rect.h(currentY - this.top());
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        for (KeyWidget kw : this.childKeys) {
            kw.m_88315_(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    public List<KeyWidget> childKeys() {
        return this.childKeys;
    }

    public int gap() {
        return this.gap;
    }

    public VirtualKeyboardWidget gap(int gap) {
        this.gap = gap;
        return this;
    }

    public VirtualKeyboardWidget onKeyClicked(EWidget.SimpleWidgetAction<VirtualKeyboardWidget> onKeyClicked) {
        this.onKeyClicked = onKeyClicked;
        return this;
    }

    public VirtualKeyboardWidget onSpecialKeyClicked(VirtualKeyboardWidget.SpecialVKKeyClicked onSpecialKeyClicked) {
        this.onSpecialKeyClicked = onSpecialKeyClicked;
        return this;
    }

    public KeyWidget lastActionFrom() {
        return this.lastActionFrom;
    }

    public interface SpecialVKKeyClicked {

        void run(VirtualKeyboardWidget var1, KeyWidget var2, int var3);
    }
}