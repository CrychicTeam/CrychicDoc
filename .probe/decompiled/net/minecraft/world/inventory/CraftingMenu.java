package net.minecraft.world.inventory;

import java.util.Optional;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class CraftingMenu extends RecipeBookMenu<CraftingContainer> {

    public static final int RESULT_SLOT = 0;

    private static final int CRAFT_SLOT_START = 1;

    private static final int CRAFT_SLOT_END = 10;

    private static final int INV_SLOT_START = 10;

    private static final int INV_SLOT_END = 37;

    private static final int USE_ROW_SLOT_START = 37;

    private static final int USE_ROW_SLOT_END = 46;

    private final CraftingContainer craftSlots = new TransientCraftingContainer(this, 3, 3);

    private final ResultContainer resultSlots = new ResultContainer();

    private final ContainerLevelAccess access;

    private final Player player;

    public CraftingMenu(int int0, Inventory inventory1) {
        this(int0, inventory1, ContainerLevelAccess.NULL);
    }

    public CraftingMenu(int int0, Inventory inventory1, ContainerLevelAccess containerLevelAccess2) {
        super(MenuType.CRAFTING, int0);
        this.access = containerLevelAccess2;
        this.player = inventory1.player;
        this.m_38897_(new ResultSlot(inventory1.player, this.craftSlots, this.resultSlots, 0, 124, 35));
        for (int $$3 = 0; $$3 < 3; $$3++) {
            for (int $$4 = 0; $$4 < 3; $$4++) {
                this.m_38897_(new Slot(this.craftSlots, $$4 + $$3 * 3, 30 + $$4 * 18, 17 + $$3 * 18));
            }
        }
        for (int $$5 = 0; $$5 < 3; $$5++) {
            for (int $$6 = 0; $$6 < 9; $$6++) {
                this.m_38897_(new Slot(inventory1, $$6 + $$5 * 9 + 9, 8 + $$6 * 18, 84 + $$5 * 18));
            }
        }
        for (int $$7 = 0; $$7 < 9; $$7++) {
            this.m_38897_(new Slot(inventory1, $$7, 8 + $$7 * 18, 142));
        }
    }

    protected static void slotChangedCraftingGrid(AbstractContainerMenu abstractContainerMenu0, Level level1, Player player2, CraftingContainer craftingContainer3, ResultContainer resultContainer4) {
        if (!level1.isClientSide) {
            ServerPlayer $$5 = (ServerPlayer) player2;
            ItemStack $$6 = ItemStack.EMPTY;
            Optional<CraftingRecipe> $$7 = level1.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingContainer3, level1);
            if ($$7.isPresent()) {
                CraftingRecipe $$8 = (CraftingRecipe) $$7.get();
                if (resultContainer4.m_40135_(level1, $$5, $$8)) {
                    ItemStack $$9 = $$8.m_5874_(craftingContainer3, level1.registryAccess());
                    if ($$9.isItemEnabled(level1.m_246046_())) {
                        $$6 = $$9;
                    }
                }
            }
            resultContainer4.setItem(0, $$6);
            abstractContainerMenu0.setRemoteSlot(0, $$6);
            $$5.connection.send(new ClientboundContainerSetSlotPacket(abstractContainerMenu0.containerId, abstractContainerMenu0.incrementStateId(), 0, $$6));
        }
    }

    @Override
    public void slotsChanged(Container container0) {
        this.access.execute((p_39386_, p_39387_) -> slotChangedCraftingGrid(this, p_39386_, this.player, this.craftSlots, this.resultSlots));
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents stackedContents0) {
        this.craftSlots.m_5809_(stackedContents0);
    }

    @Override
    public void clearCraftingContent() {
        this.craftSlots.m_6211_();
        this.resultSlots.clearContent();
    }

    @Override
    public boolean recipeMatches(Recipe<? super CraftingContainer> recipeSuperCraftingContainer0) {
        return recipeSuperCraftingContainer0.matches(this.craftSlots, this.player.m_9236_());
    }

    @Override
    public void removed(Player player0) {
        super.m_6877_(player0);
        this.access.execute((p_39371_, p_39372_) -> this.m_150411_(player0, this.craftSlots));
    }

    @Override
    public boolean stillValid(Player player0) {
        return m_38889_(this.access, player0, Blocks.CRAFTING_TABLE);
    }

    @Override
    public ItemStack quickMoveStack(Player player0, int int1) {
        ItemStack $$2 = ItemStack.EMPTY;
        Slot $$3 = (Slot) this.f_38839_.get(int1);
        if ($$3 != null && $$3.hasItem()) {
            ItemStack $$4 = $$3.getItem();
            $$2 = $$4.copy();
            if (int1 == 0) {
                this.access.execute((p_39378_, p_39379_) -> $$4.getItem().onCraftedBy($$4, p_39378_, player0));
                if (!this.m_38903_($$4, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }
                $$3.onQuickCraft($$4, $$2);
            } else if (int1 >= 10 && int1 < 46) {
                if (!this.m_38903_($$4, 1, 10, false)) {
                    if (int1 < 37) {
                        if (!this.m_38903_($$4, 37, 46, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.m_38903_($$4, 10, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.m_38903_($$4, 10, 46, false)) {
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
        return 10;
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