package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12;

import com.github.alexthe666.citadel.repack.jcodec.common.io.IOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.model.RationalLarge;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MainUtils;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class TimestampUtil {

    private static final String STREAM_ALL = "all";

    private static final String STREAM_AUDIO = "audio";

    private static final String STRAM_VIDEO = "video";

    private static final MainUtils.Flag FLAG_STREAM = MainUtils.Flag.flag("stream", "s", "A stream to shift, i.e. 'video' or 'audio' or 'all' [default]");

    private static final MainUtils.Flag[] ALL_FLAGS = new MainUtils.Flag[] { FLAG_STREAM };

    private static final String COMMAND_SHIFT = "shift";

    private static final String COMMAND_SCALE = "scale";

    private static final String COMMAND_ROUND = "round";

    public static void main1(String[] args) throws IOException {
        MainUtils.Cmd cmd = MainUtils.parseArguments(args, ALL_FLAGS);
        if (cmd.args.length < 3) {
            System.out.println("A utility to tweak MPEG TS timestamps.");
            MainUtils.printHelp(ALL_FLAGS, Arrays.asList("command", "arg", "in name", "?out file"));
            System.out.println("Where command is:\n\tshift\tShift timestamps of selected stream by arg.\n\tscale\tScale timestams of selected stream by arg [num:den].\n\tround\tRound timestamps of selected stream to multiples of arg.");
        } else {
            File src = new File(cmd.getArg(2));
            if (cmd.argsLength() > 3) {
                File dst = new File(cmd.getArg(3));
                IOUtils.copyFile(src, dst);
                src = dst;
            }
            String command = cmd.getArg(0);
            String stream = cmd.getStringFlagD(FLAG_STREAM, "all");
            if ("shift".equalsIgnoreCase(command)) {
                final long shift = Long.parseLong(cmd.getArg(1));
                (new TimestampUtil.BaseCommand(stream) {

                    @Override
                    protected long withTimestamp(long pts, boolean isPts) {
                        return Math.max(pts + shift, 0L);
                    }
                }).fix(src);
            } else if ("scale".equalsIgnoreCase(command)) {
                final RationalLarge scale = RationalLarge.parse(cmd.getArg(1));
                (new TimestampUtil.BaseCommand(stream) {

                    @Override
                    protected long withTimestamp(long pts, boolean isPts) {
                        return scale.multiplyS(pts);
                    }
                }).fix(src);
            } else if ("round".equalsIgnoreCase(command)) {
                final int precision = Integer.parseInt(cmd.getArg(1));
                (new TimestampUtil.BaseCommand(stream) {

                    @Override
                    protected long withTimestamp(long pts, boolean isPts) {
                        return Math.round((double) pts / (double) precision) * (long) precision;
                    }
                }).fix(src);
            }
        }
    }

    private abstract static class BaseCommand extends FixTimestamp {

        private String streamSelector;

        public BaseCommand(String stream) {
            this.streamSelector = stream;
        }

        @Override
        protected long doWithTimestamp(int streamId, long pts, boolean isPts) {
            return !"all".equals(this.streamSelector) && (!"video".equals(this.streamSelector) || !this.isVideo(streamId)) && (!"audio".equals(this.streamSelector) || !this.isAudio(streamId)) ? pts : this.withTimestamp(pts, isPts);
        }

        protected abstract long withTimestamp(long var1, boolean var3);
    }
}