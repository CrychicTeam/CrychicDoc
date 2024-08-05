package icyllis.modernui.mc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class OptiFineIntegration {

    private static Field of_fast_render;

    private static Field shaderPackLoaded;

    private OptiFineIntegration() {
    }

    public static void openShadersGui() {
        Minecraft minecraft = Minecraft.getInstance();
        try {
            Class<?> clazz = Class.forName("net.optifine.shaders.gui.GuiShaders");
            Constructor<?> constructor = clazz.getConstructor(Screen.class, Options.class);
            minecraft.setScreen((Screen) constructor.newInstance(minecraft.screen, minecraft.options));
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    public static void setFastRender(boolean fastRender) {
        Minecraft minecraft = Minecraft.getInstance();
        if (of_fast_render != null) {
            try {
                of_fast_render.setBoolean(minecraft.options, fastRender);
                minecraft.options.save();
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }
    }

    public static void setGuiScale(OptionInstance<Integer> option) {
        Minecraft minecraft = Minecraft.getInstance();
        try {
            Field field = Options.class.getDeclaredField("GUI_SCALE");
            field.setAccessible(true);
            field.set(minecraft.options, option);
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    public static boolean isShaderPackLoaded() {
        if (shaderPackLoaded != null) {
            try {
                return shaderPackLoaded.getBoolean(null);
            } catch (Exception var1) {
                var1.printStackTrace();
            }
        }
        return false;
    }

    static {
        try {
            of_fast_render = Options.class.getDeclaredField("ofFastRender");
        } catch (Exception var2) {
            var2.printStackTrace();
        }
        try {
            Class<?> clazz = Class.forName("net.optifine.shaders.Shaders");
            shaderPackLoaded = clazz.getDeclaredField("shaderPackLoaded");
        } catch (Exception var1) {
            var1.printStackTrace();
        }
    }
}