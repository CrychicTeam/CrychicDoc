package net.zanckor.questapi.api.enuminterface.enumquest;

import net.zanckor.questapi.api.file.quest.abstracquest.AbstractReward;
import net.zanckor.questapi.api.registry.EnumRegistry;

public interface IEnumQuestReward {

    AbstractReward getReward();

    default void registerEnumReward(Class<?> enumClass) {
        EnumRegistry.registerQuestReward(enumClass);
    }
}