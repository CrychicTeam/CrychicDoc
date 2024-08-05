package com.github.alexthe666.citadel.repack.jcodec.containers.mp4;

import com.github.alexthe666.citadel.repack.jcodec.common.model.RationalLarge;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Edit;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.NodeBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TimeToSampleBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TimecodeSampleEntry;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TrakBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.demuxer.TimecodeMP4DemuxerTrack;
import java.io.IOException;
import java.util.List;

public class QTTimeUtil {

    public static long getEditedDuration(TrakBox track) {
        List<Edit> edits = track.getEdits();
        if (edits == null) {
            return track.getDuration();
        } else {
            long duration = 0L;
            for (Edit edit : edits) {
                duration += edit.getDuration();
            }
            return duration;
        }
    }

    public static long frameToTimevalue(TrakBox trak, int frameNumber) {
        TimeToSampleBox stts = NodeBox.findFirstPath(trak, TimeToSampleBox.class, Box.path("mdia.minf.stbl.stts"));
        TimeToSampleBox.TimeToSampleEntry[] timeToSamples = stts.getEntries();
        long pts = 0L;
        int sttsInd = 0;
        int sttsSubInd;
        for (sttsSubInd = frameNumber; sttsSubInd >= timeToSamples[sttsInd].getSampleCount(); sttsInd++) {
            sttsSubInd -= timeToSamples[sttsInd].getSampleCount();
            pts += (long) (timeToSamples[sttsInd].getSampleCount() * timeToSamples[sttsInd].getSampleDuration());
        }
        return pts + (long) (timeToSamples[sttsInd].getSampleDuration() * sttsSubInd);
    }

    public static int timevalueToFrame(TrakBox trak, long tv) {
        TimeToSampleBox.TimeToSampleEntry[] tts = NodeBox.<TimeToSampleBox>findFirstPath(trak, TimeToSampleBox.class, Box.path("mdia.minf.stbl.stts")).getEntries();
        int frame = 0;
        for (int i = 0; tv > 0L && i < tts.length; i++) {
            long rem = tv / (long) tts[i].getSampleDuration();
            tv -= (long) (tts[i].getSampleCount() * tts[i].getSampleDuration());
            frame = (int) ((long) frame + (tv > 0L ? (long) tts[i].getSampleCount() : rem));
        }
        return frame;
    }

    public static long mediaToEdited(TrakBox trak, long mediaTv, int movieTimescale) {
        if (trak.getEdits() == null) {
            return mediaTv;
        } else {
            long accum = 0L;
            for (Edit edit : trak.getEdits()) {
                if (mediaTv < edit.getMediaTime()) {
                    return accum;
                }
                long duration = trak.rescale(edit.getDuration(), (long) movieTimescale);
                if (edit.getMediaTime() != -1L && mediaTv >= edit.getMediaTime() && mediaTv < edit.getMediaTime() + duration) {
                    accum += mediaTv - edit.getMediaTime();
                    break;
                }
                accum += duration;
            }
            return accum;
        }
    }

    public static long editedToMedia(TrakBox trak, long editedTv, int movieTimescale) {
        if (trak.getEdits() == null) {
            return editedTv;
        } else {
            long accum = 0L;
            for (Edit edit : trak.getEdits()) {
                long duration = trak.rescale(edit.getDuration(), (long) movieTimescale);
                if (accum + duration > editedTv) {
                    return edit.getMediaTime() + editedTv - accum;
                }
                accum += duration;
            }
            return accum;
        }
    }

    public static int qtPlayerFrameNo(MovieBox movie, int mediaFrameNo) {
        TrakBox videoTrack = movie.getVideoTrack();
        long editedTv = mediaToEdited(videoTrack, frameToTimevalue(videoTrack, mediaFrameNo), movie.getTimescale());
        return tv2QTFrameNo(movie, editedTv);
    }

    public static int tv2QTFrameNo(MovieBox movie, long tv) {
        TrakBox videoTrack = movie.getVideoTrack();
        TrakBox timecodeTrack = movie.getTimecodeTrack();
        return timecodeTrack != null && BoxUtil.containsBox2(videoTrack, "tref", "tmcd") ? timevalueToTimecodeFrame(timecodeTrack, new RationalLarge(tv, (long) videoTrack.getTimescale()), movie.getTimescale()) : timevalueToFrame(videoTrack, tv);
    }

    public static String qtPlayerTime(MovieBox movie, int mediaFrameNo) {
        TrakBox videoTrack = movie.getVideoTrack();
        long editedTv = mediaToEdited(videoTrack, frameToTimevalue(videoTrack, mediaFrameNo), movie.getTimescale());
        int sec = (int) (editedTv / (long) videoTrack.getTimescale());
        return String.format("%02d", sec / 3600) + "_" + String.format("%02d", sec % 3600 / 60) + "_" + String.format("%02d", sec % 60);
    }

    public static String qtPlayerTimecodeFromMovie(MovieBox movie, TimecodeMP4DemuxerTrack timecodeTrack, int mediaFrameNo) throws IOException {
        TrakBox videoTrack = movie.getVideoTrack();
        long editedTv = mediaToEdited(videoTrack, frameToTimevalue(videoTrack, mediaFrameNo), movie.getTimescale());
        TrakBox tt = timecodeTrack.getBox();
        int ttTimescale = tt.getTimescale();
        long ttTv = editedToMedia(tt, editedTv * (long) ttTimescale / (long) videoTrack.getTimescale(), movie.getTimescale());
        return formatTimecode(timecodeTrack.getBox(), timecodeTrack.getStartTimecode() + timevalueToTimecodeFrame(timecodeTrack.getBox(), new RationalLarge(ttTv, (long) ttTimescale), movie.getTimescale()));
    }

    public static String qtPlayerTimecode(TimecodeMP4DemuxerTrack timecodeTrack, RationalLarge tv, int movieTimescale) throws IOException {
        TrakBox tt = timecodeTrack.getBox();
        int ttTimescale = tt.getTimescale();
        long ttTv = editedToMedia(tt, tv.multiplyS((long) ttTimescale), movieTimescale);
        return formatTimecode(timecodeTrack.getBox(), timecodeTrack.getStartTimecode() + timevalueToTimecodeFrame(timecodeTrack.getBox(), new RationalLarge(ttTv, (long) ttTimescale), movieTimescale));
    }

    public static int timevalueToTimecodeFrame(TrakBox timecodeTrack, RationalLarge tv, int movieTimescale) {
        TimecodeSampleEntry se = (TimecodeSampleEntry) timecodeTrack.getSampleEntries()[0];
        return (int) (2L * tv.multiplyS((long) se.getTimescale()) / (long) se.getFrameDuration() + 1L) / 2;
    }

    public static String formatTimecode(TrakBox timecodeTrack, int counter) {
        TimecodeSampleEntry tmcd = NodeBox.findFirstPath(timecodeTrack, TimecodeSampleEntry.class, Box.path("mdia.minf.stbl.stsd.tmcd"));
        byte nf = tmcd.getNumFrames();
        String tc = String.format("%02d", counter % nf);
        counter /= nf;
        tc = String.format("%02d", counter % 60) + ":" + tc;
        counter /= 60;
        tc = String.format("%02d", counter % 60) + ":" + tc;
        counter /= 60;
        return String.format("%02d", counter) + ":" + tc;
    }
}