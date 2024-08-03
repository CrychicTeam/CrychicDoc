package net.minecraft.world.inventory;

import com.mojang.logging.LogUtils;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

public class AnvilMenu extends ItemCombinerMenu {

    public static final int INPUT_SLOT = 0;

    public static final int ADDITIONAL_SLOT = 1;

    public static final int RESULT_SLOT = 2;

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final boolean DEBUG_COST = false;

    public static final int MAX_NAME_LENGTH = 50;

    private int repairItemCountCost;

    @Nullable
    private String itemName;

    private final DataSlot cost = DataSlot.standalone();

    private static final int COST_FAIL = 0;

    private static final int COST_BASE = 1;

    private static final int COST_ADDED_BASE = 1;

    private static final int COST_REPAIR_MATERIAL = 1;

    private static final int COST_REPAIR_SACRIFICE = 2;

    private static final int COST_INCOMPATIBLE_PENALTY = 1;

    private static final int COST_RENAME = 1;

    private static final int INPUT_SLOT_X_PLACEMENT = 27;

    private static final int ADDITIONAL_SLOT_X_PLACEMENT = 76;

    private static final int RESULT_SLOT_X_PLACEMENT = 134;

    private static final int SLOT_Y_PLACEMENT = 47;

    public AnvilMenu(int int0, Inventory inventory1) {
        this(int0, inventory1, ContainerLevelAccess.NULL);
    }

    public AnvilMenu(int int0, Inventory inventory1, ContainerLevelAccess containerLevelAccess2) {
        super(MenuType.ANVIL, int0, inventory1, containerLevelAccess2);
        this.m_38895_(this.cost);
    }

