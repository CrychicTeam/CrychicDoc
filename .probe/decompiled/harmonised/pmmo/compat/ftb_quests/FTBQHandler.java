package harmonised.pmmo.compat.ftb_quests;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftbquests.quest.reward.RewardType;
import dev.ftb.mods.ftbquests.quest.reward.RewardTypes;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import dev.ftb.mods.ftbquests.quest.task.TaskTypes;
import net.minecraft.resources.ResourceLocation;

public class FTBQHandler {

    public static TaskType SKILL = TaskTypes.register(new ResourceLocation("pmmo", "skill"), SkillTask::new, () -> Icon.getIcon("pmmo:textures/gui/star.png"));

    public static RewardType XP_REWARD = RewardTypes.register(new ResourceLocation("pmmo", "xpreward"), XpReward::new, () -> Icon.getIcon("pmmo:textures/gui/star.png"));

    public static RewardType LEVEL_REWARD = RewardTypes.register(new ResourceLocation("pmmo", "levelreward"), LevelReward::new, () -> Icon.getIcon("pmmo:textures/gui/star.png"));

    public static void init() {
    }
}