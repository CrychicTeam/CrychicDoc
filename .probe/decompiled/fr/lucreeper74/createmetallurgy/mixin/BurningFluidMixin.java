package fr.lucreeper74.createmetallurgy.mixin;

import com.simibubi.create.AllTags;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ Entity.class })
public abstract class BurningFluidMixin {

    @Shadow
    protected boolean firstTick;

    @Shadow
    protected Object2DoubleMap<FluidType> forgeFluidTypeHeight;

    @Inject(method = { "isInLava()Z" }, at = { @At("HEAD") }, cancellable = true)
    private void create$isInBurningFluid(CallbackInfoReturnable<Boolean> cir) {
        if (!this.firstTick) {
            if (this.forgeFluidTypeHeight.getDouble(ForgeMod.LAVA_TYPE.get()) > 0.0) {
                cir.setReturnValue(true);
            } else {
                for (Fluid fluid : ForgeRegistries.FLUIDS.tags().getTag(AllTags.forgeFluidTag("molten_materials"))) {
                    if (this.forgeFluidTypeHeight.getDouble(fluid.getFluidType()) > 0.0) {
                        cir.setReturnValue(true);
                        break;
                    }
                }
            }
        }
    }
}