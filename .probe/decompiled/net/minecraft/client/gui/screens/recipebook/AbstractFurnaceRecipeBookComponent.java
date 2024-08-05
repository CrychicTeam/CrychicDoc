package net.minecraft.client.gui.screens.recipebook;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

public abstract class AbstractFurnaceRecipeBookComponent extends RecipeBookComponent {

    @Nullable
    private Ingredient fuels;

    @Override
    protected void initFilterButtonTextures() {
        this.f_100270_.initTextureValues(152, 182, 28, 18, f_100268_);
    }

    @Override
    public void slotClicked(@Nullable Slot slot0) {
        super.slotClicked(slot0);
        if (slot0 != null && slot0.index < this.f_100271_.getSize()) {
            this.f_100269_.clear();
        }
    }

    @Override
    public void setupGhostRecipe(Recipe<?> recipe0, List<Slot> listSlot1) {
        ItemStack $$2 = recipe0.getResultItem(this.f_100272_.level.m_9598_());
        this.f_100269_.setRecipe(recipe0);
        this.f_100269_.addIngredient(Ingredient.of($$2), ((Slot) listSlot1.get(2)).x, ((Slot) listSlot1.get(2)).y);
        NonNullList<Ingredient> $$3 = recipe0.getIngredients();
        Slot $$4 = (Slot) listSlot1.get(1);
        if ($$4.getItem().isEmpty()) {
            if (this.fuels == null) {
                this.fuels = Ingredient.of(this.getFuelItems().stream().filter(p_280880_ -> p_280880_.m_245993_(this.f_100272_.level.enabledFeatures())).map(ItemStack::new));
            }
            this.f_100269_.addIngredient(this.fuels, $$4.x, $$4.y);
        }
        Iterator<Ingredient> $$5 = $$3.iterator();
        for (int $$6 = 0; $$6 < 2; $$6++) {
            if (!$$5.hasNext()) {
                return;
            }
            Ingredient $$7 = (Ingredient) $$5.next();
            if (!$$7.isEmpty()) {
                Slot $$8 = (Slot) listSlot1.get($$6);
                this.f_100269_.addIngredient($$7, $$8.x, $$8.y);
            }
        }
    }

    protected abstract Set<Item> getFuelItems();
}