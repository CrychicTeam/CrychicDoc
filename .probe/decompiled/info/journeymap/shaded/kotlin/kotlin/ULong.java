package info.journeymap.shaded.kotlin.kotlin;

import info.journeymap.shaded.kotlin.kotlin.internal.InlineOnly;
import info.journeymap.shaded.kotlin.kotlin.jvm.JvmInline;
import info.journeymap.shaded.kotlin.kotlin.ranges.ULongRange;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;

@JvmInline
@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\"\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0007\n\u0002\u0010\n\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u000e\b\u0087@\u0018\u0000 |2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001|B\u0014\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005J\u001b\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\fø\u0001\u0000¢\u0006\u0004\b\n\u0010\u000bJ\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u000eH\u0087\nø\u0001\u0000¢\u0006\u0004\b\u000f\u0010\u0010J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u0012\u0010\u0013J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0000H\u0097\nø\u0001\u0000¢\u0006\u0004\b\u0014\u0010\u0015J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0016H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u0017\u0010\u0018J\u0016\u0010\u0019\u001a\u00020\u0000H\u0087\nø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b\u001a\u0010\u0005J\u001b\u0010\u001b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001c\u0010\u001dJ\u001b\u0010\u001b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u001fJ\u001b\u0010\u001b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b \u0010\u000bJ\u001b\u0010\u001b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\nø\u0001\u0000¢\u0006\u0004\b!\u0010\"J\u001a\u0010#\u001a\u00020$2\b\u0010\t\u001a\u0004\u0018\u00010%HÖ\u0003¢\u0006\u0004\b&\u0010'J\u001b\u0010(\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\bø\u0001\u0000¢\u0006\u0004\b)\u0010\u001dJ\u001b\u0010(\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\bø\u0001\u0000¢\u0006\u0004\b*\u0010\u001fJ\u001b\u0010(\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\b+\u0010\u000bJ\u001b\u0010(\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\bø\u0001\u0000¢\u0006\u0004\b,\u0010\"J\u0010\u0010-\u001a\u00020\rHÖ\u0001¢\u0006\u0004\b.\u0010/J\u0016\u00100\u001a\u00020\u0000H\u0087\nø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b1\u0010\u0005J\u0016\u00102\u001a\u00020\u0000H\u0087\bø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b3\u0010\u0005J\u001b\u00104\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\nø\u0001\u0000¢\u0006\u0004\b5\u0010\u001dJ\u001b\u00104\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\b6\u0010\u001fJ\u001b\u00104\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b7\u0010\u000bJ\u001b\u00104\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\nø\u0001\u0000¢\u0006\u0004\b8\u0010\"J\u001b\u00109\u001a\u00020\u000e2\u0006\u0010\t\u001a\u00020\u000eH\u0087\bø\u0001\u0000¢\u0006\u0004\b:\u0010;J\u001b\u00109\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\bø\u0001\u0000¢\u0006\u0004\b<\u0010\u0013J\u001b\u00109\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\b=\u0010\u000bJ\u001b\u00109\u001a\u00020\u00162\u0006\u0010\t\u001a\u00020\u0016H\u0087\bø\u0001\u0000¢\u0006\u0004\b>\u0010?J\u001b\u0010@\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\fø\u0001\u0000¢\u0006\u0004\bA\u0010\u000bJ\u001b\u0010B\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\nø\u0001\u0000¢\u0006\u0004\bC\u0010\u001dJ\u001b\u0010B\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\bD\u0010\u001fJ\u001b\u0010B\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\bE\u0010\u000bJ\u001b\u0010B\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\nø\u0001\u0000¢\u0006\u0004\bF\u0010\"J\u001b\u0010G\u001a\u00020H2\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\bI\u0010JJ\u001b\u0010K\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\nø\u0001\u0000¢\u0006\u0004\bL\u0010\u001dJ\u001b\u0010K\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\bM\u0010\u001fJ\u001b\u0010K\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\bN\u0010\u000bJ\u001b\u0010K\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\nø\u0001\u0000¢\u0006\u0004\bO\u0010\"J\u001e\u0010P\u001a\u00020\u00002\u0006\u0010Q\u001a\u00020\rH\u0087\fø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\bR\u0010\u001fJ\u001e\u0010S\u001a\u00020\u00002\u0006\u0010Q\u001a\u00020\rH\u0087\fø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\bT\u0010\u001fJ\u001b\u0010U\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\nø\u0001\u0000¢\u0006\u0004\bV\u0010\u001dJ\u001b\u0010U\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\bW\u0010\u001fJ\u001b\u0010U\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\bX\u0010\u000bJ\u001b\u0010U\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\nø\u0001\u0000¢\u0006\u0004\bY\u0010\"J\u0010\u0010Z\u001a\u00020[H\u0087\b¢\u0006\u0004\b\\\u0010]J\u0010\u0010^\u001a\u00020_H\u0087\b¢\u0006\u0004\b`\u0010aJ\u0010\u0010b\u001a\u00020cH\u0087\b¢\u0006\u0004\bd\u0010eJ\u0010\u0010f\u001a\u00020\rH\u0087\b¢\u0006\u0004\bg\u0010/J\u0010\u0010h\u001a\u00020\u0003H\u0087\b¢\u0006\u0004\bi\u0010\u0005J\u0010\u0010j\u001a\u00020kH\u0087\b¢\u0006\u0004\bl\u0010mJ\u000f\u0010n\u001a\u00020oH\u0016¢\u0006\u0004\bp\u0010qJ\u0016\u0010r\u001a\u00020\u000eH\u0087\bø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\bs\u0010]J\u0016\u0010t\u001a\u00020\u0011H\u0087\bø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\bu\u0010/J\u0016\u0010v\u001a\u00020\u0000H\u0087\bø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\bw\u0010\u0005J\u0016\u0010x\u001a\u00020\u0016H\u0087\bø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\by\u0010mJ\u001b\u0010z\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\fø\u0001\u0000¢\u0006\u0004\b{\u0010\u000bR\u0016\u0010\u0002\u001a\u00020\u00038\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0007\u0088\u0001\u0002\u0092\u0001\u00020\u0003ø\u0001\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!¨\u0006}" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/ULong;", "", "data", "", "constructor-impl", "(J)J", "getData$annotations", "()V", "and", "other", "and-VKZWuLQ", "(JJ)J", "compareTo", "", "Linfo/journeymap/shaded/kotlin/kotlin/UByte;", "compareTo-7apg3OU", "(JB)I", "Linfo/journeymap/shaded/kotlin/kotlin/UInt;", "compareTo-WZ4Q5Ns", "(JI)I", "compareTo-VKZWuLQ", "(JJ)I", "Linfo/journeymap/shaded/kotlin/kotlin/UShort;", "compareTo-xj2QHRw", "(JS)I", "dec", "dec-s-VKNKU", "div", "div-7apg3OU", "(JB)J", "div-WZ4Q5Ns", "(JI)J", "div-VKZWuLQ", "div-xj2QHRw", "(JS)J", "equals", "", "", "equals-impl", "(JLjava/lang/Object;)Z", "floorDiv", "floorDiv-7apg3OU", "floorDiv-WZ4Q5Ns", "floorDiv-VKZWuLQ", "floorDiv-xj2QHRw", "hashCode", "hashCode-impl", "(J)I", "inc", "inc-s-VKNKU", "inv", "inv-s-VKNKU", "minus", "minus-7apg3OU", "minus-WZ4Q5Ns", "minus-VKZWuLQ", "minus-xj2QHRw", "mod", "mod-7apg3OU", "(JB)B", "mod-WZ4Q5Ns", "mod-VKZWuLQ", "mod-xj2QHRw", "(JS)S", "or", "or-VKZWuLQ", "plus", "plus-7apg3OU", "plus-WZ4Q5Ns", "plus-VKZWuLQ", "plus-xj2QHRw", "rangeTo", "Linfo/journeymap/shaded/kotlin/kotlin/ranges/ULongRange;", "rangeTo-VKZWuLQ", "(JJ)Lkotlin/ranges/ULongRange;", "rem", "rem-7apg3OU", "rem-WZ4Q5Ns", "rem-VKZWuLQ", "rem-xj2QHRw", "shl", "bitCount", "shl-s-VKNKU", "shr", "shr-s-VKNKU", "times", "times-7apg3OU", "times-WZ4Q5Ns", "times-VKZWuLQ", "times-xj2QHRw", "toByte", "", "toByte-impl", "(J)B", "toDouble", "", "toDouble-impl", "(J)D", "toFloat", "", "toFloat-impl", "(J)F", "toInt", "toInt-impl", "toLong", "toLong-impl", "toShort", "", "toShort-impl", "(J)S", "toString", "", "toString-impl", "(J)Ljava/lang/String;", "toUByte", "toUByte-w2LRezQ", "toUInt", "toUInt-pVg5ArA", "toULong", "toULong-s-VKNKU", "toUShort", "toUShort-Mh2AYeg", "xor", "xor-VKZWuLQ", "Companion", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
@SinceKotlin(version = "1.5")
@WasExperimental(markerClass = { ExperimentalUnsignedTypes.class })
public final class ULong implements Comparable<ULong> {

