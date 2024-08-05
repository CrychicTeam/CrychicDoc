package fr.lucreeper74.createmetallurgy.content.light_bulb;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.ResetableLazy;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import fr.lucreeper74.createmetallurgy.content.light_bulb.network.address.NetworkAddressBehaviour;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LightBulbBlockEntity extends SmartBlockEntity {

    private boolean receivedSignalChanged;

    private NetworkAddressBehaviour addressBehaviour;

    private int receivedSignal;

    private int transmittedSignal;

    ResetableLazy<DyeColor> colorProvider;

    public LerpedFloat glow = LerpedFloat.linear();

    public LightBulbBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.colorProvider = ResetableLazy.of(() -> {
            BlockState blockState = this.m_58900_();
            return blockState.m_60734_() instanceof LightBulbBlock ? ((LightBulbBlock) blockState.m_60734_()).getColor() : DyeColor.WHITE;
        });
    }

    public DyeColor getColor() {
        return this.colorProvider.get();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public void addBehavioursDeferred(List<BlockEntityBehaviour> behaviours) {
        this.addressBehaviour = this.createAddressSlot();
        behaviours.add(this.addressBehaviour);
    }

    protected NetworkAddressBehaviour createAddressSlot() {
        return NetworkAddressBehaviour.networkNode(this, new LightBulbAddressSlot(), this::setSignal, this::getSignal);
    }

    public int getSignal() {
        return this.transmittedSignal;
    }

    public void setSignal(int strength) {
        if (this.receivedSignal != strength) {
            this.receivedSignalChanged = true;
        }
        this.receivedSignal = strength;
    }

    public void transmit(int strength) {
        this.transmittedSignal = strength;
        this.glow.chase((double) strength / 15.0, 0.5, LerpedFloat.Chaser.EXP);
        if (this.addressBehaviour != null) {
            this.addressBehaviour.notifySignalChange();
        }
    }

    @Override
    public void initialize() {
        if (this.addressBehaviour == null) {
            this.addressBehaviour = this.createAddressSlot();
            this.attachBehaviourLate(this.addressBehaviour);
        }
        super.initialize();
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("Receive", this.getReceivedSignal());
        compound.putBoolean("ReceivedChanged", this.receivedSignalChanged);
        compound.putInt("Transmit", this.transmittedSignal);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.receivedSignal = compound.getInt("Receive");
        this.receivedSignalChanged = compound.getBoolean("ReceivedChanged");
        if (this.f_58857_ == null || this.f_58857_.isClientSide) {
            this.transmittedSignal = compound.getInt("Transmit");
        }
    }

    @Override
    public void tick() {
        super.tick();
        BlockState blockState = this.m_58900_();
        int lightLevel = (Integer) blockState.m_61143_(LightBulbBlock.LEVEL);
        if (this.f_58857_.isClientSide) {
            this.glow.tickChaser();
            this.glow.chase((double) lightLevel, 0.2F, LerpedFloat.Chaser.EXP);
        }
        if (this.receivedSignal != lightLevel) {
            this.receivedSignalChanged = true;
            this.f_58857_.setBlockAndUpdate(this.f_58858_, (BlockState) blockState.m_61124_(LightBulbBlock.LEVEL, this.receivedSignal));
        }
        if (this.receivedSignalChanged) {
            this.f_58857_.m_6289_(this.m_58899_(), blockState.m_60734_());
            this.receivedSignalChanged = false;
        }
    }

    public int getReceivedSignal() {
        return this.receivedSignal;
    }

    @Override
    public void setBlockState(BlockState state) {
        super.m_155250_(state);
        this.colorProvider.reset();
    }
}