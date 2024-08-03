package net.minecraft.world.inventory;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class SmithingMenu extends ItemCombinerMenu {

    public static final int TEMPLATE_SLOT = 0;

    public static final int BASE_SLOT = 1;

    public static final int ADDITIONAL_SLOT = 2;

    public static final int RESULT_SLOT = 3;

    public static final int TEMPLATE_SLOT_X_PLACEMENT = 8;

    public static final int BASE_SLOT_X_PLACEMENT = 26;

    public static final int ADDITIONAL_SLOT_X_PLACEMENT = 44;

    private static final int RESULT_SLOT_X_PLACEMENT = 98;

    public static final int SLOT_Y_PLACEMENT = 48;

    private final Level level;

    @Nullable
    private SmithingRecipe selectedRecipe;

    private final List<SmithingRecipe> recipes;

    public SmithingMenu(int int0, Inventory inventory1) {
        this(int0, inventory1, ContainerLevelAccess.NULL);
    }

    public SmithingMenu(int int0, Inventory inventory1, ContainerLevelAccess containerLevelAccess2) {
        super(MenuType.SMITHING, int0, inventory1, containerLevelAccess2);
        this.level = inventory1.player.m_9236_();
        this.recipes = this.level.getRecipeManager().getAllRecipesFor(RecipeType.SMITHING);
    }

    @Override
    protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create().withSlot(0, 8, 48, p_266643_ -> this.recipes.stream().anyMatch(p_266642_ -> p_266642_.isTemplateIngredient(p_266643_))).withSlot(1, 26, 48, p_286208_ -> this.recipes.stream().anyMatch(p_286206_ -> p_286206_.isBaseIngredient(p_286208_))).withSlot(2, 44, 48, p_286207_ -> this.recipes.stream().anyMatch(p_286204_ -> p_286204_.isAdditionIngredient(p_286207_))).withResultSlot(3, 98, 48).build();
    }

    @Override
    protected boolean isValidBlock(BlockState blockState0) {
        return blockState0.m_60713_(Blocks.SMITHING_TABLE);
    }

    @Override
    protected boolean mayPickup(Player player0, boolean boolean1) {
        return this.selectedRecipe != null && this.selectedRecipe.m_5818_(this.f_39769_, this.level);
    }

    @Override
    protected void onTake(Player player0, ItemStack itemStack1) {
        itemStack1.onCraftedBy(player0.m_9236_(), player0, itemStack1.getCount());
        this.f_39768_.m_58395_(player0, this.getRelevantItems());
        this.shrinkStackInSlot(0);
        this.shrinkStackInSlot(1);
        this.shrinkStackInSlot(2);
        this.f_39770_.execute((p_40263_, p_40264_) -> p_40263_.m_46796_(1044, p_40264_, 0));
    }

    private List<ItemStack> getRelevantItems() {
        return List.of(this.f_39769_.getItem(0), this.f_39769_.getItem(1), this.f_39769_.getItem(2));
    }

    private void shrinkStackInSlot(int int0) {
        ItemStack $$1 = this.f_39769_.getItem(int0);
        if (!$$1.isEmpty()) {
            $$1.shrink(1);
            this.f_39769_.setItem(int0, $$1);
        }
    }

    @Override
    public void createResult() {
        List<SmithingRecipe> $$0 = this.level.getRecipeManager().getRecipesFor(RecipeType.SMITHING, this.f_39769_, this.level);
        if ($$0.isEmpty()) {
            this.f_39768_.setItem(0, ItemStack.EMPTY);
        } else {
            SmithingRecipe $$1 = (SmithingRecipe) $$0.get(0);
            ItemStack $$2 = $$1.m_5874_(this.f_39769_, this.level.registryAccess());
            if ($$2.isItemEnabled(this.level.m_246046_())) {
                this.selectedRecipe = $$1;
                this.f_39768_.setRecipeUsed($$1);
                this.f_39768_.setItem(0, $$2);
            }
        }
    }

    @Override
    public int getSlotToQuickMoveTo(ItemStack itemStack0) {
        return (Integer) ((Optional) this.recipes.stream().map(p_266640_ -> findSlotMatchingIngredient(p_266640_, itemStack0)).filter(Optional::isPresent).findFirst().orElse(Optional.of(0))).get();
    }

    private static Optional<Integer> findSlotMatchingIngredient(SmithingRecipe smithingRecipe0, ItemStack itemStack1) {
        if (smithingRecipe0.isTemplateIngredient(itemStack1)) {
            return Optional.of(0);
        } else if (smithingRecipe0.isBaseIngredient(itemStack1)) {
            return Optional.of(1);
        } else {
            return smithingRecipe0.isAdditionIngredient(itemStack1) ? Optional.of(2) : Optional.empty();
        }
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack itemStack0, Slot slot1) {
        return slot1.container != this.f_39768_ && super.m_5882_(itemStack0, slot1);
    }

    @Override
    public boolean canMoveIntoInputSlots(ItemStack itemStack0) {
        return this.recipes.stream().map(p_266647_ -> findSlotMatchingIngredient(p_266647_, itemStack0)).anyMatch(Optional::isPresent);
    }
}