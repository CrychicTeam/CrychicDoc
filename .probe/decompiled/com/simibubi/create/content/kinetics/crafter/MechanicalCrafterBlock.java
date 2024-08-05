package com.simibubi.create.content.kinetics.crafter;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.InvManipulationBehaviour;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Pointing;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class MechanicalCrafterBlock extends HorizontalKineticBlock implements IBE<MechanicalCrafterBlockEntity>, ICogWheel {

    public static final EnumProperty<Pointing> POINTING = EnumProperty.create("pointing", Pointing.class);

    public MechanicalCrafterBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(POINTING, Pointing.UP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(POINTING));
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return ((Direction) state.m_61143_(HORIZONTAL_FACING)).getAxis();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction face = context.m_43719_();
        BlockPos placedOnPos = context.getClickedPos().relative(face.getOpposite());
        BlockState blockState = context.m_43725_().getBlockState(placedOnPos);
        if (blockState.m_60734_() != this || context.m_43723_() != null && context.m_43723_().m_6144_()) {
            BlockState stateForPlacement = super.getStateForPlacement(context);
            Direction direction = (Direction) stateForPlacement.m_61143_(HORIZONTAL_FACING);
            if (direction != face) {
                stateForPlacement = (BlockState) stateForPlacement.m_61124_(POINTING, pointingFromFacing(face, direction));
            }
            return stateForPlacement;
        } else {
            Direction otherFacing = (Direction) blockState.m_61143_(HORIZONTAL_FACING);
            Pointing pointing = pointingFromFacing(face, otherFacing);
            return (BlockState) ((BlockState) this.m_49966_().m_61124_(HORIZONTAL_FACING, otherFacing)).m_61124_(POINTING, pointing);
        }
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() == newState.m_60734_() && getTargetDirection(state) != getTargetDirection(newState)) {
            MechanicalCrafterBlockEntity crafter = CrafterHelper.getCrafter(worldIn, pos);
            if (crafter != null) {
                crafter.blockChanged();
            }
        }
        if (state.m_155947_() && !state.m_60713_(newState.m_60734_())) {
            MechanicalCrafterBlockEntity crafter = CrafterHelper.getCrafter(worldIn, pos);
            if (crafter != null) {
                if (crafter.covered) {
                    Block.popResource(worldIn, pos, AllItems.CRAFTER_SLOT_COVER.asStack());
                }
                if (!isMoving) {
                    crafter.ejectWholeGrid();
                }
            }
            for (Direction direction : Iterate.directions) {
                if (direction.getAxis() != ((Direction) state.m_61143_(HORIZONTAL_FACING)).getAxis()) {
                    BlockPos otherPos = pos.relative(direction);
                    ConnectedInputHandler.ConnectedInput thisInput = CrafterHelper.getInput(worldIn, pos);
                    ConnectedInputHandler.ConnectedInput otherInput = CrafterHelper.getInput(worldIn, otherPos);
                    if (thisInput != null && otherInput != null && pos.offset((Vec3i) thisInput.data.get(0)).equals(otherPos.offset((Vec3i) otherInput.data.get(0)))) {
                        ConnectedInputHandler.toggleConnection(worldIn, pos, otherPos);
                    }
                }
            }
        }
        super.m_6810_(state, worldIn, pos, newState, isMoving);
    }

    public static Pointing pointingFromFacing(Direction pointingFace, Direction blockFacing) {
        boolean positive = blockFacing.getAxisDirection() == Direction.AxisDirection.POSITIVE;
        Pointing pointing = pointingFace == Direction.DOWN ? Pointing.UP : Pointing.DOWN;
        if (pointingFace == Direction.EAST) {
            pointing = positive ? Pointing.LEFT : Pointing.RIGHT;
        }
        if (pointingFace == Direction.WEST) {
            pointing = positive ? Pointing.RIGHT : Pointing.LEFT;
        }
        if (pointingFace == Direction.NORTH) {
            pointing = positive ? Pointing.LEFT : Pointing.RIGHT;
        }
        if (pointingFace == Direction.SOUTH) {
            pointing = positive ? Pointing.RIGHT : Pointing.LEFT;
        }
        return pointing;
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        if (context.getClickedFace() == state.m_61143_(HORIZONTAL_FACING)) {
            if (!context.getLevel().isClientSide) {
                KineticBlockEntity.switchToBlockState(context.getLevel(), context.getClickedPos(), (BlockState) state.m_61122_(POINTING));
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.getBlockEntity(pos) instanceof MechanicalCrafterBlockEntity crafter) {
            ItemStack heldItem = player.m_21120_(handIn);
            if (AllBlocks.MECHANICAL_ARM.isIn(heldItem)) {
                return InteractionResult.PASS;
            } else {
                boolean isHand = heldItem.isEmpty() && handIn == InteractionHand.MAIN_HAND;
                boolean wrenched = AllItems.WRENCH.isIn(heldItem);
                if (hit.getDirection() == state.m_61143_(HORIZONTAL_FACING)) {
                    if (crafter.phase != MechanicalCrafterBlockEntity.Phase.IDLE && !wrenched) {
                        crafter.ejectWholeGrid();
                        return InteractionResult.SUCCESS;
                    } else if (crafter.phase != MechanicalCrafterBlockEntity.Phase.IDLE || isHand || wrenched) {
                        ItemStack inSlot = crafter.getInventory().m_8020_(0);
                        if (inSlot.isEmpty()) {
                            if (!crafter.covered || wrenched) {
                                return InteractionResult.PASS;
                            } else if (worldIn.isClientSide) {
                                return InteractionResult.SUCCESS;
                            } else {
                                crafter.covered = false;
                                crafter.m_6596_();
                                crafter.sendData();
                                if (!player.isCreative()) {
                                    player.getInventory().placeItemBackInInventory(AllItems.CRAFTER_SLOT_COVER.asStack());
                                }
                                return InteractionResult.SUCCESS;
                            }
                        } else if (!isHand && !ItemHandlerHelper.canItemStacksStack(heldItem, inSlot)) {
                            return InteractionResult.PASS;
                        } else if (worldIn.isClientSide) {
                            return InteractionResult.SUCCESS;
                        } else {
                            player.getInventory().placeItemBackInInventory(inSlot);
                            crafter.getInventory().setStackInSlot(0, ItemStack.EMPTY);
                            return InteractionResult.SUCCESS;
                        }
                    } else if (worldIn.isClientSide) {
                        return InteractionResult.SUCCESS;
                    } else if (!AllItems.CRAFTER_SLOT_COVER.isIn(heldItem)) {
                        LazyOptional<IItemHandler> capability = crafter.getCapability(ForgeCapabilities.ITEM_HANDLER);
                        if (!capability.isPresent()) {
                            return InteractionResult.PASS;
                        } else {
                            ItemStack remainder = ItemHandlerHelper.insertItem(capability.orElse(new ItemStackHandler()), heldItem.copy(), false);
                            if (remainder.getCount() != heldItem.getCount()) {
                                player.m_21008_(handIn, remainder);
                            }
                            return InteractionResult.SUCCESS;
                        }
                    } else if (crafter.covered) {
                        return InteractionResult.PASS;
                    } else if (!crafter.inventory.m_7983_()) {
                        return InteractionResult.PASS;
                    } else {
                        crafter.covered = true;
                        crafter.m_6596_();
                        crafter.sendData();
                        if (!player.isCreative()) {
                            heldItem.shrink(1);
                        }
                        return InteractionResult.SUCCESS;
                    }
                } else {
                    return InteractionResult.PASS;
                }
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        InvManipulationBehaviour behaviour = BlockEntityBehaviour.get(worldIn, pos, InvManipulationBehaviour.TYPE);
        if (behaviour != null) {
            behaviour.onNeighborChanged(fromPos);
        }
    }

    @Override
    public float getParticleTargetRadius() {
        return 0.85F;
    }

    @Override
    public float getParticleInitialRadius() {
        return 0.75F;
    }

    public static Direction getTargetDirection(BlockState state) {
        if (!AllBlocks.MECHANICAL_CRAFTER.has(state)) {
            return Direction.UP;
        } else {
            Direction facing = (Direction) state.m_61143_(HORIZONTAL_FACING);
            Pointing point = (Pointing) state.m_61143_(POINTING);
            Vec3 targetVec = new Vec3(0.0, 1.0, 0.0);
            targetVec = VecHelper.rotate(targetVec, (double) (-point.getXRotation()), Direction.Axis.Z);
            targetVec = VecHelper.rotate(targetVec, (double) AngleHelper.horizontalAngle(facing), Direction.Axis.Y);
            return Direction.getNearest(targetVec.x, targetVec.y, targetVec.z);
        }
    }

    public static boolean isValidTarget(Level world, BlockPos targetPos, BlockState crafterState) {
        BlockState targetState = world.getBlockState(targetPos);
        if (!world.isLoaded(targetPos)) {
            return false;
        } else if (!AllBlocks.MECHANICAL_CRAFTER.has(targetState)) {
            return false;
        } else {
            return crafterState.m_61143_(HORIZONTAL_FACING) != targetState.m_61143_(HORIZONTAL_FACING) ? false : Math.abs(((Pointing) crafterState.m_61143_(POINTING)).getXRotation() - ((Pointing) targetState.m_61143_(POINTING)).getXRotation()) != 180;
        }
    }

    @Override
    public Class<MechanicalCrafterBlockEntity> getBlockEntityClass() {
        return MechanicalCrafterBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MechanicalCrafterBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends MechanicalCrafterBlockEntity>) AllBlockEntityTypes.MECHANICAL_CRAFTER.get();
    }
}