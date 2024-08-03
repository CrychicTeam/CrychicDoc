package dev.ftb.mods.ftbquests.block.entity;

import dev.ftb.mods.ftblibrary.integration.stages.StageHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class StageBarrierBlockEntity extends BlockEntity implements BarrierBlockEntity {

    private String stage = "";

    public StageBarrierBlockEntity(BlockPos blockPos, BlockState blockState) {
        super((BlockEntityType<?>) FTBQuestsBlockEntities.STAGE_BARRIER.get(), blockPos, blockState);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.stage = tag.getString("Stage");
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("Stage", this.stage);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.f_58857_ instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().blockChanged(this.m_58899_());
        }
    }

    @Override
    public void update(String stage) {
        this.stage = stage;
        this.setChanged();
    }

    @Override
    public boolean isOpen(Player player) {
        return !this.stage.isEmpty() && StageHelper.INSTANCE.getProvider().has(player, this.stage);
    }
}