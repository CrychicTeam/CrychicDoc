package me.shedaniel.math.impl;

import me.shedaniel.math.FloatingPoint;
import me.shedaniel.math.Point;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PointHelper {

    public static Point ofMouse() {
        Minecraft client = Minecraft.getInstance();
        double mx = client.mouseHandler.xpos() * (double) client.getWindow().getGuiScaledWidth() / (double) client.getWindow().getScreenWidth();
        double my = client.mouseHandler.ypos() * (double) client.getWindow().getGuiScaledHeight() / (double) client.getWindow().getScreenHeight();
        return new Point(mx, my);
    }

    public static FloatingPoint ofFloatingMouse() {
        Minecraft client = Minecraft.getInstance();
        double mx = client.mouseHandler.xpos() * (double) client.getWindow().getGuiScaledWidth() / (double) client.getWindow().getScreenWidth();
        double my = client.mouseHandler.ypos() * (double) client.getWindow().getGuiScaledHeight() / (double) client.getWindow().getScreenHeight();
        return new FloatingPoint(mx, my);
    }

    public static int getMouseX() {
        return ofMouse().x;
    }

    public static int getMouseY() {
        return ofMouse().y;
    }

    public static double getMouseFloatingX() {
        return ofFloatingMouse().x;
    }

    public static double getMouseFloatingY() {
        return ofFloatingMouse().y;
    }
}