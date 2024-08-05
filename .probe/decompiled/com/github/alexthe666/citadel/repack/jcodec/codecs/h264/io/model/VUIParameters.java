package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model;

public class VUIParameters {

    public boolean aspectRatioInfoPresentFlag;

    public int sarWidth;

    public int sarHeight;

    public boolean overscanInfoPresentFlag;

    public boolean overscanAppropriateFlag;

    public boolean videoSignalTypePresentFlag;

    public int videoFormat;

    public boolean videoFullRangeFlag;

    public boolean colourDescriptionPresentFlag;

    public int colourPrimaries;

    public int transferCharacteristics;

    public int matrixCoefficients;

    public boolean chromaLocInfoPresentFlag;

    public int chromaSampleLocTypeTopField;

    public int chromaSampleLocTypeBottomField;

    public boolean timingInfoPresentFlag;

    public int numUnitsInTick;

    public int timeScale;

    public boolean fixedFrameRateFlag;

    public boolean lowDelayHrdFlag;

    public boolean picStructPresentFlag;

    public HRDParameters nalHRDParams;

    public HRDParameters vclHRDParams;

    public VUIParameters.BitstreamRestriction bitstreamRestriction;

    public AspectRatio aspectRatio;

    public static class BitstreamRestriction {

        public boolean motionVectorsOverPicBoundariesFlag;

        public int maxBytesPerPicDenom;

        public int maxBitsPerMbDenom;

        public int log2MaxMvLengthHorizontal;

        public int log2MaxMvLengthVertical;

        public int numReorderFrames;

        public int maxDecFrameBuffering;
    }
}