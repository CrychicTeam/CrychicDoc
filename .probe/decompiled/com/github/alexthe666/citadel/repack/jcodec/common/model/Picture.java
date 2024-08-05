package com.github.alexthe666.citadel.repack.jcodec.common.model;

import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;
import java.util.Arrays;

public class Picture {

    private ColorSpace color;

    private int width;

    private int height;

    private byte[][] data;

    private byte[][] lowBits;

    private int lowBitsNum;

    private Rect crop;

    public static Picture createPicture(int width, int height, byte[][] data, ColorSpace color) {
        return new Picture(width, height, data, (byte[][]) null, color, 0, new Rect(0, 0, width, height));
    }

    public static Picture createPictureHiBD(int width, int height, byte[][] data, byte[][] lowBits, ColorSpace color, int lowBitsNum) {
        return new Picture(width, height, data, lowBits, color, lowBitsNum, new Rect(0, 0, width, height));
    }

    public Picture(int width, int height, byte[][] data, byte[][] lowBits, ColorSpace color, int lowBitsNum, Rect crop) {
        this.width = width;
        this.height = height;
        this.data = data;
        this.lowBits = lowBits;
        this.color = color;
        this.lowBitsNum = lowBitsNum;
        this.crop = crop;
        if (color != null) {
            for (int i = 0; i < color.nComp; i++) {
                int mask = 255 >> 8 - color.compWidth[i];
                if ((width & mask) != 0) {
                    throw new IllegalArgumentException("Component " + i + " width should be a multiple of " + (1 << color.compWidth[i]) + " for colorspace: " + color);
                }
                if (crop != null && (crop.getWidth() & mask) != 0) {
                    throw new IllegalArgumentException("Component " + i + " cropped width should be a multiple of " + (1 << color.compWidth[i]) + " for colorspace: " + color);
                }
                mask = 255 >> 8 - color.compHeight[i];
                if ((height & mask) != 0) {
                    throw new IllegalArgumentException("Component " + i + " height should be a multiple of " + (1 << color.compHeight[i]) + " for colorspace: " + color);
                }
                if (crop != null && (crop.getHeight() & mask) != 0) {
                    throw new IllegalArgumentException("Component " + i + " cropped height should be a multiple of " + (1 << color.compHeight[i]) + " for colorspace: " + color);
                }
            }
        }
    }

    public static Picture copyPicture(Picture other) {
        return new Picture(other.width, other.height, other.data, other.lowBits, other.color, 0, other.crop);
    }

    public static Picture create(int width, int height, ColorSpace colorSpace) {
        return createCropped(width, height, colorSpace, null);
    }

    public static Picture createCropped(int width, int height, ColorSpace colorSpace, Rect crop) {
        int[] planeSizes = new int[4];
        for (int i = 0; i < colorSpace.nComp; i++) {
            planeSizes[colorSpace.compPlane[i]] = planeSizes[colorSpace.compPlane[i]] + (width >> colorSpace.compWidth[i]) * (height >> colorSpace.compHeight[i]);
        }
        int nPlanes = 0;
        for (int i = 0; i < 4; i++) {
            nPlanes += planeSizes[i] != 0 ? 1 : 0;
        }
        byte[][] data = new byte[nPlanes][];
        int i = 0;
        for (int plane = 0; i < 4; i++) {
            if (planeSizes[i] != 0) {
                data[plane++] = new byte[planeSizes[i]];
            }
        }
        return new Picture(width, height, data, (byte[][]) null, colorSpace, 0, crop);
    }

    public static Picture createCroppedHiBD(int width, int height, int lowBitsNum, ColorSpace colorSpace, Rect crop) {
        Picture result = createCropped(width, height, colorSpace, crop);
        if (lowBitsNum <= 0) {
            return result;
        } else {
            byte[][] data = result.getData();
            int nPlanes = data.length;
            byte[][] lowBits = new byte[nPlanes][];
            int i = 0;
            for (int plane = 0; i < nPlanes; i++) {
                lowBits[plane++] = new byte[data[i].length];
            }
            result.setLowBits(lowBits);
            result.setLowBitsNum(lowBitsNum);
            return result;
        }
    }

