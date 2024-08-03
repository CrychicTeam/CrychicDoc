package yesman.epicfight.client.gui.component;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.compress.utils.Lists;
import yesman.epicfight.api.utils.ParseUtil;

@OnlyIn(Dist.CLIENT)
public class Grid extends ObjectSelectionList<Grid.Row> implements ResizableComponent {

    private final Screen owner;

    private final Map<String, Grid.Column<?>> columns = Maps.newHashMap();

    private final List<ResizableButton> rowEditButtons = Lists.newArrayList();

    private final boolean transparentBackground;

    private AbstractWidget editingWidget;

    private Grid.Column<?> editingColumn;

    private int rowpostition;

    private final int xParam1;

    private final int yParam1;

    private final int xParam2;

    private final int yParam2;

    private final ResizableComponent.HorizontalSizing horizontalSizingOption;

    private final ResizableComponent.VerticalSizing verticalSizingOption;

    public Grid(Grid.GridBuilder gb) {
        super(gb.minecraft, gb.x2, gb.y2, gb.y1, gb.y1 + gb.y2, gb.rowHeight);
        this.owner = gb.owner;
        this.transparentBackground = gb.transparentBackground;
        this.horizontalSizingOption = gb.horizontalSizing;
        this.verticalSizingOption = gb.verticalSizing;
        this.xParam1 = gb.x1;
        this.yParam1 = gb.y1;
        this.xParam2 = gb.x2;
        this.yParam2 = gb.y2;
        this.resize(gb.minecraft.screen.getRectangle());
        gb.columns.entrySet().stream().map(entry -> {
            ((Grid.Column) entry.getValue()).size = (int) ((float) ((Grid.Column) entry.getValue()).size * ((float) this.f_93388_ / (float) gb.sizeTotal));
            return Pair.of((String) entry.getKey(), (Grid.Column) entry.getValue());
        }).forEach(pair -> this.columns.put((String) pair.getFirst(), (Grid.Column) pair.getSecond()));
        this.setLeftPos(gb.x1);
        this.m_93496_(false);
        if (gb.rowEditable) {
            int x1 = this.f_93392_ - 12;
            int x2 = 12;
            int xCorrect1 = 12;
            int xCorrect2 = 0;
            if (gb.buttonHorizontalSizing == ResizableComponent.HorizontalSizing.WIDTH_RIGHT) {
                x1 = 12;
                x2 = this.owner.width - this.f_93392_;
                xCorrect1 = 0;
                xCorrect2 = 12;
            }
            this.rowEditButtons.add(ResizableButton.builder(Component.literal("+"), button -> gb.onAddPress.accept(this, button)).pos(x1 - xCorrect1, this.f_93390_ - 12).size(x2 - xCorrect2, 12).horizontalSizing(gb.buttonHorizontalSizing).build());
            this.rowEditButtons.add(ResizableButton.builder(Component.literal("x"), button -> gb.onRemovePress.accept(this, button)).pos(x1, this.f_93390_ - 12).size(x2, 12).horizontalSizing(gb.buttonHorizontalSizing).build());
        }
    }

    public int addRow() {
        return this.addRow(this.m_6702_().size());
    }

    public int addRow(int rowposition) {
        this.editingColumn = null;
        this.editingWidget = null;
        Grid.Row row = new Grid.Row();
        this.m_6702_().add(rowposition, row);
        for (java.util.Map.Entry<String, Grid.Column<?>> entry : this.columns.entrySet()) {
            row.setValue((String) entry.getKey(), ((Grid.Column) entry.getValue()).defaultVal);
        }
        return rowposition;
    }

    public int addRowWithDefaultValues(Object... defaultValues) {
        return this.addRow(this.m_6702_().size(), defaultValues);
    }

