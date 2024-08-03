package me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

abstract class Container {

    abstract boolean accepts(String var1);

    abstract void put(String var1, Object var2);

    abstract Object get(String var1);

    abstract boolean isImplicit();

    private Container() {
    }

    static class Table extends Container {

        private final Map<String, Object> values = new HashMap();

        final String name;

        final boolean implicit;

        Table() {
            this(null, false);
        }

        public Table(String name) {
            this(name, false);
        }

        public Table(String tableName, boolean implicit) {
            this.name = tableName;
            this.implicit = implicit;
        }

        @Override
        boolean accepts(String key) {
            return !this.values.containsKey(key) || this.values.get(key) instanceof Container.TableArray;
        }

        @Override
        void put(String key, Object value) {
            this.values.put(key, value);
        }

        @Override
        Object get(String key) {
            return this.values.get(key);
        }

        @Override
        boolean isImplicit() {
            return this.implicit;
        }

        Map<String, Object> consume() {
            for (Entry<String, Object> entry : this.values.entrySet()) {
                if (entry.getValue() instanceof Container.Table) {
                    entry.setValue(((Container.Table) entry.getValue()).consume());
                } else if (entry.getValue() instanceof Container.TableArray) {
                    entry.setValue(((Container.TableArray) entry.getValue()).getValues());
                }
            }
            return this.values;
        }

        public String toString() {
            return this.values.toString();
        }
    }

    static class TableArray extends Container {

        private final List<Container.Table> values = new ArrayList();

        TableArray() {
            this.values.add(new Container.Table());
        }

        @Override
        boolean accepts(String key) {
            return this.getCurrent().accepts(key);
        }

        @Override
        void put(String key, Object value) {
            this.values.add((Container.Table) value);
        }

        @Override
        Object get(String key) {
            throw new UnsupportedOperationException();
        }

        @Override
        boolean isImplicit() {
            return false;
        }

        List<Map<String, Object>> getValues() {
            ArrayList<Map<String, Object>> unwrappedValues = new ArrayList();
            for (Container.Table table : this.values) {
                unwrappedValues.add(table.consume());
            }
            return unwrappedValues;
        }

        Container.Table getCurrent() {
            return (Container.Table) this.values.get(this.values.size() - 1);
        }

        public String toString() {
            return this.values.toString();
        }
    }
}