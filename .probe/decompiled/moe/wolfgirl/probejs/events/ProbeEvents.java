package moe.wolfgirl.probejs.events;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface ProbeEvents {

    EventGroup GROUP = EventGroup.of("ProbeEvents");

    EventHandler ASSIGN_TYPE = GROUP.client("assignType", () -> TypeAssignmentEventJS.class);

    EventHandler MODIFY_DOC = GROUP.client("modifyClass", () -> TypingModificationEventJS.class);

    EventHandler SNIPPETS = GROUP.client("snippets", () -> SnippetGenerationEventJS.class);
}