    public int addRow(int rowposition, Object... defaultValues) {
        this.editingColumn = null;
        this.editingWidget = null;
        Grid.Row row = new Grid.Row();
        for (java.util.Map.Entry<String, Grid.Column<?>> entry : this.columns.entrySet()) {
            row.setValue((String) entry.getKey(), ((Grid.Column) entry.getValue()).defaultVal);
        }
        for (int i = 0; i < defaultValues.length; i += 2) {
            row.setValue((String) defaultValues[i], defaultValues[i + 1]);
        }
        this.m_6702_().add(rowposition, row);
        return rowposition;
    }

    public int removeRow() {
        return this.removeRow(this.rowpostition);
    }

    public int removeRow(int row) {
        if (row < 0) {
            return -1;
        } else if (this.m_6702_().size() == 0) {
            return -1;
        } else {
            if (this.rowpostition == row) {
                this.editingColumn = null;
                this.editingWidget = null;
            }
            this.m_6702_().remove(row);
            this.rowpostition = Math.min(row, this.m_6702_().size() - 1);
            return this.rowpostition;
        }
    }

    public void setSelected(int rowposition) {
        this.setSelected((Grid.Row) this.m_6702_().get(rowposition));
    }

    public void setSelected(@Nullable Grid.Row row) {
        super.m_6987_(row);
        this.rowpostition = this.m_6702_().indexOf(row);
    }

    public <T> T getValue(int rowposition, String columnName) {
        return ((Grid.Row) this.m_6702_().get(rowposition)).getValue(columnName);
    }

    public <T> void setValue(int rowposition, String columnName, T value) {
        Grid.Row row = (Grid.Row) this.m_6702_().get(rowposition);
        row.setValue(columnName, value);
    }

    public void setGridFocus(int rowposition, String columnName) {
        this.setSelected(rowposition);
        int startX = 0;
        this.editingColumn = null;
        for (java.util.Map.Entry<String, Grid.Column<?>> entry : this.columns.entrySet()) {
            if (entry.getKey() == columnName) {
                this.editingColumn = (Grid.Column<?>) entry.getValue();
                break;
            }
            startX += ((Grid.Column) entry.getValue()).size;
        }
        if (this.editingColumn == null) {
            this.editingWidget = null;
        } else if (this.editingColumn.editable) {
            this.editingWidget = ((Grid.Column<Object>) this.editingColumn).createEditWidget(this.owner, this.owner.getMinecraft().font, this.f_93393_ + startX + 2, this.getRowTop(rowposition) - 2, this.f_93387_ - 3, (Grid.Row) this.m_93511_(), columnName, ((Grid.Row) this.m_93511_()).getValue(columnName));
            this.editingWidget.setFocused(true);
        }
    }

    public List<ResizableButton> getRowEditButtons() {
        return this.rowEditButtons;
    }

    private void relocateButtons() {
        int x = this.f_93392_ - 24;
        int y = this.f_93390_ - 14;
        for (AbstractWidget widget : this.rowEditButtons) {
            widget.setX(x);
            widget.setY(y);
            x += 12;
        }
    }

    @Override
    public void resize(ScreenRectangle screenRectangle) {
        if (this.getHorizontalSizingOption() != null) {
            this.getHorizontalSizingOption().resizeFunction.resize(this, screenRectangle, this.getX1(), this.getX2());
        }
        if (this.getVerticalSizingOption() != null) {
            this.getVerticalSizingOption().resizeFunction.resize(this, screenRectangle, this.getY1(), this.getY2());
        }
        this.getRowEditButtons().forEach(button -> button.resize(screenRectangle));
    }

    @Override
    public void updateSize(int width, int height, int y0, int y1) {
        this.f_93388_ = width;
        this.f_93389_ = height;
        this.f_93390_ = y0;
        this.f_93391_ = y1;
        this.f_93393_ = 0;
        this.f_93392_ = width;
        this.relocateButtons();
    }

    @Override
    public void setLeftPos(int x) {
        this.f_93393_ = x;
        this.f_93392_ = x + this.f_93388_;
        this.relocateButtons();
    }

    @Override
    public int getRowLeft() {
        return this.f_93393_ + this.f_93388_ / 2 - this.getRowWidth() / 2 + 2;
    }

