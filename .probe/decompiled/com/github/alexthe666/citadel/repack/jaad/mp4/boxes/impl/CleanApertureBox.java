package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.BoxImpl;
import java.io.IOException;

public class CleanApertureBox extends BoxImpl {

    private long cleanApertureWidthN;

    private long cleanApertureWidthD;

    private long cleanApertureHeightN;

    private long cleanApertureHeightD;

    private long horizOffN;

    private long horizOffD;

    private long vertOffN;

    private long vertOffD;

    public CleanApertureBox() {
        super("Clean Aperture Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        this.cleanApertureWidthN = in.readBytes(4);
        this.cleanApertureWidthD = in.readBytes(4);
        this.cleanApertureHeightN = in.readBytes(4);
        this.cleanApertureHeightD = in.readBytes(4);
        this.horizOffN = in.readBytes(4);
        this.horizOffD = in.readBytes(4);
        this.vertOffN = in.readBytes(4);
        this.vertOffD = in.readBytes(4);
    }

    public long getCleanApertureWidthN() {
        return this.cleanApertureWidthN;
    }

    public long getCleanApertureWidthD() {
        return this.cleanApertureWidthD;
    }

    public long getCleanApertureHeightN() {
        return this.cleanApertureHeightN;
    }

    public long getCleanApertureHeightD() {
        return this.cleanApertureHeightD;
    }

    public long getHorizOffN() {
        return this.horizOffN;
    }

    public long getHorizOffD() {
        return this.horizOffD;
    }

    public long getVertOffN() {
        return this.vertOffN;
    }

    public long getVertOffD() {
        return this.vertOffD;
    }
}