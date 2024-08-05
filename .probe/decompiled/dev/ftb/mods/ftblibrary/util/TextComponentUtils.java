package dev.ftb.mods.ftblibrary.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.ftb.mods.ftblibrary.util.forge.TextComponentUtilsImpl;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class TextComponentUtils {

    @ExpectPlatform
    @Transformed
    public static Component withLinks(String message) {
        return TextComponentUtilsImpl.withLinks(message);
    }

    public static Component hotkeyTooltip(String txt) {
        return Component.literal("[").withStyle(ChatFormatting.DARK_GRAY).append(Component.literal(txt).withStyle(ChatFormatting.GRAY)).append(Component.literal("]").withStyle(ChatFormatting.DARK_GRAY));
    }
}