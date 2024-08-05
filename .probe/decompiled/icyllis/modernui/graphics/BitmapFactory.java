package icyllis.modernui.graphics;

import icyllis.arc3d.core.ColorSpace;
import icyllis.arc3d.core.ImageInfo;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Core;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.lwjgl.stb.STBIEOFCallback;
import org.lwjgl.stb.STBIIOCallbacks;
import org.lwjgl.stb.STBIReadCallback;
import org.lwjgl.stb.STBISkipCallback;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public final class BitmapFactory {

    private static void validate(BitmapFactory.Options opts) {
        if (opts != null) {
            if (opts.inPreferredColorSpace != null) {
                if (!(opts.inPreferredColorSpace instanceof ColorSpace.Rgb rgbColorSpace)) {
                    throw new IllegalArgumentException("The destination color space must use the RGB color model");
                }
                if (rgbColorSpace.getTransferParameters() == null) {
                    throw new IllegalArgumentException("The destination color space must use an ICC parametric transfer function");
                }
            }
        }
    }

    @NonNull
    public static Bitmap decodeFile(@NonNull File file) throws IOException {
        return decodeFile(file, null);
    }

    @NonNull
    public static Bitmap decodeFile(@NonNull File file, @Nullable BitmapFactory.Options opts) throws IOException {
        validate(opts);
        if (opts != null && opts.inDecodeMimeType) {
            decodeMimeType(opts, file);
        }
        FileInputStream stream = new FileInputStream(file);
        Bitmap bm;
        try {
            bm = decodeSeekableChannel(stream.getChannel(), opts, false);
        } catch (Throwable var7) {
            try {
                stream.close();
            } catch (Throwable var6) {
                var7.addSuppressed(var6);
            }
            throw var7;
        }
        stream.close();
        assert bm != null;
        return bm;
    }

    public static void decodeFileInfo(@NonNull File file, @NonNull BitmapFactory.Options opts) throws IOException {
        if (opts.inDecodeMimeType) {
            decodeMimeType(opts, file);
        }
        FileInputStream stream = new FileInputStream(file);
        try {
            decodeSeekableChannel(stream.getChannel(), opts, true);
        } catch (Throwable var6) {
            try {
                stream.close();
            } catch (Throwable var5) {
                var6.addSuppressed(var5);
            }
            throw var6;
        }
        stream.close();
    }

    @NonNull
    public static Bitmap decodePath(@NonNull java.nio.file.Path path) throws IOException {
        return decodePath(path, null);
    }

    @NonNull
    public static Bitmap decodePath(@NonNull java.nio.file.Path path, @Nullable BitmapFactory.Options opts) throws IOException {
        validate(opts);
        if (opts != null && opts.inDecodeMimeType) {
            decodeMimeType(opts, path.toFile());
        }
        FileChannel channel = FileChannel.open(path, StandardOpenOption.READ);
        Bitmap bm;
        try {
            bm = decodeSeekableChannel(channel, opts, false);
        } catch (Throwable var7) {
            if (channel != null) {
                try {
                    channel.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }
            }
            throw var7;
        }
        if (channel != null) {
            channel.close();
        }
        assert bm != null;
        return bm;
    }

    public static void decodePathInfo(@NonNull java.nio.file.Path path, @NonNull BitmapFactory.Options opts) throws IOException {
        if (opts.inDecodeMimeType) {
            decodeMimeType(opts, path.toFile());
        }
        FileChannel channel = FileChannel.open(path, StandardOpenOption.READ);
        try {
            decodeSeekableChannel(channel, opts, true);
        } catch (Throwable var6) {
            if (channel != null) {
                try {
                    channel.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }
            }
            throw var6;
        }
        if (channel != null) {
            channel.close();
        }
    }

    @NonNull
    public static Bitmap decodeStream(@NonNull InputStream stream) throws IOException {
        return decodeStream(stream, null);
    }

    @NonNull
    public static Bitmap decodeStream(@NonNull InputStream stream, @Nullable BitmapFactory.Options opts) throws IOException {
        validate(opts);
        Bitmap bm;
        if (stream.getClass() == FileInputStream.class) {
            FileChannel ch = ((FileInputStream) stream).getChannel();
            if (opts != null && opts.inDecodeMimeType) {
                long pos = ch.position();
                decodeMimeType(opts, stream);
                ch.position(pos);
            }
            bm = decodeSeekableChannel(ch, opts, false);
        } else {
            ByteBuffer p = null;
            try {
                p = Core.readIntoNativeBuffer(stream);
                bm = decodeBuffer(p.rewind(), opts);
            } finally {
                MemoryUtil.memFree(p);
            }
        }
        assert bm != null;
        return bm;
    }

    public static void decodeStreamInfo(@NonNull InputStream stream, @NonNull BitmapFactory.Options opts) throws IOException {
        if (stream.getClass() == FileInputStream.class) {
            FileChannel ch = ((FileInputStream) stream).getChannel();
            if (opts.inDecodeMimeType) {
                long pos = ch.position();
                decodeMimeType(opts, stream);
                ch.position(pos);
            }
            decodeSeekableChannel(ch, opts, true);
        } else {
            ByteBuffer p = null;
            try {
                p = Core.readIntoNativeBuffer(stream);
                decodeBufferInfo(p.rewind(), opts);
            } finally {
                MemoryUtil.memFree(p);
            }
        }
    }

    @NonNull
    public static Bitmap decodeChannel(@NonNull ReadableByteChannel channel) throws IOException {
        return decodeChannel(channel, null);
    }

    @NonNull
    public static Bitmap decodeChannel(@NonNull ReadableByteChannel channel, @Nullable BitmapFactory.Options opts) throws IOException {
        validate(opts);
        Bitmap bm;
        if (channel instanceof SeekableByteChannel ch) {
            if (opts != null && opts.inDecodeMimeType) {
                long pos = ch.position();
                decodeMimeType(opts, Channels.newInputStream(channel));
                ch.position(pos);
            }
            bm = decodeSeekableChannel(ch, opts, false);
        } else {
            ByteBuffer p = null;
            try {
                p = Core.readIntoNativeBuffer(channel);
                bm = decodeBuffer(p.rewind(), opts);
            } finally {
                MemoryUtil.memFree(p);
            }
        }
        assert bm != null;
        return bm;
    }

    public static void decodeChannelInfo(@NonNull ReadableByteChannel channel, @NonNull BitmapFactory.Options opts) throws IOException {
        if (channel instanceof SeekableByteChannel ch) {
            if (opts.inDecodeMimeType) {
                long pos = ch.position();
                decodeMimeType(opts, Channels.newInputStream(channel));
                ch.position(pos);
            }
            decodeSeekableChannel(ch, opts, true);
        } else {
            ByteBuffer p = null;
            try {
                p = Core.readIntoNativeBuffer(channel);
                decodeBufferInfo(p.rewind(), opts);
            } finally {
                MemoryUtil.memFree(p);
            }
        }
    }

    @NonNull
    public static Bitmap decodeByteArray(byte[] data, int offset, int length) throws IOException {
        return (Bitmap) Objects.requireNonNull(decodeByteArray(data, offset, length, null));
    }

    @NonNull
    public static Bitmap decodeByteArray(byte[] data, int offset, int length, @Nullable BitmapFactory.Options opts) throws IOException {
        if ((offset | length) >= 0 && data.length >= offset + length) {
            validate(opts);
            if (opts != null && opts.inDecodeMimeType) {
                decodeMimeType(opts, new ByteArrayInputStream(data, offset, length));
            }
            ByteBuffer p = null;
            Bitmap bm;
            try {
                p = MemoryUtil.memAlloc(length);
                bm = decodeBuffer0(p.put(data, offset, length).rewind(), opts);
            } finally {
                MemoryUtil.memFree(p);
            }
            return bm;
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    @NonNull
    public static Bitmap decodeBuffer(@NonNull ByteBuffer buffer, @Nullable BitmapFactory.Options opts) throws IOException {
        validate(opts);
        assert buffer.isDirect();
        if (opts != null && opts.inDecodeMimeType) {
            byte[] seek = new byte[Math.min(buffer.limit(), 96)];
            buffer.get(0, seek, 0, seek.length);
            decodeMimeType(opts, new ByteArrayInputStream(seek));
        }
        return decodeBuffer0(buffer, opts);
    }

    public static void decodeBufferInfo(@NonNull ByteBuffer buffer, @NonNull BitmapFactory.Options opts) throws IOException {
        assert buffer.isDirect();
        if (opts.inDecodeMimeType) {
            byte[] seek = new byte[Math.min(buffer.limit(), 96)];
            buffer.get(0, seek, 0, seek.length);
            decodeMimeType(opts, new ByteArrayInputStream(seek));
        }
        boolean isHDR = STBImage.nstbi_is_hdr_from_memory(MemoryUtil.memAddress(buffer), buffer.remaining()) != 0;
        boolean isU16 = !isHDR && STBImage.nstbi_is_16_bit_from_memory(MemoryUtil.memAddress(buffer), buffer.remaining()) != 0;
        decodeInfo0(null, buffer, opts, null, isU16, isHDR);
    }

    @NonNull
    private static Bitmap decodeBuffer0(@NonNull ByteBuffer buffer, @Nullable BitmapFactory.Options opts) throws IOException {
        boolean isU16;
        boolean isHDR;
        if (opts != null && opts.inPreferredFormat != null) {
            isU16 = opts.inPreferredFormat.isChannelU16();
            isHDR = opts.inPreferredFormat.isChannelHDR();
        } else {
            isHDR = STBImage.nstbi_is_hdr_from_memory(MemoryUtil.memAddress(buffer), buffer.remaining()) != 0;
            isU16 = !isHDR && STBImage.nstbi_is_16_bit_from_memory(MemoryUtil.memAddress(buffer), buffer.remaining()) != 0;
        }
        return decode0(null, buffer, opts, null, isU16, isHDR);
    }

    @Nullable
    private static Bitmap decodeSeekableChannel(@NonNull SeekableByteChannel channel, @Nullable BitmapFactory.Options opts, boolean info) throws IOException {
        final boolean[] eof = new boolean[] { false };
        final IOException[] ioe = new IOException[] { null };
        var readcb = new STBIReadCallback() {

            public int invoke(long user, long data, int size) {
                if (eof[0]) {
                    return 0;
                } else {
                    int n;
                    try {
                        n = channel.read(STBIReadCallback.getData(data, size));
                    } catch (IOException var8) {
                        ioe[0] = var8;
                        eof[0] = true;
                        return 0;
                    }
                    if (n < 0) {
                        eof[0] = eof[0] | true;
                        return 0;
                    } else {
                        return n;
                    }
                }
            }
        };
        var skipcb = new STBISkipCallback() {

            public void invoke(long user, int n) {
                if (!eof[0]) {
                    try {
                        channel.position(channel.position() + (long) n);
                    } catch (IOException var5) {
                        ioe[0] = var5;
                        eof[0] = true;
                    }
                }
            }
        };
        var eofcb = new STBIEOFCallback() {

            public int invoke(long user) {
                return eof[0] ? 1 : 0;
            }
        };
        MemoryStack stack = MemoryStack.stackPush();
        Bitmap var25;
        label160: {
            try {
                label162: {
                    Object var9 = readcb;
                    try {
                        Object var10 = skipcb;
                        label145: {
                            try {
                                label165: {
                                    Object var11 = eofcb;
                                    try {
                                        STBIIOCallbacks callbacks = STBIIOCallbacks.mallocStack(stack).set(readcb, skipcb, eofcb);
                                        boolean isU16;
                                        boolean isHDR;
                                        if (!info && opts != null && opts.inPreferredFormat != null) {
                                            isU16 = opts.inPreferredFormat.isChannelU16();
                                            isHDR = opts.inPreferredFormat.isChannelHDR();
                                        } else {
                                            long start = channel.position();
                                            isHDR = STBImage.nstbi_is_hdr_from_callbacks(callbacks.address(), 0L) != 0;
                                            channel.position(start);
                                            if (isHDR) {
                                                isU16 = false;
                                            } else {
                                                isU16 = STBImage.nstbi_is_16_bit_from_callbacks(callbacks.address(), 0L) != 0;
                                                channel.position(start);
                                            }
                                        }
                                        if (!info) {
                                            var25 = decode0(callbacks, null, opts, ioe, isU16, isHDR);
                                            break label165;
                                        }
                                        assert opts != null;
                                        decodeInfo0(callbacks, null, opts, ioe, isU16, isHDR);
                                        var25 = null;
                                    } catch (Throwable var21) {
                                        if (eofcb != null) {
                                            try {
                                                var11.close();
                                            } catch (Throwable var20) {
                                                var21.addSuppressed(var20);
                                            }
                                        }
                                        throw var21;
                                    }
                                    if (eofcb != null) {
                                        eofcb.close();
                                    }
                                    break label145;
                                }
                                if (eofcb != null) {
                                    eofcb.close();
                                }
                            } catch (Throwable var22) {
                                if (skipcb != null) {
                                    try {
                                        var10.close();
                                    } catch (Throwable var19) {
                                        var22.addSuppressed(var19);
                                    }
                                }
                                throw var22;
                            }
                            if (skipcb != null) {
                                skipcb.close();
                            }
                            break label162;
                        }
                        if (skipcb != null) {
                            skipcb.close();
                        }
                    } catch (Throwable var23) {
                        if (readcb != null) {
                            try {
                                var9.close();
                            } catch (Throwable var18) {
                                var23.addSuppressed(var18);
                            }
                        }
                        throw var23;
                    }
                    if (readcb != null) {
                        readcb.close();
                    }
                    break label160;
                }
                if (readcb != null) {
                    readcb.close();
                }
            } catch (Throwable var24) {
                if (stack != null) {
                    try {
                        stack.close();
                    } catch (Throwable var17) {
                        var24.addSuppressed(var17);
                    }
                }
                throw var24;
            }
            if (stack != null) {
                stack.close();
            }
            return var25;
        }
        if (stack != null) {
            stack.close();
        }
        return var25;
    }

    @NonNull
    private static Bitmap decode0(@Nullable STBIIOCallbacks callbacks, @Nullable ByteBuffer buffer, @Nullable BitmapFactory.Options opts, @Nullable IOException[] ioe, boolean isU16, boolean isHDR) throws IOException {
        assert callbacks != null || buffer != null;
        Bitmap.Format requiredFormat = null;
        if (opts != null && opts.inPreferredFormat != null) {
            requiredFormat = opts.inPreferredFormat;
        }
        MemoryStack stack = MemoryStack.stackPush();
        Bitmap var20;
        try {
            long pOuts = stack.nmalloc(4, 12);
            int requiredChannels = requiredFormat != null ? requiredFormat.getChannels() : 0;
            long address;
            if (callbacks != null) {
                long cbk = callbacks.address();
                if (isHDR) {
                    address = STBImage.nstbi_loadf_from_callbacks(cbk, 0L, pOuts, pOuts + 4L, pOuts + 8L, requiredChannels);
                } else if (isU16) {
                    address = STBImage.nstbi_load_16_from_callbacks(cbk, 0L, pOuts, pOuts + 4L, pOuts + 8L, requiredChannels);
                } else {
                    address = STBImage.nstbi_load_from_callbacks(cbk, 0L, pOuts, pOuts + 4L, pOuts + 8L, requiredChannels);
                }
            } else {
                long buf = MemoryUtil.memAddress(buffer);
                int len = buffer.remaining();
                if (isHDR) {
                    address = STBImage.nstbi_loadf_from_memory(buf, len, pOuts, pOuts + 4L, pOuts + 8L, requiredChannels);
                } else if (isU16) {
                    address = STBImage.nstbi_load_16_from_memory(buf, len, pOuts, pOuts + 4L, pOuts + 8L, requiredChannels);
                } else {
                    address = STBImage.nstbi_load_from_memory(buf, len, pOuts, pOuts + 4L, pOuts + 8L, requiredChannels);
                }
            }
            if (address == 0L) {
                throw new IOException("Failed to decode image: " + STBImage.stbi_failure_reason());
            }
            if (ioe != null && ioe[0] != null) {
                throw new IOException("Failed to read image", ioe[0]);
            }
            int width = MemoryUtil.memGetInt(pOuts);
            int height = MemoryUtil.memGetInt(pOuts + 4L);
            int channels_in_file = MemoryUtil.memGetInt(pOuts + 8L);
            Bitmap.Format format = requiredFormat != null ? requiredFormat : Bitmap.Format.get(channels_in_file, isU16, isHDR);
            ColorSpace cs = isHDR ? ColorSpace.get(ColorSpace.Named.LINEAR_EXTENDED_SRGB) : ColorSpace.get(ColorSpace.Named.SRGB);
            if (opts != null) {
                opts.outWidth = width;
                opts.outHeight = height;
                opts.outFormat = format;
                opts.outColorSpace = cs;
            }
            int at = !isHDR && (channels_in_file == 2 || channels_in_file == 4) && format.hasAlpha() ? 3 : 1;
            Bitmap bitmap = new Bitmap(format, ImageInfo.make(width, height, format.getColorType(), at, cs), address, width * format.getBytesPerPixel(), STBImage::nstbi_image_free);
            bitmap.setImmutable();
            var20 = bitmap;
        } catch (Throwable var22) {
            if (stack != null) {
                try {
                    stack.close();
                } catch (Throwable var21) {
                    var22.addSuppressed(var21);
                }
            }
            throw var22;
        }
        if (stack != null) {
            stack.close();
        }
        return var20;
    }

    private static void decodeInfo0(@Nullable STBIIOCallbacks callbacks, @Nullable ByteBuffer buffer, @NonNull BitmapFactory.Options opts, @Nullable IOException[] ioe, boolean isU16, boolean isHDR) throws IOException {
        assert callbacks != null || buffer != null;
        MemoryStack stack = MemoryStack.stackPush();
        try {
            long pOuts = stack.nmalloc(4, 12);
            boolean success;
            if (callbacks != null) {
                success = STBImage.nstbi_info_from_callbacks(callbacks.address(), 0L, pOuts, pOuts + 4L, pOuts + 8L) != 0;
            } else {
                success = STBImage.nstbi_info_from_memory(MemoryUtil.memAddress(buffer), buffer.remaining(), pOuts, pOuts + 4L, pOuts + 8L) != 0;
            }
            if (!success) {
                throw new IOException("Failed to decode image: " + STBImage.stbi_failure_reason());
            }
            if (ioe != null && ioe[0] != null) {
                throw new IOException("Failed to read image", ioe[0]);
            }
            int width = MemoryUtil.memGetInt(pOuts);
            int height = MemoryUtil.memGetInt(pOuts + 4L);
            int channels_in_file = MemoryUtil.memGetInt(pOuts + 8L);
            Bitmap.Format format = Bitmap.Format.get(channels_in_file, isU16, isHDR);
            ColorSpace cs = isHDR ? ColorSpace.get(ColorSpace.Named.LINEAR_EXTENDED_SRGB) : ColorSpace.get(ColorSpace.Named.SRGB);
            opts.outWidth = width;
            opts.outHeight = height;
            opts.outFormat = format;
            opts.outColorSpace = cs;
        } catch (Throwable var16) {
            if (stack != null) {
                try {
                    stack.close();
                } catch (Throwable var15) {
                    var16.addSuppressed(var15);
                }
            }
            throw var16;
        }
        if (stack != null) {
            stack.close();
        }
    }

    public static void decodeMimeType(@NonNull BitmapFactory.Options opts, @NonNull Object input) {
        try {
            ImageInputStream stream = ImageIO.createImageInputStream(input);
            label75: {
                try {
                    if (stream != null) {
                        Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);
                        if (readers.hasNext()) {
                            String[] mimeTypes = ((ImageReader) readers.next()).getOriginatingProvider().getMIMETypes();
                            if (mimeTypes != null) {
                                opts.outMimeType = mimeTypes[0];
                                break label75;
                            }
                        }
                        if (test(stream, BitmapFactory::filterPSD)) {
                            opts.outMimeType = "image/vnd.adobe.photoshop";
                        } else if (test(stream, BitmapFactory::filterPIC)) {
                            opts.outMimeType = "image/x-pict";
                        } else if (test(stream, BitmapFactory::filterPGM)) {
                            opts.outMimeType = "image/x-portable-graymap";
                        } else if (test(stream, BitmapFactory::filterPPM)) {
                            opts.outMimeType = "image/x-portable-pixmap";
                        } else if (test(stream, BitmapFactory::filterHDR)) {
                            opts.outMimeType = "image/vnd.radiance";
                        } else if (test(stream, BitmapFactory::filterTGA)) {
                            opts.outMimeType = "image/x-tga";
                        }
                    }
                } catch (Throwable var6) {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (Throwable var5) {
                            var6.addSuppressed(var5);
                        }
                    }
                    throw var6;
                }
                if (stream != null) {
                    stream.close();
                }
                return;
            }
            if (stream != null) {
                stream.close();
            }
        } catch (Exception var7) {
        }
    }

    public static boolean test(@NonNull ImageInputStream stream, @NonNull Predicate<ImageInputStream> filter) {
        try {
            stream.mark();
            boolean e;
            try {
                e = filter.test(stream);
            } finally {
                stream.reset();
            }
            return e;
        } catch (Exception var7) {
            return false;
        }
    }

    public static boolean filterPSD(@NonNull ImageInputStream stream) {
        try {
            return stream.read() == 56 && stream.read() == 66 && stream.read() == 80 && stream.read() == 83;
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static boolean filterPIC(@NonNull ImageInputStream stream) {
        try {
            if (stream.read() == 83 && stream.read() == 128 && stream.read() == 246 && stream.read() == 52) {
                stream.seek(stream.getStreamPosition() + 84L);
                return stream.read() == 80 && stream.read() == 73 && stream.read() == 67 && stream.read() == 84;
            } else {
                return false;
            }
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static boolean filterPGM(@NonNull ImageInputStream stream) {
        try {
            return stream.read() == 80 && stream.read() == 53;
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static boolean filterPPM(@NonNull ImageInputStream stream) {
        try {
            return stream.read() == 80 && stream.read() == 54;
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static boolean filterHDR(@NonNull ImageInputStream stream) {
        try {
            return stream.read() == 35 && stream.read() == 63 && stream.read() == 82 && stream.read() == 65 && stream.read() == 68 && stream.read() == 73 && stream.read() == 65 && stream.read() == 78 && stream.read() == 67 && stream.read() == 69 && stream.read() == 10;
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static boolean filterTGA(@NonNull ImageInputStream stream) {
        try {
            stream.read();
            int color_map_type = stream.read();
            if (color_map_type != 0 && color_map_type != 1) {
                return false;
            } else {
                int data_type_code = stream.read();
                if (color_map_type == 1) {
                    if (data_type_code != 1 && data_type_code != 9) {
                        return false;
                    }
                    stream.readInt();
                    int color_map_depth = stream.read();
                    if (color_map_depth != 16 && color_map_depth != 24 && color_map_depth != 32) {
                        return false;
                    }
                } else {
                    if (data_type_code != 2 && data_type_code != 3 && data_type_code != 10 && data_type_code != 11) {
                        return false;
                    }
                    stream.readInt();
                    stream.read();
                }
                stream.readInt();
                stream.readInt();
                int bits_per_pixel = stream.read();
                return color_map_type == 1 && bits_per_pixel != 8 && bits_per_pixel != 16 ? false : bits_per_pixel == 16 || bits_per_pixel == 24 || bits_per_pixel == 32;
            }
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        }
    }

    public static class Options {

        public boolean inDecodeMimeType;

        public Bitmap.Format inPreferredFormat = null;

        public ColorSpace inPreferredColorSpace = null;

        public int outWidth;

        public int outHeight;

        public String outMimeType;

        public Bitmap.Format outFormat;

        public ColorSpace outColorSpace;
    }
}