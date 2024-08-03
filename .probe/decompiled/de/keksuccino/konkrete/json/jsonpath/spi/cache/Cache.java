package de.keksuccino.konkrete.json.jsonpath.spi.cache;

import de.keksuccino.konkrete.json.jsonpath.JsonPath;

public interface Cache {

    JsonPath get(String var1);

    void put(String var1, JsonPath var2);
}