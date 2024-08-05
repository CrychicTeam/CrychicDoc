package noppes.npcs.api.wrapper.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.gui.IComponentsWrapper;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.gui.IItemSlot;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.wrapper.ItemScriptedWrapper;

public class GuiComponentsWrapper implements IComponentsWrapper {

    private List<ICustomGuiComponent> components = new ArrayList();

    private List<IItemSlot> slots = new ArrayList();

    private List<IItemSlot> playerSlots = new ArrayList();

    public int slotId = 0;

    protected IPlayer player;

    public GuiComponentsWrapper(IPlayer player) {
        this.player = player;
    }

    public CustomGuiButtonWrapper addButton(int id, String label, int x, int y) {
        CustomGuiButtonWrapper component = new CustomGuiButtonWrapper(id, label, x, y);
        this.addComponent(component);
        return component;
    }

    public CustomGuiButtonWrapper addButton(int id, String label, int x, int y, int width, int height) {
        CustomGuiButtonWrapper component = new CustomGuiButtonWrapper(id, label, x, y, width, height);
        this.addComponent(component);
        return component;
    }

    public CustomGuiButtonListWrapper addButtonList(int id, int x, int y, int width, int height) {
        CustomGuiButtonListWrapper component = new CustomGuiButtonListWrapper(id, x, y, width, height);
        this.addComponent(component);
        return component;
    }

    public CustomGuiButtonWrapper addTexturedButton(int id, String label, int x, int y, int width, int height, String texture) {
        CustomGuiButtonWrapper component = new CustomGuiButtonWrapper(id, label, x, y, width, height, texture);
        this.addComponent(component);
        return component;
    }

    public CustomGuiButtonWrapper addTexturedButton(int id, String label, int x, int y, int width, int height, String texture, int textureX, int textureY) {
        CustomGuiButtonWrapper component = new CustomGuiButtonWrapper(id, label, x, y, width, height, texture, textureX, textureY);
        this.addComponent(component);
        return component;
    }

    public CustomGuiLabelWrapper addLabel(int id, String label, int x, int y, int width, int height) {
        CustomGuiLabelWrapper component = new CustomGuiLabelWrapper(id, label, x, y, width, height);
        this.addComponent(component);
        return component;
    }

    public CustomGuiLabelWrapper addLabel(int id, String label, int x, int y, int width, int height, int color) {
        CustomGuiLabelWrapper component = new CustomGuiLabelWrapper(id, label, x, y, width, height, color);
        this.addComponent(component);
        return component;
    }

    public CustomGuiTextFieldWrapper addTextField(int id, int x, int y, int width, int height) {
        CustomGuiTextFieldWrapper component = new CustomGuiTextFieldWrapper(id, x, y, width, height);
        this.addComponent(component);
        return component;
    }

    public CustomGuiTextAreaWrapper addTextArea(int id, int x, int y, int width, int height) {
        CustomGuiTextAreaWrapper component = new CustomGuiTextAreaWrapper(id, x, y, width, height);
        this.addComponent(component);
        return component;
    }

    public CustomGuiTexturedRectWrapper addTexturedRect(int id, String texture, int x, int y, int width, int height) {
        CustomGuiTexturedRectWrapper component = new CustomGuiTexturedRectWrapper(id, texture, x, y, width, height);
        this.addComponent(component);
        return component;
    }

    public CustomGuiTexturedRectWrapper addTexturedRect(int id, String texture, int x, int y, int width, int height, int textureX, int textureY) {
        CustomGuiTexturedRectWrapper component = new CustomGuiTexturedRectWrapper(id, texture, x, y, width, height, textureX, textureY);
        this.addComponent(component);
        return component;
    }

    public CustomGuiScrollWrapper addScroll(int id, int x, int y, int width, int height, String... list) {
        CustomGuiScrollWrapper component = new CustomGuiScrollWrapper(id, x, y, width, height, list);
        this.addComponent(component);
        return component;
    }

    public CustomGuiSliderWrapper addSlider(int id, int x, int y, int width, int height, String format) {
        CustomGuiSliderWrapper component = new CustomGuiSliderWrapper(id, format, x, y, width, height);
        this.addComponent(component);
        return component;
    }

    public CustomGuiEntityDisplayWrapper addEntityDisplay(int id, int x, int y, IEntity entity) {
        CustomGuiEntityDisplayWrapper component = new CustomGuiEntityDisplayWrapper(id, entity, x, y);
        this.addComponent(component);
        return component;
    }

