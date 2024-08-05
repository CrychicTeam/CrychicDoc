package com.simibubi.create.compat.computercraft;

import com.simibubi.create.compat.Mods;
import com.simibubi.create.compat.computercraft.implementation.ComputerBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import java.util.function.Function;

public class ComputerCraftProxy {

    private static Function<SmartBlockEntity, ? extends AbstractComputerBehaviour> fallbackFactory;

    private static Function<SmartBlockEntity, ? extends AbstractComputerBehaviour> computerFactory;

    public static void register() {
        fallbackFactory = FallbackComputerBehaviour::new;
        Mods.COMPUTERCRAFT.executeIfInstalled(() -> ComputerCraftProxy::registerWithDependency);
    }

    private static void registerWithDependency() {
        computerFactory = ComputerBehaviour::new;
    }

    public static AbstractComputerBehaviour behaviour(SmartBlockEntity sbe) {
        return computerFactory == null ? (AbstractComputerBehaviour) fallbackFactory.apply(sbe) : (AbstractComputerBehaviour) computerFactory.apply(sbe);
    }
}