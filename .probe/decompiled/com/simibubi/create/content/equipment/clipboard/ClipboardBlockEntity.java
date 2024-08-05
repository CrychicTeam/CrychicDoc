package com.simibubi.create.content.equipment.clipboard;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

public class ClipboardBlockEntity extends SmartBlockEntity {

    public ItemStack dataContainer = AllBlocks.CLIPBOARD.asStack();

    private UUID lastEdit;

    public ClipboardBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void initialize() {
        super.initialize();
        this.updateWrittenState();
    }

    public void onEditedBy(Player player) {
        this.lastEdit = player.m_20148_();
        this.notifyUpdate();
        this.updateWrittenState();
    }

    public void updateWrittenState() {
        BlockState blockState = this.m_58900_();
        if (AllBlocks.CLIPBOARD.has(blockState)) {
            if (!this.f_58857_.isClientSide()) {
                boolean isWritten = (Boolean) blockState.m_61143_(ClipboardBlock.WRITTEN);
                boolean shouldBeWritten = this.dataContainer.getTag() != null;
                if (isWritten != shouldBeWritten) {
                    this.f_58857_.setBlockAndUpdate(this.f_58858_, (BlockState) blockState.m_61124_(ClipboardBlock.WRITTEN, shouldBeWritten));
                }
            }
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        tag.put("Item", this.dataContainer.serializeNBT());
        if (clientPacket && this.lastEdit != null) {
            tag.putUUID("LastEdit", this.lastEdit);
        }
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        this.dataContainer = ItemStack.of(tag.getCompound("Item"));
        if (clientPacket) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.readClientSide(tag));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void readClientSide(CompoundTag tag) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen instanceof ClipboardScreen cs) {
            if (!tag.contains("LastEdit") || !tag.getUUID("LastEdit").equals(mc.player.m_20148_())) {
                if (this.f_58858_.equals(cs.targetedBlock)) {
                    cs.reopenWith(this.dataContainer);
                }
            }
        }
    }
}