package mezz.jei.api.helpers;

import mezz.jei.api.ingredients.IIngredientTypeWithSubtypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

public interface IPlatformFluidHelper<T> {

    IIngredientTypeWithSubtypes<Fluid, T> getFluidIngredientType();

    T create(Fluid var1, long var2, @Nullable CompoundTag var4);

    T create(Fluid var1, long var2);

    long bucketVolume();
}