    public CustomGuiAssetsSelectorWrapper addAssetsSelector(int id, int x, int y, int width, int height) {
        CustomGuiAssetsSelectorWrapper component = new CustomGuiAssetsSelectorWrapper(id, x, y, width, height);
        this.addComponent(component);
        return component;
    }

    @Override
    public ICustomGuiComponent getComponent(int componentID) {
        for (ICustomGuiComponent component : this.components) {
            if (component.getID() == componentID) {
                return component;
            }
        }
        return null;
    }

    @Override
    public void addComponent(ICustomGuiComponent component) {
        if (this.components.stream().anyMatch(t -> t.getID() == component.getID())) {
            throw new CustomNPCsException("This gui already contains component id:" + component.getID());
        } else {
            this.components.add(component);
        }
    }

    @Override
    public void removeComponent(int componentID) {
        this.components.removeIf(c -> c.getID() == componentID);
    }

    @Override
    public List<ICustomGuiComponent> getComponents() {
        return this.components;
    }

    public CompoundTag getComponentNbt() {
        CompoundTag comp = new CompoundTag();
        ListTag list = new ListTag();
        for (ICustomGuiComponent c : this.components) {
            list.add(((CustomGuiComponentWrapper) c).toNBT(new CompoundTag()));
        }
        comp.put("components", list);
        list = new ListTag();
        for (ICustomGuiComponent c : this.slots) {
            list.add(((CustomGuiComponentWrapper) c).toNBT(new CompoundTag()));
        }
        comp.put("slots", list);
        list = new ListTag();
        for (ICustomGuiComponent c : this.playerSlots) {
            list.add(((CustomGuiComponentWrapper) c).toNBT(new CompoundTag()));
        }
        comp.put("playerSlots", list);
        return comp;
    }

    private List<IItemSlot> getNbtSlots(CompoundTag tag, String key) {
        List<IItemSlot> slots = new ArrayList();
        for (Tag b : tag.getList(key, 10)) {
            CustomGuiItemSlotWrapper component = (CustomGuiItemSlotWrapper) CustomGuiComponentWrapper.createFromNBT((CompoundTag) b);
            slots.add(component);
        }
        return slots;
    }

    public void setComponentNbt(CompoundTag comp) {
        List<ICustomGuiComponent> components = new ArrayList();
        for (Tag b : comp.getList("components", 10)) {
            components.add(CustomGuiComponentWrapper.createFromNBT((CompoundTag) b));
        }
        this.components = components;
        this.slots = this.getNbtSlots(comp, "slots");
        this.playerSlots = this.getNbtSlots(comp, "playerSlots");
    }

    public ICustomGuiComponent getComponentUuid(UUID id) {
        for (ICustomGuiComponent comp : this.components) {
            if (comp.getUniqueID().equals(id)) {
                return comp;
            }
        }
        return null;
    }

    @Override
    public List<IItemSlot> getSlots() {
        return this.slots;
    }

    @Override
    public List<IItemSlot> getPlayerSlots() {
        return this.playerSlots;
    }

    @Override
    public IItemSlot addItemSlot(int x, int y) {
        return this.addItemSlot(x, y, ItemScriptedWrapper.AIR);
    }

    @Override
    public IItemSlot addItemSlot(int x, int y, IItemStack stack) {
        CustomGuiItemSlotWrapper slot = new CustomGuiItemSlotWrapper(x, y, stack);
        GuiComponentsWrapper w = this;
        if (this instanceof GuiComponentsScrollableWrapper scroll) {
            w = scroll.parent;
        }
        slot.setID(w.slotId++);
        this.slots.add(slot);
        return slot;
    }

    @Override
    public void removeItemSlot(IItemSlot slot) {
        this.slots.add(slot);
    }

    @Override
    public void showPlayerInventory(int x, int y) {
        this.showPlayerInventory(x, y, true);
    }

    @Override
    public IItemSlot[] showPlayerInventory(int x, int y, boolean full) {
        List<IItemSlot> playerSlots = new ArrayList();
        if (full) {
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 9; col++) {
                    CustomGuiItemSlotWrapper slot = new CustomGuiItemSlotWrapper(x + col * 18, y + row * 18, this.player.getMCEntity());
                    slot.setID(9 + row * 9 + col);
                    playerSlots.add(slot);
                }
            }
            y += 58;
        }
        for (int col = 0; col < 9; col++) {
            CustomGuiItemSlotWrapper slot = new CustomGuiItemSlotWrapper(x + col * 18, y, this.player.getMCEntity());
            slot.setID(col);
            playerSlots.add(slot);
        }
        this.playerSlots = playerSlots;
        return (IItemSlot[]) this.playerSlots.toArray(new IItemSlot[0]);
    }
}