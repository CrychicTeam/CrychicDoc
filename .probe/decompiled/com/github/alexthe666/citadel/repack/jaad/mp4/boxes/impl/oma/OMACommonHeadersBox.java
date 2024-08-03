package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.oma;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OMACommonHeadersBox extends FullBox {

    private int encryptionMethod;

    private int paddingScheme;

    private long plaintextLength;

    private byte[] contentID;

    private byte[] rightsIssuerURL;

    private Map<String, String> textualHeaders;

    public OMACommonHeadersBox() {
        super("OMA DRM Common Header Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.encryptionMethod = in.read();
        this.paddingScheme = in.read();
        this.plaintextLength = in.readBytes(8);
        int contentIDLength = (int) in.readBytes(2);
        int rightsIssuerURLLength = (int) in.readBytes(2);
        int textualHeadersLength = (int) in.readBytes(2);
        this.contentID = new byte[contentIDLength];
        in.readBytes(this.contentID);
        this.rightsIssuerURL = new byte[rightsIssuerURLLength];
        in.readBytes(this.rightsIssuerURL);
        this.textualHeaders = new HashMap();
        while (textualHeadersLength > 0) {
            String key = new String(in.readTerminated((int) this.getLeft(in), 58));
            String value = new String(in.readTerminated((int) this.getLeft(in), 0));
            this.textualHeaders.put(key, value);
            textualHeadersLength -= key.length() + value.length() + 2;
        }
        this.readChildren(in);
    }

    public int getEncryptionMethod() {
        return this.encryptionMethod;
    }

    public int getPaddingScheme() {
        return this.paddingScheme;
    }

    public long getPlaintextLength() {
        return this.plaintextLength;
    }

    public byte[] getContentID() {
        return this.contentID;
    }

    public byte[] getRightsIssuerURL() {
        return this.rightsIssuerURL;
    }

    public Map<String, String> getTextualHeaders() {
        return this.textualHeaders;
    }
}