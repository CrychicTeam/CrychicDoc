package de.keksuccino.konkrete.json.jsonpath.internal.function.latebinding;

import de.keksuccino.konkrete.json.jsonpath.internal.function.Parameter;
import de.keksuccino.konkrete.json.jsonpath.spi.json.JsonProvider;

public class JsonLateBindingValue implements ILateBindingValue {

    private final JsonProvider jsonProvider;

    private final Parameter jsonParameter;

    public JsonLateBindingValue(JsonProvider jsonProvider, Parameter jsonParameter) {
        this.jsonProvider = jsonProvider;
        this.jsonParameter = jsonParameter;
    }

    @Override
    public Object get() {
        return this.jsonProvider.parse(this.jsonParameter.getJson());
    }
}