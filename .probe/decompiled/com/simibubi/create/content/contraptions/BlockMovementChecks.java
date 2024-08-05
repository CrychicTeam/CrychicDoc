package com.simibubi.create.content.contraptions;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.contraptions.actors.AttachedActorBlock;
import com.simibubi.create.content.contraptions.actors.harvester.HarvesterBlock;
import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceBlock;
import com.simibubi.create.content.contraptions.bearing.ClockworkBearingBlock;
import com.simibubi.create.content.contraptions.bearing.ClockworkBearingBlockEntity;
import com.simibubi.create.content.contraptions.bearing.MechanicalBearingBlock;
import com.simibubi.create.content.contraptions.bearing.MechanicalBearingBlockEntity;
import com.simibubi.create.content.contraptions.bearing.SailBlock;
import com.simibubi.create.content.contraptions.chassis.AbstractChassisBlock;
import com.simibubi.create.content.contraptions.chassis.StickerBlock;
import com.simibubi.create.content.contraptions.mounted.CartAssemblerBlock;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonBlock;
import com.simibubi.create.content.contraptions.pulley.PulleyBlock;
import com.simibubi.create.content.contraptions.pulley.PulleyBlockEntity;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlock;
import com.simibubi.create.content.decoration.steamWhistle.WhistleExtenderBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.kinetics.crank.HandCrankBlock;
import com.simibubi.create.content.kinetics.fan.NozzleBlock;
import com.simibubi.create.content.logistics.vault.ItemVaultBlock;
import com.simibubi.create.content.redstone.link.RedstoneLinkBlock;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlock;
import com.simibubi.create.content.trains.station.StationBlock;
import com.simibubi.create.content.trains.track.ITrackBlock;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.GrindstoneBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.RedstoneWallTorchBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BellAttachType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.PushReaction;

public class BlockMovementChecks {

    private static final List<BlockMovementChecks.MovementNecessaryCheck> MOVEMENT_NECESSARY_CHECKS = new ArrayList();

    private static final List<BlockMovementChecks.MovementAllowedCheck> MOVEMENT_ALLOWED_CHECKS = new ArrayList();

    private static final List<BlockMovementChecks.BrittleCheck> BRITTLE_CHECKS = new ArrayList();

    private static final List<BlockMovementChecks.AttachedCheck> ATTACHED_CHECKS = new ArrayList();

    private static final List<BlockMovementChecks.NotSupportiveCheck> NOT_SUPPORTIVE_CHECKS = new ArrayList();

    public static void registerMovementNecessaryCheck(BlockMovementChecks.MovementNecessaryCheck check) {
        MOVEMENT_NECESSARY_CHECKS.add(0, check);
    }

    public static void registerMovementAllowedCheck(BlockMovementChecks.MovementAllowedCheck check) {
        MOVEMENT_ALLOWED_CHECKS.add(0, check);
    }

    public static void registerBrittleCheck(BlockMovementChecks.BrittleCheck check) {
        BRITTLE_CHECKS.add(0, check);
    }

    public static void registerAttachedCheck(BlockMovementChecks.AttachedCheck check) {
        ATTACHED_CHECKS.add(0, check);
    }

    public static void registerNotSupportiveCheck(BlockMovementChecks.NotSupportiveCheck check) {
        NOT_SUPPORTIVE_CHECKS.add(0, check);
    }

    public static void registerAllChecks(BlockMovementChecks.AllChecks checks) {
        registerMovementNecessaryCheck(checks);
        registerMovementAllowedCheck(checks);
        registerBrittleCheck(checks);
        registerAttachedCheck(checks);
        registerNotSupportiveCheck(checks);
    }

    public static boolean isMovementNecessary(BlockState state, Level world, BlockPos pos) {
        for (BlockMovementChecks.MovementNecessaryCheck check : MOVEMENT_NECESSARY_CHECKS) {
            BlockMovementChecks.CheckResult result = check.isMovementNecessary(state, world, pos);
            if (result != BlockMovementChecks.CheckResult.PASS) {
                return result.toBoolean();
            }
        }
        return isMovementNecessaryFallback(state, world, pos);
    }

    public static boolean isMovementAllowed(BlockState state, Level world, BlockPos pos) {
        for (BlockMovementChecks.MovementAllowedCheck check : MOVEMENT_ALLOWED_CHECKS) {
            BlockMovementChecks.CheckResult result = check.isMovementAllowed(state, world, pos);
            if (result != BlockMovementChecks.CheckResult.PASS) {
                return result.toBoolean();
            }
        }
        return isMovementAllowedFallback(state, world, pos);
    }

