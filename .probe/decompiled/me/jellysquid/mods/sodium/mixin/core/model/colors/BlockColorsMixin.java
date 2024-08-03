package me.jellysquid.mods.sodium.mixin.core.model.colors;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMaps;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import it.unimi.dsi.fastutil.objects.ReferenceSets;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.model.color.interop.BlockColorsExtended;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ BlockColors.class })
public class BlockColorsMixin implements BlockColorsExtended {

    @Unique
    private final Reference2ReferenceMap<Block, BlockColor> blocksToColor = new Reference2ReferenceOpenHashMap();

    @Unique
    private final ReferenceSet<Block> overridenBlocks = new ReferenceOpenHashSet();

    @Inject(method = { "register" }, at = { @At("HEAD") })
    private void preRegisterColorProvider(BlockColor provider, Block[] blocks, CallbackInfo ci) {
        if (provider != null) {
            synchronized (this.blocksToColor) {
                for (Block block : blocks) {
                    if (this.blocksToColor.put(block, provider) != null) {
                        this.overridenBlocks.add(block);
                        SodiumClientMod.logger().info("Block {} had its color provider replaced and will not use per-vertex coloring", BuiltInRegistries.BLOCK.getKey(block));
                    }
                }
            }
        }
    }

    @Override
    public Reference2ReferenceMap<Block, BlockColor> sodium$getProviders() {
        return Reference2ReferenceMaps.unmodifiable(this.blocksToColor);
    }

    @Override
    public ReferenceSet<Block> embeddium$getOverridenVanillaBlocks() {
        return ReferenceSets.unmodifiable(this.overridenBlocks);
    }
}