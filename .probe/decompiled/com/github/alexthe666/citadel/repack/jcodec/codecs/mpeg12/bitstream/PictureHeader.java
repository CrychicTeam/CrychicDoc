package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream;

import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitWriter;
import java.nio.ByteBuffer;

public class PictureHeader implements MPEGHeader {

    public int temporal_reference;

    public int picture_coding_type;

    public int vbv_delay;

    public int full_pel_forward_vector;

    public int forward_f_code;

    public int full_pel_backward_vector;

    public int backward_f_code;

    public QuantMatrixExtension quantMatrixExtension;

    public CopyrightExtension copyrightExtension;

    public PictureDisplayExtension pictureDisplayExtension;

    public PictureCodingExtension pictureCodingExtension;

    public PictureSpatialScalableExtension pictureSpatialScalableExtension;

    public PictureTemporalScalableExtension pictureTemporalScalableExtension;

    private boolean _hasExtensions;

    public static PictureHeader createPictureHeader(int temporal_reference, int picture_coding_type, int vbv_delay, int full_pel_forward_vector, int forward_f_code, int full_pel_backward_vector, int backward_f_code) {
        PictureHeader p = new PictureHeader();
        p.temporal_reference = temporal_reference;
        p.picture_coding_type = picture_coding_type;
        p.vbv_delay = vbv_delay;
        p.full_pel_forward_vector = full_pel_forward_vector;
        p.forward_f_code = forward_f_code;
        p.full_pel_backward_vector = full_pel_backward_vector;
        p.backward_f_code = backward_f_code;
        return p;
    }

    private PictureHeader() {
    }

    public static PictureHeader read(ByteBuffer bb) {
        BitReader _in = BitReader.createBitReader(bb);
        PictureHeader ph = new PictureHeader();
        ph.temporal_reference = _in.readNBit(10);
        ph.picture_coding_type = _in.readNBit(3);
        ph.vbv_delay = _in.readNBit(16);
        if (ph.picture_coding_type == 2 || ph.picture_coding_type == 3) {
            ph.full_pel_forward_vector = _in.read1Bit();
            ph.forward_f_code = _in.readNBit(3);
        }
        if (ph.picture_coding_type == 3) {
            ph.full_pel_backward_vector = _in.read1Bit();
            ph.backward_f_code = _in.readNBit(3);
        }
        while (_in.read1Bit() == 1) {
            _in.readNBit(8);
        }
        return ph;
    }

    public static void readExtension(ByteBuffer bb, PictureHeader ph, SequenceHeader sh) {
        ph._hasExtensions = true;
        BitReader _in = BitReader.createBitReader(bb);
        int extType = _in.readNBit(4);
        switch(extType) {
            case 3:
                ph.quantMatrixExtension = QuantMatrixExtension.read(_in);
                break;
            case 4:
                ph.copyrightExtension = CopyrightExtension.read(_in);
                break;
            case 5:
            case 6:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            default:
                throw new RuntimeException("Unsupported extension: " + extType);
            case 7:
                ph.pictureDisplayExtension = PictureDisplayExtension.read(_in, sh.sequenceExtension, ph.pictureCodingExtension);
                break;
            case 8:
                ph.pictureCodingExtension = PictureCodingExtension.read(_in);
                break;
            case 9:
                ph.pictureSpatialScalableExtension = PictureSpatialScalableExtension.read(_in);
                break;
            case 16:
                ph.pictureTemporalScalableExtension = PictureTemporalScalableExtension.read(_in);
        }
    }

    @Override
    public void write(ByteBuffer os) {
        BitWriter out = new BitWriter(os);
        out.writeNBit(this.temporal_reference, 10);
        out.writeNBit(this.picture_coding_type, 3);
        out.writeNBit(this.vbv_delay, 16);
        if (this.picture_coding_type == 2 || this.picture_coding_type == 3) {
            out.write1Bit(this.full_pel_forward_vector);
            out.write1Bit(this.forward_f_code);
        }
        if (this.picture_coding_type == 3) {
            out.write1Bit(this.full_pel_backward_vector);
            out.writeNBit(this.backward_f_code, 3);
        }
        out.write1Bit(0);
        out.flush();
        this.writeExtensions(os);
    }

    private void writeExtensions(ByteBuffer out) {
        if (this.quantMatrixExtension != null) {
            out.putInt(181);
            this.quantMatrixExtension.write(out);
        }
        if (this.copyrightExtension != null) {
            out.putInt(181);
            this.copyrightExtension.write(out);
        }
        if (this.pictureCodingExtension != null) {
            out.putInt(181);
            this.pictureCodingExtension.write(out);
        }
        if (this.pictureDisplayExtension != null) {
            out.putInt(181);
            this.pictureDisplayExtension.write(out);
        }
        if (this.pictureSpatialScalableExtension != null) {
            out.putInt(181);
            this.pictureSpatialScalableExtension.write(out);
        }
        if (this.pictureTemporalScalableExtension != null) {
            out.putInt(181);
            this.pictureTemporalScalableExtension.write(out);
        }
    }

    public boolean hasExtensions() {
        return this._hasExtensions;
    }
}