package me.lucko.spark.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.LongConsumer;
import java.util.zip.GZIPOutputStream;

public enum Compression {

    GZIP {

        @Override
        public Path compress(Path file, LongConsumer progressHandler) throws IOException {
            Path compressedFile = file.getParent().resolve(file.getFileName().toString() + ".gz");
            InputStream in = Files.newInputStream(file);
            try {
                OutputStream out = Files.newOutputStream(compressedFile);
                try {
                    GZIPOutputStream compressionOut = new GZIPOutputStream(out, 65536);
                    try {
                        Compression.copy(in, compressionOut, progressHandler);
                    } catch (Throwable var12) {
                        try {
                            compressionOut.close();
                        } catch (Throwable var11) {
                            var12.addSuppressed(var11);
                        }
                        throw var12;
                    }
                    compressionOut.close();
                } catch (Throwable var13) {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Throwable var10) {
                            var13.addSuppressed(var10);
                        }
                    }
                    throw var13;
                }
                if (out != null) {
                    out.close();
                }
            } catch (Throwable var14) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Throwable var9) {
                        var14.addSuppressed(var9);
                    }
                }
                throw var14;
            }
            if (in != null) {
                in.close();
            }
            return compressedFile;
        }
    }
    ;

    private Compression() {
    }

    public abstract Path compress(Path var1, LongConsumer var2) throws IOException;

    private static long copy(InputStream from, OutputStream to, LongConsumer progress) throws IOException {
        byte[] buf = new byte[65536];
        long total = 0L;
        long iterations = 0L;
        while (true) {
            int r = from.read(buf);
            if (r == -1) {
                return total;
            }
            to.write(buf, 0, r);
            total += (long) r;
            if (iterations++ % 80L == 0L) {
                progress.accept(total);
            }
        }
    }
}