package vazkii.patchouli.common.multiblock;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.common.util.RotationUtil;

public class SparseMultiblock extends AbstractMultiblock {

    private final Map<BlockPos, IStateMatcher> data;

    private final Vec3i size;

    public SparseMultiblock(Map<BlockPos, IStateMatcher> data) {
        Preconditions.checkArgument(!data.isEmpty(), "No data given to sparse multiblock!");
        this.data = ImmutableMap.copyOf(data);
        this.size = this.calculateSize();
    }

    @Override
    public Vec3i getSize() {
        return this.size;
    }

    private Vec3i calculateSize() {
        int minX = this.data.keySet().stream().mapToInt(Vec3i::m_123341_).min().getAsInt();
        int maxX = this.data.keySet().stream().mapToInt(Vec3i::m_123341_).max().getAsInt();
        int minY = this.data.keySet().stream().mapToInt(Vec3i::m_123342_).min().getAsInt();
        int maxY = this.data.keySet().stream().mapToInt(Vec3i::m_123342_).max().getAsInt();
        int minZ = this.data.keySet().stream().mapToInt(Vec3i::m_123343_).min().getAsInt();
        int maxZ = this.data.keySet().stream().mapToInt(Vec3i::m_123343_).max().getAsInt();
        return new Vec3i(maxX - minX + 1, maxY - minY + 1, maxZ - minZ + 1);
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        long ticks = this.world != null ? this.world.getGameTime() : 0L;
        return ((IStateMatcher) this.data.getOrDefault(pos, StateMatcher.AIR)).getDisplayedState(ticks);
    }

    @Override
    public Pair<BlockPos, Collection<IMultiblock.SimulateResult>> simulate(Level world, BlockPos anchor, Rotation rotation, boolean forView) {
        BlockPos disp = forView ? new BlockPos(-this.viewOffX, -this.viewOffY + 1, -this.viewOffZ).rotate(rotation) : new BlockPos(-this.offX, -this.offY, -this.offZ).rotate(rotation);
        BlockPos origin = anchor.offset(disp);
        List<IMultiblock.SimulateResult> ret = new ArrayList();
        for (Entry<BlockPos, IStateMatcher> e : this.data.entrySet()) {
            BlockPos currDisp = ((BlockPos) e.getKey()).rotate(rotation);
            BlockPos actionPos = origin.offset(currDisp);
            ret.add(new SimulateResultImpl(actionPos, (IStateMatcher) e.getValue(), null));
        }
        return Pair.of(origin, ret);
    }

    @Override
    public boolean test(Level world, BlockPos start, int x, int y, int z, Rotation rotation) {
        this.setWorld(world);
        BlockPos checkPos = start.offset(new BlockPos(x, y, z).rotate(rotation));
        BlockState state = world.getBlockState(checkPos).m_60717_(RotationUtil.fixHorizontal(rotation));
        IStateMatcher matcher = (IStateMatcher) this.data.getOrDefault(new BlockPos(x, y, z), StateMatcher.ANY);
        return matcher.getStatePredicate().test(world, checkPos, state);
    }

    @Override
    public int getHeight() {
        return 255;
    }

    @Override
    public int getMinBuildHeight() {
        return 0;
    }
}