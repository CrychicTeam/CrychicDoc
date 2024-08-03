package dev.xkmc.l2library.base.effects.api;

public record IconRenderRegion(float x, float y, float scale) {

    public static IconRenderRegion identity() {
        return new IconRenderRegion(0.0F, 0.0F, 1.0F);
    }

    public static IconRenderRegion of(int r, int ix, int iy, int w, int h) {
        float y = ((float) (r - h) / 2.0F + (float) iy) / (float) r;
        float x = ((float) (r - w) / 2.0F + (float) ix) / (float) r;
        return new IconRenderRegion(x, y, 1.0F / (float) r);
    }

    public IconRenderRegion resize(IconRenderRegion inner) {
        return new IconRenderRegion(this.x + inner.x() * this.scale, this.y + inner.y() * this.scale, this.scale * inner.scale);
    }
}