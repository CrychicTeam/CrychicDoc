package malte0811.ferritecore.impl;

import com.google.common.base.Suppliers;
import it.unimi.dsi.fastutil.booleans.BooleanArrays;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import malte0811.ferritecore.ducks.BlockStateCacheAccess;
import malte0811.ferritecore.hash.ArrayVoxelShapeHash;
import malte0811.ferritecore.hash.VoxelShapeHash;
import malte0811.ferritecore.mixin.blockstatecache.ArrayVSAccess;
import malte0811.ferritecore.mixin.blockstatecache.SliceShapeAccess;
import malte0811.ferritecore.util.Constants;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.ArrayVoxelShape;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

public class BlockStateCacheImpl {

    public static final Map<ArrayVSAccess, ArrayVSAccess> CACHE_COLLIDE = new Object2ObjectOpenCustomHashMap(ArrayVoxelShapeHash.INSTANCE);

    public static final Map<VoxelShape, Pair<VoxelShape, VoxelShape[]>> CACHE_PROJECT = new Object2ObjectOpenCustomHashMap(VoxelShapeHash.INSTANCE);

    public static final Map<boolean[], boolean[]> CACHE_FACE_STURDY = new Object2ObjectOpenCustomHashMap(BooleanArrays.HASH_STRATEGY);

    private static final Supplier<Function<BlockBehaviour.BlockStateBase, BlockStateCacheAccess>> GET_CACHE = Suppliers.memoize(() -> {
        try {
            String cacheName = Constants.PLATFORM_HOOKS.computeBlockstateCacheFieldName();
            Field cacheField = BlockBehaviour.BlockStateBase.class.getDeclaredField(cacheName);
            cacheField.setAccessible(true);
            MethodHandle getter = MethodHandles.lookup().unreflectGetter(cacheField);
            return state -> {
                try {
                    return (BlockStateCacheAccess) getter.invoke(state);
                } catch (Throwable var3x) {
                    throw new RuntimeException(var3x);
                }
            };
        } catch (IllegalAccessException | NoSuchFieldException var3) {
            throw new RuntimeException(var3);
        }
    });

    private static final ThreadLocal<BlockStateCacheAccess> LAST_CACHE = new ThreadLocal();

    public static void deduplicateCachePre(BlockBehaviour.BlockStateBase state) {
        LAST_CACHE.set((BlockStateCacheAccess) ((Function) GET_CACHE.get()).apply(state));
    }

    public static void deduplicateCachePost(BlockBehaviour.BlockStateBase state) {
        BlockStateCacheAccess newCache = (BlockStateCacheAccess) ((Function) GET_CACHE.get()).apply(state);
        if (newCache != null) {
            BlockStateCacheAccess oldCache = (BlockStateCacheAccess) LAST_CACHE.get();
            deduplicateCollisionShape(newCache, oldCache);
            deduplicateRenderShapes(newCache, oldCache);
            deduplicateFaceSturdyArray(newCache, oldCache);
            LAST_CACHE.set(null);
        }
    }

    private static void deduplicateCollisionShape(BlockStateCacheAccess newCache, @Nullable BlockStateCacheAccess oldCache) {
        VoxelShape dedupedCollisionShape;
        if (oldCache != null && VoxelShapeHash.INSTANCE.equals(oldCache.getCollisionShape(), newCache.getCollisionShape())) {
            dedupedCollisionShape = oldCache.getCollisionShape();
        } else {
            dedupedCollisionShape = newCache.getCollisionShape();
            if (dedupedCollisionShape instanceof ArrayVSAccess access) {
                dedupedCollisionShape = (VoxelShape) CACHE_COLLIDE.computeIfAbsent(access, Function.identity());
            }
        }
        replaceInternals(dedupedCollisionShape, newCache.getCollisionShape());
        newCache.setCollisionShape(dedupedCollisionShape);
    }

    private static void deduplicateRenderShapes(BlockStateCacheAccess newCache, @Nullable BlockStateCacheAccess oldCache) {
        VoxelShape newRenderShape = getRenderShape(newCache.getOcclusionShapes());
        if (newRenderShape != null) {
            Pair<VoxelShape, VoxelShape[]> dedupedRenderShapes = null;
            if (oldCache != null) {
                VoxelShape oldRenderShape = getRenderShape(oldCache.getOcclusionShapes());
                if (VoxelShapeHash.INSTANCE.equals(newRenderShape, oldRenderShape)) {
                    dedupedRenderShapes = Pair.of(oldRenderShape, oldCache.getOcclusionShapes());
                }
            }
            if (dedupedRenderShapes == null) {
                Pair<VoxelShape, VoxelShape[]> newPair = Pair.of(newRenderShape, newCache.getOcclusionShapes());
                dedupedRenderShapes = (Pair<VoxelShape, VoxelShape[]>) CACHE_PROJECT.putIfAbsent(newRenderShape, newPair);
                if (dedupedRenderShapes == null) {
                    dedupedRenderShapes = newPair;
                }
            }
            replaceInternals((VoxelShape) dedupedRenderShapes.getLeft(), newRenderShape);
            newCache.setOcclusionShapes((VoxelShape[]) dedupedRenderShapes.getRight());
        }
    }

    private static void deduplicateFaceSturdyArray(BlockStateCacheAccess newCache, @Nullable BlockStateCacheAccess oldCache) {
        boolean[] dedupedFaceSturdy;
        if (oldCache != null && Arrays.equals(oldCache.getFaceSturdy(), newCache.getFaceSturdy())) {
            dedupedFaceSturdy = oldCache.getFaceSturdy();
        } else {
            dedupedFaceSturdy = (boolean[]) CACHE_FACE_STURDY.computeIfAbsent(newCache.getFaceSturdy(), Function.identity());
        }
        newCache.setFaceSturdy(dedupedFaceSturdy);
    }

    private static void replaceInternals(VoxelShape toKeep, VoxelShape toReplace) {
        if (toKeep instanceof ArrayVoxelShape keepArray && toReplace instanceof ArrayVoxelShape replaceArray) {
            replaceInternals(keepArray, replaceArray);
        }
    }

    public static void replaceInternals(ArrayVoxelShape toKeep, ArrayVoxelShape toReplace) {
        if (toKeep != toReplace) {
            ArrayVSAccess toReplaceAccess = (ArrayVSAccess) toReplace;
            ArrayVSAccess toKeepAccess = (ArrayVSAccess) toKeep;
            toReplaceAccess.setXPoints(toKeepAccess.getXPoints());
            toReplaceAccess.setYPoints(toKeepAccess.getYPoints());
            toReplaceAccess.setZPoints(toKeepAccess.getZPoints());
            toReplaceAccess.setFaces(toKeepAccess.getFaces());
            toReplaceAccess.setShape(toKeepAccess.getShape());
        }
    }

    @Nullable
    private static VoxelShape getRenderShape(@Nullable VoxelShape[] projected) {
        if (projected != null) {
            for (VoxelShape side : projected) {
                if (side instanceof SliceShapeAccess slice) {
                    return slice.getDelegate();
                }
            }
        }
        return null;
    }
}