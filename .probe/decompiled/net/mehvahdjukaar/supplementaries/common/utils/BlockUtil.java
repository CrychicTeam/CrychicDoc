package net.mehvahdjukaar.supplementaries.common.utils;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.block.IOwnerProtected;
import net.mehvahdjukaar.moonlight.api.block.IRotatable;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.CompatObjects;
import net.mehvahdjukaar.supplementaries.integration.QuarkCompat;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.Vec3;

public class BlockUtil {

    public static <T extends Comparable<T>, A extends Property<T>> BlockState replaceProperty(BlockState from, BlockState to, A property) {
        return from.m_61138_(property) ? (BlockState) to.m_61124_(property, from.m_61143_(property)) : to;
    }

    public static <T extends BlockEntity & IOwnerProtected> void addOptionalOwnership(LivingEntity placer, T tileEntity) {
        if ((Boolean) CommonConfigs.General.SERVER_PROTECTION.get() && placer instanceof Player) {
            tileEntity.setOwner(placer.m_20148_());
        }
    }

    public static void addOptionalOwnership(LivingEntity placer, Level world, BlockPos pos) {
        if ((Boolean) CommonConfigs.General.SERVER_PROTECTION.get() && placer instanceof Player && world.getBlockEntity(pos) instanceof IOwnerProtected tile) {
            tile.setOwner(placer.m_20148_());
        }
    }

    public static Optional<Direction> tryRotatingBlockAndConnected(Direction face, boolean ccw, BlockPos targetPos, Level level, Vec3 hit) {
        BlockState state = level.getBlockState(targetPos);
        if (state.m_60734_() instanceof IRotatable rotatable) {
            return rotatable.rotateOverAxis(state, level, targetPos, ccw ? Rotation.COUNTERCLOCKWISE_90 : Rotation.CLOCKWISE_90, face, hit);
        } else {
            Optional<Direction> special = tryRotatingSpecial(face, ccw, targetPos, level, state, hit);
            if (special.isPresent()) {
                return special;
            } else {
                Optional<Direction> ret = tryRotatingBlock(face, ccw, targetPos, level, state, hit);
                if (ret.isEmpty()) {
                    ret = tryRotatingBlock(Direction.UP, ccw, targetPos, level, level.getBlockState(targetPos), hit);
                }
                return ret;
            }
        }
    }

    public static Optional<Direction> tryRotatingBlock(Direction face, boolean ccw, BlockPos targetPos, Level level, Vec3 hit) {
        return tryRotatingBlock(face, ccw, targetPos, level, level.getBlockState(targetPos), hit);
    }

    public static Optional<Direction> tryRotatingBlock(Direction dir, boolean ccw, BlockPos targetPos, Level level, BlockState state, Vec3 hit) {
        if (!level.isClientSide && (Boolean) CommonConfigs.Redstone.TURN_TABLE_SHUFFLE.get() && dir.getAxis() != Direction.Axis.Y && state.m_61138_(BarrelBlock.FACING) && state.m_60734_() != ModRegistry.HOURGLASS.get() && level.getBlockEntity(targetPos) instanceof Container c) {
            shuffleContainerContent(c, level);
        }
        if (state.m_60734_() instanceof IRotatable rotatable) {
            return rotatable.rotateOverAxis(state, level, targetPos, ccw ? Rotation.COUNTERCLOCKWISE_90 : Rotation.CLOCKWISE_90, dir, hit);
        } else {
            Optional<BlockState> optional = getRotatedState(dir, ccw, targetPos, level, state);
            if (optional.isPresent()) {
                BlockState rotated = (BlockState) optional.get();
                if (rotated.m_60710_(level, targetPos)) {
                    rotated = Block.updateFromNeighbourShapes(rotated, level, targetPos);
                    if (rotated != state) {
                        if (level instanceof ServerLevel) {
                            level.setBlock(targetPos, rotated, 11);
                            level.neighborChanged(rotated, targetPos, rotated.m_60734_(), targetPos, false);
                        }
                        return Optional.of(dir);
                    }
                }
            }
            return Optional.empty();
        }
    }

