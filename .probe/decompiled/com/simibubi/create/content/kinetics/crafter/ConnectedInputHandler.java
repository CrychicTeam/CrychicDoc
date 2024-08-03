package com.simibubi.create.content.kinetics.crafter;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class ConnectedInputHandler {

    public static boolean shouldConnect(Level world, BlockPos pos, Direction face, Direction direction) {
        BlockState refState = world.getBlockState(pos);
        if (!refState.m_61138_(HorizontalKineticBlock.HORIZONTAL_FACING)) {
            return false;
        } else {
            Direction refDirection = (Direction) refState.m_61143_(HorizontalKineticBlock.HORIZONTAL_FACING);
            if (direction.getAxis() == refDirection.getAxis()) {
                return false;
            } else if (face == refDirection) {
                return false;
            } else {
                BlockState neighbour = world.getBlockState(pos.relative(direction));
                return !AllBlocks.MECHANICAL_CRAFTER.has(neighbour) ? false : refDirection == neighbour.m_61143_(HorizontalKineticBlock.HORIZONTAL_FACING);
            }
        }
    }

    public static void toggleConnection(Level world, BlockPos pos, BlockPos pos2) {
        MechanicalCrafterBlockEntity crafter1 = CrafterHelper.getCrafter(world, pos);
        MechanicalCrafterBlockEntity crafter2 = CrafterHelper.getCrafter(world, pos2);
        if (crafter1 != null && crafter2 != null) {
            BlockPos controllerPos1 = crafter1.m_58899_().offset((Vec3i) crafter1.input.data.get(0));
            BlockPos controllerPos2 = crafter2.m_58899_().offset((Vec3i) crafter2.input.data.get(0));
            if (!controllerPos1.equals(controllerPos2)) {
                if (!crafter1.input.isController) {
                    crafter1 = CrafterHelper.getCrafter(world, controllerPos1);
                }
                if (!crafter2.input.isController) {
                    crafter2 = CrafterHelper.getCrafter(world, controllerPos2);
                }
                if (crafter1 != null && crafter2 != null) {
                    connectControllers(world, crafter1, crafter2);
                    world.setBlock(crafter1.m_58899_(), crafter1.m_58900_(), 3);
                    crafter1.m_6596_();
                    crafter1.connectivityChanged();
                    crafter2.m_6596_();
                    crafter2.connectivityChanged();
                }
            } else {
                MechanicalCrafterBlockEntity controller = CrafterHelper.getCrafter(world, controllerPos1);
                Set<BlockPos> positions = (Set<BlockPos>) controller.input.data.stream().map(controllerPos1::m_121955_).collect(Collectors.toSet());
                List<BlockPos> frontier = new LinkedList();
                List<BlockPos> splitGroup = new ArrayList();
                frontier.add(pos2);
                positions.remove(pos2);
                positions.remove(pos);
                while (!frontier.isEmpty()) {
                    BlockPos current = (BlockPos) frontier.remove(0);
                    for (Direction direction : Iterate.directions) {
                        BlockPos next = current.relative(direction);
                        if (positions.remove(next)) {
                            splitGroup.add(next);
                            frontier.add(next);
                        }
                    }
                }
                initAndAddAll(world, crafter1, positions);
                initAndAddAll(world, crafter2, splitGroup);
                crafter1.m_6596_();
                crafter1.connectivityChanged();
                crafter2.m_6596_();
                crafter2.connectivityChanged();
            }
        }
    }

    public static void initAndAddAll(Level world, MechanicalCrafterBlockEntity crafter, Collection<BlockPos> positions) {
        crafter.input = new ConnectedInputHandler.ConnectedInput();
        positions.forEach(splitPos -> modifyAndUpdate(world, splitPos, input -> {
            input.attachTo(crafter.m_58899_(), splitPos);
            crafter.input.data.add(splitPos.subtract(crafter.m_58899_()));
        }));
    }

    public static void connectControllers(Level world, MechanicalCrafterBlockEntity crafter1, MechanicalCrafterBlockEntity crafter2) {
        crafter1.input.data.forEach(offset -> {
            BlockPos connectedPos = crafter1.m_58899_().offset(offset);
            modifyAndUpdate(world, connectedPos, input -> {
            });
        });
        crafter2.input.data.forEach(offset -> {
            if (!offset.equals(BlockPos.ZERO)) {
                BlockPos connectedPos = crafter2.m_58899_().offset(offset);
                modifyAndUpdate(world, connectedPos, input -> {
                    input.attachTo(crafter1.m_58899_(), connectedPos);
                    crafter1.input.data.add(BlockPos.ZERO.subtract((Vec3i) input.data.get(0)));
                });
            }
        });
        crafter2.input.attachTo(crafter1.m_58899_(), crafter2.m_58899_());
        crafter1.input.data.add(BlockPos.ZERO.subtract((Vec3i) crafter2.input.data.get(0)));
    }

    private static void modifyAndUpdate(Level world, BlockPos pos, Consumer<ConnectedInputHandler.ConnectedInput> callback) {
        if (world.getBlockEntity(pos) instanceof MechanicalCrafterBlockEntity crafter) {
            callback.accept(crafter.input);
            crafter.m_6596_();
            crafter.connectivityChanged();
        }
    }

    public static class ConnectedInput {

        boolean isController;

        List<BlockPos> data = Collections.synchronizedList(new ArrayList());

        public ConnectedInput() {
            this.isController = true;
            this.data.add(BlockPos.ZERO);
        }

        public void attachTo(BlockPos controllerPos, BlockPos myPos) {
            this.isController = false;
            this.data.clear();
            this.data.add(controllerPos.subtract(myPos));
        }

        public IItemHandler getItemHandler(Level world, BlockPos pos) {
            if (!this.isController) {
                BlockPos controllerPos = pos.offset((Vec3i) this.data.get(0));
                ConnectedInputHandler.ConnectedInput input = CrafterHelper.getInput(world, controllerPos);
                return (IItemHandler) (input != this && input != null && input.isController ? input.getItemHandler(world, controllerPos) : new ItemStackHandler());
            } else {
                Direction facing = Direction.SOUTH;
                BlockState blockState = world.getBlockState(pos);
                if (blockState.m_61138_(MechanicalCrafterBlock.HORIZONTAL_FACING)) {
                    facing = (Direction) blockState.m_61143_(MechanicalCrafterBlock.HORIZONTAL_FACING);
                }
                Direction.AxisDirection axisDirection = facing.getAxisDirection();
                Direction.Axis compareAxis = facing.getClockWise().getAxis();
                Comparator<BlockPos> invOrdering = (p1, p2) -> {
                    int compareY = -Integer.compare(p1.m_123342_(), p2.m_123342_());
                    int modifier = axisDirection.getStep() * (compareAxis == Direction.Axis.Z ? -1 : 1);
                    int c1 = compareAxis.choose(p1.m_123341_(), p1.m_123342_(), p1.m_123343_());
                    int c2 = compareAxis.choose(p2.m_123341_(), p2.m_123342_(), p2.m_123343_());
                    return compareY != 0 ? compareY : modifier * Integer.compare(c1, c2);
                };
                List<IItemHandlerModifiable> list = (List<IItemHandlerModifiable>) this.data.stream().sorted(invOrdering).map(l -> CrafterHelper.getCrafter(world, pos.offset(l))).filter(Objects::nonNull).map(crafter -> crafter.getInventory()).collect(Collectors.toList());
                return new CombinedInvWrapper((IItemHandlerModifiable[]) Arrays.copyOf(list.toArray(), list.size(), IItemHandlerModifiable[].class));
            }
        }

        public void write(CompoundTag nbt) {
            nbt.putBoolean("Controller", this.isController);
            ListTag list = new ListTag();
            this.data.forEach(pos -> list.add(NbtUtils.writeBlockPos(pos)));
            nbt.put("Data", list);
        }

        public void read(CompoundTag nbt) {
            this.isController = nbt.getBoolean("Controller");
            this.data.clear();
            nbt.getList("Data", 10).forEach(inbt -> this.data.add(NbtUtils.readBlockPos((CompoundTag) inbt)));
            if (this.data.isEmpty()) {
                this.isController = true;
                this.data.add(BlockPos.ZERO);
            }
        }
    }
}