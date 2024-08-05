package dev.xkmc.l2artifacts.content.search.augment;

import dev.xkmc.l2artifacts.content.core.ArtifactStats;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.misc.ArtifactChestItem;
import dev.xkmc.l2artifacts.content.search.common.IFilterMenu;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.upgrades.ArtifactUpgradeManager;
import dev.xkmc.l2artifacts.content.upgrades.StatContainerItem;
import dev.xkmc.l2artifacts.content.upgrades.Upgrade;
import dev.xkmc.l2artifacts.init.data.ArtifactConfig;
import dev.xkmc.l2artifacts.init.registrate.ArtifactMenuRegistry;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
import dev.xkmc.l2library.base.menu.base.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.base.PredSlot;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.l2library.base.menu.data.IntDataSlot;
import dev.xkmc.l2library.util.code.GenericItemStack;
import java.util.Optional;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class AugmentMenu extends BaseContainerMenu<AugmentMenu> implements IFilterMenu {

    private static final SpriteManager MANAGER = new SpriteManager("l2artifacts", "augment");

    public final ArtifactChestToken token;

    public final Player player;

    public final IntDataSlot experience;

    public final IntDataSlot exp_cost;

    public final IntDataSlot mask;

    private final PredSlot input;

    private final PredSlot in_0;

    private final PredSlot in_1;

    private final PredSlot in_2;

    public static AugmentMenu fromNetwork(MenuType<AugmentMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
        int i = buf.readInt();
        return new AugmentMenu(wid, plInv, ArtifactChestToken.of(plInv.player, i));
    }

    public AugmentMenu(int wid, Inventory plInv, ArtifactChestToken token) {
        super((MenuType<?>) ArtifactMenuRegistry.MT_AUGMENT.get(), wid, plInv, MANAGER, e -> new BaseContainerMenu.BaseContainer<>(4, e), true);
        this.token = token;
        this.player = plInv.player;
        this.addSlot("input", e -> e.getItem() instanceof BaseArtifact);
        this.addSlot("in_0", e -> e.getItem() == ArtifactItems.ITEM_STAT[this.getMainItem().item().rank - 1].get(), e -> e.setInputLockPred(this::isSlotLocked));
        this.addSlot("in_1", e -> e.getItem() == ArtifactItems.ITEM_BOOST_MAIN[this.getMainItem().item().rank - 1].get(), e -> e.setInputLockPred(this::isSlotLocked));
        this.addSlot("in_2", e -> e.getItem() == ArtifactItems.ITEM_BOOST_SUB[this.getMainItem().item().rank - 1].get(), e -> e.setInputLockPred(this::isSlotLocked));
        this.experience = new IntDataSlot(this);
        this.exp_cost = new IntDataSlot(this);
        this.mask = new IntDataSlot(this);
        this.experience.set(token.exp);
        this.input = this.getAsPredSlot("input");
        this.in_0 = this.getAsPredSlot("in_0");
        this.in_1 = this.getAsPredSlot("in_1");
        this.in_2 = this.getAsPredSlot("in_2");
    }

    private GenericItemStack<BaseArtifact> getMainItem() {
        return GenericItemStack.of(this.input.m_7993_());
    }

    private boolean isSlotLocked() {
        return this.input.m_7993_().isEmpty();
    }

    @Override
    protected void securedServerSlotChange(Container cont) {
        ItemStack stack = this.input.m_7993_();
        int ec = 0;
        boolean useStat = false;
        boolean useSub = false;
        boolean useMain = false;
        if (!stack.isEmpty()) {
            BaseArtifact item = this.getMainItem().item();
            InteractionResultHolder<ItemStack> result = item.resolve(stack, false, this.player.m_217043_());
            if (result.getResult().consumesAction()) {
                stack = result.getObject();
                this.input.m_5852_(stack);
            }
        }
        this.in_0.updateEject(this.player);
        this.in_1.updateEject(this.player);
        this.in_2.updateEject(this.player);
        if (!stack.isEmpty()) {
            BaseArtifact item = this.getMainItem().item();
            Optional<ArtifactStats> opt = BaseArtifact.getStats(stack);
            if (opt.isPresent()) {
                ArtifactStats stats = (ArtifactStats) opt.get();
                if (stats.level < ArtifactUpgradeManager.getMaxLevel(item.rank)) {
                    ec = ArtifactUpgradeManager.getExpForLevel(item.rank, stats.level) - stats.exp;
                    useMain = !this.in_1.m_7993_().isEmpty();
                    if ((stats.level + 1) % ArtifactConfig.COMMON.levelPerSubStat.get() == 0) {
                        useSub = !this.in_2.m_7993_().isEmpty();
                        ItemStack stat = this.in_0.m_7993_();
                        Optional<ResourceLocation> opt_stat = StatContainerItem.getType(stat);
                        if (opt_stat.isPresent()) {
                            ResourceLocation astat = (ResourceLocation) opt_stat.get();
                            if (!stats.main_stat.type.equals(astat) && stats.map.containsKey(astat)) {
                                useStat = true;
                            }
                        }
                    }
                }
            }
        }
        this.exp_cost.set(ec);
        this.mask.set((useStat ? 1 : 0) + (useMain ? 2 : 0) + (useSub ? 4 : 0));
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
            ItemStack stack = this.getAsPredSlot("input").m_7993_();
            Upgrade upgrade = (Upgrade) BaseArtifact.getUpgrade(stack).orElseGet(Upgrade::new);
            int mask = this.mask.get();
            if ((mask & 1) > 0) {
                ItemStack stat = this.getAsPredSlot("in_0").m_7993_();
                Optional<ResourceLocation> opt_stat = StatContainerItem.getType(stat);
                opt_stat.ifPresent(artifactStatType -> upgrade.stats.add(artifactStatType));
                this.getAsPredSlot("in_0").m_7993_().shrink(1);
            }
            if ((mask & 2) > 0) {
                upgrade.main++;
                this.getAsPredSlot("in_1").m_7993_().shrink(1);
            }
            if ((mask & 4) > 0) {
                upgrade.sub++;
                this.getAsPredSlot("in_2").m_7993_().shrink(1);
            }
            BaseArtifact.setUpgrade(stack, upgrade);
            BaseArtifact.upgrade(stack, cost, player.m_217043_());
            stack = ((BaseArtifact) stack.getItem()).resolve(stack, false, player.m_217043_()).getObject();
            this.getAsPredSlot("input").m_5852_(stack);
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