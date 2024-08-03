package info.journeymap.shaded.kotlin.kotlin;

import info.journeymap.shaded.kotlin.kotlin.internal.InlineOnly;
import info.journeymap.shaded.kotlin.kotlin.jvm.JvmInline;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.kotlin.kotlin.ranges.UIntRange;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;

@JvmInline
@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\n\n\u0002\b\t\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b!\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u000e\b\u0087@\u0018\u0000 t2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001tB\u0014\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005J\u001b\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\fø\u0001\u0000¢\u0006\u0004\b\n\u0010\u000bJ\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u000eH\u0087\nø\u0001\u0000¢\u0006\u0004\b\u000f\u0010\u0010J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u0012\u0010\u0013J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0014H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0000H\u0097\nø\u0001\u0000¢\u0006\u0004\b\u0017\u0010\u0018J\u0016\u0010\u0019\u001a\u00020\u0000H\u0087\nø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b\u001a\u0010\u0005J\u001b\u0010\u001b\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001c\u0010\u0010J\u001b\u0010\u001b\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001d\u0010\u0013J\u001b\u0010\u001b\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u001fJ\u001b\u0010\u001b\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b \u0010\u0018J\u001a\u0010!\u001a\u00020\"2\b\u0010\t\u001a\u0004\u0018\u00010#HÖ\u0003¢\u0006\u0004\b$\u0010%J\u001b\u0010&\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\bø\u0001\u0000¢\u0006\u0004\b'\u0010\u0010J\u001b\u0010&\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\bø\u0001\u0000¢\u0006\u0004\b(\u0010\u0013J\u001b\u0010&\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\bø\u0001\u0000¢\u0006\u0004\b)\u0010\u001fJ\u001b\u0010&\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\b*\u0010\u0018J\u0010\u0010+\u001a\u00020\rHÖ\u0001¢\u0006\u0004\b,\u0010-J\u0016\u0010.\u001a\u00020\u0000H\u0087\nø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b/\u0010\u0005J\u0016\u00100\u001a\u00020\u0000H\u0087\bø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b1\u0010\u0005J\u001b\u00102\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\nø\u0001\u0000¢\u0006\u0004\b3\u0010\u0010J\u001b\u00102\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\b4\u0010\u0013J\u001b\u00102\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\nø\u0001\u0000¢\u0006\u0004\b5\u0010\u001fJ\u001b\u00102\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b6\u0010\u0018J\u001b\u00107\u001a\u00020\u000e2\u0006\u0010\t\u001a\u00020\u000eH\u0087\bø\u0001\u0000¢\u0006\u0004\b8\u00109J\u001b\u00107\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\bø\u0001\u0000¢\u0006\u0004\b:\u0010\u0013J\u001b\u00107\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\bø\u0001\u0000¢\u0006\u0004\b;\u0010\u001fJ\u001b\u00107\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\b<\u0010\u000bJ\u001b\u0010=\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\fø\u0001\u0000¢\u0006\u0004\b>\u0010\u000bJ\u001b\u0010?\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\nø\u0001\u0000¢\u0006\u0004\b@\u0010\u0010J\u001b\u0010?\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\bA\u0010\u0013J\u001b\u0010?\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\nø\u0001\u0000¢\u0006\u0004\bB\u0010\u001fJ\u001b\u0010?\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\bC\u0010\u0018J\u001b\u0010D\u001a\u00020E2\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\bF\u0010GJ\u001b\u0010H\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\nø\u0001\u0000¢\u0006\u0004\bI\u0010\u0010J\u001b\u0010H\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\bJ\u0010\u0013J\u001b\u0010H\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\nø\u0001\u0000¢\u0006\u0004\bK\u0010\u001fJ\u001b\u0010H\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\bL\u0010\u0018J\u001b\u0010M\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u000eH\u0087\nø\u0001\u0000¢\u0006\u0004\bN\u0010\u0010J\u001b\u0010M\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\nø\u0001\u0000¢\u0006\u0004\bO\u0010\u0013J\u001b\u0010M\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0087\nø\u0001\u0000¢\u0006\u0004\bP\u0010\u001fJ\u001b\u0010M\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\bQ\u0010\u0018J\u0010\u0010R\u001a\u00020SH\u0087\b¢\u0006\u0004\bT\u0010UJ\u0010\u0010V\u001a\u00020WH\u0087\b¢\u0006\u0004\bX\u0010YJ\u0010\u0010Z\u001a\u00020[H\u0087\b¢\u0006\u0004\b\\\u0010]J\u0010\u0010^\u001a\u00020\rH\u0087\b¢\u0006\u0004\b_\u0010-J\u0010\u0010`\u001a\u00020aH\u0087\b¢\u0006\u0004\bb\u0010cJ\u0010\u0010d\u001a\u00020\u0003H\u0087\b¢\u0006\u0004\be\u0010\u0005J\u000f\u0010f\u001a\u00020gH\u0016¢\u0006\u0004\bh\u0010iJ\u0016\u0010j\u001a\u00020\u000eH\u0087\bø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\bk\u0010UJ\u0016\u0010l\u001a\u00020\u0011H\u0087\bø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\bm\u0010-J\u0016\u0010n\u001a\u00020\u0014H\u0087\bø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\bo\u0010cJ\u0016\u0010p\u001a\u00020\u0000H\u0087\bø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\bq\u0010\u0005J\u001b\u0010r\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\fø\u0001\u0000¢\u0006\u0004\bs\u0010\u000bR\u0016\u0010\u0002\u001a\u00020\u00038\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0007\u0088\u0001\u0002\u0092\u0001\u00020\u0003ø\u0001\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!¨\u0006u" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/UShort;", "", "data", "", "constructor-impl", "(S)S", "getData$annotations", "()V", "and", "other", "and-xj2QHRw", "(SS)S", "compareTo", "", "Linfo/journeymap/shaded/kotlin/kotlin/UByte;", "compareTo-7apg3OU", "(SB)I", "Linfo/journeymap/shaded/kotlin/kotlin/UInt;", "compareTo-WZ4Q5Ns", "(SI)I", "Linfo/journeymap/shaded/kotlin/kotlin/ULong;", "compareTo-VKZWuLQ", "(SJ)I", "compareTo-xj2QHRw", "(SS)I", "dec", "dec-Mh2AYeg", "div", "div-7apg3OU", "div-WZ4Q5Ns", "div-VKZWuLQ", "(SJ)J", "div-xj2QHRw", "equals", "", "", "equals-impl", "(SLjava/lang/Object;)Z", "floorDiv", "floorDiv-7apg3OU", "floorDiv-WZ4Q5Ns", "floorDiv-VKZWuLQ", "floorDiv-xj2QHRw", "hashCode", "hashCode-impl", "(S)I", "inc", "inc-Mh2AYeg", "inv", "inv-Mh2AYeg", "minus", "minus-7apg3OU", "minus-WZ4Q5Ns", "minus-VKZWuLQ", "minus-xj2QHRw", "mod", "mod-7apg3OU", "(SB)B", "mod-WZ4Q5Ns", "mod-VKZWuLQ", "mod-xj2QHRw", "or", "or-xj2QHRw", "plus", "plus-7apg3OU", "plus-WZ4Q5Ns", "plus-VKZWuLQ", "plus-xj2QHRw", "rangeTo", "Linfo/journeymap/shaded/kotlin/kotlin/ranges/UIntRange;", "rangeTo-xj2QHRw", "(SS)Lkotlin/ranges/UIntRange;", "rem", "rem-7apg3OU", "rem-WZ4Q5Ns", "rem-VKZWuLQ", "rem-xj2QHRw", "times", "times-7apg3OU", "times-WZ4Q5Ns", "times-VKZWuLQ", "times-xj2QHRw", "toByte", "", "toByte-impl", "(S)B", "toDouble", "", "toDouble-impl", "(S)D", "toFloat", "", "toFloat-impl", "(S)F", "toInt", "toInt-impl", "toLong", "", "toLong-impl", "(S)J", "toShort", "toShort-impl", "toString", "", "toString-impl", "(S)Ljava/lang/String;", "toUByte", "toUByte-w2LRezQ", "toUInt", "toUInt-pVg5ArA", "toULong", "toULong-s-VKNKU", "toUShort", "toUShort-Mh2AYeg", "xor", "xor-xj2QHRw", "Companion", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
@SinceKotlin(version = "1.5")
@WasExperimental(markerClass = { ExperimentalUnsignedTypes.class })
public final class UShort implements Comparable<UShort> {

