package com.mna.api.particles.parameters;

import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ParticleColor {

    private final float r;

    private final float g;

    private final float b;

    private final float a;

    private final int color;

    public ParticleColor(int r, int g, int b) {
        this.r = (float) r / 255.0F;
        this.g = (float) g / 255.0F;
        this.b = (float) b / 255.0F;
        this.a = 1.0F;
        this.color = 0xFF000000 | r << 16 | g << 8 | b;
    }

    public ParticleColor(int r, int g, int b, int a) {
        this.r = (float) r / 255.0F;
        this.g = (float) g / 255.0F;
        this.b = (float) b / 255.0F;
        this.a = (float) a / 255.0F;
        this.color = a << 24 | r << 16 | g << 8 | b;
    }

    public static ParticleColor makeRandomColor(int r, int g, int b, Random random) {
        return new ParticleColor(random.nextInt(r), random.nextInt(g), random.nextInt(b));
    }

    public ParticleColor(float r, float g, float b) {
        this((int) r, (int) g, (int) b);
    }

    public ParticleColor(float r, float g, float b, float a) {
        this((int) r, (int) g, (int) b, (int) a);
    }

    public static ParticleColor fromInt(int color) {
        int a = color >> 24 & 0xFF;
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color >> 0 & 0xFF;
        return new ParticleColor(r, g, b, a);
    }

    public float getRed() {
        return this.r;
    }

    public float getGreen() {
        return this.g;
    }

    public float getBlue() {
        return this.b;
    }

    public float getAlpha() {
        return this.a;
    }

    public int getColor() {
        return this.color;
    }

    public String serialize() {
        return this.r + "," + this.g + "," + this.b + "," + this.a;
    }

    public ParticleColor.IntWrapper toWrapper() {
        return new ParticleColor.IntWrapper(this);
    }

    @Nullable
    public static ParticleColor deserialize(String string) {
        if (string == null) {
            return null;
        } else {
            String[] arr = string.split(",");
            if (arr.length == 3) {
                return new ParticleColor(Integer.parseInt(arr[0].trim()), Integer.parseInt(arr[1].trim()), Integer.parseInt(arr[2].trim()));
            } else {
                return arr.length == 4 ? new ParticleColor(Integer.parseInt(arr[0].trim()), Integer.parseInt(arr[1].trim()), Integer.parseInt(arr[2].trim()), Integer.parseInt(arr[3].trim())) : null;
            }
        }
    }

    public static class IntWrapper {

        public int r;

        public int g;

        public int b;

        public int a;

        public IntWrapper(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = 255;
        }

        public IntWrapper(int r, int g, int b, int a) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
        }

        public IntWrapper(ParticleColor color) {
            this.r = (int) ((double) color.getRed() * 255.0);
            this.g = (int) ((double) color.getGreen() * 255.0);
            this.b = (int) ((double) color.getBlue() * 255.0);
            this.a = (int) ((double) color.getAlpha() * 255.0);
        }

        public ParticleColor toParticleColor() {
            return new ParticleColor(this.r, this.g, this.b, this.a);
        }

        public String serialize() {
            return this.r + "," + this.g + "," + this.b + "," + this.a;
        }

        @Nonnull
        public static ParticleColor.IntWrapper deserialize(String string) {
            ParticleColor.IntWrapper color = defaultParticleColorWrapper();
            try {
                String[] arr = string.split(",");
                return new ParticleColor.IntWrapper(Integer.parseInt(arr[0].trim()), Integer.parseInt(arr[1].trim()), Integer.parseInt(arr[2].trim()), Integer.parseInt(arr[3].trim()));
            } catch (Exception var3) {
                var3.printStackTrace();
                return color;
            }
        }

        public static ParticleColor.IntWrapper defaultParticleColorWrapper() {
            return new ParticleColor.IntWrapper(255, 25, 180, 255);
        }
    }
}