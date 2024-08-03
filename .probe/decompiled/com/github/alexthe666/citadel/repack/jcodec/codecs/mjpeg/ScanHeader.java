package com.github.alexthe666.citadel.repack.jcodec.codecs.mjpeg;

import java.nio.ByteBuffer;

public class ScanHeader {

    int ls;

    int ns;

    ScanHeader.Component[] components;

    int ss;

    int se;

    int ah;

    int al;

    public boolean isInterleaved() {
        return this.ns > 1;
    }

    public static ScanHeader read(ByteBuffer bb) {
        ScanHeader scan = new ScanHeader();
        scan.ls = bb.getShort() & '\uffff';
        scan.ns = bb.get() & 255;
        scan.components = new ScanHeader.Component[scan.ns];
        for (int i = 0; i < scan.components.length; i++) {
            ScanHeader.Component c = scan.components[i] = new ScanHeader.Component();
            c.cs = bb.get() & 255;
            int tdta = bb.get() & 255;
            c.td = (tdta & 240) >>> 4;
            c.ta = tdta & 15;
        }
        scan.ss = bb.get() & 255;
        scan.se = bb.get() & 255;
        int ahal = bb.get() & 255;
        scan.ah = (ahal & 240) >>> 4;
        scan.al = ahal & 15;
        return scan;
    }

    public static class Component {

        int cs;

        int td;

        int ta;
    }
}