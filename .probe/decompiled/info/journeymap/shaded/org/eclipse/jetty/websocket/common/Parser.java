package info.journeymap.shaded.org.eclipse.jetty.websocket.common;

import info.journeymap.shaded.org.eclipse.jetty.io.ByteBufferPool;
import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.MessageTooLargeException;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.ProtocolException;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketBehavior;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketException;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketPolicy;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Extension;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Frame;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.frames.BinaryFrame;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.frames.CloseFrame;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.frames.ContinuationFrame;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.frames.PingFrame;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.frames.PongFrame;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.frames.TextFrame;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.io.payload.DeMaskProcessor;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.io.payload.PayloadProcessor;
import java.nio.ByteBuffer;
import java.util.List;

public class Parser {

    private static final Logger LOG = Log.getLogger(Parser.class);

    private final WebSocketPolicy policy;

    private final ByteBufferPool bufferPool;

    private Parser.State state = Parser.State.START;

    private int cursor = 0;

    private WebSocketFrame frame;

    private boolean priorDataFrame;

    private ByteBuffer payload;

    private int payloadLength;

    private PayloadProcessor maskProcessor = new DeMaskProcessor();

    private byte flagsInUse = 0;

    private IncomingFrames incomingFramesHandler;

    public Parser(WebSocketPolicy wspolicy, ByteBufferPool bufferPool) {
        this.bufferPool = bufferPool;
        this.policy = wspolicy;
    }

