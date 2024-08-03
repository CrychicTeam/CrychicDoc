package vazkii.patchouli.common.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.common.util.RotationUtil;

public class SimulateResultImpl implements IMultiblock.SimulateResult {

    private final BlockPos worldPosition;

    private final IStateMatcher stateMatcher;

    @Nullable
    private final Character character;

    public SimulateResultImpl(BlockPos worldPosition, IStateMatcher stateMatcher, @Nullable Character character) {
        this.worldPosition = worldPosition;
        this.stateMatcher = stateMatcher;
        this.character = character;
    }

    @Override
    public BlockPos getWorldPosition() {
        return this.worldPosition;
    }

    @Override
    public IStateMatcher getStateMatcher() {
        return this.stateMatcher;
    }

    @Nullable
    @Override
    public Character getCharacter() {
        return this.character;
    }

    @Override
    public boolean test(Level world, Rotation rotation) {
        BlockState state = world.getBlockState(this.getWorldPosition()).m_60717_(RotationUtil.fixHorizontal(rotation));
        return this.getStateMatcher().getStatePredicate().test(world, this.getWorldPosition(), state);
    }
}