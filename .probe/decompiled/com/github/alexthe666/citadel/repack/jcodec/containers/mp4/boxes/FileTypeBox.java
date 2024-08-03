package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.common.JCodecUtil2;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.LinkedList;

public class FileTypeBox extends Box {

    private String majorBrand;

    private int minorVersion;

    private Collection<String> compBrands = new LinkedList();

    public FileTypeBox(Header header) {
        super(header);
    }

    public static String fourcc() {
        return "ftyp";
    }

    public static FileTypeBox createFileTypeBox(String majorBrand, int minorVersion, Collection<String> compBrands) {
        FileTypeBox ftyp = new FileTypeBox(new Header(fourcc()));
        ftyp.majorBrand = majorBrand;
        ftyp.minorVersion = minorVersion;
        ftyp.compBrands = compBrands;
        return ftyp;
    }

    @Override
    public void parse(ByteBuffer input) {
        this.majorBrand = NIOUtils.readString(input, 4);
        this.minorVersion = input.getInt();
        String brand;
        while (input.hasRemaining() && (brand = NIOUtils.readString(input, 4)) != null) {
            this.compBrands.add(brand);
        }
    }

    public String getMajorBrand() {
        return this.majorBrand;
    }

    public Collection<String> getCompBrands() {
        return this.compBrands;
    }

    @Override
    public void doWrite(ByteBuffer out) {
        out.put(JCodecUtil2.asciiString(this.majorBrand));
        out.putInt(this.minorVersion);
        for (String string : this.compBrands) {
            out.put(JCodecUtil2.asciiString(string));
        }
    }

    @Override
    public int estimateSize() {
        int size = 13;
        for (String string : this.compBrands) {
            size += JCodecUtil2.asciiString(string).length;
        }
        return size;
    }
}