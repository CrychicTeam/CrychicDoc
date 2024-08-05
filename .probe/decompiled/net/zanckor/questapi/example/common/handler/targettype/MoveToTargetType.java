package net.zanckor.questapi.example.common.handler.targettype;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractTargetType;
import net.zanckor.questapi.api.file.quest.codec.user.UserGoal;
import net.zanckor.questapi.mod.common.util.MCUtilClient;

public class MoveToTargetType extends AbstractTargetType {

    @Override
    public MutableComponent handler(String resourceLocationString, UserGoal goal, Player player, ChatFormatting chatFormatting, ChatFormatting chatFormatting1) {
        return MCUtilClient.formatString("Location", " " + goal.getAdditionalListData().toString().substring(1, goal.getAdditionalListData().toString().length() - 1) + " / " + (int) player.m_20185_() + ", " + (int) player.m_20186_() + ", " + (int) player.m_20189_(), chatFormatting, chatFormatting1);
    }

    @Override
    public String target(String resourceLocationString) {
        return "Location";
    }

    @Override
    public void renderTarget(PoseStack poseStack, int xPosition, int yPosition, double size, double rotation, UserGoal goal, String resourceLocationString) {
    }
}