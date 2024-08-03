package io.github.apace100.origins.power;

import io.github.edwinmindcraft.origins.common.condition.OriginCondition;
import io.github.edwinmindcraft.origins.common.registry.OriginRegisters;
import net.minecraftforge.registries.RegistryObject;

public class OriginsEntityConditions {

    public static final RegistryObject<OriginCondition> ORIGIN = OriginRegisters.ENTITY_CONDITIONS.register("origin", OriginCondition::new);

    public static void register() {
    }
}