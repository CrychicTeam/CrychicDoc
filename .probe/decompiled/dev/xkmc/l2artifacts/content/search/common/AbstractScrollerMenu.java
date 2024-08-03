package dev.xkmc.l2artifacts.content.search.common;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2library.base.menu.base.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.l2library.base.menu.data.IntDataSlot;
import dev.xkmc.l2library.base.menu.scroller.ScrollerMenu;
import dev.xkmc.l2library.util.code.GenericItemStack;
import java.util.List;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractScrollerMenu<T extends AbstractScrollerMenu<T>> extends BaseContainerMenu<T> implements ScrollerMenu, IFilterMenu {

    public final ArtifactChestToken token;

    public final IntDataSlot total_count;

    public final IntDataSlot current_count;

    public final IntDataSlot experience;

    public final DataSlot max_row;

    public final DataSlot row;

    protected final Player player;

    public final int extra;

    public AbstractScrollerMenu(MenuType<?> type, int wid, Inventory plInv, SpriteManager manager, int extra, ArtifactChestToken token, boolean isVirtual) {
        super(type, wid, plInv, manager, e -> new BaseContainerMenu.BaseContainer<>(36 + extra, e), isVirtual);
        this.token = token;
        this.player = plInv.player;
        this.extra = extra;
        this.total_count = new IntDataSlot(this);
        this.current_count = new IntDataSlot(this);
        this.experience = new IntDataSlot(this);
        this.max_row = this.m_38895_(DataSlot.standalone());
        this.row = this.m_38895_(DataSlot.standalone());
    }

    protected void reload(boolean changeContent) {
        if (!this.player.m_9236_().isClientSide()) {
            List<GenericItemStack<BaseArtifact>> list = this.token.getFiltered();
            this.max_row.set((int) Math.ceil((double) list.size() / 6.0));
            if (this.row.get() < 0) {
                this.row.set(0);
            }
            if (this.row.get() > this.getMaxScroll()) {
                this.row.set(this.getMaxScroll());
            }
            for (int i = 0; i < 36; i++) {
                int index = this.row.get() * 6 + i;
                ItemStack stack = index >= list.size() ? ItemStack.EMPTY : ((GenericItemStack) list.get(index)).stack();
                this.container.setItem(i + this.extra, stack);
            }
            this.total_count.set(this.token.list.size());
            this.current_count.set(list.size());
            this.experience.set(this.token.exp);
        }
    }

    @Override
    public final int getMaxScroll() {
        return Math.max(0, this.max_row.get() - 6);
    }

    @Override
    public final int getScroll() {
        return this.row.get();
    }

    @Override
    public boolean clickMenuButton(Player pPlayer, int pId) {
        int amount = pId / 100;
        pId %= 100;
        if (pId == 0) {
            this.row.set(this.row.get() - amount);
            this.reload(false);
            return true;
        } else if (pId == 1) {
            this.row.set(this.row.get() + amount);
            this.reload(false);
            return true;
        } else {
            pId -= 2;
            pId += this.row.get() * 6;
            if (pId >= 0 && pId < this.current_count.get()) {
                if (!this.player.m_9236_().isClientSide()) {
                    this.clickSlot(pId);
                }
                return true;
            } else {
                return super.m_6366_(pPlayer, pId);
            }
        }
    }

    protected abstract void clickSlot(int var1);

    @Override
    public final boolean stillValid(Player player) {
        return player.getInventory().getItem(this.token.invSlot) == this.token.stack;
    }

    @Override
    protected boolean shouldClear(Container container, int slot) {
        return slot < this.extra;
    }
}