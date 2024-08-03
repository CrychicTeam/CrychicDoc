package dev.xkmc.modulargolems.compat.misc;

import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import dev.xkmc.modulargolems.events.event.GolemHandleExpEvent;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import plus.dragons.createenchantmentindustry.content.contraptions.enchanting.disenchanter.DisenchanterBlockEntity;
import plus.dragons.createenchantmentindustry.content.contraptions.fluids.experience.ExperienceFluid;
import plus.dragons.createenchantmentindustry.entry.CeiFluids;

public class CEICompat {

    public static void register() {
        MinecraftForge.EVENT_BUS.register(CEICompat.class);
    }

    @SubscribeEvent
    public static void onHandleExp(GolemHandleExpEvent event) {
        if (!event.getOrb().m_213877_()) {
            int val = event.getOrb().getValue();
            if (val > 0) {
                BlockPos pos = event.getEntity().m_20183_();
                for (BlockPos i : List.of(pos, pos.above())) {
                    BlockEntity fluidStack = event.getEntity().m_9236_().getBlockEntity(i);
                    if (fluidStack instanceof DisenchanterBlockEntity) {
                        DisenchanterBlockEntity be = (DisenchanterBlockEntity) fluidStack;
                        FluidStack fluidStackx = new FluidStack(((ExperienceFluid) CeiFluids.EXPERIENCE.get()).m_5613_(), val);
                        LazyOptional<IFluidHandler> lazyOpt = be.getCapability(ForgeCapabilities.FLUID_HANDLER);
                        if (lazyOpt.resolve().isPresent()) {
                            IFluidHandler cap = (IFluidHandler) lazyOpt.resolve().get();
                            if (cap instanceof SmartFluidTankBehaviour.InternalFluidHandler tank) {
                                int fill = tank.forceFill(fluidStackx, IFluidHandler.FluidAction.EXECUTE);
                                val -= fill;
                            }
                        }
                    }
                }
                event.getOrb().value = val;
            }
        }
    }
}