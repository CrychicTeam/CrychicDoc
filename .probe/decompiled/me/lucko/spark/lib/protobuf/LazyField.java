package me.lucko.spark.lib.protobuf;

import java.util.Iterator;
import java.util.Map.Entry;

public class LazyField extends LazyFieldLite {

    private final MessageLite defaultInstance;

    public LazyField(MessageLite defaultInstance, ExtensionRegistryLite extensionRegistry, ByteString bytes) {
        super(extensionRegistry, bytes);
        this.defaultInstance = defaultInstance;
    }

    @Override
    public boolean containsDefaultInstance() {
        return super.containsDefaultInstance() || this.value == this.defaultInstance;
    }

    public MessageLite getValue() {
        return this.getValue(this.defaultInstance);
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.getValue().equals(obj);
    }

    public String toString() {
        return this.getValue().toString();
    }

    static class LazyEntry<K> implements Entry<K, Object> {

        private Entry<K, LazyField> entry;

        private LazyEntry(Entry<K, LazyField> entry) {
            this.entry = entry;
        }

        public K getKey() {
            return (K) this.entry.getKey();
        }

        public Object getValue() {
            LazyField field = (LazyField) this.entry.getValue();
            return field == null ? null : field.getValue();
        }

        public LazyField getField() {
            return (LazyField) this.entry.getValue();
        }

        public Object setValue(Object value) {
            if (!(value instanceof MessageLite)) {
                throw new IllegalArgumentException("LazyField now only used for MessageSet, and the value of MessageSet must be an instance of MessageLite");
            } else {
                return ((LazyField) this.entry.getValue()).setValue((MessageLite) value);
            }
        }
    }

    static class LazyIterator<K> implements Iterator<Entry<K, Object>> {

        private Iterator<Entry<K, Object>> iterator;

        public LazyIterator(Iterator<Entry<K, Object>> iterator) {
            this.iterator = iterator;
        }

        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        public Entry<K, Object> next() {
            Entry<K, ?> entry = (Entry<K, ?>) this.iterator.next();
            return (Entry<K, Object>) (entry.getValue() instanceof LazyField ? new LazyField.LazyEntry(entry) : entry);
        }

        public void remove() {
            this.iterator.remove();
        }
    }
}