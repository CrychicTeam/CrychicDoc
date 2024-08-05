package ca.fxco.memoryleakfix.mixinextras.lib.apache.commons.builder;

import java.util.Comparator;

public class CompareToBuilder {

    private int comparison = 0;

    public CompareToBuilder append(Object lhs, Object rhs) {
        return this.append(lhs, rhs, null);
    }

    public CompareToBuilder append(Object lhs, Object rhs, Comparator<?> comparator) {
        if (this.comparison != 0) {
            return this;
        } else if (lhs == rhs) {
            return this;
        } else if (lhs == null) {
            this.comparison = -1;
            return this;
        } else if (rhs == null) {
            this.comparison = 1;
            return this;
        } else {
            if (lhs.getClass().isArray()) {
                if (lhs instanceof long[]) {
                    this.append((long[]) lhs, (long[]) rhs);
                } else if (lhs instanceof int[]) {
                    this.append((int[]) lhs, (int[]) rhs);
                } else if (lhs instanceof short[]) {
                    this.append((short[]) lhs, (short[]) rhs);
                } else if (lhs instanceof char[]) {
                    this.append((char[]) lhs, (char[]) rhs);
                } else if (lhs instanceof byte[]) {
                    this.append((byte[]) lhs, (byte[]) rhs);
                } else if (lhs instanceof double[]) {
                    this.append((double[]) lhs, (double[]) rhs);
                } else if (lhs instanceof float[]) {
                    this.append((float[]) lhs, (float[]) rhs);
                } else if (lhs instanceof boolean[]) {
                    this.append((boolean[]) lhs, (boolean[]) rhs);
                } else {
                    this.append((Object[]) lhs, (Object[]) rhs, comparator);
                }
            } else if (comparator == null) {
                Comparable<Object> comparable = (Comparable<Object>) lhs;
                this.comparison = comparable.compareTo(rhs);
            } else {
                this.comparison = comparator.compare(lhs, rhs);
            }
            return this;
        }
    }

    public CompareToBuilder append(long lhs, long rhs) {
        if (this.comparison != 0) {
            return this;
        } else {
            this.comparison = lhs < rhs ? -1 : (lhs > rhs ? 1 : 0);
            return this;
        }
    }

    public CompareToBuilder append(int lhs, int rhs) {
        if (this.comparison != 0) {
            return this;
        } else {
            this.comparison = lhs < rhs ? -1 : (lhs > rhs ? 1 : 0);
            return this;
        }
    }

    public CompareToBuilder append(short lhs, short rhs) {
        if (this.comparison != 0) {
            return this;
        } else {
            this.comparison = lhs < rhs ? -1 : (lhs > rhs ? 1 : 0);
            return this;
        }
    }

    public CompareToBuilder append(char lhs, char rhs) {
        if (this.comparison != 0) {
            return this;
        } else {
            this.comparison = lhs < rhs ? -1 : (lhs > rhs ? 1 : 0);
            return this;
        }
    }

    public CompareToBuilder append(byte lhs, byte rhs) {
        if (this.comparison != 0) {
            return this;
        } else {
            this.comparison = lhs < rhs ? -1 : (lhs > rhs ? 1 : 0);
            return this;
        }
    }

    public CompareToBuilder append(double lhs, double rhs) {
        if (this.comparison != 0) {
            return this;
        } else {
            this.comparison = Double.compare(lhs, rhs);
            return this;
        }
    }

    public CompareToBuilder append(float lhs, float rhs) {
        if (this.comparison != 0) {
            return this;
        } else {
            this.comparison = Float.compare(lhs, rhs);
            return this;
        }
    }

    public CompareToBuilder append(boolean lhs, boolean rhs) {
        if (this.comparison != 0) {
            return this;
        } else if (lhs == rhs) {
            return this;
        } else {
            if (!lhs) {
                this.comparison = -1;
            } else {
                this.comparison = 1;
            }
            return this;
        }
    }

