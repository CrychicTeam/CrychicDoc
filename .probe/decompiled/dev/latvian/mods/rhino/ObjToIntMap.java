package dev.latvian.mods.rhino;

public class ObjToIntMap {

    private static final int A = -1640531527;

    private static final Object DELETED = new Object();

    private static final boolean check = false;

    private transient Object[] keys;

    private transient int[] values;

    private int power;

    private int keyCount;

    private transient int occupiedCount;

    private static int tableLookupStep(int fraction, int mask, int power) {
        int shift = 32 - 2 * power;
        return shift >= 0 ? fraction >>> shift & mask | 1 : fraction & mask >>> -shift | 1;
    }

    public ObjToIntMap() {
        this(4);
    }

    public ObjToIntMap(int keyCountHint) {
        if (keyCountHint < 0) {
            Kit.codeBug();
        }
        int minimalCapacity = keyCountHint * 4 / 3;
        int i = 2;
        while (1 << i < minimalCapacity) {
            i++;
        }
        this.power = i;
    }

    public boolean isEmpty() {
        return this.keyCount == 0;
    }

    public int size() {
        return this.keyCount;
    }

    public boolean has(Object key) {
        if (key == null) {
            key = UniqueTag.NULL_VALUE;
        }
        return 0 <= this.findIndex(key);
    }

    public int get(Object key, int defaultValue) {
        if (key == null) {
            key = UniqueTag.NULL_VALUE;
        }
        int index = this.findIndex(key);
        return 0 <= index ? this.values[index] : defaultValue;
    }

    public int getExisting(Object key) {
        if (key == null) {
            key = UniqueTag.NULL_VALUE;
        }
        int index = this.findIndex(key);
        if (0 <= index) {
            return this.values[index];
        } else {
            Kit.codeBug();
            return 0;
        }
    }

    public void put(Object key, int value) {
        if (key == null) {
            key = UniqueTag.NULL_VALUE;
        }
        int index = this.ensureIndex(key);
        this.values[index] = value;
    }

    public Object intern(Object keyArg) {
        boolean nullKey = false;
        if (keyArg == null) {
            nullKey = true;
            keyArg = UniqueTag.NULL_VALUE;
        }
        int index = this.ensureIndex(keyArg);
        this.values[index] = 0;
        return nullKey ? null : this.keys[index];
    }

    public void remove(Object key) {
        if (key == null) {
            key = UniqueTag.NULL_VALUE;
        }
        int index = this.findIndex(key);
        if (0 <= index) {
            this.keys[index] = DELETED;
            this.keyCount--;
        }
    }

    public void clear() {
        for (int i = this.keys.length; i != 0; this.keys[i] = null) {
            i--;
        }
        this.keyCount = 0;
        this.occupiedCount = 0;
    }

    public ObjToIntMap.Iterator newIterator() {
        return new ObjToIntMap.Iterator(this);
    }

    final void initIterator(ObjToIntMap.Iterator i) {
        i.init(this.keys, this.values, this.keyCount);
    }

    public Object[] getKeys() {
        Object[] array = new Object[this.keyCount];
        this.getKeys(array, 0);
        return array;
    }

    public void getKeys(Object[] array, int offset) {
        int count = this.keyCount;
        for (int i = 0; count != 0; i++) {
            Object key = this.keys[i];
            if (key != null && key != DELETED) {
                if (key == UniqueTag.NULL_VALUE) {
                    key = null;
                }
                array[offset] = key;
                offset++;
                count--;
            }
        }
    }

    private int findIndex(Object key) {
        if (this.keys != null) {
            int hash = key.hashCode();
            int fraction = hash * -1640531527;
            int index = fraction >>> 32 - this.power;
            Object test = this.keys[index];
            if (test != null) {
                int N = 1 << this.power;
                if (test == key || this.values[N + index] == hash && test.equals(key)) {
                    return index;
                }
                int mask = N - 1;
                int step = tableLookupStep(fraction, mask, this.power);
                int n = 0;
                while (true) {
                    index = index + step & mask;
                    test = this.keys[index];
                    if (test == null) {
                        break;
                    }
                    if (test == key || this.values[N + index] == hash && test.equals(key)) {
                        return index;
                    }
                }
            }
        }
        return -1;
    }

