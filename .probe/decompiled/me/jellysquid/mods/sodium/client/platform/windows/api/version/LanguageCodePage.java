package me.jellysquid.mods.sodium.client.platform.windows.api.version;

import org.lwjgl.system.MemoryUtil;

public record LanguageCodePage(int languageId, int codePage) {

    static final int STRIDE = 4;

    static LanguageCodePage decode(long address) {
        int value = MemoryUtil.memGetInt(address);
        int languageId = value & 65535;
        int codePage = (value & -65536) >> 16;
        return new LanguageCodePage(languageId, codePage);
    }
}