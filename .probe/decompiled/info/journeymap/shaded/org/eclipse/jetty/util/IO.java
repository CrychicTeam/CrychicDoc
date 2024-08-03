package info.journeymap.shaded.org.eclipse.jetty.util;

import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.nio.charset.Charset;

public class IO {

    private static final Logger LOG = Log.getLogger(IO.class);

    public static final String CRLF = "\r\n";

    public static final byte[] CRLF_BYTES = new byte[] { 13, 10 };

    public static final int bufferSize = 65536;

    private static IO.NullOS __nullStream = new IO.NullOS();

    private static IO.ClosedIS __closedStream = new IO.ClosedIS();

    private static IO.NullWrite __nullWriter = new IO.NullWrite();

    private static PrintWriter __nullPrintWriter = new PrintWriter(__nullWriter);

    public static void copy(InputStream in, OutputStream out) throws IOException {
        copy(in, out, -1L);
    }

    public static void copy(Reader in, Writer out) throws IOException {
        copy(in, out, -1L);
    }

    public static void copy(InputStream in, OutputStream out, long byteCount) throws IOException {
        byte[] buffer = new byte[65536];
        int len = 65536;
        if (byteCount >= 0L) {
            while (byteCount > 0L) {
                int max = byteCount < 65536L ? (int) byteCount : 65536;
                len = in.read(buffer, 0, max);
                if (len == -1) {
                    break;
                }
                byteCount -= (long) len;
                out.write(buffer, 0, len);
            }
        } else {
            while (true) {
                len = in.read(buffer, 0, 65536);
                if (len < 0) {
                    break;
                }
                out.write(buffer, 0, len);
            }
        }
    }

    public static void copy(Reader in, Writer out, long byteCount) throws IOException {
        char[] buffer = new char[65536];
        int len = 65536;
        if (byteCount >= 0L) {
            while (byteCount > 0L) {
                if (byteCount < 65536L) {
                    len = in.read(buffer, 0, (int) byteCount);
                } else {
                    len = in.read(buffer, 0, 65536);
                }
                if (len == -1) {
                    break;
                }
                byteCount -= (long) len;
                out.write(buffer, 0, len);
            }
        } else if (out instanceof PrintWriter) {
            PrintWriter pout = (PrintWriter) out;
            while (!pout.checkError()) {
                len = in.read(buffer, 0, 65536);
                if (len == -1) {
                    break;
                }
                out.write(buffer, 0, len);
            }
        } else {
            while (true) {
                len = in.read(buffer, 0, 65536);
                if (len == -1) {
                    break;
                }
                out.write(buffer, 0, len);
            }
        }
    }

    public static void copy(File from, File to) throws IOException {
        if (from.isDirectory()) {
            copyDir(from, to);
        } else {
            copyFile(from, to);
        }
    }

