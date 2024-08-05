package dev.latvian.mods.rhino.regexp;

final class RECharSet {

    final int length;

    final int startIndex;

    final int strlength;

    final boolean sense;

    transient volatile boolean converted;

    transient volatile byte[] bits;

    RECharSet(int length, int startIndex, int strlength, boolean sense) {
        this.length = length;
        this.startIndex = startIndex;
        this.strlength = strlength;
        this.sense = sense;
    }
}