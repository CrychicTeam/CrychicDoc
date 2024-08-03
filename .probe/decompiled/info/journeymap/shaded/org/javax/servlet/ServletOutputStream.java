package info.journeymap.shaded.org.javax.servlet;

import java.io.CharConversionException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public abstract class ServletOutputStream extends OutputStream {

    private static final String LSTRING_FILE = "info.journeymap.shaded.org.javax.servlet.LocalStrings";

    private static ResourceBundle lStrings = ResourceBundle.getBundle("info.journeymap.shaded.org.javax.servlet.LocalStrings");

    protected ServletOutputStream() {
    }

    public void print(String s) throws IOException {
        if (s == null) {
            s = "null";
        }
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if ((c & '\uff00') != 0) {
                String errMsg = lStrings.getString("err.not_iso8859_1");
                Object[] errArgs = new Object[] { c };
                errMsg = MessageFormat.format(errMsg, errArgs);
                throw new CharConversionException(errMsg);
            }
            this.write(c);
        }
    }

    public void print(boolean b) throws IOException {
        String msg;
        if (b) {
            msg = lStrings.getString("value.true");
        } else {
            msg = lStrings.getString("value.false");
        }
        this.print(msg);
    }

    public void print(char c) throws IOException {
        this.print(String.valueOf(c));
    }

    public void print(int i) throws IOException {
        this.print(String.valueOf(i));
    }

    public void print(long l) throws IOException {
        this.print(String.valueOf(l));
    }

    public void print(float f) throws IOException {
        this.print(String.valueOf(f));
    }

    public void print(double d) throws IOException {
        this.print(String.valueOf(d));
    }

    public void println() throws IOException {
        this.print("\r\n");
    }

    public void println(String s) throws IOException {
        this.print(s);
        this.println();
    }

    public void println(boolean b) throws IOException {
        this.print(b);
        this.println();
    }

    public void println(char c) throws IOException {
        this.print(c);
        this.println();
    }

    public void println(int i) throws IOException {
        this.print(i);
        this.println();
    }

    public void println(long l) throws IOException {
        this.print(l);
        this.println();
    }

    public void println(float f) throws IOException {
        this.print(f);
        this.println();
    }

    public void println(double d) throws IOException {
        this.print(d);
        this.println();
    }

    public abstract boolean isReady();

    public abstract void setWriteListener(WriteListener var1);
}