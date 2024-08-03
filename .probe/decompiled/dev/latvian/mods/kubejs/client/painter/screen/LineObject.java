package dev.latvian.mods.kubejs.client.painter.screen;

import dev.latvian.mods.kubejs.client.painter.Painter;
import dev.latvian.mods.kubejs.client.painter.PainterObjectProperties;
import dev.latvian.mods.unit.FixedColorUnit;
import dev.latvian.mods.unit.FixedNumberUnit;
import dev.latvian.mods.unit.Unit;

public class LineObject extends ScreenPainterObject {

    public Unit color = FixedColorUnit.WHITE;

    public Unit x2 = FixedNumberUnit.ZERO;

    public Unit y2 = FixedNumberUnit.ZERO;

    public Unit size = FixedNumberUnit.ZERO;

    public Unit length = FixedNumberUnit.ZERO;

    public Unit rotation = FixedNumberUnit.ZERO;

    public Unit offset = FixedNumberUnit.ZERO;

    public LineObject(Painter painter) {
    }

    @Override
    protected void load(PainterObjectProperties properties) {
        super.load(properties);
        this.color = properties.getColor("color", this.color);
        this.x2 = properties.getUnit("x2", this.x2);
        this.y2 = properties.getUnit("y2", this.y2);
        this.size = properties.getUnit("size", this.size);
        this.length = properties.getUnit("length", this.length);
        this.rotation = properties.getUnit("rotation", this.rotation);
        this.offset = properties.getUnit("offset", this.offset);
    }

    @Override
    public void draw(PaintScreenEventJS event) {
        float ax = this.x.getFloat(event);
        float ay = this.y.getFloat(event);
        float az = this.z.getFloat(event);
        float as = ((Unit) (this.size == FixedNumberUnit.ZERO ? event.painter.defaultLineSizeUnit : this.size)).getFloat(event);
        float alength = this.length.getFloat(event);
        float aangle = this.rotation.getFloat(event);
        if (alength <= 1.0E-4F) {
            if (this.x2 == FixedNumberUnit.ZERO && this.y2 == FixedNumberUnit.ZERO) {
                return;
            }
            float ax2 = this.x2.getFloat(event);
            float ay2 = this.y2.getFloat(event);
            alength = (float) Math.sqrt((double) ((ax2 - ax) * (ax2 - ax) + (ay2 - ay) * (ay2 - ay)));
            aangle = (float) Math.toDegrees(Math.atan2((double) (ay2 - ay), (double) (ax2 - ax)));
        }
        event.push();
        event.translate((double) ax, (double) ay);
        event.rotateDeg(aangle);
        event.setPositionColorShader();
        event.blend(true);
        event.beginQuads(false);
        event.rectangle(this.offset.getFloat(event), -as / 2.0F, az, alength, as, this.color.getInt(event));
        event.end();
        event.pop();
    }
}