package noppes.npcs.api.wrapper.gui;

import net.minecraft.nbt.CompoundTag;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.gui.IComponentsScrollableWrapper;
import noppes.npcs.api.gui.ICustomGuiComponent;

public class GuiComponentsScrollableWrapper extends GuiComponentsWrapper implements IComponentsScrollableWrapper {

    private boolean enabled = false;

    public int x;

    public int y;

    public int width;

    public int height;

    public int scrollAmount = 0;

    public GuiComponentsWrapper parent;

    public GuiComponentsScrollableWrapper(GuiComponentsWrapper parent, IPlayer player) {
        super(player);
        this.parent = parent;
    }

    public GuiComponentsScrollableWrapper init(int x, int y, int width, int height) {
        this.enabled = true;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        return this;
    }

    @Override
    public CompoundTag getComponentNbt() {
        CompoundTag comp = super.getComponentNbt();
        comp.putBoolean("enabled", this.enabled);
        comp.putInt("x", this.x);
        comp.putInt("y", this.y);
        comp.putInt("width", this.width);
        comp.putInt("height", this.height);
        return comp;
    }

    @Override
    public void setComponentNbt(CompoundTag comp) {
        super.setComponentNbt(comp);
        this.enabled = comp.getBoolean("enabled");
        this.x = comp.getInt("x");
        this.y = comp.getInt("y");
        this.width = comp.getInt("width");
        this.height = comp.getInt("height");
    }

    public boolean isVisible(ICustomGuiComponent component) {
        return component.getPosY() >= this.scrollAmount && component.getPosY() + component.getHeight() <= this.height + this.scrollAmount;
    }
}