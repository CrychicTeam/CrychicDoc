package net.minecraft.world.inventory;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.ClientSideMerchant;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

public class MerchantMenu extends AbstractContainerMenu {

    protected static final int PAYMENT1_SLOT = 0;

    protected static final int PAYMENT2_SLOT = 1;

    protected static final int RESULT_SLOT = 2;

    private static final int INV_SLOT_START = 3;

    private static final int INV_SLOT_END = 30;

    private static final int USE_ROW_SLOT_START = 30;

    private static final int USE_ROW_SLOT_END = 39;

    private static final int SELLSLOT1_X = 136;

    private static final int SELLSLOT2_X = 162;

    private static final int BUYSLOT_X = 220;

    private static final int ROW_Y = 37;

    private final Merchant trader;

    private final MerchantContainer tradeContainer;

    private int merchantLevel;

    private boolean showProgressBar;

    private boolean canRestock;

    public MerchantMenu(int int0, Inventory inventory1) {
        this(int0, inventory1, new ClientSideMerchant(inventory1.player));
    }

    public MerchantMenu(int int0, Inventory inventory1, Merchant merchant2) {
        super(MenuType.MERCHANT, int0);
        this.trader = merchant2;
        this.tradeContainer = new MerchantContainer(merchant2);
        this.m_38897_(new Slot(this.tradeContainer, 0, 136, 37));
        this.m_38897_(new Slot(this.tradeContainer, 1, 162, 37));
        this.m_38897_(new MerchantResultSlot(inventory1.player, merchant2, this.tradeContainer, 2, 220, 37));
        for (int $$3 = 0; $$3 < 3; $$3++) {
            for (int $$4 = 0; $$4 < 9; $$4++) {
                this.m_38897_(new Slot(inventory1, $$4 + $$3 * 9 + 9, 108 + $$4 * 18, 84 + $$3 * 18));
            }
        }
        for (int $$5 = 0; $$5 < 9; $$5++) {
            this.m_38897_(new Slot(inventory1, $$5, 108 + $$5 * 18, 142));
        }
    }

    public void setShowProgressBar(boolean boolean0) {
        this.showProgressBar = boolean0;
    }

    @Override
    public void slotsChanged(Container container0) {
        this.tradeContainer.updateSellItem();
        super.slotsChanged(container0);
    }

    public void setSelectionHint(int int0) {
        this.tradeContainer.setSelectionHint(int0);
    }

    @Override
    public boolean stillValid(Player player0) {
        return this.trader.getTradingPlayer() == player0;
    }

    public int getTraderXp() {
        return this.trader.getVillagerXp();
    }

    public int getFutureTraderXp() {
        return this.tradeContainer.getFutureXp();
    }

    public void setXp(int int0) {
        this.trader.overrideXp(int0);
    }

    public int getTraderLevel() {
        return this.merchantLevel;
    }

    public void setMerchantLevel(int int0) {
        this.merchantLevel = int0;
    }

    public void setCanRestock(boolean boolean0) {
        this.canRestock = boolean0;
    }

    public boolean canRestock() {
        return this.canRestock;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack itemStack0, Slot slot1) {
        return false;
    }

