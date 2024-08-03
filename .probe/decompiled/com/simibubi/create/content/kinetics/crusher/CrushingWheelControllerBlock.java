package com.simibubi.create.content.kinetics.crusher;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CrushingWheelControllerBlock extends DirectionalBlock implements IBE<CrushingWheelControllerBlockEntity> {

    public static final BooleanProperty VALID = BooleanProperty.create("valid");

    public CrushingWheelControllerBlock(BlockBehaviour.Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return false;
    }

    public boolean addRunningEffects(BlockState state, Level world, BlockPos pos, Entity entity) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VALID);
        builder.add(f_52588_);
        super.m_7926_(builder);
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if ((Boolean) state.m_61143_(VALID)) {
            Direction facing = (Direction) state.m_61143_(f_52588_);
            Direction.Axis axis = facing.getAxis();
            this.checkEntityForProcessing(worldIn, pos, entityIn);
            this.withBlockEntityDo(worldIn, pos, be -> {
                if (be.processingEntity == entityIn) {
                    entityIn.makeStuckInBlock(state, new Vec3(axis == Direction.Axis.X ? 0.05F : 0.25, axis == Direction.Axis.Y ? 0.05F : 0.25, axis == Direction.Axis.Z ? 0.05F : 0.25));
                }
            });
        }
    }

    public void checkEntityForProcessing(Level worldIn, BlockPos pos, Entity entityIn) {
        CrushingWheelControllerBlockEntity be = this.getBlockEntity(worldIn, pos);
        if (be != null) {
            if (be.crushingspeed != 0.0F) {
                CompoundTag data = entityIn.getPersistentData();
                if (!data.contains("BypassCrushingWheel") || !pos.equals(NbtUtils.readBlockPos(data.getCompound("BypassCrushingWheel")))) {
                    if (!be.isOccupied()) {
                        boolean isPlayer = entityIn instanceof Player;
                        if (!isPlayer || !((Player) entityIn).isCreative()) {
                            if (!isPlayer || entityIn.level().m_46791_() != Difficulty.PEACEFUL) {
                                be.startCrushing(entityIn);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter worldIn, Entity entityIn) {
        super.m_5548_(worldIn, entityIn);
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        if ((Boolean) stateIn.m_61143_(VALID)) {
            if (rand.nextInt(1) == 0) {
                double d0 = (double) ((float) pos.m_123341_() + rand.nextFloat());
                double d1 = (double) ((float) pos.m_123342_() + rand.nextFloat());
                double d2 = (double) ((float) pos.m_123343_() + rand.nextFloat());
                worldIn.addParticle(ParticleTypes.CRIT, d0, d1, d2, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        this.updateSpeed(stateIn, worldIn, currentPos);
        return stateIn;
    }

    public void updateSpeed(BlockState state, LevelAccessor world, BlockPos pos) {
        this.withBlockEntityDo(world, pos, be -> {
            if (!(Boolean) state.m_61143_(VALID)) {
                if (be.crushingspeed != 0.0F) {
                    be.crushingspeed = 0.0F;
                    be.sendData();
                }
            } else {
                for (Direction d : Iterate.directions) {
                    BlockState neighbour = world.m_8055_(pos.relative(d));
                    if (AllBlocks.CRUSHING_WHEEL.has(neighbour) && neighbour.m_61143_(BlockStateProperties.AXIS) != d.getAxis() && world.m_7702_(pos.relative(d)) instanceof CrushingWheelBlockEntity cwbe) {
                        be.crushingspeed = Math.abs(cwbe.getSpeed() / 50.0F);
                        be.sendData();
                        cwbe.award(AllAdvancements.CRUSHING_WHEEL);
                        if (cwbe.getSpeed() > 255.0F) {
                            cwbe.award(AllAdvancements.CRUSHER_MAXED);
                        }
                        break;
                    }
                }
            }
        });
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        VoxelShape standardShape = AllShapes.CRUSHING_WHEEL_CONTROLLER_COLLISION.get((Direction) state.m_61143_(f_52588_));
        if (!(Boolean) state.m_61143_(VALID)) {
            return standardShape;
        } else if (!(context instanceof EntityCollisionContext)) {
            return standardShape;
        } else {
            Entity entity = ((EntityCollisionContext) context).getEntity();
            if (entity == null) {
                return standardShape;
            } else {
                CompoundTag data = entity.getPersistentData();
                if (data.contains("BypassCrushingWheel") && pos.equals(NbtUtils.readBlockPos(data.getCompound("BypassCrushingWheel"))) && state.m_61143_(f_52588_) != Direction.UP) {
                    return Shapes.empty();
                } else {
                    CrushingWheelControllerBlockEntity be = this.getBlockEntity(worldIn, pos);
                    return be != null && be.processingEntity == entity ? Shapes.empty() : standardShape;
                }
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_155947_() && state.m_60734_() != newState.m_60734_()) {
            this.withBlockEntityDo(worldIn, pos, be -> ItemHelper.dropContents(worldIn, pos, be.inventory));
            worldIn.removeBlockEntity(pos);
        }
    }

    @Override
    public Class<CrushingWheelControllerBlockEntity> getBlockEntityClass() {
        return CrushingWheelControllerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CrushingWheelControllerBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends CrushingWheelControllerBlockEntity>) AllBlockEntityTypes.CRUSHING_WHEEL_CONTROLLER.get();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }
}