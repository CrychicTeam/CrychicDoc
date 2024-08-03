package info.journeymap.shaded.org.eclipse.jetty.server;

import java.io.IOException;

public class Iso88591HttpWriter extends HttpWriter {

    public Iso88591HttpWriter(HttpOutput out) {
        super(out);
    }

    @Override
    public void write(char[] s, int offset, int length) throws IOException {
        HttpOutput out = this._out;
        if (length == 0 && out.isAllContentWritten()) {
            this.close();
        } else if (length == 1) {
            int c = s[offset];
            out.write(c < 256 ? c : 63);
        } else {
            while (length > 0) {
                this._bytes.reset();
                int chars = length > 512 ? 512 : length;
                byte[] buffer = this._bytes.getBuf();
                int bytes = this._bytes.getCount();
                if (chars > buffer.length - bytes) {
                    chars = buffer.length - bytes;
                }
                for (int i = 0; i < chars; i++) {
                    int c = s[offset + i];
                    buffer[bytes++] = (byte) (c < 256 ? c : 63);
                }
                if (bytes >= 0) {
                    this._bytes.setCount(bytes);
                }
                this._bytes.writeTo(out);
                length -= chars;
                offset += chars;
            }
        }
    }
}