package net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumdialog;

import net.zanckor.questapi.api.enuminterface.enumdialog.IEnumDialogOption;
import net.zanckor.questapi.api.file.dialog.abstractdialog.AbstractDialogOption;
import net.zanckor.questapi.example.common.handler.dialogoption.DialogAddQuest;
import net.zanckor.questapi.example.common.handler.dialogoption.DialogCloseDialog;
import net.zanckor.questapi.example.common.handler.dialogoption.DialogOpenDialog;

public enum EnumDialogOption implements IEnumDialogOption {

    OPEN_DIALOG(new DialogOpenDialog()), CLOSE_DIALOG(new DialogCloseDialog()), ADD_QUEST(new DialogAddQuest());

    final AbstractDialogOption dialogOption;

    private EnumDialogOption(AbstractDialogOption abstractDialogOption) {
        this.dialogOption = abstractDialogOption;
        this.registerEnumDialogOption(this.getClass());
    }

    @Override
    public AbstractDialogOption getDialogOption() {
        return this.dialogOption;
    }
}