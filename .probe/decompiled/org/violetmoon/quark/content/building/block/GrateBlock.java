package org.violetmoon.quark.content.building.block;

import it.unimi.dsi.fastutil.floats.Float2ObjectArrayMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.api.ICrawlSpaceBlock;
import org.violetmoon.zeta.block.SimpleFluidloggedBlock;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.RenderLayerRegistry;

public class GrateBlock extends ZetaBlock implements SimpleFluidloggedBlock, ICrawlSpaceBlock {

    private static final VoxelShape TRUE_SHAPE = m_49796_(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);

    private static final Float2ObjectArrayMap<VoxelShape> WALK_BLOCK_CACHE = new Float2ObjectArrayMap();

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final BooleanProperty LAVALOGGED = BooleanProperty.create("lavalogged");

    public GrateBlock(@Nullable ZetaModule module) {
        super("grate", module, BlockBehaviour.Properties.of().strength(5.0F, 10.0F).sound(SoundType.METAL).isValidSpawn((what, huh, idk, hoh) -> false).lightLevel(state -> state.m_61143_(LAVALOGGED) ? 15 : 0).noOcclusion());
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false)).m_61124_(LAVALOGGED, false));
        if (module != null) {
            module.zeta.renderLayerRegistry.put(this, RenderLayerRegistry.Layer.CUTOUT);
            this.setCreativeTab(CreativeModeTabs.FUNCTIONAL_BLOCKS, Blocks.CHAIN, false);
        }
    }

    private static VoxelShape createNewBox(double stepHeight) {
        return m_49796_(0.0, 15.0, 0.0, 16.0, 17.0 + 16.0 * stepHeight, 16.0);
    }

    @Override
    public boolean hasDynamicShape() {
        return true;
    }

    @Override
    public boolean canCrawl(Level level, BlockState state, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public double crawlHeight(Level level, BlockState state, BlockPos pos, Direction direction) {
        return 0.0;
    }

    @Override
    public boolean isLog(ServerPlayer sp, BlockState state, BlockPos pos, Direction direction) {
        return false;
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return TRUE_SHAPE;
    }

    private static VoxelShape getCachedShape(float stepHeight) {
        return (VoxelShape) WALK_BLOCK_CACHE.computeIfAbsent(stepHeight, GrateBlock::createNewBox);
    }

    @Override
    public boolean collisionExtendsVerticallyZeta(BlockState state, BlockGetter level, BlockPos pos, Entity collidingEntity) {
        if (!(collidingEntity instanceof Animal) && !(collidingEntity instanceof WaterAnimal)) {
            return false;
        } else {
            if (collidingEntity instanceof Animal animal && animal.m_21524_() != null) {
                return false;
            }
            if (collidingEntity instanceof WaterAnimal waterAnimal && waterAnimal.m_21524_() != null) {
                return false;
            }
            return true;
        }
    }

    @NotNull
    @Override
    public VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Entity entity = context instanceof EntityCollisionContext ? ((EntityCollisionContext) context).getEntity() : null;
        if (entity == null) {
            return TRUE_SHAPE;
        } else if (!(entity instanceof ItemEntity) && !(entity instanceof ExperienceOrb)) {
            boolean preventedType;
            boolean var10000;
            label55: {
                preventedType = entity instanceof Animal || entity instanceof WaterAnimal;
                if (entity instanceof Animal animal && animal.m_21524_() != null || entity instanceof WaterAnimal waterAnimal && waterAnimal.m_21524_() != null) {
                    var10000 = true;
                    break label55;
                }
                var10000 = false;
            }
            boolean leashed = var10000;
            boolean onGrate = world.getBlockState(entity.blockPosition().offset(0, -1, 0)).m_60734_() instanceof GrateBlock;
            return preventedType && !leashed && !onGrate ? getCachedShape(entity.getStepHeight()) : TRUE_SHAPE;
        } else {
            return Shapes.empty();
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Fluid fluidAt = context.m_43725_().getFluidState(context.getClickedPos()).getType();
        BlockState state = this.m_49966_();
        return this.acceptsFluid(fluidAt) ? this.withFluid(state, fluidAt) : state;
    }

    @Override
    public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull PathComputationType path) {
        return false;
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos) {
        return this.fluidContained(state) == Fluids.EMPTY;
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block updatedBlock, @NotNull BlockPos neighbor, boolean isMoving) {
        super.m_6861_(state, level, pos, updatedBlock, neighbor, isMoving);
        if (!pos.below().equals(neighbor)) {
            BlockState neighborState = level.getBlockState(neighbor);
            if (neighborState.m_60819_().is(FluidTags.WATER) && this.fluidContained(state).isSame(Fluids.LAVA)) {
                level.m_46961_(pos, true);
                level.setBlock(pos, ForgeEventFactory.fireFluidPlaceBlockEvent(level, pos, neighbor, Blocks.OBSIDIAN.defaultBlockState()), 3);
                level.m_46796_(1501, pos, 0);
            }
        }
    }

    @NotNull
    @Override
    public BlockState updateShape(@NotNull BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos facingPos) {
        if ((Boolean) state.m_61143_(LAVALOGGED) && (Boolean) state.m_61143_(WATERLOGGED)) {
            state = this.withFluid(state, Fluids.WATER);
        }
        Fluid fluid = this.fluidContained(state);
        if (fluid != Fluids.EMPTY) {
            level.scheduleTick(pos, fluid, fluid.getTickDelay(level));
        }
        return super.m_7417_(state, facing, facingState, level, pos, facingPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, LAVALOGGED);
    }

    @NotNull
    @Override
    public FluidState getFluidState(@NotNull BlockState state) {
        FluidState contained = this.fluidContained(state).defaultFluidState();
        if (contained.m_61138_(BlockStateProperties.FALLING)) {
            contained = (FluidState) contained.m_61124_(BlockStateProperties.FALLING, false);
        }
        return contained;
    }

    @Override
    public boolean acceptsFluid(@NotNull Fluid fluid) {
        return fluid == Fluids.WATER || fluid == Fluids.LAVA;
    }

    @NotNull
    @Override
    public BlockState withFluid(@NotNull BlockState state, @NotNull Fluid fluid) {
        return (BlockState) ((BlockState) state.m_61124_(WATERLOGGED, fluid == Fluids.WATER)).m_61124_(LAVALOGGED, fluid == Fluids.LAVA);
    }

    @NotNull
    @Override
    public Fluid fluidContained(@NotNull BlockState state) {
        if ((Boolean) state.m_61143_(WATERLOGGED)) {
            return Fluids.WATER;
        } else {
            return (Fluid) (state.m_61143_(LAVALOGGED) ? Fluids.LAVA : Fluids.EMPTY);
        }
    }
}