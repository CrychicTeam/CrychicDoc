package dev.ftb.mods.ftbquests.block.entity;

import dev.ftb.mods.ftbquests.block.QuestBarrierBlock;
import dev.ftb.mods.ftbquests.client.FTBQuestsClient;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface BarrierBlockEntity {

    void update(String var1);

    boolean isOpen(Player var1);

    static void tick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        if (blockEntity instanceof BarrierBlockEntity barrier && level.isClientSide && FTBQuestsClient.isClientDataLoaded() && level.getGameTime() % 5L == 0L) {
            boolean completed = barrier.isOpen(FTBQuestsClient.getClientPlayer());
            if (completed != (Boolean) blockState.m_61143_(QuestBarrierBlock.OPEN)) {
                level.setBlock(blockPos, (BlockState) blockState.m_61124_(QuestBarrierBlock.OPEN, completed), 10);
                blockEntity.setChanged();
            }
        }
    }
}