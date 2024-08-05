package info.journeymap.shaded.org.eclipse.jetty.websocket.common.frames;

import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Frame;
import java.nio.ByteBuffer;

public class ReadOnlyDelegatedFrame implements Frame {

    private final Frame delegate;

    public ReadOnlyDelegatedFrame(Frame frame) {
        this.delegate = frame;
    }

    @Override
    public byte[] getMask() {
        return this.delegate.getMask();
    }

    @Override
    public byte getOpCode() {
        return this.delegate.getOpCode();
    }

    @Override
    public ByteBuffer getPayload() {
        return !this.delegate.hasPayload() ? null : this.delegate.getPayload().asReadOnlyBuffer();
    }

    @Override
    public int getPayloadLength() {
        return this.delegate.getPayloadLength();
    }

    @Override
    public Frame.Type getType() {
        return this.delegate.getType();
    }

    @Override
    public boolean hasPayload() {
        return this.delegate.hasPayload();
    }

    @Override
    public boolean isFin() {
        return this.delegate.isFin();
    }

    @Deprecated
    @Override
    public boolean isLast() {
        return this.delegate.isLast();
    }

    @Override
    public boolean isMasked() {
        return this.delegate.isMasked();
    }

    @Override
    public boolean isRsv1() {
        return this.delegate.isRsv1();
    }

    @Override
    public boolean isRsv2() {
        return this.delegate.isRsv2();
    }

    @Override
    public boolean isRsv3() {
        return this.delegate.isRsv3();
    }
}