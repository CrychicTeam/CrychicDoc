package com.craisinlord.integrated_api.mixins.blocks;

import net.minecraft.client.renderer.blockentity.StructureBlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = { StructureBlockRenderer.class }, priority = 900)
public class StructureBlockRenderMixin {

    @ModifyConstant(method = { "getViewDistance" }, constant = { @Constant(intValue = 96) }, require = 0)
    public int getRenderDistance(int value) {
        return 256;
    }
}