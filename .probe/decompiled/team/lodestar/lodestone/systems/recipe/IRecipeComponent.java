package team.lodestar.lodestone.systems.recipe;

import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public interface IRecipeComponent {

    ItemStack getStack();

    List<ItemStack> getStacks();

    Item getItem();

    int getCount();

    default boolean isValid() {
        return !this.getStack().is(Items.BARRIER);
    }

    boolean matches(ItemStack var1);
}