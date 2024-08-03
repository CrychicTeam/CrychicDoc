package net.zanckor.questapi.example.common.handler.targettype;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractTargetType;
import net.zanckor.questapi.api.file.quest.codec.user.UserGoal;
import net.zanckor.questapi.mod.common.util.MCUtilClient;

public class EntityUUIDTargetType extends AbstractTargetType {

    @Override
    public MutableComponent handler(String resourceLocationString, UserGoal goal, Player player, ChatFormatting chatFormatting, ChatFormatting chatFormatting1) {
        LivingEntity entity = (LivingEntity) MCUtilClient.getEntityByUUID(UUID.fromString(new ResourceLocation(resourceLocationString).getPath()));
        String translationKey = entity != null ? entity.m_6095_().getDescriptionId() : "";
        return MCUtilClient.formatString(MCUtilClient.properNoun(I18n.get(translationKey)), " " + goal.getCurrentAmount() + "/" + goal.getAmount(), chatFormatting, chatFormatting1);
    }

    @Override
    public String target(String resourceLocationString) {
        LivingEntity entity = (LivingEntity) MCUtilClient.getEntityByUUID(UUID.fromString(new ResourceLocation(resourceLocationString).getPath()));
        String translationKey = entity != null ? entity.m_6095_().getDescriptionId() : "";
        return MCUtilClient.properNoun(I18n.get(translationKey));
    }

    @Override
    public void renderTarget(PoseStack poseStack, int xPosition, int yPosition, double size, double rotation, UserGoal goal, String resourceLocationString) {
        EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(resourceLocationString));
        rotation = goal.getCurrentAmount() >= goal.getAmount() ? rotation : 0.0;
        MCUtilClient.renderEntity((double) (xPosition + 10), (double) (yPosition + 4), 5.0, rotation, (LivingEntity) entityType.create(Minecraft.getInstance().level), poseStack);
    }
}