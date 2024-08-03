package me.lucko.spark.lib.bytesocks.ws.exceptions;

import java.io.IOException;
import me.lucko.spark.lib.bytesocks.ws.WebSocket;

public class WrappedIOException extends Exception {

    private final transient WebSocket connection;

    private final IOException ioException;

    public WrappedIOException(WebSocket connection, IOException ioException) {
        this.connection = connection;
        this.ioException = ioException;
    }

    public WebSocket getConnection() {
        return this.connection;
    }

    public IOException getIOException() {
        return this.ioException;
    }
}