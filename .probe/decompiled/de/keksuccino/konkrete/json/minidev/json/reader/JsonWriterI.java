package de.keksuccino.konkrete.json.minidev.json.reader;

import de.keksuccino.konkrete.json.minidev.json.JSONStyle;
import java.io.IOException;

public interface JsonWriterI<T> {

    <E extends T> void writeJSONString(E var1, Appendable var2, JSONStyle var3) throws IOException;
}