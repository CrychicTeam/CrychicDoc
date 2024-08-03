package net.zanckor.questapi.api.file.quest.register;

import java.util.HashMap;
import net.zanckor.questapi.api.enuminterface.enumdialog.IEnumDialogOption;
import net.zanckor.questapi.api.enuminterface.enumdialog.IEnumDialogReq;
import net.zanckor.questapi.api.enuminterface.enumquest.IEnumQuestGoal;
import net.zanckor.questapi.api.enuminterface.enumquest.IEnumQuestRequirement;
import net.zanckor.questapi.api.enuminterface.enumquest.IEnumQuestReward;
import net.zanckor.questapi.api.enuminterface.enumquest.IEnumTargetType;
import net.zanckor.questapi.api.file.dialog.abstractdialog.AbstractDialogOption;
import net.zanckor.questapi.api.file.dialog.abstractdialog.AbstractDialogRequirement;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractGoal;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractQuestRequirement;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractReward;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractTargetType;

public class QuestTemplateRegistry {

    private static final HashMap<Enum<?>, AbstractGoal> quest_goal = new HashMap();

    private static final HashMap<Enum<?>, AbstractReward> quest_reward = new HashMap();

    private static final HashMap<Enum<?>, AbstractQuestRequirement> quest_requirement = new HashMap();

    private static final HashMap<Enum<?>, AbstractDialogRequirement> dialog_requirement = new HashMap();

    private static final HashMap<Enum<?>, AbstractDialogOption> dialog_template = new HashMap();

    private static final HashMap<Enum<?>, AbstractTargetType> target_type = new HashMap();

    public static void registerQuest(IEnumQuestGoal key) {
        quest_goal.put((Enum) key, key.getQuest());
    }

    public static void registerDialogOption(IEnumDialogOption key) {
        dialog_template.put((Enum) key, key.getDialogOption());
    }

    public static void registerReward(IEnumQuestReward key) {
        quest_reward.put((Enum) key, key.getReward());
    }

    public static void registerQuestRequirement(IEnumQuestRequirement key) {
        quest_requirement.put((Enum) key, key.getRequirement());
    }

    public static void registerDialogRequirement(IEnumDialogReq key) {
        dialog_requirement.put((Enum) key, key.getDialogRequirement());
    }

    public static void registerTargetType(IEnumTargetType key) {
        target_type.put((Enum) key, key.getTargetType());
    }

    public static AbstractGoal getQuestTemplate(Enum<?> key) {
        try {
            return (AbstractGoal) quest_goal.get(key);
        } catch (NullPointerException var2) {
            throw new RuntimeException("Incorrect quest key: " + key);
        }
    }

    public static HashMap<Enum<?>, AbstractGoal> getAllGoals() {
        return quest_goal;
    }

    public static AbstractDialogOption getDialogTemplate(Enum<?> key) {
        try {
            return (AbstractDialogOption) dialog_template.get(key);
        } catch (NullPointerException var2) {
            throw new RuntimeException("Incorrect quest key: " + key);
        }
    }

    public static AbstractReward getQuestReward(Enum<?> key) {
        try {
            return (AbstractReward) quest_reward.get(key);
        } catch (NullPointerException var2) {
            throw new RuntimeException("Incorrect reward key: " + key);
        }
    }

    public static AbstractQuestRequirement getQuestRequirement(Enum<?> key) {
        try {
            return (AbstractQuestRequirement) quest_requirement.get(key);
        } catch (NullPointerException var2) {
            throw new RuntimeException("Incorrect requirement key: " + key);
        }
    }

    public static AbstractDialogRequirement getDialogRequirement(Enum<?> key) {
        try {
            return (AbstractDialogRequirement) dialog_requirement.get(key);
        } catch (NullPointerException var2) {
            throw new RuntimeException("Incorrect requirement key: " + key);
        }
    }

    public static AbstractTargetType getTranslatableTargetType(Enum<?> key) {
        return (AbstractTargetType) target_type.getOrDefault(key, null);
    }
}