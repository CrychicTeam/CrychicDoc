package info.journeymap.shaded.org.javax.servlet;

import java.io.IOException;
import java.io.InputStream;

public abstract class ServletInputStream extends InputStream {

    protected ServletInputStream() {
    }

    public int readLine(byte[] b, int off, int len) throws IOException {
        if (len <= 0) {
            return 0;
        } else {
            int count = 0;
            int c;
            while ((c = this.read()) != -1) {
                b[off++] = (byte) c;
                if (c == 10 || ++count == len) {
                    break;
                }
            }
            return count > 0 ? count : -1;
        }
    }

    public abstract boolean isFinished();

    public abstract boolean isReady();

    public abstract void setReadListener(ReadListener var1);
}