package me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.api.Marshaller;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.impl.MarshallerImpl;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.impl.serializer.CommentSerializer;

public class JsonArray extends JsonElement implements List<JsonElement>, Iterable<JsonElement> {

    private List<JsonArray.Entry> entries = new ArrayList();

    protected Marshaller marshaller = MarshallerImpl.getFallback();

    public JsonArray() {
    }

    public <T> JsonArray(T[] ts, Marshaller marshaller) {
        this.marshaller = marshaller;
        for (T t : ts) {
            this.add(marshaller.serialize(t));
        }
    }

    public JsonArray(Collection<?> ts, Marshaller marshaller) {
        this.marshaller = marshaller;
        for (Object t : ts) {
            this.add(marshaller.serialize(t));
        }
    }

    public JsonElement get(int i) {
        return ((JsonArray.Entry) this.entries.get(i)).value;
    }

    public String getString(int index, String defaultValue) {
        JsonElement elem = this.get(index);
        return elem != null && elem instanceof JsonPrimitive ? ((JsonPrimitive) elem).asString() : defaultValue;
    }

    public boolean getBoolean(int index, boolean defaultValue) {
        JsonElement elem = this.get(index);
        return elem != null && elem instanceof JsonPrimitive ? ((JsonPrimitive) elem).asBoolean(defaultValue) : defaultValue;
    }

    public byte getByte(int index, byte defaultValue) {
        JsonElement elem = this.get(index);
        return elem != null && elem instanceof JsonPrimitive ? ((JsonPrimitive) elem).asByte(defaultValue) : defaultValue;
    }

    public char getChar(int index, char defaultValue) {
        JsonElement elem = this.get(index);
        return elem != null && elem instanceof JsonPrimitive ? ((JsonPrimitive) elem).asChar(defaultValue) : defaultValue;
    }

    public short getShort(int index, short defaultValue) {
        JsonElement elem = this.get(index);
        return elem != null && elem instanceof JsonPrimitive ? ((JsonPrimitive) elem).asShort(defaultValue) : defaultValue;
    }

    public int getInt(int index, int defaultValue) {
        JsonElement elem = this.get(index);
        return elem != null && elem instanceof JsonPrimitive ? ((JsonPrimitive) elem).asInt(defaultValue) : defaultValue;
    }

    public long getLong(int index, long defaultValue) {
        JsonElement elem = this.get(index);
        return elem != null && elem instanceof JsonPrimitive ? ((JsonPrimitive) elem).asLong(defaultValue) : defaultValue;
    }

    public float getFloat(int index, float defaultValue) {
        JsonElement elem = this.get(index);
        return elem != null && elem instanceof JsonPrimitive ? ((JsonPrimitive) elem).asFloat(defaultValue) : defaultValue;
    }

    public double getDouble(int index, double defaultValue) {
        JsonElement elem = this.get(index);
        return elem != null && elem instanceof JsonPrimitive ? ((JsonPrimitive) elem).asDouble(defaultValue) : defaultValue;
    }

    public String getComment(int i) {
        return ((JsonArray.Entry) this.entries.get(i)).comment;
    }

    public void setComment(int i, String comment) {
        ((JsonArray.Entry) this.entries.get(i)).comment = comment;
    }

    @Override
    public String toJson(boolean comments, boolean newlines, int depth) {
        JsonGrammar grammar = JsonGrammar.builder().withComments(comments).printWhitespace(newlines).build();
        return this.toJson(grammar, depth);
    }

