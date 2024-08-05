package net.minecraft.world.level;

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ClipContext {

    private final Vec3 from;

    private final Vec3 to;

    private final ClipContext.Block block;

    private final ClipContext.Fluid fluid;

    private final CollisionContext collisionContext;

    public ClipContext(Vec3 vec0, Vec3 vec1, ClipContext.Block clipContextBlock2, ClipContext.Fluid clipContextFluid3, Entity entity4) {
        this.from = vec0;
        this.to = vec1;
        this.block = clipContextBlock2;
        this.fluid = clipContextFluid3;
        this.collisionContext = CollisionContext.of(entity4);
    }

    public Vec3 getTo() {
        return this.to;
    }

    public Vec3 getFrom() {
        return this.from;
    }

    public VoxelShape getBlockShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return this.block.get(blockState0, blockGetter1, blockPos2, this.collisionContext);
    }

    public VoxelShape getFluidShape(FluidState fluidState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return this.fluid.canPick(fluidState0) ? fluidState0.getShape(blockGetter1, blockPos2) : Shapes.empty();
    }

    public static enum Block implements ClipContext.ShapeGetter {

        COLLIDER(BlockBehaviour.BlockStateBase::m_60742_), OUTLINE(BlockBehaviour.BlockStateBase::m_60651_), VISUAL(BlockBehaviour.BlockStateBase::m_60771_), FALLDAMAGE_RESETTING((p_201982_, p_201983_, p_201984_, p_201985_) -> p_201982_.m_204336_(BlockTags.FALL_DAMAGE_RESETTING) ? Shapes.block() : Shapes.empty());

        private final ClipContext.ShapeGetter shapeGetter;

        private Block(ClipContext.ShapeGetter p_45712_) {
            this.shapeGetter = p_45712_;
        }

        @Override
        public VoxelShape get(BlockState p_45714_, BlockGetter p_45715_, BlockPos p_45716_, CollisionContext p_45717_) {
            return this.shapeGetter.get(p_45714_, p_45715_, p_45716_, p_45717_);
        }
    }

    public static enum Fluid {

        NONE(p_45736_ -> false), SOURCE_ONLY(FluidState::m_76170_), ANY(p_45734_ -> !p_45734_.isEmpty()), WATER(p_201988_ -> p_201988_.is(FluidTags.WATER));

        private final Predicate<FluidState> canPick;

        private Fluid(Predicate<FluidState> p_45730_) {
            this.canPick = p_45730_;
        }

        public boolean canPick(FluidState p_45732_) {
            return this.canPick.test(p_45732_);
        }
    }

    public interface ShapeGetter {

        VoxelShape get(BlockState var1, BlockGetter var2, BlockPos var3, CollisionContext var4);
    }
}