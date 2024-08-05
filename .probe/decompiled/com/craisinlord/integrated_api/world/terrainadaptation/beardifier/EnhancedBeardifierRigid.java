package com.craisinlord.integrated_api.world.terrainadaptation.beardifier;

import com.craisinlord.integrated_api.world.terrainadaptation.EnhancedTerrainAdaptation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public record EnhancedBeardifierRigid(BoundingBox pieceBoundingBox, EnhancedTerrainAdaptation pieceTerrainAdaptation, int pieceGroundLevelDelta) {
}