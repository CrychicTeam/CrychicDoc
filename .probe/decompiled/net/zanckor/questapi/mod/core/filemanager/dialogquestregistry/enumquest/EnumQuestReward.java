package net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumquest;

import net.zanckor.questapi.api.enuminterface.enumquest.IEnumQuestReward;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractReward;
import net.zanckor.questapi.example.common.handler.questreward.CommandReward;
import net.zanckor.questapi.example.common.handler.questreward.ItemReward;
import net.zanckor.questapi.example.common.handler.questreward.LootTableReward;
import net.zanckor.questapi.example.common.handler.questreward.QuestReward;
import net.zanckor.questapi.example.common.handler.questreward.XpReward;

public enum EnumQuestReward implements IEnumQuestReward {

    ITEM(new ItemReward()),
    COMMAND(new CommandReward()),
    QUEST(new QuestReward()),
    XP(new XpReward()),
    LEVEL(new XpReward()),
    POINTS(new XpReward()),
    LOOT_TABLE(new LootTableReward());

    final AbstractReward reward;

    private EnumQuestReward(AbstractReward reward) {
        this.reward = reward;
        this.registerEnumReward(this.getClass());
    }

    @Override
    public AbstractReward getReward() {
        return this.reward;
    }
}