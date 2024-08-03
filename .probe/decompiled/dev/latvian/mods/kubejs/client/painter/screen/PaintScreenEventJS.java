package dev.latvian.mods.kubejs.client.painter.screen;

import com.mojang.math.Axis;
import dev.latvian.mods.kubejs.client.painter.PaintEventJS;
import dev.latvian.mods.kubejs.client.painter.Painter;
import dev.latvian.mods.unit.UnitVariables;
import dev.latvian.mods.unit.VariableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;

public class PaintScreenEventJS extends PaintEventJS implements UnitVariables {

    public final Painter painter;

    public final int mouseX;

    public final int mouseY;

    public final int width;

    public final int height;

    public final boolean inventory;

    public PaintScreenEventJS(Minecraft m, Screen s, GuiGraphics graphics, Painter painter, int mx, int my, float d) {
        super(m, graphics, d, s);
        this.painter = painter;
        this.mouseX = mx;
        this.mouseY = my;
        this.width = this.mc.getWindow().getGuiScaledWidth();
        this.height = this.mc.getWindow().getGuiScaledHeight();
        this.inventory = true;
    }

    public PaintScreenEventJS(Minecraft m, GuiGraphics graphics, Painter painter, float d) {
        super(m, graphics, d, null);
        this.painter = painter;
        this.mouseX = -1;
        this.mouseY = -1;
        this.width = this.mc.getWindow().getGuiScaledWidth();
        this.height = this.mc.getWindow().getGuiScaledHeight();
        this.inventory = false;
    }

    @Override
    public VariableSet getVariables() {
        return this.painter.getVariables();
    }

    public float alignX(float x, float w, AlignMode alignX) {
        return switch(alignX) {
            case END ->
                (float) this.width - w + x;
            case CENTER ->
                ((float) this.width - w) / 2.0F + x;
            default ->
                x;
        };
    }

    public float alignY(float y, float h, AlignMode alignY) {
        return switch(alignY) {
            case END ->
                (float) this.height - h + y;
            case CENTER ->
                ((float) this.height - h) / 2.0F + y;
            default ->
                y;
        };
    }

    public void translate(double x, double y) {
        this.translate(x, y, 0.0);
    }

    public void scale(float x, float y) {
        this.scale(x, y, 1.0F);
    }

    public void scale(float scale) {
        this.scale(scale, scale, 1.0F);
    }

    public void rotateDeg(float angle) {
        this.matrices.mulPose(Axis.ZP.rotationDegrees(angle));
    }

    public void rotateRad(float angle) {
        this.matrices.mulPose(Axis.ZP.rotation(angle));
    }

    public void rectangle(float x, float y, float z, float w, float h, int color) {
        Matrix4f m = this.getMatrix();
        this.vertex(m, x + w, y, z, color);
        this.vertex(m, x, y, z, color);
        this.vertex(m, x, y + h, z, color);
        this.vertex(m, x + w, y + h, z, color);
    }

    public void rectangle(float x, float y, float z, float w, float h, int color, float u0, float v0, float u1, float v1) {
        Matrix4f m = this.getMatrix();
        this.vertex(m, x + w, y, z, color, u1, v0);
        this.vertex(m, x, y, z, color, u0, v0);
        this.vertex(m, x, y + h, z, color, u0, v1);
        this.vertex(m, x + w, y + h, z, color, u1, v1);
    }

    public void text(Component text, int x, int y, int color, boolean shadow) {
        this.rawText(text.getVisualOrderText(), x, y, color, shadow);
    }

    public void rawText(FormattedCharSequence text, int x, int y, int color, boolean shadow) {
        this.graphics.drawString(this.mc.font, text, x, y, color, shadow);
    }
}