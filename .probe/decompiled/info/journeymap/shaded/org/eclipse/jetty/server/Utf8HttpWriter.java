package info.journeymap.shaded.org.eclipse.jetty.server;

import java.io.IOException;

public class Utf8HttpWriter extends HttpWriter {

    int _surrogate = 0;

    public Utf8HttpWriter(HttpOutput out) {
        super(out);
    }

    @Override
    public void write(char[] s, int offset, int length) throws IOException {
        HttpOutput out = this._out;
        if (length == 0 && out.isAllContentWritten()) {
            this.close();
        } else {
            while (length > 0) {
                this._bytes.reset();
                int chars = length > 512 ? 512 : length;
                byte[] buffer = this._bytes.getBuf();
                int bytes = this._bytes.getCount();
                if (bytes + chars > buffer.length) {
                    chars = buffer.length - bytes;
                }
                for (int i = 0; i < chars; i++) {
                    int code = s[offset + i];
                    if (this._surrogate == 0) {
                        if (Character.isHighSurrogate((char) code)) {
                            this._surrogate = code;
                            continue;
                        }
                    } else if (Character.isLowSurrogate((char) code)) {
                        code = Character.toCodePoint((char) this._surrogate, (char) code);
                    } else {
                        code = this._surrogate;
                        this._surrogate = 0;
                        i--;
                    }
                    if ((code & -128) == 0) {
                        if (bytes >= buffer.length) {
                            chars = i;
                            break;
                        }
                        buffer[bytes++] = (byte) code;
                    } else {
                        if ((code & -2048) == 0) {
                            if (bytes + 2 > buffer.length) {
                                chars = i;
                                break;
                            }
                            buffer[bytes++] = (byte) (192 | code >> 6);
                            buffer[bytes++] = (byte) (128 | code & 63);
                        } else if ((code & -65536) == 0) {
                            if (bytes + 3 > buffer.length) {
                                chars = i;
                                break;
                            }
                            buffer[bytes++] = (byte) (224 | code >> 12);
                            buffer[bytes++] = (byte) (128 | code >> 6 & 63);
                            buffer[bytes++] = (byte) (128 | code & 63);
                        } else if ((code & -14680064) == 0) {
                            if (bytes + 4 > buffer.length) {
                                chars = i;
                                break;
                            }
                            buffer[bytes++] = (byte) (240 | code >> 18);
                            buffer[bytes++] = (byte) (128 | code >> 12 & 63);
                            buffer[bytes++] = (byte) (128 | code >> 6 & 63);
                            buffer[bytes++] = (byte) (128 | code & 63);
                        } else if ((code & -201326592) == 0) {
                            if (bytes + 5 > buffer.length) {
                                chars = i;
                                break;
                            }
                            buffer[bytes++] = (byte) (248 | code >> 24);
                            buffer[bytes++] = (byte) (128 | code >> 18 & 63);
                            buffer[bytes++] = (byte) (128 | code >> 12 & 63);
                            buffer[bytes++] = (byte) (128 | code >> 6 & 63);
                            buffer[bytes++] = (byte) (128 | code & 63);
                        } else if ((code & -2147483648) == 0) {
                            if (bytes + 6 > buffer.length) {
                                chars = i;
                                break;
                            }
                            buffer[bytes++] = (byte) (252 | code >> 30);
                            buffer[bytes++] = (byte) (128 | code >> 24 & 63);
                            buffer[bytes++] = (byte) (128 | code >> 18 & 63);
                            buffer[bytes++] = (byte) (128 | code >> 12 & 63);
                            buffer[bytes++] = (byte) (128 | code >> 6 & 63);
                            buffer[bytes++] = (byte) (128 | code & 63);
                        } else {
                            buffer[bytes++] = 63;
                        }
                        this._surrogate = 0;
                        if (bytes == buffer.length) {
                            chars = i + 1;
                            break;
                        }
                    }
                }
                this._bytes.setCount(bytes);
                this._bytes.writeTo(out);
                length -= chars;
                offset += chars;
            }
        }
    }
}