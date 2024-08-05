package com.github.alexthe666.citadel.repack.jcodec.containers.mp4;

import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.FileTypeBox;
import java.util.Arrays;

public final class Brand {

    public static final Brand MOV = new Brand("qt  ", 512, new String[] { "qt  " });

    public static final Brand MP4 = new Brand("isom", 512, new String[] { "isom", "iso2", "avc1", "mp41" });

    private FileTypeBox ftyp;

    private Brand(String majorBrand, int version, String[] compatible) {
        this.ftyp = FileTypeBox.createFileTypeBox(majorBrand, version, Arrays.asList(compatible));
    }

    public FileTypeBox getFileTypeBox() {
        return this.ftyp;
    }
}