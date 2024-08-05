package team.lodestar.lodestone.systems.item;

import javax.annotation.Nullable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

public class LodestoneFuelItem extends Item {

    public final int fuel;

    public LodestoneFuelItem(Item.Properties properties, int fuel) {
        super(properties);
        this.fuel = fuel;
    }

    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return this.fuel;
    }
}