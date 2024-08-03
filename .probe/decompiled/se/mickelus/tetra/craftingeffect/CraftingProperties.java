package se.mickelus.tetra.craftingeffect;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CraftingProperties {

    public int stabilityOffset = 0;

    public float stabilityMultiplier = 1.0F;

    public float destabilizationOffset = 0.0F;

    public int xpOffset = 0;

    public float xpMultiplier = 1.0F;

    public static CraftingProperties merge(CraftingProperties a, CraftingProperties b) {
        CraftingProperties result = new CraftingProperties();
        result.stabilityOffset = a.stabilityOffset + b.stabilityOffset;
        result.stabilityMultiplier = a.stabilityMultiplier * b.stabilityMultiplier;
        result.destabilizationOffset = a.destabilizationOffset + b.destabilizationOffset;
        result.xpOffset = a.xpOffset + b.xpOffset;
        result.xpMultiplier = a.xpMultiplier * b.xpMultiplier;
        return result;
    }
}