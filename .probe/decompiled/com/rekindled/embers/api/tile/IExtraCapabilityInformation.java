package com.rekindled.embers.api.tile;

import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.capabilities.Capability;

public interface IExtraCapabilityInformation {

    default boolean hasCapabilityDescription(Capability<?> capability) {
        return false;
    }

    default void addCapabilityDescription(List<Component> strings, Capability<?> capability, Direction facing) {
    }

    default void addOtherDescription(List<Component> strings, Direction facing) {
    }

    static Component formatCapability(IExtraCapabilityInformation.EnumIOType ioType, String type, Component filter) {
        Component typeString = filter == null ? Component.translatable(type) : Component.translatable("embers.tooltip.goggles.filter", Component.translatable(type), filter);
        switch(ioType) {
            case NONE:
                return null;
            case INPUT:
                return Component.translatable("embers.tooltip.goggles.input", typeString);
            case OUTPUT:
                return Component.translatable("embers.tooltip.goggles.output", typeString);
            default:
                return Component.translatable("embers.tooltip.goggles.storage", typeString);
        }
    }

    public static enum EnumIOType {

        NONE, INPUT, OUTPUT, BOTH
    }
}