package com.yungnickyoung.minecraft.yungsapi.world.structure.terrainadaptation.beardifier;

import com.yungnickyoung.minecraft.yungsapi.world.structure.terrainadaptation.EnhancedTerrainAdaptation;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;

public record EnhancedJigsawJunction(JigsawJunction jigsawJunction, EnhancedTerrainAdaptation pieceTerrainAdaptation) {
}