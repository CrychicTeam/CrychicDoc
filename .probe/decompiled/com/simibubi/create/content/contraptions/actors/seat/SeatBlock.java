package com.simibubi.create.content.contraptions.actors.seat;

import com.google.common.base.Optional;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SeatBlock extends Block implements ProperWaterloggedBlock {

    protected final DyeColor color;

    public SeatBlock(BlockBehaviour.Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
        this.m_49959_((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(WATERLOGGED));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.withWater(super.getStateForPlacement(pContext), pContext);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        this.updateWater(pLevel, pState, pCurrentPos);
        return pState;
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return this.fluidState(pState);
    }

    @Override
    public void fallOn(Level level0, BlockState blockState1, BlockPos blockPos2, Entity entity3, float float4) {
        super.fallOn(level0, blockState1, blockPos2, entity3, float4 * 0.5F);
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter reader, Entity entity) {
        BlockPos pos = entity.blockPosition();
        if (!(entity instanceof Player) && entity instanceof LivingEntity && canBePickedUp(entity) && !isSeatOccupied(entity.level(), pos)) {
            if (reader.getBlockState(pos).m_60734_() == this) {
                sitDown(entity.level(), pos, entity);
            }
        } else if (entity.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(reader, entity);
        } else {
            Vec3 vec3 = entity.getDeltaMovement();
            if (vec3.y < 0.0) {
                double d0 = entity instanceof LivingEntity ? 1.0 : 0.8;
                entity.setDeltaMovement(vec3.x, -vec3.y * 0.66F * d0, vec3.z);
            }
        }
    }

    public BlockPathTypes getBlockPathType(BlockState state, BlockGetter world, BlockPos pos, @Nullable Mob entity) {
        return BlockPathTypes.RAIL;
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return AllShapes.SEAT;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, BlockGetter p_220071_2_, BlockPos p_220071_3_, CollisionContext ctx) {
        if (ctx instanceof EntityCollisionContext ecc && ecc.getEntity() instanceof Player) {
            return AllShapes.SEAT_COLLISION_PLAYERS;
        }
        return AllShapes.SEAT_COLLISION;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult p_225533_6_) {
        if (player.m_6144_()) {
            return InteractionResult.PASS;
        } else {
            ItemStack heldItem = player.m_21120_(hand);
            DyeColor color = DyeColor.getColor(heldItem);
            if (color == null || color == this.color) {
                List<SeatEntity> seats = world.m_45976_(SeatEntity.class, new AABB(pos));
                if (!seats.isEmpty()) {
                    SeatEntity seatEntity = (SeatEntity) seats.get(0);
                    List<Entity> passengers = seatEntity.m_20197_();
                    if (!passengers.isEmpty() && passengers.get(0) instanceof Player) {
                        return InteractionResult.PASS;
                    } else {
                        if (!world.isClientSide) {
                            seatEntity.m_20153_();
                            player.m_20329_(seatEntity);
                        }
                        return InteractionResult.SUCCESS;
                    }
                } else if (world.isClientSide) {
                    return InteractionResult.SUCCESS;
                } else {
                    sitDown(world, pos, (Entity) getLeashed(world, player).or(player));
                    return InteractionResult.SUCCESS;
                }
            } else if (world.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                BlockState newState = BlockHelper.copyProperties(state, AllBlocks.SEATS.get(color).getDefaultState());
                world.setBlockAndUpdate(pos, newState);
                return InteractionResult.SUCCESS;
            }
        }
    }

    public static boolean isSeatOccupied(Level world, BlockPos pos) {
        return !world.m_45976_(SeatEntity.class, new AABB(pos)).isEmpty();
    }

    public static Optional<Entity> getLeashed(Level level, Player player) {
        for (Entity e : player.m_9236_().getEntities((Entity) null, player.m_20191_().inflate(10.0), ex -> true)) {
            if (e instanceof Mob mob && mob.getLeashHolder() == player && canBePickedUp(e)) {
                return Optional.of(mob);
            }
        }
        return Optional.absent();
    }

    public static boolean canBePickedUp(Entity passenger) {
        if (passenger instanceof Shulker) {
            return false;
        } else if (passenger instanceof Player) {
            return false;
        } else if (AllTags.AllEntityTags.IGNORE_SEAT.matches(passenger)) {
            return false;
        } else {
            return !AllConfigs.server().logistics.seatHostileMobs.get() && !passenger.getType().getCategory().isFriendly() ? false : passenger instanceof LivingEntity;
        }
    }

    public static void sitDown(Level world, BlockPos pos, Entity entity) {
        if (!world.isClientSide) {
            SeatEntity seat = new SeatEntity(world, pos);
            seat.setPos((double) ((float) pos.m_123341_() + 0.5F), (double) pos.m_123342_(), (double) ((float) pos.m_123343_() + 0.5F));
            world.m_7967_(seat);
            entity.startRiding(seat, true);
            if (entity instanceof TamableAnimal ta) {
                ta.setInSittingPose(true);
            }
        }
    }

    public DyeColor getColor() {
        return this.color;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }
}