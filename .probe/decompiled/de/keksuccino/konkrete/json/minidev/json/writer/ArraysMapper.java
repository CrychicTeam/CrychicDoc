package de.keksuccino.konkrete.json.minidev.json.writer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ArraysMapper<T> extends JsonReaderI<T> {

    public static JsonReaderI<int[]> MAPPER_PRIM_INT = new ArraysMapper<int[]>(null) {

        public int[] convert(Object current) {
            int p = 0;
            int[] r = new int[((List) current).size()];
            for (Object e : (List) current) {
                r[p++] = ((Number) e).intValue();
            }
            return r;
        }
    };

    public static JsonReaderI<Integer[]> MAPPER_INT = new ArraysMapper<Integer[]>(null) {

        public Integer[] convert(Object current) {
            int p = 0;
            Integer[] r = new Integer[((List) current).size()];
            for (Object e : (List) current) {
                if (e != null) {
                    if (e instanceof Integer) {
                        r[p] = (Integer) e;
                    } else {
                        r[p] = ((Number) e).intValue();
                    }
                    p++;
                }
            }
            return r;
        }
    };

    public static JsonReaderI<short[]> MAPPER_PRIM_SHORT = new ArraysMapper<short[]>(null) {

        public short[] convert(Object current) {
            int p = 0;
            short[] r = new short[((List) current).size()];
            for (Object e : (List) current) {
                r[p++] = ((Number) e).shortValue();
            }
            return r;
        }
    };

    public static JsonReaderI<Short[]> MAPPER_SHORT = new ArraysMapper<Short[]>(null) {

        public Short[] convert(Object current) {
            int p = 0;
            Short[] r = new Short[((List) current).size()];
            for (Object e : (List) current) {
                if (e != null) {
                    if (e instanceof Short) {
                        r[p] = (Short) e;
                    } else {
                        r[p] = ((Number) e).shortValue();
                    }
                    p++;
                }
            }
            return r;
        }
    };

    public static JsonReaderI<byte[]> MAPPER_PRIM_BYTE = new ArraysMapper<byte[]>(null) {

        public byte[] convert(Object current) {
            int p = 0;
            byte[] r = new byte[((List) current).size()];
            for (Object e : (List) current) {
                r[p++] = ((Number) e).byteValue();
            }
            return r;
        }
    };

    public static JsonReaderI<Byte[]> MAPPER_BYTE = new ArraysMapper<Byte[]>(null) {

        public Byte[] convert(Object current) {
            int p = 0;
            Byte[] r = new Byte[((List) current).size()];
            for (Object e : (List) current) {
                if (e != null) {
                    if (e instanceof Byte) {
                        r[p] = (Byte) e;
                    } else {
                        r[p] = ((Number) e).byteValue();
                    }
                    p++;
                }
            }
            return r;
        }
    };

    public static JsonReaderI<char[]> MAPPER_PRIM_CHAR = new ArraysMapper<char[]>(null) {

        public char[] convert(Object current) {
            int p = 0;
            char[] r = new char[((List) current).size()];
            for (Object e : (List) current) {
                r[p++] = e.toString().charAt(0);
            }
            return r;
        }
    };

    public static JsonReaderI<Character[]> MAPPER_CHAR = new ArraysMapper<Character[]>(null) {

        public Character[] convert(Object current) {
            int p = 0;
            Character[] r = new Character[((List) current).size()];
            for (Object e : (List) current) {
                if (e != null) {
                    r[p] = e.toString().charAt(0);
                    p++;
                }
            }
            return r;
        }
    };

    public static JsonReaderI<long[]> MAPPER_PRIM_LONG = new ArraysMapper<long[]>(null) {

        public long[] convert(Object current) {
            int p = 0;
            long[] r = new long[((List) current).size()];
            for (Object e : (List) current) {
                r[p++] = (long) ((Number) e).intValue();
            }
            return r;
        }
    };

    public static JsonReaderI<Long[]> MAPPER_LONG = new ArraysMapper<Long[]>(null) {

        public Long[] convert(Object current) {
            int p = 0;
            Long[] r = new Long[((List) current).size()];
            for (Object e : (List) current) {
                if (e != null) {
                    if (e instanceof Float) {
                        r[p] = (Long) e;
                    } else {
                        r[p] = ((Number) e).longValue();
                    }
                    p++;
                }
            }
            return r;
        }
    };

    public static JsonReaderI<float[]> MAPPER_PRIM_FLOAT = new ArraysMapper<float[]>(null) {

        public float[] convert(Object current) {
            int p = 0;
            float[] r = new float[((List) current).size()];
            for (Object e : (List) current) {
                r[p++] = ((Number) e).floatValue();
            }
            return r;
        }
    };

    public static JsonReaderI<Float[]> MAPPER_FLOAT = new ArraysMapper<Float[]>(null) {

        public Float[] convert(Object current) {
            int p = 0;
            Float[] r = new Float[((List) current).size()];
            for (Object e : (List) current) {
                if (e != null) {
                    if (e instanceof Float) {
                        r[p] = (Float) e;
                    } else {
                        r[p] = ((Number) e).floatValue();
                    }
                    p++;
                }
            }
            return r;
        }
    };

    public static JsonReaderI<double[]> MAPPER_PRIM_DOUBLE = new ArraysMapper<double[]>(null) {

        public double[] convert(Object current) {
            int p = 0;
            double[] r = new double[((List) current).size()];
            for (Object e : (List) current) {
                r[p++] = ((Number) e).doubleValue();
            }
            return r;
        }
    };

    public static JsonReaderI<Double[]> MAPPER_DOUBLE = new ArraysMapper<Double[]>(null) {

        public Double[] convert(Object current) {
            int p = 0;
            Double[] r = new Double[((List) current).size()];
            for (Object e : (List) current) {
                if (e != null) {
                    if (e instanceof Double) {
                        r[p] = (Double) e;
                    } else {
                        r[p] = ((Number) e).doubleValue();
                    }
                    p++;
                }
            }
            return r;
        }
    };

    public static JsonReaderI<boolean[]> MAPPER_PRIM_BOOL = new ArraysMapper<boolean[]>(null) {

        public boolean[] convert(Object current) {
            int p = 0;
            boolean[] r = new boolean[((List) current).size()];
            for (Object e : (List) current) {
                r[p++] = (Boolean) e;
            }
            return r;
        }
    };

    public static JsonReaderI<Boolean[]> MAPPER_BOOL = new ArraysMapper<Boolean[]>(null) {

        public Boolean[] convert(Object current) {
            int p = 0;
            Boolean[] r = new Boolean[((List) current).size()];
            for (Object e : (List) current) {
                if (e != null) {
                    if (e instanceof Boolean) {
                        r[p] = (Boolean) e;
                    } else {
                        if (!(e instanceof Number)) {
                            throw new RuntimeException("can not convert " + e + " toBoolean");
                        }
                        r[p] = ((Number) e).intValue() != 0;
                    }
                    p++;
                }
            }
            return r;
        }
    };

    public ArraysMapper(JsonReader base) {
        super(base);
    }

    @Override
    public Object createArray() {
        return new ArrayList();
    }

    @Override
    public void addValue(Object current, Object value) {
        ((List) current).add(value);
    }

    @Override
    public T convert(Object current) {
        return (T) current;
    }

    public static class GenericMapper<T> extends ArraysMapper<T> {

        final Class<?> componentType;

        JsonReaderI<?> subMapper;

        public GenericMapper(JsonReader base, Class<T> type) {
            super(base);
            this.componentType = type.getComponentType();
        }

        @Override
        public T convert(Object current) {
            int p = 0;
            Object[] r = (Object[]) Array.newInstance(this.componentType, ((List) current).size());
            for (Object e : (List) current) {
                r[p++] = e;
            }
            return (T) r;
        }

        @Override
        public JsonReaderI<?> startArray(String key) {
            if (this.subMapper == null) {
                this.subMapper = this.base.getMapper((Class<T>) this.componentType);
            }
            return this.subMapper;
        }

        @Override
        public JsonReaderI<?> startObject(String key) {
            if (this.subMapper == null) {
                this.subMapper = this.base.getMapper((Class<T>) this.componentType);
            }
            return this.subMapper;
        }
    }
}