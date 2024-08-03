package net.minecraft.server.rcon;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NetworkDataOutputStream {

    private final ByteArrayOutputStream outputStream;

    private final DataOutputStream dataOutputStream;

    public NetworkDataOutputStream(int int0) {
        this.outputStream = new ByteArrayOutputStream(int0);
        this.dataOutputStream = new DataOutputStream(this.outputStream);
    }

    public void writeBytes(byte[] byte0) throws IOException {
        this.dataOutputStream.write(byte0, 0, byte0.length);
    }

    public void writeString(String string0) throws IOException {
        this.dataOutputStream.writeBytes(string0);
        this.dataOutputStream.write(0);
    }

    public void write(int int0) throws IOException {
        this.dataOutputStream.write(int0);
    }

    public void writeShort(short short0) throws IOException {
        this.dataOutputStream.writeShort(Short.reverseBytes(short0));
    }

    public void writeInt(int int0) throws IOException {
        this.dataOutputStream.writeInt(Integer.reverseBytes(int0));
    }

    public void writeFloat(float float0) throws IOException {
        this.dataOutputStream.writeInt(Integer.reverseBytes(Float.floatToIntBits(float0)));
    }

    public byte[] toByteArray() {
        return this.outputStream.toByteArray();
    }

    public void reset() {
        this.outputStream.reset();
    }
}