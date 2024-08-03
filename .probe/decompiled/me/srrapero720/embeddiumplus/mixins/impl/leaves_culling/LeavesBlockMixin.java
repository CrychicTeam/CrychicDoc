package me.srrapero720.embeddiumplus.mixins.impl.leaves_culling;

import me.srrapero720.embeddiumplus.foundation.leaves_culling.ICulleableLeaves;
import me.srrapero720.embeddiumplus.foundation.leaves_culling.LeavesCulling;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ LeavesBlock.class })
public class LeavesBlockMixin extends Block implements ICulleableLeaves {

    @Unique
    private ResourceLocation embPlus$resLoc;

    @Unique
    private int leaves_neighbor;

    public LeavesBlockMixin(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentState, Direction direction) {
        return !(adjacentState.m_60734_() instanceof ICulleableLeaves leaves) ? super.m_6104_(state, adjacentState, direction) : LeavesCulling.should(this.embplus$cast(), this, (LeavesBlock) leaves, leaves) || super.m_6104_(state, adjacentState, direction);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        super.m_6807_(pState, pLevel, pPos, pOldState, pMovedByPiston);
    }

    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, level, pos, neighbor);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        super.m_6810_(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public ResourceLocation embplus$getResourceLocation() {
        return this.embPlus$resLoc != null ? this.embPlus$resLoc : (this.embPlus$resLoc = ForgeRegistries.BLOCKS.getKey(this));
    }

    @Override
    public int embplus$activeNeighbors() {
        return this.leaves_neighbor;
    }

    public LeavesBlock embplus$cast() {
        return (LeavesBlock) this;
    }
}