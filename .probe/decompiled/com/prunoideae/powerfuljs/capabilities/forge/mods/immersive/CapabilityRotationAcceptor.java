package com.prunoideae.powerfuljs.capabilities.forge.mods.immersive;

import blusunrize.immersiveengineering.api.energy.IRotationAcceptor;
import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;

public class CapabilityRotationAcceptor {

    public CapabilityRotationAcceptor.BlockEntityBuilder blockEntity() {
        return new CapabilityRotationAcceptor.BlockEntityBuilder();
    }

    public static class BlockEntityBuilder extends CapabilityBuilderForge<BlockEntity, IRotationAcceptor> {

        private BiConsumer<BlockEntity, Double> inputRotation;

        public CapabilityRotationAcceptor.BlockEntityBuilder inputRotation(BiConsumer<BlockEntity, Double> inputRotation) {
            this.inputRotation = inputRotation;
            return this;
        }

        public IRotationAcceptor getCapability(BlockEntity instance) {
            return rotation -> {
                if (this.inputRotation != null) {
                    this.inputRotation.accept(instance, rotation);
                }
            };
        }

        public Capability<IRotationAcceptor> getCapabilityKey() {
            return IRotationAcceptor.CAPABILITY;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:ie_rotation_be_custom");
        }
    }
}