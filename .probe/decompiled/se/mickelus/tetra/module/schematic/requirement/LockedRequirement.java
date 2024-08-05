package se.mickelus.tetra.module.schematic.requirement;

import java.util.Arrays;
import net.minecraft.resources.ResourceLocation;
import se.mickelus.tetra.module.schematic.CraftingContext;

public class LockedRequirement implements CraftingRequirement {

    public ResourceLocation key;

    @Override
    public boolean test(CraftingContext context) {
        return Arrays.asList(context.unlocks).contains(this.key);
    }
}