package com.simibubi.create.content.logistics.tunnel;

import com.jozufozu.flywheel.backend.instancing.InstancedRenderDispatcher;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPackets;
import com.simibubi.create.content.logistics.funnel.BeltFunnelBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.tuple.Pair;

public class BeltTunnelBlockEntity extends SmartBlockEntity {

    public Map<Direction, LerpedFloat> flaps;

    public Set<Direction> sides;

    protected LazyOptional<IItemHandler> cap = LazyOptional.empty();

    protected List<Pair<Direction, Boolean>> flapsToSend;

    public BeltTunnelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.flaps = new EnumMap(Direction.class);
        this.sides = new HashSet();
        this.flapsToSend = new LinkedList();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.cap.invalidate();
    }

    protected void writeFlapsAndSides(CompoundTag compound) {
        ListTag flapsNBT = new ListTag();
        for (Direction direction : this.flaps.keySet()) {
            flapsNBT.add(IntTag.valueOf(direction.get3DDataValue()));
        }
        compound.put("Flaps", flapsNBT);
        ListTag sidesNBT = new ListTag();
        for (Direction direction : this.sides) {
            sidesNBT.add(IntTag.valueOf(direction.get3DDataValue()));
        }
        compound.put("Sides", sidesNBT);
    }

    @Override
    public void writeSafe(CompoundTag tag) {
        this.writeFlapsAndSides(tag);
        super.writeSafe(tag);
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        this.writeFlapsAndSides(compound);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        Set<Direction> newFlaps = new HashSet(6);
        for (Tag inbt : compound.getList("Flaps", 3)) {
            if (inbt instanceof IntTag) {
                newFlaps.add(Direction.from3DDataValue(((IntTag) inbt).getAsInt()));
            }
        }
        this.sides.clear();
        for (Tag inbtx : compound.getList("Sides", 3)) {
            if (inbtx instanceof IntTag) {
                this.sides.add(Direction.from3DDataValue(((IntTag) inbtx).getAsInt()));
            }
        }
        for (Direction d : Iterate.directions) {
            if (!newFlaps.contains(d)) {
                this.flaps.remove(d);
            } else if (!this.flaps.containsKey(d)) {
                this.flaps.put(d, this.createChasingFlap());
            }
        }
        if (!compound.contains("Sides") && compound.contains("Flaps")) {
            this.sides.addAll(this.flaps.keySet());
        }
        super.read(compound, clientPacket);
        if (clientPacket) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> InstancedRenderDispatcher.enqueueUpdate(this));
        }
    }

    private LerpedFloat createChasingFlap() {
        return LerpedFloat.linear().startWithValue(0.25).chase(0.0, 0.05F, LerpedFloat.Chaser.EXP);
    }

    public void updateTunnelConnections() {
        this.flaps.clear();
        this.sides.clear();
        BlockState tunnelState = this.m_58900_();
        for (Direction direction : Iterate.horizontalDirections) {
            if (direction.getAxis() != tunnelState.m_61143_(BlockStateProperties.HORIZONTAL_AXIS)) {
                boolean positive = direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ^ direction.getAxis() == Direction.Axis.Z;
                BeltTunnelBlock.Shape shape = (BeltTunnelBlock.Shape) tunnelState.m_61143_(BeltTunnelBlock.SHAPE);
                if (BeltTunnelBlock.isStraight(tunnelState) || positive && shape == BeltTunnelBlock.Shape.T_LEFT || !positive && shape == BeltTunnelBlock.Shape.T_RIGHT) {
                    continue;
                }
            }
            this.sides.add(direction);
            BlockState nextState = this.f_58857_.getBlockState(this.f_58858_.relative(direction));
            if (!(nextState.m_60734_() instanceof BeltTunnelBlock) && (!(nextState.m_60734_() instanceof BeltFunnelBlock) || nextState.m_61143_(BeltFunnelBlock.SHAPE) != BeltFunnelBlock.Shape.EXTENDED || nextState.m_61143_(BeltFunnelBlock.HORIZONTAL_FACING) != direction.getOpposite())) {
                this.flaps.put(direction, this.createChasingFlap());
            }
        }
        this.sendData();
    }

    public void flap(Direction side, boolean inward) {
        if (this.f_58857_.isClientSide) {
            if (this.flaps.containsKey(side)) {
                ((LerpedFloat) this.flaps.get(side)).setValue(inward ^ side.getAxis() == Direction.Axis.Z ? -1.0 : 1.0);
            }
        } else {
            this.flapsToSend.add(Pair.of(side, inward));
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        this.updateTunnelConnections();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.f_58857_.isClientSide) {
            if (!this.flapsToSend.isEmpty()) {
                this.sendFlaps();
            }
        } else {
            this.flaps.forEach((d, value) -> value.tickChaser());
        }
    }

    private void sendFlaps() {
        AllPackets.getChannel().send(this.packetTarget(), new TunnelFlapPacket(this, this.flapsToSend));
        this.flapsToSend.clear();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
        if (capability != ForgeCapabilities.ITEM_HANDLER) {
            return super.getCapability(capability, side);
        } else {
            if (!this.cap.isPresent() && AllBlocks.BELT.has(this.f_58857_.getBlockState(this.f_58858_.below()))) {
                BlockEntity teBelow = this.f_58857_.getBlockEntity(this.f_58858_.below());
                if (teBelow != null) {
                    T capBelow = (T) teBelow.getCapability(capability, Direction.UP).orElse(null);
                    if (capBelow != null) {
                        this.cap = LazyOptional.of(() -> capBelow).cast();
                    }
                }
            }
            return this.cap.cast();
        }
    }
}