package com.github.alexthe666.citadel.repack.jcodec.containers.mxf;

import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.AES3PCMDescriptor;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.CDCIEssenceDescriptor;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.ContentStorage;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.EssenceContainerData;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.FileDescriptor;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.GenericDataEssenceDescriptor;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.GenericDescriptor;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.GenericPictureEssenceDescriptor;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.GenericSoundEssenceDescriptor;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.Identification;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.IndexSegment;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.J2KPictureDescriptor;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.MPEG2VideoDescriptor;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.MXFMetadata;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.MXFPartitionPack;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.MaterialPackage;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.Preface;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.RGBAEssenceDescriptor;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.Sequence;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.SourceClip;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.SourcePackage;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.TimecodeComponent;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.TimelineTrack;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.UL;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.WaveAudioDescriptor;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class MXFConst {

    public static final UL HEADER_PARTITION_KLV = UL.newUL("06.0e.2b.34.02.05.01.01.0d.01.02.01.01.02");

    public static final UL INDEX_KLV = UL.newUL("06.0E.2B.34.02.53.01.01.0d.01.02.01.01.10.01.00");

    public static final UL GENERIC_DESCRIPTOR_KLV = UL.newUL("06.0E.2B.34.02.53.01.01.0d.01.01.01.01.01");

    public static Map<UL, Class<? extends MXFMetadata>> klMetadata = new HashMap();

    static {
        klMetadata.put(UL.newUL("06.0E.2B.34.02.53.01.01.0d.01.01.01.01.01.18.00"), ContentStorage.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.53.01.01.0d.01.01.01.01.01.37.00"), SourcePackage.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.53.01.01.0d.01.01.01.01.01.0F.00"), Sequence.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.53.01.01.0D.01.01.01.01.01.2F.00"), Preface.class);
        klMetadata.put(UL.newUL("06.0e.2b.34.02.53.01.01.0d.01.01.01.01.01.30.00"), Identification.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.53.01.01.0d.01.01.01.01.01.11.00"), SourceClip.class);
        klMetadata.put(UL.newUL("06.0e.2b.34.02.53.01.01.0d.01.01.01.01.01.23.00"), EssenceContainerData.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.53.01.01.0d.01.01.01.01.01.3A.00"), TimelineTrack.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.53.01.01.0d.01.01.01.01.01.3B.00"), TimelineTrack.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.53.01.01.0d.01.01.01.01.01.36.00"), MaterialPackage.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.53.01.01.0d.01.02.01.01.10.01.00"), IndexSegment.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.53.01.01.0d.01.01.01.01.01.44.00"), GenericDescriptor.class);
        klMetadata.put(UL.newUL("06.0e.2b.34.02.53.01.01.0d.01.01.01.01.01.5b.00"), GenericDataEssenceDescriptor.class);
        klMetadata.put(UL.newUL("06.0e.2b.34.02.53.01.01.0d.01.01.01.01.01.5b.00"), GenericDataEssenceDescriptor.class);
        klMetadata.put(UL.newUL("06.0e.2b.34.02.53.01.01.0d.01.01.01.01.01.5c.00"), GenericDataEssenceDescriptor.class);
        klMetadata.put(UL.newUL("06.0e.2b.34.02.53.01.01.0d.01.01.01.01.01.43.00"), GenericDataEssenceDescriptor.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.53.01.01.0d.01.01.01.01.01.42.00"), GenericSoundEssenceDescriptor.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.53.01.01.0d.01.01.01.01.01.28.00"), CDCIEssenceDescriptor.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.53.01.01.0d.01.01.01.01.01.29.00"), RGBAEssenceDescriptor.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.53.01.01.0d.01.01.01.01.01.51.00"), MPEG2VideoDescriptor.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.53.01.01.0d.01.01.01.01.01.48.00"), WaveAudioDescriptor.class);
        klMetadata.put(UL.newUL("06.0e.2b.34.02.53.01.01.0d.01.01.01.01.01.25.00"), FileDescriptor.class);
        klMetadata.put(UL.newUL("06.0e.2b.34.02.53.01.01.0d.01.01.01.01.01.27.00"), GenericPictureEssenceDescriptor.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.53.01.01.0d.01.01.01.01.01.47.00"), AES3PCMDescriptor.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.05.01.01.0d.01.02.01.01.05.01.00"), MXFPartitionPack.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.05.01.01.0d.01.02.01.01.02.01.00"), MXFPartitionPack.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.05.01.01.0d.01.02.01.01.02.02.00"), MXFPartitionPack.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.05.01.01.0d.01.02.01.01.02.03.00"), MXFPartitionPack.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.05.01.01.0d.01.02.01.01.02.04.00"), MXFPartitionPack.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.05.01.01.0d.01.02.01.01.03.01.00"), MXFPartitionPack.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.05.01.01.0d.01.02.01.01.03.02.00"), MXFPartitionPack.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.05.01.01.0d.01.02.01.01.03.03.00"), MXFPartitionPack.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.05.01.01.0d.01.02.01.01.03.04.00"), MXFPartitionPack.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.05.01.01.0d.01.02.01.01.04.02.00"), MXFPartitionPack.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.05.01.01.0d.01.02.01.01.04.04.00"), MXFPartitionPack.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.02.53.01.01.0D.01.01.01.01.01.14.00"), TimecodeComponent.class);
        klMetadata.put(UL.newUL("06.0E.2B.34.01.01.01.02.03.01.02.10.01.00.00.00"), MXFConst.KLVFill.class);
        klMetadata.put(UL.newUL("06.0e.2b.34.02.53.01.01.0d.01.01.01.01.01.5a.00"), J2KPictureDescriptor.class);
    }

    public static class KLVFill extends MXFMetadata {

        public KLVFill(UL ul) {
            super(ul);
        }

        @Override
        public void readBuf(ByteBuffer bb) {
        }
    }
}