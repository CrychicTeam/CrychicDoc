package de.keksuccino.konkrete.input;

import de.keksuccino.konkrete.reflection.ReflectionHelper;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MouseInput {

    private static boolean leftClicked = false;

    private static boolean rightClicked = false;

    private static Map<String, Boolean> vanillainput = new HashMap();

    private static boolean ignoreBlocked = false;

    private static boolean useRenderScale = false;

    private static float renderScale = 1.0F;

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new MouseInput());
    }

    public static int getActiveMouseButton() {
        int b = -1;
        Field f = ReflectionHelper.findField(MouseHandler.class, "f_91510_");
        try {
            b = (Integer) f.get(Minecraft.getInstance().mouseHandler);
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        return b;
    }

    public static boolean isLeftMouseDown() {
        return leftClicked;
    }

    public static boolean isRightMouseDown() {
        return rightClicked;
    }

    public static int getMouseX() {
        int x = (int) (Minecraft.getInstance().mouseHandler.xpos() * (double) Minecraft.getInstance().getWindow().getGuiScaledWidth() / (double) Minecraft.getInstance().getWindow().getScreenWidth());
        return useRenderScale ? (int) ((float) x / renderScale) : x;
    }

    public static int getMouseY() {
        int y = (int) (Minecraft.getInstance().mouseHandler.ypos() * (double) Minecraft.getInstance().getWindow().getGuiScaledHeight() / (double) Minecraft.getInstance().getWindow().getScreenHeight());
        return useRenderScale ? (int) ((float) y / renderScale) : y;
    }

    public static void setRenderScale(float scale) {
        renderScale = scale;
        useRenderScale = true;
    }

    public static void resetRenderScale() {
        useRenderScale = false;
    }

    public static void blockVanillaInput(String category) {
        vanillainput.put(category, true);
    }

    public static void unblockVanillaInput(String category) {
        vanillainput.put(category, false);
    }

    public static boolean isVanillaInputBlocked() {
        return ignoreBlocked ? false : vanillainput.containsValue(true);
    }

    public static void ignoreBlockedVanillaInput(boolean ignore) {
        ignoreBlocked = ignore;
    }

    @SubscribeEvent
    public void onMouseClicked(ScreenEvent.MouseButtonPressed.Pre e) {
        int i = e.getButton();
        if (i == 0) {
            leftClicked = true;
        }
        if (i == 1) {
            rightClicked = true;
        }
        if (isVanillaInputBlocked()) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onMouseReleased(ScreenEvent.MouseButtonReleased.Pre e) {
        int i = e.getButton();
        if (i == 0) {
            leftClicked = false;
        }
        if (i == 1) {
            rightClicked = false;
        }
    }

    @SubscribeEvent
    public void onScreenInit(ScreenEvent.Init.Pre e) {
        leftClicked = false;
        rightClicked = false;
        vanillainput.clear();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (Minecraft.getInstance() != null && Minecraft.getInstance().screen == null) {
            vanillainput.clear();
        }
    }
}