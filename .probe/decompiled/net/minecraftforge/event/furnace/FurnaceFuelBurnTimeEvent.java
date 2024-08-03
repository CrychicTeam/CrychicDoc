package net.minecraftforge.event.furnace;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Cancelable
public class FurnaceFuelBurnTimeEvent extends Event {

    @NotNull
    private final ItemStack itemStack;

    @Nullable
    private final RecipeType<?> recipeType;

    private int burnTime;

    public FurnaceFuelBurnTimeEvent(@NotNull ItemStack itemStack, int burnTime, @Nullable RecipeType<?> recipeType) {
        this.itemStack = itemStack;
        this.burnTime = burnTime;
        this.recipeType = recipeType;
    }

    @NotNull
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Nullable
    public RecipeType<?> getRecipeType() {
        return this.recipeType;
    }

    public void setBurnTime(int burnTime) {
        if (burnTime >= 0) {
            this.burnTime = burnTime;
            this.setCanceled(true);
        }
    }

    public int getBurnTime() {
        return this.burnTime;
    }
}