    @NotNull
    public static final UShort.Companion Companion = new UShort.Companion(null);

    private final short data;

    public static final short MIN_VALUE = 0;

    public static final short MAX_VALUE = -1;

    public static final int SIZE_BYTES = 2;

    public static final int SIZE_BITS = 16;

    @InlineOnly
    private static final int compareTo_7apg3OU(/* $VF was: compareTo-7apg3OU*/
    short arg0, byte other) {
        return Intrinsics.compare(arg0 & 65535, other & 0xFF);
    }

    @InlineOnly
    private static int compareTo_xj2QHRw(/* $VF was: compareTo-xj2QHRw*/
    short arg0, short other) {
        return Intrinsics.compare(arg0 & 65535, other & 65535);
    }

    @InlineOnly
    private int compareTo_xj2QHRw(/* $VF was: compareTo-xj2QHRw*/
    short other) {
        short var2 = this.unbox - impl();
        return Intrinsics.compare(var2 & 65535, other & 65535);
    }

    @InlineOnly
    private static final int compareTo_WZ4Q5Ns(/* $VF was: compareTo-WZ4Q5Ns*/
    short arg0, int other) {
        int var2 = UInt.constructor - impl(arg0 & '\uffff');
        return UnsignedKt.uintCompare(var2, other);
    }

