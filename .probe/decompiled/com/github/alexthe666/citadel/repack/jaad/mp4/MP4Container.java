package com.github.alexthe666.citadel.repack.jaad.mp4;

import com.github.alexthe666.citadel.repack.jaad.mp4.api.Brand;
import com.github.alexthe666.citadel.repack.jaad.mp4.api.Movie;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.BoxFactory;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.FileTypeBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.ProgressiveDownloadInformationBox;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MP4Container {

    private final MP4InputStream in;

    private final List<Box> boxes;

    private Brand major;

    private Brand minor;

    private Brand[] compatible;

    private FileTypeBox ftyp;

    private ProgressiveDownloadInformationBox pdin;

    private Box moov;

    private Movie movie;

    public MP4Container(InputStream in) throws IOException {
        this.in = new MP4InputStream(in);
        this.boxes = new ArrayList();
        this.readContent();
    }

    public MP4Container(RandomAccessFile in) throws IOException {
        this.in = new MP4InputStream(in);
        this.boxes = new ArrayList();
        this.readContent();
    }

    private void readContent() throws IOException {
        Box box = null;
        boolean moovFound = false;
        while (this.in.hasLeft()) {
            box = BoxFactory.parseBox(null, this.in);
            if (this.boxes.isEmpty() && box.getType() != 1718909296L) {
                throw new MP4Exception("no MP4 signature found");
            }
            this.boxes.add(box);
            long type = box.getType();
            if (type == 1718909296L) {
                if (this.ftyp == null) {
                    this.ftyp = (FileTypeBox) box;
                }
            } else if (type == 1836019574L) {
                if (this.movie == null) {
                    this.moov = box;
                }
                moovFound = true;
            } else if (type == 1885628782L) {
                if (this.pdin == null) {
                    this.pdin = (ProgressiveDownloadInformationBox) box;
                }
            } else if (type == 1835295092L) {
                if (moovFound) {
                    break;
                }
                if (!this.in.hasRandomAccess()) {
                    throw new MP4Exception("movie box at end of file, need random access");
                }
            }
        }
    }

    public Brand getMajorBrand() {
        if (this.major == null) {
            this.major = Brand.forID(this.ftyp.getMajorBrand());
        }
        return this.major;
    }

    public Brand getMinorBrand() {
        if (this.minor == null) {
            this.minor = Brand.forID(this.ftyp.getMajorBrand());
        }
        return this.minor;
    }

    public Brand[] getCompatibleBrands() {
        if (this.compatible == null) {
            String[] s = this.ftyp.getCompatibleBrands();
            this.compatible = new Brand[s.length];
            for (int i = 0; i < s.length; i++) {
                this.compatible[i] = Brand.forID(s[i]);
            }
        }
        return this.compatible;
    }

    public Movie getMovie() {
        if (this.moov == null) {
            return null;
        } else {
            if (this.movie == null) {
                this.movie = new Movie(this.moov, this.in);
            }
            return this.movie;
        }
    }

    public List<Box> getBoxes() {
        return Collections.unmodifiableList(this.boxes);
    }

    static {
        Logger log = Logger.getLogger("MP4 API");
        for (Handler h : log.getHandlers()) {
            log.removeHandler(h);
        }
        log.setLevel(Level.WARNING);
        ConsoleHandler h = new ConsoleHandler();
        h.setLevel(Level.ALL);
        log.addHandler(h);
    }
}