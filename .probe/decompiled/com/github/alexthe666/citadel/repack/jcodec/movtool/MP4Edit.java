package com.github.alexthe666.citadel.repack.jcodec.movtool;

import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieFragmentBox;

public interface MP4Edit {

    void applyToFragment(MovieBox var1, MovieFragmentBox[] var2);

    void apply(MovieBox var1);
}