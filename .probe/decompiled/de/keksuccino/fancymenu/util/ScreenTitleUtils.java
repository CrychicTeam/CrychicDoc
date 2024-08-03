package de.keksuccino.fancymenu.util;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScreenTitleUtils {

    public static Component getTitleOfScreen(@NotNull Screen screen) {
        Component c = screen.getTitle();
        return (Component) (c == null ? Component.empty() : c);
    }

    @Nullable
    public static String getTitleLocalizationKeyOfScreen(@NotNull Screen screen) {
        Component title = getTitleOfScreen(screen);
        return title instanceof MutableComponent && title.getContents() instanceof TranslatableContents t ? t.getKey() : null;
    }

    public static void setScreenTitle(@NotNull Screen screen, @NotNull Component title) {
        screen.title = title;
    }
}