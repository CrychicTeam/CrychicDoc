package net.minecraft.world.level.material;

import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class Fluids {

    public static final Fluid EMPTY = register("empty", new EmptyFluid());

    public static final FlowingFluid FLOWING_WATER = register("flowing_water", new WaterFluid.Flowing());

    public static final FlowingFluid WATER = register("water", new WaterFluid.Source());

    public static final FlowingFluid FLOWING_LAVA = register("flowing_lava", new LavaFluid.Flowing());

    public static final FlowingFluid LAVA = register("lava", new LavaFluid.Source());

    private static <T extends Fluid> T register(String string0, T t1) {
        return Registry.register(BuiltInRegistries.FLUID, string0, t1);
    }

    static {
        for (Fluid $$0 : BuiltInRegistries.FLUID) {
            UnmodifiableIterator var2 = $$0.getStateDefinition().getPossibleStates().iterator();
            while (var2.hasNext()) {
                FluidState $$1 = (FluidState) var2.next();
                Fluid.FLUID_STATE_REGISTRY.add($$1);
            }
        }
    }
}