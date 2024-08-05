package snownee.kiwi.shadowed.org.yaml.snakeyaml.parser;

import snownee.kiwi.shadowed.org.yaml.snakeyaml.events.Event;

interface Production {

    Event produce();
}