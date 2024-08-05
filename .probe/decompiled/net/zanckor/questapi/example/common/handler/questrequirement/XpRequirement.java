package net.zanckor.questapi.example.common.handler.questrequirement;

import java.io.IOException;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractQuestRequirement;
import net.zanckor.questapi.api.file.quest.codec.server.ServerQuest;
import net.zanckor.questapi.api.file.quest.codec.server.ServerRequirement;
import net.zanckor.questapi.api.registry.EnumRegistry;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumquest.EnumQuestRequirement;

public class XpRequirement extends AbstractQuestRequirement {

    @Override
    public boolean handler(Player player, ServerQuest serverQuest, int requirementIndex) throws IOException {
        ServerRequirement serverRequirement = (ServerRequirement) serverQuest.getRequirements().get(requirementIndex);
        String requirementType = serverRequirement.getType() != null ? serverRequirement.getType() : "NONE";
        Enum<?> questRequirementEnum = EnumRegistry.getEnum(requirementType, EnumRegistry.getDialogRequirement());
        if (questRequirementEnum.equals(EnumQuestRequirement.XP)) {
            ServerRequirement requirement = (ServerRequirement) serverQuest.getRequirements().get(requirementIndex);
            if (player.experienceLevel < 0) {
                player.experienceLevel = 0;
            }
            boolean hasReqs = player.experienceLevel >= requirement.getRequirements_min() && player.experienceLevel <= requirement.getRequirements_max();
            if (!hasReqs) {
                player.m_213846_(Component.literal("Player " + player.getScoreboardName() + " doesn't have the requirements to access to this quest"));
                player.m_213846_(Component.literal("Minimum: " + requirement.getRequirements_min()));
                player.m_213846_(Component.literal("Maximum: " + requirement.getRequirements_max()));
            }
            return hasReqs;
        } else {
            return false;
        }
    }
}