    public static void copyDir(File from, File to) throws IOException {
        if (to.exists()) {
            if (!to.isDirectory()) {
                throw new IllegalArgumentException(to.toString());
            }
        } else {
            to.mkdirs();
        }
        File[] files = from.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String name = files[i].getName();
                if (!".".equals(name) && !"..".equals(name)) {
                    copy(files[i], new File(to, name));
                }
            }
        }
    }

    public static void copyFile(File from, File to) throws IOException {
        InputStream in = new FileInputStream(from);
        Throwable var3 = null;
        try {
            OutputStream out = new FileOutputStream(to);
            Throwable var5 = null;
            try {
                copy(in, out);
            } catch (Throwable var28) {
                var5 = var28;
                throw var28;
            } finally {
                if (out != null) {
                    if (var5 != null) {
                        try {
                            out.close();
                        } catch (Throwable var27) {
                            var5.addSuppressed(var27);
                        }
                    } else {
                        out.close();
                    }
                }
            }
        } catch (Throwable var30) {
            var3 = var30;
            throw var30;
        } finally {
            if (in != null) {
                if (var3 != null) {
                    try {
                        in.close();
                    } catch (Throwable var26) {
                        var3.addSuppressed(var26);
                    }
                } else {
                    in.close();
                }
            }
        }
    }

    public static String toString(InputStream in) throws IOException {
        return toString(in, (Charset) null);
    }

    public static String toString(InputStream in, String encoding) throws IOException {
        return toString(in, encoding == null ? null : Charset.forName(encoding));
    }

    public static String toString(InputStream in, Charset encoding) throws IOException {
        StringWriter writer = new StringWriter();
        InputStreamReader reader = encoding == null ? new InputStreamReader(in) : new InputStreamReader(in, encoding);
        copy(reader, writer);
        return writer.toString();
    }

    public static String toString(Reader in) throws IOException {
        StringWriter writer = new StringWriter();
        copy(in, writer);
        return writer.toString();
    }

    public static boolean delete(File file) {
        if (!file.exists()) {
            return false;
        } else {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; files != null && i < files.length; i++) {
                    delete(files[i]);
                }
            }
            return file.delete();
        }
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException var2) {
            LOG.ignore(var2);
        }
    }

    public static void close(InputStream is) {
        close((Closeable) is);
    }

    public static void close(OutputStream os) {
        close((Closeable) os);
    }

    public static void close(Reader reader) {
        close((Closeable) reader);
    }

    public static void close(Writer writer) {
        close((Closeable) writer);
    }

    public static byte[] readBytes(InputStream in) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        copy(in, bout);
        return bout.toByteArray();
    }

    public static long write(GatheringByteChannel out, ByteBuffer[] buffers, int offset, int length) throws IOException {
        long total = 0L;
        label26: while (length > 0) {
            long wrote = out.write(buffers, offset, length);
            if (wrote == 0L) {
                break;
            }
            total += wrote;
            for (int i = offset; i < buffers.length; i++) {
                if (buffers[i].hasRemaining()) {
                    length -= i - offset;
                    offset = i;
                    continue label26;
                }
            }
            length = 0;
        }
        return total;
    }

    public static OutputStream getNullStream() {
        return __nullStream;
    }

    public static InputStream getClosedStream() {
        return __closedStream;
    }

    public static Writer getNullWriter() {
        return __nullWriter;
    }

    public static PrintWriter getNullPrintWriter() {
        return __nullPrintWriter;
    }

    private static class ClosedIS extends InputStream {

        private ClosedIS() {
        }

        public int read() throws IOException {
            return -1;
        }
    }

    static class Job implements Runnable {

        InputStream in;

        OutputStream out;

        Reader read;

        Writer write;

        Job(InputStream in, OutputStream out) {
            this.in = in;
            this.out = out;
            this.read = null;
            this.write = null;
        }

        Job(Reader read, Writer write) {
            this.in = null;
            this.out = null;
            this.read = read;
            this.write = write;
        }

        public void run() {
            try {
                if (this.in != null) {
                    IO.copy(this.in, this.out, -1L);
                } else {
                    IO.copy(this.read, this.write, -1L);
                }
            } catch (IOException var4) {
                IO.LOG.ignore(var4);
                try {
                    if (this.out != null) {
                        this.out.close();
                    }
                    if (this.write != null) {
                        this.write.close();
                    }
                } catch (IOException var3) {
                    IO.LOG.ignore(var3);
                }
            }
        }
    }

    private static class NullOS extends OutputStream {

        private NullOS() {
        }

        public void close() {
        }

        public void flush() {
        }

        public void write(byte[] b) {
        }

        public void write(byte[] b, int i, int l) {
        }

        public void write(int b) {
        }
    }

    private static class NullWrite extends Writer {

        private NullWrite() {
        }

        public void close() {
        }

        public void flush() {
        }

        public void write(char[] b) {
        }

        public void write(char[] b, int o, int l) {
        }

        public void write(int b) {
        }

        public void write(String s) {
        }

        public void write(String s, int o, int l) {
        }
    }
}