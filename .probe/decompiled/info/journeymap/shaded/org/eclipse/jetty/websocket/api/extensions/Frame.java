package info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions;

import java.nio.ByteBuffer;

public interface Frame {

    byte[] getMask();

    byte getOpCode();

    ByteBuffer getPayload();

    int getPayloadLength();

    Frame.Type getType();

    boolean hasPayload();

    boolean isFin();

    @Deprecated
    boolean isLast();

    boolean isMasked();

    boolean isRsv1();

    boolean isRsv2();

    boolean isRsv3();

    public static enum Type {

        CONTINUATION((byte) 0),
        TEXT((byte) 1),
        BINARY((byte) 2),
        CLOSE((byte) 8),
        PING((byte) 9),
        PONG((byte) 10);

        private byte opcode;

        public static Frame.Type from(byte op) {
            for (Frame.Type type : values()) {
                if (type.opcode == op) {
                    return type;
                }
            }
            throw new IllegalArgumentException("OpCode " + op + " is not a valid Frame.Type");
        }

        private Type(byte code) {
            this.opcode = code;
        }

        public byte getOpCode() {
            return this.opcode;
        }

        public boolean isControl() {
            return this.opcode >= CLOSE.getOpCode();
        }

        public boolean isData() {
            return this.opcode == TEXT.getOpCode() || this.opcode == BINARY.getOpCode();
        }

        public boolean isContinuation() {
            return this.opcode == CONTINUATION.getOpCode();
        }

        public String toString() {
            return this.name();
        }
    }
}