    private void setLowBitsNum(int lowBitsNum) {
        this.lowBitsNum = lowBitsNum;
    }

    private void setLowBits(byte[][] lowBits) {
        this.lowBits = lowBits;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public byte[] getPlaneData(int plane) {
        return this.data[plane];
    }

    public ColorSpace getColor() {
        return this.color;
    }

    public byte[][] getData() {
        return this.data;
    }

    public byte[][] getLowBits() {
        return this.lowBits;
    }

    public Rect getCrop() {
        return this.crop;
    }

    public int getPlaneWidth(int plane) {
        return this.width >> this.color.compWidth[plane];
    }

    public int getPlaneHeight(int plane) {
        return this.height >> this.color.compHeight[plane];
    }

    public boolean compatible(Picture src) {
        return src.color == this.color && src.width == this.width && src.height == this.height;
    }

    public Picture createCompatible() {
        return create(this.width, this.height, this.color);
    }

    public void copyFrom(Picture src) {
        if (!this.compatible(src)) {
            throw new IllegalArgumentException("Can not copy to incompatible picture");
        } else {
            for (int plane = 0; plane < this.color.nComp; plane++) {
                if (this.data[plane] != null) {
                    System.arraycopy(src.data[plane], 0, this.data[plane], 0, (this.width >> this.color.compWidth[plane]) * (this.height >> this.color.compHeight[plane]));
                }
            }
        }
    }

    public Picture cloneCropped() {
        if (this.cropNeeded()) {
            return this.cropped();
        } else {
            Picture clone = this.createCompatible();
            clone.copyFrom(this);
            return clone;
        }
    }

    public Picture cropped() {
        if (!this.cropNeeded()) {
            return this;
        } else {
            Picture result = create(this.crop.getWidth(), this.crop.getHeight(), this.color);
            if (this.color.planar) {
                for (int plane = 0; plane < this.data.length; plane++) {
                    if (this.data[plane] != null) {
                        this.cropSub(this.data[plane], this.crop.getX() >> this.color.compWidth[plane], this.crop.getY() >> this.color.compHeight[plane], this.crop.getWidth() >> this.color.compWidth[plane], this.crop.getHeight() >> this.color.compHeight[plane], this.width >> this.color.compWidth[plane], this.crop.getWidth() >> this.color.compWidth[plane], result.data[plane]);
                    }
                }
            } else {
                this.cropSub(this.data[0], this.crop.getX(), this.crop.getY(), this.crop.getWidth(), this.crop.getHeight(), this.width * this.color.nComp, this.crop.getWidth() * this.color.nComp, result.data[0]);
            }
            return result;
        }
    }

    protected boolean cropNeeded() {
        return this.crop != null && (this.crop.getX() != 0 || this.crop.getY() != 0 || this.crop.getWidth() != this.width || this.crop.getHeight() != this.height);
    }

    private void cropSub(byte[] src, int x, int y, int w, int h, int srcStride, int dstStride, byte[] tgt) {
        int srcOff = y * srcStride + x;
        int dstOff = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < dstStride; j++) {
                tgt[dstOff + j] = src[srcOff + j];
            }
            srcOff += srcStride;
            dstOff += dstStride;
        }
    }

    public void setCrop(Rect crop) {
        this.crop = crop;
    }

    public int getCroppedWidth() {
        return this.crop == null ? this.width : this.crop.getWidth();
    }

    public int getCroppedHeight() {
        return this.crop == null ? this.height : this.crop.getHeight();
    }

    public int getLowBitsNum() {
        return this.lowBitsNum;
    }

    public static Picture fromPictureHiBD(PictureHiBD pic) {
        int lowBitsNum = pic.getBitDepth() - 8;
        int lowBitsRound = 1 << lowBitsNum >> 1;
        Picture result = createCroppedHiBD(pic.getWidth(), pic.getHeight(), lowBitsNum, pic.getColor(), pic.getCrop());
        for (int i = 0; i < Math.min(pic.getData().length, result.getData().length); i++) {
            for (int j = 0; j < Math.min(pic.getData()[i].length, result.getData()[i].length); j++) {
                int val = pic.getData()[i][j];
                int round = MathUtil.clip(val + lowBitsRound >> lowBitsNum, 0, 255);
                result.getData()[i][j] = (byte) (round - 128);
            }
        }
        byte[][] lowBits = result.getLowBits();
        if (lowBits != null) {
            for (int i = 0; i < Math.min(pic.getData().length, result.getData().length); i++) {
                for (int j = 0; j < Math.min(pic.getData()[i].length, result.getData()[i].length); j++) {
                    int val = pic.getData()[i][j];
                    int round = MathUtil.clip(val + lowBitsRound >> lowBitsNum, 0, 255);
                    lowBits[i][j] = (byte) (val - (round << 2));
                }
            }
        }
        return result;
    }

    public PictureHiBD toPictureHiBD() {
        PictureHiBD create = PictureHiBD.doCreate(this.width, this.height, this.color, this.lowBitsNum + 8, this.crop);
        return this.toPictureHiBDInternal(create);
    }

    public PictureHiBD toPictureHiBDWithBuffer(int[][] buffer) {
        PictureHiBD create = new PictureHiBD(this.width, this.height, buffer, this.color, this.lowBitsNum + 8, this.crop);
        return this.toPictureHiBDInternal(create);
    }

    private PictureHiBD toPictureHiBDInternal(PictureHiBD pic) {
        int[][] dstData = pic.getData();
        for (int i = 0; i < this.data.length; i++) {
            int planeSize = this.getPlaneWidth(i) * this.getPlaneHeight(i);
            for (int j = 0; j < planeSize; j++) {
                dstData[i][j] = this.data[i][j] + 128 << this.lowBitsNum;
            }
        }
        if (this.lowBits != null) {
            for (int i = 0; i < this.lowBits.length; i++) {
                int planeSize = this.getPlaneWidth(i) * this.getPlaneHeight(i);
                for (int j = 0; j < planeSize; j++) {
                    dstData[i][j] = dstData[i][j] + this.lowBits[i][j];
                }
            }
        }
        return pic;
    }

    public void fill(int val) {
        for (int i = 0; i < this.data.length; i++) {
            Arrays.fill(this.data[i], (byte) val);
        }
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Picture) {
            Picture other = (Picture) obj;
            if (other.getCroppedWidth() == this.getCroppedWidth() && other.getCroppedHeight() == this.getCroppedHeight() && other.getColor() == this.color) {
                for (int i = 0; i < this.getData().length; i++) {
                    if (!this.planeEquals(other, i)) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean planeEquals(Picture other, int plane) {
        int cw = this.color.compWidth[plane];
        int ch = this.color.compHeight[plane];
        int offA = other.getCrop() == null ? 0 : (other.getCrop().getX() >> cw) + (other.getCrop().getY() >> ch) * (other.getWidth() >> cw);
        int offB = this.crop == null ? 0 : (this.crop.getX() >> cw) + (this.crop.getY() >> ch) * (this.width >> cw);
        byte[] planeData = other.getPlaneData(plane);
        for (int i = 0; i < this.getCroppedHeight() >> ch; offB += this.width >> cw) {
            for (int j = 0; j < this.getCroppedWidth() >> cw; j++) {
                if (planeData[offA + j] != this.data[plane][offB + j]) {
                    return false;
                }
            }
            i++;
            offA += other.getWidth() >> cw;
        }
        return true;
    }

    public int getStartX() {
        return this.crop == null ? 0 : this.crop.getX();
    }

    public int getStartY() {
        return this.crop == null ? 0 : this.crop.getY();
    }

    public boolean isHiBD() {
        return this.lowBits != null;
    }

    public Size getSize() {
        return new Size(this.width, this.height);
    }
}