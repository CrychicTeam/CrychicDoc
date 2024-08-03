package me.shedaniel.clothconfig2.api;

import me.shedaniel.clothconfig2.gui.ClothConfigScreen;
import me.shedaniel.math.Rectangle;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractConfigListEntry<T> extends AbstractConfigEntry<T> {

    private final Component fieldName;

    private boolean editable = true;

    private boolean requiresRestart;

    public AbstractConfigListEntry(Component fieldName, boolean requiresRestart) {
        this.fieldName = fieldName;
        this.requiresRestart = requiresRestart;
    }

    @Override
    public boolean isRequiresRestart() {
        return this.requiresRestart;
    }

    @Override
    public void setRequiresRestart(boolean requiresRestart) {
        this.requiresRestart = requiresRestart;
    }

    public boolean isEditable() {
        return this.getConfigScreen().isEditable() && this.editable && this.isEnabled();
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public final int getPreferredTextColor() {
        return this.getConfigError().isPresent() ? 16733525 : 16777215;
    }

    public Rectangle getEntryArea(int x, int y, int entryWidth, int entryHeight) {
        return new Rectangle(this.getParent().left, y, this.getParent().right - this.getParent().left, this.getItemHeight() - 4);
    }

    public boolean isMouseInside(int mouseX, int mouseY, int x, int y, int entryWidth, int entryHeight) {
        return this.getParent().isMouseOver((double) mouseX, (double) mouseY) && this.getEntryArea(x, y, entryWidth, entryHeight).contains(mouseX, mouseY);
    }

    @Override
    public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        if (this.isMouseInside(mouseX, mouseY, x, y, entryWidth, entryHeight)) {
            Rectangle area = this.getEntryArea(x, y, entryWidth, entryHeight);
            if (this.getParent() instanceof ClothConfigScreen.ListWidget) {
                ((ClothConfigScreen.ListWidget) this.getParent()).thisTimeTarget = new Rectangle((double) area.x, (double) area.y + this.getParent().getScroll(), (double) area.width, (double) area.height);
            }
        }
    }

    @Override
    public Component getFieldName() {
        return this.fieldName;
    }
}