    @Override
    protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create().withSlot(0, 27, 47, p_266635_ -> true).withSlot(1, 76, 47, p_266634_ -> true).withResultSlot(2, 134, 47).build();
    }

    @Override
    protected boolean isValidBlock(BlockState blockState0) {
        return blockState0.m_204336_(BlockTags.ANVIL);
    }

    @Override
    protected boolean mayPickup(Player player0, boolean boolean1) {
        return (player0.getAbilities().instabuild || player0.experienceLevel >= this.cost.get()) && this.cost.get() > 0;
    }

    @Override
    protected void onTake(Player player0, ItemStack itemStack1) {
        if (!player0.getAbilities().instabuild) {
            player0.giveExperienceLevels(-this.cost.get());
        }
        this.f_39769_.setItem(0, ItemStack.EMPTY);
        if (this.repairItemCountCost > 0) {
            ItemStack $$2 = this.f_39769_.getItem(1);
            if (!$$2.isEmpty() && $$2.getCount() > this.repairItemCountCost) {
                $$2.shrink(this.repairItemCountCost);
                this.f_39769_.setItem(1, $$2);
            } else {
                this.f_39769_.setItem(1, ItemStack.EMPTY);
            }
        } else {
            this.f_39769_.setItem(1, ItemStack.EMPTY);
        }
        this.cost.set(0);
        this.f_39770_.execute((p_150479_, p_150480_) -> {
            BlockState $$3 = p_150479_.getBlockState(p_150480_);
            if (!player0.getAbilities().instabuild && $$3.m_204336_(BlockTags.ANVIL) && player0.m_217043_().nextFloat() < 0.12F) {
                BlockState $$4 = AnvilBlock.damage($$3);
                if ($$4 == null) {
                    p_150479_.removeBlock(p_150480_, false);
                    p_150479_.m_46796_(1029, p_150480_, 0);
                } else {
                    p_150479_.setBlock(p_150480_, $$4, 2);
                    p_150479_.m_46796_(1030, p_150480_, 0);
                }
            } else {
                p_150479_.m_46796_(1030, p_150480_, 0);
            }
        });
    }

    @Override
    public void createResult() {
        ItemStack $$0 = this.f_39769_.getItem(0);
        this.cost.set(1);
        int $$1 = 0;
        int $$2 = 0;
        int $$3 = 0;
        if ($$0.isEmpty()) {
            this.f_39768_.setItem(0, ItemStack.EMPTY);
            this.cost.set(0);
        } else {
            ItemStack $$4 = $$0.copy();
            ItemStack $$5 = this.f_39769_.getItem(1);
            Map<Enchantment, Integer> $$6 = EnchantmentHelper.getEnchantments($$4);
            $$2 += $$0.getBaseRepairCost() + ($$5.isEmpty() ? 0 : $$5.getBaseRepairCost());
            this.repairItemCountCost = 0;
            if (!$$5.isEmpty()) {
                boolean $$7 = $$5.is(Items.ENCHANTED_BOOK) && !EnchantedBookItem.getEnchantments($$5).isEmpty();
                if ($$4.isDamageableItem() && $$4.getItem().isValidRepairItem($$0, $$5)) {
                    int $$8 = Math.min($$4.getDamageValue(), $$4.getMaxDamage() / 4);
                    if ($$8 <= 0) {
                        this.f_39768_.setItem(0, ItemStack.EMPTY);
                        this.cost.set(0);
                        return;
                    }
                    int $$9;
                    for ($$9 = 0; $$8 > 0 && $$9 < $$5.getCount(); $$9++) {
                        int $$10 = $$4.getDamageValue() - $$8;
                        $$4.setDamageValue($$10);
                        $$1++;
                        $$8 = Math.min($$4.getDamageValue(), $$4.getMaxDamage() / 4);
                    }
                    this.repairItemCountCost = $$9;
                } else {
                    if (!$$7 && (!$$4.is($$5.getItem()) || !$$4.isDamageableItem())) {
                        this.f_39768_.setItem(0, ItemStack.EMPTY);
                        this.cost.set(0);
                        return;
                    }
                    if ($$4.isDamageableItem() && !$$7) {
                        int $$11 = $$0.getMaxDamage() - $$0.getDamageValue();
                        int $$12 = $$5.getMaxDamage() - $$5.getDamageValue();
                        int $$13 = $$12 + $$4.getMaxDamage() * 12 / 100;
                        int $$14 = $$11 + $$13;
                        int $$15 = $$4.getMaxDamage() - $$14;
                        if ($$15 < 0) {
                            $$15 = 0;
                        }
                        if ($$15 < $$4.getDamageValue()) {
                            $$4.setDamageValue($$15);
                            $$1 += 2;
                        }
                    }
                    Map<Enchantment, Integer> $$16 = EnchantmentHelper.getEnchantments($$5);
                    boolean $$17 = false;
                    boolean $$18 = false;
                    for (Enchantment $$19 : $$16.keySet()) {
                        if ($$19 != null) {
                            int $$20 = (Integer) $$6.getOrDefault($$19, 0);
                            int $$21 = (Integer) $$16.get($$19);
                            $$21 = $$20 == $$21 ? $$21 + 1 : Math.max($$21, $$20);
                            boolean $$22 = $$19.canEnchant($$0);
                            if (this.f_39771_.getAbilities().instabuild || $$0.is(Items.ENCHANTED_BOOK)) {
                                $$22 = true;
                            }
                            for (Enchantment $$23 : $$6.keySet()) {
                                if ($$23 != $$19 && !$$19.isCompatibleWith($$23)) {
                                    $$22 = false;
                                    $$1++;
                                }
                            }
                            if (!$$22) {
                                $$18 = true;
                            } else {
                                $$17 = true;
                                if ($$21 > $$19.getMaxLevel()) {
                                    $$21 = $$19.getMaxLevel();
                                }
                                $$6.put($$19, $$21);
                                int $$24 = 0;
                                switch($$19.getRarity()) {
                                    case COMMON:
                                        $$24 = 1;
                                        break;
                                    case UNCOMMON:
                                        $$24 = 2;
                                        break;
                                    case RARE:
                                        $$24 = 4;
                                        break;
                                    case VERY_RARE:
                                        $$24 = 8;
                                }
                                if ($$7) {
                                    $$24 = Math.max(1, $$24 / 2);
                                }
                                $$1 += $$24 * $$21;
                                if ($$0.getCount() > 1) {
                                    $$1 = 40;
                                }
                            }
                        }
                    }
                    if ($$18 && !$$17) {
                        this.f_39768_.setItem(0, ItemStack.EMPTY);
                        this.cost.set(0);
                        return;
                    }
                }
            }
            if (this.itemName != null && !Util.isBlank(this.itemName)) {
                if (!this.itemName.equals($$0.getHoverName().getString())) {
                    $$3 = 1;
                    $$1 += $$3;
                    $$4.setHoverName(Component.literal(this.itemName));
                }
            } else if ($$0.hasCustomHoverName()) {
                $$3 = 1;
                $$1 += $$3;
                $$4.resetHoverName();
            }
            this.cost.set($$2 + $$1);
            if ($$1 <= 0) {
                $$4 = ItemStack.EMPTY;
            }
            if ($$3 == $$1 && $$3 > 0 && this.cost.get() >= 40) {
                this.cost.set(39);
            }
            if (this.cost.get() >= 40 && !this.f_39771_.getAbilities().instabuild) {
                $$4 = ItemStack.EMPTY;
            }
            if (!$$4.isEmpty()) {
                int $$25 = $$4.getBaseRepairCost();
                if (!$$5.isEmpty() && $$25 < $$5.getBaseRepairCost()) {
                    $$25 = $$5.getBaseRepairCost();
                }
                if ($$3 != $$1 || $$3 == 0) {
                    $$25 = calculateIncreasedRepairCost($$25);
                }
                $$4.setRepairCost($$25);
                EnchantmentHelper.setEnchantments($$6, $$4);
            }
            this.f_39768_.setItem(0, $$4);
            this.m_38946_();
        }
    }

    public static int calculateIncreasedRepairCost(int int0) {
        return int0 * 2 + 1;
    }

    public boolean setItemName(String string0) {
        String $$1 = validateName(string0);
        if ($$1 != null && !$$1.equals(this.itemName)) {
            this.itemName = $$1;
            if (this.m_38853_(2).hasItem()) {
                ItemStack $$2 = this.m_38853_(2).getItem();
                if (Util.isBlank($$1)) {
                    $$2.resetHoverName();
                } else {
                    $$2.setHoverName(Component.literal($$1));
                }
            }
            this.createResult();
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    private static String validateName(String string0) {
        String $$1 = SharedConstants.filterText(string0);
        return $$1.length() <= 50 ? $$1 : null;
    }

    public int getCost() {
        return this.cost.get();
    }
}