package com.github.alexthe666.citadel.repack.jcodec.common.io;

import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class IOUtils {

    public static final int DEFAULT_BUFFER_SIZE = 4096;

    public static void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException var2) {
            }
        }
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    public static int copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[4096];
        int count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static int copyDumb(InputStream input, OutputStream output) throws IOException {
        int count = 0;
        int n;
        for (n = 0; -1 != (n = input.read()); count++) {
            output.write(n);
        }
        return count;
    }

    public static byte[] readFileToByteArray(File file) throws IOException {
        return NIOUtils.toArray(NIOUtils.fetchFromFile(file));
    }

    public static String readToString(InputStream is) throws IOException {
        return Platform.stringFromBytes(toByteArray(is));
    }

    public static void writeStringToFile(File file, String str) throws IOException {
        NIOUtils.writeTo(ByteBuffer.wrap(str.getBytes()), file);
    }

    public static void forceMkdir(File directory) throws IOException {
        if (directory.exists()) {
            if (!directory.isDirectory()) {
                String message = "File " + directory + " exists and is not a directory. Unable to create directory.";
                throw new IOException(message);
            }
        } else if (!directory.mkdirs() && !directory.isDirectory()) {
            String message = "Unable to create directory " + directory;
            throw new IOException(message);
        }
    }

    public static void copyFile(File src, File dst) throws IOException {
        FileChannelWrapper _in = null;
        FileChannelWrapper out = null;
        try {
            _in = NIOUtils.readableChannel(src);
            out = NIOUtils.writableChannel(dst);
            NIOUtils.copy(_in, out, Long.MAX_VALUE);
        } finally {
            NIOUtils.closeQuietly(_in);
            NIOUtils.closeQuietly(out);
        }
    }
}