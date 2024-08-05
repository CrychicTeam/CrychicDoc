package vazkii.patchouli.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.jetbrains.annotations.Nullable;

public interface IVariable {

    <T> T as(Class<T> var1);

    default <T> T as(Class<T> clazz, T def) {
        return this.unwrap().isJsonNull() ? def : this.as(clazz);
    }

    JsonElement unwrap();

    default String asString() {
        return this.asString("");
    }

    default String asString(String def) {
        return this.unwrap().isJsonNull() ? def : this.unwrap().getAsString();
    }

    default Number asNumber() {
        return this.asNumber(0);
    }

    default Number asNumber(Number def) {
        return this.unwrap().isJsonNull() ? def : this.unwrap().getAsNumber();
    }

    default boolean asBoolean() {
        return this.asBoolean(false);
    }

    default boolean asBoolean(boolean def) {
        return this.unwrap().isJsonNull() ? def : !this.unwrap().getAsString().equals("false") && !this.unwrap().getAsString().isEmpty() && this.unwrap().getAsBoolean();
    }

    default Stream<IVariable> asStream() {
        return StreamSupport.stream(this.unwrap().getAsJsonArray().spliterator(), false).map(IVariable::wrap);
    }

    default Stream<IVariable> asStreamOrSingleton() {
        return this.unwrap().isJsonArray() ? this.asStream() : Stream.of(this);
    }

    default List<IVariable> asList() {
        return (List<IVariable>) this.asStream().collect(Collectors.toList());
    }

    default List<IVariable> asListOrSingleton() {
        return (List<IVariable>) this.asStreamOrSingleton().collect(Collectors.toList());
    }

    static <T> IVariable from(@Nullable T object) {
        return object != null ? VariableHelper.instance().createFromObject(object) : empty();
    }

    static IVariable wrap(@Nullable JsonElement elem) {
        return elem != null ? VariableHelper.instance().createFromJson(elem) : empty();
    }

    static IVariable wrapList(Iterable<IVariable> elems) {
        JsonArray arr = new JsonArray();
        for (IVariable v : elems) {
            arr.add(v.unwrap());
        }
        return wrap(arr);
    }

    static IVariable wrap(@Nullable Number n) {
        return n != null ? wrap(new JsonPrimitive(n)) : empty();
    }

    static IVariable wrap(@Nullable Boolean b) {
        return b != null ? wrap(new JsonPrimitive(b)) : empty();
    }

    static IVariable wrap(@Nullable String s) {
        return s != null ? wrap(new JsonPrimitive(s)) : empty();
    }

    static IVariable empty() {
        return wrap(JsonNull.INSTANCE);
    }

    public static class Serializer implements JsonDeserializer<IVariable> {

        public IVariable deserialize(JsonElement elem, Type t, JsonDeserializationContext c) {
            return IVariable.wrap(elem);
        }
    }
}