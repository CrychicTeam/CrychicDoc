package com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import mekanism.api.IAlloyInteraction;
import mekanism.api.tier.AlloyTier;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;

public class CapabilityAlloyInteractable {

    public CapabilityAlloyInteractable.BlockEntityBuilder blockEntity() {
        return new CapabilityAlloyInteractable.BlockEntityBuilder();
    }

    @FunctionalInterface
    public interface AlloyInteraction {

        void onInteraction(BlockEntity var1, Player var2, ItemStack var3, AlloyTier var4);
    }

    public static class BlockEntityBuilder extends CapabilityBuilderForge<BlockEntity, IAlloyInteraction> {

        private CapabilityAlloyInteractable.AlloyInteraction interaction;

        public CapabilityAlloyInteractable.BlockEntityBuilder interaction(CapabilityAlloyInteractable.AlloyInteraction interaction) {
            this.interaction = interaction;
            return this;
        }

        public IAlloyInteraction getCapability(BlockEntity instance) {
            return (player, stack, tier) -> {
                if (this.interaction != null) {
                    this.interaction.onInteraction(instance, player, stack, tier);
                }
            };
        }

        public Capability<IAlloyInteraction> getCapabilityKey() {
            return Capabilities.ALLOY_INTERACTION;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:alloy_be_custom");
        }
    }
}