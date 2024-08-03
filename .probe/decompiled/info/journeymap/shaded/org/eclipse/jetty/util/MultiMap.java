package info.journeymap.shaded.org.eclipse.jetty.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MultiMap<V> extends HashMap<String, List<V>> {

    public MultiMap() {
    }

    public MultiMap(Map<String, List<V>> map) {
        super(map);
    }

    public MultiMap(MultiMap<V> map) {
        super(map);
    }

    public List<V> getValues(String name) {
        List<V> vals = (List<V>) super.get(name);
        return vals != null && !vals.isEmpty() ? vals : null;
    }

    public V getValue(String name, int i) {
        List<V> vals = this.getValues(name);
        if (vals == null) {
            return null;
        } else {
            return (V) (i == 0 && vals.isEmpty() ? null : vals.get(i));
        }
    }

    public String getString(String name) {
        List<V> vals = (List<V>) this.get(name);
        if (vals != null && !vals.isEmpty()) {
            if (vals.size() == 1) {
                return vals.get(0).toString();
            } else {
                StringBuilder values = new StringBuilder(128);
                for (V e : vals) {
                    if (e != null) {
                        if (values.length() > 0) {
                            values.append(',');
                        }
                        values.append(e.toString());
                    }
                }
                return values.toString();
            }
        } else {
            return null;
        }
    }

    public List<V> put(String name, V value) {
        if (value == null) {
            return (List<V>) super.put(name, null);
        } else {
            List<V> vals = new ArrayList();
            vals.add(value);
            return (List<V>) this.put(name, vals);
        }
    }

    public void putAllValues(Map<String, V> input) {
        for (Entry<String, V> entry : input.entrySet()) {
            this.put((String) entry.getKey(), (V) entry.getValue());
        }
    }

    public List<V> putValues(String name, List<V> values) {
        return (List<V>) super.put(name, values);
    }

    @SafeVarargs
    public final List<V> putValues(String name, V... values) {
        List<V> list = new ArrayList();
        list.addAll(Arrays.asList(values));
        return (List<V>) super.put(name, list);
    }

    public void add(String name, V value) {
        List<V> lo = (List<V>) this.get(name);
        if (lo == null) {
            lo = new ArrayList();
        }
        lo.add(value);
        super.put(name, lo);
    }

    public void addValues(String name, List<V> values) {
        List<V> lo = (List<V>) this.get(name);
        if (lo == null) {
            lo = new ArrayList();
        }
        lo.addAll(values);
        this.put(name, lo);
    }

    public void addValues(String name, V[] values) {
        List<V> lo = (List<V>) this.get(name);
        if (lo == null) {
            lo = new ArrayList();
        }
        lo.addAll(Arrays.asList(values));
        this.put(name, lo);
    }

    public boolean addAllValues(MultiMap<V> map) {
        boolean merged = false;
        if (map != null && !map.isEmpty()) {
            for (Entry<String, List<V>> entry : map.entrySet()) {
                String name = (String) entry.getKey();
                List<V> values = (List<V>) entry.getValue();
                if (this.containsKey(name)) {
                    merged = true;
                }
                this.addValues(name, values);
            }
            return merged;
        } else {
            return merged;
        }
    }

    public boolean removeValue(String name, V value) {
        List<V> lo = (List<V>) this.get(name);
        if (lo != null && !lo.isEmpty()) {
            boolean ret = lo.remove(value);
            if (lo.isEmpty()) {
                this.remove(name);
            } else {
                this.put(name, lo);
            }
            return ret;
        } else {
            return false;
        }
    }

    public boolean containsSimpleValue(V value) {
        for (List<V> vals : this.values()) {
            if (vals.size() == 1 && vals.contains(value)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        Iterator<Entry<String, List<V>>> iter = this.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (boolean delim = false; iter.hasNext(); delim = true) {
            Entry<String, List<V>> e = (Entry<String, List<V>>) iter.next();
            if (delim) {
                sb.append(", ");
            }
            String key = (String) e.getKey();
            List<V> vals = (List<V>) e.getValue();
            sb.append(key);
            sb.append('=');
            if (vals.size() == 1) {
                sb.append(vals.get(0));
            } else {
                sb.append(vals);
            }
        }
        sb.append('}');
        return sb.toString();
    }

    public Map<String, String[]> toStringArrayMap() {
        HashMap<String, String[]> map = new HashMap<String, String[]>(this.size() * 3 / 2) {

            public String toString() {
                StringBuilder b = new StringBuilder();
                b.append('{');
                for (String k : super.keySet()) {
                    if (b.length() > 1) {
                        b.append(',');
                    }
                    b.append(k);
                    b.append('=');
                    b.append(Arrays.asList((Object[]) super.get(k)));
                }
                b.append('}');
                return b.toString();
            }
        };
        for (Entry<String, List<V>> entry : this.entrySet()) {
            String[] a = null;
            if (entry.getValue() != null) {
                a = new String[((List) entry.getValue()).size()];
                a = (String[]) ((List) entry.getValue()).toArray(a);
            }
            map.put(entry.getKey(), a);
        }
        return map;
    }
}