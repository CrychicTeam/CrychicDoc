package com.simibubi.create.content.contraptions.chassis;

import com.jozufozu.flywheel.backend.instancing.InstancedRenderDispatcher;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.glue.SuperGlueEntity;
import com.simibubi.create.content.contraptions.glue.SuperGlueItem;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

public class StickerBlockEntity extends SmartBlockEntity {

    LerpedFloat piston = LerpedFloat.linear();

    boolean update = false;

    public StickerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public void initialize() {
        super.initialize();
        if (this.f_58857_.isClientSide) {
            this.piston.startWithValue(this.isBlockStateExtended() ? 1.0 : 0.0);
        }
    }

    public boolean isBlockStateExtended() {
        BlockState blockState = this.m_58900_();
        return AllBlocks.STICKER.has(blockState) && (Boolean) blockState.m_61143_(StickerBlock.EXTENDED);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.f_58857_.isClientSide) {
            this.piston.tickChaser();
            if (this.isAttachedToBlock() && this.piston.getValue(0.0F) != this.piston.getValue() && this.piston.getValue() == 1.0F) {
                SuperGlueItem.spawnParticles(this.f_58857_, this.f_58858_, (Direction) this.m_58900_().m_61143_(StickerBlock.f_52588_), true);
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.playSound(true));
            }
            if (this.update) {
                this.update = false;
                int target = this.isBlockStateExtended() ? 1 : 0;
                if (this.isAttachedToBlock() && target == 0 && this.piston.getChaseTarget() == 1.0F) {
                    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.playSound(false));
                }
                this.piston.chase((double) target, 0.4F, LerpedFloat.Chaser.LINEAR);
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> InstancedRenderDispatcher.enqueueUpdate(this));
            }
        }
    }

    public boolean isAttachedToBlock() {
        BlockState blockState = this.m_58900_();
        if (!AllBlocks.STICKER.has(blockState)) {
            return false;
        } else {
            Direction direction = (Direction) blockState.m_61143_(StickerBlock.f_52588_);
            return SuperGlueEntity.isValidFace(this.f_58857_, this.f_58858_.relative(direction), direction.getOpposite());
        }
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        if (clientPacket) {
            this.update = true;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void playSound(boolean attach) {
        AllSoundEvents.SLIME_ADDED.play(this.f_58857_, Minecraft.getInstance().player, this.f_58858_, 0.35F, attach ? 0.75F : 0.2F);
    }
}