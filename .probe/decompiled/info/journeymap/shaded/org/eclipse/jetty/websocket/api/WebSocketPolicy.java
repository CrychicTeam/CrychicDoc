package info.journeymap.shaded.org.eclipse.jetty.websocket.api;

public class WebSocketPolicy {

    private static final int KB = 1024;

    private int maxTextMessageSize = 65536;

    private int maxTextMessageBufferSize = 32768;

    private int maxBinaryMessageSize = 65536;

    private int maxBinaryMessageBufferSize = 32768;

    private long asyncWriteTimeout = 60000L;

    private long idleTimeout = 300000L;

    private int inputBufferSize = 4096;

    private final WebSocketBehavior behavior;

    public static WebSocketPolicy newClientPolicy() {
        return new WebSocketPolicy(WebSocketBehavior.CLIENT);
    }

    public static WebSocketPolicy newServerPolicy() {
        return new WebSocketPolicy(WebSocketBehavior.SERVER);
    }

    public WebSocketPolicy(WebSocketBehavior behavior) {
        this.behavior = behavior;
    }

    private void assertLessThan(String name, long size, String otherName, long otherSize) {
        if (size > otherSize) {
            throw new IllegalArgumentException(String.format("%s [%d] must be less than %s [%d]", name, size, otherName, otherSize));
        }
    }

    private void assertGreaterThan(String name, long size, long minSize) {
        if (size < minSize) {
            throw new IllegalArgumentException(String.format("%s [%d] must be a greater than or equal to " + minSize, name, size));
        }
    }

    public void assertValidBinaryMessageSize(int requestedSize) {
        if (this.maxBinaryMessageSize > 0 && requestedSize > this.maxBinaryMessageSize) {
            throw new MessageTooLargeException("Binary message size [" + requestedSize + "] exceeds maximum size [" + this.maxBinaryMessageSize + "]");
        }
    }

    public void assertValidTextMessageSize(int requestedSize) {
        if (this.maxTextMessageSize > 0 && requestedSize > this.maxTextMessageSize) {
            throw new MessageTooLargeException("Text message size [" + requestedSize + "] exceeds maximum size [" + this.maxTextMessageSize + "]");
        }
    }

    public WebSocketPolicy clonePolicy() {
        WebSocketPolicy clone = new WebSocketPolicy(this.behavior);
        clone.idleTimeout = this.idleTimeout;
        clone.maxTextMessageSize = this.maxTextMessageSize;
        clone.maxTextMessageBufferSize = this.maxTextMessageBufferSize;
        clone.maxBinaryMessageSize = this.maxBinaryMessageSize;
        clone.maxBinaryMessageBufferSize = this.maxBinaryMessageBufferSize;
        clone.inputBufferSize = this.inputBufferSize;
        clone.asyncWriteTimeout = this.asyncWriteTimeout;
        return clone;
    }

    public long getAsyncWriteTimeout() {
        return this.asyncWriteTimeout;
    }

    public WebSocketBehavior getBehavior() {
        return this.behavior;
    }

    public long getIdleTimeout() {
        return this.idleTimeout;
    }

    public int getInputBufferSize() {
        return this.inputBufferSize;
    }

    public int getMaxBinaryMessageBufferSize() {
        return this.maxBinaryMessageBufferSize;
    }

    public int getMaxBinaryMessageSize() {
        return this.maxBinaryMessageSize;
    }

    public int getMaxTextMessageBufferSize() {
        return this.maxTextMessageBufferSize;
    }

    public int getMaxTextMessageSize() {
        return this.maxTextMessageSize;
    }

    public void setAsyncWriteTimeout(long ms) {
        this.assertLessThan("AsyncWriteTimeout", ms, "IdleTimeout", this.idleTimeout);
        this.asyncWriteTimeout = ms;
    }

    public void setIdleTimeout(long ms) {
        this.assertGreaterThan("IdleTimeout", ms, 0L);
        this.idleTimeout = ms;
    }

    public void setInputBufferSize(int size) {
        this.assertGreaterThan("InputBufferSize", (long) size, 1L);
        this.assertLessThan("InputBufferSize", (long) size, "MaxTextMessageBufferSize", (long) this.maxTextMessageBufferSize);
        this.assertLessThan("InputBufferSize", (long) size, "MaxBinaryMessageBufferSize", (long) this.maxBinaryMessageBufferSize);
        this.inputBufferSize = size;
    }

    public void setMaxBinaryMessageBufferSize(int size) {
        this.assertGreaterThan("MaxBinaryMessageBufferSize", (long) size, 1L);
        this.maxBinaryMessageBufferSize = size;
    }

    public void setMaxBinaryMessageSize(int size) {
        this.assertGreaterThan("MaxBinaryMessageSize", (long) size, 1L);
        this.maxBinaryMessageSize = size;
    }

    public void setMaxTextMessageBufferSize(int size) {
        this.assertGreaterThan("MaxTextMessageBufferSize", (long) size, 1L);
        this.maxTextMessageBufferSize = size;
    }

    public void setMaxTextMessageSize(int size) {
        this.assertGreaterThan("MaxTextMessageSize", (long) size, 1L);
        this.maxTextMessageSize = size;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("WebSocketPolicy@").append(Integer.toHexString(this.hashCode()));
        builder.append("[behavior=").append(this.behavior);
        builder.append(",maxTextMessageSize=").append(this.maxTextMessageSize);
        builder.append(",maxTextMessageBufferSize=").append(this.maxTextMessageBufferSize);
        builder.append(",maxBinaryMessageSize=").append(this.maxBinaryMessageSize);
        builder.append(",maxBinaryMessageBufferSize=").append(this.maxBinaryMessageBufferSize);
        builder.append(",asyncWriteTimeout=").append(this.asyncWriteTimeout);
        builder.append(",idleTimeout=").append(this.idleTimeout);
        builder.append(",inputBufferSize=").append(this.inputBufferSize);
        builder.append("]");
        return builder.toString();
    }
}