    @Override
    public String toJson(JsonGrammar grammar, int depth) {
        StringBuilder builder = new StringBuilder();
        int effectiveDepth = grammar.bareRootObject ? Math.max(depth - 1, 0) : depth;
        if (grammar.bareRootObject) {
            int var10000 = depth - 1;
        }
        builder.append("[");
        if (this.entries.size() > 0) {
            if (grammar.printWhitespace) {
                builder.append('\n');
            } else {
                builder.append(' ');
            }
        }
        for (int i = 0; i < this.entries.size(); i++) {
            JsonArray.Entry entry = (JsonArray.Entry) this.entries.get(i);
            if (grammar.printWhitespace) {
                for (int j = 0; j < effectiveDepth + 1; j++) {
                    builder.append("\t");
                }
            }
            CommentSerializer.print(builder, entry.comment, effectiveDepth, grammar);
            builder.append(entry.value.toJson(grammar, depth + 1));
            if (grammar.printCommas) {
                if (i < this.entries.size() - 1 || grammar.printTrailingCommas) {
                    builder.append(",");
                    if (i < this.entries.size() - 1 && !grammar.printWhitespace) {
                        builder.append(' ');
                    }
                }
            } else {
                builder.append(" ");
            }
            if (grammar.printWhitespace) {
                builder.append('\n');
            }
        }
        if (this.entries.size() > 0 && grammar.printWhitespace && depth > 0) {
            for (int j = 0; j < effectiveDepth; j++) {
                builder.append("\t");
            }
        }
        if (this.entries.size() > 0 && !grammar.printWhitespace) {
            builder.append(' ');
        }
        builder.append(']');
        return builder.toString();
    }

    public String toString() {
        return this.toJson(true, false, 0);
    }

    public boolean add(@Nonnull JsonElement e, String comment) {
        JsonArray.Entry entry = new JsonArray.Entry();
        entry.value = e;
        entry.comment = comment;
        this.entries.add(entry);
        return true;
    }

