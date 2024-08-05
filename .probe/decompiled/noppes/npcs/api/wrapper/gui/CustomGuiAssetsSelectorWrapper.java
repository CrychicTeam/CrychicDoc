package noppes.npcs.api.wrapper.gui;

import net.minecraft.nbt.CompoundTag;
import noppes.npcs.api.function.gui.GuiComponentClicked;
import noppes.npcs.api.function.gui.GuiComponentUpdate;
import noppes.npcs.api.gui.IAssetsSelector;
import noppes.npcs.api.gui.ICustomGui;

public class CustomGuiAssetsSelectorWrapper extends CustomGuiComponentWrapper implements IAssetsSelector {

    private String selected = "";

    private String root = "textures";

    private String type = "png";

    private GuiComponentUpdate<IAssetsSelector> onChange = null;

    private GuiComponentClicked<IAssetsSelector> onPress = null;

    public CustomGuiAssetsSelectorWrapper() {
    }

    public CustomGuiAssetsSelectorWrapper(int id, int x, int y, int width, int height) {
        this.setID(id);
        this.setPos(x, y);
        this.setSize(width, height);
    }

    @Override
    public String getSelected() {
        return this.selected;
    }

    public CustomGuiAssetsSelectorWrapper setSelected(String selected) {
        this.selected = selected;
        return this;
    }

    @Override
    public String getRoot() {
        return this.root;
    }

    public CustomGuiAssetsSelectorWrapper setRoot(String root) {
        this.root = root;
        return this;
    }

    @Override
    public String getFileType() {
        return this.type;
    }

    public CustomGuiAssetsSelectorWrapper setFileType(String type) {
        this.type = type;
        return this;
    }

    public final void onPress(ICustomGui gui) {
        if (this.onPress != null) {
            this.onPress.onClick(gui, this);
        }
    }

    public CustomGuiAssetsSelectorWrapper setOnPress(GuiComponentClicked<IAssetsSelector> onPress) {
        this.onPress = onPress;
        return this;
    }

    public final void onChange(ICustomGui gui) {
        if (this.onChange != null) {
            this.onChange.onChange(gui, this);
        }
    }

    public CustomGuiAssetsSelectorWrapper setOnChange(GuiComponentUpdate<IAssetsSelector> onChange) {
        this.onChange = onChange;
        return this;
    }

    @Override
    public int getType() {
        return 10;
    }

    @Override
    public CompoundTag toNBT(CompoundTag nbt) {
        super.toNBT(nbt);
        nbt.putString("selected", this.selected);
        nbt.putString("filetype", this.type);
        nbt.putString("root", this.root);
        return nbt;
    }

    @Override
    public CustomGuiComponentWrapper fromNBT(CompoundTag nbt) {
        super.fromNBT(nbt);
        this.setSelected(nbt.getString("selected"));
        this.setFileType(nbt.getString("filetype"));
        this.setRoot(nbt.getString("root"));
        return this;
    }
}