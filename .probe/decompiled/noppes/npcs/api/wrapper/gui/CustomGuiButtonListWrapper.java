package noppes.npcs.api.wrapper.gui;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import noppes.npcs.api.function.gui.GuiComponentClicked;
import noppes.npcs.api.gui.IButton;
import noppes.npcs.api.gui.IButtonList;
import noppes.npcs.api.gui.ITexturedRect;

public class CustomGuiButtonListWrapper extends CustomGuiButtonWrapper implements IButtonList {

    CustomGuiTexturedRectWrapper left = new CustomGuiTexturedRectWrapper();

    CustomGuiTexturedRectWrapper right = new CustomGuiTexturedRectWrapper();

    private int selected = 0;

    private String[] values = new String[0];

    public CustomGuiButtonListWrapper() {
    }

    public CustomGuiButtonListWrapper(int id, int x, int y, int width, int height) {
        super(id, "", x, y, width, height);
        ITexturedRect rect = this.getTextureRect();
        rect.setTexture("customnpcs:textures/gui/components.png");
        rect.setRepeatingTexture(64, 22, 3).setTextureOffset(0, 64).setPos(7, 0);
        this.setTextureHoverOffset(22);
        this.left.setTexture("customnpcs:textures/gui/components.png").setTextureOffset(0, 130);
        this.left.setSize(10, 20).setPos(0, 0);
        this.right.setTexture("customnpcs:textures/gui/components.png").setTextureOffset(12, 130);
        this.right.setSize(10, 20).setPos(width - 10, 0);
    }

    public CustomGuiButtonListWrapper setSize(int width, int height) {
        super.setSize(width, height);
        this.getTextureRect().setSize(width - 14, height);
        return this;
    }

    public CustomGuiButtonListWrapper setValues(String... values) {
        if (values != null && values.length != 0) {
            this.values = values;
            this.selected %= values.length;
            this.setLabel(this.values[this.selected]);
        } else {
            this.values = new String[0];
            this.setLabel("");
        }
        return this;
    }

    @Override
    public String[] getValues() {
        return this.values;
    }

    public CustomGuiButtonListWrapper setSelected(int selected) {
        if (selected < 0) {
            selected += this.values.length;
        }
        if (selected >= this.values.length) {
            selected %= this.values.length;
        }
        this.selected = selected;
        this.setLabel(this.values[this.selected]);
        return this;
    }

    @Override
    public int getSelected() {
        return this.selected;
    }

    public CustomGuiTexturedRectWrapper getLeftTexture() {
        return this.left;
    }

    public CustomGuiTexturedRectWrapper getRightTexture() {
        return this.right;
    }

    @Override
    public int getType() {
        return 7;
    }

    public CustomGuiButtonListWrapper setOnPress(GuiComponentClicked<IButton> onPress) {
        super.setOnPress(onPress);
        return this;
    }

    @Override
    public CompoundTag toNBT(CompoundTag nbt) {
        super.toNBT(nbt);
        nbt.putInt("selected", this.selected);
        ListTag list = new ListTag();
        for (String s : this.values) {
            list.add(StringTag.valueOf(s));
        }
        nbt.put("values", list);
        nbt.put("left", this.left.toNBT(new CompoundTag()));
        nbt.put("right", this.right.toNBT(new CompoundTag()));
        return nbt;
    }

    @Override
    public CustomGuiComponentWrapper fromNBT(CompoundTag nbt) {
        super.fromNBT(nbt);
        this.selected = nbt.getInt("selected");
        this.values = (String[]) nbt.getList("values", 8).stream().map(Tag::m_7916_).toArray(String[]::new);
        this.left.fromNBT(nbt.getCompound("left"));
        this.right.fromNBT(nbt.getCompound("right"));
        return this;
    }
}