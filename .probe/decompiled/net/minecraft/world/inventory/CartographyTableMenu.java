package net.minecraft.world.inventory;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class CartographyTableMenu extends AbstractContainerMenu {

    public static final int MAP_SLOT = 0;

    public static final int ADDITIONAL_SLOT = 1;

    public static final int RESULT_SLOT = 2;

    private static final int INV_SLOT_START = 3;

    private static final int INV_SLOT_END = 30;

    private static final int USE_ROW_SLOT_START = 30;

    private static final int USE_ROW_SLOT_END = 39;

    private final ContainerLevelAccess access;

    long lastSoundTime;

    public final Container container = new SimpleContainer(2) {

        @Override
        public void setChanged() {
            CartographyTableMenu.this.slotsChanged(this);
            super.setChanged();
        }
    };

    private final ResultContainer resultContainer = new ResultContainer() {

        @Override
        public void setChanged() {
            CartographyTableMenu.this.slotsChanged(this);
            super.setChanged();
        }
    };

    public CartographyTableMenu(int int0, Inventory inventory1) {
        this(int0, inventory1, ContainerLevelAccess.NULL);
    }

    public CartographyTableMenu(int int0, Inventory inventory1, final ContainerLevelAccess containerLevelAccess2) {
        super(MenuType.CARTOGRAPHY_TABLE, int0);
        this.access = containerLevelAccess2;
        this.m_38897_(new Slot(this.container, 0, 15, 15) {

            @Override
            public boolean mayPlace(ItemStack p_39194_) {
                return p_39194_.is(Items.FILLED_MAP);
            }
        });
        this.m_38897_(new Slot(this.container, 1, 15, 52) {

            @Override
            public boolean mayPlace(ItemStack p_39203_) {
                return p_39203_.is(Items.PAPER) || p_39203_.is(Items.MAP) || p_39203_.is(Items.GLASS_PANE);
            }
        });
        this.m_38897_(new Slot(this.resultContainer, 2, 145, 39) {

            @Override
            public boolean mayPlace(ItemStack p_39217_) {
                return false;
            }

            @Override
            public void onTake(Player p_150509_, ItemStack p_150510_) {
                ((Slot) CartographyTableMenu.this.f_38839_.get(0)).remove(1);
                ((Slot) CartographyTableMenu.this.f_38839_.get(1)).remove(1);
                p_150510_.getItem().onCraftedBy(p_150510_, p_150509_.m_9236_(), p_150509_);
                containerLevelAccess2.execute((p_39219_, p_39220_) -> {
                    long $$2 = p_39219_.getGameTime();
                    if (CartographyTableMenu.this.lastSoundTime != $$2) {
                        p_39219_.playSound(null, p_39220_, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
                        CartographyTableMenu.this.lastSoundTime = $$2;
                    }
                });
                super.onTake(p_150509_, p_150510_);
            }
        });
        for (int $$3 = 0; $$3 < 3; $$3++) {
            for (int $$4 = 0; $$4 < 9; $$4++) {
                this.m_38897_(new Slot(inventory1, $$4 + $$3 * 9 + 9, 8 + $$4 * 18, 84 + $$3 * 18));
            }
        }
        for (int $$5 = 0; $$5 < 9; $$5++) {
            this.m_38897_(new Slot(inventory1, $$5, 8 + $$5 * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player0) {
        return m_38889_(this.access, player0, Blocks.CARTOGRAPHY_TABLE);
    }

    @Override
    public void slotsChanged(Container container0) {
        ItemStack $$1 = this.container.getItem(0);
        ItemStack $$2 = this.container.getItem(1);
        ItemStack $$3 = this.resultContainer.getItem(2);
        if ($$3.isEmpty() || !$$1.isEmpty() && !$$2.isEmpty()) {
            if (!$$1.isEmpty() && !$$2.isEmpty()) {
                this.setupResultSlot($$1, $$2, $$3);
            }
        } else {
            this.resultContainer.removeItemNoUpdate(2);
        }
    }

    private void setupResultSlot(ItemStack itemStack0, ItemStack itemStack1, ItemStack itemStack2) {
        this.access.execute((p_279039_, p_279040_) -> {
            MapItemSavedData $$5 = MapItem.getSavedData(itemStack0, p_279039_);
            if ($$5 != null) {
                ItemStack $$6;
                if (itemStack1.is(Items.PAPER) && !$$5.locked && $$5.scale < 4) {
                    $$6 = itemStack0.copyWithCount(1);
                    $$6.getOrCreateTag().putInt("map_scale_direction", 1);
                    this.m_38946_();
                } else if (itemStack1.is(Items.GLASS_PANE) && !$$5.locked) {
                    $$6 = itemStack0.copyWithCount(1);
                    $$6.getOrCreateTag().putBoolean("map_to_lock", true);
                    this.m_38946_();
                } else {
                    if (!itemStack1.is(Items.MAP)) {
                        this.resultContainer.removeItemNoUpdate(2);
                        this.m_38946_();
                        return;
                    }
                    $$6 = itemStack0.copyWithCount(2);
                    this.m_38946_();
                }
                if (!ItemStack.matches($$6, itemStack2)) {
                    this.resultContainer.setItem(2, $$6);
                    this.m_38946_();
                }
            }
        });
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack itemStack0, Slot slot1) {
        return slot1.container != this.resultContainer && super.canTakeItemForPickAll(itemStack0, slot1);
    }

    @Override
    public ItemStack quickMoveStack(Player player0, int int1) {
        ItemStack $$2 = ItemStack.EMPTY;
        Slot $$3 = (Slot) this.f_38839_.get(int1);
        if ($$3 != null && $$3.hasItem()) {
            ItemStack $$4 = $$3.getItem();
            $$2 = $$4.copy();
            if (int1 == 2) {
                $$4.getItem().onCraftedBy($$4, player0.m_9236_(), player0);
                if (!this.m_38903_($$4, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                $$3.onQuickCraft($$4, $$2);
            } else if (int1 != 1 && int1 != 0) {
                if ($$4.is(Items.FILLED_MAP)) {
                    if (!this.m_38903_($$4, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!$$4.is(Items.PAPER) && !$$4.is(Items.MAP) && !$$4.is(Items.GLASS_PANE)) {
                    if (int1 >= 3 && int1 < 30) {
                        if (!this.m_38903_($$4, 30, 39, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (int1 >= 30 && int1 < 39 && !this.m_38903_($$4, 3, 30, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.m_38903_($$4, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_($$4, 3, 39, false)) {
                return ItemStack.EMPTY;
            }
            if ($$4.isEmpty()) {
                $$3.setByPlayer(ItemStack.EMPTY);
            }
            $$3.setChanged();
            if ($$4.getCount() == $$2.getCount()) {
                return ItemStack.EMPTY;
            }
            $$3.onTake(player0, $$4);
            this.m_38946_();
        }
        return $$2;
    }

    @Override
    public void removed(Player player0) {
        super.removed(player0);
        this.resultContainer.removeItemNoUpdate(2);
        this.access.execute((p_39152_, p_39153_) -> this.m_150411_(player0, this.container));
    }
}