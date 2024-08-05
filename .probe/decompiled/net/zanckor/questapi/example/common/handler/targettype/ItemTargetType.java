package net.zanckor.questapi.example.common.handler.targettype;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractTargetType;
import net.zanckor.questapi.api.file.quest.codec.user.UserGoal;
import net.zanckor.questapi.mod.common.util.MCUtilClient;

public class ItemTargetType extends AbstractTargetType {

    @Override
    public MutableComponent handler(String resourceLocationString, UserGoal goal, Player player, ChatFormatting chatFormatting, ChatFormatting chatFormatting1) {
        Item itemTarget = ForgeRegistries.ITEMS.getValue(new ResourceLocation(resourceLocationString));
        String translationKey = itemTarget.getDescriptionId(itemTarget.getDefaultInstance());
        return MCUtilClient.formatString(MCUtilClient.properNoun(I18n.get(translationKey)), " " + goal.getCurrentAmount() + "/" + goal.getAmount(), chatFormatting, chatFormatting1);
    }

    @Override
    public String target(String resourceLocationString) {
        Item itemTarget = ForgeRegistries.ITEMS.getValue(new ResourceLocation(resourceLocationString));
        String translationKey = itemTarget.getDescriptionId(itemTarget.getDefaultInstance());
        return MCUtilClient.properNoun(I18n.get(translationKey));
    }

    @Override
    public void renderTarget(PoseStack poseStack, int xPosition, int yPosition, double size, double rotation, UserGoal goal, String resourceLocationString) {
        ItemStack itemStack = ForgeRegistries.ITEMS.getValue(new ResourceLocation(resourceLocationString)).getDefaultInstance();
        rotation = goal.getCurrentAmount() >= goal.getAmount() ? rotation : 0.0;
        poseStack.pushPose();
        MCUtilClient.renderItem(itemStack, xPosition, yPosition, size, rotation, poseStack);
        poseStack.popPose();
    }
}