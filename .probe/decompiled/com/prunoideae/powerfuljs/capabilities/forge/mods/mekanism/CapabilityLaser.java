package com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import mekanism.api.lasers.ILaserReceptor;
import mekanism.api.math.FloatingLong;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.NotNull;

public class CapabilityLaser {

    public CapabilityLaser.BlockEntityBuilder blockEntity() {
        return new CapabilityLaser.BlockEntityBuilder();
    }

    public static class BlockEntityBuilder extends CapabilityBuilderForge<BlockEntity, ILaserReceptor> {

        private Predicate<BlockEntity> canLaserDig;

        private BiConsumer<BlockEntity, FloatingLong> receiveEnergy;

        public CapabilityLaser.BlockEntityBuilder canLaserDig(Predicate<BlockEntity> canLaserDig) {
            this.canLaserDig = canLaserDig;
            return this;
        }

        public CapabilityLaser.BlockEntityBuilder receiveEnergy(BiConsumer<BlockEntity, FloatingLong> receiveEnergy) {
            this.receiveEnergy = receiveEnergy;
            return this;
        }

        public ILaserReceptor getCapability(BlockEntity instance) {
            return new ILaserReceptor() {

                public void receiveLaserEnergy(@NotNull FloatingLong floatingLong) {
                    if (BlockEntityBuilder.this.receiveEnergy != null) {
                        BlockEntityBuilder.this.receiveEnergy.accept(instance, floatingLong);
                    }
                }

                public boolean canLasersDig() {
                    return BlockEntityBuilder.this.canLaserDig != null && BlockEntityBuilder.this.canLaserDig.test(instance);
                }
            };
        }

        public Capability<ILaserReceptor> getCapabilityKey() {
            return Capabilities.LASER_RECEPTOR;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:mek_laser_be");
        }
    }
}