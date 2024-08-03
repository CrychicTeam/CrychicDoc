package snownee.kiwi.shadowed.org.yaml.snakeyaml.events;

import snownee.kiwi.shadowed.org.yaml.snakeyaml.error.Mark;

public abstract class CollectionEndEvent extends Event {

    public CollectionEndEvent(Mark startMark, Mark endMark) {
        super(startMark, endMark);
    }
}