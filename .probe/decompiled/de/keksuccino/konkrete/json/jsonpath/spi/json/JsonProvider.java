package de.keksuccino.konkrete.json.jsonpath.spi.json;

import de.keksuccino.konkrete.json.jsonpath.InvalidJsonException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public interface JsonProvider {

    Object UNDEFINED = new Object();

    Object parse(String var1) throws InvalidJsonException;

    default Object parse(byte[] json) throws InvalidJsonException {
        return this.parse(new String(json, StandardCharsets.UTF_8));
    }

    Object parse(InputStream var1, String var2) throws InvalidJsonException;

    String toJson(Object var1);

    Object createArray();

    Object createMap();

    boolean isArray(Object var1);

    int length(Object var1);

    Iterable<?> toIterable(Object var1);

    Collection<String> getPropertyKeys(Object var1);

    Object getArrayIndex(Object var1, int var2);

    @Deprecated
    Object getArrayIndex(Object var1, int var2, boolean var3);

    void setArrayIndex(Object var1, int var2, Object var3);

    Object getMapValue(Object var1, String var2);

    void setProperty(Object var1, Object var2, Object var3);

    void removeProperty(Object var1, Object var2);

    boolean isMap(Object var1);

    Object unwrap(Object var1);
}