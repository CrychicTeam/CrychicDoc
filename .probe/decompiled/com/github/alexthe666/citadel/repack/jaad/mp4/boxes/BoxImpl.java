package com.github.alexthe666.citadel.repack.jaad.mp4.boxes;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoxImpl implements Box {

    private final String name;

    protected long size;

    protected long type;

    protected long offset;

    protected Box parent;

    protected final List<Box> children;

    public BoxImpl(String name) {
        this.name = name;
        this.children = new ArrayList(4);
    }

    public void setParams(Box parent, long size, long type, long offset) {
        this.size = size;
        this.type = type;
        this.parent = parent;
        this.offset = offset;
    }

    protected long getLeft(MP4InputStream in) throws IOException {
        return this.offset + this.size - in.getOffset();
    }

    public void decode(MP4InputStream in) throws IOException {
    }

    @Override
    public long getType() {
        return this.type;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public long getOffset() {
        return this.offset;
    }

    @Override
    public Box getParent() {
        return this.parent;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name + " [" + BoxFactory.typeToString(this.type) + "]";
    }

    @Override
    public boolean hasChildren() {
        return this.children.size() > 0;
    }

    @Override
    public boolean hasChild(long type) {
        boolean b = false;
        for (Box box : this.children) {
            if (box.getType() == type) {
                b = true;
                break;
            }
        }
        return b;
    }

    @Override
    public Box getChild(long type) {
        Box box = null;
        Box b = null;
        for (int i = 0; box == null && i < this.children.size(); i++) {
            b = (Box) this.children.get(i);
            if (b.getType() == type) {
                box = b;
            }
        }
        return box;
    }

    @Override
    public List<Box> getChildren() {
        return Collections.unmodifiableList(this.children);
    }

    @Override
    public List<Box> getChildren(long type) {
        List<Box> l = new ArrayList();
        for (Box box : this.children) {
            if (box.getType() == type) {
                l.add(box);
            }
        }
        return l;
    }

    protected void readChildren(MP4InputStream in) throws IOException {
        while (in.getOffset() < this.offset + this.size) {
            Box box = BoxFactory.parseBox(this, in);
            this.children.add(box);
        }
    }

    protected void readChildren(MP4InputStream in, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            Box box = BoxFactory.parseBox(this, in);
            this.children.add(box);
        }
    }
}