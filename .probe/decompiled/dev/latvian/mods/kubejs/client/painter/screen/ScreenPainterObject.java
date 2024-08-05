package dev.latvian.mods.kubejs.client.painter.screen;

import dev.latvian.mods.kubejs.client.painter.PainterObject;
import dev.latvian.mods.kubejs.client.painter.PainterObjectProperties;
import dev.latvian.mods.unit.FixedNumberUnit;
import dev.latvian.mods.unit.Unit;

public abstract class ScreenPainterObject extends PainterObject {

    public Unit x = FixedNumberUnit.ZERO;

    public Unit y = FixedNumberUnit.ZERO;

    public Unit z = FixedNumberUnit.ZERO;

    public ScreenDrawMode draw = ScreenDrawMode.INGAME;

    public void preDraw(PaintScreenEventJS event) {
    }

    public abstract void draw(PaintScreenEventJS var1);

    @Override
    protected void load(PainterObjectProperties properties) {
        super.load(properties);
        this.x = properties.getUnit("x", this.x).add(properties.getUnit("moveX", FixedNumberUnit.ZERO));
        this.y = properties.getUnit("y", this.y).add(properties.getUnit("moveY", FixedNumberUnit.ZERO));
        this.z = properties.getUnit("z", this.z);
        if (properties.hasString("draw")) {
            String var2 = properties.getString("draw", "ingame");
            switch(var2) {
                case "always":
                    this.draw = ScreenDrawMode.ALWAYS;
                    break;
                case "gui":
                    this.draw = ScreenDrawMode.GUI;
                    break;
                default:
                    this.draw = ScreenDrawMode.INGAME;
            }
        }
    }
}