    @NotNull
    public static final ULong.Companion Companion = new ULong.Companion(null);

    private final long data;

    public static final long MIN_VALUE = 0L;

    public static final long MAX_VALUE = -1L;

    public static final int SIZE_BYTES = 8;

    public static final int SIZE_BITS = 64;

    @InlineOnly
    private static final int compareTo_7apg3OU(/* $VF was: compareTo-7apg3OU*/
    long arg0, byte other) {
        long var3 = constructor - impl((long) other & 255L);
        return UnsignedKt.ulongCompare(arg0, var3);
    }

    @InlineOnly
    private static final int compareTo_xj2QHRw(/* $VF was: compareTo-xj2QHRw*/
    long arg0, short other) {
        long var3 = constructor - impl((long) other & 65535L);
        return UnsignedKt.ulongCompare(arg0, var3);
    }

    @InlineOnly
    private static final int compareTo_WZ4Q5Ns(/* $VF was: compareTo-WZ4Q5Ns*/
    long arg0, int other) {
        long var3 = constructor - impl((long) other & 4294967295L);
        return UnsignedKt.ulongCompare(arg0, var3);
    }

    @InlineOnly
    private static int compareTo_VKZWuLQ(/* $VF was: compareTo-VKZWuLQ*/
    long arg0, long other) {
        return UnsignedKt.ulongCompare(arg0, other);
    }

