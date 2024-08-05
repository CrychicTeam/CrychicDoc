package snownee.kiwi.shadowed.org.yaml.snakeyaml.parser;

import snownee.kiwi.shadowed.org.yaml.snakeyaml.events.Event;

public interface Parser {

    boolean checkEvent(Event.ID var1);

    Event peekEvent();

    Event getEvent();
}