package net.minecraft.world.inventory;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class InventoryMenu extends RecipeBookMenu<CraftingContainer> {

    public static final int CONTAINER_ID = 0;

    public static final int RESULT_SLOT = 0;

    public static final int CRAFT_SLOT_START = 1;

    public static final int CRAFT_SLOT_END = 5;

    public static final int ARMOR_SLOT_START = 5;

    public static final int ARMOR_SLOT_END = 9;

    public static final int INV_SLOT_START = 9;

    public static final int INV_SLOT_END = 36;

    public static final int USE_ROW_SLOT_START = 36;

    public static final int USE_ROW_SLOT_END = 45;

    public static final int SHIELD_SLOT = 45;

    public static final ResourceLocation BLOCK_ATLAS = new ResourceLocation("textures/atlas/blocks.png");

    public static final ResourceLocation EMPTY_ARMOR_SLOT_HELMET = new ResourceLocation("item/empty_armor_slot_helmet");

    public static final ResourceLocation EMPTY_ARMOR_SLOT_CHESTPLATE = new ResourceLocation("item/empty_armor_slot_chestplate");

    public static final ResourceLocation EMPTY_ARMOR_SLOT_LEGGINGS = new ResourceLocation("item/empty_armor_slot_leggings");

    public static final ResourceLocation EMPTY_ARMOR_SLOT_BOOTS = new ResourceLocation("item/empty_armor_slot_boots");

    public static final ResourceLocation EMPTY_ARMOR_SLOT_SHIELD = new ResourceLocation("item/empty_armor_slot_shield");

    static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[] { EMPTY_ARMOR_SLOT_BOOTS, EMPTY_ARMOR_SLOT_LEGGINGS, EMPTY_ARMOR_SLOT_CHESTPLATE, EMPTY_ARMOR_SLOT_HELMET };

    private static final EquipmentSlot[] SLOT_IDS = new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };

    private final CraftingContainer craftSlots = new TransientCraftingContainer(this, 2, 2);

    private final ResultContainer resultSlots = new ResultContainer();

    public final boolean active;

    private final Player owner;

    public InventoryMenu(Inventory inventory0, boolean boolean1, final Player player2) {
        super(null, 0);
        this.active = boolean1;
        this.owner = player2;
        this.m_38897_(new ResultSlot(inventory0.player, this.craftSlots, this.resultSlots, 0, 154, 28));
        for (int $$3 = 0; $$3 < 2; $$3++) {
            for (int $$4 = 0; $$4 < 2; $$4++) {
                this.m_38897_(new Slot(this.craftSlots, $$4 + $$3 * 2, 98 + $$4 * 18, 18 + $$3 * 18));
            }
        }
        for (int $$5 = 0; $$5 < 4; $$5++) {
            final EquipmentSlot $$6 = SLOT_IDS[$$5];
            this.m_38897_(new Slot(inventory0, 39 - $$5, 8, 8 + $$5 * 18) {

                @Override
                public void setByPlayer(ItemStack p_270969_) {
                    InventoryMenu.onEquipItem(player2, $$6, p_270969_, this.m_7993_());
                    super.setByPlayer(p_270969_);
                }

                @Override
                public int getMaxStackSize() {
                    return 1;
                }

                @Override
                public boolean mayPlace(ItemStack p_39746_) {
                    return $$6 == Mob.m_147233_(p_39746_);
                }

                @Override
                public boolean mayPickup(Player p_39744_) {
                    ItemStack $$1 = this.m_7993_();
                    return !$$1.isEmpty() && !p_39744_.isCreative() && EnchantmentHelper.hasBindingCurse($$1) ? false : super.mayPickup(p_39744_);
                }

                @Override
                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.TEXTURE_EMPTY_SLOTS[$$6.getIndex()]);
                }
            });
        }
        for (int $$7 = 0; $$7 < 3; $$7++) {
            for (int $$8 = 0; $$8 < 9; $$8++) {
                this.m_38897_(new Slot(inventory0, $$8 + ($$7 + 1) * 9, 8 + $$8 * 18, 84 + $$7 * 18));
            }
        }
        for (int $$9 = 0; $$9 < 9; $$9++) {
            this.m_38897_(new Slot(inventory0, $$9, 8 + $$9 * 18, 142));
        }
        this.m_38897_(new Slot(inventory0, 40, 77, 62) {

            @Override
            public void setByPlayer(ItemStack p_270479_) {
                InventoryMenu.onEquipItem(player2, EquipmentSlot.OFFHAND, p_270479_, this.m_7993_());
                super.setByPlayer(p_270479_);
            }

            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
            }
        });
    }

    static void onEquipItem(Player player0, EquipmentSlot equipmentSlot1, ItemStack itemStack2, ItemStack itemStack3) {
        Equipable $$4 = Equipable.get(itemStack2);
        if ($$4 != null) {
            player0.m_238392_(equipmentSlot1, itemStack3, itemStack2);
        }
    }

    public static boolean isHotbarSlot(int int0) {
        return int0 >= 36 && int0 < 45 || int0 == 45;
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents stackedContents0) {
        this.craftSlots.m_5809_(stackedContents0);
    }

    @Override
    public void clearCraftingContent() {
        this.resultSlots.clearContent();
        this.craftSlots.m_6211_();
    }

    @Override
    public boolean recipeMatches(Recipe<? super CraftingContainer> recipeSuperCraftingContainer0) {
        return recipeSuperCraftingContainer0.matches(this.craftSlots, this.owner.m_9236_());
    }

    @Override
    public void slotsChanged(Container container0) {
        CraftingMenu.slotChangedCraftingGrid(this, this.owner.m_9236_(), this.owner, this.craftSlots, this.resultSlots);
    }

    @Override
    public void removed(Player player0) {
        super.m_6877_(player0);
        this.resultSlots.clearContent();
        if (!player0.m_9236_().isClientSide) {
            this.m_150411_(player0, this.craftSlots);
        }
    }

    @Override
    public boolean stillValid(Player player0) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player0, int int1) {
        ItemStack $$2 = ItemStack.EMPTY;
        Slot $$3 = (Slot) this.f_38839_.get(int1);
        if ($$3.hasItem()) {
            ItemStack $$4 = $$3.getItem();
            $$2 = $$4.copy();
            EquipmentSlot $$5 = Mob.m_147233_($$2);
            if (int1 == 0) {
                if (!this.m_38903_($$4, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }
                $$3.onQuickCraft($$4, $$2);
            } else if (int1 >= 1 && int1 < 5) {
                if (!this.m_38903_($$4, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (int1 >= 5 && int1 < 9) {
                if (!this.m_38903_($$4, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if ($$5.getType() == EquipmentSlot.Type.ARMOR && !((Slot) this.f_38839_.get(8 - $$5.getIndex())).hasItem()) {
                int $$6 = 8 - $$5.getIndex();
                if (!this.m_38903_($$4, $$6, $$6 + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if ($$5 == EquipmentSlot.OFFHAND && !((Slot) this.f_38839_.get(45)).hasItem()) {
                if (!this.m_38903_($$4, 45, 46, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (int1 >= 9 && int1 < 36) {
                if (!this.m_38903_($$4, 36, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (int1 >= 36 && int1 < 45) {
                if (!this.m_38903_($$4, 9, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_($$4, 9, 45, false)) {
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
            if (int1 == 0) {
                player0.drop($$4, false);
            }
        }
        return $$2;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack itemStack0, Slot slot1) {
        return slot1.container != this.resultSlots && super.m_5882_(itemStack0, slot1);
    }

    @Override
    public int getResultSlotIndex() {
        return 0;
    }

    @Override
    public int getGridWidth() {
        return this.craftSlots.getWidth();
    }

    @Override
    public int getGridHeight() {
        return this.craftSlots.getHeight();
    }

    @Override
    public int getSize() {
        return 5;
    }

    public CraftingContainer getCraftSlots() {
        return this.craftSlots;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    @Override
    public boolean shouldMoveToInventory(int int0) {
        return int0 != this.getResultSlotIndex();
    }
}