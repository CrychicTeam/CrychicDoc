package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

public class PartialSyncSamplesBox extends SyncSamplesBox {

    public static final String STPS = "stps";

    public PartialSyncSamplesBox(Header header) {
        super(header);
    }
}