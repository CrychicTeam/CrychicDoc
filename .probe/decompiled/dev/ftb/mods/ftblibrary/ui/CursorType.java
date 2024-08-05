package dev.ftb.mods.ftblibrary.ui;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public enum CursorType {

    ARROW(221185),
    IBEAM(221186),
    CROSSHAIR(221187),
    HAND(221188),
    HRESIZE(221189),
    VRESIZE(221190);

    private final int shape;

    private long cursor = 0L;

    private CursorType(int c) {
        this.shape = c;
    }

    @OnlyIn(Dist.CLIENT)
    public static void set(@Nullable CursorType type) {
        long window = Minecraft.getInstance().getWindow().getWindow();
        if (type == null) {
            GLFW.glfwSetCursor(window, 0L);
        } else {
            if (type.cursor == 0L) {
                type.cursor = GLFW.glfwCreateStandardCursor(type.shape);
            }
            GLFW.glfwSetCursor(window, type.cursor);
        }
    }
}