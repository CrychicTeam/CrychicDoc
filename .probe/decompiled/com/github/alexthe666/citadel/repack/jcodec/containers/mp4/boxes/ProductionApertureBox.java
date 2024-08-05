package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

public class ProductionApertureBox extends ClearApertureBox {

    public static final String PROF = "prof";

    public static ProductionApertureBox createProductionApertureBox(int width, int height) {
        ProductionApertureBox prof = new ProductionApertureBox(new Header("prof"));
        prof.width = (float) width;
        prof.height = (float) height;
        return prof;
    }

    public ProductionApertureBox(Header atom) {
        super(atom);
    }
}