package net.minecraft.client.renderer;

import net.minecraft.client.Minecraft;

public class PanoramaRenderer {

    private final Minecraft minecraft;

    private final CubeMap cubeMap;

    private float spin;

    private float bob;

    public PanoramaRenderer(CubeMap cubeMap0) {
        this.cubeMap = cubeMap0;
        this.minecraft = Minecraft.getInstance();
    }

    public void render(float float0, float float1) {
        float $$2 = (float) ((double) float0 * this.minecraft.options.panoramaSpeed().get());
        this.spin = wrap(this.spin + $$2 * 0.1F, 360.0F);
        this.bob = wrap(this.bob + $$2 * 0.001F, (float) (Math.PI * 2));
        this.cubeMap.render(this.minecraft, 10.0F, -this.spin, float1);
    }

    private static float wrap(float float0, float float1) {
        return float0 > float1 ? float0 - float1 : float0;
    }
}