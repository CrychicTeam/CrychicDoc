package de.keksuccino.konkrete.json.jsonpath.spi.cache;

import de.keksuccino.konkrete.json.jsonpath.JsonPath;

public class NOOPCache implements Cache {

    @Override
    public JsonPath get(String key) {
        return null;
    }

    @Override
    public void put(String key, JsonPath value) {
    }
}