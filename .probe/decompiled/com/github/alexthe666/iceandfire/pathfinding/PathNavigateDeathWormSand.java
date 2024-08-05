package com.github.alexthe666.iceandfire.pathfinding;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class PathNavigateDeathWormSand extends WaterBoundPathNavigation {

    public PathNavigateDeathWormSand(EntityDeathWorm deathworm, Level worldIn) {
        super(deathworm, worldIn);
    }

    @Override
    public boolean canFloat() {
        return this.f_26508_.canFloat();
    }

    @NotNull
    @Override
    protected PathFinder createPathFinder(int i) {
        this.f_26508_ = new NodeProcessorDeathWorm();
        this.f_26508_.setCanPassDoors(true);
        this.f_26508_.setCanFloat(true);
        return new PathFinder(this.f_26508_, i);
    }

    @Override
    protected boolean canUpdatePath() {
        return true;
    }

    @NotNull
    @Override
    protected Vec3 getTempMobPos() {
        return new Vec3(this.f_26494_.m_20185_(), this.f_26494_.m_20186_() + 0.5, this.f_26494_.m_20189_());
    }

    @Override
    protected boolean canMoveDirectly(@NotNull Vec3 start, @NotNull Vec3 end) {
        HitResult raytraceresult = this.f_26495_.m_45547_(new PathNavigateDeathWormSand.CustomRayTraceContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.f_26494_));
        if (raytraceresult.getType() == HitResult.Type.BLOCK) {
            Vec3 vec3i = raytraceresult.getLocation();
            return this.f_26494_.m_9236_().getBlockState(BlockPos.containing(vec3i)).m_204336_(BlockTags.SAND);
        } else {
            return raytraceresult.getType() == HitResult.Type.MISS;
        }
    }

    @Override
    public boolean isStableDestination(@NotNull BlockPos pos) {
        return this.f_26495_.getBlockState(pos).m_60815_();
    }

    public static class CustomRayTraceContext extends ClipContext {

        private final ClipContext.Block blockMode;

        private final CollisionContext context;

        public CustomRayTraceContext(Vec3 startVecIn, Vec3 endVecIn, ClipContext.Block blockModeIn, ClipContext.Fluid fluidModeIn, @Nullable Entity entityIn) {
            super(startVecIn, endVecIn, blockModeIn, fluidModeIn, entityIn);
            this.blockMode = blockModeIn;
            this.context = entityIn == null ? CollisionContext.empty() : CollisionContext.of(entityIn);
        }

        @NotNull
        @Override
        public VoxelShape getBlockShape(BlockState blockState, @NotNull BlockGetter world, @NotNull BlockPos pos) {
            return blockState.m_204336_(BlockTags.SAND) ? Shapes.empty() : this.blockMode.get(blockState, world, pos, this.context);
        }
    }
}