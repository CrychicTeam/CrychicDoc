package net.minecraft.world.inventory;

import java.util.List;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EnchantmentTableBlock;

public class EnchantmentMenu extends AbstractContainerMenu {

    private final Container enchantSlots = new SimpleContainer(2) {

        @Override
        public void setChanged() {
            super.setChanged();
            EnchantmentMenu.this.slotsChanged(this);
        }
    };

    private final ContainerLevelAccess access;

    private final RandomSource random = RandomSource.create();

    private final DataSlot enchantmentSeed = DataSlot.standalone();

    public final int[] costs = new int[3];

    public final int[] enchantClue = new int[] { -1, -1, -1 };

    public final int[] levelClue = new int[] { -1, -1, -1 };

    public EnchantmentMenu(int int0, Inventory inventory1) {
        this(int0, inventory1, ContainerLevelAccess.NULL);
    }

    public EnchantmentMenu(int int0, Inventory inventory1, ContainerLevelAccess containerLevelAccess2) {
        super(MenuType.ENCHANTMENT, int0);
        this.access = containerLevelAccess2;
        this.m_38897_(new Slot(this.enchantSlots, 0, 15, 47) {

            @Override
            public boolean mayPlace(ItemStack p_39508_) {
                return true;
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        this.m_38897_(new Slot(this.enchantSlots, 1, 35, 47) {

            @Override
            public boolean mayPlace(ItemStack p_39517_) {
                return p_39517_.is(Items.LAPIS_LAZULI);
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
        this.m_38895_(DataSlot.shared(this.costs, 0));
        this.m_38895_(DataSlot.shared(this.costs, 1));
        this.m_38895_(DataSlot.shared(this.costs, 2));
        this.m_38895_(this.enchantmentSeed).set(inventory1.player.getEnchantmentSeed());
        this.m_38895_(DataSlot.shared(this.enchantClue, 0));
        this.m_38895_(DataSlot.shared(this.enchantClue, 1));
        this.m_38895_(DataSlot.shared(this.enchantClue, 2));
        this.m_38895_(DataSlot.shared(this.levelClue, 0));
        this.m_38895_(DataSlot.shared(this.levelClue, 1));
        this.m_38895_(DataSlot.shared(this.levelClue, 2));
    }

    @Override
    public void slotsChanged(Container container0) {
        if (container0 == this.enchantSlots) {
            ItemStack $$1 = container0.getItem(0);
            if (!$$1.isEmpty() && $$1.isEnchantable()) {
                this.access.execute((p_39485_, p_39486_) -> {
                    int $$3 = 0;
                    for (BlockPos $$4 : EnchantmentTableBlock.BOOKSHELF_OFFSETS) {
                        if (EnchantmentTableBlock.isValidBookShelf(p_39485_, p_39486_, $$4)) {
                            $$3++;
                        }
                    }
                    this.random.setSeed((long) this.enchantmentSeed.get());
                    for (int $$5 = 0; $$5 < 3; $$5++) {
                        this.costs[$$5] = EnchantmentHelper.getEnchantmentCost(this.random, $$5, $$3, $$1);
                        this.enchantClue[$$5] = -1;
                        this.levelClue[$$5] = -1;
                        if (this.costs[$$5] < $$5 + 1) {
                            this.costs[$$5] = 0;
                        }
                    }
                    for (int $$6 = 0; $$6 < 3; $$6++) {
                        if (this.costs[$$6] > 0) {
                            List<EnchantmentInstance> $$7 = this.getEnchantmentList($$1, $$6, this.costs[$$6]);
                            if ($$7 != null && !$$7.isEmpty()) {
                                EnchantmentInstance $$8 = (EnchantmentInstance) $$7.get(this.random.nextInt($$7.size()));
                                this.enchantClue[$$6] = BuiltInRegistries.ENCHANTMENT.getId($$8.enchantment);
                                this.levelClue[$$6] = $$8.level;
                            }
                        }
                    }
                    this.m_38946_();
                });
            } else {
                for (int $$2 = 0; $$2 < 3; $$2++) {
                    this.costs[$$2] = 0;
                    this.enchantClue[$$2] = -1;
                    this.levelClue[$$2] = -1;
                }
            }
        }
    }

    @Override
    public boolean clickMenuButton(Player player0, int int1) {
        if (int1 >= 0 && int1 < this.costs.length) {
            ItemStack $$2 = this.enchantSlots.getItem(0);
            ItemStack $$3 = this.enchantSlots.getItem(1);
            int $$4 = int1 + 1;
            if (($$3.isEmpty() || $$3.getCount() < $$4) && !player0.getAbilities().instabuild) {
                return false;
            } else if (this.costs[int1] <= 0 || $$2.isEmpty() || (player0.experienceLevel < $$4 || player0.experienceLevel < this.costs[int1]) && !player0.getAbilities().instabuild) {
                return false;
            } else {
                this.access.execute((p_39481_, p_39482_) -> {
                    ItemStack $$7 = $$2;
                    List<EnchantmentInstance> $$8 = this.getEnchantmentList($$2, int1, this.costs[int1]);
                    if (!$$8.isEmpty()) {
                        player0.onEnchantmentPerformed($$2, $$4);
                        boolean $$9 = $$2.is(Items.BOOK);
                        if ($$9) {
                            $$7 = new ItemStack(Items.ENCHANTED_BOOK);
                            CompoundTag $$10 = $$2.getTag();
                            if ($$10 != null) {
                                $$7.setTag($$10.copy());
                            }
                            this.enchantSlots.setItem(0, $$7);
                        }
                        for (int $$11 = 0; $$11 < $$8.size(); $$11++) {
                            EnchantmentInstance $$12 = (EnchantmentInstance) $$8.get($$11);
                            if ($$9) {
                                EnchantedBookItem.addEnchantment($$7, $$12);
                            } else {
                                $$7.enchant($$12.enchantment, $$12.level);
                            }
                        }
                        if (!player0.getAbilities().instabuild) {
                            $$3.shrink($$4);
                            if ($$3.isEmpty()) {
                                this.enchantSlots.setItem(1, ItemStack.EMPTY);
                            }
                        }
                        player0.awardStat(Stats.ENCHANT_ITEM);
                        if (player0 instanceof ServerPlayer) {
                            CriteriaTriggers.ENCHANTED_ITEM.trigger((ServerPlayer) player0, $$7, $$4);
                        }
                        this.enchantSlots.setChanged();
                        this.enchantmentSeed.set(player0.getEnchantmentSeed());
                        this.slotsChanged(this.enchantSlots);
                        p_39481_.playSound(null, p_39482_, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, p_39481_.random.nextFloat() * 0.1F + 0.9F);
                    }
                });
                return true;
            }
        } else {
            Util.logAndPauseIfInIde(player0.getName() + " pressed invalid button id: " + int1);
            return false;
        }
    }

    private List<EnchantmentInstance> getEnchantmentList(ItemStack itemStack0, int int1, int int2) {
        this.random.setSeed((long) (this.enchantmentSeed.get() + int1));
        List<EnchantmentInstance> $$3 = EnchantmentHelper.selectEnchantment(this.random, itemStack0, int2, false);
        if (itemStack0.is(Items.BOOK) && $$3.size() > 1) {
            $$3.remove(this.random.nextInt($$3.size()));
        }
        return $$3;
    }

    public int getGoldCount() {
        ItemStack $$0 = this.enchantSlots.getItem(1);
        return $$0.isEmpty() ? 0 : $$0.getCount();
    }

    public int getEnchantmentSeed() {
        return this.enchantmentSeed.get();
    }

    @Override
    public void removed(Player player0) {
        super.removed(player0);
        this.access.execute((p_39469_, p_39470_) -> this.m_150411_(player0, this.enchantSlots));
    }

    @Override
    public boolean stillValid(Player player0) {
        return m_38889_(this.access, player0, Blocks.ENCHANTING_TABLE);
    }

    @Override
    public ItemStack quickMoveStack(Player player0, int int1) {
        ItemStack $$2 = ItemStack.EMPTY;
        Slot $$3 = (Slot) this.f_38839_.get(int1);
        if ($$3 != null && $$3.hasItem()) {
            ItemStack $$4 = $$3.getItem();
            $$2 = $$4.copy();
            if (int1 == 0) {
                if (!this.m_38903_($$4, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (int1 == 1) {
                if (!this.m_38903_($$4, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if ($$4.is(Items.LAPIS_LAZULI)) {
                if (!this.m_38903_($$4, 1, 2, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (((Slot) this.f_38839_.get(0)).hasItem() || !((Slot) this.f_38839_.get(0)).mayPlace($$4)) {
                    return ItemStack.EMPTY;
                }
                ItemStack $$5 = $$4.copyWithCount(1);
                $$4.shrink(1);
                ((Slot) this.f_38839_.get(0)).setByPlayer($$5);
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
}