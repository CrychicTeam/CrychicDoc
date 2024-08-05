package com.mna.rituals;

import com.mna.api.rituals.RitualBlockPos;
import com.mna.recipes.rituals.RitualRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;

public class MatchedRitual {

    private NonNullList<RitualBlockPos> matchedPositions;

    private RitualRecipe ritual;

    private BlockPos center;

    public MatchedRitual(NonNullList<RitualBlockPos> positions, RitualRecipe ritual, BlockPos center) {
        this.matchedPositions = positions;
        this.ritual = ritual;
        this.center = center;
    }

    public RitualRecipe getRitual() {
        return this.ritual;
    }

    public NonNullList<RitualBlockPos> getPositions() {
        return this.matchedPositions;
    }

    public BlockPos getCenter() {
        return this.center;
    }
}