    public static boolean isBrittle(BlockState state) {
        for (BlockMovementChecks.BrittleCheck check : BRITTLE_CHECKS) {
            BlockMovementChecks.CheckResult result = check.isBrittle(state);
            if (result != BlockMovementChecks.CheckResult.PASS) {
                return result.toBoolean();
            }
        }
        return isBrittleFallback(state);
    }

    public static boolean isBlockAttachedTowards(BlockState state, Level world, BlockPos pos, Direction direction) {
        for (BlockMovementChecks.AttachedCheck check : ATTACHED_CHECKS) {
            BlockMovementChecks.CheckResult result = check.isBlockAttachedTowards(state, world, pos, direction);
            if (result != BlockMovementChecks.CheckResult.PASS) {
                return result.toBoolean();
            }
        }
        return isBlockAttachedTowardsFallback(state, world, pos, direction);
    }

    public static boolean isNotSupportive(BlockState state, Direction facing) {
        for (BlockMovementChecks.NotSupportiveCheck check : NOT_SUPPORTIVE_CHECKS) {
            BlockMovementChecks.CheckResult result = check.isNotSupportive(state, facing);
            if (result != BlockMovementChecks.CheckResult.PASS) {
                return result.toBoolean();
            }
        }
        return isNotSupportiveFallback(state, facing);
    }

    private static boolean isMovementNecessaryFallback(BlockState state, Level world, BlockPos pos) {
        if (isBrittle(state)) {
            return true;
        } else if (AllTags.AllBlockTags.MOVABLE_EMPTY_COLLIDER.matches(state)) {
            return true;
        } else {
            return state.m_60812_(world, pos).isEmpty() ? false : !state.m_247087_();
        }
    }

    private static boolean isMovementAllowedFallback(BlockState state, Level world, BlockPos pos) {
        Block block = state.m_60734_();
        if (block instanceof AbstractChassisBlock) {
            return true;
        } else if (state.m_60800_(world, pos) == -1.0F) {
            return false;
        } else if (AllTags.AllBlockTags.RELOCATION_NOT_SUPPORTED.matches(state)) {
            return false;
        } else if (AllTags.AllBlockTags.NON_MOVABLE.matches(state)) {
            return false;
        } else if (ContraptionMovementSetting.get(state.m_60734_()) == ContraptionMovementSetting.UNMOVABLE) {
            return false;
        } else if (block instanceof MechanicalPistonBlock && state.m_61143_(MechanicalPistonBlock.STATE) != MechanicalPistonBlock.PistonState.MOVING) {
            return true;
        } else {
            if (block instanceof MechanicalBearingBlock) {
                BlockEntity be = world.getBlockEntity(pos);
                if (be instanceof MechanicalBearingBlockEntity) {
                    return !((MechanicalBearingBlockEntity) be).isRunning();
                }
            }
            if (block instanceof ClockworkBearingBlock) {
                BlockEntity be = world.getBlockEntity(pos);
                if (be instanceof ClockworkBearingBlockEntity) {
                    return !((ClockworkBearingBlockEntity) be).isRunning();
                }
            }
            if (block instanceof PulleyBlock) {
                BlockEntity be = world.getBlockEntity(pos);
                if (be instanceof PulleyBlockEntity) {
                    return !((PulleyBlockEntity) be).running;
                }
            }
            if (AllBlocks.BELT.has(state)) {
                return true;
            } else if (state.m_60734_() instanceof GrindstoneBlock) {
                return true;
            } else if (state.m_60734_() instanceof ITrackBlock) {
                return false;
            } else {
                return state.m_60734_() instanceof StationBlock ? false : state.m_60811_() != PushReaction.BLOCK;
            }
        }
    }

    private static boolean isBrittleFallback(BlockState state) {
        Block block = state.m_60734_();
        if (state.m_61138_(BlockStateProperties.HANGING)) {
            return true;
        } else if (block instanceof LadderBlock) {
            return true;
        } else if (block instanceof TorchBlock) {
            return true;
        } else if (block instanceof SignBlock) {
            return true;
        } else if (block instanceof BasePressurePlateBlock) {
            return true;
        } else if (block instanceof FaceAttachedHorizontalDirectionalBlock && !(block instanceof GrindstoneBlock)) {
            return true;
        } else if (block instanceof CartAssemblerBlock) {
            return false;
        } else if (block instanceof BaseRailBlock) {
            return true;
        } else if (block instanceof DiodeBlock) {
            return true;
        } else if (block instanceof RedStoneWireBlock) {
            return true;
        } else if (block instanceof WoolCarpetBlock) {
            return true;
        } else if (block instanceof WhistleBlock) {
            return true;
        } else {
            return block instanceof WhistleExtenderBlock ? true : AllTags.AllBlockTags.BRITTLE.matches(state);
        }
    }

