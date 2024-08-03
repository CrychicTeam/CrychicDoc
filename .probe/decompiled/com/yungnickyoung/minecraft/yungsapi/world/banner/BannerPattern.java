package com.yungnickyoung.minecraft.yungsapi.world.banner;

public class BannerPattern {

    private String pattern;

    private int color;

    public BannerPattern(String pattern, int color) {
        this.pattern = pattern;
        this.color = color;
    }

    public String getPattern() {
        return this.pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}