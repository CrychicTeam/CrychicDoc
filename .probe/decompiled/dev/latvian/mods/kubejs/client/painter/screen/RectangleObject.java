package dev.latvian.mods.kubejs.client.painter.screen;

import dev.latvian.mods.kubejs.client.painter.Painter;
import dev.latvian.mods.kubejs.client.painter.PainterObjectProperties;
import dev.latvian.mods.unit.FixedColorUnit;
import dev.latvian.mods.unit.FixedNumberUnit;
import dev.latvian.mods.unit.Unit;
import net.minecraft.resources.ResourceLocation;

public class RectangleObject extends BoxObject {

    public Unit color = FixedColorUnit.WHITE;

    public ResourceLocation texture = null;

    public Unit u0 = FixedNumberUnit.ZERO;

    public Unit v0 = FixedNumberUnit.ZERO;

    public Unit u1 = FixedNumberUnit.ONE;

    public Unit v1 = FixedNumberUnit.ONE;

    public RectangleObject(Painter painter) {
        super(painter);
    }

    @Override
    protected void load(PainterObjectProperties properties) {
        super.load(properties);
        this.color = properties.getColor("color", this.color);
        this.texture = properties.getResourceLocation("texture", this.texture);
        this.u0 = properties.getUnit("u0", this.u0);
        this.v0 = properties.getUnit("v0", this.v0);
        this.u1 = properties.getUnit("u1", this.u1);
        this.v1 = properties.getUnit("v1", this.v1);
    }

    @Override
    public void draw(PaintScreenEventJS event) {
        float aw = this.w.getFloat(event);
        float ah = this.h.getFloat(event);
        float ax = event.alignX(this.x.getFloat(event), aw, this.alignX);
        float ay = event.alignY(this.y.getFloat(event), ah, this.alignY);
        float az = this.z.getFloat(event);
        if (this.texture == null) {
            event.setPositionColorShader();
            event.blend(true);
            event.beginQuads(false);
            event.rectangle(ax, ay, az, aw, ah, this.color.getInt(event));
            event.end();
        } else {
            float u0f = this.u0.getFloat(event);
            float v0f = this.v0.getFloat(event);
            float u1f = this.u1.getFloat(event);
            float v1f = this.v1.getFloat(event);
            event.setPositionColorTextureShader();
            event.setShaderTexture(this.texture);
            event.blend(true);
            event.beginQuads(true);
            event.rectangle(ax, ay, az, aw, ah, this.color.getInt(event), u0f, v0f, u1f, v1f);
            event.end();
        }
    }
}