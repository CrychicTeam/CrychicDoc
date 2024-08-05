package info.journeymap.shaded.kotlin.kotlin.jvm.internal;

import info.journeymap.shaded.kotlin.kotlin.Function;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function0;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function1;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function10;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function11;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function12;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function13;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function14;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function15;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function16;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function17;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function18;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function19;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function2;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function20;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function21;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function22;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function3;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function4;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function5;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function6;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function7;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function8;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function9;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.markers.KMappedMarker;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.markers.KMutableCollection;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.markers.KMutableIterable;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.markers.KMutableIterator;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.markers.KMutableList;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.markers.KMutableListIterator;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.markers.KMutableMap;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.markers.KMutableSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class TypeIntrinsics {

    private static <T extends Throwable> T sanitizeStackTrace(T throwable) {
        return Intrinsics.sanitizeStackTrace(throwable, TypeIntrinsics.class.getName());
    }

    public static void throwCce(Object argument, String requestedClassName) {
        String argumentClassName = argument == null ? "null" : argument.getClass().getName();
        throwCce(argumentClassName + " cannot be cast to " + requestedClassName);
    }

    public static void throwCce(String message) {
        throw throwCce(new ClassCastException(message));
    }

    public static ClassCastException throwCce(ClassCastException e) {
        throw (ClassCastException) sanitizeStackTrace(e);
    }

    public static boolean isMutableIterator(Object obj) {
        return obj instanceof Iterator && (!(obj instanceof KMappedMarker) || obj instanceof KMutableIterator);
    }

    public static Iterator asMutableIterator(Object obj) {
        if (obj instanceof KMappedMarker && !(obj instanceof KMutableIterator)) {
            throwCce(obj, "info.journeymap.shaded.kotlin.kotlin.collections.MutableIterator");
        }
        return castToIterator(obj);
    }

    public static Iterator asMutableIterator(Object obj, String message) {
        if (obj instanceof KMappedMarker && !(obj instanceof KMutableIterator)) {
            throwCce(message);
        }
        return castToIterator(obj);
    }

    public static Iterator castToIterator(Object obj) {
        try {
            return (Iterator) obj;
        } catch (ClassCastException var2) {
            throw throwCce(var2);
        }
    }

    public static boolean isMutableListIterator(Object obj) {
        return obj instanceof ListIterator && (!(obj instanceof KMappedMarker) || obj instanceof KMutableListIterator);
    }

    public static ListIterator asMutableListIterator(Object obj) {
        if (obj instanceof KMappedMarker && !(obj instanceof KMutableListIterator)) {
            throwCce(obj, "info.journeymap.shaded.kotlin.kotlin.collections.MutableListIterator");
        }
        return castToListIterator(obj);
    }

    public static ListIterator asMutableListIterator(Object obj, String message) {
        if (obj instanceof KMappedMarker && !(obj instanceof KMutableListIterator)) {
            throwCce(message);
        }
        return castToListIterator(obj);
    }

    public static ListIterator castToListIterator(Object obj) {
        try {
            return (ListIterator) obj;
        } catch (ClassCastException var2) {
            throw throwCce(var2);
        }
    }

    public static boolean isMutableIterable(Object obj) {
        return obj instanceof Iterable && (!(obj instanceof KMappedMarker) || obj instanceof KMutableIterable);
    }

    public static Iterable asMutableIterable(Object obj) {
        if (obj instanceof KMappedMarker && !(obj instanceof KMutableIterable)) {
            throwCce(obj, "info.journeymap.shaded.kotlin.kotlin.collections.MutableIterable");
        }
        return castToIterable(obj);
    }

    public static Iterable asMutableIterable(Object obj, String message) {
        if (obj instanceof KMappedMarker && !(obj instanceof KMutableIterable)) {
            throwCce(message);
        }
        return castToIterable(obj);
    }

    public static Iterable castToIterable(Object obj) {
        try {
            return (Iterable) obj;
        } catch (ClassCastException var2) {
            throw throwCce(var2);
        }
    }

    public static boolean isMutableCollection(Object obj) {
        return obj instanceof Collection && (!(obj instanceof KMappedMarker) || obj instanceof KMutableCollection);
    }

    public static Collection asMutableCollection(Object obj) {
        if (obj instanceof KMappedMarker && !(obj instanceof KMutableCollection)) {
            throwCce(obj, "info.journeymap.shaded.kotlin.kotlin.collections.MutableCollection");
        }
        return castToCollection(obj);
    }

    public static Collection asMutableCollection(Object obj, String message) {
        if (obj instanceof KMappedMarker && !(obj instanceof KMutableCollection)) {
            throwCce(message);
        }
        return castToCollection(obj);
    }

    public static Collection castToCollection(Object obj) {
        try {
            return (Collection) obj;
        } catch (ClassCastException var2) {
            throw throwCce(var2);
        }
    }

    public static boolean isMutableList(Object obj) {
        return obj instanceof List && (!(obj instanceof KMappedMarker) || obj instanceof KMutableList);
    }

    public static List asMutableList(Object obj) {
        if (obj instanceof KMappedMarker && !(obj instanceof KMutableList)) {
            throwCce(obj, "info.journeymap.shaded.kotlin.kotlin.collections.MutableList");
        }
        return castToList(obj);
    }

    public static List asMutableList(Object obj, String message) {
        if (obj instanceof KMappedMarker && !(obj instanceof KMutableList)) {
            throwCce(message);
        }
        return castToList(obj);
    }

    public static List castToList(Object obj) {
        try {
            return (List) obj;
        } catch (ClassCastException var2) {
            throw throwCce(var2);
        }
    }

    public static boolean isMutableSet(Object obj) {
        return obj instanceof Set && (!(obj instanceof KMappedMarker) || obj instanceof KMutableSet);
    }

    public static Set asMutableSet(Object obj) {
        if (obj instanceof KMappedMarker && !(obj instanceof KMutableSet)) {
            throwCce(obj, "info.journeymap.shaded.kotlin.kotlin.collections.MutableSet");
        }
        return castToSet(obj);
    }

    public static Set asMutableSet(Object obj, String message) {
        if (obj instanceof KMappedMarker && !(obj instanceof KMutableSet)) {
            throwCce(message);
        }
        return castToSet(obj);
    }

    public static Set castToSet(Object obj) {
        try {
            return (Set) obj;
        } catch (ClassCastException var2) {
            throw throwCce(var2);
        }
    }

    public static boolean isMutableMap(Object obj) {
        return obj instanceof Map && (!(obj instanceof KMappedMarker) || obj instanceof KMutableMap);
    }

    public static Map asMutableMap(Object obj) {
        if (obj instanceof KMappedMarker && !(obj instanceof KMutableMap)) {
            throwCce(obj, "info.journeymap.shaded.kotlin.kotlin.collections.MutableMap");
        }
        return castToMap(obj);
    }

    public static Map asMutableMap(Object obj, String message) {
        if (obj instanceof KMappedMarker && !(obj instanceof KMutableMap)) {
            throwCce(message);
        }
        return castToMap(obj);
    }

    public static Map castToMap(Object obj) {
        try {
            return (Map) obj;
        } catch (ClassCastException var2) {
            throw throwCce(var2);
        }
    }

    public static boolean isMutableMapEntry(Object obj) {
        return obj instanceof Entry && (!(obj instanceof KMappedMarker) || obj instanceof KMutableMap.Entry);
    }

    public static Entry asMutableMapEntry(Object obj) {
        if (obj instanceof KMappedMarker && !(obj instanceof KMutableMap.Entry)) {
            throwCce(obj, "info.journeymap.shaded.kotlin.kotlin.collections.MutableMap.MutableEntry");
        }
        return castToMapEntry(obj);
    }

    public static Entry asMutableMapEntry(Object obj, String message) {
        if (obj instanceof KMappedMarker && !(obj instanceof KMutableMap.Entry)) {
            throwCce(message);
        }
        return castToMapEntry(obj);
    }

    public static Entry castToMapEntry(Object obj) {
        try {
            return (Entry) obj;
        } catch (ClassCastException var2) {
            throw throwCce(var2);
        }
    }

    public static int getFunctionArity(Object obj) {
        if (obj instanceof FunctionBase) {
            return ((FunctionBase) obj).getArity();
        } else if (obj instanceof Function0) {
            return 0;
        } else if (obj instanceof Function1) {
            return 1;
        } else if (obj instanceof Function2) {
            return 2;
        } else if (obj instanceof Function3) {
            return 3;
        } else if (obj instanceof Function4) {
            return 4;
        } else if (obj instanceof Function5) {
            return 5;
        } else if (obj instanceof Function6) {
            return 6;
        } else if (obj instanceof Function7) {
            return 7;
        } else if (obj instanceof Function8) {
            return 8;
        } else if (obj instanceof Function9) {
            return 9;
        } else if (obj instanceof Function10) {
            return 10;
        } else if (obj instanceof Function11) {
            return 11;
        } else if (obj instanceof Function12) {
            return 12;
        } else if (obj instanceof Function13) {
            return 13;
        } else if (obj instanceof Function14) {
            return 14;
        } else if (obj instanceof Function15) {
            return 15;
        } else if (obj instanceof Function16) {
            return 16;
        } else if (obj instanceof Function17) {
            return 17;
        } else if (obj instanceof Function18) {
            return 18;
        } else if (obj instanceof Function19) {
            return 19;
        } else if (obj instanceof Function20) {
            return 20;
        } else if (obj instanceof Function21) {
            return 21;
        } else {
            return obj instanceof Function22 ? 22 : -1;
        }
    }

    public static boolean isFunctionOfArity(Object obj, int arity) {
        return obj instanceof Function && getFunctionArity(obj) == arity;
    }

    public static Object beforeCheckcastToFunctionOfArity(Object obj, int arity) {
        if (obj != null && !isFunctionOfArity(obj, arity)) {
            throwCce(obj, "info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function" + arity);
        }
        return obj;
    }

    public static Object beforeCheckcastToFunctionOfArity(Object obj, int arity, String message) {
        if (obj != null && !isFunctionOfArity(obj, arity)) {
            throwCce(message);
        }
        return obj;
    }
}