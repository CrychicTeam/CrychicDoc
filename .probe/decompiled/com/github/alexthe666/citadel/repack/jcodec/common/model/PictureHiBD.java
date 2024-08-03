package com.github.alexthe666.citadel.repack.jcodec.common.model;

public class PictureHiBD {

    private ColorSpace color;

    private int width;

    private int height;

    private int[][] data;

    private Rect crop;

    private int bitDepth;

    public static PictureHiBD createPicture(int width, int height, int[][] data, ColorSpace color) {
        return new PictureHiBD(width, height, data, color, 8, new Rect(0, 0, width, height));
    }

    public static PictureHiBD createPictureWithDepth(int width, int height, int[][] data, ColorSpace color, int bitDepth) {
        return new PictureHiBD(width, height, data, color, bitDepth, new Rect(0, 0, width, height));
    }

    public static PictureHiBD createPictureCropped(int width, int height, int[][] data, ColorSpace color, Rect crop) {
        return new PictureHiBD(width, height, data, color, 8, crop);
    }

    public PictureHiBD(int width, int height, int[][] data, ColorSpace color, int bitDepth, Rect crop) {
        this.width = width;
        this.height = height;
        this.data = data;
        this.color = color;
        this.crop = crop;
        this.bitDepth = bitDepth;
    }

    public static PictureHiBD clonePicture(PictureHiBD other) {
        return new PictureHiBD(other.width, other.height, other.data, other.color, other.bitDepth, other.crop);
    }

    public static PictureHiBD create(int width, int height, ColorSpace colorSpace) {
        return doCreate(width, height, colorSpace, 8, null);
    }

    public static PictureHiBD createWithDepth(int width, int height, ColorSpace colorSpace, int bitDepth) {
        return doCreate(width, height, colorSpace, bitDepth, null);
    }

    public static PictureHiBD createCropped(int width, int height, ColorSpace colorSpace, Rect crop) {
        return doCreate(width, height, colorSpace, 8, crop);
    }

    public static PictureHiBD doCreate(int width, int height, ColorSpace colorSpace, int bitDepth, Rect crop) {
        int[] planeSizes = new int[4];
        for (int i = 0; i < colorSpace.nComp; i++) {
            planeSizes[colorSpace.compPlane[i]] = planeSizes[colorSpace.compPlane[i]] + (width >> colorSpace.compWidth[i]) * (height >> colorSpace.compHeight[i]);
        }
        int nPlanes = 0;
        for (int i = 0; i < 4; i++) {
            nPlanes += planeSizes[i] != 0 ? 1 : 0;
        }
        int[][] data = new int[nPlanes][];
        int i = 0;
        for (int plane = 0; i < 4; i++) {
            if (planeSizes[i] != 0) {
                data[plane++] = new int[planeSizes[i]];
            }
        }
        return new PictureHiBD(width, height, data, colorSpace, 8, crop);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int[] getPlaneData(int plane) {
        return this.data[plane];
    }

    public ColorSpace getColor() {
        return this.color;
    }

    public int[][] getData() {
        return this.data;
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

    public boolean compatible(PictureHiBD src) {
        return src.color == this.color && src.width == this.width && src.height == this.height;
    }

    public PictureHiBD createCompatible() {
        return create(this.width, this.height, this.color);
    }

    public void copyFrom(PictureHiBD src) {
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

    public PictureHiBD cropped() {
        if (this.crop != null && (this.crop.getX() != 0 || this.crop.getY() != 0 || this.crop.getWidth() != this.width || this.crop.getHeight() != this.height)) {
            PictureHiBD result = create(this.crop.getWidth(), this.crop.getHeight(), this.color);
            for (int plane = 0; plane < this.color.nComp; plane++) {
                if (this.data[plane] != null) {
                    this.cropSub(this.data[plane], this.crop.getX() >> this.color.compWidth[plane], this.crop.getY() >> this.color.compHeight[plane], this.crop.getWidth() >> this.color.compWidth[plane], this.crop.getHeight() >> this.color.compHeight[plane], this.width >> this.color.compWidth[plane], result.data[plane]);
                }
            }
            return result;
        } else {
            return this;
        }
    }

    private void cropSub(int[] src, int x, int y, int w, int h, int srcStride, int[] tgt) {
        int srcOff = y * srcStride + x;
        int dstOff = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                tgt[dstOff + j] = src[srcOff + j];
            }
            srcOff += srcStride;
            dstOff += w;
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

    public void setBitDepth(int bitDepth) {
        this.bitDepth = bitDepth;
    }

    public int getBitDepth() {
        return this.bitDepth;
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof PictureHiBD) {
            PictureHiBD other = (PictureHiBD) obj;
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

    private boolean planeEquals(PictureHiBD other, int plane) {
        int cw = this.color.compWidth[plane];
        int ch = this.color.compHeight[plane];
        int offA = other.getCrop() == null ? 0 : (other.getCrop().getX() >> cw) + (other.getCrop().getY() >> ch) * (other.getWidth() >> cw);
        int offB = this.crop == null ? 0 : (this.crop.getX() >> cw) + (this.crop.getY() >> ch) * (this.width >> cw);
        int[] planeData = other.getPlaneData(plane);
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
}