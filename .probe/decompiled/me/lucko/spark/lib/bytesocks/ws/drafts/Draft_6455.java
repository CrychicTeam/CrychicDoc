package me.lucko.spark.lib.bytesocks.ws.drafts;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import me.lucko.spark.lib.bytesocks.ws.WebSocketImpl;
import me.lucko.spark.lib.bytesocks.ws.enums.CloseHandshakeType;
import me.lucko.spark.lib.bytesocks.ws.enums.HandshakeState;
import me.lucko.spark.lib.bytesocks.ws.enums.Opcode;
import me.lucko.spark.lib.bytesocks.ws.enums.ReadyState;
import me.lucko.spark.lib.bytesocks.ws.enums.Role;
import me.lucko.spark.lib.bytesocks.ws.exceptions.IncompleteException;
import me.lucko.spark.lib.bytesocks.ws.exceptions.InvalidDataException;
import me.lucko.spark.lib.bytesocks.ws.exceptions.InvalidFrameException;
import me.lucko.spark.lib.bytesocks.ws.exceptions.InvalidHandshakeException;
import me.lucko.spark.lib.bytesocks.ws.exceptions.LimitExceededException;
import me.lucko.spark.lib.bytesocks.ws.exceptions.NotSendableException;
import me.lucko.spark.lib.bytesocks.ws.extensions.DefaultExtension;
import me.lucko.spark.lib.bytesocks.ws.extensions.IExtension;
import me.lucko.spark.lib.bytesocks.ws.framing.BinaryFrame;
import me.lucko.spark.lib.bytesocks.ws.framing.CloseFrame;
import me.lucko.spark.lib.bytesocks.ws.framing.Framedata;
import me.lucko.spark.lib.bytesocks.ws.framing.FramedataImpl1;
import me.lucko.spark.lib.bytesocks.ws.framing.TextFrame;
import me.lucko.spark.lib.bytesocks.ws.handshake.ClientHandshake;
import me.lucko.spark.lib.bytesocks.ws.handshake.ClientHandshakeBuilder;
import me.lucko.spark.lib.bytesocks.ws.handshake.HandshakeBuilder;
import me.lucko.spark.lib.bytesocks.ws.handshake.ServerHandshake;
import me.lucko.spark.lib.bytesocks.ws.handshake.ServerHandshakeBuilder;
import me.lucko.spark.lib.bytesocks.ws.protocols.IProtocol;
import me.lucko.spark.lib.bytesocks.ws.protocols.Protocol;
import me.lucko.spark.lib.bytesocks.ws.util.Base64;
import me.lucko.spark.lib.bytesocks.ws.util.Charsetfunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Draft_6455 extends Draft {

    private static final String SEC_WEB_SOCKET_KEY = "Sec-WebSocket-Key";

    private static final String SEC_WEB_SOCKET_PROTOCOL = "Sec-WebSocket-Protocol";

    private static final String SEC_WEB_SOCKET_EXTENSIONS = "Sec-WebSocket-Extensions";

    private static final String SEC_WEB_SOCKET_ACCEPT = "Sec-WebSocket-Accept";

    private static final String UPGRADE = "Upgrade";

    private static final String CONNECTION = "Connection";

    private final Logger log = LoggerFactory.getLogger(Draft_6455.class);

    private IExtension negotiatedExtension = new DefaultExtension();

    private IExtension defaultExtension = new DefaultExtension();

    private List<IExtension> knownExtensions;

    private IExtension currentDecodingExtension;

    private IProtocol protocol;

    private List<IProtocol> knownProtocols;

    private Framedata currentContinuousFrame;

    private final List<ByteBuffer> byteBufferList;

    private ByteBuffer incompleteframe;

    private final SecureRandom reuseableRandom = new SecureRandom();

    private int maxFrameSize;

    public Draft_6455() {
        this(Collections.emptyList());
    }

    public Draft_6455(IExtension inputExtension) {
        this(Collections.singletonList(inputExtension));
    }

    public Draft_6455(List<IExtension> inputExtensions) {
        this(inputExtensions, Collections.singletonList(new Protocol("")));
    }

    public Draft_6455(List<IExtension> inputExtensions, List<IProtocol> inputProtocols) {
        this(inputExtensions, inputProtocols, Integer.MAX_VALUE);
    }

    public Draft_6455(List<IExtension> inputExtensions, int inputMaxFrameSize) {
        this(inputExtensions, Collections.singletonList(new Protocol("")), inputMaxFrameSize);
    }

    public Draft_6455(List<IExtension> inputExtensions, List<IProtocol> inputProtocols, int inputMaxFrameSize) {
        if (inputExtensions != null && inputProtocols != null && inputMaxFrameSize >= 1) {
            this.knownExtensions = new ArrayList(inputExtensions.size());
            this.knownProtocols = new ArrayList(inputProtocols.size());
            boolean hasDefault = false;
            this.byteBufferList = new ArrayList();
            for (IExtension inputExtension : inputExtensions) {
                if (inputExtension.getClass().equals(DefaultExtension.class)) {
                    hasDefault = true;
                }
            }
            this.knownExtensions.addAll(inputExtensions);
            if (!hasDefault) {
                this.knownExtensions.add(this.knownExtensions.size(), this.negotiatedExtension);
            }
            this.knownProtocols.addAll(inputProtocols);
            this.maxFrameSize = inputMaxFrameSize;
            this.currentDecodingExtension = null;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public HandshakeState acceptHandshakeAsServer(ClientHandshake handshakedata) throws InvalidHandshakeException {
        int v = this.readVersion(handshakedata);
        if (v != 13) {
            this.log.trace("acceptHandshakeAsServer - Wrong websocket version.");
            return HandshakeState.NOT_MATCHED;
        } else {
            HandshakeState extensionState = HandshakeState.NOT_MATCHED;
            String requestedExtension = handshakedata.getFieldValue("Sec-WebSocket-Extensions");
            for (IExtension knownExtension : this.knownExtensions) {
                if (knownExtension.acceptProvidedExtensionAsServer(requestedExtension)) {
                    this.negotiatedExtension = knownExtension;
                    extensionState = HandshakeState.MATCHED;
                    this.log.trace("acceptHandshakeAsServer - Matching extension found: {}", this.negotiatedExtension);
                    break;
                }
            }
            HandshakeState protocolState = this.containsRequestedProtocol(handshakedata.getFieldValue("Sec-WebSocket-Protocol"));
            if (protocolState == HandshakeState.MATCHED && extensionState == HandshakeState.MATCHED) {
                return HandshakeState.MATCHED;
            } else {
                this.log.trace("acceptHandshakeAsServer - No matching extension or protocol found.");
                return HandshakeState.NOT_MATCHED;
            }
        }
    }

    private HandshakeState containsRequestedProtocol(String requestedProtocol) {
        for (IProtocol knownProtocol : this.knownProtocols) {
            if (knownProtocol.acceptProvidedProtocol(requestedProtocol)) {
                this.protocol = knownProtocol;
                this.log.trace("acceptHandshake - Matching protocol found: {}", this.protocol);
                return HandshakeState.MATCHED;
            }
        }
        return HandshakeState.NOT_MATCHED;
    }

    @Override
    public HandshakeState acceptHandshakeAsClient(ClientHandshake request, ServerHandshake response) throws InvalidHandshakeException {
        if (!this.basicAccept(response)) {
            this.log.trace("acceptHandshakeAsClient - Missing/wrong upgrade or connection in handshake.");
            return HandshakeState.NOT_MATCHED;
        } else if (request.hasFieldValue("Sec-WebSocket-Key") && response.hasFieldValue("Sec-WebSocket-Accept")) {
            String seckeyAnswer = response.getFieldValue("Sec-WebSocket-Accept");
            String seckeyChallenge = request.getFieldValue("Sec-WebSocket-Key");
            seckeyChallenge = this.generateFinalKey(seckeyChallenge);
            if (!seckeyChallenge.equals(seckeyAnswer)) {
                this.log.trace("acceptHandshakeAsClient - Wrong key for Sec-WebSocket-Key.");
                return HandshakeState.NOT_MATCHED;
            } else {
                HandshakeState extensionState = HandshakeState.NOT_MATCHED;
                String requestedExtension = response.getFieldValue("Sec-WebSocket-Extensions");
                for (IExtension knownExtension : this.knownExtensions) {
                    if (knownExtension.acceptProvidedExtensionAsClient(requestedExtension)) {
                        this.negotiatedExtension = knownExtension;
                        extensionState = HandshakeState.MATCHED;
                        this.log.trace("acceptHandshakeAsClient - Matching extension found: {}", this.negotiatedExtension);
                        break;
                    }
                }
                HandshakeState protocolState = this.containsRequestedProtocol(response.getFieldValue("Sec-WebSocket-Protocol"));
                if (protocolState == HandshakeState.MATCHED && extensionState == HandshakeState.MATCHED) {
                    return HandshakeState.MATCHED;
                } else {
                    this.log.trace("acceptHandshakeAsClient - No matching extension or protocol found.");
                    return HandshakeState.NOT_MATCHED;
                }
            }
        } else {
            this.log.trace("acceptHandshakeAsClient - Missing Sec-WebSocket-Key or Sec-WebSocket-Accept");
            return HandshakeState.NOT_MATCHED;
        }
    }

    public IExtension getExtension() {
        return this.negotiatedExtension;
    }

    public List<IExtension> getKnownExtensions() {
        return this.knownExtensions;
    }

    public IProtocol getProtocol() {
        return this.protocol;
    }

    public int getMaxFrameSize() {
        return this.maxFrameSize;
    }

    public List<IProtocol> getKnownProtocols() {
        return this.knownProtocols;
    }

    @Override
    public ClientHandshakeBuilder postProcessHandshakeRequestAsClient(ClientHandshakeBuilder request) {
        request.put("Upgrade", "websocket");
        request.put("Connection", "Upgrade");
        byte[] random = new byte[16];
        this.reuseableRandom.nextBytes(random);
        request.put("Sec-WebSocket-Key", Base64.encodeBytes(random));
        request.put("Sec-WebSocket-Version", "13");
        StringBuilder requestedExtensions = new StringBuilder();
        for (IExtension knownExtension : this.knownExtensions) {
            if (knownExtension.getProvidedExtensionAsClient() != null && knownExtension.getProvidedExtensionAsClient().length() != 0) {
                if (requestedExtensions.length() > 0) {
                    requestedExtensions.append(", ");
                }
                requestedExtensions.append(knownExtension.getProvidedExtensionAsClient());
            }
        }
        if (requestedExtensions.length() != 0) {
            request.put("Sec-WebSocket-Extensions", requestedExtensions.toString());
        }
        StringBuilder requestedProtocols = new StringBuilder();
        for (IProtocol knownProtocol : this.knownProtocols) {
            if (knownProtocol.getProvidedProtocol().length() != 0) {
                if (requestedProtocols.length() > 0) {
                    requestedProtocols.append(", ");
                }
                requestedProtocols.append(knownProtocol.getProvidedProtocol());
            }
        }
        if (requestedProtocols.length() != 0) {
            request.put("Sec-WebSocket-Protocol", requestedProtocols.toString());
        }
        return request;
    }

    @Override
    public HandshakeBuilder postProcessHandshakeResponseAsServer(ClientHandshake request, ServerHandshakeBuilder response) throws InvalidHandshakeException {
        response.put("Upgrade", "websocket");
        response.put("Connection", request.getFieldValue("Connection"));
        String seckey = request.getFieldValue("Sec-WebSocket-Key");
        if (seckey != null && !"".equals(seckey)) {
            response.put("Sec-WebSocket-Accept", this.generateFinalKey(seckey));
            if (this.getExtension().getProvidedExtensionAsServer().length() != 0) {
                response.put("Sec-WebSocket-Extensions", this.getExtension().getProvidedExtensionAsServer());
            }
            if (this.getProtocol() != null && this.getProtocol().getProvidedProtocol().length() != 0) {
                response.put("Sec-WebSocket-Protocol", this.getProtocol().getProvidedProtocol());
            }
            response.setHttpStatusMessage("Web Socket Protocol Handshake");
            response.put("Server", "TooTallNate Java-WebSocket");
            response.put("Date", this.getServerTime());
            return response;
        } else {
            throw new InvalidHandshakeException("missing Sec-WebSocket-Key");
        }
    }

    @Override
    public Draft copyInstance() {
        ArrayList<IExtension> newExtensions = new ArrayList();
        for (IExtension knownExtension : this.getKnownExtensions()) {
            newExtensions.add(knownExtension.copyInstance());
        }
        ArrayList<IProtocol> newProtocols = new ArrayList();
        for (IProtocol knownProtocol : this.getKnownProtocols()) {
            newProtocols.add(knownProtocol.copyInstance());
        }
        return new Draft_6455(newExtensions, newProtocols, this.maxFrameSize);
    }

    @Override
    public ByteBuffer createBinaryFrame(Framedata framedata) {
        this.getExtension().encodeFrame(framedata);
        if (this.log.isTraceEnabled()) {
            this.log.trace("afterEnconding({}): {}", framedata.getPayloadData().remaining(), framedata.getPayloadData().remaining() > 1000 ? "too big to display" : new String(framedata.getPayloadData().array()));
        }
        return this.createByteBufferFromFramedata(framedata);
    }

    private ByteBuffer createByteBufferFromFramedata(Framedata framedata) {
        ByteBuffer mes = framedata.getPayloadData();
        boolean mask = this.role == Role.CLIENT;
        int sizebytes = this.getSizeBytes(mes);
        ByteBuffer buf = ByteBuffer.allocate(1 + (sizebytes > 1 ? sizebytes + 1 : sizebytes) + (mask ? 4 : 0) + mes.remaining());
        byte optcode = this.fromOpcode(framedata.getOpcode());
        byte one = (byte) (framedata.isFin() ? -128 : 0);
        one = (byte) (one | optcode);
        if (framedata.isRSV1()) {
            one |= this.getRSVByte(1);
        }
        if (framedata.isRSV2()) {
            one |= this.getRSVByte(2);
        }
        if (framedata.isRSV3()) {
            one |= this.getRSVByte(3);
        }
        buf.put(one);
        byte[] payloadlengthbytes = this.toByteArray((long) mes.remaining(), sizebytes);
        assert payloadlengthbytes.length == sizebytes;
        if (sizebytes == 1) {
            buf.put((byte) (payloadlengthbytes[0] | this.getMaskByte(mask)));
        } else if (sizebytes == 2) {
            buf.put((byte) (126 | this.getMaskByte(mask)));
            buf.put(payloadlengthbytes);
        } else {
            if (sizebytes != 8) {
                throw new IllegalStateException("Size representation not supported/specified");
            }
            buf.put((byte) (127 | this.getMaskByte(mask)));
            buf.put(payloadlengthbytes);
        }
        if (mask) {
            ByteBuffer maskkey = ByteBuffer.allocate(4);
            maskkey.putInt(this.reuseableRandom.nextInt());
            buf.put(maskkey.array());
            for (int i = 0; mes.hasRemaining(); i++) {
                buf.put((byte) (mes.get() ^ maskkey.get(i % 4)));
            }
        } else {
            buf.put(mes);
            mes.flip();
        }
        assert buf.remaining() == 0 : buf.remaining();
        buf.flip();
        return buf;
    }

    private Framedata translateSingleFrame(ByteBuffer buffer) throws IncompleteException, InvalidDataException {
        if (buffer == null) {
            throw new IllegalArgumentException();
        } else {
            int maxpacketsize = buffer.remaining();
            int realpacketsize = 2;
            this.translateSingleFrameCheckPacketSize(maxpacketsize, realpacketsize);
            byte b1 = buffer.get();
            boolean fin = b1 >> 8 != 0;
            boolean rsv1 = (b1 & 64) != 0;
            boolean rsv2 = (b1 & 32) != 0;
            boolean rsv3 = (b1 & 16) != 0;
            byte b2 = buffer.get();
            boolean mask = (b2 & -128) != 0;
            int payloadlength = (byte) (b2 & 127);
            Opcode optcode = this.toOpcode((byte) (b1 & 15));
            if (payloadlength < 0 || payloadlength > 125) {
                Draft_6455.TranslatedPayloadMetaData payloadData = this.translateSingleFramePayloadLength(buffer, optcode, payloadlength, maxpacketsize, realpacketsize);
                payloadlength = payloadData.getPayloadLength();
                realpacketsize = payloadData.getRealPackageSize();
            }
            this.translateSingleFrameCheckLengthLimit((long) payloadlength);
            realpacketsize += mask ? 4 : 0;
            realpacketsize += payloadlength;
            this.translateSingleFrameCheckPacketSize(maxpacketsize, realpacketsize);
            ByteBuffer payload = ByteBuffer.allocate(this.checkAlloc(payloadlength));
            if (mask) {
                byte[] maskskey = new byte[4];
                buffer.get(maskskey);
                for (int i = 0; i < payloadlength; i++) {
                    payload.put((byte) (buffer.get() ^ maskskey[i % 4]));
                }
            } else {
                payload.put(buffer.array(), buffer.position(), payload.limit());
                buffer.position(buffer.position() + payload.limit());
            }
            FramedataImpl1 frame = FramedataImpl1.get(optcode);
            frame.setFin(fin);
            frame.setRSV1(rsv1);
            frame.setRSV2(rsv2);
            frame.setRSV3(rsv3);
            payload.flip();
            frame.setPayload(payload);
            if (frame.getOpcode() != Opcode.CONTINUOUS) {
                if (!frame.isRSV1() && !frame.isRSV2() && !frame.isRSV3()) {
                    this.currentDecodingExtension = this.defaultExtension;
                } else {
                    this.currentDecodingExtension = this.getExtension();
                }
            }
            if (this.currentDecodingExtension == null) {
                this.currentDecodingExtension = this.defaultExtension;
            }
            this.currentDecodingExtension.isFrameValid(frame);
            this.currentDecodingExtension.decodeFrame(frame);
            if (this.log.isTraceEnabled()) {
                this.log.trace("afterDecoding({}): {}", frame.getPayloadData().remaining(), frame.getPayloadData().remaining() > 1000 ? "too big to display" : new String(frame.getPayloadData().array()));
            }
            frame.isValid();
            return frame;
        }
    }

    private Draft_6455.TranslatedPayloadMetaData translateSingleFramePayloadLength(ByteBuffer buffer, Opcode optcode, int oldPayloadlength, int maxpacketsize, int oldRealpacketsize) throws InvalidFrameException, IncompleteException, LimitExceededException {
        if (optcode != Opcode.PING && optcode != Opcode.PONG && optcode != Opcode.CLOSING) {
            int payloadlength;
            int realpacketsize;
            if (oldPayloadlength == 126) {
                realpacketsize = oldRealpacketsize + 2;
                this.translateSingleFrameCheckPacketSize(maxpacketsize, realpacketsize);
                byte[] sizebytes = new byte[] { 0, buffer.get(), buffer.get() };
                payloadlength = new BigInteger(sizebytes).intValue();
            } else {
                realpacketsize = oldRealpacketsize + 8;
                this.translateSingleFrameCheckPacketSize(maxpacketsize, realpacketsize);
                byte[] bytes = new byte[8];
                for (int i = 0; i < 8; i++) {
                    bytes[i] = buffer.get();
                }
                long length = new BigInteger(bytes).longValue();
                this.translateSingleFrameCheckLengthLimit(length);
                payloadlength = (int) length;
            }
            return new Draft_6455.TranslatedPayloadMetaData(payloadlength, realpacketsize);
        } else {
            this.log.trace("Invalid frame: more than 125 octets");
            throw new InvalidFrameException("more than 125 octets");
        }
    }

    private void translateSingleFrameCheckLengthLimit(long length) throws LimitExceededException {
        if (length > 2147483647L) {
            this.log.trace("Limit exedeed: Payloadsize is to big...");
            throw new LimitExceededException("Payloadsize is to big...");
        } else if (length > (long) this.maxFrameSize) {
            this.log.trace("Payload limit reached. Allowed: {} Current: {}", this.maxFrameSize, length);
            throw new LimitExceededException("Payload limit reached.", this.maxFrameSize);
        } else if (length < 0L) {
            this.log.trace("Limit underflow: Payloadsize is to little...");
            throw new LimitExceededException("Payloadsize is to little...");
        }
    }

    private void translateSingleFrameCheckPacketSize(int maxpacketsize, int realpacketsize) throws IncompleteException {
        if (maxpacketsize < realpacketsize) {
            this.log.trace("Incomplete frame: maxpacketsize < realpacketsize");
            throw new IncompleteException(realpacketsize);
        }
    }

    private byte getRSVByte(int rsv) {
        switch(rsv) {
            case 1:
                return 64;
            case 2:
                return 32;
            case 3:
                return 16;
            default:
                return 0;
        }
    }

    private byte getMaskByte(boolean mask) {
        return (byte) (mask ? -128 : 0);
    }

    private int getSizeBytes(ByteBuffer mes) {
        if (mes.remaining() <= 125) {
            return 1;
        } else {
            return mes.remaining() <= 65535 ? 2 : 8;
        }
    }

    @Override
    public List<Framedata> translateFrame(ByteBuffer buffer) throws InvalidDataException {
        while (true) {
            List<Framedata> frames = new LinkedList();
            if (this.incompleteframe != null) {
                try {
                    buffer.mark();
                    int availableNextByteCount = buffer.remaining();
                    int expectedNextByteCount = this.incompleteframe.remaining();
                    if (expectedNextByteCount > availableNextByteCount) {
                        this.incompleteframe.put(buffer.array(), buffer.position(), availableNextByteCount);
                        buffer.position(buffer.position() + availableNextByteCount);
                        return Collections.emptyList();
                    }
                    this.incompleteframe.put(buffer.array(), buffer.position(), expectedNextByteCount);
                    buffer.position(buffer.position() + expectedNextByteCount);
                    Framedata cur = this.translateSingleFrame((ByteBuffer) this.incompleteframe.duplicate().position(0));
                    frames.add(cur);
                    this.incompleteframe = null;
                } catch (IncompleteException var6) {
                    ByteBuffer extendedframe = ByteBuffer.allocate(this.checkAlloc(var6.getPreferredSize()));
                    assert extendedframe.limit() > this.incompleteframe.limit();
                    this.incompleteframe.rewind();
                    extendedframe.put(this.incompleteframe);
                    this.incompleteframe = extendedframe;
                    continue;
                }
            }
            while (buffer.hasRemaining()) {
                buffer.mark();
                try {
                    Framedata cur = this.translateSingleFrame(buffer);
                    frames.add(cur);
                } catch (IncompleteException var7) {
                    buffer.reset();
                    int pref = var7.getPreferredSize();
                    this.incompleteframe = ByteBuffer.allocate(this.checkAlloc(pref));
                    this.incompleteframe.put(buffer);
                    break;
                }
            }
            return frames;
        }
    }

    @Override
    public List<Framedata> createFrames(ByteBuffer binary, boolean mask) {
        BinaryFrame curframe = new BinaryFrame();
        curframe.setPayload(binary);
        curframe.setTransferemasked(mask);
        try {
            curframe.isValid();
        } catch (InvalidDataException var5) {
            throw new NotSendableException(var5);
        }
        return Collections.singletonList(curframe);
    }

    @Override
    public List<Framedata> createFrames(String text, boolean mask) {
        TextFrame curframe = new TextFrame();
        curframe.setPayload(ByteBuffer.wrap(Charsetfunctions.utf8Bytes(text)));
        curframe.setTransferemasked(mask);
        try {
            curframe.isValid();
        } catch (InvalidDataException var5) {
            throw new NotSendableException(var5);
        }
        return Collections.singletonList(curframe);
    }

    @Override
    public void reset() {
        this.incompleteframe = null;
        if (this.negotiatedExtension != null) {
            this.negotiatedExtension.reset();
        }
        this.negotiatedExtension = new DefaultExtension();
        this.protocol = null;
    }

    private String getServerTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }

    private String generateFinalKey(String in) {
        String seckey = in.trim();
        String acc = seckey + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
        MessageDigest sh1;
        try {
            sh1 = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException var6) {
            throw new IllegalStateException(var6);
        }
        return Base64.encodeBytes(sh1.digest(acc.getBytes()));
    }

    private byte[] toByteArray(long val, int bytecount) {
        byte[] buffer = new byte[bytecount];
        int highest = 8 * bytecount - 8;
        for (int i = 0; i < bytecount; i++) {
            buffer[i] = (byte) ((int) (val >>> highest - 8 * i));
        }
        return buffer;
    }

    private byte fromOpcode(Opcode opcode) {
        if (opcode == Opcode.CONTINUOUS) {
            return 0;
        } else if (opcode == Opcode.TEXT) {
            return 1;
        } else if (opcode == Opcode.BINARY) {
            return 2;
        } else if (opcode == Opcode.CLOSING) {
            return 8;
        } else if (opcode == Opcode.PING) {
            return 9;
        } else if (opcode == Opcode.PONG) {
            return 10;
        } else {
            throw new IllegalArgumentException("Don't know how to handle " + opcode.toString());
        }
    }

    private Opcode toOpcode(byte opcode) throws InvalidFrameException {
        switch(opcode) {
            case 0:
                return Opcode.CONTINUOUS;
            case 1:
                return Opcode.TEXT;
            case 2:
                return Opcode.BINARY;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            default:
                throw new InvalidFrameException("Unknown opcode " + (short) opcode);
            case 8:
                return Opcode.CLOSING;
            case 9:
                return Opcode.PING;
            case 10:
                return Opcode.PONG;
        }
    }

    @Override
    public void processFrame(WebSocketImpl webSocketImpl, Framedata frame) throws InvalidDataException {
        Opcode curop = frame.getOpcode();
        if (curop == Opcode.CLOSING) {
            this.processFrameClosing(webSocketImpl, frame);
        } else if (curop == Opcode.PING) {
            webSocketImpl.getWebSocketListener().onWebsocketPing(webSocketImpl, frame);
        } else if (curop == Opcode.PONG) {
            webSocketImpl.updateLastPong();
            webSocketImpl.getWebSocketListener().onWebsocketPong(webSocketImpl, frame);
        } else if (frame.isFin() && curop != Opcode.CONTINUOUS) {
            if (this.currentContinuousFrame != null) {
                this.log.error("Protocol error: Continuous frame sequence not completed.");
                throw new InvalidDataException(1002, "Continuous frame sequence not completed.");
            }
            if (curop == Opcode.TEXT) {
                this.processFrameText(webSocketImpl, frame);
            } else {
                if (curop != Opcode.BINARY) {
                    this.log.error("non control or continious frame expected");
                    throw new InvalidDataException(1002, "non control or continious frame expected");
                }
                this.processFrameBinary(webSocketImpl, frame);
            }
        } else {
            this.processFrameContinuousAndNonFin(webSocketImpl, frame, curop);
        }
    }

    private void processFrameContinuousAndNonFin(WebSocketImpl webSocketImpl, Framedata frame, Opcode curop) throws InvalidDataException {
        if (curop != Opcode.CONTINUOUS) {
            this.processFrameIsNotFin(frame);
        } else if (frame.isFin()) {
            this.processFrameIsFin(webSocketImpl, frame);
        } else if (this.currentContinuousFrame == null) {
            this.log.error("Protocol error: Continuous frame sequence was not started.");
            throw new InvalidDataException(1002, "Continuous frame sequence was not started.");
        }
        if (curop == Opcode.TEXT && !Charsetfunctions.isValidUTF8(frame.getPayloadData())) {
            this.log.error("Protocol error: Payload is not UTF8");
            throw new InvalidDataException(1007);
        } else {
            if (curop == Opcode.CONTINUOUS && this.currentContinuousFrame != null) {
                this.addToBufferList(frame.getPayloadData());
            }
        }
    }

    private void processFrameBinary(WebSocketImpl webSocketImpl, Framedata frame) {
        try {
            webSocketImpl.getWebSocketListener().onWebsocketMessage(webSocketImpl, frame.getPayloadData());
        } catch (RuntimeException var4) {
            this.logRuntimeException(webSocketImpl, var4);
        }
    }

    private void logRuntimeException(WebSocketImpl webSocketImpl, RuntimeException e) {
        this.log.error("Runtime exception during onWebsocketMessage", e);
        webSocketImpl.getWebSocketListener().onWebsocketError(webSocketImpl, e);
    }

    private void processFrameText(WebSocketImpl webSocketImpl, Framedata frame) throws InvalidDataException {
        try {
            webSocketImpl.getWebSocketListener().onWebsocketMessage(webSocketImpl, Charsetfunctions.stringUtf8(frame.getPayloadData()));
        } catch (RuntimeException var4) {
            this.logRuntimeException(webSocketImpl, var4);
        }
    }

    private void processFrameIsFin(WebSocketImpl webSocketImpl, Framedata frame) throws InvalidDataException {
        if (this.currentContinuousFrame == null) {
            this.log.trace("Protocol error: Previous continuous frame sequence not completed.");
            throw new InvalidDataException(1002, "Continuous frame sequence was not started.");
        } else {
            this.addToBufferList(frame.getPayloadData());
            this.checkBufferLimit();
            if (this.currentContinuousFrame.getOpcode() == Opcode.TEXT) {
                ((FramedataImpl1) this.currentContinuousFrame).setPayload(this.getPayloadFromByteBufferList());
                ((FramedataImpl1) this.currentContinuousFrame).isValid();
                try {
                    webSocketImpl.getWebSocketListener().onWebsocketMessage(webSocketImpl, Charsetfunctions.stringUtf8(this.currentContinuousFrame.getPayloadData()));
                } catch (RuntimeException var5) {
                    this.logRuntimeException(webSocketImpl, var5);
                }
            } else if (this.currentContinuousFrame.getOpcode() == Opcode.BINARY) {
                ((FramedataImpl1) this.currentContinuousFrame).setPayload(this.getPayloadFromByteBufferList());
                ((FramedataImpl1) this.currentContinuousFrame).isValid();
                try {
                    webSocketImpl.getWebSocketListener().onWebsocketMessage(webSocketImpl, this.currentContinuousFrame.getPayloadData());
                } catch (RuntimeException var4) {
                    this.logRuntimeException(webSocketImpl, var4);
                }
            }
            this.currentContinuousFrame = null;
            this.clearBufferList();
        }
    }

    private void processFrameIsNotFin(Framedata frame) throws InvalidDataException {
        if (this.currentContinuousFrame != null) {
            this.log.trace("Protocol error: Previous continuous frame sequence not completed.");
            throw new InvalidDataException(1002, "Previous continuous frame sequence not completed.");
        } else {
            this.currentContinuousFrame = frame;
            this.addToBufferList(frame.getPayloadData());
            this.checkBufferLimit();
        }
    }

    private void processFrameClosing(WebSocketImpl webSocketImpl, Framedata frame) {
        int code = 1005;
        String reason = "";
        if (frame instanceof CloseFrame) {
            CloseFrame cf = (CloseFrame) frame;
            code = cf.getCloseCode();
            reason = cf.getMessage();
        }
        if (webSocketImpl.getReadyState() == ReadyState.CLOSING) {
            webSocketImpl.closeConnection(code, reason, true);
        } else if (this.getCloseHandshakeType() == CloseHandshakeType.TWOWAY) {
            webSocketImpl.close(code, reason, true);
        } else {
            webSocketImpl.flushAndClose(code, reason, false);
        }
    }

    private void clearBufferList() {
        synchronized (this.byteBufferList) {
            this.byteBufferList.clear();
        }
    }

    private void addToBufferList(ByteBuffer payloadData) {
        synchronized (this.byteBufferList) {
            this.byteBufferList.add(payloadData);
        }
    }

    private void checkBufferLimit() throws LimitExceededException {
        long totalSize = this.getByteBufferListSize();
        if (totalSize > (long) this.maxFrameSize) {
            this.clearBufferList();
            this.log.trace("Payload limit reached. Allowed: {} Current: {}", this.maxFrameSize, totalSize);
            throw new LimitExceededException(this.maxFrameSize);
        }
    }

    @Override
    public CloseHandshakeType getCloseHandshakeType() {
        return CloseHandshakeType.TWOWAY;
    }

    @Override
    public String toString() {
        String result = super.toString();
        if (this.getExtension() != null) {
            result = result + " extension: " + this.getExtension().toString();
        }
        if (this.getProtocol() != null) {
            result = result + " protocol: " + this.getProtocol().toString();
        }
        return result + " max frame size: " + this.maxFrameSize;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Draft_6455 that = (Draft_6455) o;
            if (this.maxFrameSize != that.getMaxFrameSize()) {
                return false;
            } else if (this.negotiatedExtension != null ? this.negotiatedExtension.equals(that.getExtension()) : that.getExtension() == null) {
                return this.protocol != null ? this.protocol.equals(that.getProtocol()) : that.getProtocol() == null;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.negotiatedExtension != null ? this.negotiatedExtension.hashCode() : 0;
        result = 31 * result + (this.protocol != null ? this.protocol.hashCode() : 0);
        return 31 * result + (this.maxFrameSize ^ this.maxFrameSize >>> 32);
    }

    private ByteBuffer getPayloadFromByteBufferList() throws LimitExceededException {
        long totalSize = 0L;
        ByteBuffer resultingByteBuffer;
        synchronized (this.byteBufferList) {
            for (ByteBuffer buffer : this.byteBufferList) {
                totalSize += (long) buffer.limit();
            }
            this.checkBufferLimit();
            resultingByteBuffer = ByteBuffer.allocate((int) totalSize);
            for (ByteBuffer buffer : this.byteBufferList) {
                resultingByteBuffer.put(buffer);
            }
        }
        resultingByteBuffer.flip();
        return resultingByteBuffer;
    }

    private long getByteBufferListSize() {
        long totalSize = 0L;
        synchronized (this.byteBufferList) {
            for (ByteBuffer buffer : this.byteBufferList) {
                totalSize += (long) buffer.limit();
            }
            return totalSize;
        }
    }

    private class TranslatedPayloadMetaData {

        private int payloadLength;

        private int realPackageSize;

        private int getPayloadLength() {
            return this.payloadLength;
        }

        private int getRealPackageSize() {
            return this.realPackageSize;
        }

        TranslatedPayloadMetaData(int newPayloadLength, int newRealPackageSize) {
            this.payloadLength = newPayloadLength;
            this.realPackageSize = newRealPackageSize;
        }
    }
}