    public static Optional<BlockState> getRotatedState(Direction dir, boolean ccw, BlockPos targetPos, Level world, BlockState state) {
        if (isBlacklisted(state)) {
            return Optional.empty();
        } else {
            Rotation rot = ccw ? Rotation.COUNTERCLOCKWISE_90 : Rotation.CLOCKWISE_90;
            Block block = state.m_60734_();
            if (state.m_61138_(ModBlockProperties.FLIPPED)) {
                return Optional.of((BlockState) state.m_61122_(ModBlockProperties.FLIPPED));
            } else if (dir.getAxis() == Direction.Axis.Y) {
                if (block == Blocks.CAKE) {
                    Block dc = (Block) CompatObjects.DIRECTIONAL_CAKE.get();
                    if (dc != null) {
                        int bites = (Integer) state.m_61143_(CakeBlock.BITES);
                        if (bites != 0) {
                            return Optional.of(ForgeHelper.rotateBlock((BlockState) dc.defaultBlockState().m_61124_(CakeBlock.BITES, bites), world, targetPos, rot));
                        }
                    }
                }
                BlockState rotated = ForgeHelper.rotateBlock(state, world, targetPos, rot);
                if (rotated == state) {
                    if (state.m_61138_(BlockStateProperties.FACING)) {
                        rotated = (BlockState) state.m_61124_(BlockStateProperties.FACING, rot.rotate((Direction) state.m_61143_(BlockStateProperties.FACING)));
                    } else if (state.m_61138_(BlockStateProperties.HORIZONTAL_FACING)) {
                        rotated = (BlockState) state.m_61124_(BlockStateProperties.HORIZONTAL_FACING, rot.rotate((Direction) state.m_61143_(BlockStateProperties.HORIZONTAL_FACING)));
                    } else if (state.m_61138_(RotatedPillarBlock.AXIS)) {
                        rotated = RotatedPillarBlock.rotatePillar(state, rot);
                    } else if (state.m_61138_(BlockStateProperties.HORIZONTAL_AXIS)) {
                        rotated = (BlockState) state.m_61122_(BlockStateProperties.HORIZONTAL_AXIS);
                    }
                }
                return Optional.of(rotated);
            } else if (state.m_61138_(BlockStateProperties.ATTACH_FACE) && state.m_61138_(BlockStateProperties.HORIZONTAL_FACING)) {
                return Optional.of(rotateFaceBlockHorizontal(dir, ccw, state));
            } else if (state.m_61138_(BlockStateProperties.FACING)) {
                return Optional.of(rotateBlockStateOnAxis(state, dir, ccw));
            } else {
                if (state.m_61138_(BlockStateProperties.AXIS)) {
                    Direction.Axis targetAxis = (Direction.Axis) state.m_61143_(BlockStateProperties.AXIS);
                    Direction.Axis myAxis = dir.getAxis();
                    if (myAxis == Direction.Axis.X) {
                        return Optional.of((BlockState) state.m_61124_(BlockStateProperties.AXIS, targetAxis == Direction.Axis.Y ? Direction.Axis.Z : Direction.Axis.Y));
                    }
                    if (myAxis == Direction.Axis.Z) {
                        return Optional.of((BlockState) state.m_61124_(BlockStateProperties.AXIS, targetAxis == Direction.Axis.Y ? Direction.Axis.X : Direction.Axis.Y));
                    }
                }
                if (block instanceof StairBlock) {
                    Direction facing = (Direction) state.m_61143_(StairBlock.FACING);
                    if (facing.getAxis() == dir.getAxis()) {
                        return Optional.empty();
                    } else {
                        boolean flipped = dir.getAxisDirection() == Direction.AxisDirection.POSITIVE ^ ccw;
                        Half half = (Half) state.m_61143_(StairBlock.HALF);
                        boolean top = half == Half.TOP;
                        boolean positive = facing.getAxisDirection() == Direction.AxisDirection.POSITIVE;
                        if (top ^ positive ^ flipped) {
                            half = top ? Half.BOTTOM : Half.TOP;
                        } else {
                            facing = facing.getOpposite();
                        }
                        return Optional.of((BlockState) ((BlockState) state.m_61124_(StairBlock.HALF, half)).m_61124_(StairBlock.FACING, facing));
                    }
                } else if (state.m_61138_(SlabBlock.TYPE)) {
                    SlabType type = (SlabType) state.m_61143_(SlabBlock.TYPE);
                    return type == SlabType.DOUBLE ? Optional.empty() : Optional.of((BlockState) state.m_61124_(SlabBlock.TYPE, type == SlabType.BOTTOM ? SlabType.TOP : SlabType.BOTTOM));
                } else if (state.m_61138_(TrapDoorBlock.HALF)) {
                    return Optional.of((BlockState) state.m_61122_(TrapDoorBlock.HALF));
                } else {
                    if (CompatHandler.QUARK) {
                        WoodType type = WoodTypeRegistry.INSTANCE.getBlockTypeOf(block);
                        if (type != null && type.planks == block) {
                            Block verticalPlanks = type.getBlockOfThis("quark:vertical_planks");
                            if (verticalPlanks != null) {
                                return Optional.of(verticalPlanks.defaultBlockState());
                            }
                        }
                    }
                    return Optional.empty();
                }
            }
        }
    }

