package com.mojang.blaze3d.platform;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWVidMode.Buffer;

public final class VideoMode {

    private final int width;

    private final int height;

    private final int redBits;

    private final int greenBits;

    private final int blueBits;

    private final int refreshRate;

    private static final Pattern PATTERN = Pattern.compile("(\\d+)x(\\d+)(?:@(\\d+)(?::(\\d+))?)?");

    public VideoMode(int int0, int int1, int int2, int int3, int int4, int int5) {
        this.width = int0;
        this.height = int1;
        this.redBits = int2;
        this.greenBits = int3;
        this.blueBits = int4;
        this.refreshRate = int5;
    }

    public VideoMode(Buffer buffer0) {
        this.width = buffer0.width();
        this.height = buffer0.height();
        this.redBits = buffer0.redBits();
        this.greenBits = buffer0.greenBits();
        this.blueBits = buffer0.blueBits();
        this.refreshRate = buffer0.refreshRate();
    }

    public VideoMode(GLFWVidMode gLFWVidMode0) {
        this.width = gLFWVidMode0.width();
        this.height = gLFWVidMode0.height();
        this.redBits = gLFWVidMode0.redBits();
        this.greenBits = gLFWVidMode0.greenBits();
        this.blueBits = gLFWVidMode0.blueBits();
        this.refreshRate = gLFWVidMode0.refreshRate();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getRedBits() {
        return this.redBits;
    }

    public int getGreenBits() {
        return this.greenBits;
    }

    public int getBlueBits() {
        return this.blueBits;
    }

    public int getRefreshRate() {
        return this.refreshRate;
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else if (object0 != null && this.getClass() == object0.getClass()) {
            VideoMode $$1 = (VideoMode) object0;
            return this.width == $$1.width && this.height == $$1.height && this.redBits == $$1.redBits && this.greenBits == $$1.greenBits && this.blueBits == $$1.blueBits && this.refreshRate == $$1.refreshRate;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.width, this.height, this.redBits, this.greenBits, this.blueBits, this.refreshRate });
    }

    public String toString() {
        return String.format(Locale.ROOT, "%sx%s@%s (%sbit)", this.width, this.height, this.refreshRate, this.redBits + this.greenBits + this.blueBits);
    }

    public static Optional<VideoMode> read(@Nullable String string0) {
        if (string0 == null) {
            return Optional.empty();
        } else {
            try {
                Matcher $$1 = PATTERN.matcher(string0);
                if ($$1.matches()) {
                    int $$2 = Integer.parseInt($$1.group(1));
                    int $$3 = Integer.parseInt($$1.group(2));
                    String $$4 = $$1.group(3);
                    int $$5;
                    if ($$4 == null) {
                        $$5 = 60;
                    } else {
                        $$5 = Integer.parseInt($$4);
                    }
                    String $$7 = $$1.group(4);
                    int $$8;
                    if ($$7 == null) {
                        $$8 = 24;
                    } else {
                        $$8 = Integer.parseInt($$7);
                    }
                    int $$10 = $$8 / 3;
                    return Optional.of(new VideoMode($$2, $$3, $$10, $$10, $$10, $$5));
                }
            } catch (Exception var9) {
            }
            return Optional.empty();
        }
    }

    public String write() {
        return String.format(Locale.ROOT, "%sx%s@%s:%s", this.width, this.height, this.refreshRate, this.redBits + this.greenBits + this.blueBits);
    }
}