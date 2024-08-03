package com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class EbmlString extends EbmlBin {

    public String charset = "UTF-8";

    public EbmlString(byte[] id) {
        super(id);
    }

    public static EbmlString createEbmlString(byte[] id, String value) {
        EbmlString e = new EbmlString(id);
        e.setString(value);
        return e;
    }

    public String getString() {
        try {
            return new String(this.data.array(), this.charset);
        } catch (UnsupportedEncodingException var2) {
            var2.printStackTrace();
            return "";
        }
    }

    public void setString(String value) {
        try {
            this.data = ByteBuffer.wrap(value.getBytes(this.charset));
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
        }
    }
}