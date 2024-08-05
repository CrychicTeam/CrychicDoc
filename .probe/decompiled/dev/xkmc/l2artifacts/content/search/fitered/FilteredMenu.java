package dev.xkmc.l2artifacts.content.search.fitered;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.misc.RandomArtifactItem;
import dev.xkmc.l2artifacts.content.search.common.AbstractScrollerMenu;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.init.data.ArtifactConfig;
import dev.xkmc.l2artifacts.init.registrate.ArtifactMenuRegistry;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.l2library.util.code.GenericItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class FilteredMenu extends AbstractScrollerMenu<FilteredMenu> {

    private static final SpriteManager MANAGER = new SpriteManager("l2artifacts", "filtered");

    private ItemStack selected = ItemStack.EMPTY;

    public static FilteredMenu fromNetwork(MenuType<FilteredMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
        int i = buf.readInt();
        return new FilteredMenu(wid, plInv, ArtifactChestToken.of(plInv.player, i));
    }

    public FilteredMenu(int wid, Inventory plInv, ArtifactChestToken token) {
        super((MenuType<?>) ArtifactMenuRegistry.MT_FILTER.get(), wid, plInv, MANAGER, 2, token, false);
        this.addSlot("input", e -> token.list.size() < this.getMaxSize() && (e.getItem() instanceof BaseArtifact || e.getItem() instanceof RandomArtifactItem));
        this.addSlot("output", e -> false);
        this.addSlot("grid", e -> false, e -> e.setPickup(() -> false));
        this.reload(true);
    }

    private int getMaxSize() {
        return this.token.stack.getItem() == ArtifactItems.FILTER.get() ? ArtifactConfig.COMMON.storageSmall.get() : ArtifactConfig.COMMON.storageLarge.get();
    }

    @Override
    protected void clickSlot(int slot) {
        this.selected = ((GenericItemStack) this.token.getFiltered().get(slot)).stack();
        this.container.setItem(1, this.selected.copy());
    }

    @Override
    public void slotsChanged(Container cont) {
        if (!this.player.m_9236_().isClientSide()) {
            if (!cont.getItem(0).isEmpty()) {
                ItemStack stack = cont.getItem(0).copy();
                if (stack.getItem() instanceof RandomArtifactItem item) {
                    for (int i = 0; i < stack.getCount(); i++) {
                        this.addItemToList(RandomArtifactItem.getRandomArtifact(stack, item.rank, this.player.m_217043_()));
                    }
                } else {
                    this.addItemToList(stack);
                }
                cont.setItem(0, ItemStack.EMPTY);
                this.token.update();
                this.token.save();
                this.selected = ItemStack.EMPTY;
                this.container.setItem(1, this.selected);
                this.reload(true);
            }
            if (!this.selected.isEmpty() && cont.getItem(1).isEmpty()) {
                this.removeSelected();
            }
        }
        super.m_6199_(cont);
    }

    private void addItemToList(ItemStack stack) {
        stack = ((BaseArtifact) stack.getItem()).resolve(stack, false, this.player.m_217043_()).getObject();
        this.token.list.add(stack);
    }

    private void removeSelected() {
        this.token.list.remove(this.selected);
        this.token.update();
        this.token.save();
        this.selected = ItemStack.EMPTY;
        this.reload(true);
    }
}