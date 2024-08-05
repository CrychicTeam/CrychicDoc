package journeymap.client.ui.option;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import journeymap.client.Constants;
import journeymap.client.properties.ClientCategory;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.ui.component.Button;
import journeymap.client.ui.component.Slot;
import journeymap.common.properties.catagory.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class CategorySlot extends Slot implements Comparable<CategorySlot> {

    Minecraft mc = Minecraft.getInstance();

    SlotMetadata metadata;

    Category category;

    int currentSlotIndex;

    Button button;

    int currentListWidth;

    int currentColumns;

    int currentColumnWidth;

    SlotMetadata masterSlot;

    SlotMetadata currentTooltip;

    LinkedList<SlotMetadata> childMetadataList = new LinkedList();

    List<Slot> childSlots = new ArrayList();

    String glyphClosed = "▶";

    String glyphOpen = "▼";

    private boolean selected;

    public CategorySlot(Category category) {
        this.category = category;
        boolean advanced = category == ClientCategory.Advanced;
        this.button = new Button(category.getLabel());
        this.metadata = new SlotMetadata(this.button, category.getLabel(), category.getTooltip(), advanced);
        this.updateButtonLabel();
    }

    public CategorySlot add(Slot slot) {
        this.childSlots.add(slot);
        this.childMetadataList.addAll(slot.getMetadata());
        for (SlotMetadata slotMetadata : slot.getMetadata()) {
            if (slotMetadata.isMasterPropertyForCategory()) {
                this.masterSlot = slotMetadata;
            }
        }
        return this;
    }

    public void clear() {
        this.childSlots.clear();
    }

    public int size() {
        return this.childSlots.size();
    }

    public void sort() {
        Collections.sort(this.childMetadataList);
    }

    @Override
    public int getColumnWidth() {
        int columnWidth = 100;
        for (Slot slot : this.childSlots) {
            columnWidth = Math.max(columnWidth, slot.getColumnWidth());
        }
        return columnWidth;
    }

    @Override
    public List<Slot> getChildSlots(int listWidth, int columnWidth) {
        if (!this.selected) {
            return Collections.EMPTY_LIST;
        } else {
            int columns = listWidth / (columnWidth + ButtonListSlot.hgap);
            if ((columnWidth != this.currentColumnWidth || columns != this.currentColumns) && columns != 0) {
                this.currentListWidth = listWidth;
                this.currentColumnWidth = columnWidth;
                this.currentColumns = columns;
                this.childSlots.clear();
                this.sort();
                ArrayList<SlotMetadata> remaining = new ArrayList(this.childMetadataList);
                while (!remaining.isEmpty()) {
                    ButtonListSlot row = new ButtonListSlot(this);
                    SlotMetadata.ValueType lastType = null;
                    for (int i = 0; i < columns && !remaining.isEmpty(); i++) {
                        SlotMetadata.ValueType thisType = ((SlotMetadata) remaining.get(0)).valueType;
                        if (lastType == null && thisType == SlotMetadata.ValueType.Toolbar) {
                            row.addAll(remaining);
                            remaining.clear();
                            break;
                        }
                        if (lastType != null && lastType != thisType && (thisType == SlotMetadata.ValueType.Toolbar || lastType == SlotMetadata.ValueType.Boolean && remaining.size() > columns - i)) {
                            break;
                        }
                        SlotMetadata column = (SlotMetadata) remaining.remove(0);
                        lastType = column.valueType;
                        row.add(column);
                    }
                    row.buttons.setWidths(columnWidth);
                    this.childSlots.add(row);
                }
                return this.childSlots;
            } else {
                return this.childSlots;
            }
        }
    }

    @Override
    public Collection<SlotMetadata> getMetadata() {
        return Arrays.asList(this.metadata);
    }

    public List<SlotMetadata> getAllChildMetadata() {
        return this.childMetadataList;
    }

    public int getCurrentColumns() {
        return this.currentColumns;
    }

    public int getCurrentColumnWidth() {
        return this.currentColumnWidth;
    }

    @Override
    public void render(GuiGraphics graphics, int slotIndex, int y, int x, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
        this.currentSlotIndex = slotIndex;
        this.button.setWidth(listWidth);
        this.button.setScrollablePosition(x, y);
        this.button.setHeight(slotHeight);
        this.button.render(graphics, mouseX, mouseY, 0.0F);
        DrawUtil.drawRectangle(graphics.pose(), (double) (this.button.m_252754_() + 4), (double) (this.button.getMiddleY() - 5), 11.0, 10.0, 0, 0.2F);
        DrawUtil.drawLabel(graphics, this.selected ? this.glyphOpen : this.glyphClosed, (double) (this.button.m_252754_() + 12), (double) this.button.getMiddleY(), DrawUtil.HAlign.Left, DrawUtil.VAlign.Middle, 0, 0.0F, this.button.getLabelColor(), 1.0F, 1.0, true);
        if (this.masterSlot != null && this.selected) {
            boolean enabled = this.masterSlot.button.isActive();
            for (Slot slot : this.childSlots) {
                slot.setEnabled(enabled);
            }
        }
        this.currentTooltip = null;
        if (this.button.mouseOver((double) mouseX, (double) mouseY)) {
            this.currentTooltip = this.metadata;
        }
    }

    private void updateButtonLabel() {
        this.button.m_93666_(Constants.getStringTextComponent(this.category.getLabel()));
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean mouseClicked(double x, double y, int mouseButton) {
        if (mouseButton == 0) {
            boolean pressed = this.button.mouseClicked(x, y, mouseButton);
            if (pressed) {
                this.setSelected(!this.selected);
                this.updateButtonLabel();
            }
            return pressed;
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double mouseDX, double mouseDY) {
        return false;
    }

    @Override
    public boolean mouseReleased(double x, double y, int mouseButton) {
        return this.button.m_6348_(x, y, mouseButton);
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        return false;
    }

    @Override
    public boolean keyPressed(int key, int value, int modifier) {
        return false;
    }

    public int compareTo(CategorySlot other) {
        return this.category.compareTo(other.category);
    }

    @Override
    public void setEnabled(boolean enabled) {
    }

    @Override
    public SlotMetadata getLastPressed() {
        return this.metadata;
    }

    @Override
    public SlotMetadata getCurrentTooltip() {
        return this.currentTooltip;
    }

    @Override
    public boolean contains(SlotMetadata slotMetadata) {
        return this.childMetadataList.contains(slotMetadata);
    }

    public Category getCategory() {
        return this.category;
    }
}