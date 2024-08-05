package net.cristellib.api;

import java.util.Set;
import net.cristellib.CristelLibRegistry;
import net.cristellib.StructureConfig;

public interface CristelLibAPI {

    default void registerConfigs(Set<StructureConfig> sets) {
    }

    default void registerStructureSets(CristelLibRegistry registry) {
    }
}