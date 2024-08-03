package com.prunoideae.powerfuljs.capabilities.forge.mods.immersive;

import blusunrize.immersiveengineering.api.tool.ExternalHeaterHandler;
import blusunrize.immersiveengineering.api.tool.ExternalHeaterHandler.IExternalHeatable;
import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;

public class CapabilityExternalHeatable {

    public CapabilityExternalHeatable.BlockEntityBuilder blockEntity() {
        return new CapabilityExternalHeatable.BlockEntityBuilder();
    }

    public static class BlockEntityBuilder extends CapabilityBuilderForge<BlockEntity, IExternalHeatable> {

        CapabilityExternalHeatable.DoHeatTick doHeatTick = (a, b, c) -> 0;

        public CapabilityExternalHeatable.BlockEntityBuilder doHeatTick(CapabilityExternalHeatable.DoHeatTick doHeatTick) {
            this.doHeatTick = doHeatTick;
            return this;
        }

        public IExternalHeatable getCapability(BlockEntity instance) {
            return (IExternalHeatable) this.doHeatTick;
        }

        public Capability<IExternalHeatable> getCapabilityKey() {
            return ExternalHeaterHandler.CAPABILITY;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("ie_exheater_be_custom");
        }
    }

    @FunctionalInterface
    interface DoHeatTick {

        int doHeatTick(BlockEntity var1, int var2, boolean var3);
    }
}