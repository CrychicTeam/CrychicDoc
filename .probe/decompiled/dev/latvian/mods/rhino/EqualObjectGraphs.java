package dev.latvian.mods.rhino;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

final class EqualObjectGraphs {

    private static final ThreadLocal<EqualObjectGraphs> instance = new ThreadLocal();

    private final Map<Object, Object> knownEquals = new IdentityHashMap();

    private final Map<Object, Object> currentlyCompared = new IdentityHashMap();

    static <T> T withThreadLocal(java.util.function.Function<EqualObjectGraphs, T> action) {
        EqualObjectGraphs currEq = (EqualObjectGraphs) instance.get();
        if (currEq == null) {
            EqualObjectGraphs eq = new EqualObjectGraphs();
            instance.set(eq);
            Object var3;
            try {
                var3 = action.apply(eq);
            } finally {
                instance.set(null);
            }
            return (T) var3;
        } else {
            return (T) action.apply(currEq);
        }
    }

    private static Iterator<Entry> sortedEntries(Map m) {
        Map sortedMap = (Map) (m instanceof SortedMap ? m : new TreeMap(m));
        return sortedMap.entrySet().iterator();
    }

    private static Object[] sortedSet(Set<?> s) {
        Object[] a = s.toArray();
        Arrays.sort(a);
        return a;
    }

    private static Object[] getSortedIds(Context cx, Scriptable s) {
        Object[] ids = getIds(cx, s);
        Arrays.sort(ids, (a, b) -> {
            if (a instanceof Integer) {
                if (b instanceof Integer) {
                    return ((Integer) a).compareTo((Integer) b);
                }
                if (b instanceof String || b instanceof Symbol) {
                    return -1;
                }
            } else if (a instanceof String) {
                if (b instanceof String) {
                    return ((String) a).compareTo((String) b);
                }
                if (b instanceof Integer) {
                    return 1;
                }
                if (b instanceof Symbol) {
                    return -1;
                }
            } else if (a instanceof Symbol) {
                if (b instanceof Symbol) {
                    return getSymbolName((Symbol) a).compareTo(getSymbolName((Symbol) b));
                }
                if (b instanceof Integer || b instanceof String) {
                    return 1;
                }
            }
            throw new ClassCastException();
        });
        return ids;
    }

    private static String getSymbolName(Symbol s) {
        if (s instanceof SymbolKey) {
            return ((SymbolKey) s).getName();
        } else if (s instanceof NativeSymbol) {
            return ((NativeSymbol) s).getKey().getName();
        } else {
            throw new ClassCastException();
        }
    }

    private static Object[] getIds(Context cx, Scriptable s) {
        return s instanceof ScriptableObject ? ((ScriptableObject) s).getIds(cx, true, true) : s.getAllIds(cx);
    }

    private static Object getValue(Scriptable s, Object id, Context cx) {
        if (id instanceof Symbol) {
            return ScriptableObject.getProperty(s, (Symbol) id, cx);
        } else if (id instanceof Integer) {
            return ScriptableObject.getProperty(s, (Integer) id, cx);
        } else if (id instanceof String) {
            return ScriptableObject.getProperty(s, (String) id, cx);
        } else {
            throw new ClassCastException();
        }
    }

