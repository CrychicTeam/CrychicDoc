package dev.xkmc.l2complements.content.recipe;

import dev.xkmc.l2complements.init.registrate.LCRecipes;
import dev.xkmc.l2library.serial.recipe.BaseRecipe;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

@SerialClass
public class DiffusionRecipe extends BaseRecipe<DiffusionRecipe, DiffusionRecipe, DiffusionRecipe.Inv> {

    @SerialField
    public Block ingredient;

    @SerialField
    public Block base;

    @SerialField
    public Block result;

    public DiffusionRecipe(ResourceLocation id) {
        super(id, (BaseRecipe.RecType<DiffusionRecipe, DiffusionRecipe, DiffusionRecipe.Inv>) LCRecipes.RS_DIFFUSION.get());
    }

    public boolean matches(DiffusionRecipe.Inv inv, Level level) {
        if (inv.m_8020_(0).getItem() instanceof BlockItem b0 && b0.getBlock() == this.ingredient && inv.m_8020_(1).getItem() instanceof BlockItem b1 && b1.getBlock() == this.base) {
            return true;
        }
        return false;
    }

    public ItemStack assemble(DiffusionRecipe.Inv inv, RegistryAccess access) {
        return this.result.asItem().getDefaultInstance();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return this.result.asItem().getDefaultInstance();
    }

    public static class Inv extends SimpleContainer implements BaseRecipe.RecInv<DiffusionRecipe> {

        public Inv() {
            super(2);
        }
    }
}