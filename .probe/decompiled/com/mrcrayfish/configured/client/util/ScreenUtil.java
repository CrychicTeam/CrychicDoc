package com.mrcrayfish.configured.client.util;

import com.mrcrayfish.configured.client.screen.ILabelProvider;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ScreenUtil {

    public static boolean isMouseWithin(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    public static Tooltip createTooltip(Screen screen, Component message, int maxWidth) {
        return Tooltip.create(message);
    }

    public static Tooltip createTooltip(Screen screen, Component message, int maxWidth, Predicate<Button> predicate) {
        return Tooltip.create(message);
    }

    public static void updateSearchTextFieldSuggestion(EditBox editBox, String value, List<? extends ILabelProvider> entries) {
        if (!value.isEmpty()) {
            Optional<? extends ILabelProvider> optional = entries.stream().filter(info -> info.getLabel().toLowerCase(Locale.ENGLISH).startsWith(value.toLowerCase(Locale.ENGLISH))).min(Comparator.comparing(ILabelProvider::getLabel));
            if (optional.isPresent()) {
                int length = value.length();
                String displayName = ((ILabelProvider) optional.get()).getLabel();
                editBox.setSuggestion(displayName.substring(length));
            } else {
                editBox.setSuggestion("");
            }
        } else {
            editBox.setSuggestion(Component.translatable("configured.gui.search").getString());
        }
    }

    public static Button button(int x, int y, int width, int height, Component label, Button.OnPress onPress) {
        return Button.builder(label, onPress).pos(x, y).size(width, height).build();
    }
}