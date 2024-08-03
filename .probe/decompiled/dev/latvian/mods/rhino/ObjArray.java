package dev.latvian.mods.rhino;

import java.util.Objects;

public class ObjArray {

    private static final int FIELDS_STORE_SIZE = 5;

    private int size;

    private boolean sealed;

    private transient Object f0;

    private transient Object f1;

    private transient Object f2;

    private transient Object f3;

    private transient Object f4;

    private transient Object[] data;

    private static RuntimeException onInvalidIndex(int index, int upperBound) {
        String msg = index + " ∉ [0, " + upperBound + ")";
        throw new IndexOutOfBoundsException(msg);
    }

    private static RuntimeException onEmptyStackTopRead() {
        throw new RuntimeException("Empty stack");
    }

    private static RuntimeException onSeledMutation() {
        throw new IllegalStateException("Attempt to modify sealed array");
    }

    public final boolean isSealed() {
        return this.sealed;
    }

    public final void seal() {
        this.sealed = true;
    }

    public final boolean isEmpty() {
        return this.size == 0;
    }

    public final int size() {
        return this.size;
    }

    public final void setSize(int newSize) {
        if (newSize < 0) {
            throw new IllegalArgumentException();
        } else if (this.sealed) {
            throw onSeledMutation();
        } else {
            int N = this.size;
            if (newSize < N) {
                for (int i = newSize; i != N; i++) {
                    this.setImpl(i, null);
                }
            } else if (newSize > N && newSize > 5) {
                this.ensureCapacity(newSize);
            }
            this.size = newSize;
        }
    }

    public final Object get(int index) {
        if (0 <= index && index < this.size) {
            return this.getImpl(index);
        } else {
            throw onInvalidIndex(index, this.size);
        }
    }

    public final void set(int index, Object value) {
        if (0 > index || index >= this.size) {
            throw onInvalidIndex(index, this.size);
        } else if (this.sealed) {
            throw onSeledMutation();
        } else {
            this.setImpl(index, value);
        }
    }

    private Object getImpl(int index) {
        return switch(index) {
            case 0 ->
                this.f0;
            case 1 ->
                this.f1;
            case 2 ->
                this.f2;
            case 3 ->
                this.f3;
            case 4 ->
                this.f4;
            default ->
                this.data[index - 5];
        };
    }

    private void setImpl(int index, Object value) {
        switch(index) {
            case 0:
                this.f0 = value;
                break;
            case 1:
                this.f1 = value;
                break;
            case 2:
                this.f2 = value;
                break;
            case 3:
                this.f3 = value;
                break;
            case 4:
                this.f4 = value;
                break;
            default:
                this.data[index - 5] = value;
        }
    }

