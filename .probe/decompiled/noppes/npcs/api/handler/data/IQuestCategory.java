package noppes.npcs.api.handler.data;

import java.util.List;

public interface IQuestCategory {

    List<IQuest> quests();

    String getName();

    IQuest create();
}