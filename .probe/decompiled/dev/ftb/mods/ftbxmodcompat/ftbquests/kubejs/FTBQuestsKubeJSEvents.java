package dev.ftb.mods.ftbxmodcompat.ftbquests.kubejs;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.Extra;

public interface FTBQuestsKubeJSEvents {

    EventGroup EVENT_GROUP = EventGroup.of("FTBQuestsEvents");

    EventHandler CUSTOM_TASK = EVENT_GROUP.server("customTask", () -> CustomTaskEventJS.class).extra(Extra.STRING).hasResult();

    EventHandler CUSTOM_REWARD = EVENT_GROUP.server("customReward", () -> CustomRewardEventJS.class).extra(Extra.STRING).hasResult();

    EventHandler OBJECT_COMPLETED = EVENT_GROUP.server("completed", () -> QuestObjectCompletedEventJS.class).extra(Extra.STRING);

    EventHandler OBJECT_STARTED = EVENT_GROUP.server("started", () -> QuestObjectStartedEventJS.class).extra(Extra.STRING);

    EventHandler CUSTOM_FILTER_ITEM = EVENT_GROUP.client("customFilterItem", () -> CustomFilterItemEventJS.class);
}