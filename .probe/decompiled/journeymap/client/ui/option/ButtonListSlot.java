package journeymap.client.ui.option;

import com.google.common.base.Strings;
import java.awt.Color;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.ui.component.Button;
import journeymap.client.ui.component.ButtonList;
import journeymap.client.ui.component.SliderButton;
import journeymap.client.ui.component.Slot;
import journeymap.client.ui.component.TextFieldButton;
import journeymap.common.Journeymap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class ButtonListSlot extends Slot implements Comparable<ButtonListSlot> {

    static int hgap = 8;

    Minecraft mc = Minecraft.getInstance();

    Font fontRenderer = Minecraft.getInstance().font;

    ButtonList buttons = new ButtonList();

    HashMap<Button, SlotMetadata> buttonOptionMetadata = new HashMap();

    CategorySlot parent;

    SlotMetadata lastPressed = null;

    SlotMetadata currentToolTip = null;

    Integer colorToolbarBgStart = new Color(0, 0, 100).getRGB();

    Integer colorToolbarBgEnd = new Color(0, 0, 100).getRGB();

    public ButtonListSlot(CategorySlot parent) {
        this.parent = parent;
    }

    public ButtonListSlot add(SlotMetadata slotMetadata) {
        this.buttons.add(slotMetadata.getButton());
        this.buttonOptionMetadata.put(slotMetadata.getButton(), slotMetadata);
        return this;
    }

    public ButtonListSlot addAll(Collection<SlotMetadata> slotMetadataCollection) {
        for (SlotMetadata slotMetadata : slotMetadataCollection) {
            this.add(slotMetadata);
        }
        return this;
    }

    public ButtonListSlot merge(ButtonListSlot other) {
        for (SlotMetadata otherSlot : other.buttonOptionMetadata.values()) {
            this.add(otherSlot);
        }
        return this;
    }

    public void clear() {
        this.buttons.clear();
        this.buttonOptionMetadata.clear();
    }

    @Override
    public Collection<SlotMetadata> getMetadata() {
        return this.buttonOptionMetadata.values();
    }

    @Override
    public void render(GuiGraphics graphics, int slotIndex, int y, int x, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
        int margin = 0;
        if (this.parent.getCurrentColumnWidth() > 0) {
            int cols = this.parent.currentColumns;
            margin = (listWidth - (hgap * cols - 1 + cols * this.parent.getCurrentColumnWidth())) / 2;
            x += margin;
            listWidth -= margin * 2;
        }
        SlotMetadata tooltipMetadata = null;
        if (this.buttons.size() > 0) {
            this.buttons.setHeights(slotHeight);
            if (((SlotMetadata) this.buttonOptionMetadata.get(this.buttons.get(0))).isToolbar()) {
                this.buttons.fitWidths(this.fontRenderer);
                this.buttons.layoutHorizontal(x + listWidth - hgap, y, false, hgap);
                DrawUtil.drawGradientRect(graphics.pose(), (double) x, (double) y, (double) listWidth, (double) slotHeight, this.colorToolbarBgStart, 0.15F, this.colorToolbarBgEnd, 0.6F);
            } else {
                this.buttons.setWidths(this.parent.currentColumnWidth);
                this.buttons.layoutHorizontal(x, y, true, hgap);
            }
            for (Button button : this.buttons) {
                button.render(graphics, mouseX, mouseY, 0.0F);
                if (tooltipMetadata == null && button.mouseOver((double) mouseX, (double) mouseY)) {
                    tooltipMetadata = (SlotMetadata) this.buttonOptionMetadata.get(button);
                }
            }
        } else {
            Journeymap.getLogger().warn("no buttons in " + this.parent.category.getLabel());
        }
        this.currentToolTip = tooltipMetadata;
    }

    @Override
    public boolean mouseClicked(double x, double y, int mouseButton) {
        if (mouseButton == 0) {
            for (Button button : this.buttons) {
                if (button.mouseClicked(x, y, mouseButton)) {
                    this.lastPressed = (SlotMetadata) this.buttonOptionMetadata.get(button);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double x, double y, int mouseButton) {
        for (Button button : this.buttons) {
            button.m_6348_(x, y, mouseButton);
        }
        return true;
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        for (SlotMetadata slot : this.buttonOptionMetadata.values()) {
            Button button = slot.button;
            if (slot.equals(this.lastPressed) && button.isMouseOver() && button instanceof TextFieldButton) {
                return button.m_5534_(typedChar, keyCode);
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double mouseDX, double mouseDY) {
        for (SlotMetadata slot : this.buttonOptionMetadata.values()) {
            Button button = slot.button;
            if (slot.equals(this.lastPressed) && button.isMouseOver() && (button instanceof SliderButton || button instanceof TextFieldButton)) {
                return button.m_7979_(mouseX, mouseY, mouseButton, mouseDX, mouseDY);
            }
        }
        return false;
    }

    @Override
    public boolean keyPressed(int key, int value, int modifier) {
        for (SlotMetadata slot : this.buttonOptionMetadata.values()) {
            Button button = slot.button;
            if (slot.equals(this.lastPressed) && button.isMouseOver() && (button instanceof TextFieldButton || button instanceof SliderButton)) {
                return button.m_7933_(key, value, modifier);
            }
        }
        return false;
    }

    @Override
    public void setEnabled(boolean enabled) {
        for (SlotMetadata slot : this.buttonOptionMetadata.values()) {
            if (!slot.isMasterPropertyForCategory()) {
                slot.button.setEnabled(enabled);
            }
        }
    }

    @Override
    public List<Slot> getChildSlots(int listWidth, int columnWidth) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public SlotMetadata getLastPressed() {
        return this.lastPressed;
    }

    @Override
    public SlotMetadata getCurrentTooltip() {
        return this.currentToolTip;
    }

    @Override
    public int getColumnWidth() {
        this.buttons.equalizeWidths(this.fontRenderer);
        return ((Button) this.buttons.get(0)).m_5711_();
    }

    @Override
    public boolean contains(SlotMetadata slotMetadata) {
        return this.buttonOptionMetadata.values().contains(slotMetadata);
    }

    protected String getFirstButtonString() {
        return this.buttons.size() > 0 ? ((Button) this.buttons.get(0)).m_6035_().getString() : null;
    }

    public int compareTo(ButtonListSlot o) {
        String buttonString = this.getFirstButtonString();
        String otherButtonString = o.getFirstButtonString();
        if (!Strings.isNullOrEmpty(buttonString)) {
            return buttonString.compareTo(otherButtonString);
        } else {
            return !Strings.isNullOrEmpty(otherButtonString) ? 1 : 0;
        }
    }
}