package dev.xkmc.l2artifacts.content.search.recycle;

import dev.xkmc.l2artifacts.content.core.ArtifactStats;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.misc.ArtifactChestItem;
import dev.xkmc.l2artifacts.content.misc.ExpItem;
import dev.xkmc.l2artifacts.content.search.common.AbstractScrollerMenu;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.upgrades.ArtifactUpgradeManager;
import dev.xkmc.l2artifacts.init.registrate.ArtifactMenuRegistry;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.l2library.base.menu.data.BoolArrayDataSlot;
import dev.xkmc.l2library.base.menu.data.IntDataSlot;
import dev.xkmc.l2library.util.code.GenericItemStack;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class RecycleMenu extends AbstractScrollerMenu<RecycleMenu> {

    private static final SpriteManager MANAGER = new SpriteManager("l2artifacts", "recycle");

    public final IntDataSlot select_count;

    public final IntDataSlot to_gain;

    protected final BoolArrayDataSlot sel;

    protected boolean[] selected;

    public static RecycleMenu fromNetwork(MenuType<RecycleMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
        int i = buf.readInt();
        return new RecycleMenu(wid, plInv, ArtifactChestToken.of(plInv.player, i));
    }

    public RecycleMenu(int wid, Inventory plInv, ArtifactChestToken token) {
        super((MenuType<?>) ArtifactMenuRegistry.MT_RECYCLE.get(), wid, plInv, MANAGER, 1, token, true);
        this.addSlot("input", e -> e.getItem() instanceof ExpItem);
        this.addSlot("grid", e -> false, e -> e.setPickup(() -> false));
        this.select_count = new IntDataSlot(this);
        this.to_gain = new IntDataSlot(this);
        this.sel = new BoolArrayDataSlot(this, 36);
        this.reload(true);
    }

    @Override
    protected void reload(boolean changeContent) {
        super.reload(changeContent);
        if (changeContent) {
            List<GenericItemStack<BaseArtifact>> list = this.token.getFiltered();
            this.selected = new boolean[list.size()];
            for (int i = 0; i < list.size(); i++) {
                this.selected[i] = false;
            }
        }
        this.refreshSelected();
    }

    private void refreshSelected() {
        int val_0 = 0;
        int val_1 = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                int ind = i * 6 + j;
                int index = this.getScroll() * 6 + ind;
                this.sel.set(index < this.current_count.get() && this.selected[index], ind);
            }
        }
    }

    @Override
    protected void clickSlot(int slot) {
        int exp = this.getExp(slot);
        int s = this.selected[slot] ? -1 : 1;
        this.select_count.set(this.select_count.get() + s);
        this.to_gain.set(this.to_gain.get() + s * exp);
        this.selected[slot] = this.selected[slot] ^ true;
        this.refreshSelected();
    }

    private int getExp(int i) {
        List<GenericItemStack<BaseArtifact>> list = this.token.getFiltered();
        ItemStack stack = ((GenericItemStack) list.get(i)).stack();
        int rank = ((BaseArtifact) ((GenericItemStack) list.get(i)).item()).rank;
        ArtifactStats stat = (ArtifactStats) BaseArtifact.getStats(stack).orElse(null);
        return ArtifactUpgradeManager.getExpForConversion(rank, stat);
    }

    @Override
    public void slotsChanged(Container cont) {
        if (!this.player.m_9236_().isClientSide() && !cont.getItem(0).isEmpty()) {
            ItemStack stack = cont.getItem(0).copy();
            ExpItem item = (ExpItem) stack.getItem();
            this.addExp(ArtifactUpgradeManager.getExpForConversion(item.rank, null) * stack.getCount());
            cont.setItem(0, ItemStack.EMPTY);
        }
        super.m_6199_(cont);
    }

    @Override
    public boolean clickMenuButton(Player player, int pId) {
        if (pId == 50) {
            if (player.m_9236_().isClientSide) {
                return true;
            } else {
                List<GenericItemStack<BaseArtifact>> list = this.token.getFiltered();
                int exp = 0;
                for (int i = 0; i < this.selected.length; i++) {
                    if (this.selected[i]) {
                        ItemStack stack = ((GenericItemStack) list.get(i)).stack();
                        exp += this.getExp(i);
                        this.token.list.remove(stack);
                    }
                }
                this.addExp(exp);
                this.select_count.set(0);
                this.to_gain.set(0);
                this.token.update();
                this.token.save();
                this.reload(true);
                return true;
            }
        } else if (pId == 51) {
            if (player.m_9236_().isClientSide) {
                return true;
            } else {
                List<GenericItemStack<BaseArtifact>> list = this.token.getFiltered();
                int exp = 0;
                for (int ix = 0; ix < list.size(); ix++) {
                    this.selected[ix] = true;
                    exp += this.getExp(ix);
                }
                this.select_count.set(list.size());
                this.to_gain.set(exp);
                this.refreshSelected();
                return true;
            }
        } else if (pId != 52) {
            return super.clickMenuButton(player, pId);
        } else if (player.m_9236_().isClientSide) {
            return true;
        } else {
            List<GenericItemStack<BaseArtifact>> list = this.token.getFiltered();
            for (int ix = 0; ix < list.size(); ix++) {
                this.selected[ix] = false;
            }
            this.select_count.set(0);
            this.to_gain.set(0);
            this.refreshSelected();
            return true;
        }
    }

    private void addExp(int exp) {
        this.token.exp += exp;
        ArtifactChestItem.setExp(this.token.stack, this.token.exp);
        this.experience.set(this.token.exp);
        this.m_150429_();
    }
}