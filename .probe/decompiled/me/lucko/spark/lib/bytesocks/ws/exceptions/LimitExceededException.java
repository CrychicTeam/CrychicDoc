package me.lucko.spark.lib.bytesocks.ws.exceptions;

public class LimitExceededException extends InvalidDataException {

    private static final long serialVersionUID = 6908339749836826785L;

    private final int limit;

    public LimitExceededException() {
        this(Integer.MAX_VALUE);
    }

    public LimitExceededException(int limit) {
        super(1009);
        this.limit = limit;
    }

    public LimitExceededException(String s, int limit) {
        super(1009, s);
        this.limit = limit;
    }

    public LimitExceededException(String s) {
        this(s, Integer.MAX_VALUE);
    }

    public int getLimit() {
        return this.limit;
    }
}