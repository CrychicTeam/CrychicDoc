package dev.xkmc.l2artifacts.content.swap;

import dev.xkmc.l2library.base.menu.base.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.l2library.base.menu.data.BoolArrayDataSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class ArtifactSwapMenu extends BaseContainerMenu<ArtifactSwapMenu> {

    public static final SpriteManager MANAGER = new SpriteManager("l2artifacts", "swap");

    private final int slot;

    private final ItemStack stack;

    public final ArtifactSwapData data;

    public final BoolArrayDataSlot disable = new BoolArrayDataSlot(this, 45);

    private boolean init = false;

    public static ArtifactSwapMenu fromNetwork(MenuType<ArtifactSwapMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
        int i = buf.readInt();
        return new ArtifactSwapMenu(type, wid, plInv, i);
    }

    protected ArtifactSwapMenu(MenuType<?> type, int wid, Inventory plInv, int slot) {
        super(type, wid, plInv, MANAGER, e -> new BaseContainerMenu.BaseContainer<>(45, e), false);
        this.slot = slot;
        this.stack = plInv.player.getInventory().getItem(slot);
        this.data = ArtifactSwapItem.getData(this.stack);
        this.addSlot("grid", (i, st) -> this.data.contents[i].canAccept(st));
        this.reload();
        this.init = true;
    }

    private void reload() {
        this.init = false;
        for (int i = 0; i < 45; i++) {
            this.container.setItem(i, this.data.contents[i].getStack().copy());
            this.disable.set(this.data.contents[i].isLocked(), i);
        }
        this.init = true;
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id >= 0 && id < 45) {
            if (!player.m_9236_().isClientSide()) {
                this.data.contents[id].toggle();
                this.save();
                this.reload();
            }
            return true;
        } else {
            return false;
        }
    }

    private void save() {
        if (this.init && !this.inventory.player.m_9236_().isClientSide()) {
            for (int i = 0; i < 45; i++) {
                this.data.contents[i].setStack(this.container.getItem(i).copy());
            }
            ArtifactSwapItem.setData(this.stack, this.data);
        }
    }

    @Override
    protected void securedServerSlotChange(Container cont) {
        if (this.init) {
            this.save();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getInventory().getItem(this.slot) == this.stack;
    }
}