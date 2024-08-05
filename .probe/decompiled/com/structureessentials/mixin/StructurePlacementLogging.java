package com.structureessentials.mixin;

import com.structureessentials.StructureEssentials;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ JigsawPlacement.Placer.class })
public class StructurePlacementLogging {

    @Redirect(method = { "tryPlacingChildren" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/PoolElementStructurePiece;getBoundingBox()Lnet/minecraft/world/level/levelgen/structure/BoundingBox;"))
    private BoundingBox addLog(PoolElementStructurePiece instance) {
        if (StructureEssentials.config.getCommonConfig().structurePlacementLogging) {
            StructureEssentials.LOGGER.warn("Placing:" + instance);
        }
        return instance.m_73547_();
    }
}