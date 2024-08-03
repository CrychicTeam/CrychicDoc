package net.zanckor.questapi.api.enuminterface.enumquest;

import net.zanckor.questapi.api.file.quest.abstracquest.AbstractTargetType;
import net.zanckor.questapi.api.registry.EnumRegistry;

public interface IEnumTargetType {

    AbstractTargetType getTargetType();

    default void registerTargetType(Class<?> enumClass) {
        EnumRegistry.registerTargetType(enumClass);
    }
}