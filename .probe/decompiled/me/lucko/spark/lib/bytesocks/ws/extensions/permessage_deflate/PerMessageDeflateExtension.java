package me.lucko.spark.lib.bytesocks.ws.extensions.permessage_deflate;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import me.lucko.spark.lib.bytesocks.ws.enums.Opcode;
import me.lucko.spark.lib.bytesocks.ws.exceptions.InvalidDataException;
import me.lucko.spark.lib.bytesocks.ws.exceptions.InvalidFrameException;
import me.lucko.spark.lib.bytesocks.ws.extensions.CompressionExtension;
import me.lucko.spark.lib.bytesocks.ws.extensions.ExtensionRequestData;
import me.lucko.spark.lib.bytesocks.ws.extensions.IExtension;
import me.lucko.spark.lib.bytesocks.ws.framing.ContinuousFrame;
import me.lucko.spark.lib.bytesocks.ws.framing.DataFrame;
import me.lucko.spark.lib.bytesocks.ws.framing.Framedata;
import me.lucko.spark.lib.bytesocks.ws.framing.FramedataImpl1;

public class PerMessageDeflateExtension extends CompressionExtension {

    private static final String EXTENSION_REGISTERED_NAME = "permessage-deflate";

    private static final String SERVER_NO_CONTEXT_TAKEOVER = "server_no_context_takeover";

    private static final String CLIENT_NO_CONTEXT_TAKEOVER = "client_no_context_takeover";

    private static final String SERVER_MAX_WINDOW_BITS = "server_max_window_bits";

    private static final String CLIENT_MAX_WINDOW_BITS = "client_max_window_bits";

    private static final int serverMaxWindowBits = 32768;

    private static final int clientMaxWindowBits = 32768;

    private static final byte[] TAIL_BYTES = new byte[] { 0, 0, -1, -1 };

    private static final int BUFFER_SIZE = 1024;

    private int threshold = 1024;

    private boolean serverNoContextTakeover = true;

    private boolean clientNoContextTakeover = false;

    private Map<String, String> requestedParameters = new LinkedHashMap();

    private Inflater inflater = new Inflater(true);

    private Deflater deflater = new Deflater(-1, true);

    public Inflater getInflater() {
        return this.inflater;
    }

    public void setInflater(Inflater inflater) {
        this.inflater = inflater;
    }

    public Deflater getDeflater() {
        return this.deflater;
    }

    public void setDeflater(Deflater deflater) {
        this.deflater = deflater;
    }

