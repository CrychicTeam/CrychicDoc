package dev.ftb.mods.ftbquests.util;

import com.google.gson.JsonParseException;
import dev.ftb.mods.ftblibrary.util.client.ClientTextComponentUtils;
import java.util.regex.Pattern;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class TextUtils {

    private static final Pattern JSON_TEXT_PATTERN = Pattern.compile("^[{\\[]\\s*\"");

    public static Component parseRawText(String str) {
        return JSON_TEXT_PATTERN.matcher(str).find() ? deserializeRawJsonText(str) : ClientTextComponentUtils.parse(str);
    }

    private static Component deserializeRawJsonText(String raw) {
        try {
            return Component.Serializer.fromJson(raw);
        } catch (JsonParseException var2) {
            return Component.literal("ERROR: " + var2.getMessage()).withStyle(ChatFormatting.RED);
        }
    }
}