    @InlineOnly
    private static final int compareTo_VKZWuLQ(/* $VF was: compareTo-VKZWuLQ*/
    short arg0, long other) {
        long var3 = ULong.constructor - impl((long) arg0 & 65535L);
        return UnsignedKt.ulongCompare(var3, other);
    }

    @InlineOnly
    private static final int plus_7apg3OU(/* $VF was: plus-7apg3OU*/
    short arg0, byte other) {
        int var2 = UInt.constructor - impl(arg0 & '\uffff');
        int var3 = UInt.constructor - impl(other & 255);
        return UInt.constructor - impl(var2 + var3);
    }

    @InlineOnly
    private static final int plus_xj2QHRw(/* $VF was: plus-xj2QHRw*/
    short arg0, short other) {
        int var2 = UInt.constructor - impl(arg0 & '\uffff');
        int var3 = UInt.constructor - impl(other & '\uffff');
        return UInt.constructor - impl(var2 + var3);
    }

    @InlineOnly
    private static final int plus_WZ4Q5Ns(/* $VF was: plus-WZ4Q5Ns*/
    short arg0, int other) {
        int var2 = UInt.constructor - impl(arg0 & '\uffff');
        return UInt.constructor - impl(var2 + other);
    }

    @InlineOnly
    private static final long plus_VKZWuLQ(/* $VF was: plus-VKZWuLQ*/
    short arg0, long other) {
        long var3 = ULong.constructor - impl((long) arg0 & 65535L);
        return ULong.constructor - impl(var3 + other);
    }

    @InlineOnly
    private static final int minus_7apg3OU(/* $VF was: minus-7apg3OU*/
    short arg0, byte other) {
        int var2 = UInt.constructor - impl(arg0 & '\uffff');
        int var3 = UInt.constructor - impl(other & 255);
        return UInt.constructor - impl(var2 - var3);
    }

    @InlineOnly
    private static final int minus_xj2QHRw(/* $VF was: minus-xj2QHRw*/
    short arg0, short other) {
        int var2 = UInt.constructor - impl(arg0 & '\uffff');
        int var3 = UInt.constructor - impl(other & '\uffff');
        return UInt.constructor - impl(var2 - var3);
    }

