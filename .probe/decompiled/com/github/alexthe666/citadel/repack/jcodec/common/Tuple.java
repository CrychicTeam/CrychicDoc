package com.github.alexthe666.citadel.repack.jcodec.common;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Tuple {

    public static <T0> Tuple._1<T0> single(T0 v0) {
        return new Tuple._1<>(v0);
    }

    public static <T0, T1> Tuple._2<T0, T1> pair(T0 v0, T1 v1) {
        return new Tuple._2<>(v0, v1);
    }

    public static <T0, T1, T2> Tuple._3<T0, T1, T2> triple(T0 v0, T1 v1, T2 v2) {
        return new Tuple._3<>(v0, v1, v2);
    }

    public static <T0, T1, T2, T3> Tuple._4<T0, T1, T2, T3> quad(T0 v0, T1 v1, T2 v2, T3 v3) {
        return new Tuple._4<>(v0, v1, v2, v3);
    }

    public static <T0, T1> Map<T0, T1> asMap(Iterable<Tuple._2<T0, T1>> it) {
        HashMap<T0, T1> result = new HashMap();
        for (Tuple._2<T0, T1> el : it) {
            result.put(el.v0, el.v1);
        }
        return result;
    }

    public static <T0, T1> Map<T0, T1> arrayAsMap(Tuple._2<T0, T1>[] arr) {
        HashMap<T0, T1> result = new HashMap();
        for (int i = 0; i < arr.length; i++) {
            Tuple._2<T0, T1> el = arr[i];
            result.put(el.v0, el.v1);
        }
        return result;
    }

    public static <T0, T1> List<Tuple._2<T0, T1>> asList(Map<T0, T1> m) {
        LinkedList<Tuple._2<T0, T1>> result = new LinkedList();
        for (Entry<T0, T1> entry : m.entrySet()) {
            result.add(pair(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    public static <T0> List<T0> _1_project0(List<Tuple._1<T0>> temp) {
        List<T0> result = new LinkedList();
        for (Tuple._1<T0> _1 : temp) {
            result.add(_1.v0);
        }
        return result;
    }

    public static <T0, T1> List<T0> _2_project0(List<Tuple._2<T0, T1>> temp) {
        List<T0> result = new LinkedList();
        for (Tuple._2<T0, T1> _2 : temp) {
            result.add(_2.v0);
        }
        return result;
    }

    public static <T0, T1> List<T1> _2_project1(List<Tuple._2<T0, T1>> temp) {
        List<T1> result = new LinkedList();
        for (Tuple._2<T0, T1> _2 : temp) {
            result.add(_2.v1);
        }
        return result;
    }

    public static <T0, T1, T2, T3> List<T0> _3_project0(List<Tuple._3<T0, T1, T2>> temp) {
        List<T0> result = new LinkedList();
        for (Tuple._3<T0, T1, T2> _3 : temp) {
            result.add(_3.v0);
        }
        return result;
    }

    public static <T0, T1, T2, T3> List<T1> _3_project1(List<Tuple._3<T0, T1, T2>> temp) {
        List<T1> result = new LinkedList();
        for (Tuple._3<T0, T1, T2> _3 : temp) {
            result.add(_3.v1);
        }
        return result;
    }

    public static <T0, T1, T2, T3> List<T2> _3_project2(List<Tuple._3<T0, T1, T2>> temp) {
        List<T2> result = new LinkedList();
        for (Tuple._3<T0, T1, T2> _3 : temp) {
            result.add(_3.v2);
        }
        return result;
    }

    public static <T0, T1, T2, T3> List<T0> _4_project0(List<Tuple._4<T0, T1, T2, T3>> temp) {
        List<T0> result = new LinkedList();
        for (Tuple._4<T0, T1, T2, T3> _4 : temp) {
            result.add(_4.v0);
        }
        return result;
    }

    public static <T0, T1, T2, T3> List<T1> _4_project1(List<Tuple._4<T0, T1, T2, T3>> temp) {
        List<T1> result = new LinkedList();
        for (Tuple._4<T0, T1, T2, T3> _4 : temp) {
            result.add(_4.v1);
        }
        return result;
    }

    public static <T0, T1, T2, T3> List<T2> _4_project2(List<Tuple._4<T0, T1, T2, T3>> temp) {
        List<T2> result = new LinkedList();
        for (Tuple._4<T0, T1, T2, T3> _4 : temp) {
            result.add(_4.v2);
        }
        return result;
    }

    public static <T0, T1, T2, T3> List<T3> _4_project3(List<Tuple._4<T0, T1, T2, T3>> temp) {
        List<T3> result = new LinkedList();
        for (Tuple._4<T0, T1, T2, T3> _4 : temp) {
            result.add(_4.v3);
        }
        return result;
    }

    public static <T0, U> List<Tuple._1<U>> _1map0(List<Tuple._1<T0>> l, Tuple.Mapper<T0, U> mapper) {
        LinkedList<Tuple._1<U>> result = new LinkedList();
        for (Tuple._1<T0> _1 : l) {
            result.add(single(mapper.map(_1.v0)));
        }
        return result;
    }

    public static <T0, T1, U> List<Tuple._2<U, T1>> _2map0(List<Tuple._2<T0, T1>> l, Tuple.Mapper<T0, U> mapper) {
        LinkedList<Tuple._2<U, T1>> result = new LinkedList();
        for (Tuple._2<T0, T1> _2 : l) {
            result.add(pair(mapper.map(_2.v0), _2.v1));
        }
        return result;
    }

    public static <T0, T1, U> List<Tuple._2<T0, U>> _2map1(List<Tuple._2<T0, T1>> l, Tuple.Mapper<T1, U> mapper) {
        LinkedList<Tuple._2<T0, U>> result = new LinkedList();
        for (Tuple._2<T0, T1> _2 : l) {
            result.add(pair(_2.v0, mapper.map(_2.v1)));
        }
        return result;
    }

    public static <T0, T1, T2, U> List<Tuple._3<U, T1, T2>> _3map0(List<Tuple._3<T0, T1, T2>> l, Tuple.Mapper<T0, U> mapper) {
        LinkedList<Tuple._3<U, T1, T2>> result = new LinkedList();
        for (Tuple._3<T0, T1, T2> _3 : l) {
            result.add(triple(mapper.map(_3.v0), _3.v1, _3.v2));
        }
        return result;
    }

    public static <T0, T1, T2, U> List<Tuple._3<T0, U, T2>> _3map1(List<Tuple._3<T0, T1, T2>> l, Tuple.Mapper<T1, U> mapper) {
        LinkedList<Tuple._3<T0, U, T2>> result = new LinkedList();
        for (Tuple._3<T0, T1, T2> _3 : l) {
            result.add(triple(_3.v0, mapper.map(_3.v1), _3.v2));
        }
        return result;
    }

    public static <T0, T1, T2, U> List<Tuple._3<T0, T1, U>> _3map3(List<Tuple._3<T0, T1, T2>> l, Tuple.Mapper<T2, U> mapper) {
        LinkedList<Tuple._3<T0, T1, U>> result = new LinkedList();
        for (Tuple._3<T0, T1, T2> _3 : l) {
            result.add(triple(_3.v0, _3.v1, mapper.map(_3.v2)));
        }
        return result;
    }

    public static <T0, T1, T2, T3, U> List<Tuple._4<U, T1, T2, T3>> _4map0(List<Tuple._4<T0, T1, T2, T3>> l, Tuple.Mapper<T0, U> mapper) {
        LinkedList<Tuple._4<U, T1, T2, T3>> result = new LinkedList();
        for (Tuple._4<T0, T1, T2, T3> _4 : l) {
            result.add(quad(mapper.map(_4.v0), _4.v1, _4.v2, _4.v3));
        }
        return result;
    }

    public static <T0, T1, T2, T3, U> List<Tuple._4<T0, U, T2, T3>> _4map1(List<Tuple._4<T0, T1, T2, T3>> l, Tuple.Mapper<T1, U> mapper) {
        LinkedList<Tuple._4<T0, U, T2, T3>> result = new LinkedList();
        for (Tuple._4<T0, T1, T2, T3> _4 : l) {
            result.add(quad(_4.v0, mapper.map(_4.v1), _4.v2, _4.v3));
        }
        return result;
    }

    public static <T0, T1, T2, T3, U> List<Tuple._4<T0, T1, U, T3>> _4map3(List<Tuple._4<T0, T1, T2, T3>> l, Tuple.Mapper<T2, U> mapper) {
        LinkedList<Tuple._4<T0, T1, U, T3>> result = new LinkedList();
        for (Tuple._4<T0, T1, T2, T3> _4 : l) {
            result.add(quad(_4.v0, _4.v1, mapper.map(_4.v2), _4.v3));
        }
        return result;
    }

    public static <T0, T1, T2, T3, U> List<Tuple._4<T0, T1, T2, U>> _4map4(List<Tuple._4<T0, T1, T2, T3>> l, Tuple.Mapper<T3, U> mapper) {
        LinkedList<Tuple._4<T0, T1, T2, U>> result = new LinkedList();
        for (Tuple._4<T0, T1, T2, T3> _4 : l) {
            result.add(quad(_4.v0, _4.v1, _4.v2, mapper.map(_4.v3)));
        }
        return result;
    }

    public interface Mapper<T, U> {

        U map(T var1);
    }

    public static class _1<T0> {

        public final T0 v0;

        public _1(T0 v0) {
            this.v0 = v0;
        }
    }

    public static class _2<T0, T1> {

        public final T0 v0;

        public final T1 v1;

        public _2(T0 v0, T1 v1) {
            this.v0 = v0;
            this.v1 = v1;
        }
    }

    public static class _3<T0, T1, T2> {

        public final T0 v0;

        public final T1 v1;

        public final T2 v2;

        public _3(T0 v0, T1 v1, T2 v2) {
            this.v0 = v0;
            this.v1 = v1;
            this.v2 = v2;
        }
    }

    public static class _4<T0, T1, T2, T3> {

        public final T0 v0;

        public final T1 v1;

        public final T2 v2;

        public final T3 v3;

        public _4(T0 v0, T1 v1, T2 v2, T3 v3) {
            this.v0 = v0;
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
        }
    }
}