package com.github.alexthe666.citadel.repack.jcodec.containers.mps.index;

import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.MPEGDecoder;
import com.github.alexthe666.citadel.repack.jcodec.common.Codec;
import com.github.alexthe666.citadel.repack.jcodec.common.MuxerTrack;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.io.FileChannelWrapper;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.Brand;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.MP4Packet;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.muxer.MP4Muxer;
import java.io.File;
import java.io.IOException;

public class MTSRandomAccessDemuxerMain {

    public static void main1(String[] args) throws IOException {
        MTSIndexer indexer = new MTSIndexer();
        File source = new File(args[0]);
        File indexFile = new File(source.getParentFile(), source.getName() + ".idx");
        MTSIndex index;
        if (!indexFile.exists()) {
            indexer.index(source, null);
            index = indexer.serialize();
            NIOUtils.writeTo(index.serialize(), indexFile);
        } else {
            System.out.println("Reading index from: " + indexFile.getName());
            index = MTSIndex.parse(NIOUtils.fetchFromFile(indexFile));
        }
        MTSRandomAccessDemuxer demuxer = new MTSRandomAccessDemuxer(NIOUtils.readableChannel(source), index);
        int[] guids = demuxer.getGuids();
        MPSRandomAccessDemuxer.Stream video = getVideoStream(demuxer.getProgramDemuxer(guids[0]));
        FileChannelWrapper ch = NIOUtils.writableChannel(new File(args[1]));
        MP4Muxer mp4Muxer = MP4Muxer.createMP4Muxer(ch, Brand.MOV);
        video.gotoSyncFrame(175L);
        Packet pkt = video.nextFrame();
        VideoCodecMeta meta = new MPEGDecoder().getCodecMeta(pkt.getData());
        MuxerTrack videoTrack = mp4Muxer.addVideoTrack(Codec.MPEG2, meta);
        long firstPts = pkt.getPts();
        for (int i = 0; pkt != null && i < 150; i++) {
            videoTrack.addFrame(MP4Packet.createMP4Packet(pkt.getData(), pkt.getPts() - firstPts, pkt.getTimescale(), pkt.getDuration(), pkt.getFrameNo(), pkt.getFrameType(), pkt.getTapeTimecode(), 0, pkt.getPts() - firstPts, 0));
            pkt = video.nextFrame();
        }
        mp4Muxer.finish();
        NIOUtils.closeQuietly(ch);
    }

    private static MPSRandomAccessDemuxer.Stream getVideoStream(MPSRandomAccessDemuxer demuxer) {
        MPSRandomAccessDemuxer.Stream[] streams = demuxer.getStreams();
        for (MPSRandomAccessDemuxer.Stream stream : streams) {
            if (stream.getStreamId() >= 224 && stream.getStreamId() <= 239) {
                return stream;
            }
        }
        return null;
    }
}