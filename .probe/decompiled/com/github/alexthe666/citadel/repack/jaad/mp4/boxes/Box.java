package com.github.alexthe666.citadel.repack.jaad.mp4.boxes;

import java.util.List;

public interface Box {

    Box getParent();

    long getSize();

    long getType();

    long getOffset();

    String getName();

    boolean hasChildren();

    boolean hasChild(long var1);

    List<Box> getChildren();

    List<Box> getChildren(long var1);

    Box getChild(long var1);
}