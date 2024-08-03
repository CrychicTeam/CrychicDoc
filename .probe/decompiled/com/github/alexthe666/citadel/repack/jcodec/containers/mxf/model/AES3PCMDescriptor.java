package com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model;

import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class AES3PCMDescriptor extends WaveAudioDescriptor {

    private byte emphasis;

    private short blockStartOffset;

    private byte auxBitsMode;

    private ByteBuffer channelStatusMode;

    private ByteBuffer fixedChannelStatusData;

    private ByteBuffer userDataMode;

    private ByteBuffer fixedUserData;

    public AES3PCMDescriptor(UL ul) {
        super(ul);
    }

    @Override
    protected void read(Map<Integer, ByteBuffer> tags) {
        super.read(tags);
        Iterator<Entry<Integer, ByteBuffer>> it = tags.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Integer, ByteBuffer> entry = (Entry<Integer, ByteBuffer>) it.next();
            ByteBuffer _bb = (ByteBuffer) entry.getValue();
            switch(entry.getKey()) {
                case 15624:
                    this.auxBitsMode = _bb.get();
                    break;
                case 15625:
                case 15626:
                case 15627:
                case 15628:
                case 15630:
                default:
                    Logger.warn(String.format("Unknown tag [ " + this.ul + "]: %04x", entry.getKey()));
                    continue;
                case 15629:
                    this.emphasis = _bb.get();
                    break;
                case 15631:
                    this.blockStartOffset = _bb.getShort();
                    break;
                case 15632:
                    this.channelStatusMode = _bb;
                    break;
                case 15633:
                    this.fixedChannelStatusData = _bb;
                    break;
                case 15634:
                    this.userDataMode = _bb;
                    break;
                case 15635:
                    this.fixedUserData = _bb;
            }
            it.remove();
        }
    }

    public byte getEmphasis() {
        return this.emphasis;
    }

    public short getBlockStartOffset() {
        return this.blockStartOffset;
    }

    public byte getAuxBitsMode() {
        return this.auxBitsMode;
    }

    public ByteBuffer getChannelStatusMode() {
        return this.channelStatusMode;
    }

    public ByteBuffer getFixedChannelStatusData() {
        return this.fixedChannelStatusData;
    }

    public ByteBuffer getUserDataMode() {
        return this.userDataMode;
    }

    public ByteBuffer getFixedUserData() {
        return this.fixedUserData;
    }
}