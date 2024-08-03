package net.zanckor.questapi.example.common.handler.targettype;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractTargetType;
import net.zanckor.questapi.api.file.quest.codec.user.UserGoal;
import net.zanckor.questapi.mod.common.util.MCUtilClient;

public class XPTargetType extends AbstractTargetType {

    @Override
    public MutableComponent handler(String resourceLocationString, UserGoal goal, Player player, ChatFormatting chatFormatting, ChatFormatting chatFormatting1) {
        if (goal.getCurrentAmount() == null) {
            goal.setCurrentAmount(0);
        }
        return MCUtilClient.formatString("XP", " " + goal.getCurrentAmount() + "/" + goal.getAmount(), chatFormatting, chatFormatting1);
    }

    @Override
    public String target(String resourceLocationString) {
        return "XP";
    }

    @Override
    public void renderTarget(PoseStack poseStack, int xPosition, int yPosition, double size, double rotation, UserGoal goal, String resourceLocationString) {
        rotation = goal.getCurrentAmount() >= goal.getAmount() ? rotation : 0.0;
        MCUtilClient.renderItem(Items.EXPERIENCE_BOTTLE.getDefaultInstance(), xPosition + 10, yPosition, size, rotation, poseStack);
    }
}