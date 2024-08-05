package net.blay09.mods.waystones.compat.jei;

import java.util.ArrayList;
import java.util.List;
import net.blay09.mods.waystones.item.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AttunedShardJeiRecipe {

    private final List<ItemStack> inputs = new ArrayList();

    private final ItemStack output;

    public AttunedShardJeiRecipe() {
        this.inputs.add(new ItemStack(Items.FLINT));
        this.inputs.add(new ItemStack(ModItems.warpDust));
        this.inputs.add(new ItemStack(ModItems.warpDust));
        this.inputs.add(new ItemStack(ModItems.warpDust));
        this.inputs.add(new ItemStack(ModItems.warpDust));
        this.output = new ItemStack(ModItems.attunedShard);
    }

    public List<ItemStack> getInputs() {
        return this.inputs;
    }

    public ItemStack getOutput() {
        return this.output;
    }
}