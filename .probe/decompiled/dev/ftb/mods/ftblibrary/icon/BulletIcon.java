package dev.ftb.mods.ftblibrary.icon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BulletIcon extends Icon {

    private static final MutableColor4I DEFAULT_COLOR = Color4I.rgb(15592941).mutable();

    private static final MutableColor4I DEFAULT_COLOR_B = Color4I.rgb(16777215).mutable();

    private static final MutableColor4I DEFAULT_COLOR_D = Color4I.rgb(14540253).mutable();

    private Color4I color = Icon.empty();

    private Color4I colorB = Icon.empty();

    private Color4I colorD = Icon.empty();

    private boolean inverse = false;

    public BulletIcon copy() {
        BulletIcon icon = new BulletIcon();
        icon.color = this.color;
        icon.colorB = this.colorB;
        icon.colorD = this.colorD;
        icon.inverse = this.inverse;
        return icon;
    }

    public BulletIcon setColor(Color4I col) {
        this.color = col;
        if (this.color.isEmpty()) {
            return this;
        } else {
            MutableColor4I c = this.color.mutable();
            c.addBrightness(18);
            this.colorB = c.copy();
            c = this.color.mutable();
            c.addBrightness(-18);
            this.colorD = c.copy();
            return this;
        }
    }

    public BulletIcon withColor(Color4I col) {
        return this.copy().setColor(col);
    }

    public BulletIcon withTint(Color4I c) {
        return this.withColor(this.color.withTint(c));
    }

    public BulletIcon setInverse(boolean v) {
        this.inverse = v;
        return this;
    }

    @Override
    protected void setProperties(IconProperties properties) {
        super.setProperties(properties);
        this.inverse = properties.getBoolean("inverse", this.inverse);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void draw(GuiGraphics graphics, int x, int y, int w, int h) {
        Color4I c;
        Color4I cb;
        Color4I cd;
        if (this.color.isEmpty()) {
            c = DEFAULT_COLOR;
            cb = DEFAULT_COLOR_B;
            cd = DEFAULT_COLOR_D;
        } else {
            c = this.color;
            cb = this.colorB;
            cd = this.colorD;
        }
        RenderSystem.setShader(GameRenderer::m_172811_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        GuiHelper.addRectToBuffer(graphics, buffer, x, y + 1, 1, h - 2, this.inverse ? cd : cb);
        GuiHelper.addRectToBuffer(graphics, buffer, x + w - 1, y + 1, 1, h - 2, this.inverse ? cb : cd);
        GuiHelper.addRectToBuffer(graphics, buffer, x + 1, y, w - 2, 1, this.inverse ? cd : cb);
        GuiHelper.addRectToBuffer(graphics, buffer, x + 1, y + h - 1, w - 2, 1, this.inverse ? cb : cd);
        GuiHelper.addRectToBuffer(graphics, buffer, x + 1, y + 1, w - 2, h - 2, c);
        tesselator.end();
    }

    @Override
    public JsonElement getJson() {
        JsonObject o = new JsonObject();
        o.addProperty("id", "bullet");
        if (!this.color.isEmpty()) {
            o.add("color", this.color.getJson());
        }
        return o;
    }
}