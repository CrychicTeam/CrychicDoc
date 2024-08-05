package dev.latvian.mods.rhino;

import dev.latvian.mods.rhino.util.Deletable;
import dev.latvian.mods.rhino.util.ValueUnwrapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;

public class NativeJavaList extends NativeJavaObject {

    private final List list;

    private final Class<?> listType;

    private final ValueUnwrapper valueUnwrapper;

    public NativeJavaList(Context cx, Scriptable scope, Object jo, List list, @Nullable Class<?> listType, ValueUnwrapper valueUnwrapper) {
        super(scope, jo, jo.getClass(), cx);
        this.list = list;
        this.listType = listType;
        this.valueUnwrapper = valueUnwrapper;
    }

    public NativeJavaList(Context cx, Scriptable scope, Object jo, List list) {
        this(cx, scope, jo, list, null, ValueUnwrapper.DEFAULT);
    }

    @Override
    public String getClassName() {
        return "JavaList";
    }

    @Override
    public boolean has(Context cx, int index, Scriptable start) {
        return this.isWithValidIndex(index) ? true : super.has(cx, index, start);
    }

    @Override
    public boolean has(Context cx, Symbol key, Scriptable start) {
        return SymbolKey.IS_CONCAT_SPREADABLE.equals(key) ? true : super.has(cx, key, start);
    }

    @Override
    public Object get(Context cx, int index, Scriptable start) {
        return this.isWithValidIndex(index) ? this.valueUnwrapper.unwrap(cx, this, this.list.get(index)) : Undefined.instance;
    }

    @Override
    public Object get(Context cx, Symbol key, Scriptable start) {
        return SymbolKey.IS_CONCAT_SPREADABLE.equals(key) ? Boolean.TRUE : super.get(cx, key, start);
    }

    @Override
    public void put(Context cx, int index, Scriptable start, Object value) {
        if (this.isWithValidIndex(index)) {
            this.list.set(index, Context.jsToJava(cx, value, this.listType));
        } else {
            super.put(cx, index, start, value);
        }
    }

    @Override
    public Object[] getIds(Context cx) {
        List<?> list = (List<?>) this.javaObject;
        Object[] result = new Object[list.size()];
        int i = list.size();
        while (--i >= 0) {
            result[i] = i;
        }
        return result;
    }

    private boolean isWithValidIndex(int index) {
        return index >= 0 && index < this.list.size();
    }

    @Override
    public void delete(Context cx, int index) {
        if (this.isWithValidIndex(index)) {
            Deletable.deleteObject(this.list.remove(index));
        }
    }

    @Override
    protected void initMembers(Context cx, Scriptable scope) {
        super.initMembers(cx, scope);
        this.addCustomProperty("length", this::getLength);
        this.addCustomFunction("push", this::push, new Class[] { Object.class });
        this.addCustomFunction("pop", this::pop);
        this.addCustomFunction("shift", this::shift);
        this.addCustomFunction("unshift", this::unshift, new Class[] { Object.class });
        this.addCustomFunction("concat", this::concat, new Class[] { List.class });
        this.addCustomFunction("join", this::join, new Class[] { String.class });
        this.addCustomFunction("reverse", this::reverse);
        this.addCustomFunction("slice", this::slice, new Class[] { Object.class });
        this.addCustomFunction("splice", this::splice, new Class[] { Object.class });
        this.addCustomFunction("every", this::every, new Class[] { Predicate.class });
        this.addCustomFunction("some", this::some, new Class[] { Predicate.class });
        this.addCustomFunction("filter", this::filter, new Class[] { Predicate.class });
        this.addCustomFunction("map", this::map, new Class[] { java.util.function.Function.class });
        this.addCustomFunction("reduce", this::reduce, new Class[] { BinaryOperator.class });
        this.addCustomFunction("reduceRight", this::reduceRight, new Class[] { BinaryOperator.class });
        this.addCustomFunction("find", this::find, new Class[] { Predicate.class });
        this.addCustomFunction("findIndex", this::findIndex, new Class[] { Predicate.class });
        this.addCustomFunction("findLast", this::findLast, new Class[] { Predicate.class });
        this.addCustomFunction("findLastIndex", this::findLastIndex, new Class[] { Predicate.class });
    }

    private int getLength(Context cx) {
        return this.list.size();
    }

