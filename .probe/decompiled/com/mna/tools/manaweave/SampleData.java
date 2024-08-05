package com.mna.tools.manaweave;

import net.minecraft.resources.ResourceLocation;

public class SampleData implements Comparable<SampleData>, Cloneable {

    protected boolean[][] grid;

    protected ResourceLocation pattern;

    public SampleData(ResourceLocation pattern, int width, int height) {
        this.grid = new boolean[width][height];
        this.pattern = pattern;
    }

    public void clear() {
        for (int x = 0; x < this.grid.length; x++) {
            for (int y = 0; y < this.grid[0].length; y++) {
                this.grid[x][y] = false;
            }
        }
    }

    public Object clone() {
        SampleData obj = new SampleData(this.pattern, this.getWidth(), this.getHeight());
        for (int y = 0; y < this.getHeight(); y++) {
            for (int x = 0; x < this.getWidth(); x++) {
                obj.setData(x, y, this.getData(x, y));
            }
        }
        return obj;
    }

    public int compareTo(SampleData o) {
        return this.getPattern().compareNamespaced(o.getPattern());
    }

    public boolean getData(int x, int y) {
        return this.grid[x][y];
    }

    public void setData(int x, int y, boolean v) {
        this.grid[x][y] = v;
    }

    public void setGrid(byte[][] data) {
        this.grid = new boolean[data.length][data[0].length];
        for (int x = 0; x < this.grid.length; x++) {
            for (int y = 0; y < this.grid[0].length; y++) {
                this.grid[x][y] = data[x][y] != 0;
            }
        }
    }

    public int getHeight() {
        return this.grid[0].length;
    }

    public int getWidth() {
        return this.grid.length;
    }

    public ResourceLocation getPattern() {
        return this.pattern;
    }

    public void setPattern(ResourceLocation pattern) {
        this.pattern = pattern;
    }

    public String toString() {
        return this.pattern.toString();
    }
}