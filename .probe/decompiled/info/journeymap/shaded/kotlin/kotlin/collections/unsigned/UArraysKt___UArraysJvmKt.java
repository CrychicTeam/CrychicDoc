package info.journeymap.shaded.kotlin.kotlin.collections.unsigned;

import info.journeymap.shaded.kotlin.kotlin.ExperimentalUnsignedTypes;
import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.OverloadResolutionByLambdaReturnType;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.UByte;
import info.journeymap.shaded.kotlin.kotlin.UByteArray;
import info.journeymap.shaded.kotlin.kotlin.UInt;
import info.journeymap.shaded.kotlin.kotlin.UIntArray;
import info.journeymap.shaded.kotlin.kotlin.ULong;
import info.journeymap.shaded.kotlin.kotlin.ULongArray;
import info.journeymap.shaded.kotlin.kotlin.UShort;
import info.journeymap.shaded.kotlin.kotlin.UShortArray;
import info.journeymap.shaded.kotlin.kotlin.UnsignedKt;
import info.journeymap.shaded.kotlin.kotlin.collections.AbstractList;
import info.journeymap.shaded.kotlin.kotlin.collections.ArraysKt;
import info.journeymap.shaded.kotlin.kotlin.internal.InlineOnly;
import info.journeymap.shaded.kotlin.kotlin.jvm.JvmName;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function1;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;

