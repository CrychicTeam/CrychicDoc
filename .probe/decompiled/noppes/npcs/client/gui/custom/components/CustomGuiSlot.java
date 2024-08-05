package noppes.npcs.client.gui.custom.components;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.EventHooks;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.gui.IItemSlot;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiItemSlotWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.containers.ContainerCustomGui;

public class CustomGuiSlot extends Slot {

    private final Player player;

    public final IItemSlot slot;

    private final CustomGuiWrapper gui;

    private static Field xField;

    private static Field yField;

    public CustomGuiSlot(CustomGuiWrapper gui, Container inventoryIn, int id, IItemSlot slot, Player player) {
        super(inventoryIn, id, -666667, -666666);
        this.gui = gui;
        this.player = player;
        this.slot = slot;
        if (yField == null) {
            for (Field f : Slot.class.getDeclaredFields()) {
                if (!Modifier.isPrivate(f.getModifiers())) {
                    try {
                        if (f.get(this) instanceof Integer i && i == -666666) {
                            yField = f;
                            yField.setAccessible(true);
                        }
                        if (f.get(this) instanceof Integer i && i == -666667) {
                            xField = f;
                            xField.setAccessible(true);
                        }
                    } catch (IllegalAccessException var12) {
                        var12.printStackTrace();
                    }
                }
            }
        }
        this.update(0, 0);
    }

    public CustomGuiSlot update(int x, int y) {
        try {
            xField.set(this, x + this.slot.getPosX());
            yField.set(this, y + this.slot.getPosY());
        } catch (IllegalAccessException var4) {
            var4.printStackTrace();
        }
        return this;
    }

    @Override
    public void set(ItemStack is) {
        super.set(is);
        if (!this.player.m_9236_().isClientSide && this.m_7993_() != this.slot.getStack().getMCItemStack()) {
            if (!this.slot.isPlayerSlot()) {
                this.slot.setStack(NpcAPI.Instance().getIItemStack(this.m_7993_()));
                ((CustomGuiItemSlotWrapper) this.slot).onUpdate(this.gui);
            }
            if (this.player.containerMenu instanceof ContainerCustomGui container) {
                EventHooks.onCustomGuiSlot((PlayerWrapper) NpcAPI.Instance().getIEntity(this.player), container.customGui, this.slot);
            }
        }
    }
}