package de.keksuccino.konkrete.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CurrentScreenHandler {

    private static Screen lastScreen;

    private static PoseStack currentStack;

    private static GuiGraphics currentGraphics;

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new CurrentScreenHandler());
    }

    public static Screen getScreen() {
        return Minecraft.getInstance().screen;
    }

    public static PoseStack getPoseStack() {
        if (currentStack == null) {
            currentStack = new PoseStack();
        }
        return currentStack;
    }

    public static GuiGraphics getGraphics() {
        return currentGraphics != null ? currentGraphics : new GuiGraphics(Minecraft.getInstance(), Minecraft.getInstance().renderBuffers().bufferSource());
    }

    public static int getWidth() {
        return getScreen() != null ? getScreen().width : 0;
    }

    public static void setWidth(int width) {
        if (getScreen() != null) {
            getScreen().width = width;
        }
    }

    public static int getHeight() {
        return getScreen() != null ? getScreen().height : 0;
    }

    public static void setHeight(int height) {
        if (getScreen() != null) {
            getScreen().height = height;
        }
    }

    public static Screen getLastScreen() {
        return lastScreen;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onDrawScreen(ScreenEvent.Render.Pre e) {
        currentStack = e.getGuiGraphics().pose();
        currentGraphics = e.getGuiGraphics();
    }

    @SubscribeEvent
    public void onInitPost(ScreenEvent.Init.Post e) {
        lastScreen = e.getScreen();
    }
}