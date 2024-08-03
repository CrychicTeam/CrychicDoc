package net.zanckor.questapi.example.common.handler.questreward;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractReward;
import net.zanckor.questapi.api.file.quest.codec.server.ServerQuest;
import net.zanckor.questapi.api.file.quest.codec.server.ServerReward;
import net.zanckor.questapi.util.Util;

public class LootTableReward extends AbstractReward {

    @Override
    public void handler(ServerPlayer player, ServerQuest serverQuest, int rewardIndex) throws IOException {
        String lootTableRL = ((ServerReward) serverQuest.getRewards().get(rewardIndex)).getTag();
        int rolls = ((ServerReward) serverQuest.getRewards().get(rewardIndex)).getAmount();
        MinecraftServer server = player.server;
        List<ItemStack> itemStackList = new ArrayList();
        ResourceLocation rl = new ResourceLocation(lootTableRL);
        LootTable lootTable = server.getLootData().m_278676_(rl);
        for (int actualRoll = 0; actualRoll < rolls; actualRoll++) {
            LootParams lootparams = new LootParams.Builder((ServerLevel) player.m_9236_()).create(LootContextParamSets.EMPTY);
            itemStackList = lootTable.getRandomItems(lootparams);
        }
        for (ItemStack itemStack : itemStackList) {
            if (Util.getFreeSlots(player) > 0) {
                player.m_150109_().add(itemStack);
            } else {
                player.drop(itemStack, false, false);
            }
        }
    }
}