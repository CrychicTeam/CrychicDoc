package com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import java.util.function.Predicate;
import mekanism.api.IEvaporationSolar;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;

public class CapabilityEvaporationTower {

    public CapabilityEvaporationTower.BlockEntityBuilder blockEntity() {
        return new CapabilityEvaporationTower.BlockEntityBuilder();
    }

    public static class BlockEntityBuilder extends CapabilityBuilderForge<BlockEntity, IEvaporationSolar> {

        private Predicate<BlockEntity> canSeeSun;

        public CapabilityEvaporationTower.BlockEntityBuilder canSeeSun(Predicate<BlockEntity> canSeeSun) {
            this.canSeeSun = canSeeSun;
            return this;
        }

        public IEvaporationSolar getCapability(BlockEntity instance) {
            return () -> this.canSeeSun != null && this.canSeeSun.test(instance);
        }

        public Capability<IEvaporationSolar> getCapabilityKey() {
            return Capabilities.EVAPORATION_SOLAR;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:mek_solar_be");
        }
    }
}