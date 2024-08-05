package net.mehvahdjukaar.supplementaries.mixins;

import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.mehvahdjukaar.supplementaries.common.items.SliceMapItem;
import net.mehvahdjukaar.supplementaries.common.misc.ColoredMapHandler;
import net.mehvahdjukaar.supplementaries.common.misc.MapLightHandler;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ MapItem.class })
public abstract class MapItemMixin {

    @ModifyExpressionValue(method = { "update" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/dimension/DimensionType;hasCeiling()Z") })
    public boolean removeCeiling(boolean original, @Share("heightLock") LocalIntRef height) {
        return original && height.get() != Integer.MAX_VALUE && CommonConfigs.Tools.SLICE_MAP_ENABLED.get() ? false : original;
    }

    @Inject(method = { "update" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/dimension/DimensionType;hasCeiling()Z", shift = Shift.BEFORE, ordinal = 0) }, require = 1, cancellable = true)
    public void checkHeightLock(Level level, Entity viewer, MapItemSavedData data, CallbackInfo ci, @Local(ordinal = 5) LocalIntRef range, @Share("customColorMap") LocalRef<Map<Vector2i, Pair<BlockPos, Multiset<Block>>>> colorMap, @Share("customLightMap") LocalRef<Map<Vector2i, List<Vector2i>>> lightMap, @Share("heightLock") LocalIntRef height) {
        int mapHeight = SliceMapItem.getMapHeight(data);
        height.set(mapHeight);
        colorMap.set(CommonConfigs.Tweaks.TINTED_MAP.get() ? new HashMap() : null);
        lightMap.set(MapLightHandler.isActive() ? new HashMap() : null);
        if (mapHeight != Integer.MAX_VALUE) {
            if (!SliceMapItem.canPlayerSee(mapHeight, viewer)) {
                ci.cancel();
            }
            range.set((int) ((double) range.get() * SliceMapItem.getRangeMultiplier()));
        }
    }

    @ModifyExpressionValue(method = { "update" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/LevelChunk;getHeight(Lnet/minecraft/world/level/levelgen/Heightmap$Types;II)I") })
    public int modifySampleHeight(int original, @Share("heightLock") LocalIntRef height) {
        int h = height.get();
        return h != Integer.MAX_VALUE ? Math.min(original, h) : original;
    }

    @WrapOperation(method = { "update" }, at = { @At(value = "INVOKE", ordinal = 3, target = "Lnet/minecraft/world/level/block/state/BlockState;getMapColor(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/MapColor;") })
    public MapColor removeXrayAndAddAccurateColor(BlockState instance, BlockGetter level, BlockPos pos, Operation<MapColor> operation, @Local Level l, @Local LevelChunk chunk, @Local(ordinal = 14) int w, @Local(ordinal = 0) BlockState state, @Local(ordinal = 6) int k1, @Local(ordinal = 7) int l1, @Share("customColorMap") LocalRef<Map<Vector2i, Pair<BlockPos, Multiset<Block>>>> colorMap, @Share("customLightMap") LocalRef<Map<Vector2i, List<Vector2i>>> lightMap, @Share("heightLock") LocalIntRef height) {
        MapColor cutoffColor = null;
        if (height.get() != Integer.MAX_VALUE && height.get() <= w) {
            cutoffColor = SliceMapItem.getCutoffColor(pos, chunk);
        }
        if (lightMap.get() != null) {
            int brightness = cutoffColor != null && cutoffColor != MapColor.NONE ? 15 : l.m_45517_(LightLayer.BLOCK, pos.above());
            int sky = l.dimensionType().hasSkyLight() ? l.m_45517_(LightLayer.SKY, pos.above()) : 15;
            ((List) ((Map) lightMap.get()).computeIfAbsent(new Vector2i(k1, l1), p -> new ArrayList())).add(new Vector2i(brightness, sky));
        }
        if (cutoffColor != null) {
            return cutoffColor;
        } else {
            if (colorMap.get() != null) {
                ((Multiset) ((Pair) ((Map) colorMap.get()).computeIfAbsent(new Vector2i(k1, l1), p -> Pair.of(pos, LinkedHashMultiset.create()))).getSecond()).add(state.m_60734_());
            }
            return (MapColor) operation.call(new Object[] { instance, level, pos });
        }
    }

    @ModifyExpressionValue(method = { "update" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;updateColor(IIB)Z") })
    public boolean updateCustomColor(boolean original, Level level, Entity viewer, MapItemSavedData data, @Local(ordinal = 6) int x, @Local(ordinal = 7) int z, @Share("customColorMap") LocalRef<Map<Vector2i, Pair<BlockPos, Multiset<Block>>>> colorMap, @Share("customLightMap") LocalRef<Map<Vector2i, List<Vector2i>>> lightMap) {
        if (colorMap.get() != null) {
            Pair<BlockPos, Multiset<Block>> l = (Pair<BlockPos, Multiset<Block>>) ((Map) colorMap.get()).get(new Vector2i(x, z));
            if (l != null) {
                Block block = (Block) Iterables.getFirst(Multisets.copyHighestCountFirst((Multiset) l.getSecond()), Blocks.AIR);
                ColoredMapHandler.ColorData c = ColoredMapHandler.getColorData(data);
                c.markColored(x, z, block, level, (BlockPos) l.getFirst(), data);
            }
        }
        if (lightMap.get() != null) {
            if (lightMap.get() == null) {
                lightMap.set(new HashMap());
            }
            List<Vector2i> l = (List<Vector2i>) ((Map) lightMap.get()).get(new Vector2i(x, z));
            if (l != null) {
                int blockLight = (int) l.stream().mapToDouble(v -> (double) v.x).average().orElse(0.0);
                int skyLight = (int) l.stream().mapToDouble(v -> (double) v.y).average().orElse(0.0);
                MapLightHandler.LightData c = MapLightHandler.getLightData(data);
                c.setLightLevel(x, z, blockLight, skyLight, data);
            }
        }
        return original;
    }
}