    private static BlockState rotateBlockStateOnAxis(BlockState state, Direction axis, boolean ccw) {
        Vec3 targetNormal = MthUtils.V3itoV3(((Direction) state.m_61143_(BlockStateProperties.FACING)).getNormal());
        Vec3 myNormal = MthUtils.V3itoV3(axis.getNormal());
        if (!ccw) {
            targetNormal = targetNormal.scale(-1.0);
        }
        Vec3 rotated = myNormal.cross(targetNormal);
        if (rotated != Vec3.ZERO) {
            Direction newDir = Direction.getNearest(rotated.x(), rotated.y(), rotated.z());
            return (BlockState) state.m_61124_(BlockStateProperties.FACING, newDir);
        } else {
            return state;
        }
    }

    private static boolean isBlacklisted(BlockState state) {
        if (state.m_60734_() instanceof BedBlock) {
            return true;
        } else if (state.m_61138_(BlockStateProperties.CHEST_TYPE) && state.m_61143_(BlockStateProperties.CHEST_TYPE) != ChestType.SINGLE) {
            return true;
        } else if (state.m_61138_(BlockStateProperties.EXTENDED) && (Boolean) state.m_61143_(BlockStateProperties.EXTENDED)) {
            return true;
        } else {
            return state.m_61138_(BlockStateProperties.SHORT) ? true : state.m_204336_(ModTags.ROTATION_BLACKLIST);
        }
    }

    private static Optional<Direction> tryRotatingSpecial(Direction face, boolean ccw, BlockPos pos, Level level, BlockState state, Vec3 hit) {
        Block b = state.m_60734_();
        Rotation rot = ccw ? Rotation.COUNTERCLOCKWISE_90 : Rotation.CLOCKWISE_90;
        if (state.m_61138_(BlockStateProperties.ROTATION_16)) {
            int r = (Integer) state.m_61143_(BlockStateProperties.ROTATION_16);
            r += ccw ? -1 : 1;
            if (r < 0) {
                r += 16;
            }
            r %= 16;
            level.setBlock(pos, (BlockState) state.m_61124_(BlockStateProperties.ROTATION_16, r), 2);
            return Optional.of(Direction.UP);
        } else if (state.m_61138_(BlockStateProperties.EXTENDED) && (Boolean) state.m_61143_(BlockStateProperties.EXTENDED) && state.m_61138_(PistonHeadBlock.f_52588_)) {
            BlockState newBase = rotateBlockStateOnAxis(state, face, ccw);
            BlockPos headPos = pos.relative((Direction) state.m_61143_(PistonHeadBlock.f_52588_));
            if (level.getBlockState(headPos).m_61138_(PistonHeadBlock.SHORT)) {
                BlockPos newHeadPos = pos.relative((Direction) newBase.m_61143_(PistonHeadBlock.f_52588_));
                if (level.getBlockState(newHeadPos).m_247087_()) {
                    level.setBlock(newHeadPos, rotateBlockStateOnAxis(level.getBlockState(headPos), face, ccw), 2);
                    level.setBlock(pos, newBase, 2);
                    level.removeBlock(headPos, false);
                    return Optional.of(face);
                }
            }
            return Optional.empty();
        } else if (state.m_61138_(BlockStateProperties.SHORT) && state.m_61138_(PistonHeadBlock.f_52588_)) {
            BlockState newBase = rotateBlockStateOnAxis(state, face, ccw);
            BlockPos headPos = pos.relative(((Direction) state.m_61143_(PistonHeadBlock.f_52588_)).getOpposite());
            if (level.getBlockState(headPos).m_61138_(PistonBaseBlock.EXTENDED)) {
                BlockPos newHeadPos = pos.relative(((Direction) newBase.m_61143_(PistonHeadBlock.f_52588_)).getOpposite());
                if (level.getBlockState(newHeadPos).m_247087_()) {
                    level.setBlock(newHeadPos, rotateBlockStateOnAxis(level.getBlockState(headPos), face, ccw), 2);
                    level.setBlock(pos, newBase, 2);
                    level.removeBlock(headPos, false);
                    return Optional.of(face);
                }
            }
            return Optional.empty();
        } else if (b instanceof BedBlock) {
            BlockState newBed = ForgeHelper.rotateBlock(state, level, pos, rot);
            BlockPos oldPos = pos.relative(getConnectedBedDirection(state));
            BlockPos targetPos = pos.relative(getConnectedBedDirection(newBed));
            if (level.getBlockState(targetPos).m_247087_()) {
                level.setBlock(targetPos, ForgeHelper.rotateBlock(level.getBlockState(oldPos), level, oldPos, rot), 2);
                level.setBlock(pos, newBed, 2);
                level.removeBlock(oldPos, false);
                return Optional.of(face);
            } else {
                return Optional.empty();
            }
        } else if (b instanceof ChestBlock) {
            if (state.m_61143_(ChestBlock.TYPE) != ChestType.SINGLE) {
                BlockState newChest = ForgeHelper.rotateBlock(state, level, pos, rot);
                BlockPos oldPos = pos.relative(ChestBlock.getConnectedDirection(state));
                BlockPos targetPos = pos.relative(ChestBlock.getConnectedDirection(newChest));
                if (level.getBlockState(targetPos).m_247087_()) {
                    BlockState connectedNewState = ForgeHelper.rotateBlock(level.getBlockState(oldPos), level, oldPos, rot);
                    level.setBlock(targetPos, connectedNewState, 2);
                    level.setBlock(pos, newChest, 2);
                    BlockEntity tile = level.getBlockEntity(oldPos);
                    if (tile != null) {
                        CompoundTag tag = tile.saveWithoutMetadata();
                        if (level.getBlockEntity(targetPos) instanceof ChestBlockEntity newChestTile) {
                            newChestTile.load(tag);
                        }
                        tile.setRemoved();
                    }
                    level.setBlockAndUpdate(oldPos, Blocks.AIR.defaultBlockState());
                    return Optional.of(face);
                }
            }
            return Optional.empty();
        } else {
            if (DoorBlock.isWoodenDoor(state)) {
            }
            return CompatHandler.QUARK && QuarkCompat.tryRotateStool(level, state, pos) ? Optional.of(face) : Optional.empty();
        }
    }

