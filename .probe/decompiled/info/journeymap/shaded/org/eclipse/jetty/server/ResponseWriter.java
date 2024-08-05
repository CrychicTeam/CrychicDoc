package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.io.EofException;
import info.journeymap.shaded.org.eclipse.jetty.io.RuntimeIOException;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.util.Formatter;
import java.util.Locale;

public class ResponseWriter extends PrintWriter {

    private static final Logger LOG = Log.getLogger(ResponseWriter.class);

    private static final String __lineSeparator = System.getProperty("line.separator");

    private static final String __trueln = "true" + __lineSeparator;

    private static final String __falseln = "false" + __lineSeparator;

    private final HttpWriter _httpWriter;

    private final Locale _locale;

    private final String _encoding;

    private IOException _ioException;

    private boolean _isClosed = false;

    private Formatter _formatter;

    public ResponseWriter(HttpWriter httpWriter, Locale locale, String encoding) {
        super(httpWriter, false);
        this._httpWriter = httpWriter;
        this._locale = locale;
        this._encoding = encoding;
    }

    public boolean isFor(Locale locale, String encoding) {
        if (this._locale == null && locale != null) {
            return false;
        } else {
            return this._encoding == null && encoding != null ? false : this._encoding.equalsIgnoreCase(encoding) && this._locale.equals(locale);
        }
    }

    protected void reopen() {
        synchronized (this.lock) {
            this._isClosed = false;
            this.clearError();
            this.out = this._httpWriter;
        }
    }

    protected void clearError() {
        synchronized (this.lock) {
            this._ioException = null;
            super.clearError();
        }
    }

    public boolean checkError() {
        synchronized (this.lock) {
            return this._ioException != null || super.checkError();
        }
    }

    private void setError(Throwable th) {
        super.setError();
        if (th instanceof IOException) {
            this._ioException = (IOException) th;
        } else {
            this._ioException = new IOException(String.valueOf(th));
            this._ioException.initCause(th);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(th);
        }
    }

    protected void setError() {
        this.setError(new IOException());
    }

    private void isOpen() throws IOException {
        if (this._ioException != null) {
            throw new RuntimeIOException(this._ioException);
        } else if (this._isClosed) {
            throw new EofException("Stream closed");
        }
    }

    public void flush() {
        try {
            synchronized (this.lock) {
                this.isOpen();
                this.out.flush();
            }
        } catch (IOException var4) {
            this.setError(var4);
        }
    }

    public void close() {
        try {
            synchronized (this.lock) {
                this.out.close();
                this._isClosed = true;
            }
        } catch (IOException var4) {
            this.setError(var4);
        }
    }

    public void write(int c) {
        try {
            synchronized (this.lock) {
                this.isOpen();
                this.out.write(c);
            }
        } catch (InterruptedIOException var5) {
            LOG.debug(var5);
            Thread.currentThread().interrupt();
        } catch (IOException var6) {
            this.setError(var6);
        }
    }

    public void write(char[] buf, int off, int len) {
        try {
            synchronized (this.lock) {
                this.isOpen();
                this.out.write(buf, off, len);
            }
        } catch (InterruptedIOException var7) {
            LOG.debug(var7);
            Thread.currentThread().interrupt();
        } catch (IOException var8) {
            this.setError(var8);
        }
    }

    public void write(char[] buf) {
        this.write(buf, 0, buf.length);
    }

    public void write(String s, int off, int len) {
        try {
            synchronized (this.lock) {
                this.isOpen();
                this.out.write(s, off, len);
            }
        } catch (InterruptedIOException var7) {
            LOG.debug(var7);
            Thread.currentThread().interrupt();
        } catch (IOException var8) {
            this.setError(var8);
        }
    }

    public void write(String s) {
        this.write(s, 0, s.length());
    }

    public void print(boolean b) {
        this.write(b ? "true" : "false");
    }

    public void print(char c) {
        this.write(c);
    }

    public void print(int i) {
        this.write(String.valueOf(i));
    }

    public void print(long l) {
        this.write(String.valueOf(l));
    }

    public void print(float f) {
        this.write(String.valueOf(f));
    }

    public void print(double d) {
        this.write(String.valueOf(d));
    }

    public void print(char[] s) {
        this.write(s);
    }

    public void print(String s) {
        if (s == null) {
            s = "null";
        }
        this.write(s);
    }

    public void print(Object obj) {
        this.write(String.valueOf(obj));
    }

    public void println() {
        try {
            synchronized (this.lock) {
                this.isOpen();
                this.out.write(__lineSeparator);
            }
        } catch (InterruptedIOException var4) {
            LOG.debug(var4);
            Thread.currentThread().interrupt();
        } catch (IOException var5) {
            this.setError(var5);
        }
    }

    public void println(boolean b) {
        this.println(b ? __trueln : __falseln);
    }

    public void println(char c) {
        try {
            synchronized (this.lock) {
                this.isOpen();
                this.out.write(c);
            }
        } catch (InterruptedIOException var5) {
            LOG.debug(var5);
            Thread.currentThread().interrupt();
        } catch (IOException var6) {
            this.setError(var6);
        }
    }

    public void println(int x) {
        this.println(String.valueOf(x));
    }

    public void println(long x) {
        this.println(String.valueOf(x));
    }

    public void println(float x) {
        this.println(String.valueOf(x));
    }

    public void println(double x) {
        this.println(String.valueOf(x));
    }

    public void println(char[] s) {
        try {
            synchronized (this.lock) {
                this.isOpen();
                this.out.write(s, 0, s.length);
                this.out.write(__lineSeparator);
            }
        } catch (InterruptedIOException var5) {
            LOG.debug(var5);
            Thread.currentThread().interrupt();
        } catch (IOException var6) {
            this.setError(var6);
        }
    }

    public void println(String s) {
        if (s == null) {
            s = "null";
        }
        try {
            synchronized (this.lock) {
                this.isOpen();
                this.out.write(s, 0, s.length());
                this.out.write(__lineSeparator);
            }
        } catch (InterruptedIOException var5) {
            LOG.debug(var5);
            Thread.currentThread().interrupt();
        } catch (IOException var6) {
            this.setError(var6);
        }
    }

    public void println(Object x) {
        this.println(String.valueOf(x));
    }

    public PrintWriter printf(String format, Object... args) {
        return this.format(this._locale, format, args);
    }

    public PrintWriter printf(Locale l, String format, Object... args) {
        return this.format(l, format, args);
    }

    public PrintWriter format(String format, Object... args) {
        return this.format(this._locale, format, args);
    }

    public PrintWriter format(Locale l, String format, Object... args) {
        try {
            synchronized (this.lock) {
                this.isOpen();
                if (this._formatter == null || this._formatter.locale() != l) {
                    this._formatter = new Formatter(this, l);
                }
                this._formatter.format(l, format, args);
            }
        } catch (InterruptedIOException var7) {
            LOG.debug(var7);
            Thread.currentThread().interrupt();
        } catch (IOException var8) {
            this.setError(var8);
        }
        return this;
    }
}