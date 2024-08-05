package se.mickelus.tetra.module.schematic.requirement;

import java.util.Arrays;
import se.mickelus.tetra.module.schematic.CraftingContext;

public class AndRequirement implements CraftingRequirement {

    CraftingRequirement[] requirements;

    @Override
    public boolean test(CraftingContext context) {
        return Arrays.stream(this.requirements).allMatch(requirement -> requirement.test(context));
    }
}