package team.lodestar.lodestone.systems.item;

import javax.annotation.Nullable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;

public class LodestoneFuelBlockItem extends BlockItem {

    public final int fuel;

    public LodestoneFuelBlockItem(Block block, Item.Properties properties, int fuel) {
        super(block, properties);
        this.fuel = fuel;
    }

    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return this.fuel;
    }
}