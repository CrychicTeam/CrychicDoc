package noppes.npcs.api.wrapper.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import noppes.npcs.api.gui.ICustomGuiComponent;

public abstract class CustomGuiComponentWrapper implements ICustomGuiComponent {

    public UUID uniqueId = UUID.randomUUID();

    private int id;

    private int posX;

    private int posY;

    private int width;

    private int height;

    private List<Component> hoverText = new ArrayList();

    private boolean enabled = true;

    private boolean visible = true;

    public boolean disablePackets = false;

    public CustomGuiComponentWrapper setDisablePackets() {
        this.disablePackets = true;
        return this;
    }

    @Override
    public int getID() {
        return this.id;
    }

    public CustomGuiComponentWrapper setID(int id) {
        this.id = id;
        return this;
    }

    @Override
    public boolean getEnabled() {
        return this.enabled;
    }

    public CustomGuiComponentWrapper setEnabled(boolean bo) {
        this.enabled = bo;
        return this;
    }

    @Override
    public boolean getVisible() {
        return this.visible;
    }

    public CustomGuiComponentWrapper setVisible(boolean bo) {
        this.visible = bo;
        return this;
    }

    @Override
    public UUID getUniqueID() {
        return this.uniqueId;
    }

    @Override
    public int getPosX() {
        return this.posX;
    }

    @Override
    public int getPosY() {
        return this.posY;
    }

    public CustomGuiComponentWrapper setPos(int x, int y) {
        this.posX = x;
        this.posY = y;
        return this;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    public CustomGuiComponentWrapper setSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    @Override
    public boolean hasHoverText() {
        return this.hoverText.size() > 0;
    }

    @Override
    public String[] getHoverText() {
        String[] ht = new String[this.hoverText.size()];
        for (int i = 0; i < this.hoverText.size(); i++) {
            ht[i] = ((TranslatableContents) ((Component) this.hoverText.get(i)).getContents()).getKey();
        }
        return ht;
    }

    public List<Component> getHoverTextList() {
        return this.hoverText;
    }

    public CustomGuiComponentWrapper setHoverText(String text) {
        this.hoverText = new ArrayList();
        this.hoverText.add(Component.translatable(text));
        return this;
    }

    public CustomGuiComponentWrapper setHoverText(String[] text) {
        List<Component> list = new ArrayList();
        for (String s : text) {
            list.add(Component.translatable(s));
        }
        this.hoverText = list;
        return this;
    }

    public CustomGuiComponentWrapper setHoverText(List<Component> list) {
        this.hoverText = list;
        return this;
    }

    public CompoundTag toNBT(CompoundTag nbt) {
        nbt.putInt("id", this.id);
        nbt.putBoolean("enabled", this.enabled);
        nbt.putBoolean("visible", this.visible);
        nbt.putUUID("uniqueId", this.uniqueId);
        nbt.putIntArray("pos", new int[] { this.posX, this.posY });
        nbt.putIntArray("size", new int[] { this.width, this.height });
        if (this.hoverText != null) {
            ListTag list = new ListTag();
            for (Component s : this.hoverText) {
                list.add(StringTag.valueOf(((TranslatableContents) s.getContents()).getKey()));
            }
            if (list.size() > 0) {
                nbt.put("hover", list);
            }
        }
        nbt.putInt("type", this.getType());
        return nbt;
    }

    public CustomGuiComponentWrapper fromNBT(CompoundTag nbt) {
        this.setID(nbt.getInt("id"));
        this.setEnabled(nbt.getBoolean("enabled"));
        this.setVisible(nbt.getBoolean("visible"));
        this.uniqueId = nbt.getUUID("uniqueId");
        this.setPos(nbt.getIntArray("pos")[0], nbt.getIntArray("pos")[1]);
        this.setSize(nbt.getIntArray("size")[0], nbt.getIntArray("size")[1]);
        if (nbt.contains("hover")) {
            ListTag list = nbt.getList("hover", 8);
            String[] hoverText = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                hoverText[i] = list.get(i).getAsString();
            }
            this.setHoverText(hoverText);
        }
        return this;
    }

    public static CustomGuiComponentWrapper createFromNBT(CompoundTag nbt) {
        switch(nbt.getInt("type")) {
            case 0:
                return new CustomGuiButtonWrapper().fromNBT(nbt);
            case 1:
                return new CustomGuiLabelWrapper().fromNBT(nbt);
            case 2:
                return new CustomGuiTexturedRectWrapper().fromNBT(nbt);
            case 3:
                return new CustomGuiTextFieldWrapper().fromNBT(nbt);
            case 4:
                return new CustomGuiScrollWrapper().fromNBT(nbt);
            case 5:
                return new CustomGuiItemSlotWrapper().fromNBT(nbt);
            case 6:
                return new CustomGuiTextAreaWrapper().fromNBT(nbt);
            case 7:
                return new CustomGuiButtonListWrapper().fromNBT(nbt);
            case 8:
                return new CustomGuiSliderWrapper().fromNBT(nbt);
            case 9:
                return new CustomGuiEntityDisplayWrapper().fromNBT(nbt);
            case 10:
                return new CustomGuiAssetsSelectorWrapper().fromNBT(nbt);
            default:
                return null;
        }
    }
}