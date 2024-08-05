package dev.ftb.mods.ftblibrary.util;

import java.util.Map;
import net.minecraft.network.chat.Component;

public interface CustomComponentParser {

    Component parse(String var1, Map<String, String> var2);
}