    @InlineOnly
    private static final int minus_WZ4Q5Ns(/* $VF was: minus-WZ4Q5Ns*/
    short arg0, int other) {
        int var2 = UInt.constructor - impl(arg0 & '\uffff');
        return UInt.constructor - impl(var2 - other);
    }

    @InlineOnly
    private static final long minus_VKZWuLQ(/* $VF was: minus-VKZWuLQ*/
    short arg0, long other) {
        long var3 = ULong.constructor - impl((long) arg0 & 65535L);
        return ULong.constructor - impl(var3 - other);
    }

    @InlineOnly
    private static final int times_7apg3OU(/* $VF was: times-7apg3OU*/
    short arg0, byte other) {
        int var2 = UInt.constructor - impl(arg0 & '\uffff');
        int var3 = UInt.constructor - impl(other & 255);
        return UInt.constructor - impl(var2 * var3);
    }

    @InlineOnly
    private static final int times_xj2QHRw(/* $VF was: times-xj2QHRw*/
    short arg0, short other) {
        int var2 = UInt.constructor - impl(arg0 & '\uffff');
        int var3 = UInt.constructor - impl(other & '\uffff');
        return UInt.constructor - impl(var2 * var3);
    }

    @InlineOnly
    private static final int times_WZ4Q5Ns(/* $VF was: times-WZ4Q5Ns*/
    short arg0, int other) {
        int var2 = UInt.constructor - impl(arg0 & '\uffff');
        return UInt.constructor - impl(var2 * other);
    }

    @InlineOnly
    private static final long times_VKZWuLQ(/* $VF was: times-VKZWuLQ*/
    short arg0, long other) {
        long var3 = ULong.constructor - impl((long) arg0 & 65535L);
        return ULong.constructor - impl(var3 * other);
    }

    @InlineOnly
    private static final int div_7apg3OU(/* $VF was: div-7apg3OU*/
    short arg0, byte other) {
        int var2 = UInt.constructor - impl(arg0 & '\uffff');
        int var3 = UInt.constructor - impl(other & 255);
        return UnsignedKt.uintDivide - J1ME1BU(var2, var3);
    }

    @InlineOnly
    private static final int div_xj2QHRw(/* $VF was: div-xj2QHRw*/
    short arg0, short other) {
        int var2 = UInt.constructor - impl(arg0 & '\uffff');
        int var3 = UInt.constructor - impl(other & '\uffff');
        return UnsignedKt.uintDivide - J1ME1BU(var2, var3);
    }

    @InlineOnly
    private static final int div_WZ4Q5Ns(/* $VF was: div-WZ4Q5Ns*/
    short arg0, int other) {
        int var2 = UInt.constructor - impl(arg0 & '\uffff');
        return UnsignedKt.uintDivide - J1ME1BU(var2, other);
    }

    @InlineOnly
    private static final long div_VKZWuLQ(/* $VF was: div-VKZWuLQ*/
    short arg0, long other) {
        long var3 = ULong.constructor - impl((long) arg0 & 65535L);
        return UnsignedKt.ulongDivide - eb3DHEI(var3, other);
    }

    @InlineOnly
    private static final int rem_7apg3OU(/* $VF was: rem-7apg3OU*/
    short arg0, byte other) {
        int var2 = UInt.constructor - impl(arg0 & '\uffff');
        int var3 = UInt.constructor - impl(other & 255);
        return UnsignedKt.uintRemainder - J1ME1BU(var2, var3);
    }

    @InlineOnly
    private static final int rem_xj2QHRw(/* $VF was: rem-xj2QHRw*/
    short arg0, short other) {
        int var2 = UInt.constructor - impl(arg0 & '\uffff');
        int var3 = UInt.constructor - impl(other & '\uffff');
        return UnsignedKt.uintRemainder - J1ME1BU(var2, var3);
    }

    @InlineOnly
    private static final int rem_WZ4Q5Ns(/* $VF was: rem-WZ4Q5Ns*/
    short arg0, int other) {
        int var2 = UInt.constructor - impl(arg0 & '\uffff');
        return UnsignedKt.uintRemainder - J1ME1BU(var2, other);
    }

