package net.zanckor.questapi.example.common.handler.questgoal;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.zanckor.questapi.api.file.quest.codec.user.UserGoal;
import net.zanckor.questapi.api.file.quest.codec.user.UserQuest;
import net.zanckor.questapi.mod.common.questhandler.ForgeAbstractGoal;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumquest.EnumGoalType;
import net.zanckor.questapi.util.GsonManager;
import net.zanckor.questapi.util.Util;

public class CollectGoal extends ForgeAbstractGoal {

    @Override
    public void handler(ServerPlayer player, Entity entity, Gson gson, File file, UserQuest userQuest, int indexGoal, Enum questType) throws IOException {
        String questID = userQuest.getId();
        userQuest = (UserQuest) GsonManager.getJsonClass(file, UserQuest.class);
        if (userQuest != null && questID.equals(userQuest.getId())) {
            this.updateData(player, file);
            super.handler(player, entity, gson, file, userQuest, indexGoal, questType);
        }
    }

    @Override
    public void updateData(ServerPlayer player, File file) throws IOException {
        UserQuest userQuest = (UserQuest) GsonManager.getJsonClass(file, UserQuest.class);
        if (userQuest != null) {
            for (UserGoal questGoal : userQuest.getQuestGoals()) {
                if (questGoal.getType().equals(EnumGoalType.COLLECT.name())) {
                    String valueItem = questGoal.getTarget();
                    Item itemTarget = ForgeRegistries.ITEMS.getValue(new ResourceLocation(valueItem));
                    if (!player.m_150109_().contains(itemTarget.getDefaultInstance())) {
                        questGoal.setCurrentAmount(0);
                    } else {
                        int itemSlot = player.m_150109_().findSlotMatchingItem(itemTarget.getDefaultInstance());
                        ItemStack item = player.m_150109_().getItem(itemSlot);
                        int itemCount = item.getCount() > questGoal.getAmount() ? questGoal.getAmount() : item.getCount();
                        questGoal.setCurrentAmount(itemCount);
                    }
                    GsonManager.writeJson(file, userQuest);
                }
            }
        }
    }

    public static void removeItems(ServerPlayer player, UserGoal goalEnhanced) {
        if (goalEnhanced.getType().equals(EnumGoalType.COLLECT.name())) {
            String valueItem = goalEnhanced.getTarget();
            Item itemTarget = ForgeRegistries.ITEMS.getValue(new ResourceLocation(valueItem));
            int itemSlot = player.m_150109_().findSlotMatchingItem(itemTarget.getDefaultInstance());
            if (itemSlot >= 0) {
                player.m_150109_().removeItem(itemSlot, goalEnhanced.getAmount());
            }
        }
    }

    @Override
    public void enhancedCompleteQuest(ServerPlayer player, File file, UserGoal userGoal) throws IOException {
        UserQuest userQuest = (UserQuest) GsonManager.getJsonClass(file, UserQuest.class);
        if (userQuest != null) {
            if (Util.isQuestCompleted(userQuest)) {
                removeItems(player, userGoal);
            }
        }
    }

    @Override
    public Enum getGoalType() {
        return EnumGoalType.COLLECT;
    }
}