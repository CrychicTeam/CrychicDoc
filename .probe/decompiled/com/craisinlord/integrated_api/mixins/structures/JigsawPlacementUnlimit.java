package com.craisinlord.integrated_api.mixins.structures;

import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = { JigsawPlacement.class }, priority = 900)
public class JigsawPlacementUnlimit {

    @ModifyConstant(method = { "generateJigsaw" }, constant = { @Constant(intValue = 128) }, require = 0)
    private static int changeMaxGenDistance(int value) {
        return 512;
    }
}