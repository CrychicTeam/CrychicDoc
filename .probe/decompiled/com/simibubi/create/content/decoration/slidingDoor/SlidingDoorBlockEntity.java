package com.simibubi.create.content.decoration.slidingDoor;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class SlidingDoorBlockEntity extends SmartBlockEntity {

    LerpedFloat animation;

    int bridgeTicks;

    boolean deferUpdate;

    public SlidingDoorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.animation = LerpedFloat.linear().startWithValue(isOpen(state) ? 1.0 : 0.0);
    }

    @Override
    public void tick() {
        if (this.deferUpdate && !this.f_58857_.isClientSide()) {
            this.deferUpdate = false;
            BlockState blockState = this.m_58900_();
            blockState.m_60690_(this.f_58857_, this.f_58858_, Blocks.AIR, this.f_58858_, false);
        }
        super.tick();
        boolean open = isOpen(this.m_58900_());
        boolean wasSettled = this.animation.settled();
        this.animation.chase(open ? 1.0 : 0.0, 0.15F, LerpedFloat.Chaser.LINEAR);
        this.animation.tickChaser();
        if (!this.f_58857_.isClientSide()) {
            if (!open && !wasSettled && this.animation.settled() && !this.isVisible(this.m_58900_())) {
                this.showBlockModel();
            }
        } else {
            if (this.bridgeTicks < 2 && open) {
                this.bridgeTicks++;
            } else if (this.bridgeTicks > 0 && !open && this.isVisible(this.m_58900_())) {
                this.bridgeTicks--;
            }
        }
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().inflate(1.0);
    }

    protected boolean isVisible(BlockState state) {
        return (Boolean) state.m_61145_(SlidingDoorBlock.VISIBLE).orElse(true);
    }

    protected boolean shouldRenderSpecial(BlockState state) {
        return !this.isVisible(state) || this.bridgeTicks != 0;
    }

    protected void showBlockModel() {
        this.f_58857_.setBlock(this.f_58858_, (BlockState) this.m_58900_().m_61124_(SlidingDoorBlock.VISIBLE, true), 3);
        this.f_58857_.playSound(null, this.f_58858_, SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 0.5F, 1.0F);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    public static boolean isOpen(BlockState state) {
        return (Boolean) state.m_61145_(DoorBlock.OPEN).orElse(false);
    }
}