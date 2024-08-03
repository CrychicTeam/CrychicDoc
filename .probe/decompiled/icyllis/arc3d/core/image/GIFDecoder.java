package icyllis.arc3d.core.image;

import java.io.EOFException;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GIFDecoder {

    public static volatile int sDefaultDelayMillis = 40;

    private final ByteBuffer mBuf;

    private final int mHeaderPos;

    private final int mScreenWidth;

    private final int mScreenHeight;

    @Nullable
    private final byte[] mGlobalPalette;

    private final byte[] mImage;

    @Nullable
    private byte[] mTmpPalette;

    private final byte[] mTmpImage;

    private final int[] mTmpInterlace;

    public GIFDecoder(ByteBuffer buf) throws IOException {
        this.mBuf = buf;
        int b;
        if (this.readByte() == 71 && this.readByte() == 73 && this.readByte() == 70 && this.readByte() == 56 && ((b = this.readByte()) == 55 || b == 57) && this.readByte() == 97) {
            this.mScreenWidth = this.readShort();
            this.mScreenHeight = this.readShort();
            int packedField = this.readByte();
            this.skipBytes(2);
            if ((packedField & 128) != 0) {
                this.mGlobalPalette = this.readPalette(2 << (packedField & 7), -1, null);
            } else {
                this.mGlobalPalette = null;
            }
            this.mImage = new byte[this.mScreenWidth * this.mScreenHeight * 4];
            this.mTmpImage = new byte[this.mScreenWidth * this.mScreenHeight];
            this.mTmpInterlace = new int[this.mScreenHeight];
            this.mHeaderPos = this.mBuf.position();
        } else {
            throw new IOException("Not GIF");
        }
    }

    public static boolean checkMagic(@Nonnull byte[] buf) {
        return buf.length >= 6 && buf[0] == 71 && buf[1] == 73 && buf[2] == 70 && buf[3] == 56 && (buf[4] == 55 || buf[4] == 57) && buf[5] == 97;
    }

    public int getScreenWidth() {
        return this.mScreenWidth;
    }

    public int getScreenHeight() {
        return this.mScreenHeight;
    }

    public int decodeNextFrame(ByteBuffer pixels) throws IOException {
        int imageControlCode = this.syncNextFrame();
        if (imageControlCode < 0) {
            throw new IOException();
        } else {
            int left = this.readShort();
            int top = this.readShort();
            int width = this.readShort();
            int height = this.readShort();
            if (left + width <= this.mScreenWidth && top + height <= this.mScreenHeight) {
                int packedField = this.readByte();
                boolean isTransparent = (imageControlCode >>> 24 & 1) != 0;
                int transparentIndex = isTransparent ? imageControlCode >>> 16 & 0xFF : -1;
                boolean localPalette = (packedField & 128) != 0;
                boolean isInterlaced = (packedField & 64) != 0;
                int paletteSize = 2 << (packedField & 7);
                if (this.mTmpPalette == null || this.mTmpPalette.length < paletteSize * 4) {
                    this.mTmpPalette = new byte[paletteSize * 4];
                }
                byte[] palette = localPalette ? this.readPalette(paletteSize, transparentIndex, this.mTmpPalette) : (byte[]) Objects.requireNonNull(this.mGlobalPalette);
                int delayTime = imageControlCode & 65535;
                int disposalCode = imageControlCode >>> 26 & 7;
                this.decodeImage(this.mTmpImage, width, height, isInterlaced ? this.computeInterlaceReIndex(height, this.mTmpInterlace) : null);
                this.decodePalette(this.mTmpImage, palette, transparentIndex, left, top, width, height, disposalCode, pixels);
                return delayTime != 0 ? delayTime * 10 : sDefaultDelayMillis;
            } else {
                throw new IOException();
            }
        }
    }

    @Nonnull
    private byte[] readPalette(int size, int transparentIndex, @Nullable byte[] palette) throws IOException {
        if (palette == null) {
            palette = new byte[size * 4];
        }
        int i = 0;
        for (int iPos = 0; i < size; i++) {
            try {
                this.mBuf.get(palette, iPos, 3);
            } catch (BufferUnderflowException var7) {
                throw new EOFException();
            }
            palette[iPos + 3] = (byte) (i == transparentIndex ? 0 : -1);
            iPos += 4;
        }
        return palette;
    }

    public void skipExtension() throws IOException {
        for (int blockSize = this.readByte(); blockSize != 0; blockSize = this.readByte()) {
            this.skipBytes(blockSize);
        }
    }

    private int readControlCode() throws IOException {
        int blockSize = this.readByte();
        int packedField = this.readByte();
        int delayTime = this.readShort();
        int transparentIndex = this.readByte();
        if (blockSize == 4 && this.readByte() == 0) {
            return ((packedField & 31) << 24) + (transparentIndex << 16) + delayTime;
        } else {
            throw new IOException();
        }
    }

    private int syncNextFrame() throws IOException {
        int controlData = 0;
        boolean restarted = false;
        while (true) {
            int ch = this.read();
            switch(ch) {
                case -1:
                case 59:
                    if (restarted) {
                        return -1;
                    }
                    this.mBuf.position(this.mHeaderPos);
                    controlData = 0;
                    restarted = true;
                    break;
                case 33:
                    if (this.readByte() == 249) {
                        controlData = this.readControlCode();
                    } else {
                        this.skipExtension();
                    }
                    break;
                case 44:
                    return controlData;
                default:
                    throw new IOException(String.valueOf(ch));
            }
        }
    }

    private void decodeImage(byte[] image, int width, int height, @Nullable int[] interlace) throws IOException {
        LZWDecoder dec = LZWDecoder.getInstance();
        byte[] data = dec.setData(this.mBuf, this.readByte());
        int y = 0;
        int iPos = 0;
        int xr = width;
        while (true) {
            int len = dec.readString();
            if (len == -1) {
                this.skipExtension();
                return;
            }
            int pos = 0;
            while (pos < len) {
                int ax = Math.min(xr, len - pos);
                System.arraycopy(data, pos, image, iPos, ax);
                iPos += ax;
                pos += ax;
                if ((xr -= ax) == 0) {
                    if (++y == height) {
                        this.skipExtension();
                        return;
                    }
                    int iY = interlace == null ? y : interlace[y];
                    iPos = iY * width;
                    xr = width;
                }
            }
        }
    }

    @Nonnull
    private int[] computeInterlaceReIndex(int height, int[] data) {
        int pos = 0;
        for (int i = 0; i < height; i += 8) {
            data[pos++] = i;
        }
        for (int i = 4; i < height; i += 8) {
            data[pos++] = i;
        }
        for (int i = 2; i < height; i += 4) {
            data[pos++] = i;
        }
        for (int i = 1; i < height; i += 2) {
            data[pos++] = i;
        }
        return data;
    }

    private void restoreToBackground(byte[] image, int left, int top, int width, int height) {
        for (int y = 0; y < height; y++) {
            int iPos = ((top + y) * this.mScreenWidth + left) * 4;
            for (int x = 0; x < width; x++) {
                image[iPos + 3] = 0;
                iPos += 4;
            }
        }
    }

    private void decodePalette(byte[] srcImage, byte[] palette, int transparentIndex, int left, int top, int width, int height, int disposalCode, ByteBuffer pixels) {
        if (disposalCode == 3) {
            pixels.put(this.mImage);
            for (int y = 0; y < height; y++) {
                int iPos = ((top + y) * this.mScreenWidth + left) * 4;
                int i = y * width;
                if (transparentIndex < 0) {
                    for (int x = 0; x < width; x++) {
                        int index = 255 & srcImage[i + x];
                        pixels.put(iPos, palette, index * 4, 4);
                        iPos += 4;
                    }
                } else {
                    for (int x = 0; x < width; x++) {
                        int index = 255 & srcImage[i + x];
                        if (index != transparentIndex) {
                            pixels.put(iPos, palette, index * 4, 4);
                        }
                        iPos += 4;
                    }
                }
            }
            pixels.rewind();
        } else {
            byte[] image = this.mImage;
            for (int yx = 0; yx < height; yx++) {
                int iPos = ((top + yx) * this.mScreenWidth + left) * 4;
                int i = yx * width;
                if (transparentIndex < 0) {
                    for (int x = 0; x < width; x++) {
                        int index = 255 & srcImage[i + x];
                        System.arraycopy(palette, index * 4, image, iPos, 4);
                        iPos += 4;
                    }
                } else {
                    for (int x = 0; x < width; x++) {
                        int index = 255 & srcImage[i + x];
                        if (index != transparentIndex) {
                            System.arraycopy(palette, index * 4, image, iPos, 4);
                        }
                        iPos += 4;
                    }
                }
            }
            pixels.put(image).rewind();
            if (disposalCode == 2) {
                this.restoreToBackground(this.mImage, left, top, width, height);
            }
        }
    }

    private int read() {
        try {
            return this.mBuf.get() & 0xFF;
        } catch (BufferUnderflowException var2) {
            return -1;
        }
    }

    public int readByte() throws IOException {
        try {
            return this.mBuf.get() & 0xFF;
        } catch (BufferUnderflowException var2) {
            throw new EOFException();
        }
    }

    private int readShort() throws IOException {
        int lsb = this.readByte();
        int msb = this.readByte();
        return lsb + (msb << 8);
    }

    private void skipBytes(int n) throws IOException {
        try {
            this.mBuf.position(this.mBuf.position() + n);
        } catch (IllegalArgumentException var3) {
            throw new EOFException();
        }
    }
}