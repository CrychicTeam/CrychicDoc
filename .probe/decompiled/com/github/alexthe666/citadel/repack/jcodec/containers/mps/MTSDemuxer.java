package com.github.alexthe666.citadel.repack.jcodec.containers.mps;

import com.github.alexthe666.citadel.repack.jcodec.common.IntObjectMap;
import com.github.alexthe666.citadel.repack.jcodec.common.Preconditions;
import com.github.alexthe666.citadel.repack.jcodec.common.UsedViaReflection;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MTSDemuxer {

    private SeekableByteChannel channel;

    private Map<Integer, MTSDemuxer.ProgramChannel> programs;

    public Set<Integer> getPrograms() {
        return this.programs.keySet();
    }

    public Set<Integer> findPrograms(SeekableByteChannel src) throws IOException {
        long rem = src.position();
        Set<Integer> guids = new HashSet();
        for (int i = 0; guids.size() == 0 || i < guids.size() * 500; i++) {
            MTSDemuxer.MTSPacket pkt = readPacket(src);
            if (pkt == null) {
                break;
            }
            if (pkt.payload != null) {
                ByteBuffer payload = pkt.payload;
                if (!guids.contains(pkt.pid) && (payload.duplicate().getInt() & -256) == 256) {
                    guids.add(pkt.pid);
                }
            }
        }
        src.setPosition(rem);
        return guids;
    }

    public MTSDemuxer(SeekableByteChannel src) throws IOException {
        this.channel = src;
        this.programs = new HashMap();
        for (int pid : this.findPrograms(src)) {
            this.programs.put(pid, new MTSDemuxer.ProgramChannel(this));
        }
        src.setPosition(0L);
    }

    public ReadableByteChannel getProgram(int pid) {
        return (ReadableByteChannel) this.programs.get(pid);
    }

    private boolean readAndDispatchNextTSPacket() throws IOException {
        MTSDemuxer.MTSPacket pkt = readPacket(this.channel);
        if (pkt == null) {
            return false;
        } else {
            MTSDemuxer.ProgramChannel program = (MTSDemuxer.ProgramChannel) this.programs.get(pkt.pid);
            if (program != null) {
                program.storePacket(pkt);
            }
            return true;
        }
    }

    public static MTSDemuxer.MTSPacket readPacket(ReadableByteChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(188);
        if (NIOUtils.readFromChannel(channel, buffer) != 188) {
            return null;
        } else {
            buffer.flip();
            return parsePacket(buffer);
        }
    }

    public static MTSDemuxer.MTSPacket parsePacket(ByteBuffer buffer) {
        int marker = buffer.get() & 255;
        Preconditions.checkState(71 == marker);
        int guidFlags = buffer.getShort();
        int guid = guidFlags & 8191;
        int payloadStart = guidFlags >> 14 & 1;
        int b0 = buffer.get() & 255;
        int counter = b0 & 15;
        if ((b0 & 32) != 0) {
            int taken = 0;
            taken = (buffer.get() & 255) + 1;
            NIOUtils.skip(buffer, taken - 1);
        }
        return new MTSDemuxer.MTSPacket(guid, payloadStart == 1, (b0 & 16) != 0 ? buffer : null);
    }

    @UsedViaReflection
    public static int probe(ByteBuffer b_) {
        ByteBuffer b = b_.duplicate();
        IntObjectMap<List<ByteBuffer>> streams = new IntObjectMap<>();
        while (true) {
            try {
                ByteBuffer sub = NIOUtils.read(b, 188);
                if (sub.remaining() < 188) {
                    break;
                }
                MTSDemuxer.MTSPacket tsPkt = parsePacket(sub);
                if (tsPkt == null) {
                    break;
                }
                List<ByteBuffer> data = streams.get(tsPkt.pid);
                if (data == null) {
                    data = new ArrayList();
                    streams.put(tsPkt.pid, data);
                }
                if (tsPkt.payload != null) {
                    data.add(tsPkt.payload);
                }
            } catch (Throwable var11) {
                break;
            }
        }
        int maxScore = 0;
        int[] keys = streams.keys();
        for (int i : keys) {
            List<ByteBuffer> packets = streams.get(i);
            int score = MPSDemuxer.probe(NIOUtils.combineBuffers(packets));
            if (score > maxScore) {
                maxScore = score + (packets.size() > 20 ? 50 : 0);
            }
        }
        return maxScore;
    }

    public static class MTSPacket {

        public ByteBuffer payload;

        public boolean payloadStart;

        public int pid;

        public MTSPacket(int guid, boolean payloadStart, ByteBuffer payload) {
            this.pid = guid;
            this.payloadStart = payloadStart;
            this.payload = payload;
        }
    }

    private static class ProgramChannel implements ReadableByteChannel {

        private final MTSDemuxer demuxer;

        private List<ByteBuffer> data;

        private boolean closed;

        public ProgramChannel(MTSDemuxer demuxer) {
            this.demuxer = demuxer;
            this.data = new ArrayList();
        }

        public boolean isOpen() {
            return !this.closed && this.demuxer.channel.isOpen();
        }

        public void close() throws IOException {
            this.closed = true;
            this.data.clear();
        }

        public int read(ByteBuffer dst) throws IOException {
            int bytesRead = 0;
            while (dst.hasRemaining()) {
                while (this.data.size() == 0) {
                    if (!this.demuxer.readAndDispatchNextTSPacket()) {
                        return bytesRead > 0 ? bytesRead : -1;
                    }
                }
                ByteBuffer first = (ByteBuffer) this.data.get(0);
                int toRead = Math.min(dst.remaining(), first.remaining());
                dst.put(NIOUtils.read(first, toRead));
                if (!first.hasRemaining()) {
                    this.data.remove(0);
                }
                bytesRead += toRead;
            }
            return bytesRead;
        }

        public void storePacket(MTSDemuxer.MTSPacket pkt) {
            if (!this.closed) {
                this.data.add(pkt.payload);
            }
        }
    }
}