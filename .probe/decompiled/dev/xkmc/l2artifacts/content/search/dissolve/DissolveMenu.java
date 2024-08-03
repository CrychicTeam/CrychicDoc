package dev.xkmc.l2artifacts.content.search.dissolve;

import dev.xkmc.l2artifacts.content.core.ArtifactStats;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.search.common.AbstractScrollerMenu;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.upgrades.StatContainerItem;
import dev.xkmc.l2artifacts.init.registrate.ArtifactMenuRegistry;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.l2library.base.menu.data.IntDataSlot;
import dev.xkmc.l2library.util.code.GenericItemStack;
import java.util.Optional;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class DissolveMenu extends AbstractScrollerMenu<DissolveMenu> {

    private static final SpriteManager MANAGER = new SpriteManager("l2artifacts", "dissolve");

    protected final IntDataSlot select_index;

    private int selected = -1;

    public static DissolveMenu fromNetwork(MenuType<DissolveMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
        int i = buf.readInt();
        return new DissolveMenu(wid, plInv, ArtifactChestToken.of(plInv.player, i));
    }

    public DissolveMenu(int wid, Inventory plInv, ArtifactChestToken token) {
        super((MenuType<?>) ArtifactMenuRegistry.MT_DISSOLVE.get(), wid, plInv, MANAGER, 2, token, true);
        this.addSlot("input", e -> e.getItem() instanceof StatContainerItem && StatContainerItem.getType(e).isEmpty());
        this.addSlot("output", e -> false);
        this.addSlot("grid", e -> false, e -> e.setPickup(() -> false));
        this.select_index = new IntDataSlot(this);
        this.reload(true);
    }

    @Override
    protected void reload(boolean changeContent) {
        super.reload(changeContent);
        this.select_index.set(this.selected - this.getScroll() * 6);
    }

    private ItemStack setSelected(int slot) {
        this.selected = slot;
        this.select_index.set(this.selected - this.getScroll() * 6);
        if (this.selected < 0) {
            return ItemStack.EMPTY;
        } else {
            GenericItemStack<BaseArtifact> art = (GenericItemStack<BaseArtifact>) this.token.getFiltered().get(slot);
            ItemStack ans = this.container.getItem(0).getItem().getDefaultInstance();
            Optional<ArtifactStats> opt = BaseArtifact.getStats(art.stack());
            if (opt.isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                StatContainerItem.setStat(ans, ((ArtifactStats) opt.get()).main_stat.type);
                return ans;
            }
        }
    }

    @Override
    protected void clickSlot(int slot) {
        if (!this.container.getItem(0).isEmpty()) {
            if (((BaseArtifact) ((GenericItemStack) this.token.getFiltered().get(slot)).item()).rank == ((StatContainerItem) this.container.getItem(0).getItem()).rank) {
                this.container.setItem(1, this.setSelected(slot));
            }
        }
    }

    @Override
    public void slotsChanged(Container cont) {
        if (!this.player.m_9236_().isClientSide()) {
            if (!cont.getItem(0).isEmpty() && this.selected >= 0 && ((BaseArtifact) ((GenericItemStack) this.token.getFiltered().get(this.selected)).item()).rank != ((StatContainerItem) this.container.getItem(0).getItem()).rank) {
                this.container.setItem(1, this.setSelected(-1));
            }
            if (cont.getItem(0).isEmpty() && this.selected >= 0) {
                this.container.setItem(1, this.setSelected(-1));
            }
            if (this.selected >= 0 && cont.getItem(1).isEmpty()) {
                this.container.getItem(0).shrink(1);
                this.removeSelected();
            }
        }
        super.m_6199_(cont);
    }

    private void removeSelected() {
        this.token.list.remove(((GenericItemStack) this.token.getFiltered().get(this.selected)).stack());
        this.token.update();
        this.token.save();
        this.setSelected(-1);
        this.reload(true);
    }

    @Override
    protected boolean shouldClear(Container container, int slot) {
        return slot == 0;
    }
}