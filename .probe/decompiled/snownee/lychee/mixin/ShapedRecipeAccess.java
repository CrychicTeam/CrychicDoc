package snownee.lychee.mixin;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ ShapedRecipe.class })
public interface ShapedRecipeAccess {

    @Invoker
    boolean callMatches(CraftingContainer var1, int var2, int var3, boolean var4);

    @Accessor
    ItemStack getResult();
}