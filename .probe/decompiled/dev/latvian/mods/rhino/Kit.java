package dev.latvian.mods.rhino;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

public class Kit {

    public static Class<?> classOrNull(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException var2) {
        } catch (SecurityException var3) {
        } catch (LinkageError var4) {
        } catch (IllegalArgumentException var5) {
        }
        return null;
    }

    public static Class<?> classOrNull(ClassLoader loader, String className) {
        try {
            return loader.loadClass(className);
        } catch (ClassNotFoundException var3) {
        } catch (SecurityException var4) {
        } catch (LinkageError var5) {
        } catch (IllegalArgumentException var6) {
        }
        return null;
    }

    static Object newInstanceOrNull(Class<?> cl) {
        try {
            return cl.newInstance();
        } catch (SecurityException var2) {
        } catch (LinkageError var3) {
        } catch (InstantiationException var4) {
        } catch (IllegalAccessException var5) {
        }
        return null;
    }

    static boolean testIfCanLoadRhinoClasses(ClassLoader loader) {
        Class<?> testClass = Context.class;
        Class<?> x = classOrNull(loader, testClass.getName());
        return x == testClass;
    }

    public static int xDigitToInt(int c, int accumulator) {
        if (c <= 57) {
            c -= 48;
            if (0 > c) {
                return -1;
            }
        } else if (c <= 70) {
            if (65 > c) {
                return -1;
            }
            c -= 55;
        } else {
            if (c > 102 || 97 > c) {
                return -1;
            }
            c -= 87;
        }
        return accumulator << 4 | c;
    }

    public static Object addListener(Object bag, Object listener) {
        if (listener == null) {
            throw new IllegalArgumentException();
        } else if (listener instanceof Object[]) {
            throw new IllegalArgumentException();
        } else {
            if (bag == null) {
                bag = listener;
            } else if (bag instanceof Object[] array) {
                int L = array.length;
                if (L < 2) {
                    throw new IllegalArgumentException();
                }
                Object[] tmp = new Object[L + 1];
                System.arraycopy(array, 0, tmp, 0, L);
                tmp[L] = listener;
                bag = tmp;
            } else {
                bag = new Object[] { bag, listener };
            }
            return bag;
        }
    }

    public static Object removeListener(Object bag, Object listener) {
        if (listener == null) {
            throw new IllegalArgumentException();
        } else if (listener instanceof Object[]) {
            throw new IllegalArgumentException();
        } else {
            if (bag == listener) {
                bag = null;
            } else if (bag instanceof Object[] array) {
                int L = array.length;
                if (L < 2) {
                    throw new IllegalArgumentException();
                }
                if (L == 2) {
                    if (array[1] == listener) {
                        bag = array[0];
                    } else if (array[0] == listener) {
                        bag = array[1];
                    }
                } else {
                    int i = L;
                    do {
                        if (array[--i] == listener) {
                            Object[] tmp = new Object[L - 1];
                            System.arraycopy(array, 0, tmp, 0, i);
                            System.arraycopy(array, i + 1, tmp, i, L - (i + 1));
                            bag = tmp;
                            break;
                        }
                    } while (i != 0);
                }
            }
            return bag;
        }
    }

    public static Object getListener(Object bag, int index) {
        if (index == 0) {
            if (bag == null) {
                return null;
            } else if (bag instanceof Object[] array) {
                if (array.length < 2) {
                    throw new IllegalArgumentException();
                } else {
                    return array[0];
                }
            } else {
                return bag;
            }
        } else if (index == 1) {
            if (bag instanceof Object[] arrayx) {
                return arrayx[1];
            } else if (bag == null) {
                throw new IllegalArgumentException();
            } else {
                return null;
            }
        } else {
            Object[] arrayx = (Object[]) bag;
            int L = arrayx.length;
            if (L < 2) {
                throw new IllegalArgumentException();
            } else {
                return index == L ? null : arrayx[index];
            }
        }
    }

    static Object initHash(Map<Object, Object> h, Object key, Object initialValue) {
        synchronized (h) {
            Object current = h.get(key);
            if (current == null) {
                h.put(key, initialValue);
            } else {
                initialValue = current;
            }
            return initialValue;
        }
    }

    public static Object makeHashKeyFromPair(Object key1, Object key2) {
        if (key1 == null) {
            throw new IllegalArgumentException();
        } else if (key2 == null) {
            throw new IllegalArgumentException();
        } else {
            return new Kit.ComplexKey(key1, key2);
        }
    }

    public static String readReader(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String var5;
        try {
            char[] cbuf = new char[1024];
            StringBuilder sb = new StringBuilder(1024);
            int bytes_read;
            while ((bytes_read = in.read(cbuf, 0, 1024)) != -1) {
                sb.append(cbuf, 0, bytes_read);
            }
            var5 = sb.toString();
        } catch (Throwable var7) {
            try {
                in.close();
            } catch (Throwable var6) {
                var7.addSuppressed(var6);
            }
            throw var7;
        }
        in.close();
        return var5;
    }

    public static byte[] readStream(InputStream is, int initialBufferCapacity) throws IOException {
        if (initialBufferCapacity <= 0) {
            throw new IllegalArgumentException("Bad initialBufferCapacity: " + initialBufferCapacity);
        } else {
            byte[] buffer = new byte[initialBufferCapacity];
            int cursor = 0;
            while (true) {
                int n = is.read(buffer, cursor, buffer.length - cursor);
                if (n < 0) {
                    if (cursor != buffer.length) {
                        byte[] tmp = new byte[cursor];
                        System.arraycopy(buffer, 0, tmp, 0, cursor);
                        buffer = tmp;
                    }
                    return buffer;
                }
                cursor += n;
                if (cursor == buffer.length) {
                    byte[] tmp = new byte[buffer.length * 2];
                    System.arraycopy(buffer, 0, tmp, 0, cursor);
                    buffer = tmp;
                }
            }
        }
    }

    public static RuntimeException codeBug() throws RuntimeException {
        RuntimeException ex = new IllegalStateException("FAILED ASSERTION");
        ex.printStackTrace(System.err);
        throw ex;
    }

    public static RuntimeException codeBug(String msg) throws RuntimeException {
        msg = "FAILED ASSERTION: " + msg;
        RuntimeException ex = new IllegalStateException(msg);
        ex.printStackTrace(System.err);
        throw ex;
    }

    private static final class ComplexKey {

        private final Object key1;

        private final Object key2;

        private int hash;

        ComplexKey(Object key1, Object key2) {
            this.key1 = key1;
            this.key2 = key2;
        }

        public boolean equals(Object anotherObj) {
            return !(anotherObj instanceof Kit.ComplexKey another) ? false : this.key1.equals(another.key1) && this.key2.equals(another.key2);
        }

        public int hashCode() {
            if (this.hash == 0) {
                this.hash = this.key1.hashCode() ^ this.key2.hashCode();
            }
            return this.hash;
        }
    }
}