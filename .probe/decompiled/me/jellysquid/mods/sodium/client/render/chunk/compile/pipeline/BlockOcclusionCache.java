package me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline;

import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockOcclusionCache {

    private static final byte UNCACHED_VALUE = 127;

    private final Object2ByteLinkedOpenHashMap<BlockOcclusionCache.CachedOcclusionShapeTest> map;

    private final BlockOcclusionCache.CachedOcclusionShapeTest cachedTest = new BlockOcclusionCache.CachedOcclusionShapeTest();

    private final BlockPos.MutableBlockPos cpos = new BlockPos.MutableBlockPos();

    public BlockOcclusionCache() {
        this.map = new Object2ByteLinkedOpenHashMap(2048, 0.5F);
        this.map.defaultReturnValue((byte) 127);
    }

    public boolean shouldDrawSide(BlockState selfState, BlockGetter view, BlockPos pos, Direction facing) {
        BlockPos.MutableBlockPos adjPos = this.cpos;
        adjPos.set(pos.m_123341_() + facing.getStepX(), pos.m_123342_() + facing.getStepY(), pos.m_123343_() + facing.getStepZ());
        BlockState adjState = view.getBlockState(adjPos);
        if (!selfState.m_60719_(adjState, facing) && (!adjState.hidesNeighborFace(view, pos, selfState, facing.getOpposite()) || !selfState.supportsExternalFaceHiding())) {
            if (adjState.m_60815_()) {
                VoxelShape selfShape = selfState.m_60655_(view, pos, facing);
                VoxelShape adjShape = adjState.m_60655_(view, adjPos, facing.getOpposite());
                if (selfShape == Shapes.block() && adjShape == Shapes.block()) {
                    return false;
                } else {
                    return selfShape.isEmpty() ? true : this.calculate(selfShape, adjShape);
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean calculate(VoxelShape selfShape, VoxelShape adjShape) {
        BlockOcclusionCache.CachedOcclusionShapeTest cache = this.cachedTest;
        cache.a = selfShape;
        cache.b = adjShape;
        cache.updateHash();
        byte cached = this.map.getByte(cache);
        if (cached != 127) {
            return cached == 1;
        } else {
            boolean ret = Shapes.joinIsNotEmpty(selfShape, adjShape, BooleanOp.ONLY_FIRST);
            this.map.put(cache.copy(), (byte) (ret ? 1 : 0));
            if (this.map.size() > 2048) {
                this.map.removeFirstByte();
            }
            return ret;
        }
    }

    private static final class CachedOcclusionShapeTest {

        private VoxelShape a;

        private VoxelShape b;

        private int hashCode;

        private CachedOcclusionShapeTest() {
        }

        private CachedOcclusionShapeTest(VoxelShape a, VoxelShape b, int hashCode) {
            this.a = a;
            this.b = b;
            this.hashCode = hashCode;
        }

        public void updateHash() {
            int result = System.identityHashCode(this.a);
            result = 31 * result + System.identityHashCode(this.b);
            this.hashCode = result;
        }

        public BlockOcclusionCache.CachedOcclusionShapeTest copy() {
            return new BlockOcclusionCache.CachedOcclusionShapeTest(this.a, this.b, this.hashCode);
        }

        public boolean equals(Object o) {
            return !(o instanceof BlockOcclusionCache.CachedOcclusionShapeTest that) ? false : this.a == that.a && this.b == that.b;
        }

        public int hashCode() {
            return this.hashCode;
        }
    }
}