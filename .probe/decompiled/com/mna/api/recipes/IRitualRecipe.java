package com.mna.api.recipes;

import com.mna.api.rituals.IRitualReagent;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public interface IRitualRecipe extends IMARecipe {

    int[][] getPattern();

    IRitualReagent[][] getReagents();

    String[] getManaweavePatterns();

    boolean isValid();

    int countRunes();

    int getLowerBound();

    @Nullable
    Direction getMatchedDirection(Level var1, BlockPos var2);

    boolean hasCommand();

    String getCommand();
}