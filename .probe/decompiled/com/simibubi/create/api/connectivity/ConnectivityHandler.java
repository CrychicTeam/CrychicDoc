package com.simibubi.create.api.connectivity;

import com.simibubi.create.content.fluids.tank.CreativeFluidTankBlockEntity;
import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.apache.commons.lang3.tuple.Pair;

public class ConnectivityHandler {

    public static <T extends BlockEntity & IMultiBlockEntityContainer> void formMulti(T be) {
        ConnectivityHandler.SearchCache<T> cache = new ConnectivityHandler.SearchCache<>();
        List<T> frontier = new ArrayList();
        frontier.add(be);
        formMulti(be.getType(), be.getLevel(), cache, frontier);
    }

    private static <T extends BlockEntity & IMultiBlockEntityContainer> void formMulti(BlockEntityType<?> type, BlockGetter level, ConnectivityHandler.SearchCache<T> cache, List<T> frontier) {
        PriorityQueue<Pair<Integer, T>> creationQueue = makeCreationQueue();
        Set<BlockPos> visited = new HashSet();
        Direction.Axis mainAxis = ((IMultiBlockEntityContainer) ((BlockEntity) frontier.get(0))).getMainConnectionAxis();
        int minX = mainAxis == Direction.Axis.Y ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        int minY = mainAxis != Direction.Axis.Y ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        int minZ = mainAxis == Direction.Axis.Y ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        for (T be : frontier) {
            BlockPos pos = be.getBlockPos();
            minX = Math.min(pos.m_123341_(), minX);
            minY = Math.min(pos.m_123342_(), minY);
            minZ = Math.min(pos.m_123343_(), minZ);
        }
        if (mainAxis == Direction.Axis.Y) {
            minX -= ((IMultiBlockEntityContainer) ((BlockEntity) frontier.get(0))).getMaxWidth();
        }
        if (mainAxis != Direction.Axis.Y) {
            minY -= ((IMultiBlockEntityContainer) ((BlockEntity) frontier.get(0))).getMaxWidth();
        }
        if (mainAxis == Direction.Axis.Y) {
            minZ -= ((IMultiBlockEntityContainer) ((BlockEntity) frontier.get(0))).getMaxWidth();
        }
        while (!frontier.isEmpty()) {
            T part = (T) frontier.remove(0);
            BlockPos partPos = part.getBlockPos();
            if (!visited.contains(partPos)) {
                visited.add(partPos);
                int amount = tryToFormNewMulti(part, cache, true);
                if (amount > 1) {
                    creationQueue.add(Pair.of(amount, part));
                }
                for (Direction.Axis axis : Iterate.axes) {
                    Direction dir = Direction.get(Direction.AxisDirection.NEGATIVE, axis);
                    BlockPos next = partPos.relative(dir);
                    if (next.m_123341_() > minX && next.m_123342_() > minY && next.m_123343_() > minZ && !visited.contains(next)) {
                        T nextBe = partAt(type, level, next);
                        if (nextBe != null && !nextBe.isRemoved()) {
                            frontier.add(nextBe);
                        }
                    }
                }
            }
        }
        visited.clear();
        while (!creationQueue.isEmpty()) {
            Pair<Integer, T> next = (Pair<Integer, T>) creationQueue.poll();
            T toCreate = (T) next.getValue();
            if (!visited.contains(toCreate.getBlockPos())) {
                visited.add(toCreate.getBlockPos());
                tryToFormNewMulti(toCreate, cache, false);
            }
        }
    }

