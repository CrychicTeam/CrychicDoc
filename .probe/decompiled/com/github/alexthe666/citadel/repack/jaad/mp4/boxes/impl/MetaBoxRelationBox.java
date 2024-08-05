package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class MetaBoxRelationBox extends FullBox {

    private long firstMetaboxHandlerType;

    private long secondMetaboxHandlerType;

    private int metaboxRelation;

    public MetaBoxRelationBox() {
        super("Meta Box Relation Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.firstMetaboxHandlerType = in.readBytes(4);
        this.secondMetaboxHandlerType = in.readBytes(4);
        this.metaboxRelation = in.read();
    }

    public long getFirstMetaboxHandlerType() {
        return this.firstMetaboxHandlerType;
    }

    public long getSecondMetaboxHandlerType() {
        return this.secondMetaboxHandlerType;
    }

    public int getMetaboxRelation() {
        return this.metaboxRelation;
    }
}