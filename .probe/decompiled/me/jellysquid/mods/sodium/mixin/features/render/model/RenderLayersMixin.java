package me.jellysquid.mods.sodium.mixin.features.render.model;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import java.util.Map;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ ItemBlockRenderTypes.class })
public class RenderLayersMixin {

    @Mutable
    @Shadow
    @Final
    private static Map<Block, RenderType> TYPE_BY_BLOCK = new Reference2ReferenceOpenHashMap(RenderLayersMixin.TYPE_BY_BLOCK);

    @Mutable
    @Shadow
    @Final
    private static Map<Fluid, RenderType> TYPE_BY_FLUID = new Reference2ReferenceOpenHashMap(RenderLayersMixin.TYPE_BY_FLUID);
}