package com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import java.util.function.ToDoubleFunction;
import mekanism.api.lasers.ILaserDissipation;
import mekanism.api.radiation.capability.IRadiationShielding;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;

public class CapabilityProtection {

    public CapabilityProtection.LaserItemStackBuilder itemStackLaserProtection() {
        return new CapabilityProtection.LaserItemStackBuilder();
    }

    public CapabilityProtection.RadiationItemStackBuilder itemStackRadiationProtection() {
        return new CapabilityProtection.RadiationItemStackBuilder();
    }

    public static class LaserItemStackBuilder extends CapabilityBuilderForge<ItemStack, ILaserDissipation> {

        private ToDoubleFunction<ItemStack> getDissipationPercent;

        private ToDoubleFunction<ItemStack> getRefractionPercent;

        public CapabilityProtection.LaserItemStackBuilder getDissipationPercent(ToDoubleFunction<ItemStack> getDissipationPercent) {
            this.getDissipationPercent = getDissipationPercent;
            return this;
        }

        public CapabilityProtection.LaserItemStackBuilder getRefractionPercent(ToDoubleFunction<ItemStack> getRefractionPercent) {
            this.getRefractionPercent = getRefractionPercent;
            return this;
        }

        public ILaserDissipation getCapability(ItemStack instance) {
            return new ILaserDissipation() {

                public double getDissipationPercent() {
                    return LaserItemStackBuilder.this.getDissipationPercent == null ? 0.0 : LaserItemStackBuilder.this.getDissipationPercent.applyAsDouble(instance);
                }

                public double getRefractionPercent() {
                    return LaserItemStackBuilder.this.getRefractionPercent == null ? 0.0 : LaserItemStackBuilder.this.getRefractionPercent.applyAsDouble(instance);
                }
            };
        }

        public Capability<ILaserDissipation> getCapabilityKey() {
            return Capabilities.LASER_DISSIPATION;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:laser_protection_item");
        }
    }

    public static class RadiationItemStackBuilder extends CapabilityBuilderForge<ItemStack, IRadiationShielding> {

        private ToDoubleFunction<ItemStack> getRadiationShielding;

        public CapabilityProtection.RadiationItemStackBuilder getRadiationShielding(ToDoubleFunction<ItemStack> getRadiationShielding) {
            this.getRadiationShielding = getRadiationShielding;
            return this;
        }

        public IRadiationShielding getCapability(ItemStack instance) {
            return () -> this.getRadiationShielding == null ? 0.0 : this.getRadiationShielding.applyAsDouble(instance);
        }

        public Capability<IRadiationShielding> getCapabilityKey() {
            return Capabilities.RADIATION_SHIELDING;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:radiation_protection_item");
        }
    }
}