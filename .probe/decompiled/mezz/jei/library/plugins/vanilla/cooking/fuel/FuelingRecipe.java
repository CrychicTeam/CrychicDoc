package mezz.jei.library.plugins.vanilla.cooking.fuel;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.List;
import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import net.minecraft.world.item.ItemStack;

public class FuelingRecipe implements IJeiFuelingRecipe {

    private final List<ItemStack> inputs;

    private final int burnTime;

    public FuelingRecipe(Collection<ItemStack> input, int burnTime) {
        Preconditions.checkArgument(burnTime > 0, "burn time must be greater than 0");
        this.inputs = List.copyOf(input);
        this.burnTime = burnTime;
    }

    @Override
    public List<ItemStack> getInputs() {
        return this.inputs;
    }

    @Override
    public int getBurnTime() {
        return this.burnTime;
    }
}