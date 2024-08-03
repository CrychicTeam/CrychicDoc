package de.keksuccino.konkrete.json.jsonpath;

public interface ReadContext {

    Configuration configuration();

    <T> T json();

    String jsonString();

    <T> T read(String var1, Predicate... var2);

    <T> T read(String var1, Class<T> var2, Predicate... var3);

    <T> T read(JsonPath var1);

    <T> T read(JsonPath var1, Class<T> var2);

    <T> T read(JsonPath var1, TypeRef<T> var2);

    <T> T read(String var1, TypeRef<T> var2);

    ReadContext limit(int var1);

    ReadContext withListeners(EvaluationListener... var1);
}