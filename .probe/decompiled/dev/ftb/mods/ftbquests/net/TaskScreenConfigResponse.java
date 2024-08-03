package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.block.TaskScreenBlock;
import dev.ftb.mods.ftbquests.block.entity.TaskScreenBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class TaskScreenConfigResponse extends BaseC2SMessage {

    private final BlockPos pos;

    private final CompoundTag payload;

    public TaskScreenConfigResponse(TaskScreenBlockEntity taskScreenBlockEntity) {
        this.pos = taskScreenBlockEntity.m_58899_();
        this.payload = taskScreenBlockEntity.m_187482_();
    }

    public TaskScreenConfigResponse(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.payload = buf.readNbt();
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.TASK_SCREEN_CONFIG_RESP;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeNbt(this.payload);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof ServerPlayer serverPlayer && serverPlayer.m_9236_().getBlockEntity(this.pos) instanceof TaskScreenBlockEntity taskScreen && TaskScreenBlock.hasPermissionToEdit(serverPlayer, taskScreen)) {
            taskScreen.load(this.payload);
            serverPlayer.m_9236_().sendBlockUpdated(taskScreen.m_58899_(), taskScreen.m_58900_(), taskScreen.m_58900_(), 3);
            taskScreen.m_6596_();
        }
    }
}