    private static <T extends BlockEntity & IMultiBlockEntityContainer> int tryToFormNewMulti(T be, ConnectivityHandler.SearchCache<T> cache, boolean simulate) {
        int bestWidth = 1;
        int bestAmount = -1;
        if (!be.isController()) {
            return 0;
        } else {
            int radius = be.getMaxWidth();
            for (int w = 1; w <= radius; w++) {
                int amount = tryToFormNewMultiOfWidth(be, w, cache, true);
                if (amount >= bestAmount) {
                    bestWidth = w;
                    bestAmount = amount;
                }
            }
            if (!simulate) {
                int beWidth = be.getWidth();
                if (beWidth == bestWidth && beWidth * beWidth * be.getHeight() == bestAmount) {
                    return bestAmount;
                }
                splitMultiAndInvalidate(be, cache, false);
                if (be instanceof IMultiBlockEntityContainer.Fluid ifluid && ifluid.hasTank()) {
                    ifluid.setTankSize(0, bestAmount);
                }
                tryToFormNewMultiOfWidth(be, bestWidth, cache, false);
                be.preventConnectivityUpdate();
                be.setWidth(bestWidth);
                be.setHeight(bestAmount / bestWidth / bestWidth);
                be.notifyMultiUpdated();
            }
            return bestAmount;
        }
    }

    private static <T extends BlockEntity & IMultiBlockEntityContainer> int tryToFormNewMultiOfWidth(T be, int width, ConnectivityHandler.SearchCache<T> cache, boolean simulate) {
        int amount = 0;
        int height = 0;
        BlockEntityType<?> type = be.getType();
        Level level = be.getLevel();
        if (level == null) {
            return 0;
        } else {
            BlockPos origin = be.getBlockPos();
            IFluidTank beTank = null;
            FluidStack fluid = FluidStack.EMPTY;
            if (be instanceof IMultiBlockEntityContainer.Fluid ifluid && ifluid.hasTank()) {
                beTank = ifluid.getTank(0);
                fluid = beTank.getFluid();
            }
            Direction.Axis axis = be.getMainConnectionAxis();
            label193: for (int yOffset = 0; yOffset < be.getMaxLength(axis, width); yOffset++) {
                for (int xOffset = 0; xOffset < width; xOffset++) {
                    for (int zOffset = 0; zOffset < width; zOffset++) {
                        BlockPos pos = switch(axis) {
                            case X ->
                                origin.offset(yOffset, xOffset, zOffset);
                            case Y ->
                                origin.offset(xOffset, yOffset, zOffset);
                            case Z ->
                                origin.offset(xOffset, zOffset, yOffset);
                        };
                        Optional<T> part = cache.getOrCache(type, level, pos);
                        if (part.isEmpty()) {
                            break label193;
                        }
                        T controller = (T) part.get();
                        int otherWidth = controller.getWidth();
                        if (otherWidth > width || otherWidth == width && controller.getHeight() == be.getMaxLength(axis, width)) {
                            break label193;
                        }
                        Direction.Axis conAxis = controller.getMainConnectionAxis();
                        if (axis != conAxis) {
                            break label193;
                        }
                        BlockPos conPos = controller.getBlockPos();
                        if (!conPos.equals(origin) && (axis == Direction.Axis.Y ? conPos.m_123341_() < origin.m_123341_() || conPos.m_123343_() < origin.m_123343_() || conPos.m_123341_() + otherWidth > origin.m_123341_() + width || conPos.m_123343_() + otherWidth > origin.m_123343_() + width : axis == Direction.Axis.Z && conPos.m_123341_() < origin.m_123341_() || conPos.m_123342_() < origin.m_123342_() || axis == Direction.Axis.X && conPos.m_123343_() < origin.m_123343_() || axis == Direction.Axis.Z && conPos.m_123341_() + otherWidth > origin.m_123341_() + width || conPos.m_123342_() + otherWidth > origin.m_123342_() + width || axis == Direction.Axis.X && conPos.m_123343_() + otherWidth > origin.m_123343_() + width)) {
                            break label193;
                        }
                        if (controller instanceof IMultiBlockEntityContainer.Fluid ifluidCon && ifluidCon.hasTank()) {
                            FluidStack otherFluid = ifluidCon.getFluid(0);
                            if (!fluid.isEmpty() && !otherFluid.isEmpty() && !fluid.isFluidEqual(otherFluid)) {
                                break label193;
                            }
                        }
                    }
                }
                amount += width * width;
                height++;
            }
            if (simulate) {
                return amount;
            } else {
                Object extraData = be.getExtraData();
                for (int yOffset = 0; yOffset < height; yOffset++) {
                    for (int xOffset = 0; xOffset < width; xOffset++) {
                        for (int zOffset = 0; zOffset < width; zOffset++) {
                            BlockPos posx = switch(axis) {
                                case X ->
                                    origin.offset(yOffset, xOffset, zOffset);
                                case Y ->
                                    origin.offset(xOffset, yOffset, zOffset);
                                case Z ->
                                    origin.offset(xOffset, zOffset, yOffset);
                            };
                            T partx = partAt(type, level, posx);
                            if (partx != null && partx != be) {
                                extraData = be.modifyExtraData(extraData);
                                if (partx instanceof IMultiBlockEntityContainer.Fluid ifluidPart && ifluidPart.hasTank()) {
                                    IFluidTank tankAt = ifluidPart.getTank(0);
                                    FluidStack fluidAt = tankAt.getFluid();
                                    if (!fluidAt.isEmpty()) {
                                        if (beTank != null && fluid.isEmpty() && beTank instanceof CreativeFluidTankBlockEntity.CreativeSmartFluidTank) {
                                            ((CreativeFluidTankBlockEntity.CreativeSmartFluidTank) beTank).setContainedFluid(fluidAt);
                                        }
                                        if (be instanceof IMultiBlockEntityContainer.Fluid ifluidBE && ifluidBE.hasTank() && beTank != null) {
                                            beTank.fill(fluidAt, IFluidHandler.FluidAction.EXECUTE);
                                        }
                                    }
                                    tankAt.drain(tankAt.getCapacity(), IFluidHandler.FluidAction.EXECUTE);
                                }
                                splitMultiAndInvalidate(partx, cache, false);
                                partx.setController(origin);
                                partx.preventConnectivityUpdate();
                                cache.put(posx, be);
                                partx.setHeight(height);
                                partx.setWidth(width);
                                partx.notifyMultiUpdated();
                            }
                        }
                    }
                }
                be.setExtraData(extraData);
                be.notifyMultiUpdated();
                return amount;
            }
        }
    }