@Metadata(mv = { 1, 6, 0 }, k = 5, xi = 49, d1 = { "\u0000T\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0003H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00060\u0001*\u00020\u0007H\u0007ø\u0001\u0000¢\u0006\u0004\b\b\u0010\t\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\n0\u0001*\u00020\u000bH\u0007ø\u0001\u0000¢\u0006\u0004\b\f\u0010\r\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0001*\u00020\u000fH\u0007ø\u0001\u0000¢\u0006\u0004\b\u0010\u0010\u0011\u001a2\u0010\u0012\u001a\u00020\u0013*\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u00022\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0017\u0010\u0018\u001a2\u0010\u0012\u001a\u00020\u0013*\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u00062\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0019\u0010\u001a\u001a2\u0010\u0012\u001a\u00020\u0013*\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\n2\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007ø\u0001\u0000¢\u0006\u0004\b\u001b\u0010\u001c\u001a2\u0010\u0012\u001a\u00020\u0013*\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\u000e2\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007ø\u0001\u0000¢\u0006\u0004\b\u001d\u0010\u001e\u001a\u001f\u0010\u001f\u001a\u00020\u0002*\u00020\u00032\u0006\u0010 \u001a\u00020\u0013H\u0087\bø\u0001\u0000¢\u0006\u0004\b!\u0010\"\u001a\u001f\u0010\u001f\u001a\u00020\u0006*\u00020\u00072\u0006\u0010 \u001a\u00020\u0013H\u0087\bø\u0001\u0000¢\u0006\u0004\b#\u0010$\u001a\u001f\u0010\u001f\u001a\u00020\n*\u00020\u000b2\u0006\u0010 \u001a\u00020\u0013H\u0087\bø\u0001\u0000¢\u0006\u0004\b%\u0010&\u001a\u001f\u0010\u001f\u001a\u00020\u000e*\u00020\u000f2\u0006\u0010 \u001a\u00020\u0013H\u0087\bø\u0001\u0000¢\u0006\u0004\b'\u0010(\u001a.\u0010)\u001a\u00020**\u00020\u00032\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020*0,H\u0087\bø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b-\u0010.\u001a.\u0010)\u001a\u00020/*\u00020\u00032\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020/0,H\u0087\bø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b0\u00101\u001a.\u0010)\u001a\u00020**\u00020\u00072\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020*0,H\u0087\bø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b-\u00102\u001a.\u0010)\u001a\u00020/*\u00020\u00072\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020/0,H\u0087\bø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b0\u00103\u001a.\u0010)\u001a\u00020**\u00020\u000b2\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020*0,H\u0087\bø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b-\u00104\u001a.\u0010)\u001a\u00020/*\u00020\u000b2\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020/0,H\u0087\bø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b0\u00105\u001a.\u0010)\u001a\u00020**\u00020\u000f2\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020*0,H\u0087\bø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b-\u00106\u001a.\u0010)\u001a\u00020/*\u00020\u000f2\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020/0,H\u0087\bø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b0\u00107\u0082\u0002\u000b\n\u0002\b\u0019\n\u0005\b\u009920\u0001¨\u00068" }, d2 = { "asList", "", "Linfo/journeymap/shaded/kotlin/kotlin/UByte;", "Linfo/journeymap/shaded/kotlin/kotlin/UByteArray;", "asList-GBYM_sE", "([B)Ljava/util/List;", "Linfo/journeymap/shaded/kotlin/kotlin/UInt;", "Linfo/journeymap/shaded/kotlin/kotlin/UIntArray;", "asList--ajY-9A", "([I)Ljava/util/List;", "Linfo/journeymap/shaded/kotlin/kotlin/ULong;", "Linfo/journeymap/shaded/kotlin/kotlin/ULongArray;", "asList-QwZRm1k", "([J)Ljava/util/List;", "Linfo/journeymap/shaded/kotlin/kotlin/UShort;", "Linfo/journeymap/shaded/kotlin/kotlin/UShortArray;", "asList-rL5Bavg", "([S)Ljava/util/List;", "binarySearch", "", "element", "fromIndex", "toIndex", "binarySearch-WpHrYlw", "([BBII)I", "binarySearch-2fe2U9s", "([IIII)I", "binarySearch-K6DWlUc", "([JJII)I", "binarySearch-EtDCXyQ", "([SSII)I", "elementAt", "index", "elementAt-PpDY95g", "([BI)B", "elementAt-qFRl0hI", "([II)I", "elementAt-r7IrZao", "([JI)J", "elementAt-nggk6HY", "([SI)S", "sumOf", "Ljava/math/BigDecimal;", "selector", "Linfo/journeymap/shaded/kotlin/kotlin/Function1;", "sumOfBigDecimal", "([BLkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "Ljava/math/BigInteger;", "sumOfBigInteger", "([BLkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "([ILkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "([ILkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "([JLkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "([JLkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "([SLkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "([SLkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "info.journeymap.shaded.kotlin.kotlin-stdlib" }, xs = "info/journeymap/shaded/kotlin/kotlin/collections/unsigned/UArraysKt", pn = "info.journeymap.shaded.kotlin.kotlin.collections")
class UArraysKt___UArraysJvmKt {

    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final int elementAt_qFRl0hI(int[] $this$elementAt, int index) {
        Intrinsics.checkNotNullParameter($this$elementAt, "$this$elementAt");
        return UIntArray.get - pVg5ArA($this$elementAt, index);
    }

    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final long elementAt_r7IrZao(long[] $this$elementAt, int index) {
        Intrinsics.checkNotNullParameter($this$elementAt, "$this$elementAt");
        return ULongArray.get - s - VKNKU($this$elementAt, index);
    }

    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final byte elementAt_PpDY95g(byte[] $this$elementAt, int index) {
        Intrinsics.checkNotNullParameter($this$elementAt, "$this$elementAt");
        return UByteArray.get - w2LRezQ($this$elementAt, index);
    }

    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final short elementAt_nggk6HY(short[] $this$elementAt, int index) {
        Intrinsics.checkNotNullParameter($this$elementAt, "$this$elementAt");
        return UShortArray.get - Mh2AYeg($this$elementAt, index);
    }

    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final List<UInt> asList__ajY_9A(@NotNull final int[] $this$asList) {
        Intrinsics.checkNotNullParameter($this$asList, "$this$asList");
        return new RandomAccess() {

            @Override
            public int getSize() {
                return UIntArray.getSize - impl($this$asList);
            }

            @Override
            public boolean isEmpty() {
                return UIntArray.isEmpty - impl($this$asList);
            }

            public boolean contains_WZ4Q5Ns(int element) {
                return UIntArray.contains - WZ4Q5Ns($this$asList, element);
            }

            public int get_pVg5ArA(int index) {
                return UIntArray.get - pVg5ArA($this$asList, index);
            }

            public int indexOf_WZ4Q5Ns(int element) {
                int[] var2 = $this$asList;
                return ArraysKt.indexOf(var2, element);
            }

            public int lastIndexOf_WZ4Q5Ns(int element) {
                int[] var2 = $this$asList;
                return ArraysKt.lastIndexOf(var2, element);
            }
        };
    }

    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final List<ULong> asList_QwZRm1k(@NotNull final long[] $this$asList) {
        Intrinsics.checkNotNullParameter($this$asList, "$this$asList");
        return new RandomAccess() {

            @Override
            public int getSize() {
                return ULongArray.getSize - impl($this$asList);
            }

            @Override
            public boolean isEmpty() {
                return ULongArray.isEmpty - impl($this$asList);
            }

            public boolean contains_VKZWuLQ(long element) {
                return ULongArray.contains - VKZWuLQ($this$asList, element);
            }

            public long get_s_VKNKU(int index) {
                return ULongArray.get - s - VKNKU($this$asList, index);
            }

            public int indexOf_VKZWuLQ(long element) {
                long[] var3 = $this$asList;
                return ArraysKt.indexOf(var3, element);
            }

            public int lastIndexOf_VKZWuLQ(long element) {
                long[] var3 = $this$asList;
                return ArraysKt.lastIndexOf(var3, element);
            }
        };
    }

    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final List<UByte> asList_GBYM_sE(@NotNull final byte[] $this$asList) {
        Intrinsics.checkNotNullParameter($this$asList, "$this$asList");
        return new RandomAccess() {

            @Override
            public int getSize() {
                return UByteArray.getSize - impl($this$asList);
            }

            @Override
            public boolean isEmpty() {
                return UByteArray.isEmpty - impl($this$asList);
            }

            public boolean contains_7apg3OU(byte element) {
                ???;
            }

            public byte get_w2LRezQ(int index) {
                return UByteArray.get - w2LRezQ($this$asList, index);
            }

            public int indexOf_7apg3OU(byte element) {
                byte[] var2 = $this$asList;
                return ArraysKt.indexOf(var2, element);
            }

            public int lastIndexOf_7apg3OU(byte element) {
                byte[] var2 = $this$asList;
                return ArraysKt.lastIndexOf(var2, element);
            }
        };
    }

    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final List<UShort> asList_rL5Bavg(@NotNull final short[] $this$asList) {
        Intrinsics.checkNotNullParameter($this$asList, "$this$asList");
        return new RandomAccess() {

            @Override
            public int getSize() {
                return UShortArray.getSize - impl($this$asList);
            }

            @Override
            public boolean isEmpty() {
                return UShortArray.isEmpty - impl($this$asList);
            }

            public boolean contains_xj2QHRw(short element) {
                return UShortArray.contains - xj2QHRw($this$asList, element);
            }

            public short get_Mh2AYeg(int index) {
                return UShortArray.get - Mh2AYeg($this$asList, index);
            }

            public int indexOf_xj2QHRw(short element) {
                short[] var2 = $this$asList;
                return ArraysKt.indexOf(var2, element);
            }

            public int lastIndexOf_xj2QHRw(short element) {
                short[] var2 = $this$asList;
                return ArraysKt.lastIndexOf(var2, element);
            }
        };
    }

    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final int binarySearch_2fe2U9s(@NotNull int[] $this$binarySearch, int element, int fromIndex, int toIndex) {
        Intrinsics.checkNotNullParameter($this$binarySearch, "$this$binarySearch");
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(fromIndex, toIndex, UIntArray.getSize - impl($this$binarySearch));
        int signedElement = element;
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = low + high >>> 1;
            int midVal = $this$binarySearch[mid];
            int cmp = UnsignedKt.uintCompare(midVal, signedElement);
            if (cmp < 0) {
                low = mid + 1;
            } else {
                if (cmp <= 0) {
                    return mid;
                }
                high = mid - 1;
            }
        }
        return -(low + 1);
    }

    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final int binarySearch_K6DWlUc(@NotNull long[] $this$binarySearch, long element, int fromIndex, int toIndex) {
        Intrinsics.checkNotNullParameter($this$binarySearch, "$this$binarySearch");
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(fromIndex, toIndex, ULongArray.getSize - impl($this$binarySearch));
        long signedElement = element;
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = low + high >>> 1;
            long midVal = $this$binarySearch[mid];
            int cmp = UnsignedKt.ulongCompare(midVal, signedElement);
            if (cmp < 0) {
                low = mid + 1;
            } else {
                if (cmp <= 0) {
                    return mid;
                }
                high = mid - 1;
            }
        }
        return -(low + 1);
    }

    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final int binarySearch_WpHrYlw(@NotNull byte[] $this$binarySearch, byte element, int fromIndex, int toIndex) {
        Intrinsics.checkNotNullParameter($this$binarySearch, "$this$binarySearch");
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(fromIndex, toIndex, UByteArray.getSize - impl($this$binarySearch));
        int signedElement = element & 255;
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = low + high >>> 1;
            byte midVal = $this$binarySearch[mid];
            int cmp = UnsignedKt.uintCompare(midVal, signedElement);
            if (cmp < 0) {
                low = mid + 1;
            } else {
                if (cmp <= 0) {
                    return mid;
                }
                high = mid - 1;
            }
        }
        return -(low + 1);
    }

    @SinceKotlin(version = "1.3")
    @ExperimentalUnsignedTypes
    public static final int binarySearch_EtDCXyQ(@NotNull short[] $this$binarySearch, short element, int fromIndex, int toIndex) {
        Intrinsics.checkNotNullParameter($this$binarySearch, "$this$binarySearch");
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(fromIndex, toIndex, UShortArray.getSize - impl($this$binarySearch));
        int signedElement = element & '\uffff';
        int low = fromIndex;
        int high = toIndex - 1;
        while (low <= high) {
            int mid = low + high >>> 1;
            short midVal = $this$binarySearch[mid];
            int cmp = UnsignedKt.uintCompare(midVal, signedElement);
            if (cmp < 0) {
                low = mid + 1;
            } else {
                if (cmp <= 0) {
                    return mid;
                }
                high = mid - 1;
            }
        }
        return -(low + 1);
    }

    @SinceKotlin(version = "1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name = "sumOfBigDecimal")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final BigDecimal sumOfBigDecimal(int[] $this$sumOf, Function1<? super UInt, ? extends BigDecimal> selector) {
        Intrinsics.checkNotNullParameter($this$sumOf, "$this$sumOf");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigDecimal element = BigDecimal.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(element, "valueOf(this.toLong())");
        BigDecimal sum = element;
        Iterator var3 = UIntArray.iterator - impl($this$sumOf);
        while (var3.hasNext()) {
            int elementx = ((UInt) var3.next()).unbox - impl();
            BigDecimal var5 = sum.add((BigDecimal) selector.invoke(UInt.box - impl(elementx)));
            Intrinsics.checkNotNullExpressionValue(var5, "this.add(other)");
            sum = var5;
        }
        return sum;
    }

    @SinceKotlin(version = "1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name = "sumOfBigDecimal")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final BigDecimal sumOfBigDecimal(long[] $this$sumOf, Function1<? super ULong, ? extends BigDecimal> selector) {
        Intrinsics.checkNotNullParameter($this$sumOf, "$this$sumOf");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigDecimal element = BigDecimal.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(element, "valueOf(this.toLong())");
        BigDecimal sum = element;
        Iterator var3 = ULongArray.iterator - impl($this$sumOf);
        while (var3.hasNext()) {
            long elementx = ((ULong) var3.next()).unbox - impl();
            BigDecimal var6 = sum.add((BigDecimal) selector.invoke(ULong.box - impl(elementx)));
            Intrinsics.checkNotNullExpressionValue(var6, "this.add(other)");
            sum = var6;
        }
        return sum;
    }

    @SinceKotlin(version = "1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name = "sumOfBigDecimal")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final BigDecimal sumOfBigDecimal(byte[] $this$sumOf, Function1<? super UByte, ? extends BigDecimal> selector) {
        Intrinsics.checkNotNullParameter($this$sumOf, "$this$sumOf");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigDecimal element = BigDecimal.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(element, "valueOf(this.toLong())");
        BigDecimal sum = element;
        Iterator var3 = UByteArray.iterator - impl($this$sumOf);
        while (var3.hasNext()) {
            byte elementx = ((UByte) var3.next()).unbox - impl();
            BigDecimal var5 = sum.add((BigDecimal) selector.invoke(UByte.box - impl(elementx)));
            Intrinsics.checkNotNullExpressionValue(var5, "this.add(other)");
            sum = var5;
        }
        return sum;
    }

    @SinceKotlin(version = "1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name = "sumOfBigDecimal")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final BigDecimal sumOfBigDecimal(short[] $this$sumOf, Function1<? super UShort, ? extends BigDecimal> selector) {
        Intrinsics.checkNotNullParameter($this$sumOf, "$this$sumOf");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigDecimal element = BigDecimal.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(element, "valueOf(this.toLong())");
        BigDecimal sum = element;
        Iterator var3 = UShortArray.iterator - impl($this$sumOf);
        while (var3.hasNext()) {
            short elementx = ((UShort) var3.next()).unbox - impl();
            BigDecimal var5 = sum.add((BigDecimal) selector.invoke(UShort.box - impl(elementx)));
            Intrinsics.checkNotNullExpressionValue(var5, "this.add(other)");
            sum = var5;
        }
        return sum;
    }

    @SinceKotlin(version = "1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name = "sumOfBigInteger")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final BigInteger sumOfBigInteger(int[] $this$sumOf, Function1<? super UInt, ? extends BigInteger> selector) {
        Intrinsics.checkNotNullParameter($this$sumOf, "$this$sumOf");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigInteger element = BigInteger.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(element, "valueOf(this.toLong())");
        BigInteger sum = element;
        Iterator var3 = UIntArray.iterator - impl($this$sumOf);
        while (var3.hasNext()) {
            int elementx = ((UInt) var3.next()).unbox - impl();
            BigInteger var5 = sum.add((BigInteger) selector.invoke(UInt.box - impl(elementx)));
            Intrinsics.checkNotNullExpressionValue(var5, "this.add(other)");
            sum = var5;
        }
        return sum;
    }

    @SinceKotlin(version = "1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name = "sumOfBigInteger")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final BigInteger sumOfBigInteger(long[] $this$sumOf, Function1<? super ULong, ? extends BigInteger> selector) {
        Intrinsics.checkNotNullParameter($this$sumOf, "$this$sumOf");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigInteger element = BigInteger.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(element, "valueOf(this.toLong())");
        BigInteger sum = element;
        Iterator var3 = ULongArray.iterator - impl($this$sumOf);
        while (var3.hasNext()) {
            long elementx = ((ULong) var3.next()).unbox - impl();
            BigInteger var6 = sum.add((BigInteger) selector.invoke(ULong.box - impl(elementx)));
            Intrinsics.checkNotNullExpressionValue(var6, "this.add(other)");
            sum = var6;
        }
        return sum;
    }

    @SinceKotlin(version = "1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name = "sumOfBigInteger")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final BigInteger sumOfBigInteger(byte[] $this$sumOf, Function1<? super UByte, ? extends BigInteger> selector) {
        Intrinsics.checkNotNullParameter($this$sumOf, "$this$sumOf");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigInteger element = BigInteger.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(element, "valueOf(this.toLong())");
        BigInteger sum = element;
        Iterator var3 = UByteArray.iterator - impl($this$sumOf);
        while (var3.hasNext()) {
            byte elementx = ((UByte) var3.next()).unbox - impl();
            BigInteger var5 = sum.add((BigInteger) selector.invoke(UByte.box - impl(elementx)));
            Intrinsics.checkNotNullExpressionValue(var5, "this.add(other)");
            sum = var5;
        }
        return sum;
    }

    @SinceKotlin(version = "1.4")
    @OverloadResolutionByLambdaReturnType
    @JvmName(name = "sumOfBigInteger")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final BigInteger sumOfBigInteger(short[] $this$sumOf, Function1<? super UShort, ? extends BigInteger> selector) {
        Intrinsics.checkNotNullParameter($this$sumOf, "$this$sumOf");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigInteger element = BigInteger.valueOf(0L);
        Intrinsics.checkNotNullExpressionValue(element, "valueOf(this.toLong())");
        BigInteger sum = element;
        Iterator var3 = UShortArray.iterator - impl($this$sumOf);
        while (var3.hasNext()) {
            short elementx = ((UShort) var3.next()).unbox - impl();
            BigInteger var5 = sum.add((BigInteger) selector.invoke(UShort.box - impl(elementx)));
            Intrinsics.checkNotNullExpressionValue(var5, "this.add(other)");
            sum = var5;
        }
        return sum;
    }

    public UArraysKt___UArraysJvmKt() {
    }
}