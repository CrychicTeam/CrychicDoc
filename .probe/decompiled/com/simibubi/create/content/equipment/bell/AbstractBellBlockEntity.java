package com.simibubi.create.content.equipment.bell;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.NBTHelper;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class AbstractBellBlockEntity extends SmartBlockEntity {

    public static final int RING_DURATION = 74;

    public boolean isRinging;

    public int ringingTicks;

    public Direction ringDirection;

    public AbstractBellBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    public boolean ring(Level world, BlockPos pos, Direction direction) {
        this.isRinging = true;
        this.ringingTicks = 0;
        this.ringDirection = direction;
        this.sendData();
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isRinging) {
            this.ringingTicks++;
        }
        if (this.ringingTicks >= 74) {
            this.isRinging = false;
            this.ringingTicks = 0;
        }
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        if (clientPacket && this.ringingTicks == 0 && this.isRinging) {
            NBTHelper.writeEnum(tag, "Ringing", this.ringDirection);
        }
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        if (clientPacket && tag.contains("Ringing")) {
            this.ringDirection = NBTHelper.readEnum(tag, "Ringing", Direction.class);
            this.ringingTicks = 0;
            this.isRinging = true;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public abstract PartialModel getBellModel();
}