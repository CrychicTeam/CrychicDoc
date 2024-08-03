package de.keksuccino.konkrete.json.jsonpath.internal;

import de.keksuccino.konkrete.json.jsonpath.Configuration;
import de.keksuccino.konkrete.json.jsonpath.InvalidModificationException;
import de.keksuccino.konkrete.json.jsonpath.MapFunction;
import de.keksuccino.konkrete.json.jsonpath.PathNotFoundException;
import de.keksuccino.konkrete.json.jsonpath.spi.json.JsonProvider;
import java.util.Collection;

public abstract class PathRef implements Comparable<PathRef> {

    public static final PathRef NO_OP = new PathRef(null) {

        @Override
        public Object getAccessor() {
            return null;
        }

        @Override
        public void set(Object newVal, Configuration configuration) {
        }

        @Override
        public void convert(MapFunction mapFunction, Configuration configuration) {
        }

        @Override
        public void delete(Configuration configuration) {
        }

        @Override
        public void add(Object newVal, Configuration configuration) {
        }

        @Override
        public void put(String key, Object newVal, Configuration configuration) {
        }

        @Override
        public void renameKey(String oldKeyName, String newKeyName, Configuration configuration) {
        }
    };

    protected Object parent;

    private PathRef(Object parent) {
        this.parent = parent;
    }

    abstract Object getAccessor();

    public abstract void set(Object var1, Configuration var2);

    public abstract void convert(MapFunction var1, Configuration var2);

    public abstract void delete(Configuration var1);

    public abstract void add(Object var1, Configuration var2);

    public abstract void put(String var1, Object var2, Configuration var3);

    public abstract void renameKey(String var1, String var2, Configuration var3);

    protected void renameInMap(Object targetMap, String oldKeyName, String newKeyName, Configuration configuration) {
        if (configuration.jsonProvider().isMap(targetMap)) {
            if (configuration.jsonProvider().getMapValue(targetMap, oldKeyName) == JsonProvider.UNDEFINED) {
                throw new PathNotFoundException("No results for Key " + oldKeyName + " found in map!");
            } else {
                configuration.jsonProvider().setProperty(targetMap, newKeyName, configuration.jsonProvider().getMapValue(targetMap, oldKeyName));
                configuration.jsonProvider().removeProperty(targetMap, oldKeyName);
            }
        } else {
            throw new InvalidModificationException("Can only rename properties in a map");
        }
    }

    protected boolean targetInvalid(Object target) {
        return target == JsonProvider.UNDEFINED || target == null;
    }

    public int compareTo(PathRef o) {
        return this.getAccessor().toString().compareTo(o.getAccessor().toString()) * -1;
    }

    public static PathRef create(Object obj, String property) {
        return new PathRef.ObjectPropertyPathRef(obj, property);
    }

    public static PathRef create(Object obj, Collection<String> properties) {
        return new PathRef.ObjectMultiPropertyPathRef(obj, properties);
    }

    public static PathRef create(Object array, int index) {
        return new PathRef.ArrayIndexPathRef(array, index);
    }

    public static PathRef createRoot(Object root) {
        return new PathRef.RootPathRef(root);
    }

    private static class ArrayIndexPathRef extends PathRef {

        private int index;

        private ArrayIndexPathRef(Object parent, int index) {
            super(parent);
            this.index = index;
        }

        @Override
        public void set(Object newVal, Configuration configuration) {
            configuration.jsonProvider().setArrayIndex(this.parent, this.index, newVal);
        }

        @Override
        public void convert(MapFunction mapFunction, Configuration configuration) {
            Object currentValue = configuration.jsonProvider().getArrayIndex(this.parent, this.index);
            configuration.jsonProvider().setArrayIndex(this.parent, this.index, mapFunction.map(currentValue, configuration));
        }

        @Override
        public void delete(Configuration configuration) {
            configuration.jsonProvider().removeProperty(this.parent, this.index);
        }

        @Override
        public void add(Object value, Configuration configuration) {
            Object target = configuration.jsonProvider().getArrayIndex(this.parent, this.index);
            if (!this.targetInvalid(target)) {
                if (configuration.jsonProvider().isArray(target)) {
                    configuration.jsonProvider().setProperty(target, null, value);
                } else {
                    throw new InvalidModificationException("Can only add to an array");
                }
            }
        }

        @Override
        public void put(String key, Object value, Configuration configuration) {
            Object target = configuration.jsonProvider().getArrayIndex(this.parent, this.index);
            if (!this.targetInvalid(target)) {
                if (configuration.jsonProvider().isMap(target)) {
                    configuration.jsonProvider().setProperty(target, key, value);
                } else {
                    throw new InvalidModificationException("Can only add properties to a map");
                }
            }
        }

        @Override
        public void renameKey(String oldKeyName, String newKeyName, Configuration configuration) {
            Object target = configuration.jsonProvider().getArrayIndex(this.parent, this.index);
            if (!this.targetInvalid(target)) {
                this.renameInMap(target, oldKeyName, newKeyName, configuration);
            }
        }

        @Override
        public Object getAccessor() {
            return this.index;
        }

