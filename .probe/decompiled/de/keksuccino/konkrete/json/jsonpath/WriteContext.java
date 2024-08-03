package de.keksuccino.konkrete.json.jsonpath;

public interface WriteContext {

    Configuration configuration();

    <T> T json();

    String jsonString();

    DocumentContext set(String var1, Object var2, Predicate... var3);

    DocumentContext set(JsonPath var1, Object var2);

    DocumentContext map(String var1, MapFunction var2, Predicate... var3);

    DocumentContext map(JsonPath var1, MapFunction var2);

    DocumentContext delete(String var1, Predicate... var2);

    DocumentContext delete(JsonPath var1);

    DocumentContext add(String var1, Object var2, Predicate... var3);

    DocumentContext add(JsonPath var1, Object var2);

    DocumentContext put(String var1, String var2, Object var3, Predicate... var4);

    DocumentContext put(JsonPath var1, String var2, Object var3);

    DocumentContext renameKey(String var1, String var2, String var3, Predicate... var4);

    DocumentContext renameKey(JsonPath var1, String var2, String var3);
}