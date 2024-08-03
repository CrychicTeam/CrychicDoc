package snownee.kiwi.shadowed.org.yaml.snakeyaml.events;

import snownee.kiwi.shadowed.org.yaml.snakeyaml.error.Mark;

public final class MappingEndEvent extends CollectionEndEvent {

    public MappingEndEvent(Mark startMark, Mark endMark) {
        super(startMark, endMark);
    }

    @Override
    public Event.ID getEventId() {
        return Event.ID.MappingEnd;
    }
}