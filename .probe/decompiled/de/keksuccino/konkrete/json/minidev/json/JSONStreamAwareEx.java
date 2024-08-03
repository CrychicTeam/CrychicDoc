package de.keksuccino.konkrete.json.minidev.json;

import java.io.IOException;

public interface JSONStreamAwareEx extends JSONStreamAware {

    void writeJSONString(Appendable var1, JSONStyle var2) throws IOException;
}