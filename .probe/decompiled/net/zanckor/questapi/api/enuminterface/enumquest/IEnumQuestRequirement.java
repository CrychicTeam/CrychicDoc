package net.zanckor.questapi.api.enuminterface.enumquest;

import net.zanckor.questapi.api.file.quest.abstracquest.AbstractQuestRequirement;
import net.zanckor.questapi.api.registry.EnumRegistry;

public interface IEnumQuestRequirement {

    AbstractQuestRequirement getRequirement();

    default void registerEnumQuestReq(Class enumClass) {
        EnumRegistry.registerQuestRequirement(enumClass);
    }
}