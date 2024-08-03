package se.mickelus.tetra.module.schematic.requirement;

import se.mickelus.tetra.module.schematic.CraftingContext;

public interface CraftingRequirement {

    CraftingRequirement any = ctx -> true;

    boolean test(CraftingContext var1);
}