    public static <T extends BlockEntity & IMultiBlockEntityContainer> void splitMulti(T be) {
        splitMultiAndInvalidate(be, null, false);
    }

    private static <T extends BlockEntity & IMultiBlockEntityContainer> void splitMultiAndInvalidate(T be, @Nullable ConnectivityHandler.SearchCache<T> cache, boolean tryReconnect) {
        Level level = be.getLevel();
        if (level != null) {
            be = be.getControllerBE();
            if (be != null) {
                int height = be.getHeight();
                int width = be.getWidth();
                if (width != 1 || height != 1) {
                    BlockPos origin = be.getBlockPos();
                    List<T> frontier = new ArrayList();
                    Direction.Axis axis = be.getMainConnectionAxis();
                    FluidStack toDistribute = FluidStack.EMPTY;
                    int maxCapacity = 0;
                    if (be instanceof IMultiBlockEntityContainer.Fluid ifluidBE && ifluidBE.hasTank()) {
                        toDistribute = ifluidBE.getFluid(0);
                        maxCapacity = ifluidBE.getTankSize(0);
                        if (!toDistribute.isEmpty() && !be.isRemoved()) {
                            toDistribute.shrink(maxCapacity);
                        }
                        ifluidBE.setTankSize(0, 1);
                    }
                    for (int yOffset = 0; yOffset < height; yOffset++) {
                        for (int xOffset = 0; xOffset < width; xOffset++) {
                            for (int zOffset = 0; zOffset < width; zOffset++) {
                                BlockPos pos = switch(axis) {
                                    case X ->
                                        origin.offset(yOffset, xOffset, zOffset);
                                    case Y ->
                                        origin.offset(xOffset, yOffset, zOffset);
                                    case Z ->
                                        origin.offset(xOffset, zOffset, yOffset);
                                };
                                T partAt = partAt(be.getType(), level, pos);
                                if (partAt != null && partAt.getController().equals(origin)) {
                                    T controllerBE = partAt.getControllerBE();
                                    partAt.setExtraData(controllerBE == null ? null : controllerBE.getExtraData());
                                    partAt.removeController(true);
                                    if (!toDistribute.isEmpty() && partAt != be) {
                                        FluidStack copy = toDistribute.copy();
                                        IFluidTank tank = partAt instanceof IMultiBlockEntityContainer.Fluid ifluidPart ? ifluidPart.getTank(0) : null;
                                        if (tank instanceof CreativeFluidTankBlockEntity.CreativeSmartFluidTank creativeTank) {
                                            if (creativeTank.isEmpty()) {
                                                creativeTank.setContainedFluid(toDistribute);
                                            }
                                        } else {
                                            int split = Math.min(maxCapacity, toDistribute.getAmount());
                                            copy.setAmount(split);
                                            toDistribute.shrink(split);
                                            if (tank != null) {
                                                tank.fill(copy, IFluidHandler.FluidAction.EXECUTE);
                                            }
                                        }
                                    }
                                    if (tryReconnect) {
                                        frontier.add(partAt);
                                        partAt.preventConnectivityUpdate();
                                    }
                                    if (cache != null) {
                                        cache.put(pos, partAt);
                                    }
                                }
                            }
                        }
                    }
                    if (be instanceof IMultiBlockEntityContainer.Inventory inv && inv.hasInventory()) {
                        be.getCapability(ForgeCapabilities.ITEM_HANDLER).invalidate();
                    }
                    if (be instanceof IMultiBlockEntityContainer.Fluid fluid && fluid.hasTank()) {
                        be.getCapability(ForgeCapabilities.FLUID_HANDLER).invalidate();
                    }
                    if (tryReconnect) {
                        formMulti(be.getType(), level, cache == null ? new ConnectivityHandler.SearchCache<>() : cache, frontier);
                    }
                }
            }
        }
    }

