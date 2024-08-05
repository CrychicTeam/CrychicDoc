package com.github.alexthe666.citadel.repack.jcodec.containers.mkv;

import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlBase;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlBin;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlMaster;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlVoid;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.util.EbmlUtil;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MKVParser {

    private SeekableByteChannel channel;

    private LinkedList<EbmlMaster> trace;

    public MKVParser(SeekableByteChannel channel) {
        this.channel = channel;
        this.trace = new LinkedList();
    }

    public List<EbmlMaster> parse() throws IOException {
        List<EbmlMaster> tree = new ArrayList();
        EbmlBase e = null;
        while ((e = this.nextElement()) != null) {
            if (!this.isKnownType(e.id)) {
                System.err.println("Unspecified header: " + EbmlUtil.toHexString(e.id) + " at " + e.offset);
            }
            while (!this.possibleChild((EbmlMaster) this.trace.peekFirst(), e)) {
                this.closeElem((EbmlMaster) this.trace.removeFirst(), tree);
            }
            this.openElem(e);
            if (e instanceof EbmlMaster) {
                this.trace.push((EbmlMaster) e);
            } else if (e instanceof EbmlBin) {
                EbmlBin bin = (EbmlBin) e;
                EbmlMaster traceTop = (EbmlMaster) this.trace.peekFirst();
                if (traceTop.dataOffset + (long) traceTop.dataLen < e.dataOffset + (long) e.dataLen) {
                    this.channel.setPosition(traceTop.dataOffset + (long) traceTop.dataLen);
                } else {
                    try {
                        bin.readChannel(this.channel);
                    } catch (OutOfMemoryError var6) {
                        throw new RuntimeException(e.type + " 0x" + EbmlUtil.toHexString(bin.id) + " size: " + Long.toHexString((long) bin.dataLen) + " offset: 0x" + Long.toHexString(e.offset), var6);
                    }
                }
                ((EbmlMaster) this.trace.peekFirst()).add(e);
            } else {
                if (!(e instanceof EbmlVoid)) {
                    throw new RuntimeException("Currently there are no elements that are neither Master nor Binary, should never actually get here");
                }
                ((EbmlVoid) e).skip(this.channel);
            }
        }
        while (this.trace.peekFirst() != null) {
            this.closeElem((EbmlMaster) this.trace.removeFirst(), tree);
        }
        return tree;
    }

    private boolean possibleChild(EbmlMaster parent, EbmlBase child) {
        return parent != null && MKVType.Cluster.equals(parent.type) && child != null && !MKVType.Cluster.equals(child.type) && !MKVType.Info.equals(child.type) && !MKVType.SeekHead.equals(child.type) && !MKVType.Tracks.equals(child.type) && !MKVType.Cues.equals(child.type) && !MKVType.Attachments.equals(child.type) && !MKVType.Tags.equals(child.type) && !MKVType.Chapters.equals(child.type) ? true : MKVType.possibleChild(parent, child);
    }

    private void openElem(EbmlBase e) {
    }

    private void closeElem(EbmlMaster e, List<EbmlMaster> tree) {
        if (this.trace.peekFirst() == null) {
            tree.add(e);
        } else {
            ((EbmlMaster) this.trace.peekFirst()).add(e);
        }
    }

    private EbmlBase nextElement() throws IOException {
        long offset = this.channel.position();
        if (offset >= this.channel.size()) {
            return null;
        } else {
            byte[] typeId;
            for (typeId = readEbmlId(this.channel); typeId == null && !this.isKnownType(typeId) && offset < this.channel.size(); typeId = readEbmlId(this.channel)) {
                this.channel.setPosition(++offset);
            }
            long dataLen = readEbmlInt(this.channel);
            EbmlBase elem = MKVType.createById(typeId, offset);
            elem.offset = offset;
            elem.typeSizeLength = (int) (this.channel.position() - offset);
            elem.dataOffset = this.channel.position();
            elem.dataLen = (int) dataLen;
            return elem;
        }
    }

    public boolean isKnownType(byte[] b) {
        return !this.trace.isEmpty() && MKVType.Cluster.equals(((EbmlMaster) this.trace.peekFirst()).type) ? true : MKVType.isSpecifiedHeader(b);
    }

    public static byte[] readEbmlId(SeekableByteChannel source) throws IOException {
        if (source.position() == source.size()) {
            return null;
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.limit(1);
            source.read(buffer);
            buffer.flip();
            byte firstByte = buffer.get();
            int numBytes = EbmlUtil.computeLength(firstByte);
            if (numBytes == 0) {
                return null;
            } else {
                if (numBytes > 1) {
                    buffer.limit(numBytes);
                    source.read(buffer);
                }
                buffer.flip();
                ByteBuffer val = ByteBuffer.allocate(buffer.remaining());
                val.put(buffer);
                return val.array();
            }
        }
    }

    public static long readEbmlInt(SeekableByteChannel source) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.limit(1);
        source.read(buffer);
        buffer.flip();
        byte firstByte = buffer.get();
        int length = EbmlUtil.computeLength(firstByte);
        if (length == 0) {
            throw new RuntimeException("Invalid ebml integer size.");
        } else {
            buffer.limit(length);
            source.read(buffer);
            buffer.position(1);
            long value = (long) (firstByte & 255 >>> length);
            length--;
            while (length > 0) {
                value = value << 8 | (long) (buffer.get() & 255);
                length--;
            }
            return value;
        }
    }
}