    private int insertNewKey(Object key, int hash) {
        int fraction = hash * -1640531527;
        int index = fraction >>> 32 - this.power;
        int N = 1 << this.power;
        if (this.keys[index] != null) {
            int mask = N - 1;
            int step = tableLookupStep(fraction, mask, this.power);
            do {
                index = index + step & mask;
            } while (this.keys[index] != null);
        }
        this.keys[index] = key;
        this.values[N + index] = hash;
        this.occupiedCount++;
        this.keyCount++;
        return index;
    }

    private void rehashTable() {
        if (this.keys == null) {
            int N = 1 << this.power;
            this.keys = new Object[N];
            this.values = new int[2 * N];
        } else {
            if (this.keyCount * 2 >= this.occupiedCount) {
                this.power++;
            }
            int N = 1 << this.power;
            Object[] oldKeys = this.keys;
            int[] oldValues = this.values;
            int oldN = oldKeys.length;
            this.keys = new Object[N];
            this.values = new int[2 * N];
            int remaining = this.keyCount;
            this.occupiedCount = this.keyCount = 0;
            for (int i = 0; remaining != 0; i++) {
                Object key = oldKeys[i];
                if (key != null && key != DELETED) {
                    int keyHash = oldValues[oldN + i];
                    int index = this.insertNewKey(key, keyHash);
                    this.values[index] = oldValues[i];
                    remaining--;
                }
            }
        }
    }

    private int ensureIndex(Object key) {
        int hash = key.hashCode();
        int index = -1;
        int firstDeleted = -1;
        if (this.keys != null) {
            int fraction = hash * -1640531527;
            index = fraction >>> 32 - this.power;
            Object test = this.keys[index];
            if (test != null) {
                int N = 1 << this.power;
                if (test == key || this.values[N + index] == hash && test.equals(key)) {
                    return index;
                }
                if (test == DELETED) {
                    firstDeleted = index;
                }
                int mask = N - 1;
                int step = tableLookupStep(fraction, mask, this.power);
                int n = 0;
                while (true) {
                    index = index + step & mask;
                    test = this.keys[index];
                    if (test == null) {
                        break;
                    }
                    if (test == key || this.values[N + index] == hash && test.equals(key)) {
                        return index;
                    }
                    if (test == DELETED && firstDeleted < 0) {
                        firstDeleted = index;
                    }
                }
            }
        }
        if (firstDeleted >= 0) {
            index = firstDeleted;
        } else {
            if (this.keys == null || this.occupiedCount * 4 >= (1 << this.power) * 3) {
                this.rehashTable();
                return this.insertNewKey(key, hash);
            }
            this.occupiedCount++;
        }
        this.keys[index] = key;
        this.values[(1 << this.power) + index] = hash;
        this.keyCount++;
        return index;
    }

    public static class Iterator {

        ObjToIntMap master;

        private int cursor;

        private int remaining;

        private Object[] keys;

        private int[] values;

        Iterator(ObjToIntMap master) {
            this.master = master;
        }

        final void init(Object[] keys, int[] values, int keyCount) {
            this.keys = keys;
            this.values = values;
            this.cursor = -1;
            this.remaining = keyCount;
        }

        public void start() {
            this.master.initIterator(this);
            this.next();
        }

        public boolean done() {
            return this.remaining < 0;
        }

        public void next() {
            if (this.remaining == -1) {
                Kit.codeBug();
            }
            if (this.remaining == 0) {
                this.remaining = -1;
                this.cursor = -1;
            } else {
                this.cursor++;
                while (true) {
                    Object key = this.keys[this.cursor];
                    if (key != null && key != ObjToIntMap.DELETED) {
                        this.remaining--;
                        break;
                    }
                    this.cursor++;
                }
            }
        }

        public Object getKey() {
            Object key = this.keys[this.cursor];
            if (key == UniqueTag.NULL_VALUE) {
                key = null;
            }
            return key;
        }

        public int getValue() {
            return this.values[this.cursor];
        }

        public void setValue(int value) {
            this.values[this.cursor] = value;
        }
    }
}