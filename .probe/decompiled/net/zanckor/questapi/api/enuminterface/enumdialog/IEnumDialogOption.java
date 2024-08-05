package net.zanckor.questapi.api.enuminterface.enumdialog;

import net.zanckor.questapi.api.file.dialog.abstractdialog.AbstractDialogOption;
import net.zanckor.questapi.api.registry.EnumRegistry;

public interface IEnumDialogOption {

    AbstractDialogOption getDialogOption();

    default void registerEnumDialogOption(Class<?> enumClass) {
        EnumRegistry.registerDialogOption(enumClass);
    }
}