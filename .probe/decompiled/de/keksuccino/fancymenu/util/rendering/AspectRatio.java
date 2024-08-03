package de.keksuccino.fancymenu.util.rendering;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AspectRatio {

    private static final Logger LOGGER = LogManager.getLogger();

    protected final int width;

    protected final int height;

    public AspectRatio(int originalWidth, int originalHeight) {
        this.width = originalWidth;
        this.height = originalHeight;
    }

    public int getInputWidth() {
        return this.width;
    }

    public int getInputHeight() {
        return this.height;
    }

    public int getAspectRatioWidth(int givenHeight) {
        double ratio = (double) this.getInputWidth() / (double) this.getInputHeight();
        return (int) ((double) givenHeight * ratio);
    }

    public int getAspectRatioHeight(int givenWidth) {
        double ratio = (double) this.getInputWidth() / (double) this.getInputHeight();
        return (int) ((double) givenWidth / ratio);
    }

    public int[] getAspectRatioSizeByMinimumSize(int givenWidth, int givenHeight) {
        int aw = this.getAspectRatioWidth(givenHeight);
        int ah = givenHeight;
        if (aw < givenWidth) {
            ah = this.getAspectRatioHeight(givenWidth);
            aw = givenWidth;
        }
        return new int[] { aw, ah };
    }

    public int[] getAspectRatioSizeByMaximumSize(int givenWidth, int givenHeight) {
        int aw = this.getAspectRatioWidth(givenHeight);
        int ah = givenHeight;
        if (aw > givenWidth) {
            ah = this.getAspectRatioHeight(givenWidth);
            aw = givenWidth;
        }
        return new int[] { aw, ah };
    }
}