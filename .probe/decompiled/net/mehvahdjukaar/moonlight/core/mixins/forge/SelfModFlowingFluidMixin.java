package net.mehvahdjukaar.moonlight.core.mixins.forge;

import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.client.forge.ModFluidType;
import net.mehvahdjukaar.moonlight.api.fluids.ModFlowingFluid;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ ModFlowingFluid.class })
public abstract class SelfModFlowingFluidMixin extends FlowingFluid {

    @Unique
    private Supplier<FluidType> type;

    @Overwrite(remap = false)
    private void afterInit(ModFlowingFluid.Properties properties) {
        if (properties.copyFluid != null) {
            this.type = properties.copyFluid::getFluidType;
        } else {
            ModFluidType t = ModFluidType.create(properties, (ModFlowingFluid) this);
            this.type = () -> t;
        }
    }

    public FluidType getFluidType() {
        return (FluidType) this.type.get();
    }
}