    public boolean equals(Object other) {
        if (other != null && other instanceof JsonArray) {
            List<JsonArray.Entry> a = this.entries;
            List<JsonArray.Entry> b = ((JsonArray) other).entries;
            if (a.size() != b.size()) {
                return false;
            } else {
                for (int i = 0; i < a.size(); i++) {
                    JsonArray.Entry ae = (JsonArray.Entry) a.get(i);
                    JsonArray.Entry be = (JsonArray.Entry) b.get(i);
                    if (!ae.value.equals(be.value)) {
                        return false;
                    }
                    if (!Objects.equals(ae.comment, be.comment)) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.entries.hashCode();
    }

    @Nullable
    public <E> E get(@Nonnull Class<E> clazz, int index) {
        JsonElement elem = this.get(index);
        return this.marshaller.marshall(clazz, elem);
    }

    public void setMarshaller(Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    public Marshaller getMarshaller() {
        return this.marshaller;
    }

    public JsonArray clone() {
        JsonArray result = new JsonArray();
        result.marshaller = this.marshaller;
        for (JsonArray.Entry entry : this.entries) {
            result.add(entry.value.clone(), entry.comment);
        }
        return result;
    }

    public int size() {
        return this.entries.size();
    }

    public boolean add(@Nonnull JsonElement e) {
        JsonArray.Entry entry = new JsonArray.Entry();
        entry.value = e;
        this.entries.add(entry);
        return true;
    }

    public boolean addAll(Collection<? extends JsonElement> c) {
        boolean result = false;
        for (JsonElement elem : c) {
            result |= this.add(elem);
        }
        return result;
    }

    public void clear() {
        this.entries.clear();
    }

    public boolean contains(Object o) {
        if (o != null && o instanceof JsonElement) {
            for (JsonArray.Entry entry : this.entries) {
                if (entry.value.equals(o)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!this.contains(o)) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        return this.entries.isEmpty();
    }

    public boolean remove(Object o) {
        for (int i = 0; i < this.entries.size(); i++) {
            JsonArray.Entry cur = (JsonArray.Entry) this.entries.get(i);
            if (cur.value.equals(o)) {
                this.entries.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("removeAll not supported");
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("retainAll not supported");
    }

    public JsonElement[] toArray() {
        JsonElement[] result = new JsonElement[this.entries.size()];
        for (int i = 0; i < this.entries.size(); i++) {
            result[i] = ((JsonArray.Entry) this.entries.get(i)).value;
        }
        return result;
    }

    public <T> T[] toArray(T[] a) {
        if (a.length < this.entries.size()) {
            a = (T[]) (new Object[this.entries.size()]);
        }
        for (int i = 0; i < this.entries.size(); i++) {
            a[i] = (T) ((JsonArray.Entry) this.entries.get(i)).value;
        }
        if (a.length > this.entries.size()) {
            a[this.entries.size()] = null;
        }
        return a;
    }

    public Iterator<JsonElement> iterator() {
        return new JsonArray.EntryIterator(this.entries);
    }

    public void add(int index, JsonElement element) {
        this.entries.add(index, new JsonArray.Entry(element));
    }

    public boolean addAll(int index, Collection<? extends JsonElement> elements) {
        if (elements.isEmpty()) {
            return false;
        } else {
            int i = index;
            for (JsonElement element : elements) {
                this.entries.add(i, new JsonArray.Entry(element));
                i++;
            }
            return true;
        }
    }

    public int indexOf(Object obj) {
        if (obj == null) {
            return -1;
        } else {
            for (int i = 0; i < this.entries.size(); i++) {
                JsonElement val = ((JsonArray.Entry) this.entries.get(i)).value;
                if (val != null && val.equals(obj)) {
                    return i;
                }
            }
            return -1;
        }
    }

    public int lastIndexOf(Object obj) {
        if (obj == null) {
            return -1;
        } else {
            for (int i = this.entries.size() - 1; i >= 0; i--) {
                JsonElement val = ((JsonArray.Entry) this.entries.get(i)).value;
                if (val != null && val.equals(obj)) {
                    return i;
                }
            }
            return -1;
        }
    }

    public ListIterator<JsonElement> listIterator() {
        return new JsonArray.EntryIterator(this.entries);
    }

    public ListIterator<JsonElement> listIterator(int index) {
        return new JsonArray.EntryIterator(this.entries, index);
    }

    public JsonElement remove(int index) {
        return ((JsonArray.Entry) this.entries.remove(index)).value;
    }

    public JsonElement set(int index, JsonElement element) {
        JsonArray.Entry old = (JsonArray.Entry) this.entries.set(index, new JsonArray.Entry(element));
        return old == null ? null : old.value;
    }

    public List<JsonElement> subList(int arg0, int arg1) {
        throw new UnsupportedOperationException();
    }

    private static class Entry {

        String comment;

        JsonElement value;

        public Entry() {
        }

        public Entry(JsonElement value) {
            this.value = value;
        }

        public boolean equals(Object other) {
            if (!(other instanceof JsonArray.Entry)) {
                return false;
            } else {
                JsonArray.Entry o = (JsonArray.Entry) other;
                return Objects.equals(this.comment, o.comment) && Objects.equals(this.value, o.value);
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[] { this.comment, this.value });
        }
    }

    private static class EntryIterator implements ListIterator<JsonElement> {

        private final ListIterator<JsonArray.Entry> delegate;

        public EntryIterator(List<JsonArray.Entry> list) {
            this.delegate = list.listIterator();
        }

        public EntryIterator(List<JsonArray.Entry> list, int index) {
            this.delegate = list.listIterator(index);
        }

        public boolean hasNext() {
            return this.delegate.hasNext();
        }

        public JsonElement next() {
            return ((JsonArray.Entry) this.delegate.next()).value;
        }

        public void remove() {
            this.delegate.remove();
        }

        public void add(JsonElement elem) {
            this.delegate.add(new JsonArray.Entry(elem));
        }

        public boolean hasPrevious() {
            return this.delegate.hasPrevious();
        }

        public int nextIndex() {
            return this.delegate.nextIndex();
        }

        public JsonElement previous() {
            return ((JsonArray.Entry) this.delegate.previous()).value;
        }

        public int previousIndex() {
            return this.delegate.previousIndex();
        }

        public void set(JsonElement obj) {
            this.delegate.set(new JsonArray.Entry(obj));
        }
    }
}