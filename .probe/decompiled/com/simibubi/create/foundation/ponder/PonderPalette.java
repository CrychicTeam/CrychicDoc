package com.simibubi.create.foundation.ponder;

import com.simibubi.create.foundation.utility.Color;

public enum PonderPalette {

    WHITE(-1118482),
    BLACK(-14544623),
    RED(-41620),
    GREEN(-7554479),
    BLUE(-10523473),
    SLOW(-14483678),
    MEDIUM(-16743169),
    FAST(-43521),
    INPUT(-8401440),
    OUTPUT(-2244250);

    private final Color color;

    private PonderPalette(int color) {
        this.color = new Color(color);
    }

    public int getColor() {
        return this.color.getRGB();
    }

    public Color getColorObject() {
        return this.color;
    }
}