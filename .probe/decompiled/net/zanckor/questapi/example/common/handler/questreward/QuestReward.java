package net.zanckor.questapi.example.common.handler.questreward;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.minecraft.server.level.ServerPlayer;
import net.zanckor.questapi.CommonMain;
import net.zanckor.questapi.api.data.QuestDialogManager;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractQuestRequirement;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractReward;
import net.zanckor.questapi.api.file.quest.codec.server.ServerQuest;
import net.zanckor.questapi.api.file.quest.codec.server.ServerRequirement;
import net.zanckor.questapi.api.file.quest.codec.server.ServerReward;
import net.zanckor.questapi.api.file.quest.codec.user.UserQuest;
import net.zanckor.questapi.api.file.quest.register.QuestTemplateRegistry;
import net.zanckor.questapi.api.registry.EnumRegistry;
import net.zanckor.questapi.util.GsonManager;
import net.zanckor.questapi.util.Timer;

public class QuestReward extends AbstractReward {

    @Override
    public void handler(ServerPlayer player, ServerQuest serverQuest, int rewardIndex) throws IOException {
        String quest = ((ServerReward) serverQuest.getRewards().get(rewardIndex)).getTag() + ".json";
        Path userFolder = Paths.get(CommonMain.playerData.toString(), player.m_20148_().toString());
        for (File file : CommonMain.serverQuests.toFile().listFiles()) {
            if (file.getName().equals(quest)) {
                Path path = Paths.get(CommonMain.getActiveQuest(userFolder).toString(), File.separator, file.getName());
                ServerQuest rewardServerQuest = (ServerQuest) GsonManager.getJsonClass(file, ServerQuest.class);
                for (int requirementIndex = 0; requirementIndex < rewardServerQuest.getRequirements().size(); requirementIndex++) {
                    ServerRequirement serverRequirement = (ServerRequirement) serverQuest.getRequirements().get(requirementIndex);
                    String requirementType = serverRequirement.getType() != null ? serverRequirement.getType() : "NONE";
                    Enum<?> questRequirementEnum = EnumRegistry.getEnum(requirementType, EnumRegistry.getDialogRequirement());
                    AbstractQuestRequirement requirement = QuestTemplateRegistry.getQuestRequirement(questRequirementEnum);
                    if (!requirement.handler(player, rewardServerQuest, requirementIndex)) {
                        return;
                    }
                }
                UserQuest userQuest = UserQuest.createQuest(rewardServerQuest, path);
                GsonManager.writeJson(path.toFile(), userQuest);
                if (userQuest.hasTimeLimit()) {
                    Timer.updateCooldown(player.m_20148_(), userQuest.getId(), (float) userQuest.getTimeLimitInSeconds());
                }
                QuestDialogManager.registerQuestByID(((ServerReward) serverQuest.getRewards().get(rewardIndex)).getTag(), path);
                break;
            }
        }
    }
}