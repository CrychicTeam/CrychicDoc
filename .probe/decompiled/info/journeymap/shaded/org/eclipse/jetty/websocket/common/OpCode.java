package info.journeymap.shaded.org.eclipse.jetty.websocket.common;

public final class OpCode {

    public static final byte CONTINUATION = 0;

    public static final byte TEXT = 1;

    public static final byte BINARY = 2;

    public static final byte CLOSE = 8;

    public static final byte PING = 9;

    public static final byte PONG = 10;

    public static final byte UNDEFINED = -1;

    public static boolean isControlFrame(byte opcode) {
        return opcode >= 8;
    }

    public static boolean isDataFrame(byte opcode) {
        return opcode == 1 || opcode == 2;
    }

    public static boolean isKnown(byte opcode) {
        return opcode == 0 || opcode == 1 || opcode == 2 || opcode == 8 || opcode == 9 || opcode == 10;
    }

    public static String name(byte opcode) {
        switch(opcode) {
            case -1:
                return "NO-OP";
            case 0:
                return "CONTINUATION";
            case 1:
                return "TEXT";
            case 2:
                return "BINARY";
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            default:
                return "NON-SPEC[" + opcode + "]";
            case 8:
                return "CLOSE";
            case 9:
                return "PING";
            case 10:
                return "PONG";
        }
    }
}