    public int indexOf(Object obj) {
        int N = this.size;
        for (int i = 0; i != N; i++) {
            Object current = this.getImpl(i);
            if (Objects.equals(current, obj)) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(Object obj) {
        int i = this.size;
        while (i != 0) {
            Object current = this.getImpl(--i);
            if (Objects.equals(current, obj)) {
                return i;
            }
        }
        return -1;
    }

    public final Object peek() {
        int N = this.size;
        if (N == 0) {
            throw onEmptyStackTopRead();
        } else {
            return this.getImpl(N - 1);
        }
    }

    public final Object pop() {
        if (this.sealed) {
            throw onSeledMutation();
        } else {
            int N = this.size;
            N--;
            Object top;
            switch(N) {
                case -1:
                    throw onEmptyStackTopRead();
                case 0:
                    top = this.f0;
                    this.f0 = null;
                    break;
                case 1:
                    top = this.f1;
                    this.f1 = null;
                    break;
                case 2:
                    top = this.f2;
                    this.f2 = null;
                    break;
                case 3:
                    top = this.f3;
                    this.f3 = null;
                    break;
                case 4:
                    top = this.f4;
                    this.f4 = null;
                    break;
                default:
                    top = this.data[N - 5];
                    this.data[N - 5] = null;
            }
            this.size = N;
            return top;
        }
    }

    public final void push(Object value) {
        this.add(value);
    }

    public final void add(Object value) {
        if (this.sealed) {
            throw onSeledMutation();
        } else {
            int N = this.size;
            if (N >= 5) {
                this.ensureCapacity(N + 1);
            }
            this.size = N + 1;
            this.setImpl(N, value);
        }
    }

    public final void add(int index, Object value) {
        int N = this.size;
        if (0 > index || index > N) {
            throw onInvalidIndex(index, N + 1);
        } else if (this.sealed) {
            throw onSeledMutation();
        } else {
            switch(index) {
                case 0:
                    if (N == 0) {
                        this.f0 = value;
                        break;
                    } else {
                        Object tmp = this.f0;
                        this.f0 = value;
                        value = tmp;
                    }
                case 1:
                    if (N == 1) {
                        this.f1 = value;
                        break;
                    } else {
                        Object tmp = this.f1;
                        this.f1 = value;
                        value = tmp;
                    }
                case 2:
                    if (N == 2) {
                        this.f2 = value;
                        break;
                    } else {
                        Object tmp = this.f2;
                        this.f2 = value;
                        value = tmp;
                    }
                case 3:
                    if (N == 3) {
                        this.f3 = value;
                        break;
                    } else {
                        Object tmp = this.f3;
                        this.f3 = value;
                        value = tmp;
                    }
                case 4:
                    if (N == 4) {
                        this.f4 = value;
                        break;
                    } else {
                        Object tmp = this.f4;
                        this.f4 = value;
                        value = tmp;
                        index = 5;
                    }
                default:
                    this.ensureCapacity(N + 1);
                    if (index != N) {
                        System.arraycopy(this.data, index - 5, this.data, index - 5 + 1, N - index);
                    }
                    this.data[index - 5] = value;
            }
            this.size = N + 1;
        }
    }

    public final void remove(int index) {
        int N = this.size;
        if (0 > index || index >= N) {
            throw onInvalidIndex(index, N);
        } else if (this.sealed) {
            throw onSeledMutation();
        } else {
            N--;
            switch(index) {
                case 0:
                    if (N == 0) {
                        this.f0 = null;
                        break;
                    } else {
                        this.f0 = this.f1;
                    }
                case 1:
                    if (N == 1) {
                        this.f1 = null;
                        break;
                    } else {
                        this.f1 = this.f2;
                    }
                case 2:
                    if (N == 2) {
                        this.f2 = null;
                        break;
                    } else {
                        this.f2 = this.f3;
                    }
                case 3:
                    if (N == 3) {
                        this.f3 = null;
                        break;
                    } else {
                        this.f3 = this.f4;
                    }
                case 4:
                    if (N == 4) {
                        this.f4 = null;
                        break;
                    } else {
                        this.f4 = this.data[0];
                        index = 5;
                    }
                default:
                    if (index != N) {
                        System.arraycopy(this.data, index - 5 + 1, this.data, index - 5, N - index);
                    }
                    this.data[N - 5] = null;
            }
            this.size = N;
        }
    }

    public final void clear() {
        if (this.sealed) {
            throw onSeledMutation();
        } else {
            int N = this.size;
            for (int i = 0; i != N; i++) {
                this.setImpl(i, null);
            }
            this.size = 0;
        }
    }

    public final Object[] toArray() {
        Object[] array = new Object[this.size];
        this.toArray(array, 0);
        return array;
    }

    public final void toArray(Object[] array) {
        this.toArray(array, 0);
    }

    public final void toArray(Object[] array, int offset) {
        int N = this.size;
        switch(N) {
            default:
                System.arraycopy(this.data, 0, array, offset + 5, N - 5);
            case 5:
                array[offset + 4] = this.f4;
            case 4:
                array[offset + 3] = this.f3;
            case 3:
                array[offset + 2] = this.f2;
            case 2:
                array[offset + 1] = this.f1;
            case 1:
                array[offset] = this.f0;
            case 0:
        }
    }

    private void ensureCapacity(int minimalCapacity) {
        int required = minimalCapacity - 5;
        if (required <= 0) {
            throw new IllegalArgumentException();
        } else {
            if (this.data == null) {
                int alloc = 10;
                if (alloc < required) {
                    alloc = required;
                }
                this.data = new Object[alloc];
            } else {
                int alloc = this.data.length;
                if (alloc < required) {
                    if (alloc <= 5) {
                        alloc = 10;
                    } else {
                        alloc *= 2;
                    }
                    if (alloc < required) {
                        alloc = required;
                    }
                    Object[] tmp = new Object[alloc];
                    if (this.size > 5) {
                        System.arraycopy(this.data, 0, tmp, 0, this.size - 5);
                    }
                    this.data = tmp;
                }
            }
        }
    }
}