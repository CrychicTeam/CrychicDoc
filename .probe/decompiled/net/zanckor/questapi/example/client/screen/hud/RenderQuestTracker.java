package net.zanckor.questapi.example.client.screen.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractTargetType;
import net.zanckor.questapi.api.file.quest.codec.user.UserGoal;
import net.zanckor.questapi.api.file.quest.codec.user.UserQuest;
import net.zanckor.questapi.api.file.quest.register.QuestTemplateRegistry;
import net.zanckor.questapi.api.registry.EnumRegistry;
import net.zanckor.questapi.api.screen.AbstractQuestTracked;
import net.zanckor.questapi.mod.common.network.handler.ClientHandler;
import net.zanckor.questapi.mod.common.util.MCUtilClient;
import net.zanckor.questapi.util.Util;

public class RenderQuestTracker extends AbstractQuestTracked {

    static float xPosition;

    static float yPosition;

    static float scale;

    static float sin;

    @Override
    public void renderQuestTracked(GuiGraphics graphics, int width, int height) {
        Minecraft minecraft = Minecraft.getInstance();
        HashMap<String, List<UserGoal>> userQuestHashMap = new HashMap();
        PoseStack poseStack = graphics.pose();
        if (!ClientHandler.trackedQuestList.isEmpty() && ClientHandler.trackedQuestList != null && !minecraft.player.m_36330_() && !minecraft.options.keyPlayerList.isDown() && !minecraft.options.renderDebug) {
            xPosition = (float) width / 100.0F;
            yPosition = (float) width / 100.0F;
            scale = (float) width / 700.0F;
            minecraft.getProfiler().push("hud_tracked");
            poseStack.pushPose();
            poseStack.scale(scale, scale, 1.0F);
            renderQuests(graphics, poseStack, minecraft, userQuestHashMap);
            poseStack.popPose();
            minecraft.getProfiler().pop();
        }
    }

    public static void renderQuests(GuiGraphics graphics, PoseStack poseStack, Minecraft minecraft, HashMap<String, List<UserGoal>> userQuestHashMap) {
        ClientHandler.trackedQuestList.forEach(userQuest -> {
            userQuestHashMap.clear();
            if (!Util.isQuestCompleted(userQuest)) {
                for (UserGoal questGoal : userQuest.getQuestGoals()) {
                    String type = questGoal.getType();
                    List<UserGoal> questGoalList = (List<UserGoal>) userQuestHashMap.get(type);
                    if (questGoalList == null) {
                        questGoalList = new ArrayList();
                    }
                    questGoalList.add(questGoal);
                    userQuestHashMap.put(type, questGoalList);
                }
                renderTitle(graphics, poseStack, minecraft, userQuest);
                renderQuestType(graphics, poseStack, minecraft, userQuestHashMap);
                if (userQuest.hasTimeLimit()) {
                    MCUtilClient.renderLine(graphics, poseStack, (float) ((int) xPosition), (float) ((int) yPosition), 10.0F, I18n.get("tracker.questapi.time_limit") + userQuest.getTimeLimitInSeconds(), minecraft.font);
                }
            }
        });
    }

    public static void renderTitle(GuiGraphics graphics, PoseStack poseStack, Minecraft minecraft, UserQuest userQuest) {
        String title = I18n.get(userQuest.getTitle());
        MCUtilClient.renderLine(graphics, poseStack, (float) ((int) xPosition), (float) ((int) yPosition), 20.0F, Component.literal(I18n.get("tracker.questapi.quest") + title).withStyle(ChatFormatting.WHITE), minecraft.font);
    }

    public static void renderQuestType(GuiGraphics graphics, PoseStack poseStack, Minecraft minecraft, HashMap<String, List<UserGoal>> userQuestHashMap) {
        Font font = minecraft.font;
        Player player = minecraft.player;
        sin = (float) ((double) sin + 0.25);
        for (Entry<String, List<UserGoal>> entry : userQuestHashMap.entrySet()) {
            List<UserGoal> questGoalList = (List<UserGoal>) entry.getValue();
            MCUtilClient.renderLine(graphics, poseStack, Integer.MAX_VALUE, xPosition, yPosition, 10.0F, Component.literal(I18n.get("tracker.questapi.quest_type") + I18n.get("quest_type." + ((UserGoal) questGoalList.get(0)).getTranslatableType().toLowerCase())).withStyle(ChatFormatting.WHITE), font);
            for (UserGoal questGoal : questGoalList) {
                Enum<?> goalEnum = EnumRegistry.getEnum("TARGET_TYPE_" + questGoal.getType(), EnumRegistry.getTargetType());
                AbstractTargetType translatableTargetType = QuestTemplateRegistry.getTranslatableTargetType(goalEnum);
                MutableComponent goalComponentTarget = translatableTargetType.handler(questGoal.getTarget(), questGoal, player, ChatFormatting.GRAY, ChatFormatting.BLACK);
                translatableTargetType.renderTarget(poseStack, (int) ((float) (goalComponentTarget.getString().length() * 6) + xPosition - 4.0F), (int) yPosition + 3, 0.7, Math.sin((double) sin), questGoal, questGoal.getTarget());
                MCUtilClient.renderLine(graphics, poseStack, 30, xPosition, yPosition, 10.0F, goalComponentTarget.withStyle(ChatFormatting.ITALIC), font);
            }
            poseStack.translate(0.0F, 10.0F, 0.0F);
        }
    }
}