    @InlineOnly
    private int compareTo_VKZWuLQ(/* $VF was: compareTo-VKZWuLQ*/
    long other) {
        long var3 = this.unbox - impl();
        return UnsignedKt.ulongCompare(var3, other);
    }

    @InlineOnly
    private static final long plus_7apg3OU(/* $VF was: plus-7apg3OU*/
    long arg0, byte other) {
        long var3 = constructor - impl((long) other & 255L);
        return constructor - impl(arg0 + var3);
    }

    @InlineOnly
    private static final long plus_xj2QHRw(/* $VF was: plus-xj2QHRw*/
    long arg0, short other) {
        long var3 = constructor - impl((long) other & 65535L);
        return constructor - impl(arg0 + var3);
    }

    @InlineOnly
    private static final long plus_WZ4Q5Ns(/* $VF was: plus-WZ4Q5Ns*/
    long arg0, int other) {
        long var3 = constructor - impl((long) other & 4294967295L);
        return constructor - impl(arg0 + var3);
    }

    @InlineOnly
    private static final long plus_VKZWuLQ(/* $VF was: plus-VKZWuLQ*/
    long arg0, long other) {
        return constructor - impl(arg0 + other);
    }

    @InlineOnly
    private static final long minus_7apg3OU(/* $VF was: minus-7apg3OU*/
    long arg0, byte other) {
        long var3 = constructor - impl((long) other & 255L);
        return constructor - impl(arg0 - var3);
    }

