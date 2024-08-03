package com.github.alexthe666.citadel.repack.jcodec.movtool;

import com.github.alexthe666.citadel.repack.jcodec.common.Format;
import com.github.alexthe666.citadel.repack.jcodec.common.JCodecUtil;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.MP4Util;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Header;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MetaBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MetaValue;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieFragmentBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.NodeBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.UdtaMetaBox;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MetadataEditor {

    private Map<String, MetaValue> keyedMeta;

    private Map<Integer, MetaValue> itunesMeta;

    private File source;

    public MetadataEditor(File source, Map<String, MetaValue> keyedMeta, Map<Integer, MetaValue> itunesMeta) {
        this.source = source;
        this.keyedMeta = keyedMeta;
        this.itunesMeta = itunesMeta;
    }

    public static MetadataEditor createFrom(File f) throws IOException {
        Format format = JCodecUtil.detectFormat(f);
        if (format != Format.MOV) {
            throw new IllegalArgumentException("Unsupported format: " + format);
        } else {
            MP4Util.Movie movie = MP4Util.parseFullMovie(f);
            MetaBox keyedMeta = NodeBox.findFirst(movie.getMoov(), MetaBox.class, MetaBox.fourcc());
            MetaBox itunesMeta = NodeBox.findFirstPath(movie.getMoov(), MetaBox.class, new String[] { "udta", MetaBox.fourcc() });
            return new MetadataEditor(f, (Map<String, MetaValue>) (keyedMeta == null ? new HashMap() : keyedMeta.getKeyedMeta()), (Map<Integer, MetaValue>) (itunesMeta == null ? new HashMap() : itunesMeta.getItunesMeta()));
        }
    }

    public void save(boolean fast) throws IOException {
        final MetadataEditor self = this;
        MP4Edit edit = new MP4Edit() {

            @Override
            public void applyToFragment(MovieBox mov, MovieFragmentBox[] fragmentBox) {
            }

            @Override
            public void apply(MovieBox movie) {
                MetaBox meta1 = NodeBox.findFirst(movie, MetaBox.class, MetaBox.fourcc());
                MetaBox meta2 = NodeBox.findFirstPath(movie, MetaBox.class, new String[] { "udta", MetaBox.fourcc() });
                if (self.keyedMeta != null && self.keyedMeta.size() > 0) {
                    if (meta1 == null) {
                        meta1 = MetaBox.createMetaBox();
                        movie.add(meta1);
                    }
                    meta1.setKeyedMeta(self.keyedMeta);
                }
                if (self.itunesMeta != null && self.itunesMeta.size() > 0) {
                    if (meta2 == null) {
                        meta2 = UdtaMetaBox.createUdtaMetaBox();
                        NodeBox udta = NodeBox.findFirst(movie, NodeBox.class, "udta");
                        if (udta == null) {
                            udta = new NodeBox(Header.createHeader("udta", 0L));
                            movie.add(udta);
                        }
                        udta.add(meta2);
                    }
                    meta2.setItunesMeta(self.itunesMeta);
                }
            }
        };
        if (fast) {
            new RelocateMP4Editor().modifyOrRelocate(this.source, edit);
        } else {
            new ReplaceMP4Editor().modifyOrReplace(this.source, edit);
        }
    }

    public Map<Integer, MetaValue> getItunesMeta() {
        return this.itunesMeta;
    }

    public Map<String, MetaValue> getKeyedMeta() {
        return this.keyedMeta;
    }
}