package com.github.alexthe666.citadel.repack.jcodec.movtool;

import com.github.alexthe666.citadel.repack.jcodec.common.io.IOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.BoxFactory;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.BoxUtil;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.MP4Util;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Header;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.NodeBox;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class Undo {

    public static void main1(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Syntax: qt-undo [-l] <movie>");
            System.err.println("\t-l\t\tList all the previous versions of this movie.");
            System.exit(-1);
        }
        Undo undo = new Undo();
        if ("-l".equals(args[0])) {
            List<MP4Util.Atom> list = undo.list(args[1]);
            System.out.println(list.size() - 1 + " versions.");
        } else {
            undo.undo(args[0]);
        }
    }

    private void undo(String fineName) throws IOException {
        List<MP4Util.Atom> versions = this.list(fineName);
        if (versions.size() < 2) {
            System.err.println("Nowhere to rollback.");
        } else {
            RandomAccessFile raf = null;
            try {
                raf = new RandomAccessFile(new File(fineName), "rw");
                raf.seek(((MP4Util.Atom) versions.get(versions.size() - 2)).getOffset() + 4L);
                raf.write(new byte[] { 109, 111, 111, 118 });
                raf.seek(((MP4Util.Atom) versions.get(versions.size() - 1)).getOffset() + 4L);
                raf.write(new byte[] { 102, 114, 101, 101 });
            } finally {
                IOUtils.closeQuietly(raf);
            }
        }
    }

    private List<MP4Util.Atom> list(String fileName) throws IOException {
        ArrayList<MP4Util.Atom> result = new ArrayList();
        SeekableByteChannel is = null;
        try {
            is = NIOUtils.readableChannel(new File(fileName));
            int version = 0;
            for (MP4Util.Atom atom : MP4Util.getRootAtoms(is)) {
                if ("free".equals(atom.getHeader().getFourcc()) && this.isMoov(is, atom)) {
                    result.add(atom);
                }
                if ("moov".equals(atom.getHeader().getFourcc())) {
                    result.add(atom);
                    break;
                }
            }
        } finally {
            IOUtils.closeQuietly(is);
        }
        return result;
    }

    private boolean isMoov(SeekableByteChannel is, MP4Util.Atom atom) throws IOException {
        is.setPosition(atom.getOffset() + atom.getHeader().headerSize());
        try {
            Box mov = BoxUtil.parseBox(NIOUtils.fetchFromChannel(is, (int) atom.getHeader().getSize()), Header.createHeader("moov", atom.getHeader().getSize()), BoxFactory.getDefault());
            return mov instanceof MovieBox && BoxUtil.containsBox((NodeBox) mov, "mvhd");
        } catch (Throwable var4) {
            return false;
        }
    }
}