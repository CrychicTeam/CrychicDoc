package dev.ftb.mods.ftbquests.block.entity;

import dev.ftb.mods.ftbquests.api.FTBQuestsAPI;
import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.QuestObject;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
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

public class QuestBarrierBlockEntity extends BlockEntity implements BarrierBlockEntity {

    private long objId = 0L;

    public QuestBarrierBlockEntity(BlockPos blockPos, BlockState blockState) {
        super((BlockEntityType<?>) FTBQuestsBlockEntities.BARRIER.get(), blockPos, blockState);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.objId = QuestObjectBase.parseCodeString(tag.getString("Object"));
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("Object", QuestObjectBase.getCodeString(this.objId));
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
    public void update(String s) {
        this.objId = ServerQuestFile.INSTANCE.getID(s);
        this.setChanged();
    }

    @Override
    public boolean isOpen(Player player) {
        BaseQuestFile file = FTBQuestsAPI.api().getQuestFile(player.m_9236_().isClientSide());
        QuestObject qo = file.get(this.objId);
        return qo != null && file.getOrCreateTeamData(player).isCompleted(qo);
    }
}