package mezz.jei.common.platform;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface IPlatformHelper {

    <T> IPlatformRegistry<T> getRegistry(ResourceKey<? extends Registry<T>> var1);

    IPlatformItemStackHelper getItemStackHelper();

    IPlatformFluidHelperInternal<?> getFluidHelper();

    IPlatformRenderHelper getRenderHelper();

    IPlatformRecipeHelper getRecipeHelper();

    IPlatformConfigHelper getConfigHelper();

    IPlatformInputHelper getInputHelper();

    IPlatformScreenHelper getScreenHelper();

    IPlatformIngredientHelper getIngredientHelper();

    IPlatformModHelper getModHelper();
}