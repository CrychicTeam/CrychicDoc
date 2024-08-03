package net.minecraft.world.inventory;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class GrindstoneMenu extends AbstractContainerMenu {

    public static final int MAX_NAME_LENGTH = 35;

    public static final int INPUT_SLOT = 0;

    public static final int ADDITIONAL_SLOT = 1;

    public static final int RESULT_SLOT = 2;

    private static final int INV_SLOT_START = 3;

    private static final int INV_SLOT_END = 30;

    private static final int USE_ROW_SLOT_START = 30;

    private static final int USE_ROW_SLOT_END = 39;

    private final Container resultSlots = new ResultContainer();

    final Container repairSlots = new SimpleContainer(2) {

        @Override
        public void setChanged() {
            super.setChanged();
            GrindstoneMenu.this.slotsChanged(this);
        }
    };

    private final ContainerLevelAccess access;

    public GrindstoneMenu(int int0, Inventory inventory1) {
        this(int0, inventory1, ContainerLevelAccess.NULL);
    }

    public GrindstoneMenu(int int0, Inventory inventory1, final ContainerLevelAccess containerLevelAccess2) {
        super(MenuType.GRINDSTONE, int0);
        this.access = containerLevelAccess2;
        this.m_38897_(new Slot(this.repairSlots, 0, 49, 19) {

            @Override
            public boolean mayPlace(ItemStack p_39607_) {
                return p_39607_.isDamageableItem() || p_39607_.is(Items.ENCHANTED_BOOK) || p_39607_.isEnchanted();
            }
        });
        this.m_38897_(new Slot(this.repairSlots, 1, 49, 40) {

            @Override
            public boolean mayPlace(ItemStack p_39616_) {
                return p_39616_.isDamageableItem() || p_39616_.is(Items.ENCHANTED_BOOK) || p_39616_.isEnchanted();
            }
        });
        this.m_38897_(new Slot(this.resultSlots, 2, 129, 34) {

            @Override
            public boolean mayPlace(ItemStack p_39630_) {
                return false;
            }

            @Override
            public void onTake(Player p_150574_, ItemStack p_150575_) {
                containerLevelAccess2.execute((p_39634_, p_39635_) -> {
                    if (p_39634_ instanceof ServerLevel) {
                        ExperienceOrb.award((ServerLevel) p_39634_, Vec3.atCenterOf(p_39635_), this.getExperienceAmount(p_39634_));
                    }
                    p_39634_.m_46796_(1042, p_39635_, 0);
                });
                GrindstoneMenu.this.repairSlots.setItem(0, ItemStack.EMPTY);
                GrindstoneMenu.this.repairSlots.setItem(1, ItemStack.EMPTY);
            }

            private int getExperienceAmount(Level p_39632_) {
                int $$1 = 0;
                $$1 += this.getExperienceFromItem(GrindstoneMenu.this.repairSlots.getItem(0));
                $$1 += this.getExperienceFromItem(GrindstoneMenu.this.repairSlots.getItem(1));
                if ($$1 > 0) {
                    int $$2 = (int) Math.ceil((double) $$1 / 2.0);
                    return $$2 + p_39632_.random.nextInt($$2);
                } else {
                    return 0;
                }
            }

            private int getExperienceFromItem(ItemStack p_39637_) {
                int $$1 = 0;
                Map<Enchantment, Integer> $$2 = EnchantmentHelper.getEnchantments(p_39637_);
                for (Entry<Enchantment, Integer> $$3 : $$2.entrySet()) {
                    Enchantment $$4 = (Enchantment) $$3.getKey();
                    Integer $$5 = (Integer) $$3.getValue();
                    if (!$$4.isCurse()) {
                        $$1 += $$4.getMinCost($$5);
                    }
                }
                return $$1;
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
    public void slotsChanged(Container container0) {
        super.slotsChanged(container0);
        if (container0 == this.repairSlots) {
            this.createResult();
        }
    }

    private void createResult() {
        ItemStack $$0 = this.repairSlots.getItem(0);
        ItemStack $$1 = this.repairSlots.getItem(1);
        boolean $$2 = !$$0.isEmpty() || !$$1.isEmpty();
        boolean $$3 = !$$0.isEmpty() && !$$1.isEmpty();
        if (!$$2) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
        } else {
            boolean $$4 = !$$0.isEmpty() && !$$0.is(Items.ENCHANTED_BOOK) && !$$0.isEnchanted() || !$$1.isEmpty() && !$$1.is(Items.ENCHANTED_BOOK) && !$$1.isEnchanted();
            if ($$0.getCount() > 1 || $$1.getCount() > 1 || !$$3 && $$4) {
                this.resultSlots.setItem(0, ItemStack.EMPTY);
                this.m_38946_();
                return;
            }
            int $$5 = 1;
            int $$10;
            ItemStack $$11;
            if ($$3) {
                if (!$$0.is($$1.getItem())) {
                    this.resultSlots.setItem(0, ItemStack.EMPTY);
                    this.m_38946_();
                    return;
                }
                Item $$6 = $$0.getItem();
                int $$7 = $$6.getMaxDamage() - $$0.getDamageValue();
                int $$8 = $$6.getMaxDamage() - $$1.getDamageValue();
                int $$9 = $$7 + $$8 + $$6.getMaxDamage() * 5 / 100;
                $$10 = Math.max($$6.getMaxDamage() - $$9, 0);
                $$11 = this.mergeEnchants($$0, $$1);
                if (!$$11.isDamageableItem()) {
                    if (!ItemStack.matches($$0, $$1)) {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.m_38946_();
                        return;
                    }
                    $$5 = 2;
                }
            } else {
                boolean $$12 = !$$0.isEmpty();
                $$10 = $$12 ? $$0.getDamageValue() : $$1.getDamageValue();
                $$11 = $$12 ? $$0 : $$1;
            }
            this.resultSlots.setItem(0, this.removeNonCurses($$11, $$10, $$5));
        }
        this.m_38946_();
    }

    private ItemStack mergeEnchants(ItemStack itemStack0, ItemStack itemStack1) {
        ItemStack $$2 = itemStack0.copy();
        Map<Enchantment, Integer> $$3 = EnchantmentHelper.getEnchantments(itemStack1);
        for (Entry<Enchantment, Integer> $$4 : $$3.entrySet()) {
            Enchantment $$5 = (Enchantment) $$4.getKey();
            if (!$$5.isCurse() || EnchantmentHelper.getItemEnchantmentLevel($$5, $$2) == 0) {
                $$2.enchant($$5, (Integer) $$4.getValue());
            }
        }
        return $$2;
    }

    private ItemStack removeNonCurses(ItemStack itemStack0, int int1, int int2) {
        ItemStack $$3 = itemStack0.copyWithCount(int2);
        $$3.removeTagKey("Enchantments");
        $$3.removeTagKey("StoredEnchantments");
        if (int1 > 0) {
            $$3.setDamageValue(int1);
        } else {
            $$3.removeTagKey("Damage");
        }
        Map<Enchantment, Integer> $$4 = (Map<Enchantment, Integer>) EnchantmentHelper.getEnchantments(itemStack0).entrySet().stream().filter(p_39584_ -> ((Enchantment) p_39584_.getKey()).isCurse()).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        EnchantmentHelper.setEnchantments($$4, $$3);
        $$3.setRepairCost(0);
        if ($$3.is(Items.ENCHANTED_BOOK) && $$4.size() == 0) {
            $$3 = new ItemStack(Items.BOOK);
            if (itemStack0.hasCustomHoverName()) {
                $$3.setHoverName(itemStack0.getHoverName());
            }
        }
        for (int $$5 = 0; $$5 < $$4.size(); $$5++) {
            $$3.setRepairCost(AnvilMenu.calculateIncreasedRepairCost($$3.getBaseRepairCost()));
        }
        return $$3;
    }

    @Override
    public void removed(Player player0) {
        super.removed(player0);
        this.access.execute((p_39575_, p_39576_) -> this.m_150411_(player0, this.repairSlots));
    }

    @Override
    public boolean stillValid(Player player0) {
        return m_38889_(this.access, player0, Blocks.GRINDSTONE);
    }

    @Override
    public ItemStack quickMoveStack(Player player0, int int1) {
        ItemStack $$2 = ItemStack.EMPTY;
        Slot $$3 = (Slot) this.f_38839_.get(int1);
        if ($$3 != null && $$3.hasItem()) {
            ItemStack $$4 = $$3.getItem();
            $$2 = $$4.copy();
            ItemStack $$5 = this.repairSlots.getItem(0);
            ItemStack $$6 = this.repairSlots.getItem(1);
            if (int1 == 2) {
                if (!this.m_38903_($$4, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                $$3.onQuickCraft($$4, $$2);
            } else if (int1 != 0 && int1 != 1) {
                if (!$$5.isEmpty() && !$$6.isEmpty()) {
                    if (int1 >= 3 && int1 < 30) {
                        if (!this.m_38903_($$4, 30, 39, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (int1 >= 30 && int1 < 39 && !this.m_38903_($$4, 3, 30, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.m_38903_($$4, 0, 2, false)) {
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
}