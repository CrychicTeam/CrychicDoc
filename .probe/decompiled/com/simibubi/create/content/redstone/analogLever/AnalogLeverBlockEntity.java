package com.simibubi.create.content.redstone.analogLever;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AnalogLeverBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    int state = 0;

    int lastChange;

    LerpedFloat clientState = LerpedFloat.linear();

    public AnalogLeverBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("State", this.state);
        compound.putInt("ChangeTimer", this.lastChange);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        this.state = compound.getInt("State");
        this.lastChange = compound.getInt("ChangeTimer");
        this.clientState.chase((double) this.state, 0.2F, LerpedFloat.Chaser.EXP);
        super.read(compound, clientPacket);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.lastChange > 0) {
            this.lastChange--;
            if (this.lastChange == 0) {
                this.updateOutput();
            }
        }
        if (this.f_58857_.isClientSide) {
            this.clientState.tickChaser();
        }
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    private void updateOutput() {
        AnalogLeverBlock.updateNeighbors(this.m_58900_(), this.f_58857_, this.f_58858_);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    public void changeState(boolean back) {
        int prevState = this.state;
        this.state += back ? -1 : 1;
        this.state = Mth.clamp(this.state, 0, 15);
        if (prevState != this.state) {
            this.lastChange = 15;
        }
        this.sendData();
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("tooltip.analogStrength", this.state)));
        return true;
    }

    public int getState() {
        return this.state;
    }
}