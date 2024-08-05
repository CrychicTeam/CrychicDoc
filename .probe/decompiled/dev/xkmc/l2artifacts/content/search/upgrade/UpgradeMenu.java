package dev.xkmc.l2artifacts.content.search.upgrade;

import dev.xkmc.l2artifacts.content.core.ArtifactStats;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.misc.ArtifactChestItem;
import dev.xkmc.l2artifacts.content.search.common.IFilterMenu;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.upgrades.ArtifactUpgradeManager;
import dev.xkmc.l2artifacts.init.registrate.ArtifactMenuRegistry;
import dev.xkmc.l2library.base.menu.base.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.l2library.base.menu.data.IntDataSlot;
import java.util.Optional;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class UpgradeMenu extends BaseContainerMenu<UpgradeMenu> implements IFilterMenu {

    private static final SpriteManager MANAGER = new SpriteManager("l2artifacts", "upgrade");

    public final ArtifactChestToken token;

    public final Player player;

    public final IntDataSlot experience;

    public final IntDataSlot exp_cost;

    public static UpgradeMenu fromNetwork(MenuType<UpgradeMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
        int i = buf.readInt();
        return new UpgradeMenu(wid, plInv, ArtifactChestToken.of(plInv.player, i));
    }

    public UpgradeMenu(int wid, Inventory plInv, ArtifactChestToken token) {
        super((MenuType<?>) ArtifactMenuRegistry.MT_UPGRADE.get(), wid, plInv, MANAGER, e -> new BaseContainerMenu.BaseContainer<>(1, e), true);
        this.token = token;
        this.player = plInv.player;
        this.addSlot("input", e -> e.getItem() instanceof BaseArtifact);
        this.experience = new IntDataSlot(this);
        this.exp_cost = new IntDataSlot(this);
        this.experience.set(token.exp);
    }

    @Override
    public void slotsChanged(Container cont) {
        if (!this.player.m_9236_().isClientSide) {
            ItemStack stack = cont.getItem(0);
            int ec = 0;
            if (!stack.isEmpty()) {
                BaseArtifact item = (BaseArtifact) stack.getItem();
                InteractionResultHolder<ItemStack> result = item.resolve(stack, false, this.player.m_217043_());
                if (result.getResult().consumesAction()) {
                    stack = result.getObject();
                    cont.setItem(0, stack);
                }
                Optional<ArtifactStats> opt = BaseArtifact.getStats(stack);
                if (opt.isPresent()) {
                    ArtifactStats stats = (ArtifactStats) opt.get();
                    if (stats.level < ArtifactUpgradeManager.getMaxLevel(item.rank)) {
                        ec = ArtifactUpgradeManager.getExpForLevel(item.rank, stats.level) - stats.exp;
                    }
                }
            }
            this.exp_cost.set(ec);
            super.slotsChanged(cont);
        }
    }

    @Override
    public boolean clickMenuButton(Player player, int data) {
        if (data == 0) {
            int cost = this.exp_cost.get();
            boolean canUpgrade = cost > 0 && cost <= this.experience.get();
            if (player.m_9236_().isClientSide) {
                return canUpgrade;
            }
            if (!canUpgrade) {
                return false;
            }
            ItemStack stack = this.container.getItem(0);
            BaseArtifact.upgrade(stack, cost, player.m_217043_());
            stack = ((BaseArtifact) stack.getItem()).resolve(stack, false, player.m_217043_()).getObject();
            this.container.setItem(0, stack);
            this.costExp(cost);
        }
        return false;
    }

    private void costExp(int exp) {
        this.token.exp -= exp;
        ArtifactChestItem.setExp(this.token.stack, this.token.exp);
        this.experience.set(this.token.exp);
        this.m_150429_();
    }
}