    @InlineOnly
    private static final long minus_xj2QHRw(/* $VF was: minus-xj2QHRw*/
    long arg0, short other) {
        long var3 = constructor - impl((long) other & 65535L);
        return constructor - impl(arg0 - var3);
    }

    @InlineOnly
    private static final long minus_WZ4Q5Ns(/* $VF was: minus-WZ4Q5Ns*/
    long arg0, int other) {
        long var3 = constructor - impl((long) other & 4294967295L);
        return constructor - impl(arg0 - var3);
    }

    @InlineOnly
    private static final long minus_VKZWuLQ(/* $VF was: minus-VKZWuLQ*/
    long arg0, long other) {
        return constructor - impl(arg0 - other);
    }

    @InlineOnly
    private static final long times_7apg3OU(/* $VF was: times-7apg3OU*/
    long arg0, byte other) {
        long var3 = constructor - impl((long) other & 255L);
        return constructor - impl(arg0 * var3);
    }

    @InlineOnly
    private static final long times_xj2QHRw(/* $VF was: times-xj2QHRw*/
    long arg0, short other) {
        long var3 = constructor - impl((long) other & 65535L);
        return constructor - impl(arg0 * var3);
    }

    @InlineOnly
    private static final long times_WZ4Q5Ns(/* $VF was: times-WZ4Q5Ns*/
    long arg0, int other) {
        long var3 = constructor - impl((long) other & 4294967295L);
        return constructor - impl(arg0 * var3);
    }

    @InlineOnly
    private static final long times_VKZWuLQ(/* $VF was: times-VKZWuLQ*/
    long arg0, long other) {
        return constructor - impl(arg0 * other);
    }

    @InlineOnly
    private static final long div_7apg3OU(/* $VF was: div-7apg3OU*/
    long arg0, byte other) {
        long var3 = constructor - impl((long) other & 255L);
        return UnsignedKt.ulongDivide - eb3DHEI(arg0, var3);
    }

    @InlineOnly
    private static final long div_xj2QHRw(/* $VF was: div-xj2QHRw*/
    long arg0, short other) {
        long var3 = constructor - impl((long) other & 65535L);
        return UnsignedKt.ulongDivide - eb3DHEI(arg0, var3);
    }

    @InlineOnly
    private static final long div_WZ4Q5Ns(/* $VF was: div-WZ4Q5Ns*/
    long arg0, int other) {
        long var3 = constructor - impl((long) other & 4294967295L);
        return UnsignedKt.ulongDivide - eb3DHEI(arg0, var3);
    }

    @InlineOnly
    private static final long div_VKZWuLQ(/* $VF was: div-VKZWuLQ*/
    long arg0, long other) {
        return UnsignedKt.ulongDivide - eb3DHEI(arg0, other);
    }

    @InlineOnly
    private static final long rem_7apg3OU(/* $VF was: rem-7apg3OU*/
    long arg0, byte other) {
        long var3 = constructor - impl((long) other & 255L);
        return UnsignedKt.ulongRemainder - eb3DHEI(arg0, var3);
    }

    @InlineOnly
    private static final long rem_xj2QHRw(/* $VF was: rem-xj2QHRw*/
    long arg0, short other) {
        long var3 = constructor - impl((long) other & 65535L);
        return UnsignedKt.ulongRemainder - eb3DHEI(arg0, var3);
    }

    @InlineOnly
    private static final long rem_WZ4Q5Ns(/* $VF was: rem-WZ4Q5Ns*/
    long arg0, int other) {
        long var3 = constructor - impl((long) other & 4294967295L);
        return UnsignedKt.ulongRemainder - eb3DHEI(arg0, var3);
    }

