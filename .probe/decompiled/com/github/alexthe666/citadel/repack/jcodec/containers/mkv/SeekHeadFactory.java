package com.github.alexthe666.citadel.repack.jcodec.containers.mkv;

import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlBase;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlBin;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlMaster;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlUint;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.util.EbmlUtil;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class SeekHeadFactory {

    List<SeekHeadFactory.SeekMock> a;

    long currentDataOffset = 0L;

    public SeekHeadFactory() {
        this.a = new ArrayList();
    }

    public void add(EbmlBase e) {
        SeekHeadFactory.SeekMock z = SeekHeadFactory.SeekMock.make(e);
        z.dataOffset = this.currentDataOffset;
        z.seekPointerSize = EbmlUint.calculatePayloadSize(z.dataOffset);
        this.currentDataOffset = this.currentDataOffset + (long) z.size;
        this.a.add(z);
    }

    public EbmlMaster indexSeekHead() {
        int seekHeadSize = this.computeSeekHeadSize();
        EbmlMaster seekHead = MKVType.createByType(MKVType.SeekHead);
        for (SeekHeadFactory.SeekMock z : this.a) {
            EbmlMaster seek = MKVType.createByType(MKVType.Seek);
            EbmlBin seekId = MKVType.createByType(MKVType.SeekID);
            seekId.setBuf(ByteBuffer.wrap(z.id));
            seek.add(seekId);
            EbmlUint seekPosition = MKVType.createByType(MKVType.SeekPosition);
            seekPosition.setUint(z.dataOffset + (long) seekHeadSize);
            if (seekPosition.data.limit() != z.seekPointerSize) {
                System.err.println("estimated size of seekPosition differs from the one actually used. ElementId: " + EbmlUtil.toHexString(z.id) + " " + seekPosition.getData().limit() + " vs " + z.seekPointerSize);
            }
            seek.add(seekPosition);
            seekHead.add(seek);
        }
        ByteBuffer mux = seekHead.getData();
        if (mux.limit() != seekHeadSize) {
            System.err.println("estimated size of seekHead differs from the one actually used. " + mux.limit() + " vs " + seekHeadSize);
        }
        return seekHead;
    }

    public int computeSeekHeadSize() {
        int seekHeadSize = this.estimateSize();
        boolean reindex = false;
        do {
            reindex = false;
            for (SeekHeadFactory.SeekMock z : this.a) {
                int minSize = EbmlUint.calculatePayloadSize(z.dataOffset + (long) seekHeadSize);
                if (minSize > z.seekPointerSize) {
                    System.out.println("Size " + seekHeadSize + " seems too small for element " + EbmlUtil.toHexString(z.id) + " increasing size by one.");
                    z.seekPointerSize++;
                    seekHeadSize++;
                    reindex = true;
                    break;
                }
                if (minSize < z.seekPointerSize) {
                    throw new RuntimeException("Downsizing the index is not well thought through.");
                }
            }
        } while (reindex);
        return seekHeadSize;
    }

    int estimateSize() {
        int s = MKVType.SeekHead.id.length + 1;
        s += estimeteSeekSize(((SeekHeadFactory.SeekMock) this.a.get(0)).id.length, 1);
        for (int i = 1; i < this.a.size(); i++) {
            s += estimeteSeekSize(((SeekHeadFactory.SeekMock) this.a.get(i)).id.length, ((SeekHeadFactory.SeekMock) this.a.get(i)).seekPointerSize);
        }
        return s;
    }

    public static int estimeteSeekSize(int idLength, int offsetSizeInBytes) {
        int seekIdSize = MKVType.SeekID.id.length + EbmlUtil.ebmlLength((long) idLength) + idLength;
        int seekPositionSize = MKVType.SeekPosition.id.length + EbmlUtil.ebmlLength((long) offsetSizeInBytes) + offsetSizeInBytes;
        return MKVType.Seek.id.length + EbmlUtil.ebmlLength((long) (seekIdSize + seekPositionSize)) + seekIdSize + seekPositionSize;
    }

    public static class SeekMock {

        public long dataOffset;

        byte[] id;

        int size;

        int seekPointerSize;

        public static SeekHeadFactory.SeekMock make(EbmlBase e) {
            SeekHeadFactory.SeekMock z = new SeekHeadFactory.SeekMock();
            z.id = e.id;
            z.size = (int) e.size();
            return z;
        }
    }
}