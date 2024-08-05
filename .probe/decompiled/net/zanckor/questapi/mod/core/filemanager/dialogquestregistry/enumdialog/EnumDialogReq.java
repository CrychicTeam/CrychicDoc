package net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumdialog;

import net.zanckor.questapi.api.enuminterface.enumdialog.IEnumDialogReq;
import net.zanckor.questapi.api.file.dialog.abstractdialog.AbstractDialogRequirement;
import net.zanckor.questapi.example.common.handler.dialogrequirement.DialogRequirement;
import net.zanckor.questapi.example.common.handler.dialogrequirement.NoneRequirement;
import net.zanckor.questapi.example.common.handler.dialogrequirement.QuestRequirement;

public enum EnumDialogReq implements IEnumDialogReq {

    QUEST(new QuestRequirement()), DIALOG(new DialogRequirement()), NONE(new NoneRequirement());

    final AbstractDialogRequirement dialogRequirement;

    private EnumDialogReq(AbstractDialogRequirement abstractDialogRequirement) {
        this.dialogRequirement = abstractDialogRequirement;
        this.registerEnumDialogReq(this.getClass());
    }

    @Override
    public AbstractDialogRequirement getDialogRequirement() {
        return this.dialogRequirement;
    }
}