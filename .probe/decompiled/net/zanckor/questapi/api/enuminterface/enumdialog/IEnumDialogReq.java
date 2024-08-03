package net.zanckor.questapi.api.enuminterface.enumdialog;

import net.zanckor.questapi.api.file.dialog.abstractdialog.AbstractDialogRequirement;
import net.zanckor.questapi.api.registry.EnumRegistry;

public interface IEnumDialogReq {

    AbstractDialogRequirement getDialogRequirement();

    default void registerEnumDialogReq(Class<?> enumClass) {
        EnumRegistry.registerDialogRequirement(enumClass);
    }
}