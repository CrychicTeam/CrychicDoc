package icyllis.modernui.util;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import java.util.Comparator;
import org.jetbrains.annotations.Contract;

public final class AlgorithmUtils {

    public static int gcd(int a, int b) {
        assert a >= 0 && b >= 0;
        if (a == 0) {
            return b;
        } else if (b == 0) {
            return a;
        } else {
            int aTwos = Integer.numberOfTrailingZeros(a);
            a >>= aTwos;
            int bTwos = Integer.numberOfTrailingZeros(b);
            b >>= bTwos;
            while (a != b) {
                int delta = a - b;
                int minDeltaOrZero = delta & delta >> 31;
                a = delta - minDeltaOrZero - minDeltaOrZero;
                b += minDeltaOrZero;
                a >>= Integer.numberOfTrailingZeros(a);
            }
            return a << Math.min(aTwos, bTwos);
        }
    }

    public static long gcd(long a, long b) {
        assert a >= 0L && b >= 0L;
        if (a == 0L) {
            return b;
        } else if (b == 0L) {
            return a;
        } else {
            int aTwos = Long.numberOfTrailingZeros(a);
            a >>= aTwos;
            int bTwos = Long.numberOfTrailingZeros(b);
            b >>= bTwos;
            while (a != b) {
                long delta = a - b;
                long minDeltaOrZero = delta & delta >> 63;
                a = delta - minDeltaOrZero - minDeltaOrZero;
                b += minDeltaOrZero;
                a >>= Long.numberOfTrailingZeros(a);
            }
            return a << Math.min(aTwos, bTwos);
        }
    }

    public static int quickPow(int a, int b) {
        assert b >= 0;
        int res = 1;
        while (b != 0) {
            if ((b & 1) != 0) {
                res *= a;
            }
            b >>= 1;
            a *= a;
        }
        return res;
    }

    public static long quickPow(long a, long b) {
        assert b >= 0L;
        long res = 1L;
        while (b != 0L) {
            if ((b & 1L) != 0L) {
                res *= a;
            }
            b >>= 1;
            a *= a;
        }
        return res;
    }

    public static int quickModPow(int a, int b, int m) {
        assert b >= 0 && m > 0;
        int res = 1;
        while (b != 0) {
            if ((b & 1) != 0) {
                res = res * a % m;
            }
            b >>= 1;
            a = a * a % m;
        }
        return res;
    }

    public static long quickModPow(long a, long b, int m) {
        assert b >= 0L && m > 0;
        long res = 1L;
        while (b != 0L) {
            if ((b & 1L) != 0L) {
                res = res * a % (long) m;
            }
            b >>= 1;
            a = a * a % (long) m;
        }
        return res;
    }

    public static int lower(@NonNull int[] a, int value) {
        return lower(a, 0, a.length, value);
    }

