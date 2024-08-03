package snownee.kiwi.shadowed.org.yaml.snakeyaml.emitter;

import java.io.IOException;
import snownee.kiwi.shadowed.org.yaml.snakeyaml.events.Event;

public interface Emitable {

    void emit(Event var1) throws IOException;
}