    @InlineOnly
    private static final long rem_VKZWuLQ(/* $VF was: rem-VKZWuLQ*/
    long arg0, long other) {
        return UnsignedKt.ulongRemainder - eb3DHEI(arg0, other);
    }

    @InlineOnly
    private static final long floorDiv_7apg3OU(/* $VF was: floorDiv-7apg3OU*/
    long arg0, byte other) {
        long var3 = constructor - impl((long) other & 255L);
        return UnsignedKt.ulongDivide - eb3DHEI(arg0, var3);
    }

    @InlineOnly
    private static final long floorDiv_xj2QHRw(/* $VF was: floorDiv-xj2QHRw*/
    long arg0, short other) {
        long var3 = constructor - impl((long) other & 65535L);
        return UnsignedKt.ulongDivide - eb3DHEI(arg0, var3);
    }

    @InlineOnly
    private static final long floorDiv_WZ4Q5Ns(/* $VF was: floorDiv-WZ4Q5Ns*/
    long arg0, int other) {
        long var3 = constructor - impl((long) other & 4294967295L);
        return UnsignedKt.ulongDivide - eb3DHEI(arg0, var3);
    }

    @InlineOnly
    private static final long floorDiv_VKZWuLQ(/* $VF was: floorDiv-VKZWuLQ*/
    long arg0, long other) {
        return UnsignedKt.ulongDivide - eb3DHEI(arg0, other);
    }

    @InlineOnly
    private static final byte mod_7apg3OU(/* $VF was: mod-7apg3OU*/
    long arg0, byte other) {
        long var3 = constructor - impl((long) other & 255L);
        var3 = UnsignedKt.ulongRemainder - eb3DHEI(arg0, var3);
        return UByte.constructor - impl((byte) ((int) var3));
    }

    @InlineOnly
    private static final short mod_xj2QHRw(/* $VF was: mod-xj2QHRw*/
    long arg0, short other) {
        long var3 = constructor - impl((long) other & 65535L);
        var3 = UnsignedKt.ulongRemainder - eb3DHEI(arg0, var3);
        return UShort.constructor - impl((short) ((int) var3));
    }

    @InlineOnly
    private static final int mod_WZ4Q5Ns(/* $VF was: mod-WZ4Q5Ns*/
    long arg0, int other) {
        long var3 = constructor - impl((long) other & 4294967295L);
        var3 = UnsignedKt.ulongRemainder - eb3DHEI(arg0, var3);
        return UInt.constructor - impl((int) var3);
    }

    @InlineOnly
    private static final long mod_VKZWuLQ(/* $VF was: mod-VKZWuLQ*/
    long arg0, long other) {
        return UnsignedKt.ulongRemainder - eb3DHEI(arg0, other);
    }

    @InlineOnly
    private static final long inc_s_VKNKU(/* $VF was: inc-s-VKNKU*/
    long arg0) {
        return constructor - impl(arg0 + 1L);
    }

    @InlineOnly
    private static final long dec_s_VKNKU(/* $VF was: dec-s-VKNKU*/
    long arg0) {
        return constructor - impl(arg0 + -1L);
    }

    @InlineOnly
    private static final ULongRange rangeTo_VKZWuLQ(/* $VF was: rangeTo-VKZWuLQ*/
    long arg0, long other) {
        return new ULongRange(arg0, other, null);
    }

    @InlineOnly
    private static final long shl_s_VKNKU(/* $VF was: shl-s-VKNKU*/
    long arg0, int bitCount) {
        return constructor - impl(arg0 << bitCount);
    }

    @InlineOnly
    private static final long shr_s_VKNKU(/* $VF was: shr-s-VKNKU*/
    long arg0, int bitCount) {
        return constructor - impl(arg0 >>> bitCount);
    }

    @InlineOnly
    private static final long and_VKZWuLQ(/* $VF was: and-VKZWuLQ*/
    long arg0, long other) {
        return constructor - impl(arg0 & other);
    }

