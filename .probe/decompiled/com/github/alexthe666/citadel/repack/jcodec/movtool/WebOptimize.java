package com.github.alexthe666.citadel.repack.jcodec.movtool;

import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.MP4Util;
import java.io.File;
import java.io.IOException;

public class WebOptimize {

    public static void main1(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Syntax: optimize <movie>");
            System.exit(-1);
        }
        File tgt = new File(args[0]);
        File src = hidFile(tgt);
        tgt.renameTo(src);
        try {
            MP4Util.Movie movie = MP4Util.createRefFullMovieFromFile(src);
            new Flatten().flatten(movie, tgt);
        } catch (Throwable var4) {
            var4.printStackTrace();
            tgt.renameTo(new File(tgt.getParentFile(), tgt.getName() + ".error"));
            src.renameTo(tgt);
        }
    }

    public static File hidFile(File tgt) {
        File src = new File(tgt.getParentFile(), "." + tgt.getName());
        if (src.exists()) {
            int i = 1;
            do {
                src = new File(tgt.getParentFile(), "." + tgt.getName() + "." + i++);
            } while (src.exists());
        }
        return src;
    }
}