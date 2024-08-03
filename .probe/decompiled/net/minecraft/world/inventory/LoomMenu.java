package net.minecraft.world.inventory;

import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BannerPatternTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class LoomMenu extends AbstractContainerMenu {

    private static final int PATTERN_NOT_SET = -1;

    private static final int INV_SLOT_START = 4;

    private static final int INV_SLOT_END = 31;

    private static final int USE_ROW_SLOT_START = 31;

    private static final int USE_ROW_SLOT_END = 40;

    private final ContainerLevelAccess access;

    final DataSlot selectedBannerPatternIndex = DataSlot.standalone();

    private List<Holder<BannerPattern>> selectablePatterns = List.of();

    Runnable slotUpdateListener = () -> {
    };

    final Slot bannerSlot;

    final Slot dyeSlot;

    private final Slot patternSlot;

    private final Slot resultSlot;

    long lastSoundTime;

    private final Container inputContainer = new SimpleContainer(3) {

        @Override
        public void setChanged() {
            super.setChanged();
            LoomMenu.this.slotsChanged(this);
            LoomMenu.this.slotUpdateListener.run();
        }
    };

    private final Container outputContainer = new SimpleContainer(1) {

        @Override
        public void setChanged() {
            super.setChanged();
            LoomMenu.this.slotUpdateListener.run();
        }
    };

    public LoomMenu(int int0, Inventory inventory1) {
        this(int0, inventory1, ContainerLevelAccess.NULL);
    }

    public LoomMenu(int int0, Inventory inventory1, final ContainerLevelAccess containerLevelAccess2) {
        super(MenuType.LOOM, int0);
        this.access = containerLevelAccess2;
        this.bannerSlot = this.m_38897_(new Slot(this.inputContainer, 0, 13, 26) {

            @Override
            public boolean mayPlace(ItemStack p_39918_) {
                return p_39918_.getItem() instanceof BannerItem;
            }
        });
        this.dyeSlot = this.m_38897_(new Slot(this.inputContainer, 1, 33, 26) {

            @Override
            public boolean mayPlace(ItemStack p_39927_) {
                return p_39927_.getItem() instanceof DyeItem;
            }
        });
        this.patternSlot = this.m_38897_(new Slot(this.inputContainer, 2, 23, 45) {

            @Override
            public boolean mayPlace(ItemStack p_39936_) {
                return p_39936_.getItem() instanceof BannerPatternItem;
            }
        });
        this.resultSlot = this.m_38897_(new Slot(this.outputContainer, 0, 143, 58) {

            @Override
            public boolean mayPlace(ItemStack p_39950_) {
                return false;
            }

            @Override
            public void onTake(Player p_150617_, ItemStack p_150618_) {
                LoomMenu.this.bannerSlot.remove(1);
                LoomMenu.this.dyeSlot.remove(1);
                if (!LoomMenu.this.bannerSlot.hasItem() || !LoomMenu.this.dyeSlot.hasItem()) {
                    LoomMenu.this.selectedBannerPatternIndex.set(-1);
                }
                containerLevelAccess2.execute((p_39952_, p_39953_) -> {
                    long $$2 = p_39952_.getGameTime();
                    if (LoomMenu.this.lastSoundTime != $$2) {
                        p_39952_.playSound(null, p_39953_, SoundEvents.UI_LOOM_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
                        LoomMenu.this.lastSoundTime = $$2;
                    }
                });
                super.onTake(p_150617_, p_150618_);
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
        this.m_38895_(this.selectedBannerPatternIndex);
    }

    @Override
    public boolean stillValid(Player player0) {
        return m_38889_(this.access, player0, Blocks.LOOM);
    }

    @Override
    public boolean clickMenuButton(Player player0, int int1) {
        if (int1 >= 0 && int1 < this.selectablePatterns.size()) {
            this.selectedBannerPatternIndex.set(int1);
            this.setupResultSlot((Holder<BannerPattern>) this.selectablePatterns.get(int1));
            return true;
        } else {
            return false;
        }
    }

    private List<Holder<BannerPattern>> getSelectablePatterns(ItemStack itemStack0) {
        if (itemStack0.isEmpty()) {
            return (List<Holder<BannerPattern>>) BuiltInRegistries.BANNER_PATTERN.getTag(BannerPatternTags.NO_ITEM_REQUIRED).map(ImmutableList::copyOf).orElse(ImmutableList.of());
        } else {
            return itemStack0.getItem() instanceof BannerPatternItem $$1 ? (List) BuiltInRegistries.BANNER_PATTERN.getTag($$1.getBannerPattern()).map(ImmutableList::copyOf).orElse(ImmutableList.of()) : List.of();
        }
    }

    private boolean isValidPatternIndex(int int0) {
        return int0 >= 0 && int0 < this.selectablePatterns.size();
    }

    @Override
    public void slotsChanged(Container container0) {
        ItemStack $$1 = this.bannerSlot.getItem();
        ItemStack $$2 = this.dyeSlot.getItem();
        ItemStack $$3 = this.patternSlot.getItem();
        if (!$$1.isEmpty() && !$$2.isEmpty()) {
            int $$4 = this.selectedBannerPatternIndex.get();
            boolean $$5 = this.isValidPatternIndex($$4);
            List<Holder<BannerPattern>> $$6 = this.selectablePatterns;
            this.selectablePatterns = this.getSelectablePatterns($$3);
            Holder<BannerPattern> $$7;
            if (this.selectablePatterns.size() == 1) {
                this.selectedBannerPatternIndex.set(0);
                $$7 = (Holder<BannerPattern>) this.selectablePatterns.get(0);
            } else if (!$$5) {
                this.selectedBannerPatternIndex.set(-1);
                $$7 = null;
            } else {
                Holder<BannerPattern> $$9 = (Holder<BannerPattern>) $$6.get($$4);
                int $$10 = this.selectablePatterns.indexOf($$9);
                if ($$10 != -1) {
                    $$7 = $$9;
                    this.selectedBannerPatternIndex.set($$10);
                } else {
                    $$7 = null;
                    this.selectedBannerPatternIndex.set(-1);
                }
            }
            if ($$7 != null) {
                CompoundTag $$13 = BlockItem.getBlockEntityData($$1);
                boolean $$14 = $$13 != null && $$13.contains("Patterns", 9) && !$$1.isEmpty() && $$13.getList("Patterns", 10).size() >= 6;
                if ($$14) {
                    this.selectedBannerPatternIndex.set(-1);
                    this.resultSlot.set(ItemStack.EMPTY);
                } else {
                    this.setupResultSlot($$7);
                }
            } else {
                this.resultSlot.set(ItemStack.EMPTY);
            }
            this.m_38946_();
        } else {
            this.resultSlot.set(ItemStack.EMPTY);
            this.selectablePatterns = List.of();
            this.selectedBannerPatternIndex.set(-1);
        }
    }

    public List<Holder<BannerPattern>> getSelectablePatterns() {
        return this.selectablePatterns;
    }

    public int getSelectedBannerPatternIndex() {
        return this.selectedBannerPatternIndex.get();
    }

    public void registerUpdateListener(Runnable runnable0) {
        this.slotUpdateListener = runnable0;
    }

    @Override
    public ItemStack quickMoveStack(Player player0, int int1) {
        ItemStack $$2 = ItemStack.EMPTY;
        Slot $$3 = (Slot) this.f_38839_.get(int1);
        if ($$3 != null && $$3.hasItem()) {
            ItemStack $$4 = $$3.getItem();
            $$2 = $$4.copy();
            if (int1 == this.resultSlot.index) {
                if (!this.m_38903_($$4, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }
                $$3.onQuickCraft($$4, $$2);
            } else if (int1 != this.dyeSlot.index && int1 != this.bannerSlot.index && int1 != this.patternSlot.index) {
                if ($$4.getItem() instanceof BannerItem) {
                    if (!this.m_38903_($$4, this.bannerSlot.index, this.bannerSlot.index + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if ($$4.getItem() instanceof DyeItem) {
                    if (!this.m_38903_($$4, this.dyeSlot.index, this.dyeSlot.index + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if ($$4.getItem() instanceof BannerPatternItem) {
                    if (!this.m_38903_($$4, this.patternSlot.index, this.patternSlot.index + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (int1 >= 4 && int1 < 31) {
                    if (!this.m_38903_($$4, 31, 40, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (int1 >= 31 && int1 < 40 && !this.m_38903_($$4, 4, 31, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_($$4, 4, 40, false)) {
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

    @Override
    public void removed(Player player0) {
        super.removed(player0);
        this.access.execute((p_39871_, p_39872_) -> this.m_150411_(player0, this.inputContainer));
    }

    private void setupResultSlot(Holder<BannerPattern> holderBannerPattern0) {
        ItemStack $$1 = this.bannerSlot.getItem();
        ItemStack $$2 = this.dyeSlot.getItem();
        ItemStack $$3 = ItemStack.EMPTY;
        if (!$$1.isEmpty() && !$$2.isEmpty()) {
            $$3 = $$1.copyWithCount(1);
            DyeColor $$4 = ((DyeItem) $$2.getItem()).getDyeColor();
            CompoundTag $$5 = BlockItem.getBlockEntityData($$3);
            ListTag $$6;
            if ($$5 != null && $$5.contains("Patterns", 9)) {
                $$6 = $$5.getList("Patterns", 10);
            } else {
                $$6 = new ListTag();
                if ($$5 == null) {
                    $$5 = new CompoundTag();
                }
                $$5.put("Patterns", $$6);
            }
            CompoundTag $$8 = new CompoundTag();
            $$8.putString("Pattern", holderBannerPattern0.value().getHashname());
            $$8.putInt("Color", $$4.getId());
            $$6.add($$8);
            BlockItem.setBlockEntityData($$3, BlockEntityType.BANNER, $$5);
        }
        if (!ItemStack.matches($$3, this.resultSlot.getItem())) {
            this.resultSlot.set($$3);
        }
    }

    public Slot getBannerSlot() {
        return this.bannerSlot;
    }

    public Slot getDyeSlot() {
        return this.dyeSlot;
    }

    public Slot getPatternSlot() {
        return this.patternSlot;
    }

    public Slot getResultSlot() {
        return this.resultSlot;
    }
}