    private int push(Context cx, Object[] args) {
        if (args.length == 1) {
            this.list.add(Context.jsToJava(cx, args[0], this.listType));
        } else if (args.length > 1) {
            Object[] args1 = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                args1[i] = Context.jsToJava(cx, args[i], this.listType);
            }
            this.list.addAll(Arrays.asList(args1));
        }
        return this.list.size();
    }

    private Object pop(Context cx) {
        return this.list.isEmpty() ? Undefined.instance : this.list.remove(this.list.size() - 1);
    }

    private Object shift(Context cx) {
        return this.list.isEmpty() ? Undefined.instance : this.list.remove(0);
    }

    private int unshift(Context cx, Object[] args) {
        for (int i = args.length - 1; i >= 0; i--) {
            this.list.add(0, Context.jsToJava(cx, args[i], this.listType));
        }
        return this.list.size();
    }

    private Object concat(Context cx, Object[] args) {
        List<Object> list1 = new ArrayList(this.list);
        if (args.length > 0 && args[0] instanceof List) {
            list1.addAll((List) args[0]);
        }
        return list1;
    }

    private String join(Context cx, Object[] args) {
        if (this.list.isEmpty()) {
            return "";
        } else if (this.list.size() == 1) {
            return ScriptRuntime.toString(cx, this.list.get(0));
        } else {
            String j = ScriptRuntime.toString(cx, args[0]);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.list.size(); i++) {
                if (i > 0) {
                    sb.append(j);
                }
                sb.append(ScriptRuntime.toString(cx, this.list.get(i)));
            }
            return sb.toString();
        }
    }

    private NativeJavaList reverse(Context cx) {
        if (this.list.size() > 1) {
            Collections.reverse(this.list);
        }
        return this;
    }

    private Object slice(Context cx, Object[] args) {
        throw new IllegalStateException("Not implemented yet!");
    }

    private Object splice(Context cx, Object[] args) {
        throw new IllegalStateException("Not implemented yet!");
    }

    private Object every(Context cx, Object[] args) {
        Predicate predicate = (Predicate) args[0];
        for (Object o : this.list) {
            if (!predicate.test(o)) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    private Object some(Context cx, Object[] args) {
        Predicate predicate = (Predicate) args[0];
        for (Object o : this.list) {
            if (predicate.test(o)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    private Object filter(Context cx, Object[] args) {
        if (this.list.isEmpty()) {
            return this;
        } else {
            Predicate predicate = (Predicate) args[0];
            List<Object> list1 = new ArrayList();
            for (Object o : this.list) {
                if (predicate.test(o)) {
                    list1.add(o);
                }
            }
            return list1;
        }
    }

    private Object map(Context cx, Object[] args) {
        if (this.list.isEmpty()) {
            return this;
        } else {
            java.util.function.Function function = (java.util.function.Function) args[0];
            List<Object> list1 = new ArrayList();
            for (Object o : this.list) {
                list1.add(function.apply(o));
            }
            return list1;
        }
    }

    private Object reduce(Context cx, Object[] args) {
        if (this.list.isEmpty()) {
            return Undefined.instance;
        } else if (this.list.size() == 1) {
            return this.list.get(0);
        } else {
            BinaryOperator operator = (BinaryOperator) args[0];
            Object o = this.valueUnwrapper.unwrap(cx, this, this.list.get(0));
            for (int i = 1; i < this.list.size(); i++) {
                o = this.valueUnwrapper.unwrap(cx, this, operator.apply(o, this.valueUnwrapper.unwrap(cx, this, this.list.get(i))));
            }
            return o;
        }
    }

    private Object reduceRight(Context cx, Object[] args) {
        if (this.list.isEmpty()) {
            return Undefined.instance;
        } else if (this.list.size() == 1) {
            return this.list.get(0);
        } else {
            BinaryOperator operator = (BinaryOperator) args[0];
            Object o = this.valueUnwrapper.unwrap(cx, this, this.list.get(0));
            for (int i = this.list.size() - 1; i >= 1; i--) {
                o = this.valueUnwrapper.unwrap(cx, this, operator.apply(o, this.valueUnwrapper.unwrap(cx, this, this.list.get(i))));
            }
            return o;
        }
    }

    private Object find(Context cx, Object[] args) {
        if (this.list.isEmpty()) {
            return Undefined.instance;
        } else {
            Predicate predicate = (Predicate) args[0];
            for (Object o : this.list) {
                if (predicate.test(o)) {
                    return o;
                }
            }
            return Undefined.instance;
        }
    }

    private Object findIndex(Context cx, Object[] args) {
        if (this.list.isEmpty()) {
            return -1;
        } else {
            Predicate predicate = (Predicate) args[0];
            for (int i = 0; i < this.list.size(); i++) {
                if (predicate.test(this.list.get(i))) {
                    return i;
                }
            }
            return -1;
        }
    }

    private Object findLast(Context cx, Object[] args) {
        if (this.list.isEmpty()) {
            return Undefined.instance;
        } else {
            Predicate predicate = (Predicate) args[0];
            for (int i = this.list.size() - 1; i >= 0; i--) {
                Object o = this.list.get(i);
                if (predicate.test(o)) {
                    return o;
                }
            }
            return Undefined.instance;
        }
    }

    private Object findLastIndex(Context cx, Object[] args) {
        if (this.list.isEmpty()) {
            return -1;
        } else {
            Predicate predicate = (Predicate) args[0];
            for (int i = this.list.size() - 1; i >= 0; i--) {
                if (predicate.test(this.list.get(i))) {
                    return i;
                }
            }
            return -1;
        }
    }
}