package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.common.JCodecUtil2;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.LinkedList;

public class SegmentTypeBox extends Box {

    private String majorBrand;

    private int minorVersion;

    private Collection<String> compBrands = new LinkedList();

    public SegmentTypeBox(Header header) {
        super(header);
    }

    public static SegmentTypeBox createSegmentTypeBox(String majorBrand, int minorVersion, Collection<String> compBrands) {
        SegmentTypeBox styp = new SegmentTypeBox(new Header(fourcc()));
        styp.majorBrand = majorBrand;
        styp.minorVersion = minorVersion;
        styp.compBrands = compBrands;
        return styp;
    }

    public static String fourcc() {
        return "styp";
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
        int sz = 13;
        for (String string : this.compBrands) {
            sz += JCodecUtil2.asciiString(string).length;
        }
        return sz;
    }
}