        @Override
        public int compareTo(PathRef o) {
            return o instanceof PathRef.ArrayIndexPathRef pf ? Integer.compare(pf.index, this.index) : super.compareTo(o);
        }
    }

    private static class ObjectMultiPropertyPathRef extends PathRef {

        private Collection<String> properties;

        private ObjectMultiPropertyPathRef(Object parent, Collection<String> properties) {
            super(parent);
            this.properties = properties;
        }

        @Override
        public void set(Object newVal, Configuration configuration) {
            for (String property : this.properties) {
                configuration.jsonProvider().setProperty(this.parent, property, newVal);
            }
        }

        @Override
        public void convert(MapFunction mapFunction, Configuration configuration) {
            for (String property : this.properties) {
                Object currentValue = configuration.jsonProvider().getMapValue(this.parent, property);
                if (currentValue != JsonProvider.UNDEFINED) {
                    configuration.jsonProvider().setProperty(this.parent, property, mapFunction.map(currentValue, configuration));
                }
            }
        }

        @Override
        public void delete(Configuration configuration) {
            for (String property : this.properties) {
                configuration.jsonProvider().removeProperty(this.parent, property);
            }
        }

        @Override
        public void add(Object newVal, Configuration configuration) {
            throw new InvalidModificationException("Add can not be performed to multiple properties");
        }

        @Override
        public void put(String key, Object newVal, Configuration configuration) {
            throw new InvalidModificationException("Put can not be performed to multiple properties");
        }

        @Override
        public void renameKey(String oldKeyName, String newKeyName, Configuration configuration) {
            throw new InvalidModificationException("Rename can not be performed to multiple properties");
        }

        @Override
        public Object getAccessor() {
            return Utils.join("&&", this.properties);
        }
    }

    private static class ObjectPropertyPathRef extends PathRef {

        private String property;

        private ObjectPropertyPathRef(Object parent, String property) {
            super(parent);
            this.property = property;
        }

        @Override
        public void set(Object newVal, Configuration configuration) {
            configuration.jsonProvider().setProperty(this.parent, this.property, newVal);
        }

        @Override
        public void convert(MapFunction mapFunction, Configuration configuration) {
            Object currentValue = configuration.jsonProvider().getMapValue(this.parent, this.property);
            configuration.jsonProvider().setProperty(this.parent, this.property, mapFunction.map(currentValue, configuration));
        }

        @Override
        public void delete(Configuration configuration) {
            configuration.jsonProvider().removeProperty(this.parent, this.property);
        }

        @Override
        public void add(Object value, Configuration configuration) {
            Object target = configuration.jsonProvider().getMapValue(this.parent, this.property);
            if (!this.targetInvalid(target)) {
                if (configuration.jsonProvider().isArray(target)) {
                    configuration.jsonProvider().setArrayIndex(target, configuration.jsonProvider().length(target), value);
                } else {
                    throw new InvalidModificationException("Can only add to an array");
                }
            }
        }

        @Override
        public void put(String key, Object value, Configuration configuration) {
            Object target = configuration.jsonProvider().getMapValue(this.parent, this.property);
            if (!this.targetInvalid(target)) {
                if (configuration.jsonProvider().isMap(target)) {
                    configuration.jsonProvider().setProperty(target, key, value);
                } else {
                    throw new InvalidModificationException("Can only add properties to a map");
                }
            }
        }

        @Override
        public void renameKey(String oldKeyName, String newKeyName, Configuration configuration) {
            Object target = configuration.jsonProvider().getMapValue(this.parent, this.property);
            if (!this.targetInvalid(target)) {
                this.renameInMap(target, oldKeyName, newKeyName, configuration);
            }
        }

        @Override
        public Object getAccessor() {
            return this.property;
        }
    }

    private static class RootPathRef extends PathRef {

        private RootPathRef(Object parent) {
            super(parent);
        }

        @Override
        Object getAccessor() {
            return "$";
        }

        @Override
        public void set(Object newVal, Configuration configuration) {
            throw new InvalidModificationException("Invalid set operation");
        }

        @Override
        public void convert(MapFunction mapFunction, Configuration configuration) {
            throw new InvalidModificationException("Invalid map operation");
        }

        @Override
        public void delete(Configuration configuration) {
            throw new InvalidModificationException("Invalid delete operation");
        }

        @Override
        public void add(Object newVal, Configuration configuration) {
            if (configuration.jsonProvider().isArray(this.parent)) {
                configuration.jsonProvider().setArrayIndex(this.parent, configuration.jsonProvider().length(this.parent), newVal);
            } else {
                throw new InvalidModificationException("Invalid add operation. $ is not an array");
            }
        }

        @Override
        public void put(String key, Object newVal, Configuration configuration) {
            if (configuration.jsonProvider().isMap(this.parent)) {
                configuration.jsonProvider().setProperty(this.parent, key, newVal);
            } else {
                throw new InvalidModificationException("Invalid put operation. $ is not a map");
            }
        }

        @Override
        public void renameKey(String oldKeyName, String newKeyName, Configuration configuration) {
            Object target = this.parent;
            if (!this.targetInvalid(target)) {
                this.renameInMap(target, oldKeyName, newKeyName, configuration);
            }
        }
    }
}