    private static <T extends BlockEntity & IMultiBlockEntityContainer> PriorityQueue<Pair<Integer, T>> makeCreationQueue() {
        return new PriorityQueue((one, two) -> (Integer) two.getKey() - (Integer) one.getKey());
    }

    @Nullable
    public static <T extends BlockEntity & IMultiBlockEntityContainer> T partAt(BlockEntityType<?> type, BlockGetter level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        return be != null && be.getType() == type && !be.isRemoved() ? checked(be) : null;
    }

    public static <T extends BlockEntity & IMultiBlockEntityContainer> boolean isConnected(BlockGetter level, BlockPos pos, BlockPos other) {
        T one = checked(level.getBlockEntity(pos));
        T two = checked(level.getBlockEntity(other));
        return one != null && two != null ? one.getController().equals(two.getController()) : false;
    }

    @Nullable
    private static <T extends BlockEntity & IMultiBlockEntityContainer> T checked(BlockEntity be) {
        return (T) (be instanceof IMultiBlockEntityContainer ? be : null);
    }

    private static class SearchCache<T extends BlockEntity & IMultiBlockEntityContainer> {

        Map<BlockPos, Optional<T>> controllerMap = new HashMap();

        public SearchCache() {
        }

        void put(BlockPos pos, T target) {
            this.controllerMap.put(pos, Optional.of(target));
        }

        void putEmpty(BlockPos pos) {
            this.controllerMap.put(pos, Optional.empty());
        }

        boolean hasVisited(BlockPos pos) {
            return this.controllerMap.containsKey(pos);
        }

        Optional<T> getOrCache(BlockEntityType<?> type, BlockGetter level, BlockPos pos) {
            if (this.hasVisited(pos)) {
                return (Optional<T>) this.controllerMap.get(pos);
            } else {
                T partAt = ConnectivityHandler.partAt(type, level, pos);
                if (partAt == null) {
                    this.putEmpty(pos);
                    return Optional.empty();
                } else {
                    T controller = ConnectivityHandler.checked(level.getBlockEntity(partAt.getController()));
                    if (controller == null) {
                        this.putEmpty(pos);
                        return Optional.empty();
                    } else {
                        this.put(pos, controller);
                        return Optional.of(controller);
                    }
                }
            }
        }
    }
}