package net.zanckor.questapi.api.enuminterface.enumquest;

import net.zanckor.questapi.api.file.quest.abstracquest.AbstractGoal;
import net.zanckor.questapi.api.registry.EnumRegistry;

public interface IEnumQuestGoal {

    AbstractGoal getQuest();

    default void registerEnumGoal(Class<?> enumClass) {
        EnumRegistry.registerQuestGoal(enumClass);
    }
}