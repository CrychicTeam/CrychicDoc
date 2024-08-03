package icyllis.modernui.graphics;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import icyllis.arc3d.core.ColorSpace;
import icyllis.arc3d.core.ImageInfo;
import icyllis.arc3d.core.PixelMap;
import icyllis.arc3d.core.PixelRef;
import icyllis.arc3d.core.PixelUtils;
import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.ColorInt;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.annotation.Size;
import icyllis.modernui.annotation.WorkerThread;
import icyllis.modernui.core.Core;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.Cleaner.Cleanable;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Date;
import java.util.function.LongConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.lwjgl.PointerBuffer;
import org.lwjgl.stb.STBIWriteCallback;
import org.lwjgl.stb.STBIWriteCallbackI;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageWrite;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

public final class Bitmap implements AutoCloseable {

    public static final Marker MARKER = MarkerManager.getMarker("Bitmap");

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

    @NonNull
    private final Bitmap.Format mFormat;

    @NonNull
    private PixelMap mPixelMap;

    @Nullable
    private Bitmap.SafePixelRef mPixelRef;

    Bitmap(@NonNull Bitmap.Format format, @NonNull ImageInfo info, long addr, int rowStride, @Nullable LongConsumer freeFn) {
        this.mFormat = format;
        this.mPixelMap = new PixelMap(info, null, addr, rowStride);
        this.mPixelRef = new Bitmap.SafePixelRef(this, info, addr, rowStride, freeFn);
    }

