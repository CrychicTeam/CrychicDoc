package com.simibubi.create.content.contraptions.chassis;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllKeys;
import com.simibubi.create.content.contraptions.BlockMovementChecks;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.CenteredSideValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBoard;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsFormatter;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.BulkScrollValueBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

public class ChassisBlockEntity extends SmartBlockEntity {

    ScrollValueBehaviour range;

    public int currentlySelectedRange;

    public ChassisBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        int max = AllConfigs.server().kinetics.maxChassisRange.get();
        this.range = new ChassisBlockEntity.ChassisScrollValueBehaviour(Lang.translateDirect("contraptions.chassis.range"), this, new CenteredSideValueBoxTransform(), be -> ((ChassisBlockEntity) be).collectChassisGroup());
        this.range.requiresWrench();
        this.range.between(1, max);
        this.range.withClientCallback(i -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ChassisRangeDisplay.display(this)));
        this.range.setValue(max / 2);
        this.range.withFormatter(s -> String.valueOf(this.currentlySelectedRange));
        behaviours.add(this.range);
        this.currentlySelectedRange = this.range.getValue();
    }

    @Override
    public void initialize() {
        super.initialize();
        if (this.m_58900_().m_60734_() instanceof RadialChassisBlock) {
            this.range.setLabel(Lang.translateDirect("contraptions.chassis.radius"));
        }
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        if (clientPacket) {
            this.currentlySelectedRange = this.getRange();
        }
    }

    public int getRange() {
        return this.range.getValue();
    }

    public List<BlockPos> getIncludedBlockPositions(Direction forcedMovement, boolean visualize) {
        if (!(this.m_58900_().m_60734_() instanceof AbstractChassisBlock)) {
            return Collections.emptyList();
        } else {
            return this.isRadial() ? this.getIncludedBlockPositionsRadial(forcedMovement, visualize) : this.getIncludedBlockPositionsLinear(forcedMovement, visualize);
        }
    }

    protected boolean isRadial() {
        return this.f_58857_.getBlockState(this.f_58858_).m_60734_() instanceof RadialChassisBlock;
    }

    public List<ChassisBlockEntity> collectChassisGroup() {
        Queue<BlockPos> frontier = new LinkedList();
        List<ChassisBlockEntity> collected = new ArrayList();
        Set<BlockPos> visited = new HashSet();
        frontier.add(this.f_58858_);
        while (!frontier.isEmpty()) {
            BlockPos current = (BlockPos) frontier.poll();
            if (!visited.contains(current)) {
                visited.add(current);
                if (this.f_58857_.getBlockEntity(current) instanceof ChassisBlockEntity chassis) {
                    collected.add(chassis);
                    visited.add(current);
                    chassis.addAttachedChasses(frontier, visited);
                }
            }
        }
        return collected;
    }

    public boolean addAttachedChasses(Queue<BlockPos> frontier, Set<BlockPos> visited) {
        BlockState state = this.m_58900_();
        if (!(state.m_60734_() instanceof AbstractChassisBlock)) {
            return false;
        } else {
            Direction.Axis axis = (Direction.Axis) state.m_61143_(AbstractChassisBlock.f_55923_);
            if (this.isRadial()) {
                for (int offset : new int[] { -1, 1 }) {
                    Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, axis);
                    BlockPos currentPos = this.f_58858_.relative(direction, offset);
                    if (!this.f_58857_.isLoaded(currentPos)) {
                        return false;
                    }
                    BlockState neighbourState = this.f_58857_.getBlockState(currentPos);
                    if (AllBlocks.RADIAL_CHASSIS.has(neighbourState) && axis == neighbourState.m_61143_(BlockStateProperties.AXIS) && !visited.contains(currentPos)) {
                        frontier.add(currentPos);
                    }
                }
                return true;
            } else {
                for (Direction offset : Iterate.directions) {
                    BlockPos current = this.f_58858_.relative(offset);
                    if (!visited.contains(current)) {
                        if (!this.f_58857_.isLoaded(current)) {
                            return false;
                        }
                        BlockState neighbourState = this.f_58857_.getBlockState(current);
                        if (LinearChassisBlock.isChassis(neighbourState) && LinearChassisBlock.sameKind(state, neighbourState) && neighbourState.m_61143_(LinearChassisBlock.f_55923_) == axis) {
                            frontier.add(current);
                        }
                    }
                }
                return true;
            }
        }
    }

    private List<BlockPos> getIncludedBlockPositionsLinear(Direction forcedMovement, boolean visualize) {
        List<BlockPos> positions = new ArrayList();
        BlockState state = this.m_58900_();
        AbstractChassisBlock block = (AbstractChassisBlock) state.m_60734_();
        Direction.Axis axis = (Direction.Axis) state.m_61143_(AbstractChassisBlock.f_55923_);
        Direction facing = Direction.get(Direction.AxisDirection.POSITIVE, axis);
        int chassisRange = visualize ? this.currentlySelectedRange : this.getRange();
        for (int offset : new int[] { 1, -1 }) {
            if (offset == -1) {
                facing = facing.getOpposite();
            }
            boolean sticky = (Boolean) state.m_61143_(block.getGlueableSide(state, facing));
            for (int i = 1; i <= chassisRange; i++) {
                BlockPos current = this.f_58858_.relative(facing, i);
                BlockState currentState = this.f_58857_.getBlockState(current);
                if (forcedMovement != facing && !sticky || !BlockMovementChecks.isMovementNecessary(currentState, this.f_58857_, current) || BlockMovementChecks.isBrittle(currentState)) {
                    break;
                }
                positions.add(current);
                if (BlockMovementChecks.isNotSupportive(currentState, facing)) {
                    break;
                }
            }
        }
        return positions;
    }

    private List<BlockPos> getIncludedBlockPositionsRadial(Direction forcedMovement, boolean visualize) {
        List<BlockPos> positions = new ArrayList();
        BlockState state = this.f_58857_.getBlockState(this.f_58858_);
        Direction.Axis axis = (Direction.Axis) state.m_61143_(AbstractChassisBlock.f_55923_);
        AbstractChassisBlock block = (AbstractChassisBlock) state.m_60734_();
        int chassisRange = visualize ? this.currentlySelectedRange : this.getRange();
        for (Direction facing : Iterate.directions) {
            if (facing.getAxis() != axis && (Boolean) state.m_61143_(block.getGlueableSide(state, facing))) {
                BlockPos startPos = this.f_58858_.relative(facing);
                List<BlockPos> localFrontier = new LinkedList();
                Set<BlockPos> localVisited = new HashSet();
                localFrontier.add(startPos);
                while (!localFrontier.isEmpty()) {
                    BlockPos searchPos = (BlockPos) localFrontier.remove(0);
                    BlockState searchedState = this.f_58857_.getBlockState(searchPos);
                    if (!localVisited.contains(searchPos) && searchPos.m_123314_(this.f_58858_, (double) ((float) chassisRange + 0.5F)) && BlockMovementChecks.isMovementNecessary(searchedState, this.f_58857_, searchPos) && !BlockMovementChecks.isBrittle(searchedState)) {
                        localVisited.add(searchPos);
                        if (!searchPos.equals(this.f_58858_)) {
                            positions.add(searchPos);
                        }
                        for (Direction offset : Iterate.directions) {
                            if (offset.getAxis() != axis && (!searchPos.equals(this.f_58858_) || offset == facing) && !BlockMovementChecks.isNotSupportive(searchedState, offset)) {
                                localFrontier.add(searchPos.relative(offset));
                            }
                        }
                    }
                }
            }
        }
        return positions;
    }

    class ChassisScrollValueBehaviour extends BulkScrollValueBehaviour {

        public ChassisScrollValueBehaviour(Component label, SmartBlockEntity be, ValueBoxTransform slot, Function<SmartBlockEntity, List<? extends SmartBlockEntity>> groupGetter) {
            super(label, be, slot, groupGetter);
        }

        @Override
        public ValueSettingsBoard createBoard(Player player, BlockHitResult hitResult) {
            ImmutableList<Component> rows = ImmutableList.of(Lang.translateDirect("contraptions.chassis.distance"));
            ValueSettingsFormatter formatter = new ValueSettingsFormatter(vs -> new ValueSettingsBehaviour.ValueSettings(vs.row(), vs.value() + 1).format());
            return new ValueSettingsBoard(this.label, this.max - 1, 1, rows, formatter);
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public void newSettingHovered(ValueSettingsBehaviour.ValueSettings valueSetting) {
            if (ChassisBlockEntity.this.f_58857_.isClientSide) {
                if (!AllKeys.ctrlDown()) {
                    ChassisBlockEntity.this.currentlySelectedRange = valueSetting.value() + 1;
                } else {
                    for (SmartBlockEntity be : this.getBulk()) {
                        if (be instanceof ChassisBlockEntity cbe) {
                            cbe.currentlySelectedRange = valueSetting.value() + 1;
                        }
                    }
                }
                ChassisRangeDisplay.display(ChassisBlockEntity.this);
            }
        }

        @Override
        public void setValueSettings(Player player, ValueSettingsBehaviour.ValueSettings vs, boolean ctrlHeld) {
            super.setValueSettings(player, new ValueSettingsBehaviour.ValueSettings(vs.row(), vs.value() + 1), ctrlHeld);
        }

        @Override
        public ValueSettingsBehaviour.ValueSettings getValueSettings() {
            ValueSettingsBehaviour.ValueSettings vs = super.getValueSettings();
            return new ValueSettingsBehaviour.ValueSettings(vs.row(), vs.value() - 1);
        }

        @Override
        public String getClipboardKey() {
            return "Chassis";
        }
    }
}