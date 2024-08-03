package mezz.jei.api.recipe.vanilla;

import java.util.List;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Unmodifiable;

public interface IJeiAnvilRecipe {

    @Unmodifiable
    List<ItemStack> getLeftInputs();

    @Unmodifiable
    List<ItemStack> getRightInputs();

    @Unmodifiable
    List<ItemStack> getOutputs();
}