    boolean equalGraphs(Context cx, Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        } else if (o1 != null && o2 != null) {
            Object curr2 = this.currentlyCompared.get(o1);
            if (curr2 == o2) {
                return true;
            } else if (curr2 != null) {
                return false;
            } else {
                Object prev2 = this.knownEquals.get(o1);
                if (prev2 == o2) {
                    return true;
                } else if (prev2 != null) {
                    return false;
                } else {
                    Object prev1 = this.knownEquals.get(o2);
                    assert prev1 != o1;
                    if (prev1 != null) {
                        return false;
                    } else {
                        this.currentlyCompared.put(o1, o2);
                        boolean eq = this.equalGraphsNoMemo(cx, o1, o2);
                        if (eq) {
                            this.knownEquals.put(o1, o2);
                            this.knownEquals.put(o2, o1);
                        }
                        this.currentlyCompared.remove(o1);
                        return eq;
                    }
                }
            }
        } else {
            return false;
        }
    }

    private boolean equalGraphsNoMemo(Context cx, Object o1, Object o2) {
        if (o1 instanceof Wrapper) {
            return o2 instanceof Wrapper && this.equalGraphs(cx, ((Wrapper) o1).unwrap(), ((Wrapper) o2).unwrap());
        } else if (o1 instanceof Scriptable) {
            return o2 instanceof Scriptable && this.equalScriptables(cx, (Scriptable) o1, (Scriptable) o2);
        } else if (o1 instanceof ConsString) {
            return ((ConsString) o1).toString().equals(o2);
        } else if (o2 instanceof ConsString) {
            return o1.equals(((ConsString) o2).toString());
        } else if (o1 instanceof SymbolKey) {
            return o2 instanceof SymbolKey && this.equalGraphs(cx, ((SymbolKey) o1).getName(), ((SymbolKey) o2).getName());
        } else if (o1 instanceof Object[]) {
            return o2 instanceof Object[] && this.equalObjectArrays(cx, (Object[]) o1, (Object[]) o2);
        } else if (o1.getClass().isArray()) {
            return Objects.deepEquals(o1, o2);
        } else if (o1 instanceof List) {
            return o2 instanceof List && this.equalLists(cx, (List<?>) o1, (List<?>) o2);
        } else if (o1 instanceof Map) {
            return o2 instanceof Map && this.equalMaps(cx, (Map<?, ?>) o1, (Map<?, ?>) o2);
        } else if (!(o1 instanceof Set)) {
            if (o1 instanceof NativeGlobal) {
                return o2 instanceof NativeGlobal;
            } else {
                return o1 instanceof JavaAdapter ? o2 instanceof JavaAdapter : o1.equals(o2);
            }
        } else {
            return o2 instanceof Set && this.equalSets(cx, (Set<?>) o1, (Set<?>) o2);
        }
    }

    private boolean equalScriptables(Context cx, Scriptable s1, Scriptable s2) {
        Object[] ids1 = getSortedIds(cx, s1);
        Object[] ids2 = getSortedIds(cx, s2);
        if (!this.equalObjectArrays(cx, ids1, ids2)) {
            return false;
        } else {
            int l = ids1.length;
            for (int i = 0; i < l; i++) {
                if (!this.equalGraphs(cx, getValue(s1, ids1[i], cx), getValue(s2, ids2[i], cx))) {
                    return false;
                }
            }
            if (!this.equalGraphs(cx, s1.getPrototype(cx), s2.getPrototype(cx))) {
                return false;
            } else if (!this.equalGraphs(cx, s1.getParentScope(), s2.getParentScope())) {
                return false;
            } else if (s1 instanceof IdFunctionObject s3) {
                if (s2 instanceof IdFunctionObject s4 && IdFunctionObject.equalObjectGraphs(cx, s3, s4, this)) {
                    return true;
                }
                return false;
            } else if (s1 instanceof ArrowFunction s3) {
                if (s2 instanceof ArrowFunction s4 && ArrowFunction.equalObjectGraphs(cx, s3, s4, this)) {
                    return true;
                }
                return false;
            } else if (s1 instanceof BoundFunction s3) {
                if (s2 instanceof BoundFunction s4 && BoundFunction.equalObjectGraphs(cx, s3, s4, this)) {
                    return true;
                }
                return false;
            } else if (!(s1 instanceof NativeSymbol s3)) {
                return true;
            } else {
                if (s2 instanceof NativeSymbol s4 && this.equalGraphs(cx, s3.getKey(), s4.getKey())) {
                    return true;
                }
                return false;
            }
        }
    }

    private boolean equalObjectArrays(Context cx, Object[] a1, Object[] a2) {
        if (a1.length != a2.length) {
            return false;
        } else {
            for (int i = 0; i < a1.length; i++) {
                if (!this.equalGraphs(cx, a1[i], a2[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean equalLists(Context cx, List<?> l1, List<?> l2) {
        if (l1.size() != l2.size()) {
            return false;
        } else {
            Iterator<?> i1 = l1.iterator();
            Iterator<?> i2 = l2.iterator();
            while (i1.hasNext() && i2.hasNext()) {
                if (!this.equalGraphs(cx, i1.next(), i2.next())) {
                    return false;
                }
            }
            assert !i1.hasNext() && !i2.hasNext();
            return true;
        }
    }

    private boolean equalMaps(Context cx, Map<?, ?> m1, Map<?, ?> m2) {
        if (m1.size() != m2.size()) {
            return false;
        } else {
            Iterator<Entry> i1 = sortedEntries(m1);
            Iterator<Entry> i2 = sortedEntries(m2);
            while (i1.hasNext() && i2.hasNext()) {
                Entry kv1 = (Entry) i1.next();
                Entry kv2 = (Entry) i2.next();
                if (!this.equalGraphs(cx, kv1.getKey(), kv2.getKey()) || !this.equalGraphs(cx, kv1.getValue(), kv2.getValue())) {
                    return false;
                }
            }
            assert !i1.hasNext() && !i2.hasNext();
            return true;
        }
    }

    private boolean equalSets(Context cx, Set<?> s1, Set<?> s2) {
        return this.equalObjectArrays(cx, sortedSet(s1), sortedSet(s2));
    }
}