    @Override
    public ItemStack quickMoveStack(Player player0, int int1) {
        ItemStack $$2 = ItemStack.EMPTY;
        Slot $$3 = (Slot) this.f_38839_.get(int1);
        if ($$3 != null && $$3.hasItem()) {
            ItemStack $$4 = $$3.getItem();
            $$2 = $$4.copy();
            if (int1 == 2) {
                if (!this.m_38903_($$4, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                $$3.onQuickCraft($$4, $$2);
                this.playTradeSound();
            } else if (int1 != 0 && int1 != 1) {
                if (int1 >= 3 && int1 < 30) {
                    if (!this.m_38903_($$4, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (int1 >= 30 && int1 < 39 && !this.m_38903_($$4, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_($$4, 3, 39, false)) {
                return ItemStack.EMPTY;
            }
            if ($$4.isEmpty()) {
                $$3.setByPlayer(ItemStack.EMPTY);
            } else {
                $$3.setChanged();
            }
            if ($$4.getCount() == $$2.getCount()) {
                return ItemStack.EMPTY;
            }
            $$3.onTake(player0, $$4);
        }
        return $$2;
    }

    private void playTradeSound() {
        if (!this.trader.isClientSide()) {
            Entity $$0 = (Entity) this.trader;
            $$0.level().playLocalSound($$0.getX(), $$0.getY(), $$0.getZ(), this.trader.getNotifyTradeSound(), SoundSource.NEUTRAL, 1.0F, 1.0F, false);
        }
    }

    @Override
    public void removed(Player player0) {
        super.removed(player0);
        this.trader.setTradingPlayer(null);
        if (!this.trader.isClientSide()) {
            if (!player0.m_6084_() || player0 instanceof ServerPlayer && ((ServerPlayer) player0).hasDisconnected()) {
                ItemStack $$1 = this.tradeContainer.removeItemNoUpdate(0);
                if (!$$1.isEmpty()) {
                    player0.drop($$1, false);
                }
                $$1 = this.tradeContainer.removeItemNoUpdate(1);
                if (!$$1.isEmpty()) {
                    player0.drop($$1, false);
                }
            } else if (player0 instanceof ServerPlayer) {
                player0.getInventory().placeItemBackInInventory(this.tradeContainer.removeItemNoUpdate(0));
                player0.getInventory().placeItemBackInInventory(this.tradeContainer.removeItemNoUpdate(1));
            }
        }
    }

    public void tryMoveItems(int int0) {
        if (int0 >= 0 && this.getOffers().size() > int0) {
            ItemStack $$1 = this.tradeContainer.getItem(0);
            if (!$$1.isEmpty()) {
                if (!this.m_38903_($$1, 3, 39, true)) {
                    return;
                }
                this.tradeContainer.setItem(0, $$1);
            }
            ItemStack $$2 = this.tradeContainer.getItem(1);
            if (!$$2.isEmpty()) {
                if (!this.m_38903_($$2, 3, 39, true)) {
                    return;
                }
                this.tradeContainer.setItem(1, $$2);
            }
            if (this.tradeContainer.getItem(0).isEmpty() && this.tradeContainer.getItem(1).isEmpty()) {
                ItemStack $$3 = ((MerchantOffer) this.getOffers().get(int0)).getCostA();
                this.moveFromInventoryToPaymentSlot(0, $$3);
                ItemStack $$4 = ((MerchantOffer) this.getOffers().get(int0)).getCostB();
                this.moveFromInventoryToPaymentSlot(1, $$4);
            }
        }
    }

    private void moveFromInventoryToPaymentSlot(int int0, ItemStack itemStack1) {
        if (!itemStack1.isEmpty()) {
            for (int $$2 = 3; $$2 < 39; $$2++) {
                ItemStack $$3 = ((Slot) this.f_38839_.get($$2)).getItem();
                if (!$$3.isEmpty() && ItemStack.isSameItemSameTags(itemStack1, $$3)) {
                    ItemStack $$4 = this.tradeContainer.getItem(int0);
                    int $$5 = $$4.isEmpty() ? 0 : $$4.getCount();
                    int $$6 = Math.min(itemStack1.getMaxStackSize() - $$5, $$3.getCount());
                    ItemStack $$7 = $$3.copy();
                    int $$8 = $$5 + $$6;
                    $$3.shrink($$6);
                    $$7.setCount($$8);
                    this.tradeContainer.setItem(int0, $$7);
                    if ($$8 >= itemStack1.getMaxStackSize()) {
                        break;
                    }
                }
            }
        }
    }

    public void setOffers(MerchantOffers merchantOffers0) {
        this.trader.overrideOffers(merchantOffers0);
    }

    public MerchantOffers getOffers() {
        return this.trader.getOffers();
    }

    public boolean showProgressBar() {
        return this.showProgressBar;
    }
}