    private void assertSanePayloadLength(long len) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} Payload Length: {} - {}", this.policy.getBehavior(), len, this);
        }
        if (len > 2147483647L) {
            throw new MessageTooLargeException("[int-sane!] cannot handle payload lengths larger than 2147483647");
        } else {
            switch(this.frame.getOpCode()) {
                case 1:
                    this.policy.assertValidTextMessageSize((int) len);
                    break;
                case 2:
                    this.policy.assertValidBinaryMessageSize((int) len);
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                default:
                    break;
                case 8:
                    if (len == 1L) {
                        throw new ProtocolException("Invalid close frame payload length, [" + this.payloadLength + "]");
                    }
                case 9:
                case 10:
                    if (len > 125L) {
                        throw new ProtocolException("Invalid control frame payload length, [" + this.payloadLength + "] cannot exceed [" + 125 + "]");
                    }
            }
        }
    }

    public void configureFromExtensions(List<? extends Extension> exts) {
        this.flagsInUse = 0;
        for (Extension ext : exts) {
            if (ext.isRsv1User()) {
                this.flagsInUse = (byte) (this.flagsInUse | 64);
            }
            if (ext.isRsv2User()) {
                this.flagsInUse = (byte) (this.flagsInUse | 32);
            }
            if (ext.isRsv3User()) {
                this.flagsInUse = (byte) (this.flagsInUse | 16);
            }
        }
    }

    public IncomingFrames getIncomingFramesHandler() {
        return this.incomingFramesHandler;
    }

    public WebSocketPolicy getPolicy() {
        return this.policy;
    }

    public boolean isRsv1InUse() {
        return (this.flagsInUse & 64) != 0;
    }

    public boolean isRsv2InUse() {
        return (this.flagsInUse & 32) != 0;
    }

    public boolean isRsv3InUse() {
        return (this.flagsInUse & 16) != 0;
    }

    protected void notifyFrame(Frame f) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} Notify {}", this.policy.getBehavior(), this.getIncomingFramesHandler());
        }
        if (this.policy.getBehavior() == WebSocketBehavior.SERVER) {
            if (!f.isMasked()) {
                throw new ProtocolException("Client MUST mask all frames (RFC-6455: Section 5.1)");
            }
        } else if (this.policy.getBehavior() == WebSocketBehavior.CLIENT && f.isMasked()) {
            throw new ProtocolException("Server MUST NOT mask any frames (RFC-6455: Section 5.1)");
        }
        if (this.incomingFramesHandler != null) {
            try {
                this.incomingFramesHandler.incomingFrame(f);
            } catch (WebSocketException var3) {
                this.notifyWebSocketException(var3);
            } catch (Throwable var4) {
                LOG.warn(var4);
                this.notifyWebSocketException(new WebSocketException(var4));
            }
        }
    }

    protected void notifyWebSocketException(WebSocketException e) {
        LOG.warn(e);
        if (this.incomingFramesHandler != null) {
            this.incomingFramesHandler.incomingError(e);
        }
    }

    public void parse(ByteBuffer buffer) throws WebSocketException {
        if (buffer.remaining() > 0) {
            try {
                while (this.parseFrame(buffer)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("{} Parsed Frame: {}", this.policy.getBehavior(), this.frame);
                    }
                    this.notifyFrame(this.frame);
                    if (this.frame.isDataFrame()) {
                        this.priorDataFrame = !this.frame.isFin();
                    }
                    this.reset();
                }
            } catch (WebSocketException var4) {
                buffer.position(buffer.limit());
                this.reset();
                this.notifyWebSocketException(var4);
                throw var4;
            } catch (Throwable var5) {
                buffer.position(buffer.limit());
                this.reset();
                WebSocketException e = new WebSocketException(var5);
                this.notifyWebSocketException(e);
                throw e;
            }
        }
    }

    private void reset() {
        if (this.frame != null) {
            this.frame.reset();
        }
        this.frame = null;
        this.bufferPool.release(this.payload);
        this.payload = null;
    }

    private boolean parseFrame(ByteBuffer buffer) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} Parsing {} bytes", this.policy.getBehavior(), buffer.remaining());
        }
        while (buffer.hasRemaining()) {
            switch(this.state) {
                case START:
                    byte bxxx = buffer.get();
                    boolean fin = (bxxx & 128) != 0;
                    byte opcode = (byte) (bxxx & 15);
                    if (!OpCode.isKnown(opcode)) {
                        throw new ProtocolException("Unknown opcode: " + opcode);
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("{} OpCode {}, fin={} rsv={}{}{}", this.policy.getBehavior(), OpCode.name(opcode), fin, Character.valueOf((char) ((bxxx & 64) != 0 ? '1' : '.')), Character.valueOf((char) ((bxxx & 32) != 0 ? '1' : '.')), Character.valueOf((char) ((bxxx & 16) != 0 ? '1' : '.')));
                    }
                    switch(opcode) {
                        case 0:
                            this.frame = new ContinuationFrame();
                            if (!this.priorDataFrame) {
                                throw new ProtocolException("CONTINUATION frame without prior !FIN");
                            }
                            break;
                        case 1:
                            this.frame = new TextFrame();
                            if (this.priorDataFrame) {
                                throw new ProtocolException("Unexpected " + OpCode.name(opcode) + " frame, was expecting CONTINUATION");
                            }
                            break;
                        case 2:
                            this.frame = new BinaryFrame();
                            if (this.priorDataFrame) {
                                throw new ProtocolException("Unexpected " + OpCode.name(opcode) + " frame, was expecting CONTINUATION");
                            }
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        default:
                            break;
                        case 8:
                            this.frame = new CloseFrame();
                            if (!fin) {
                                throw new ProtocolException("Fragmented Close Frame [" + OpCode.name(opcode) + "]");
                            }
                            break;
                        case 9:
                            this.frame = new PingFrame();
                            if (!fin) {
                                throw new ProtocolException("Fragmented Ping Frame [" + OpCode.name(opcode) + "]");
                            }
                            break;
                        case 10:
                            this.frame = new PongFrame();
                            if (!fin) {
                                throw new ProtocolException("Fragmented Pong Frame [" + OpCode.name(opcode) + "]");
                            }
                    }
                    this.frame.setFin(fin);
                    if ((bxxx & 112) != 0) {
                        if ((bxxx & 64) != 0) {
                            if (!this.isRsv1InUse()) {
                                String err = "RSV1 not allowed to be set";
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug(err + ": Remaining buffer: {}", BufferUtil.toDetailString(buffer));
                                }
                                throw new ProtocolException(err);
                            }
                            this.frame.setRsv1(true);
                        }
                        if ((bxxx & 32) != 0) {
                            if (!this.isRsv2InUse()) {
                                String err = "RSV2 not allowed to be set";
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug(err + ": Remaining buffer: {}", BufferUtil.toDetailString(buffer));
                                }
                                throw new ProtocolException(err);
                            }
                            this.frame.setRsv2(true);
                        }
                        if ((bxxx & 16) != 0) {
                            if (!this.isRsv3InUse()) {
                                String err = "RSV3 not allowed to be set";
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug(err + ": Remaining buffer: {}", BufferUtil.toDetailString(buffer));
                                }
                                throw new ProtocolException(err);
                            }
                            this.frame.setRsv3(true);
                        }
                    }
                    this.state = Parser.State.PAYLOAD_LEN;
                    break;
                case PAYLOAD_LEN:
                    byte bxx = buffer.get();
                    this.frame.setMasked((bxx & 128) != 0);
                    this.payloadLength = (byte) (127 & bxx);
                    if (this.payloadLength == 127) {
                        this.payloadLength = 0;
                        this.state = Parser.State.PAYLOAD_LEN_BYTES;
                        this.cursor = 8;
                    } else if (this.payloadLength == 126) {
                        this.payloadLength = 0;
                        this.state = Parser.State.PAYLOAD_LEN_BYTES;
                        this.cursor = 2;
                    } else {
                        this.assertSanePayloadLength((long) this.payloadLength);
                        if (this.frame.isMasked()) {
                            this.state = Parser.State.MASK;
                        } else {
                            if (this.payloadLength == 0) {
                                this.state = Parser.State.START;
                                return true;
                            }
                            this.maskProcessor.reset(this.frame);
                            this.state = Parser.State.PAYLOAD;
                        }
                    }
                    break;
                case PAYLOAD_LEN_BYTES:
                    byte bx = buffer.get();
                    this.cursor--;
                    this.payloadLength = this.payloadLength | (bx & 255) << 8 * this.cursor;
                    if (this.cursor == 0) {
                        this.assertSanePayloadLength((long) this.payloadLength);
                        if (this.frame.isMasked()) {
                            this.state = Parser.State.MASK;
                        } else {
                            if (this.payloadLength == 0) {
                                this.state = Parser.State.START;
                                return true;
                            }
                            this.maskProcessor.reset(this.frame);
                            this.state = Parser.State.PAYLOAD;
                        }
                    }
                    break;
                case MASK:
                    byte[] m = new byte[4];
                    this.frame.setMask(m);
                    if (buffer.remaining() >= 4) {
                        buffer.get(m, 0, 4);
                        if (this.payloadLength == 0) {
                            this.state = Parser.State.START;
                            return true;
                        }
                        this.maskProcessor.reset(this.frame);
                        this.state = Parser.State.PAYLOAD;
                    } else {
                        this.state = Parser.State.MASK_BYTES;
                        this.cursor = 4;
                    }
                    break;
                case MASK_BYTES:
                    byte b = buffer.get();
                    this.frame.getMask()[4 - this.cursor] = b;
                    this.cursor--;
                    if (this.cursor == 0) {
                        if (this.payloadLength == 0) {
                            this.state = Parser.State.START;
                            return true;
                        }
                        this.maskProcessor.reset(this.frame);
                        this.state = Parser.State.PAYLOAD;
                    }
                    break;
                case PAYLOAD:
                    this.frame.assertValid();
                    if (this.parsePayload(buffer)) {
                        if (this.frame.getOpCode() == 8) {
                            new CloseInfo(this.frame);
                        }
                        this.state = Parser.State.START;
                        return true;
                    }
            }
        }
        return false;
    }

    private boolean parsePayload(ByteBuffer buffer) {
        if (this.payloadLength == 0) {
            return true;
        } else {
            if (buffer.hasRemaining()) {
                int bytesSoFar = this.payload == null ? 0 : this.payload.position();
                int bytesExpected = this.payloadLength - bytesSoFar;
                int bytesAvailable = buffer.remaining();
                int windowBytes = Math.min(bytesAvailable, bytesExpected);
                int limit = buffer.limit();
                buffer.limit(buffer.position() + windowBytes);
                ByteBuffer window = buffer.slice();
                buffer.limit(limit);
                buffer.position(buffer.position() + window.remaining());
                if (LOG.isDebugEnabled()) {
                    LOG.debug("{} Window: {}", this.policy.getBehavior(), BufferUtil.toDetailString(window));
                }
                this.maskProcessor.process(window);
                if (window.remaining() == this.payloadLength) {
                    this.frame.setPayload(window);
                    return true;
                }
                if (this.payload == null) {
                    this.payload = this.bufferPool.acquire(this.payloadLength, false);
                    BufferUtil.clearToFill(this.payload);
                }
                this.payload.put(window);
                if (this.payload.position() == this.payloadLength) {
                    BufferUtil.flipToFlush(this.payload, 0);
                    this.frame.setPayload(this.payload);
                    return true;
                }
            }
            return false;
        }
    }

    public void setIncomingFramesHandler(IncomingFrames incoming) {
        this.incomingFramesHandler = incoming;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Parser@").append(Integer.toHexString(this.hashCode()));
        builder.append("[");
        if (this.incomingFramesHandler == null) {
            builder.append("NO_HANDLER");
        } else {
            builder.append(this.incomingFramesHandler.getClass().getSimpleName());
        }
        builder.append(",s=").append(this.state);
        builder.append(",c=").append(this.cursor);
        builder.append(",len=").append(this.payloadLength);
        builder.append(",f=").append(this.frame);
        builder.append("]");
        return builder.toString();
    }

    private static enum State {

        START,
        PAYLOAD_LEN,
        PAYLOAD_LEN_BYTES,
        MASK,
        MASK_BYTES,
        PAYLOAD
    }
}