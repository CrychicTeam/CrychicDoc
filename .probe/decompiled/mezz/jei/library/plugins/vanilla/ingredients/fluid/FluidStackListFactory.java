package mezz.jei.library.plugins.vanilla.ingredients.fluid;

import java.util.List;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.common.platform.IPlatformRegistry;
import net.minecraft.world.level.material.Fluid;

public final class FluidStackListFactory {

    private FluidStackListFactory() {
    }

    public static <T> List<T> create(IPlatformRegistry<Fluid> registry, IPlatformFluidHelper<T> helper) {
        return registry.getValues().filter(fluid -> fluid.isSource(fluid.defaultFluidState())).map(fluid -> helper.create(fluid, helper.bucketVolume())).toList();
    }
}