    private static boolean isBlockAttachedTowardsFallback(BlockState state, Level world, BlockPos pos, Direction direction) {
        Block block = state.m_60734_();
        if (block instanceof LadderBlock) {
            return state.m_61143_(LadderBlock.FACING) == direction.getOpposite();
        } else if (block instanceof WallTorchBlock) {
            return state.m_61143_(WallTorchBlock.FACING) == direction.getOpposite();
        } else if (block instanceof WallSignBlock) {
            return state.m_61143_(WallSignBlock.FACING) == direction.getOpposite();
        } else if (block instanceof StandingSignBlock) {
            return direction == Direction.DOWN;
        } else if (block instanceof BasePressurePlateBlock) {
            return direction == Direction.DOWN;
        } else if (block instanceof DoorBlock) {
            return state.m_61143_(DoorBlock.HALF) == DoubleBlockHalf.LOWER && direction == Direction.UP ? true : direction == Direction.DOWN;
        } else if (block instanceof BedBlock) {
            Direction facing = (Direction) state.m_61143_(BedBlock.f_54117_);
            if (state.m_61143_(BedBlock.PART) == BedPart.HEAD) {
                facing = facing.getOpposite();
            }
            return direction == facing;
        } else if (block instanceof RedstoneLinkBlock) {
            return direction.getOpposite() == state.m_61143_(RedstoneLinkBlock.f_52588_);
        } else if (block instanceof FlowerPotBlock) {
            return direction == Direction.DOWN;
        } else if (block instanceof DiodeBlock) {
            return direction == Direction.DOWN;
        } else if (block instanceof RedStoneWireBlock) {
            return direction == Direction.DOWN;
        } else if (block instanceof WoolCarpetBlock) {
            return direction == Direction.DOWN;
        } else if (block instanceof RedstoneWallTorchBlock) {
            return state.m_61143_(RedstoneWallTorchBlock.FACING) == direction.getOpposite();
        } else if (block instanceof TorchBlock) {
            return direction == Direction.DOWN;
        } else {
            if (block instanceof FaceAttachedHorizontalDirectionalBlock) {
                AttachFace attachFace = (AttachFace) state.m_61143_(FaceAttachedHorizontalDirectionalBlock.FACE);
                if (attachFace == AttachFace.CEILING) {
                    return direction == Direction.UP;
                }
                if (attachFace == AttachFace.FLOOR) {
                    return direction == Direction.DOWN;
                }
                if (attachFace == AttachFace.WALL) {
                    return direction.getOpposite() == state.m_61143_(FaceAttachedHorizontalDirectionalBlock.f_54117_);
                }
            }
            if (state.m_61138_(BlockStateProperties.HANGING)) {
                return direction == (state.m_61143_(BlockStateProperties.HANGING) ? Direction.UP : Direction.DOWN);
            } else if (block instanceof BaseRailBlock) {
                return direction == Direction.DOWN;
            } else if (block instanceof AttachedActorBlock) {
                return direction == ((Direction) state.m_61143_(HarvesterBlock.f_54117_)).getOpposite();
            } else if (block instanceof HandCrankBlock) {
                return direction == ((Direction) state.m_61143_(HandCrankBlock.FACING)).getOpposite();
            } else if (block instanceof NozzleBlock) {
                return direction == ((Direction) state.m_61143_(NozzleBlock.f_52588_)).getOpposite();
            } else if (block instanceof BellBlock) {
                BellAttachType attachment = (BellAttachType) state.m_61143_(BlockStateProperties.BELL_ATTACHMENT);
                if (attachment == BellAttachType.FLOOR) {
                    return direction == Direction.DOWN;
                } else {
                    return attachment == BellAttachType.CEILING ? direction == Direction.UP : direction == state.m_61143_(HorizontalDirectionalBlock.FACING);
                }
            } else if (state.m_60734_() instanceof SailBlock) {
                return direction.getAxis() != ((Direction) state.m_61143_(SailBlock.f_52588_)).getAxis();
            } else if (state.m_60734_() instanceof FluidTankBlock) {
                return ConnectivityHandler.isConnected(world, pos, pos.relative(direction));
            } else if (state.m_60734_() instanceof ItemVaultBlock) {
                return ConnectivityHandler.isConnected(world, pos, pos.relative(direction));
            } else if (AllBlocks.STICKER.has(state) && (Boolean) state.m_61143_(StickerBlock.EXTENDED)) {
                return direction == state.m_61143_(StickerBlock.f_52588_) && !isNotSupportive(world.getBlockState(pos.relative(direction)), direction.getOpposite());
            } else if (block instanceof AbstractBogeyBlock<?> bogey) {
                return bogey.getStickySurfaces(world, pos, state).contains(direction);
            } else if (block instanceof WhistleBlock) {
                return direction == (state.m_61143_(WhistleBlock.WALL) ? (Direction) state.m_61143_(WhistleBlock.FACING) : Direction.DOWN);
            } else {
                return block instanceof WhistleExtenderBlock ? direction == Direction.DOWN : false;
            }
        }
    }

