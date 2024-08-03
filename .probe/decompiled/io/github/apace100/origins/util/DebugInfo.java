package io.github.apace100.origins.util;

import io.github.apace100.origins.Origins;
import io.github.edwinmindcraft.apoli.api.ApoliAPI;
import io.github.edwinmindcraft.origins.api.OriginsAPI;

public class DebugInfo {

    public static void printRegistrySizes(String at) {
        printInfo(new String[] { "Registry Size at " + at, "Origins: " + OriginsAPI.getOriginsRegistry().keySet().size(), "Layers:  " + OriginsAPI.getLayersRegistry().keySet().size(), "Powers:  " + ApoliAPI.getPowers().keySet().size() });
    }

    private static void printInfo(String[] lines) {
        int longest = 0;
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].length() > longest) {
                longest = lines[i].length();
            }
            lines[i] = "| " + lines[i];
        }
        String border = "+" + "-".repeat(Math.max(0, longest + 2)) + "+";
        Origins.LOGGER.info(border);
        for (int i = 0; i < lines.length; i++) {
            while (lines[i].length() < longest + 3) {
                lines[i] = lines[i] + " ";
            }
            lines[i] = lines[i] + "|";
            Origins.LOGGER.info(lines[i]);
        }
        Origins.LOGGER.info(border);
    }
}