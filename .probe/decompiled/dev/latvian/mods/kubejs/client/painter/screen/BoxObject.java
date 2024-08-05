package dev.latvian.mods.kubejs.client.painter.screen;

import dev.latvian.mods.kubejs.client.painter.Painter;
import dev.latvian.mods.kubejs.client.painter.PainterObjectProperties;
import dev.latvian.mods.unit.FixedNumberUnit;
import dev.latvian.mods.unit.Unit;

public abstract class BoxObject extends ScreenPainterObject {

    private static final Unit DEFAULT_SIZE = FixedNumberUnit.SIXTEEN;

    public Unit w = DEFAULT_SIZE;

    public Unit h = DEFAULT_SIZE;

    public AlignMode alignX = AlignMode.START;

    public AlignMode alignY = AlignMode.START;

    public BoxObject(Painter painter) {
    }

    @Override
    protected void load(PainterObjectProperties properties) {
        super.load(properties);
        this.w = properties.getUnit("w", this.w).add(properties.getUnit("expandW", FixedNumberUnit.ZERO));
        this.h = properties.getUnit("h", this.h).add(properties.getUnit("expandH", FixedNumberUnit.ZERO));
        if (properties.hasString("alignX")) {
            String var2 = properties.getString("alignX", "left");
            switch(var2) {
                case "right":
                case "end":
                    this.alignX = AlignMode.END;
                    break;
                case "center":
                    this.alignX = AlignMode.CENTER;
                    break;
                default:
                    this.alignX = AlignMode.START;
            }
        }
        if (properties.hasString("alignY")) {
            String var4 = properties.getString("alignY", "top");
            switch(var4) {
                case "bottom":
                case "end":
                    this.alignY = AlignMode.END;
                    break;
                case "center":
                    this.alignY = AlignMode.CENTER;
                    break;
                default:
                    this.alignY = AlignMode.START;
            }
        }
    }
}