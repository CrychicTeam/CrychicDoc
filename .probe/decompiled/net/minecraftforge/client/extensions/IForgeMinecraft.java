package net.minecraftforge.client.extensions;

import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.ForgeHooksClient;

public interface IForgeMinecraft {

    private Minecraft self() {
        return (Minecraft) this;
    }

    default void pushGuiLayer(Screen screen) {
        ForgeHooksClient.pushGuiLayer(this.self(), screen);
    }

    default void popGuiLayer() {
        ForgeHooksClient.popGuiLayer(this.self());
    }

    default Locale getLocale() {
        return this.self().getLanguageManager().getJavaLocale();
    }
}