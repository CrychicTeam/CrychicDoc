package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.BoxImpl;
import java.io.IOException;

public class FileTypeBox extends BoxImpl {

    public static final String BRAND_ISO_BASE_MEDIA = "isom";

    public static final String BRAND_ISO_BASE_MEDIA_2 = "iso2";

    public static final String BRAND_ISO_BASE_MEDIA_3 = "iso3";

    public static final String BRAND_MP4_1 = "mp41";

    public static final String BRAND_MP4_2 = "mp42";

    public static final String BRAND_MOBILE_MP4 = "mmp4";

    public static final String BRAND_QUICKTIME = "qm  ";

    public static final String BRAND_AVC = "avc1";

    public static final String BRAND_AUDIO = "M4A ";

    public static final String BRAND_AUDIO_2 = "M4B ";

    public static final String BRAND_AUDIO_ENCRYPTED = "M4P ";

    public static final String BRAND_MP7 = "mp71";

    protected String majorBrand;

    protected String minorVersion;

    protected String[] compatibleBrands;

    public FileTypeBox() {
        super("File Type Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        this.majorBrand = in.readString(4);
        this.minorVersion = in.readString(4);
        this.compatibleBrands = new String[(int) this.getLeft(in) / 4];
        for (int i = 0; i < this.compatibleBrands.length; i++) {
            this.compatibleBrands[i] = in.readString(4);
        }
    }

    public String getMajorBrand() {
        return this.majorBrand;
    }

    public String getMinorVersion() {
        return this.minorVersion;
    }

    public String[] getCompatibleBrands() {
        return this.compatibleBrands;
    }
}