    public int getThreshold() {
        return this.threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public boolean isServerNoContextTakeover() {
        return this.serverNoContextTakeover;
    }

    public void setServerNoContextTakeover(boolean serverNoContextTakeover) {
        this.serverNoContextTakeover = serverNoContextTakeover;
    }

    public boolean isClientNoContextTakeover() {
        return this.clientNoContextTakeover;
    }

    public void setClientNoContextTakeover(boolean clientNoContextTakeover) {
        this.clientNoContextTakeover = clientNoContextTakeover;
    }

    @Override
    public void decodeFrame(Framedata inputFrame) throws InvalidDataException {
        if (inputFrame instanceof DataFrame) {
            if (inputFrame.isRSV1() || inputFrame.getOpcode() == Opcode.CONTINUOUS) {
                if (inputFrame.getOpcode() == Opcode.CONTINUOUS && inputFrame.isRSV1()) {
                    throw new InvalidDataException(1008, "RSV1 bit can only be set for the first frame.");
                } else {
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    try {
                        this.decompress(inputFrame.getPayloadData().array(), output);
                        if (this.inflater.getRemaining() > 0) {
                            this.inflater = new Inflater(true);
                            this.decompress(inputFrame.getPayloadData().array(), output);
                        }
                        if (inputFrame.isFin()) {
                            this.decompress(TAIL_BYTES, output);
                            if (this.clientNoContextTakeover) {
                                this.inflater = new Inflater(true);
                            }
                        }
                    } catch (DataFormatException var4) {
                        throw new InvalidDataException(1008, var4.getMessage());
                    }
                    ((FramedataImpl1) inputFrame).setPayload(ByteBuffer.wrap(output.toByteArray(), 0, output.size()));
                }
            }
        }
    }

    private void decompress(byte[] data, ByteArrayOutputStream outputBuffer) throws DataFormatException {
        this.inflater.setInput(data);
        byte[] buffer = new byte[1024];
        int bytesInflated;
        while ((bytesInflated = this.inflater.inflate(buffer)) > 0) {
            outputBuffer.write(buffer, 0, bytesInflated);
        }
    }

    @Override
    public void encodeFrame(Framedata inputFrame) {
        if (inputFrame instanceof DataFrame) {
            byte[] payloadData = inputFrame.getPayloadData().array();
            if (payloadData.length >= this.threshold) {
                if (!(inputFrame instanceof ContinuousFrame)) {
                    ((DataFrame) inputFrame).setRSV1(true);
                }
                this.deflater.setInput(payloadData);
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesCompressed;
                while ((bytesCompressed = this.deflater.deflate(buffer, 0, buffer.length, 2)) > 0) {
                    output.write(buffer, 0, bytesCompressed);
                }
                byte[] outputBytes = output.toByteArray();
                int outputLength = outputBytes.length;
                if (inputFrame.isFin()) {
                    if (endsWithTail(outputBytes)) {
                        outputLength -= TAIL_BYTES.length;
                    }
                    if (this.serverNoContextTakeover) {
                        this.deflater.end();
                        this.deflater = new Deflater(-1, true);
                    }
                }
                ((FramedataImpl1) inputFrame).setPayload(ByteBuffer.wrap(outputBytes, 0, outputLength));
            }
        }
    }

    private static boolean endsWithTail(byte[] data) {
        if (data.length < 4) {
            return false;
        } else {
            int length = data.length;
            for (int i = 0; i < TAIL_BYTES.length; i++) {
                if (TAIL_BYTES[i] != data[length - TAIL_BYTES.length + i]) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public boolean acceptProvidedExtensionAsServer(String inputExtension) {
        String[] requestedExtensions = inputExtension.split(",");
        for (String extension : requestedExtensions) {
            ExtensionRequestData extensionData = ExtensionRequestData.parseExtensionRequest(extension);
            if ("permessage-deflate".equalsIgnoreCase(extensionData.getExtensionName())) {
                Map<String, String> headers = extensionData.getExtensionParameters();
                this.requestedParameters.putAll(headers);
                if (this.requestedParameters.containsKey("client_no_context_takeover")) {
                    this.clientNoContextTakeover = true;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean acceptProvidedExtensionAsClient(String inputExtension) {
        String[] requestedExtensions = inputExtension.split(",");
        for (String extension : requestedExtensions) {
            ExtensionRequestData extensionData = ExtensionRequestData.parseExtensionRequest(extension);
            if ("permessage-deflate".equalsIgnoreCase(extensionData.getExtensionName())) {
                Map<String, String> headers = extensionData.getExtensionParameters();
                return true;
            }
        }
        return false;
    }

    @Override
    public String getProvidedExtensionAsClient() {
        this.requestedParameters.put("client_no_context_takeover", "");
        this.requestedParameters.put("server_no_context_takeover", "");
        return "permessage-deflate; server_no_context_takeover; client_no_context_takeover";
    }

    @Override
    public String getProvidedExtensionAsServer() {
        return "permessage-deflate; server_no_context_takeover" + (this.clientNoContextTakeover ? "; client_no_context_takeover" : "");
    }

    @Override
    public IExtension copyInstance() {
        return new PerMessageDeflateExtension();
    }

    @Override
    public void isFrameValid(Framedata inputFrame) throws InvalidDataException {
        if (!(inputFrame instanceof ContinuousFrame) || !inputFrame.isRSV1() && !inputFrame.isRSV2() && !inputFrame.isRSV3()) {
            super.isFrameValid(inputFrame);
        } else {
            throw new InvalidFrameException("bad rsv RSV1: " + inputFrame.isRSV1() + " RSV2: " + inputFrame.isRSV2() + " RSV3: " + inputFrame.isRSV3());
        }
    }

    @Override
    public String toString() {
        return "PerMessageDeflateExtension";
    }
}