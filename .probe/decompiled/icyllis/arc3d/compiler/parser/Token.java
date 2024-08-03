package icyllis.arc3d.compiler.parser;

public final class Token {

    public static long make(int kind, int offset, int length) {
        assert kind >= 0 && kind <= 65535;
        assert offset >= 0 && offset <= 16777215;
        assert length >= 0 && length <= 16777215;
        long token = (long) kind | (long) offset << 16 | (long) length << 40;
        assert token != -1L;
        return token;
    }

    public static int kind(long token) {
        return (int) (token & 65535L);
    }

    public static int offset(long token) {
        return (int) (token >> 16) & 16777215;
    }

    public static int length(long token) {
        return (int) (token >>> 40);
    }

    public static long replace(long token, int kind) {
        assert kind >= 0 && kind <= 65535;
        return token & -65536L | (long) kind;
    }

    private Token() {
    }
}