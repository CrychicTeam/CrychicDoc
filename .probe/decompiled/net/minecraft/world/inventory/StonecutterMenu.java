package net.minecraft.world.inventory;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class StonecutterMenu extends AbstractContainerMenu {

    public static final int INPUT_SLOT = 0;

    public static final int RESULT_SLOT = 1;

    private static final int INV_SLOT_START = 2;

    private static final int INV_SLOT_END = 29;

    private static final int USE_ROW_SLOT_START = 29;

    private static final int USE_ROW_SLOT_END = 38;

    private final ContainerLevelAccess access;

    private final DataSlot selectedRecipeIndex = DataSlot.standalone();

    private final Level level;

    private List<StonecutterRecipe> recipes = Lists.newArrayList();

    private ItemStack input = ItemStack.EMPTY;

    long lastSoundTime;

    final Slot inputSlot;

    final Slot resultSlot;

    Runnable slotUpdateListener = () -> {
    };

    public final Container container = new SimpleContainer(1) {

        @Override
        public void setChanged() {
            super.setChanged();
            StonecutterMenu.this.slotsChanged(this);
            StonecutterMenu.this.slotUpdateListener.run();
        }
    };

    final ResultContainer resultContainer = new ResultContainer();

    public StonecutterMenu(int int0, Inventory inventory1) {
        this(int0, inventory1, ContainerLevelAccess.NULL);
    }

    public StonecutterMenu(int int0, Inventory inventory1, final ContainerLevelAccess containerLevelAccess2) {
        super(MenuType.STONECUTTER, int0);
        this.access = containerLevelAccess2;
        this.level = inventory1.player.m_9236_();
        this.inputSlot = this.m_38897_(new Slot(this.container, 0, 20, 33));
        this.resultSlot = this.m_38897_(new Slot(this.resultContainer, 1, 143, 33) {

            @Override
            public boolean mayPlace(ItemStack p_40362_) {
                return false;
            }

            @Override
            public void onTake(Player p_150672_, ItemStack p_150673_) {
                p_150673_.onCraftedBy(p_150672_.m_9236_(), p_150672_, p_150673_.getCount());
                StonecutterMenu.this.resultContainer.m_58395_(p_150672_, this.getRelevantItems());
                ItemStack $$2 = StonecutterMenu.this.inputSlot.remove(1);
                if (!$$2.isEmpty()) {
                    StonecutterMenu.this.setupResultSlot();
                }
                containerLevelAccess2.execute((p_40364_, p_40365_) -> {
                    long $$2x = p_40364_.getGameTime();
                    if (StonecutterMenu.this.lastSoundTime != $$2x) {
                        p_40364_.playSound(null, p_40365_, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
                        StonecutterMenu.this.lastSoundTime = $$2x;
                    }
                });
                super.onTake(p_150672_, p_150673_);
            }

            private List<ItemStack> getRelevantItems() {
                return List.of(StonecutterMenu.this.inputSlot.getItem());
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
        this.m_38895_(this.selectedRecipeIndex);
    }

    public int getSelectedRecipeIndex() {
        return this.selectedRecipeIndex.get();
    }

    public List<StonecutterRecipe> getRecipes() {
        return this.recipes;
    }

    public int getNumRecipes() {
        return this.recipes.size();
    }

    public boolean hasInputItem() {
        return this.inputSlot.hasItem() && !this.recipes.isEmpty();
    }

    @Override
    public boolean stillValid(Player player0) {
        return m_38889_(this.access, player0, Blocks.STONECUTTER);
    }

    @Override
    public boolean clickMenuButton(Player player0, int int1) {
        if (this.isValidRecipeIndex(int1)) {
            this.selectedRecipeIndex.set(int1);
            this.setupResultSlot();
        }
        return true;
    }

    private boolean isValidRecipeIndex(int int0) {
        return int0 >= 0 && int0 < this.recipes.size();
    }

    @Override
    public void slotsChanged(Container container0) {
        ItemStack $$1 = this.inputSlot.getItem();
        if (!$$1.is(this.input.getItem())) {
            this.input = $$1.copy();
            this.setupRecipeList(container0, $$1);
        }
    }

    private void setupRecipeList(Container container0, ItemStack itemStack1) {
        this.recipes.clear();
        this.selectedRecipeIndex.set(-1);
        this.resultSlot.set(ItemStack.EMPTY);
        if (!itemStack1.isEmpty()) {
            this.recipes = this.level.getRecipeManager().getRecipesFor(RecipeType.STONECUTTING, container0, this.level);
        }
    }

    void setupResultSlot() {
        if (!this.recipes.isEmpty() && this.isValidRecipeIndex(this.selectedRecipeIndex.get())) {
            StonecutterRecipe $$0 = (StonecutterRecipe) this.recipes.get(this.selectedRecipeIndex.get());
            ItemStack $$1 = $$0.m_5874_(this.container, this.level.registryAccess());
            if ($$1.isItemEnabled(this.level.m_246046_())) {
                this.resultContainer.setRecipeUsed($$0);
                this.resultSlot.set($$1);
            } else {
                this.resultSlot.set(ItemStack.EMPTY);
            }
        } else {
            this.resultSlot.set(ItemStack.EMPTY);
        }
        this.m_38946_();
    }

    @Override
    public MenuType<?> getType() {
        return MenuType.STONECUTTER;
    }

    public void registerUpdateListener(Runnable runnable0) {
        this.slotUpdateListener = runnable0;
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
            Item $$5 = $$4.getItem();
            $$2 = $$4.copy();
            if (int1 == 1) {
                $$5.onCraftedBy($$4, player0.m_9236_(), player0);
                if (!this.m_38903_($$4, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
                $$3.onQuickCraft($$4, $$2);
            } else if (int1 == 0) {
                if (!this.m_38903_($$4, 2, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.level.getRecipeManager().getRecipeFor(RecipeType.STONECUTTING, new SimpleContainer($$4), this.level).isPresent()) {
                if (!this.m_38903_($$4, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (int1 >= 2 && int1 < 29) {
                if (!this.m_38903_($$4, 29, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (int1 >= 29 && int1 < 38 && !this.m_38903_($$4, 2, 29, false)) {
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
        this.resultContainer.removeItemNoUpdate(1);
        this.access.execute((p_40313_, p_40314_) -> this.m_150411_(player0, this.container));
    }
}