    @InlineOnly
    private static final long rem_VKZWuLQ(/* $VF was: rem-VKZWuLQ*/
    short arg0, long other) {
        long var3 = ULong.constructor - impl((long) arg0 & 65535L);
        return UnsignedKt.ulongRemainder - eb3DHEI(var3, other);
    }

    @InlineOnly
    private static final int floorDiv_7apg3OU(/* $VF was: floorDiv-7apg3OU*/
    short arg0, byte other) {
        int var2 = UInt.constructor - impl(arg0 & '\uffff');
        int var3 = UInt.constructor - impl(other & 255);
        return UnsignedKt.uintDivide - J1ME1BU(var2, var3);
    }

    @InlineOnly
    private static final int floorDiv_xj2QHRw(/* $VF was: floorDiv-xj2QHRw*/
    short arg0, short other) {
        int var2 = UInt.constructor - impl(arg0 & '\uffff');
        int var3 = UInt.constructor - impl(other & '\uffff');
        return UnsignedKt.uintDivide - J1ME1BU(var2, var3);
    }

    @InlineOnly
    private static final int floorDiv_WZ4Q5Ns(/* $VF was: floorDiv-WZ4Q5Ns*/
    short arg0, int other) {
        int var2 = UInt.constructor - impl(arg0 & '\uffff');
        return UnsignedKt.uintDivide - J1ME1BU(var2, other);
    }

    @InlineOnly
    private static final long floorDiv_VKZWuLQ(/* $VF was: floorDiv-VKZWuLQ*/
    short arg0, long other) {
        long var3 = ULong.constructor - impl((long) arg0 & 65535L);
        return UnsignedKt.ulongDivide - eb3DHEI(var3, other);
    }

    @InlineOnly
    private static final byte mod_7apg3OU(/* $VF was: mod-7apg3OU*/
    short arg0, byte other) {
        int var2 = UInt.constructor - impl(arg0 & '\uffff');
        int var3 = UInt.constructor - impl(other & 255);
        var2 = UnsignedKt.uintRemainder - J1ME1BU(var2, var3);
        return UByte.constructor - impl((byte) var2);
    }

    @InlineOnly
    private static final short mod_xj2QHRw(/* $VF was: mod-xj2QHRw*/
    short arg0, short other) {
        int var2 = UInt.constructor - impl(arg0 & '\uffff');
        int var3 = UInt.constructor - impl(other & '\uffff');
        var2 = UnsignedKt.uintRemainder - J1ME1BU(var2, var3);
        return constructor - impl((short) var2);
    }

    @InlineOnly
    private static final int mod_WZ4Q5Ns(/* $VF was: mod-WZ4Q5Ns*/
    short arg0, int other) {
        int var2 = UInt.constructor - impl(arg0 & '\uffff');
        return UnsignedKt.uintRemainder - J1ME1BU(var2, other);
    }

    @InlineOnly
    private static final long mod_VKZWuLQ(/* $VF was: mod-VKZWuLQ*/
    short arg0, long other) {
        long var3 = ULong.constructor - impl((long) arg0 & 65535L);
        return UnsignedKt.ulongRemainder - eb3DHEI(var3, other);
    }

    @InlineOnly
    private static final short inc_Mh2AYeg(/* $VF was: inc-Mh2AYeg*/
    short arg0) {
        return constructor - impl((short) (arg0 + 1));
    }

    @InlineOnly
    private static final short dec_Mh2AYeg(/* $VF was: dec-Mh2AYeg*/
    short arg0) {
        return constructor - impl((short) (arg0 + -1));
    }

    @InlineOnly
    private static final UIntRange rangeTo_xj2QHRw(/* $VF was: rangeTo-xj2QHRw*/
    short arg0, short other) {
        return new UIntRange(UInt.constructor - impl(arg0 & '\uffff'), UInt.constructor - impl(other & '\uffff'), null);
    }

    @InlineOnly
    private static final short and_xj2QHRw(/* $VF was: and-xj2QHRw*/
    short arg0, short other) {
        return constructor - impl((short) (arg0 & other));
    }

