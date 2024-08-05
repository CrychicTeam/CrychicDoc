package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg4.es;

import java.nio.ByteBuffer;
import java.util.Collection;

public class NodeDescriptor extends Descriptor {

    private Collection<Descriptor> children;

    public NodeDescriptor(int tag, Collection<Descriptor> children) {
        super(tag, 0);
        this.children = children;
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        for (Descriptor descr : this.children) {
            descr.write(out);
        }
    }

    public Collection<Descriptor> getChildren() {
        return this.children;
    }

    public static <T> T findByTag(Descriptor es, int tag) {
        if (es.getTag() == tag) {
            return (T) es;
        } else {
            if (es instanceof NodeDescriptor) {
                for (Descriptor descriptor : ((NodeDescriptor) es).getChildren()) {
                    T res = findByTag(descriptor, tag);
                    if (res != null) {
                        return res;
                    }
                }
            }
            return null;
        }
    }
}