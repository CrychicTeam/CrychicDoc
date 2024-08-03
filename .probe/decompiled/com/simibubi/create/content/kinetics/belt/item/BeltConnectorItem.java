package com.simibubi.create.content.kinetics.belt.item;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltPart;
import com.simibubi.create.content.kinetics.belt.BeltSlope;
import com.simibubi.create.content.kinetics.simpleRelays.AbstractSimpleShaftBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class BeltConnectorItem extends BlockItem {

    public BeltConnectorItem(Item.Properties properties) {
        super((Block) AllBlocks.BELT.get(), properties);
    }

    @Override
    public String getDescriptionId() {
        return this.m_41467_();
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player playerEntity = context.getPlayer();
        if (playerEntity != null && playerEntity.m_6144_()) {
            context.getItemInHand().setTag(null);
            return InteractionResult.SUCCESS;
        } else {
            Level world = context.getLevel();
            BlockPos pos = context.getClickedPos();
            boolean validAxis = validateAxis(world, pos);
            if (world.isClientSide) {
                return validAxis ? InteractionResult.SUCCESS : InteractionResult.FAIL;
            } else {
                CompoundTag tag = context.getItemInHand().getOrCreateTag();
                BlockPos firstPulley = null;
                if (tag.contains("FirstPulley")) {
                    firstPulley = NbtUtils.readBlockPos(tag.getCompound("FirstPulley"));
                    if (!validateAxis(world, firstPulley) || !firstPulley.m_123314_(pos, (double) (maxLength() * 2))) {
                        tag.remove("FirstPulley");
                        context.getItemInHand().setTag(tag);
                    }
                }
                if (!validAxis || playerEntity == null) {
                    return InteractionResult.FAIL;
                } else if (!tag.contains("FirstPulley")) {
                    tag.put("FirstPulley", NbtUtils.writeBlockPos(pos));
                    context.getItemInHand().setTag(tag);
                    playerEntity.getCooldowns().addCooldown(this, 5);
                    return InteractionResult.SUCCESS;
                } else if (!canConnect(world, firstPulley, pos)) {
                    return InteractionResult.FAIL;
                } else {
                    if (firstPulley != null && !firstPulley.equals(pos)) {
                        createBelts(world, firstPulley, pos);
                        AllAdvancements.BELT.awardTo(playerEntity);
                        if (!playerEntity.isCreative()) {
                            context.getItemInHand().shrink(1);
                        }
                    }
                    if (!context.getItemInHand().isEmpty()) {
                        context.getItemInHand().setTag(null);
                        playerEntity.getCooldowns().addCooldown(this, 5);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
    }

    public static void createBelts(Level world, BlockPos start, BlockPos end) {
        world.playSound(null, BlockPos.containing(VecHelper.getCenterOf(start.offset(end)).scale(0.5)), SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 0.5F, 1.0F);
        BeltSlope slope = getSlopeBetween(start, end);
        Direction facing = getFacingFromTo(start, end);
        BlockPos diff = end.subtract(start);
        if (diff.m_123341_() == diff.m_123343_()) {
            facing = Direction.get(facing.getAxisDirection(), world.getBlockState(start).m_61143_(BlockStateProperties.AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X);
        }
        List<BlockPos> beltsToCreate = getBeltChainBetween(start, end, slope, facing);
        BlockState beltBlock = AllBlocks.BELT.getDefaultState();
        boolean failed = false;
        for (BlockPos pos : beltsToCreate) {
            BlockState existingBlock = world.getBlockState(pos);
            if (existingBlock.m_60800_(world, pos) == -1.0F) {
                failed = true;
                break;
            }
            BeltPart part = pos.equals(start) ? BeltPart.START : (pos.equals(end) ? BeltPart.END : BeltPart.MIDDLE);
            BlockState shaftState = world.getBlockState(pos);
            boolean pulley = ShaftBlock.isShaft(shaftState);
            if (part == BeltPart.MIDDLE && pulley) {
                part = BeltPart.PULLEY;
            }
            if (pulley && shaftState.m_61143_(AbstractSimpleShaftBlock.AXIS) == Direction.Axis.Y) {
                slope = BeltSlope.SIDEWAYS;
            }
            if (!existingBlock.m_247087_()) {
                world.m_46961_(pos, false);
            }
            KineticBlockEntity.switchToBlockState(world, pos, ProperWaterloggedBlock.withWater(world, (BlockState) ((BlockState) ((BlockState) beltBlock.m_61124_(BeltBlock.SLOPE, slope)).m_61124_(BeltBlock.PART, part)).m_61124_(BeltBlock.HORIZONTAL_FACING, facing), pos));
        }
        if (failed) {
            for (BlockPos pos : beltsToCreate) {
                if (AllBlocks.BELT.has(world.getBlockState(pos))) {
                    world.m_46961_(pos, false);
                }
            }
        }
    }

    private static Direction getFacingFromTo(BlockPos start, BlockPos end) {
        Direction.Axis beltAxis = start.m_123341_() == end.m_123341_() ? Direction.Axis.Z : Direction.Axis.X;
        BlockPos diff = end.subtract(start);
        Direction.AxisDirection axisDirection = Direction.AxisDirection.POSITIVE;
        if (diff.m_123341_() == 0 && diff.m_123343_() == 0) {
            axisDirection = diff.m_123342_() > 0 ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE;
        } else {
            axisDirection = beltAxis.choose(diff.m_123341_(), 0, diff.m_123343_()) > 0 ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE;
        }
        return Direction.get(axisDirection, beltAxis);
    }

    private static BeltSlope getSlopeBetween(BlockPos start, BlockPos end) {
        BlockPos diff = end.subtract(start);
        if (diff.m_123342_() != 0) {
            if (diff.m_123343_() == 0 && diff.m_123341_() == 0) {
                return BeltSlope.VERTICAL;
            } else {
                return diff.m_123342_() > 0 ? BeltSlope.UPWARD : BeltSlope.DOWNWARD;
            }
        } else {
            return BeltSlope.HORIZONTAL;
        }
    }

    private static List<BlockPos> getBeltChainBetween(BlockPos start, BlockPos end, BeltSlope slope, Direction direction) {
        List<BlockPos> positions = new LinkedList();
        int limit = 1000;
        BlockPos current = start;
        do {
            positions.add(current);
            if (slope == BeltSlope.VERTICAL) {
                current = current.above(direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1 : -1);
            } else {
                current = current.relative(direction);
                if (slope != BeltSlope.HORIZONTAL) {
                    current = current.above(slope == BeltSlope.UPWARD ? 1 : -1);
                }
            }
        } while (!current.equals(end) && limit-- > 0);
        positions.add(end);
        return positions;
    }

    public static boolean canConnect(Level world, BlockPos first, BlockPos second) {
        if (!world.isLoaded(first) || !world.isLoaded(second)) {
            return false;
        } else if (!second.m_123314_(first, (double) maxLength().intValue())) {
            return false;
        } else {
            BlockPos diff = second.subtract(first);
            Direction.Axis shaftAxis = (Direction.Axis) world.getBlockState(first).m_61143_(BlockStateProperties.AXIS);
            int x = diff.m_123341_();
            int y = diff.m_123342_();
            int z = diff.m_123343_();
            int sames = (Math.abs(x) == Math.abs(y) ? 1 : 0) + (Math.abs(y) == Math.abs(z) ? 1 : 0) + (Math.abs(z) == Math.abs(x) ? 1 : 0);
            if (shaftAxis.choose(x, y, z) != 0) {
                return false;
            } else if (sames != 1) {
                return false;
            } else if (shaftAxis != world.getBlockState(second).m_61143_(BlockStateProperties.AXIS)) {
                return false;
            } else if (shaftAxis == Direction.Axis.Y && x != 0 && z != 0) {
                return false;
            } else {
                BlockEntity blockEntity = world.getBlockEntity(first);
                BlockEntity blockEntity2 = world.getBlockEntity(second);
                if (!(blockEntity instanceof KineticBlockEntity)) {
                    return false;
                } else if (!(blockEntity2 instanceof KineticBlockEntity)) {
                    return false;
                } else {
                    float speed1 = ((KineticBlockEntity) blockEntity).getTheoreticalSpeed();
                    float speed2 = ((KineticBlockEntity) blockEntity2).getTheoreticalSpeed();
                    if (Math.signum(speed1) != Math.signum(speed2) && speed1 != 0.0F && speed2 != 0.0F) {
                        return false;
                    } else {
                        BlockPos step = BlockPos.containing((double) Math.signum((float) diff.m_123341_()), (double) Math.signum((float) diff.m_123342_()), (double) Math.signum((float) diff.m_123343_()));
                        int limit = 1000;
                        for (BlockPos currentPos = first.offset(step); !currentPos.equals(second) && limit-- > 0; currentPos = currentPos.offset(step)) {
                            BlockState blockState = world.getBlockState(currentPos);
                            if ((!ShaftBlock.isShaft(blockState) || blockState.m_61143_(AbstractSimpleShaftBlock.AXIS) != shaftAxis) && !blockState.m_247087_()) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
            }
        }
    }

    public static Integer maxLength() {
        return AllConfigs.server().kinetics.maxBeltLength.get();
    }

    public static boolean validateAxis(Level world, BlockPos pos) {
        return !world.isLoaded(pos) ? false : ShaftBlock.isShaft(world.getBlockState(pos));
    }
}