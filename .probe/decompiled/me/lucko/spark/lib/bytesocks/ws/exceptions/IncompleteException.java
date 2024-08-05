package me.lucko.spark.lib.bytesocks.ws.exceptions;

public class IncompleteException extends Exception {

    private static final long serialVersionUID = 7330519489840500997L;

    private final int preferredSize;

    public IncompleteException(int preferredSize) {
        this.preferredSize = preferredSize;
    }

    public int getPreferredSize() {
        return this.preferredSize;
    }
}