    public CompareToBuilder append(Object[] lhs, Object[] rhs, Comparator<?> comparator) {
        if (this.comparison != 0) {
            return this;
        } else if (lhs == rhs) {
            return this;
        } else if (lhs == null) {
            this.comparison = -1;
            return this;
        } else if (rhs == null) {
            this.comparison = 1;
            return this;
        } else if (lhs.length != rhs.length) {
            this.comparison = lhs.length < rhs.length ? -1 : 1;
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
                this.append(lhs[i], rhs[i], comparator);
            }
            return this;
        }
    }

    public CompareToBuilder append(long[] lhs, long[] rhs) {
        if (this.comparison != 0) {
            return this;
        } else if (lhs == rhs) {
            return this;
        } else if (lhs == null) {
            this.comparison = -1;
            return this;
        } else if (rhs == null) {
            this.comparison = 1;
            return this;
        } else if (lhs.length != rhs.length) {
            this.comparison = lhs.length < rhs.length ? -1 : 1;
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
                this.append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public CompareToBuilder append(int[] lhs, int[] rhs) {
        if (this.comparison != 0) {
            return this;
        } else if (lhs == rhs) {
            return this;
        } else if (lhs == null) {
            this.comparison = -1;
            return this;
        } else if (rhs == null) {
            this.comparison = 1;
            return this;
        } else if (lhs.length != rhs.length) {
            this.comparison = lhs.length < rhs.length ? -1 : 1;
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
                this.append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public CompareToBuilder append(short[] lhs, short[] rhs) {
        if (this.comparison != 0) {
            return this;
        } else if (lhs == rhs) {
            return this;
        } else if (lhs == null) {
            this.comparison = -1;
            return this;
        } else if (rhs == null) {
            this.comparison = 1;
            return this;
        } else if (lhs.length != rhs.length) {
            this.comparison = lhs.length < rhs.length ? -1 : 1;
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
                this.append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public CompareToBuilder append(char[] lhs, char[] rhs) {
        if (this.comparison != 0) {
            return this;
        } else if (lhs == rhs) {
            return this;
        } else if (lhs == null) {
            this.comparison = -1;
            return this;
        } else if (rhs == null) {
            this.comparison = 1;
            return this;
        } else if (lhs.length != rhs.length) {
            this.comparison = lhs.length < rhs.length ? -1 : 1;
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
                this.append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public CompareToBuilder append(byte[] lhs, byte[] rhs) {
        if (this.comparison != 0) {
            return this;
        } else if (lhs == rhs) {
            return this;
        } else if (lhs == null) {
            this.comparison = -1;
            return this;
        } else if (rhs == null) {
            this.comparison = 1;
            return this;
        } else if (lhs.length != rhs.length) {
            this.comparison = lhs.length < rhs.length ? -1 : 1;
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
                this.append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public CompareToBuilder append(double[] lhs, double[] rhs) {
        if (this.comparison != 0) {
            return this;
        } else if (lhs == rhs) {
            return this;
        } else if (lhs == null) {
            this.comparison = -1;
            return this;
        } else if (rhs == null) {
            this.comparison = 1;
            return this;
        } else if (lhs.length != rhs.length) {
            this.comparison = lhs.length < rhs.length ? -1 : 1;
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
                this.append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public CompareToBuilder append(float[] lhs, float[] rhs) {
        if (this.comparison != 0) {
            return this;
        } else if (lhs == rhs) {
            return this;
        } else if (lhs == null) {
            this.comparison = -1;
            return this;
        } else if (rhs == null) {
            this.comparison = 1;
            return this;
        } else if (lhs.length != rhs.length) {
            this.comparison = lhs.length < rhs.length ? -1 : 1;
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
                this.append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public CompareToBuilder append(boolean[] lhs, boolean[] rhs) {
        if (this.comparison != 0) {
            return this;
        } else if (lhs == rhs) {
            return this;
        } else if (lhs == null) {
            this.comparison = -1;
            return this;
        } else if (rhs == null) {
            this.comparison = 1;
            return this;
        } else if (lhs.length != rhs.length) {
            this.comparison = lhs.length < rhs.length ? -1 : 1;
            return this;
        } else {
            for (int i = 0; i < lhs.length && this.comparison == 0; i++) {
                this.append(lhs[i], rhs[i]);
            }
            return this;
        }
    }

    public int toComparison() {
        return this.comparison;
    }
}