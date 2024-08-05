package me.lucko.spark.lib.bytesocks.ws.framing;

import java.nio.ByteBuffer;
import me.lucko.spark.lib.bytesocks.ws.enums.Opcode;
import me.lucko.spark.lib.bytesocks.ws.exceptions.InvalidDataException;
import me.lucko.spark.lib.bytesocks.ws.util.ByteBufferUtils;

public abstract class FramedataImpl1 implements Framedata {

    private boolean fin;

    private Opcode optcode;

    private ByteBuffer unmaskedpayload;

    private boolean transferemasked;

    private boolean rsv1;

    private boolean rsv2;

    private boolean rsv3;

    public abstract void isValid() throws InvalidDataException;

    public FramedataImpl1(Opcode op) {
        this.optcode = op;
        this.unmaskedpayload = ByteBufferUtils.getEmptyByteBuffer();
        this.fin = true;
        this.transferemasked = false;
        this.rsv1 = false;
        this.rsv2 = false;
        this.rsv3 = false;
    }

    @Override
    public boolean isRSV1() {
        return this.rsv1;
    }

    @Override
    public boolean isRSV2() {
        return this.rsv2;
    }

    @Override
    public boolean isRSV3() {
        return this.rsv3;
    }

    @Override
    public boolean isFin() {
        return this.fin;
    }

    @Override
    public Opcode getOpcode() {
        return this.optcode;
    }

    @Override
    public boolean getTransfereMasked() {
        return this.transferemasked;
    }

    @Override
    public ByteBuffer getPayloadData() {
        return this.unmaskedpayload;
    }

    @Override
    public void append(Framedata nextframe) {
        ByteBuffer b = nextframe.getPayloadData();
        if (this.unmaskedpayload == null) {
            this.unmaskedpayload = ByteBuffer.allocate(b.remaining());
            b.mark();
            this.unmaskedpayload.put(b);
            b.reset();
        } else {
            b.mark();
            this.unmaskedpayload.position(this.unmaskedpayload.limit());
            this.unmaskedpayload.limit(this.unmaskedpayload.capacity());
            if (b.remaining() > this.unmaskedpayload.remaining()) {
                ByteBuffer tmp = ByteBuffer.allocate(b.remaining() + this.unmaskedpayload.capacity());
                this.unmaskedpayload.flip();
                tmp.put(this.unmaskedpayload);
                tmp.put(b);
                this.unmaskedpayload = tmp;
            } else {
                this.unmaskedpayload.put(b);
            }
            this.unmaskedpayload.rewind();
            b.reset();
        }
        this.fin = nextframe.isFin();
    }

    public String toString() {
        return "Framedata{ opcode:" + this.getOpcode() + ", fin:" + this.isFin() + ", rsv1:" + this.isRSV1() + ", rsv2:" + this.isRSV2() + ", rsv3:" + this.isRSV3() + ", payload length:[pos:" + this.unmaskedpayload.position() + ", len:" + this.unmaskedpayload.remaining() + "], payload:" + (this.unmaskedpayload.remaining() > 1000 ? "(too big to display)" : new String(this.unmaskedpayload.array())) + '}';
    }

    public void setPayload(ByteBuffer payload) {
        this.unmaskedpayload = payload;
    }

    public void setFin(boolean fin) {
        this.fin = fin;
    }

    public void setRSV1(boolean rsv1) {
        this.rsv1 = rsv1;
    }

    public void setRSV2(boolean rsv2) {
        this.rsv2 = rsv2;
    }

    public void setRSV3(boolean rsv3) {
        this.rsv3 = rsv3;
    }

    public void setTransferemasked(boolean transferemasked) {
        this.transferemasked = transferemasked;
    }

    public static FramedataImpl1 get(Opcode opcode) {
        if (opcode == null) {
            throw new IllegalArgumentException("Supplied opcode cannot be null");
        } else {
            switch(opcode) {
                case PING:
                    return new PingFrame();
                case PONG:
                    return new PongFrame();
                case TEXT:
                    return new TextFrame();
                case BINARY:
                    return new BinaryFrame();
                case CLOSING:
                    return new CloseFrame();
                case CONTINUOUS:
                    return new ContinuousFrame();
                default:
                    throw new IllegalArgumentException("Supplied opcode is invalid");
            }
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            FramedataImpl1 that = (FramedataImpl1) o;
            if (this.fin != that.fin) {
                return false;
            } else if (this.transferemasked != that.transferemasked) {
                return false;
            } else if (this.rsv1 != that.rsv1) {
                return false;
            } else if (this.rsv2 != that.rsv2) {
                return false;
            } else if (this.rsv3 != that.rsv3) {
                return false;
            } else if (this.optcode != that.optcode) {
                return false;
            } else {
                return this.unmaskedpayload != null ? this.unmaskedpayload.equals(that.unmaskedpayload) : that.unmaskedpayload == null;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.fin ? 1 : 0;
        result = 31 * result + this.optcode.hashCode();
        result = 31 * result + (this.unmaskedpayload != null ? this.unmaskedpayload.hashCode() : 0);
        result = 31 * result + (this.transferemasked ? 1 : 0);
        result = 31 * result + (this.rsv1 ? 1 : 0);
        result = 31 * result + (this.rsv2 ? 1 : 0);
        return 31 * result + (this.rsv3 ? 1 : 0);
    }
}