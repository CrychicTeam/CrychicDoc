package net.zanckor.questapi.example.common.handler.questreward;

import java.io.IOException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractReward;
import net.zanckor.questapi.api.file.quest.codec.server.ServerQuest;
import net.zanckor.questapi.api.file.quest.codec.server.ServerReward;
import net.zanckor.questapi.util.Util;

public class ItemReward extends AbstractReward {

    @Override
    public void handler(ServerPlayer player, ServerQuest serverQuest, int rewardIndex) throws IOException {
        String valueItem = ((ServerReward) serverQuest.getRewards().get(rewardIndex)).getTag();
        int quantity = ((ServerReward) serverQuest.getRewards().get(rewardIndex)).getAmount();
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(valueItem));
        ItemStack baseStack = new ItemStack(item, 1);
        float stackCount = (float) quantity / (float) baseStack.getMaxStackSize();
        for (int freeSlots = Util.getFreeSlots(player); stackCount >= 0.0F; stackCount--) {
            int stackQuantity = Math.min(quantity, baseStack.getMaxStackSize());
            ItemStack stack = new ItemStack(item, stackQuantity);
            if ((float) freeSlots >= stackCount) {
                player.m_36356_(stack);
            } else {
                player.drop(stack, false, false);
            }
            quantity -= baseStack.getMaxStackSize();
        }
    }
}