    private static void shuffleContainerContent(Container c, Level level) {
        ObjectArrayList<ItemStack> content = ObjectArrayList.of();
        for (int i = 0; i < c.getContainerSize(); i++) {
            content.add(c.removeItemNoUpdate(i));
        }
        Util.shuffle(content, level.random);
        for (int i = 0; i < content.size(); i++) {
            c.setItem(i, (ItemStack) content.get(i));
        }
        c.setChanged();
    }

    public static Direction getConnectedBedDirection(BlockState bedState) {
        BedPart part = (BedPart) bedState.m_61143_(BedBlock.PART);
        Direction dir = (Direction) bedState.m_61143_(BedBlock.f_54117_);
        return part == BedPart.FOOT ? dir : dir.getOpposite();
    }

    private static BlockState rotateFaceBlockHorizontal(Direction dir, boolean ccw, BlockState original) {
        Direction facingDir = (Direction) original.m_61143_(BlockStateProperties.HORIZONTAL_FACING);
        if (facingDir.getAxis() == dir.getAxis()) {
            return original;
        } else {
            AttachFace face = (AttachFace) original.m_61143_(BlockStateProperties.ATTACH_FACE);
            return switch(face) {
                case FLOOR ->
                    (BlockState) ((BlockState) original.m_61124_(BlockStateProperties.ATTACH_FACE, AttachFace.WALL)).m_61124_(BlockStateProperties.HORIZONTAL_FACING, ccw ? dir.getClockWise() : dir.getCounterClockWise());
                case CEILING ->
                    (BlockState) ((BlockState) original.m_61124_(BlockStateProperties.ATTACH_FACE, AttachFace.WALL)).m_61124_(BlockStateProperties.HORIZONTAL_FACING, !ccw ? dir.getClockWise() : dir.getCounterClockWise());
                case WALL ->
                    {
                        ccw ^= dir.getAxisDirection() != Direction.AxisDirection.POSITIVE;
                        ???;
                    }
            };
        }
    }
}