    @Contract(pure = true)
    public static int lower(@NonNull int[] a, int first, int last, int value) {
        assert (first | last - first | a.length - last) >= 0;
        int low = first;
        int high = last - 1;
        while (low <= high) {
            int mid = low + high >>> 1;
            if (a[mid] < value) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return high;
    }

    @Contract(pure = true)
    public static int lower(@NonNull long[] a, long value) {
        return lower(a, 0, a.length, value);
    }

    @Contract(pure = true)
    public static int lower(@NonNull long[] a, int first, int last, long value) {
        assert (first | last - first | a.length - last) >= 0;
        int low = first;
        int high = last - 1;
        while (low <= high) {
            int mid = low + high >>> 1;
            if (a[mid] < value) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return high;
    }

    public static <T> int lower(@NonNull T[] a, T value, @Nullable Comparator<? super T> c) {
        return lower(a, 0, a.length, value, c);
    }

    public static <T> int lower(@NonNull T[] a, int first, int last, T value, @Nullable Comparator<? super T> c) {
        assert (first | last - first | a.length - last) >= 0;
        if (c == null) {
            c = Comparator.naturalOrder();
        }
        int low = first;
        int high = last - 1;
        while (low <= high) {
            int mid = low + high >>> 1;
            if (c.compare(a[mid], value) < 0) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return high;
    }

    public static int floor(@NonNull int[] a, int value) {
        return floor(a, 0, a.length, value);
    }

    @Contract(pure = true)
    public static int floor(@NonNull int[] a, int first, int last, int value) {
        assert (first | last - first | a.length - last) >= 0;
        int low = first;
        int high = last - 1;
        while (low <= high) {
            int mid = low + high >>> 1;
            if (a[mid] > value) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return high;
    }

    @Contract(pure = true)
    public static int floor(@NonNull long[] a, long value) {
        return floor(a, 0, a.length, value);
    }

    @Contract(pure = true)
    public static int floor(@NonNull long[] a, int first, int last, long value) {
        assert (first | last - first | a.length - last) >= 0;
        int low = first;
        int high = last - 1;
        while (low <= high) {
            int mid = low + high >>> 1;
            if (a[mid] > value) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return high;
    }

    public static <T> int floor(@NonNull T[] a, T value, @Nullable Comparator<? super T> c) {
        return floor(a, 0, a.length, value, c);
    }

    public static <T> int floor(@NonNull T[] a, int first, int last, T value, @Nullable Comparator<? super T> c) {
        assert (first | last - first | a.length - last) >= 0;
        if (c == null) {
            c = Comparator.naturalOrder();
        }
        int low = first;
        int high = last - 1;
        while (low <= high) {
            int mid = low + high >>> 1;
            if (c.compare(a[mid], value) > 0) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return high;
    }

    public static int ceil(@NonNull int[] a, int value) {
        return ceil(a, 0, a.length, value);
    }

    @Contract(pure = true)
    public static int ceil(@NonNull int[] a, int first, int last, int value) {
        assert (first | last - first | a.length - last) >= 0;
        int low = first;
        int high = last - 1;
        while (low <= high) {
            int mid = low + high >>> 1;
            if (a[mid] < value) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }

    @Contract(pure = true)
    public static int ceil(@NonNull long[] a, long value) {
        return ceil(a, 0, a.length, value);
    }

    @Contract(pure = true)
    public static int ceil(@NonNull long[] a, int first, int last, long value) {
        assert (first | last - first | a.length - last) >= 0;
        int low = first;
        int high = last - 1;
        while (low <= high) {
            int mid = low + high >>> 1;
            if (a[mid] < value) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }

    public static <T> int ceil(@NonNull T[] a, T value, @Nullable Comparator<? super T> c) {
        return ceil(a, 0, a.length, value, c);
    }

    public static <T> int ceil(@NonNull T[] a, int first, int last, T value, @Nullable Comparator<? super T> c) {
        assert (first | last - first | a.length - last) >= 0;
        if (c == null) {
            c = Comparator.naturalOrder();
        }
        int low = first;
        int high = last - 1;
        while (low <= high) {
            int mid = low + high >>> 1;
            if (c.compare(a[mid], value) < 0) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    public static int higher(@NonNull int[] a, int value) {
        return higher(a, 0, a.length, value);
    }

    @Contract(pure = true)
    public static int higher(@NonNull int[] a, int first, int last, int value) {
        assert (first | last - first | a.length - last) >= 0;
        int low = first;
        int high = last - 1;
        while (low <= high) {
            int mid = low + high >>> 1;
            if (a[mid] > value) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    public static int higher(@NonNull long[] a, long value) {
        return higher(a, 0, a.length, value);
    }

    public static int higher(@NonNull long[] a, int first, int last, long value) {
        assert (first | last - first | a.length - last) >= 0;
        int low = first;
        int high = last - 1;
        while (low <= high) {
            int mid = low + high >>> 1;
            if (a[mid] > value) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    public static <T> int higher(@NonNull T[] a, T value, @Nullable Comparator<? super T> c) {
        return higher(a, 0, a.length, value, c);
    }

    public static <T> int higher(@NonNull T[] a, int first, int last, T value, @Nullable Comparator<? super T> c) {
        assert (first | last - first | a.length - last) >= 0;
        if (c == null) {
            c = Comparator.naturalOrder();
        }
        int low = first;
        int high = last - 1;
        while (low <= high) {
            int mid = low + high >>> 1;
            if (c.compare(a[mid], value) > 0) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    @Contract(pure = true)
    public static int lengthOfLIS(@NonNull int[] a, int n) {
        assert n <= a.length;
        if (n <= 1) {
            return n;
        } else {
            int[] tail = new int[n];
            int length = 1;
            tail[0] = a[0];
            for (int i = 1; i < n; i++) {
                int v = a[i];
                int pos = higher(tail, 0, length, v);
                if (pos == length) {
                    tail[length++] = v;
                } else {
                    tail[pos] = v;
                }
            }
            return length;
        }
    }

    @Contract(pure = true)
    public static int lengthOfLIS(@NonNull int[] a, int n, boolean strict) {
        if (!strict) {
            return lengthOfLIS(a, n);
        } else {
            assert n <= a.length;
            if (n <= 1) {
                return n;
            } else {
                int[] tail = new int[n];
                int length = 1;
                tail[0] = a[0];
                for (int i = 1; i < n; i++) {
                    int v = a[i];
                    int pos = ceil(tail, 0, length, v);
                    if (pos == length) {
                        tail[length++] = v;
                    } else {
                        tail[pos] = v;
                    }
                }
                return length;
            }
        }
    }

    @Contract(pure = true)
    public static int lengthOfLIS(@NonNull long[] a, int n) {
        assert n <= a.length;
        if (n <= 1) {
            return n;
        } else {
            long[] tail = new long[n];
            int length = 1;
            tail[0] = a[0];
            for (int i = 1; i < n; i++) {
                long v = a[i];
                int pos = higher(tail, 0, length, v);
                if (pos == length) {
                    tail[length++] = v;
                } else {
                    tail[pos] = v;
                }
            }
            return length;
        }
    }

    @Contract(pure = true)
    public static int lengthOfLIS(@NonNull long[] a, int n, boolean strict) {
        if (!strict) {
            return lengthOfLIS(a, n);
        } else {
            assert n <= a.length;
            if (n <= 1) {
                return n;
            } else {
                long[] tail = new long[n];
                int length = 1;
                tail[0] = a[0];
                for (int i = 1; i < n; i++) {
                    long v = a[i];
                    int pos = ceil(tail, 0, length, v);
                    if (pos == length) {
                        tail[length++] = v;
                    } else {
                        tail[pos] = v;
                    }
                }
                return length;
            }
        }
    }

    public static double averageStable(@NonNull double[] a) {
        double r = 0.0;
        int i = 0;
        int e = a.length;
        while (i < e) {
            double var10001 = a[i] - r;
            i++;
            r += var10001 / (double) i;
        }
        return r;
    }

    public static double averageStable(@NonNull double[] a, int start, int limit) {
        double r = 0.0;
        double t = 0.0;
        int i = start;
        while (i < limit) {
            r += (a[i++] - r) / ++t;
        }
        return r;
    }

    private AlgorithmUtils() {
    }
}