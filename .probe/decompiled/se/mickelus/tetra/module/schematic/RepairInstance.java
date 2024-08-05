package se.mickelus.tetra.module.schematic;

import java.util.Collection;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.tetra.module.ItemModule;

@ParametersAreNonnullByDefault
public record RepairInstance(Collection<RepairDefinition> definitions, @Nullable ItemModule module) {
}