package mezz.jei.library.plugins.vanilla.compostable;

import com.google.common.base.Preconditions;
import java.util.List;
import mezz.jei.api.recipe.vanilla.IJeiCompostingRecipe;
import net.minecraft.world.item.ItemStack;

public class CompostingRecipe implements IJeiCompostingRecipe {

    private final List<ItemStack> inputs;

    private final float chance;

    public CompostingRecipe(ItemStack input, float chance) {
        Preconditions.checkArgument(chance > 0.0F, "composting chance must be greater than 0");
        this.inputs = List.of(input);
        this.chance = chance;
    }

    @Override
    public List<ItemStack> getInputs() {
        return this.inputs;
    }

    @Override
    public float getChance() {
        return this.chance;
    }
}