    @NonNull
    public static Bitmap createBitmap(@Size(min = 1L, max = 32768L) int width, @Size(min = 1L, max = 32768L) int height, @NonNull Bitmap.Format format) {
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("Image dimensions " + width + "x" + height + " must be positive");
        } else if (width <= 32768 && height <= 32768) {
            int rowStride = width * format.getBytesPerPixel();
            long size = (long) rowStride * (long) height;
            long address = MemoryUtil.nmemCalloc(size, 1L);
            if (address == 0L) {
                System.gc();
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException var10) {
                    Thread.currentThread().interrupt();
                }
                address = MemoryUtil.nmemCalloc(size, 1L);
                if (address == 0L) {
                    throw new OutOfMemoryError("Failed to allocate " + size + " bytes");
                }
            }
            ColorSpace cs = format.isChannelHDR() ? ColorSpace.get(ColorSpace.Named.LINEAR_EXTENDED_SRGB) : ColorSpace.get(ColorSpace.Named.SRGB);
            int at = format.hasAlpha() && !format.isChannelHDR() ? 3 : 1;
            return new Bitmap(format, ImageInfo.make(width, height, format.getColorType(), at, cs), address, rowStride, MemoryUtil::nmemFree);
        } else {
            throw new IllegalArgumentException("Image dimensions " + width + "x" + height + " must be less than or equal to 32768");
        }
    }

    @Nullable
    public static String openDialogGet(@Nullable Bitmap.SaveFormat format, @Nullable CharSequence title, @Nullable CharSequence defaultPathAndFile) {
        MemoryStack stack = MemoryStack.stackPush();
        String var5;
        try {
            PointerBuffer filters = format != null ? format.getFilters(stack) : Bitmap.SaveFormat.getAllFilters(stack);
            var5 = TinyFileDialogs.tinyfd_openFileDialog(title, defaultPathAndFile, filters, format != null ? format.getDescription() : Bitmap.SaveFormat.getAllDescription(), false);
        } catch (Throwable var7) {
            if (stack != null) {
                try {
                    stack.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }
            }
            throw var7;
        }
        if (stack != null) {
            stack.close();
        }
        return var5;
    }

    @Nullable
    public static String[] openDialogGets(@Nullable Bitmap.SaveFormat format, @Nullable CharSequence title, @Nullable CharSequence defaultPathAndFile) {
        MemoryStack stack = MemoryStack.stackPush();
        String[] var6;
        try {
            PointerBuffer filters = format != null ? format.getFilters(stack) : Bitmap.SaveFormat.getAllFilters(stack);
            String s = TinyFileDialogs.tinyfd_openFileDialog(title, defaultPathAndFile, filters, format != null ? format.getDescription() : Bitmap.SaveFormat.getAllDescription(), true);
            var6 = s != null ? s.split("\\|") : null;
        } catch (Throwable var8) {
            if (stack != null) {
                try {
                    stack.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
            }
            throw var8;
        }
        if (stack != null) {
            stack.close();
        }
        return var6;
    }

    @Nullable
    public static String saveDialogGet(@Nullable Bitmap.SaveFormat format, @Nullable CharSequence title, @Nullable String name) {
        MemoryStack stack = MemoryStack.stackPush();
        String var5;
        try {
            PointerBuffer filters = format != null ? format.getFilters(stack) : Bitmap.SaveFormat.getAllFilters(stack);
            var5 = TinyFileDialogs.tinyfd_saveFileDialog(title, Bitmap.SaveFormat.getFileName(format, name), filters, format != null ? format.getDescription() : Bitmap.SaveFormat.getAllDescription());
        } catch (Throwable var7) {
            if (stack != null) {
                try {
                    stack.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }
            }
            throw var7;
        }
        if (stack != null) {
            stack.close();
        }
        return var5;
    }

    @Internal
    public static void flipVertically(@NonNull Bitmap bitmap) {
        assert !bitmap.isImmutable();
        int height = bitmap.getHeight();
        int rowStride = bitmap.getRowStride();
        long addr = bitmap.getAddress();
        if (addr == 0L) {
            throw new IllegalStateException("src pixels is null");
        } else {
            long temp = MemoryUtil.nmemAlloc((long) rowStride);
            if (temp == 0L) {
                throw new IllegalStateException("temp pixels is null");
            } else {
                try {
                    int i = 0;
                    for (int lim = height >> 1; i < lim; i++) {
                        int srcOff = i * rowStride;
                        int dstOff = (height - i - 1) * rowStride;
                        MemoryUtil.memCopy(addr + (long) srcOff, temp, (long) rowStride);
                        MemoryUtil.memCopy(addr + (long) dstOff, addr + (long) srcOff, (long) rowStride);
                        MemoryUtil.memCopy(temp, addr + (long) dstOff, (long) rowStride);
                    }
                } finally {
                    MemoryUtil.nmemFree(temp);
                }
            }
        }
    }

    @NonNull
    public Bitmap.Format getFormat() {
        return this.mFormat;
    }

    @NonNull
    public ImageInfo getInfo() {
        return this.mPixelMap.getInfo();
    }

    public int getChannels() {
        return this.mFormat.getChannels();
    }

    public int getWidth() {
        if (this.mPixelRef == null) {
            ModernUI.LOGGER.warn(MARKER, "Called getWidth() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return this.mPixelMap.getWidth();
    }

    public int getHeight() {
        if (this.mPixelRef == null) {
            ModernUI.LOGGER.warn(MARKER, "Called getHeight() on a recycle()'d bitmap! This is undefined behavior!");
        }
        return this.mPixelMap.getHeight();
    }

    public long getSize() {
        if (this.mPixelRef == null) {
            ModernUI.LOGGER.warn(MARKER, "Called getSize() on a recycle()'d bitmap! This is undefined behavior!");
            return 0L;
        } else {
            return (long) this.getRowStride() * (long) this.getHeight();
        }
    }

    @Internal
    public int getColorType() {
        return this.mPixelMap.getColorType();
    }

    @Internal
    public int getAlphaType() {
        return this.mPixelMap.getAlphaType();
    }

    @Internal
    public void setColorInfo(int newColorType, int newAlphaType) {
        newAlphaType = ImageInfo.validateAlphaType(newColorType, newAlphaType);
        ImageInfo oldInfo = this.getInfo();
        if (oldInfo.bytesPerPixel() != ImageInfo.bytesPerPixel(newColorType)) {
            throw new IllegalArgumentException("bpp mismatch");
        } else {
            ImageInfo newInfo = new ImageInfo(oldInfo.width(), oldInfo.height(), newColorType, newAlphaType, oldInfo.colorSpace());
            this.mPixelMap = new PixelMap(newInfo, this.mPixelMap);
        }
    }

    @Nullable
    public ColorSpace getColorSpace() {
        return this.mPixelMap.getColorSpace();
    }

    public void setColorSpace(@Nullable ColorSpace newColorSpace) {
        ImageInfo oldInfo = this.getInfo();
        ColorSpace oldColorSpace = oldInfo.colorSpace();
        if (oldColorSpace != newColorSpace) {
            if (newColorSpace != null) {
                if (!(newColorSpace instanceof ColorSpace.Rgb rgbColorSpace)) {
                    throw new IllegalArgumentException("The new ColorSpace must use the RGB color model");
                }
                if (rgbColorSpace.getTransferParameters() == null) {
                    throw new IllegalArgumentException("The new ColorSpace must use an ICC parametric transfer function");
                }
            }
            if (oldColorSpace != null && newColorSpace != null) {
                for (int i = 0; i < oldColorSpace.getComponentCount(); i++) {
                    if (oldColorSpace.getMinValue(i) < newColorSpace.getMinValue(i)) {
                        throw new IllegalArgumentException("The new ColorSpace cannot increase the minimum value for any of the components compared to the current ColorSpace. To perform this type of conversion create a new Bitmap in the desired ColorSpace and draw this Bitmap into it.");
                    }
                    if (oldColorSpace.getMaxValue(i) > newColorSpace.getMaxValue(i)) {
                        throw new IllegalArgumentException("The new ColorSpace cannot decrease the maximum value for any of the components compared to the current ColorSpace/ To perform this type of conversion create a new Bitmap in the desired ColorSpace and draw this Bitmap into it.");
                    }
                }
            }
            this.mPixelMap = new PixelMap(oldInfo.makeColorSpace(newColorSpace), this.mPixelMap);
        }
    }

    @Internal
    public long getAddress() {
        return this.mPixelRef == null ? 0L : this.mPixelMap.getAddress();
    }

    public int getRowStride() {
        if (this.mPixelRef == null) {
            throw new IllegalStateException("Can't call getRowStride() on a recycled bitmap");
        } else {
            return this.mPixelMap.getRowStride();
        }
    }

    public boolean hasAlpha() {
        assert this.mPixelRef != null;
        return !this.getInfo().isOpaque();
    }

    public boolean isImmutable() {
        if (this.mPixelRef != null) {
            return this.mPixelRef.isImmutable();
        } else {
            assert false;
            return false;
        }
    }

    public void setImmutable() {
        if (this.mPixelRef != null) {
            this.mPixelRef.setImmutable();
        }
    }

    public boolean isPremultiplied() {
        assert this.mPixelRef != null;
        return this.getAlphaType() == 2;
    }

    private void checkOutOfBounds(int x, int y) {
        if (x < 0) {
            throw new IllegalArgumentException("x " + x + " must be >= 0");
        } else if (y < 0) {
            throw new IllegalArgumentException("y " + y + " must be >= 0");
        } else if (x >= this.getWidth()) {
            throw new IllegalArgumentException("x " + x + " must be < bitmap.width() " + this.getWidth());
        } else if (y >= this.getHeight()) {
            throw new IllegalArgumentException("y " + y + " must be < bitmap.height() " + this.getHeight());
        }
    }

    @ColorInt
    public int getPixelARGB(int x, int y) {
        this.checkReleased();
        this.checkOutOfBounds(x, y);
        int c = MemoryUtil.memGetInt(this.getAddress() + (long) y * (long) this.getRowStride() + (long) x * (long) this.mFormat.getBytesPerPixel());
        if (PixelUtils.NATIVE_BIG_ENDIAN) {
            c = Integer.reverseBytes(c);
        }
        int argb = switch(this.mFormat) {
            case GRAY_8 ->
                {
                }
            case GRAY_ALPHA_88 ->
                {
                    int lum = c & 0xFF;
                    ???;
                }
            case RGB_888 ->
                0xFF000000 | (c & 0xFF) << 16 | c & 0xFF00 | c >> 16 & 0xFF;
            case RGBA_8888 ->
                c & -16711936 | (c & 0xFF) << 16 | c >> 16 & 0xFF;
            default ->
                throw new UnsupportedOperationException();
        };
        if (this.getColorSpace() != null && !this.getColorSpace().isSrgb()) {
            float[] v = new float[] { (float) Color.red(argb) / 255.0F, (float) Color.green(argb) / 255.0F, (float) Color.blue(argb) / 255.0F };
            ColorSpace.connect(this.getColorSpace()).transform(v);
            return Color.argb((float) Color.alpha(argb), v[0], v[1], v[2]);
        } else {
            return argb;
        }
    }

    @Internal
    @NonNull
    public PixelMap getPixelMap() {
        return this.mPixelMap;
    }

    @Internal
    @Nullable
    public PixelRef getPixelRef() {
        return this.mPixelRef;
    }

    @WorkerThread
    public boolean saveDialog(@NonNull Bitmap.SaveFormat format, int quality, @Nullable String name) throws IOException {
        String path = saveDialogGet(format, null, name);
        if (path != null) {
            this.saveToPath(format, quality, java.nio.file.Path.of(path));
            return true;
        } else {
            return false;
        }
    }

    @WorkerThread
    public void saveToFile(@NonNull Bitmap.SaveFormat format, int quality, @NonNull File file) throws IOException {
        this.checkReleased();
        try {
            FileOutputStream stream = new FileOutputStream(file);
            try {
                this.saveToChannel(format, quality, stream.getChannel());
            } catch (Throwable var8) {
                try {
                    stream.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
                throw var8;
            }
            stream.close();
        } catch (IOException var9) {
            throw new IOException("Failed to save image to path \"" + file.getAbsolutePath() + "\"", var9);
        }
    }

    @WorkerThread
    public void saveToPath(@NonNull Bitmap.SaveFormat format, int quality, @NonNull java.nio.file.Path path) throws IOException {
        this.checkReleased();
        try {
            FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            try {
                this.saveToChannel(format, quality, channel);
            } catch (Throwable var8) {
                if (channel != null) {
                    try {
                        channel.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }
                throw var8;
            }
            if (channel != null) {
                channel.close();
            }
        } catch (IOException var9) {
            throw new IOException("Failed to save image to path \"" + path.toAbsolutePath() + "\"", var9);
        }
    }

    @WorkerThread
    public void saveToStream(@NonNull Bitmap.SaveFormat format, int quality, @NonNull OutputStream stream) throws IOException {
        this.saveToChannel(format, quality, Channels.newChannel(stream));
    }

    @WorkerThread
    public void saveToChannel(@NonNull Bitmap.SaveFormat format, int quality, @NonNull WritableByteChannel channel) throws IOException {
        this.checkReleased();
        if (quality >= 0 && quality <= 100) {
            if (Core.isOnMainThread() || Core.isOnRenderThread()) {
                ModernUI.LOGGER.warn(MARKER, "Called save() on core thread! This will hang the application!", new Exception().fillInStackTrace());
            }
            if (this.getRowStride() != this.getWidth() * this.getFormat().getBytesPerPixel()) {
                throw new IllegalStateException("Pixel data is not tightly packed");
            } else {
                var callback = new STBIWriteCallback() {

                    private IOException exception;

                    public void invoke(long context, long data, int size) {
                        try {
                            int n = channel.write(STBIWriteCallback.getData(data, size));
                            if (n != size) {
                                this.exception = new IOException("Channel does not consume all the data");
                            }
                        } catch (IOException var7) {
                            this.exception = var7;
                        }
                    }
                };
                Object var5 = callback;
                try {
                    boolean success = format.write(callback, this.getWidth(), this.getHeight(), this.mFormat, this.getAddress(), quality);
                    if (!success) {
                        throw new IOException("Failed to encode image: " + STBImage.stbi_failure_reason());
                    }
                    if (callback.exception != null) {
                        throw new IOException("Failed to save image", callback.exception);
                    }
                } catch (Throwable var9) {
                    if (callback != null) {
                        try {
                            var5.close();
                        } catch (Throwable var8) {
                            var9.addSuppressed(var8);
                        }
                    }
                    throw var9;
                }
                if (callback != null) {
                    callback.close();
                }
            }
        } else {
            throw new IllegalArgumentException("Bad quality " + quality + ", must be 0..100");
        }
    }

    private void checkReleased() {
        if (this.mPixelRef == null) {
            throw new IllegalStateException("Cannot operate a recycled bitmap!");
        }
    }

    public void close() {
        if (this.mPixelRef != null) {
            this.mPixelRef.mCleanup.clean();
            this.mPixelRef = null;
        }
    }

    public void recycle() {
        this.close();
    }

    public boolean isClosed() {
        return this.mPixelRef == null;
    }

    public boolean isRecycled() {
        return this.mPixelRef == null;
    }

    @NonNull
    public String toString() {
        return "Bitmap{mFormat=" + this.mFormat + ", mInfo=" + this.getInfo() + ", mPixelRef=" + this.mPixelRef + "}";
    }

    public static enum Format {

        GRAY_8(1, 22),
        GRAY_ALPHA_88(2, 23),
        RGB_888(3, 4),
        RGBA_8888(4, 6),
        @Internal
        GRAY_16(1, 0),
        @Internal
        GRAY_ALPHA_1616(2, 0),
        @Internal
        RGB_161616(3, 0),
        RGBA_16161616(4, 15),
        @Internal
        GRAY_F32(1, 0),
        @Internal
        GRAY_ALPHA_F32(2, 0),
        @Internal
        RGB_F32(3, 0),
        RGBA_F32(4, 18);

        private static final Bitmap.Format[] FORMATS = values();

        private final int mChannels;

        private final int mColorType;

        private final int mBytesPerPixel;

        private Format(int chs, int ct) {
            this.mChannels = chs;
            this.mColorType = ct;
            this.mBytesPerPixel = ImageInfo.bytesPerPixel(ct);
            assert (this.ordinal() & 3) == chs - 1;
        }

        public int getChannels() {
            return this.mChannels;
        }

        public int getColorType() {
            return this.mColorType;
        }

        public int getBytesPerPixel() {
            return this.mBytesPerPixel;
        }

        public boolean isChannelU8() {
            return this.ordinal() < 4;
        }

        public boolean isChannelU16() {
            return this.ordinal() >> 2 == 1;
        }

        public boolean isChannelHDR() {
            return this.ordinal() >> 2 == 2;
        }

        public boolean hasAlpha() {
            return (this.ordinal() & 1) == 1;
        }

        @NonNull
        public static Bitmap.Format get(int chs, boolean u16, boolean hdr) {
            if (chs < 1 || chs > 4) {
                throw new IllegalArgumentException();
            } else if (u16 && hdr) {
                throw new IllegalArgumentException();
            } else {
                return FORMATS[chs - 1 | (u16 ? 4 : 0) | (hdr ? 8 : 0)];
            }
        }
    }

    private static final class SafePixelRef extends PixelRef implements Runnable {

        final Cleanable mCleanup;

        private SafePixelRef(@NonNull Bitmap owner, @NonNull ImageInfo info, long address, int rowStride, @Nullable LongConsumer freeFn) {
            super(info.width(), info.height(), null, address, rowStride, freeFn);
            this.mCleanup = Core.registerCleanup(owner, this);
        }

        public void run() {
            this.unref();
        }
    }

    public static enum SaveFormat {

        PNG("*.png") {

            @Override
            public boolean write(@NonNull STBIWriteCallbackI func, int width, int height, @NonNull Bitmap.Format format, long data, int quality) throws IOException {
                if (!format.isChannelU8()) {
                    throw new IOException("Only 8-bit per channel images can be saved as " + this + ", found " + format);
                } else {
                    return STBImageWrite.nstbi_write_png_to_func(func.address(), 0L, width, height, format.getChannels(), data, 0) != 0;
                }
            }
        }
        ,
        TGA("*.tga", "*.icb", "*.vda", "*.vst") {

            @Override
            public boolean write(@NonNull STBIWriteCallbackI func, int width, int height, @NonNull Bitmap.Format format, long data, int quality) throws IOException {
                if (!format.isChannelU8()) {
                    throw new IOException("Only 8-bit per channel images can be saved as " + this + ", found " + format);
                } else {
                    return STBImageWrite.nstbi_write_tga_to_func(func.address(), 0L, width, height, format.getChannels(), data) != 0;
                }
            }
        }
        ,
        BMP("*.bmp", "*.dib") {

            @Override
            public boolean write(@NonNull STBIWriteCallbackI func, int width, int height, @NonNull Bitmap.Format format, long data, int quality) throws IOException {
                if (!format.isChannelU8()) {
                    throw new IOException("Only 8-bit per channel images can be saved as " + this + ", found " + format);
                } else {
                    return STBImageWrite.nstbi_write_bmp_to_func(func.address(), 0L, width, height, format.getChannels(), data) != 0;
                }
            }
        }
        ,
        JPEG("*.jpg", "*.jpeg", "*.jpe", "*.jif", "*.jfif", "*.jfi") {

            @Override
            public boolean write(@NonNull STBIWriteCallbackI func, int width, int height, @NonNull Bitmap.Format format, long data, int quality) throws IOException {
                if (!format.isChannelU8()) {
                    throw new IOException("Only 8-bit per channel images can be saved as " + this + ", found " + format);
                } else {
                    return STBImageWrite.nstbi_write_jpg_to_func(func.address(), 0L, width, height, format.getChannels(), data, quality) != 0;
                }
            }
        }
        ,
        HDR("*.hdr") {

            @Override
            public boolean write(@NonNull STBIWriteCallbackI func, int width, int height, @NonNull Bitmap.Format format, long data, int quality) throws IOException {
                if (!format.isChannelHDR()) {
                    throw new IOException("Only 32-bit per channel images can be saved as " + this + ", found " + format);
                } else {
                    return STBImageWrite.nstbi_write_hdr_to_func(func.address(), 0L, width, height, format.getChannels(), data) != 0;
                }
            }
        }
        ,
        RAW("*.bin") {

            @Override
            public boolean write(@NonNull STBIWriteCallbackI func, int width, int height, @NonNull Bitmap.Format format, long data, int quality) throws IOException {
                func.invoke(0L, data, width * height * format.getBytesPerPixel());
                return true;
            }
        }
        ;

        private static final Bitmap.SaveFormat[] OPEN_FORMATS;

        private static final String[] EXTRA_FILTERS = new String[] { "*.psd", "*.gif", "*.pic", "*.pnm", "*.pgm", "*.ppm" };

        @NonNull
        private final String[] filters;

        private SaveFormat(@NonNull String... filters) {
            this.filters = filters;
        }

        public abstract boolean write(@NonNull STBIWriteCallbackI var1, int var2, int var3, @NonNull Bitmap.Format var4, long var5, int var7) throws IOException;

        @NonNull
        public static PointerBuffer getAllFilters(@NonNull MemoryStack stack) {
            int length = EXTRA_FILTERS.length;
            for (Bitmap.SaveFormat format : OPEN_FORMATS) {
                length += format.filters.length;
            }
            PointerBuffer buffer = stack.mallocPointer(length);
            for (Bitmap.SaveFormat format : OPEN_FORMATS) {
                for (String filter : format.filters) {
                    stack.nUTF8(filter, true);
                    buffer.put(stack.getPointerAddress());
                }
            }
            for (String filter : EXTRA_FILTERS) {
                stack.nUTF8(filter, true);
                buffer.put(stack.getPointerAddress());
            }
            return (PointerBuffer) buffer.rewind();
        }

        @NonNull
        public static String getAllDescription() {
            return getAllDescription("Image Files");
        }

        @NonNull
        public static String getAllDescription(@NonNull String header) {
            return header + " (" + (String) Stream.concat(Arrays.stream(OPEN_FORMATS).flatMap(f -> Arrays.stream(f.filters)), Arrays.stream(EXTRA_FILTERS)).sorted().collect(Collectors.joining(";")) + ")";
        }

        @NonNull
        public PointerBuffer getFilters(@NonNull MemoryStack stack) {
            PointerBuffer buffer = stack.mallocPointer(this.filters.length);
            for (String filter : this.filters) {
                stack.nUTF8(filter, true);
                buffer.put(stack.getPointerAddress());
            }
            return (PointerBuffer) buffer.rewind();
        }

        @NonNull
        public String getDescription() {
            return this.name() + " (" + String.join(";", this.filters) + ")";
        }

        @NonNull
        public static String getFileName(@Nullable Bitmap.SaveFormat format, @Nullable String name) {
            String s = name != null ? name : "image-" + Bitmap.DATE_FORMAT.format(new Date());
            return format != null ? s + format.filters[0].substring(1) : s;
        }

        static {
            Bitmap.SaveFormat[] values = values();
            OPEN_FORMATS = (Bitmap.SaveFormat[]) Arrays.copyOf(values, values.length - 1);
        }
    }
}