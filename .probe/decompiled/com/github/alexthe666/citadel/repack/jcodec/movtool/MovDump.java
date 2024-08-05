package com.github.alexthe666.citadel.repack.jcodec.movtool;

import com.github.alexthe666.citadel.repack.jcodec.common.io.IOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.MP4Util;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.NodeBox;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MovDump {

    public static void main1(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Syntax: movdump [options] <filename>");
            System.out.println("Options: \n\t-f <filename> save header to a file\n\t-a <atom name> dump only a specific atom\n");
        } else {
            int idx = 0;
            File headerFile = null;
            String atom = null;
            while (idx < args.length) {
                if ("-f".equals(args[idx])) {
                    int var10003 = ++idx;
                    idx++;
                    headerFile = new File(args[var10003]);
                } else {
                    if (!"-a".equals(args[idx])) {
                        break;
                    }
                    int var10001 = ++idx;
                    idx++;
                    atom = args[var10001];
                }
            }
            File source = new File(args[idx]);
            if (headerFile != null) {
                dumpHeader(headerFile, source);
            }
            if (atom == null) {
                System.out.println(print(source));
            } else {
                String dump = printAtom(source, atom);
                if (dump != null) {
                    System.out.println(dump);
                }
            }
        }
    }

    private static void dumpHeader(File headerFile, File source) throws IOException, FileNotFoundException {
        SeekableByteChannel raf = null;
        SeekableByteChannel daos = null;
        try {
            raf = NIOUtils.readableChannel(source);
            daos = NIOUtils.writableChannel(headerFile);
            for (MP4Util.Atom atom : MP4Util.getRootAtoms(raf)) {
                String fourcc = atom.getHeader().getFourcc();
                if ("moov".equals(fourcc) || "ftyp".equals(fourcc)) {
                    atom.copy(raf, daos);
                }
            }
        } finally {
            IOUtils.closeQuietly(raf);
            IOUtils.closeQuietly(daos);
        }
    }

    public static String print(File file) throws IOException {
        return MP4Util.parseMovie(file).toString();
    }

    private static Box findDeep(NodeBox root, String atom) {
        for (Box b : root.getBoxes()) {
            if (atom.equalsIgnoreCase(b.getFourcc())) {
                return b;
            }
            if (b instanceof NodeBox) {
                Box res = findDeep((NodeBox) b, atom);
                if (res != null) {
                    return res;
                }
            }
        }
        return null;
    }

    public static String printAtom(File file, String atom) throws IOException {
        MovieBox mov = MP4Util.parseMovie(file);
        Box found = findDeep(mov, atom);
        if (found == null) {
            System.out.println("Atom " + atom + " not found.");
            return null;
        } else {
            return found.toString();
        }
    }
}