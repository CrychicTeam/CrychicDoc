package net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumquest;

import net.zanckor.questapi.api.enuminterface.enumquest.IEnumQuestRequirement;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractQuestRequirement;
import net.zanckor.questapi.example.common.handler.questrequirement.NoneRequirement;
import net.zanckor.questapi.example.common.handler.questrequirement.XpRequirement;

public enum EnumQuestRequirement implements IEnumQuestRequirement {

    XP(new XpRequirement()), NONE(new NoneRequirement());

    final AbstractQuestRequirement questRequirement;

    private EnumQuestRequirement(AbstractQuestRequirement abstractQuestRequirement) {
        this.questRequirement = abstractQuestRequirement;
        this.registerEnumQuestReq(this.getClass());
    }

    @Override
    public AbstractQuestRequirement getRequirement() {
        return this.questRequirement;
    }
}