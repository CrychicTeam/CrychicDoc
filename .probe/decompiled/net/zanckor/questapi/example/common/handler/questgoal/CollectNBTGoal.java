package net.zanckor.questapi.example.common.handler.questgoal;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.zanckor.questapi.api.file.quest.codec.user.UserGoal;
import net.zanckor.questapi.api.file.quest.codec.user.UserQuest;
import net.zanckor.questapi.mod.common.questhandler.ForgeAbstractGoal;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumquest.EnumGoalType;
import net.zanckor.questapi.util.GsonManager;
import net.zanckor.questapi.util.Util;

public class CollectNBTGoal extends ForgeAbstractGoal {

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
            for (UserGoal goal : userQuest.getQuestGoals()) {
                if (goal.getType().contains(EnumGoalType.COLLECT_WITH_NBT.name())) {
                    String valueItem = goal.getTarget();
                    ItemStack itemTarget = ForgeRegistries.ITEMS.getValue(new ResourceLocation(valueItem)).getDefaultInstance();
                    if (!player.m_150109_().contains(itemTarget)) {
                        goal.setCurrentAmount(0);
                    } else {
                        List<Integer> itemSlotList = Util.findSlotMatchingItemStack(itemTarget, player.m_150109_());
                        if (!itemSlotList.isEmpty() && checkItemsNBT(goal, player, itemSlotList)) {
                            ItemStack item = player.m_150109_().getItem((Integer) itemSlotList.get(0));
                            int itemCount = item.getCount() > goal.getAmount() ? goal.getAmount() : item.getCount();
                            goal.setCurrentAmount(itemCount);
                        }
                    }
                    GsonManager.writeJson(file, userQuest);
                }
            }
        }
    }

    public static void removeItems(ServerPlayer player, UserGoal goalEnhanced) {
        if (goalEnhanced.getType().equals(EnumGoalType.COLLECT_WITH_NBT.name())) {
            Inventory inventory = player.m_150109_();
            String valueItem = goalEnhanced.getTarget();
            ItemStack itemTarget = ForgeRegistries.ITEMS.getValue(new ResourceLocation(valueItem)).getDefaultInstance();
            for (int itemSlot : Util.findSlotMatchingItemStack(itemTarget, inventory)) {
                if (checkItemsNBT(goalEnhanced, player, itemSlot)) {
                    inventory.removeItem(itemSlot, goalEnhanced.getAmount());
                }
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

    public static boolean checkItemsNBT(UserGoal goal, ServerPlayer player, List<Integer> itemSlotList) {
        Map<String, String> nbtTag = (Map<String, String>) GsonManager.gson.fromJson(goal.getAdditionalClassData().toString(), (new TypeToken<Map<String, String>>() {
        }).getType());
        for (Entry<String, String> entry : nbtTag.entrySet()) {
            for (Integer integer : itemSlotList) {
                ItemStack itemStack = player.m_150109_().getItem(integer);
                if (itemStack.getTag() != null && itemStack.getTag().get((String) entry.getKey()) != null && itemStack.getTag().get((String) entry.getKey()).getAsString().contains((CharSequence) entry.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkItemsNBT(UserGoal goal, ServerPlayer player, int itemSlot) {
        Map<String, String> nbtTag = (Map<String, String>) GsonManager.gson.fromJson(goal.getAdditionalClassData().toString(), (new TypeToken<Map<String, String>>() {
        }).getType());
        for (Entry<String, String> entry : nbtTag.entrySet()) {
            ItemStack itemStack = player.m_150109_().getItem(itemSlot);
            if (itemStack.getTag() != null && itemStack.getTag().get((String) entry.getKey()) != null && itemStack.getTag().get((String) entry.getKey()).getAsString().contains((CharSequence) entry.getValue())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Enum getGoalType() {
        return EnumGoalType.COLLECT_WITH_NBT;
    }
}