    private static boolean isNotSupportiveFallback(BlockState state, Direction facing) {
        if (AllBlocks.MECHANICAL_DRILL.has(state)) {
            return state.m_61143_(BlockStateProperties.FACING) == facing;
        } else if (AllBlocks.MECHANICAL_BEARING.has(state)) {
            return state.m_61143_(BlockStateProperties.FACING) == facing;
        } else if (AllBlocks.CART_ASSEMBLER.has(state)) {
            return Direction.DOWN == facing;
        } else if (AllBlocks.MECHANICAL_SAW.has(state)) {
            return state.m_61143_(BlockStateProperties.FACING) == facing;
        } else if (AllBlocks.PORTABLE_STORAGE_INTERFACE.has(state)) {
            return state.m_61143_(PortableStorageInterfaceBlock.f_52588_) == facing;
        } else if (state.m_60734_() instanceof AttachedActorBlock && !AllBlocks.MECHANICAL_ROLLER.has(state)) {
            return state.m_61143_(BlockStateProperties.HORIZONTAL_FACING) == facing;
        } else if (AllBlocks.ROPE_PULLEY.has(state)) {
            return facing == Direction.DOWN;
        } else if (state.m_60734_() instanceof WoolCarpetBlock) {
            return facing == Direction.UP;
        } else if (state.m_60734_() instanceof SailBlock) {
            return facing.getAxis() == ((Direction) state.m_61143_(SailBlock.f_52588_)).getAxis();
        } else if (AllBlocks.PISTON_EXTENSION_POLE.has(state)) {
            return facing.getAxis() != ((Direction) state.m_61143_(BlockStateProperties.FACING)).getAxis();
        } else if (AllBlocks.MECHANICAL_PISTON_HEAD.has(state)) {
            return facing.getAxis() != ((Direction) state.m_61143_(BlockStateProperties.FACING)).getAxis();
        } else if (AllBlocks.STICKER.has(state) && !(Boolean) state.m_61143_(StickerBlock.EXTENDED)) {
            return facing == state.m_61143_(StickerBlock.f_52588_);
        } else {
            return state.m_60734_() instanceof SlidingDoorBlock ? false : isBrittle(state);
        }
    }

    public interface AllChecks extends BlockMovementChecks.MovementNecessaryCheck, BlockMovementChecks.MovementAllowedCheck, BlockMovementChecks.BrittleCheck, BlockMovementChecks.AttachedCheck, BlockMovementChecks.NotSupportiveCheck {
    }

    public interface AttachedCheck {

        BlockMovementChecks.CheckResult isBlockAttachedTowards(BlockState var1, Level var2, BlockPos var3, Direction var4);
    }

    public interface BrittleCheck {

        BlockMovementChecks.CheckResult isBrittle(BlockState var1);
    }

    public static enum CheckResult {

        SUCCESS, FAIL, PASS;

        public Boolean toBoolean() {
            return this == PASS ? null : this == SUCCESS;
        }

        public static BlockMovementChecks.CheckResult of(boolean b) {
            return b ? SUCCESS : FAIL;
        }

        public static BlockMovementChecks.CheckResult of(Boolean b) {
            return b == null ? PASS : (b ? SUCCESS : FAIL);
        }
    }

    public interface MovementAllowedCheck {

        BlockMovementChecks.CheckResult isMovementAllowed(BlockState var1, Level var2, BlockPos var3);
    }

    public interface MovementNecessaryCheck {

        BlockMovementChecks.CheckResult isMovementNecessary(BlockState var1, Level var2, BlockPos var3);
    }

    public interface NotSupportiveCheck {

        BlockMovementChecks.CheckResult isNotSupportive(BlockState var1, Direction var2);
    }
}