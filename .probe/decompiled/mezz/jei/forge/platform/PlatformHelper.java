package mezz.jei.forge.platform;

import java.util.function.Supplier;
import mezz.jei.common.platform.IPlatformFluidHelperInternal;
import mezz.jei.common.platform.IPlatformHelper;
import mezz.jei.common.platform.IPlatformRegistry;
import mezz.jei.core.util.function.LazySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class PlatformHelper implements IPlatformHelper {

    private final Supplier<ItemStackHelper> itemStackHelper = new LazySupplier<>(ItemStackHelper::new);

    private final Supplier<FluidHelper> fluidHelper = new LazySupplier<>(FluidHelper::new);

    private final Supplier<RenderHelper> renderHelper = new LazySupplier<>(RenderHelper::new);

    private final Supplier<RecipeHelper> recipeHelper = new LazySupplier<>(RecipeHelper::new);

    private final Supplier<ConfigHelper> configHelper = new LazySupplier<>(ConfigHelper::new);

    private final Supplier<InputHelper> inputHelper = new LazySupplier<>(InputHelper::new);

    private final Supplier<ScreenHelper> screenHelper = new LazySupplier<>(ScreenHelper::new);

    private final Supplier<IngredientHelper> ingredientHelper = new LazySupplier<>(IngredientHelper::new);

    private final Supplier<ModHelper> modHelper = new LazySupplier<>(ModHelper::new);

    @Override
    public <T> IPlatformRegistry<T> getRegistry(ResourceKey<? extends Registry<T>> key) {
        return RegistryWrapper.getRegistry(key);
    }

    public ItemStackHelper getItemStackHelper() {
        return (ItemStackHelper) this.itemStackHelper.get();
    }

    @Override
    public IPlatformFluidHelperInternal<?> getFluidHelper() {
        return (IPlatformFluidHelperInternal<?>) this.fluidHelper.get();
    }

    public RenderHelper getRenderHelper() {
        return (RenderHelper) this.renderHelper.get();
    }

    public RecipeHelper getRecipeHelper() {
        return (RecipeHelper) this.recipeHelper.get();
    }

    public ConfigHelper getConfigHelper() {
        return (ConfigHelper) this.configHelper.get();
    }

    public InputHelper getInputHelper() {
        return (InputHelper) this.inputHelper.get();
    }

    public ScreenHelper getScreenHelper() {
        return (ScreenHelper) this.screenHelper.get();
    }

    public IngredientHelper getIngredientHelper() {
        return (IngredientHelper) this.ingredientHelper.get();
    }

    public ModHelper getModHelper() {
        return (ModHelper) this.modHelper.get();
    }
}