    @Override
    public int getRowRight() {
        return this.getRowLeft() + this.getRowWidth();
    }

    @Override
    protected int getRowTop(int int0) {
        return this.f_93390_ - (int) this.m_93517_() + int0 * this.f_93387_ + this.f_93395_;
    }

    @Override
    protected int getRowBottom(int int0) {
        return this.getRowTop(int0) + this.f_93387_;
    }

    @Override
    public void setFocused(boolean focused) {
        if (!focused) {
            this.editingColumn = null;
            this.editingWidget = null;
        }
    }

    @Override
    public boolean isFocused() {
        return this.owner.m_7222_() == this;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.rowEditButtons.forEach(button -> button.m_88315_(guiGraphics, mouseX, mouseY, partialTicks));
        int color = this.isFocused() ? -1 : -6250336;
        guiGraphics.fill(this.f_93393_, this.f_93390_, this.f_93392_, this.f_93391_, color);
        this.renderList(guiGraphics, mouseX, mouseY, partialTicks);
        if (this.editingWidget != null) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0F, 0.0F, 1.0F);
            this.editingWidget.setY(this.getRowTop(this.rowpostition) + 2);
            guiGraphics.pose().popPose();
        }
    }

    @Override
    protected void renderList(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        int rowLeft = this.getRowLeft() - 1;
        int rowWidth = this.getRowWidth();
        int itemHeight = this.f_93387_;
        int itemCount = this.m_5773_();
        int rowBottom = this.f_93390_;
        for (int rowIndex = 0; rowIndex < itemCount; rowIndex++) {
            int rowTop = this.getRowTop(rowIndex);
            rowBottom = this.getRowTop(rowIndex) + this.f_93387_;
            if (rowBottom >= this.f_93390_ && rowTop <= this.f_93391_) {
                this.renderItem(guiGraphics, mouseX, mouseY, partialTicks, rowIndex, rowLeft, rowTop, rowWidth, itemHeight);
            }
        }
        if (rowBottom < this.f_93391_) {
            if (this.transparentBackground) {
                guiGraphics.setColor(0.12F, 0.12F, 0.12F, 1.0F);
                guiGraphics.blit(Screen.BACKGROUND_LOCATION, rowLeft, rowBottom + 1, (float) (rowLeft + rowWidth - 2), (float) (this.f_93391_ - 1), rowWidth - 2, this.f_93391_ - rowBottom - 2, 32, 32);
                guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            } else {
                guiGraphics.fill(rowLeft, rowBottom + 1, rowLeft + rowWidth - 2, this.f_93391_ - 1, -16777216);
            }
        }
    }

    @Override
    protected void renderItem(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks, int rowPosition, int rowLeft, int rowTop, int rowRight, int itemHeight) {
        Grid.Row row = (Grid.Row) this.m_93500_(rowPosition);
        if (this.m_7987_(rowPosition)) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0F, 0.0F, 1.0F);
            this.renderSelection(guiGraphics, rowTop, rowRight, itemHeight, 0, 0);
            row.render(guiGraphics, rowPosition, rowTop, rowLeft, rowRight, itemHeight, mouseX, mouseY, false, partialTicks);
            guiGraphics.pose().popPose();
        } else {
            row.render(guiGraphics, rowPosition, rowTop, rowLeft, rowRight, itemHeight, mouseX, mouseY, false, partialTicks);
        }
    }

    @Override
    protected void renderSelection(GuiGraphics guiGraphics, int rowTop, int rowRight, int itemHeight, int color, int color2) {
        guiGraphics.fill(this.f_93393_, rowTop, this.f_93392_, rowTop + itemHeight + 1, -1);
    }

    @Override
    public boolean isMouseOver(double x, double y) {
        double y0 = this.rowEditButtons.size() > 0 ? (double) (this.f_93390_ - 12) : (double) this.f_93390_;
        return y >= y0 && y <= (double) this.f_93391_ && x >= (double) this.f_93393_ && x <= (double) this.f_93392_;
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        for (Button editButton : this.rowEditButtons) {
            if (editButton.m_6375_(x, y, button)) {
                return true;
            }
        }
        if (!this.isMouseOver(x, y)) {
            return false;
        } else {
            return this.editingWidget != null && this.editingWidget.mouseClicked(x, y, button) ? true : super.m_6375_(x, y, button);
        }
    }

    @Override
    public boolean mouseScrolled(double x, double y, double button) {
        if (this.isFocused() && this.m_93518_() > 0) {
            this.m_93410_(this.m_93517_() - button * (double) this.f_93387_ / 2.0);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean keyPressed(int keycode, int int0, int int1) {
        return this.editingWidget != null ? this.editingWidget.m_7933_(keycode, int0, int1) : super.m_7933_(keycode, int0, int1);
    }

    @Override
    public boolean charTyped(char c, int i) {
        return this.editingWidget != null ? this.editingWidget.m_5534_(c, i) : super.m_5534_(c, i);
    }

    public void tick() {
        if (this.editingWidget instanceof EditBox editBox) {
            editBox.tick();
        }
    }

    @Override
    public int getRowWidth() {
        return this.f_93388_;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.f_93392_ - 6;
    }

    public static Grid.GridBuilder builder(Screen owner) {
        return new Grid.GridBuilder(owner);
    }

    @Override
    public void relocateX(int x) {
        this.f_93393_ = x;
        this.f_93392_ = x + this.f_93388_;
        this.relocateButtons();
    }

    @Override
    public void relocateY(int y) {
        this.f_93390_ = y;
        this.f_93391_ = y + this.f_93389_;
        this.relocateButtons();
    }

    @Override
    public void setX(int x) {
        this.f_93393_ = x;
        this.f_93392_ = this.f_93393_ + this.f_93388_;
        this.relocateButtons();
    }

    @Override
    public void setY(int y) {
        this.f_93390_ = y;
        this.f_93391_ = this.f_93390_ + this.f_93389_;
        this.relocateButtons();
    }

    @Override
    public void setWidth(int width) {
        this.f_93392_ = this.f_93393_ + width;
        this.f_93388_ = width;
        this.relocateButtons();
    }

    @Override
    public void setHeight(int height) {
        this.f_93391_ = this.f_93390_ + height;
        this.f_93389_ = height;
        this.relocateButtons();
    }

    @Override
    public int getX() {
        return this.f_93393_;
    }

    @Override
    public int getY() {
        return this.f_93390_;
    }

    @Override
    public int getX1() {
        return this.xParam1;
    }

    @Override
    public int getX2() {
        return this.xParam2;
    }

    @Override
    public int getY1() {
        return this.yParam1;
    }

    @Override
    public int getY2() {
        return this.yParam2;
    }

    @Override
    public ResizableComponent.HorizontalSizing getHorizontalSizingOption() {
        return this.horizontalSizingOption;
    }

    @Override
    public ResizableComponent.VerticalSizing getVerticalSizingOption() {
        return this.verticalSizingOption;
    }

    @OnlyIn(Dist.CLIENT)
    private abstract static class Column<T> {

        final Function<T, String> toVisualText;

        final T defaultVal;

        final boolean editable;

        int size;

        protected Column(Function<T, String> toVisualText, T defaultVal, boolean editable, int size) {
            this.toVisualText = toVisualText;
            this.defaultVal = defaultVal;
            this.size = size;
            this.editable = editable;
        }

        public String toVisualText(Object object) {
            return (String) this.toVisualText.apply(object);
        }

        public abstract AbstractWidget createEditWidget(Screen var1, Font var2, int var3, int var4, int var5, Grid.Row var6, String var7, T var8);
    }

    @OnlyIn(Dist.CLIENT)
    private static class ComboColumn<T> extends Grid.Column<T> {

        final List<T> enums;

        protected ComboColumn(Function<T, String> toVisualText, T defaultVal, List<T> enums, boolean editable, int size) {
            super(toVisualText, defaultVal, editable, size);
            this.enums = enums;
        }

        @Override
        public AbstractWidget createEditWidget(Screen owner, Font font, int x, int y, int height, Grid.Row row, String colName, T value) {
            return new ComboBox(owner, font, x, y, this.size - 4, height, null, null, 8, Component.literal("grid.comboEdit"), this.enums, e -> ParseUtil.makeFirstLetterToUpper(e.toString()));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static class EditBoxColumn extends Grid.Column<String> {

        protected EditBoxColumn(String defaultVal, boolean editable, int size) {
            super(string -> string, defaultVal, editable, size);
        }

        public AbstractWidget createEditWidget(Screen owner, Font font, int x, int y, int height, Grid.Row row, String colName, String value) {
            EditBox editbox = new EditBox(font, x, y, this.size - 4, height, Component.literal("grid.editbox"));
            editbox.setValue(value);
            editbox.setResponder(string -> row.setValue(colName, string));
            return editbox;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class GridBuilder {

        private final Minecraft minecraft;

        private final Screen owner;

        private final Map<String, Grid.Column<?>> columns = Maps.newLinkedHashMap();

        private int x1;

        private int y1;

        private int x2;

        private int y2;

        private int rowHeight;

        private int sizeTotal;

        private boolean rowEditable;

        private boolean transparentBackground;

        private BiConsumer<Grid, Button> onAddPress;

        private BiConsumer<Grid, Button> onRemovePress;

        private ResizableComponent.HorizontalSizing horizontalSizing = ResizableComponent.HorizontalSizing.LEFT_WIDTH;

        private ResizableComponent.VerticalSizing verticalSizing = null;

        private ResizableComponent.HorizontalSizing buttonHorizontalSizing = ResizableComponent.HorizontalSizing.LEFT_WIDTH;

        private GridBuilder(Screen owner) {
            this.owner = owner;
            this.minecraft = owner.getMinecraft();
        }

        public Grid.GridBuilder addEditboxColumn(String name, String defaultValue, boolean editable, int size) {
            this.columns.put(name, new Grid.EditBoxColumn(defaultValue, editable, size));
            this.sizeTotal += size;
            return this;
        }

        public <T> Grid.GridBuilder addComboColumn(String name, Function<T, String> toVisualText, T defaultValue, T[] enums, boolean editable, int size) {
            this.columns.put(name, new Grid.ComboColumn(toVisualText, defaultValue, Arrays.asList(enums), editable, size));
            this.sizeTotal += size;
            return this;
        }

        public <T> Grid.GridBuilder addPopupColumn(String name, Function<T, String> toVisualText, T defaultValue, IForgeRegistry<T> registry, boolean editable, int size) {
            this.columns.put(name, new Grid.RegistryPopupColumn<>(toVisualText, defaultValue, registry, editable, size));
            this.sizeTotal += size;
            return this;
        }

        public Grid.GridBuilder xy1(int x1, int y1) {
            this.x1 = x1;
            this.y1 = y1;
            return this;
        }

        public Grid.GridBuilder xy2(int x2, int y2) {
            this.x2 = x2;
            this.y2 = y2;
            return this;
        }

        public Grid.GridBuilder rowHeight(int rowHeight) {
            this.rowHeight = rowHeight;
            return this;
        }

        public Grid.GridBuilder transparentBackground(boolean transparentBackground) {
            this.transparentBackground = transparentBackground;
            return this;
        }

        public Grid.GridBuilder rowEditable(boolean rowEditable) {
            this.rowEditable = rowEditable;
            return this;
        }

        public Grid.GridBuilder onAddPress(BiConsumer<Grid, Button> onAddPress) {
            this.onAddPress = onAddPress;
            return this;
        }

        public Grid.GridBuilder onRemovePress(BiConsumer<Grid, Button> OnRemovePress) {
            this.onRemovePress = OnRemovePress;
            return this;
        }

        public Grid.GridBuilder horizontalSizing(ResizableComponent.HorizontalSizing horizontalSizing) {
            this.horizontalSizing = horizontalSizing;
            return this;
        }

        public Grid.GridBuilder verticalSizing(ResizableComponent.VerticalSizing verticalSizing) {
            this.verticalSizing = verticalSizing;
            return this;
        }

        public Grid.GridBuilder buttonHorizontalSizing(ResizableComponent.HorizontalSizing buttonHorizontalSizing) {
            this.buttonHorizontalSizing = buttonHorizontalSizing;
            return this;
        }

        public Grid build() {
            return new Grid(this);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static class RegistryPopupColumn<T> extends Grid.Column<T> {

        final IForgeRegistry<T> registry;

        protected RegistryPopupColumn(Function<T, String> toVisualText, T defaultVal, IForgeRegistry<T> registry, boolean editable, int size) {
            super(toVisualText, defaultVal, editable, size);
            this.registry = registry;
        }

        @Override
        public AbstractWidget createEditWidget(Screen owner, Font font, int x, int y, int height, Grid.Row row, String colName, T value) {
            return new PopupBox<>(owner, font, x, y, this.size - 4, height, null, null, Component.literal("grid.popupEdit"), this.registry);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class Row extends ObjectSelectionList.Entry<Grid.Row> {

        private Map<String, Object> values = Maps.newLinkedHashMap();

        private Row() {
            for (String columnName : Grid.this.columns.keySet()) {
                this.values.put(columnName, null);
            }
        }

        public <T> T getValue(String columnName) {
            return (T) this.values.get(columnName);
        }

        public <T> void setValue(String columnName, T value) {
            if (!Grid.this.columns.containsKey(columnName)) {
                throw new IllegalArgumentException("There's no column named " + columnName + " in Grid");
            } else {
                this.values.put(columnName, value);
            }
        }

        @Override
        public Component getNarration() {
            return Component.translatable("narrator.select");
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
            int startX = Grid.this.f_93393_;
            for (java.util.Map.Entry<String, Object> entry : this.values.entrySet()) {
                Grid.Column<?> column = (Grid.Column<?>) Grid.this.columns.get(entry.getKey());
                if (Grid.this.transparentBackground) {
                    guiGraphics.setColor(0.12F, 0.12F, 0.12F, 1.0F);
                    guiGraphics.blit(Screen.BACKGROUND_LOCATION, startX + 1, top + 1, (float) (startX + column.size - 1), (float) (top + height), column.size - 2, height - 1, 32, 32);
                    guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                } else {
                    guiGraphics.fill(startX + 1, top + 1, startX + column.size - 1, top + height, -16777216);
                }
                startX += column.size;
                String displayText = column.toVisualText(entry.getValue());
                String correctedString = Grid.this.f_93386_.font.plainSubstrByWidth(displayText, column.size - 1);
                guiGraphics.drawString(Grid.this.f_93386_.font, correctedString, left + 2, top + Grid.this.f_93387_ / 2 - 4, 16777215, false);
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0) {
                int rowposition = Grid.this.m_6702_().indexOf(this);
                if (Grid.this.m_93511_() == this) {
                    if (Grid.this.editingColumn == null) {
                        Grid.this.setGridFocus(rowposition, this.getColumn(mouseX));
                    } else {
                        Grid.this.setGridFocus(rowposition, null);
                    }
                } else {
                    Grid.this.setGridFocus(rowposition, this.getColumn(mouseX));
                }
                Grid.this.setSelected(this);
                return true;
            } else {
                return false;
            }
        }

        public String getColumn(double mouseX) {
            double x = 0.0;
            for (java.util.Map.Entry<String, Grid.Column<?>> entry : Grid.this.columns.entrySet()) {
                x += (double) ((Grid.Column) entry.getValue()).size;
                if (mouseX < x) {
                    return (String) entry.getKey();
                }
            }
            return null;
        }
    }
}