    @InlineOnly
    private static final short or_xj2QHRw(/* $VF was: or-xj2QHRw*/
    short arg0, short other) {
        return constructor - impl((short) (arg0 | other));
    }

    @InlineOnly
    private static final short xor_xj2QHRw(/* $VF was: xor-xj2QHRw*/
    short arg0, short other) {
        return constructor - impl((short) (arg0 ^ other));
    }

    @InlineOnly
    private static final short inv_Mh2AYeg(/* $VF was: inv-Mh2AYeg*/
    short arg0) {
        return constructor - impl((short) (~arg0));
    }

    @InlineOnly
    private static final byte toByte_impl(/* $VF was: toByte-impl*/
    short arg0) {
        return (byte) arg0;
    }

    @InlineOnly
    private static final short toShort_impl(/* $VF was: toShort-impl*/
    short arg0) {
        return arg0;
    }

    @InlineOnly
    private static final int toInt_impl(/* $VF was: toInt-impl*/
    short arg0) {
        return arg0 & 65535;
    }

    @InlineOnly
    private static final long toLong_impl(/* $VF was: toLong-impl*/
    short arg0) {
        return (long) arg0 & 65535L;
    }

    @InlineOnly
    private static final byte toUByte_w2LRezQ(/* $VF was: toUByte-w2LRezQ*/
    short arg0) {
        return UByte.constructor - impl((byte) arg0);
    }

    @InlineOnly
    private static final short toUShort_Mh2AYeg(/* $VF was: toUShort-Mh2AYeg*/
    short arg0) {
        return arg0;
    }

    @InlineOnly
    private static final int toUInt_pVg5ArA(/* $VF was: toUInt-pVg5ArA*/
    short arg0) {
        return UInt.constructor - impl(arg0 & 65535);
    }

    @InlineOnly
    private static final long toULong_s_VKNKU(/* $VF was: toULong-s-VKNKU*/
    short arg0) {
        return ULong.constructor - impl((long) arg0 & 65535L);
    }

    @InlineOnly
    private static final float toFloat_impl(/* $VF was: toFloat-impl*/
    short arg0) {
        return (float) (arg0 & '\uffff');
    }

    @InlineOnly
    private static final double toDouble_impl(/* $VF was: toDouble-impl*/
    short arg0) {
        return (double) (arg0 & '\uffff');
    }

    @NotNull
    public static String toString_impl(/* $VF was: toString-impl*/
    short arg0) {
        return String.valueOf(arg0 & '\uffff');
    }

    @NotNull
    public String toString() {
        return toString - impl(this.data);
    }

    public static int hashCode_impl(/* $VF was: hashCode-impl*/
    short arg0) {
        return arg0;
    }

    public int hashCode() {
        return hashCode - impl(this.data);
    }

    public static boolean equals_impl(/* $VF was: equals-impl*/
    short arg0, Object other) {
        if (!(other instanceof UShort)) {
            return false;
        } else {
            short var2 = ((UShort) other).unbox - impl();
            return arg0 == var2;
        }
    }

    public boolean equals(Object other) {
        return equals - impl(this.data, other);
    }

    @PublishedApi
    public static short constructor_impl(/* $VF was: constructor-impl*/
    short data) {
        return data;
    }

    public static final boolean equals_impl0(/* $VF was: equals-impl0*/
    short p1, short p2) {
        return p1 == p2;
    }

    @Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0016\u0010\u0003\u001a\u00020\u0004X\u0086Tø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\n\u0002\u0010\u0005R\u0016\u0010\u0006\u001a\u00020\u0004X\u0086Tø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\n\u0002\u0010\u0005R\u000e\u0010\u0007\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!¨\u0006\n" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/UShort$Companion;", "", "()V", "MAX_VALUE", "Linfo/journeymap/shaded/kotlin/kotlin/UShort;", "S", "MIN_VALUE", "SIZE_BITS", "", "SIZE_BYTES", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
    public static final class Companion {

        private Companion() {
        }
    }
}