    @InlineOnly
    private static final long or_VKZWuLQ(/* $VF was: or-VKZWuLQ*/
    long arg0, long other) {
        return constructor - impl(arg0 | other);
    }

    @InlineOnly
    private static final long xor_VKZWuLQ(/* $VF was: xor-VKZWuLQ*/
    long arg0, long other) {
        return constructor - impl(arg0 ^ other);
    }

    @InlineOnly
    private static final long inv_s_VKNKU(/* $VF was: inv-s-VKNKU*/
    long arg0) {
        return constructor - impl(~arg0);
    }

    @InlineOnly
    private static final byte toByte_impl(/* $VF was: toByte-impl*/
    long arg0) {
        return (byte) ((int) arg0);
    }

    @InlineOnly
    private static final short toShort_impl(/* $VF was: toShort-impl*/
    long arg0) {
        return (short) ((int) arg0);
    }

    @InlineOnly
    private static final int toInt_impl(/* $VF was: toInt-impl*/
    long arg0) {
        return (int) arg0;
    }

    @InlineOnly
    private static final long toLong_impl(/* $VF was: toLong-impl*/
    long arg0) {
        return arg0;
    }

    @InlineOnly
    private static final byte toUByte_w2LRezQ(/* $VF was: toUByte-w2LRezQ*/
    long arg0) {
        return UByte.constructor - impl((byte) ((int) arg0));
    }

    @InlineOnly
    private static final short toUShort_Mh2AYeg(/* $VF was: toUShort-Mh2AYeg*/
    long arg0) {
        return UShort.constructor - impl((short) ((int) arg0));
    }

    @InlineOnly
    private static final int toUInt_pVg5ArA(/* $VF was: toUInt-pVg5ArA*/
    long arg0) {
        return UInt.constructor - impl((int) arg0);
    }

    @InlineOnly
    private static final long toULong_s_VKNKU(/* $VF was: toULong-s-VKNKU*/
    long arg0) {
        return arg0;
    }

    @InlineOnly
    private static final float toFloat_impl(/* $VF was: toFloat-impl*/
    long arg0) {
        return (float) UnsignedKt.ulongToDouble(arg0);
    }

    @InlineOnly
    private static final double toDouble_impl(/* $VF was: toDouble-impl*/
    long arg0) {
        return UnsignedKt.ulongToDouble(arg0);
    }

    @NotNull
    public static String toString_impl(/* $VF was: toString-impl*/
    long arg0) {
        return UnsignedKt.ulongToString(arg0);
    }

    @NotNull
    public String toString() {
        return toString - impl(this.data);
    }

    public static int hashCode_impl(/* $VF was: hashCode-impl*/
    long arg0) {
        return (int) (arg0 ^ arg0 >>> 32);
    }

    public int hashCode() {
        return hashCode - impl(this.data);
    }

    public static boolean equals_impl(/* $VF was: equals-impl*/
    long arg0, Object other) {
        if (!(other instanceof ULong)) {
            return false;
        } else {
            long var3 = ((ULong) other).unbox - impl();
            return arg0 == var3;
        }
    }

    public boolean equals(Object other) {
        return equals - impl(this.data, other);
    }

    @PublishedApi
    public static long constructor_impl(/* $VF was: constructor-impl*/
    long data) {
        return data;
    }

    public static final boolean equals_impl0(/* $VF was: equals-impl0*/
    long p1, long p2) {
        return p1 == p2;
    }

    @Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0016\u0010\u0003\u001a\u00020\u0004X\u0086Tø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\n\u0002\u0010\u0005R\u0016\u0010\u0006\u001a\u00020\u0004X\u0086Tø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\n\u0002\u0010\u0005R\u000e\u0010\u0007\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!¨\u0006\n" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/ULong$Companion;", "", "()V", "MAX_VALUE", "Linfo/journeymap/shaded/kotlin/kotlin/ULong;", "J", "MIN_VALUE", "SIZE_BITS", "", "SIZE_BYTES", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
    public static final class Companion {

        private Companion() {
        }
    }
}