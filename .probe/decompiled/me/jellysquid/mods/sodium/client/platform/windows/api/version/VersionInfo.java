package me.jellysquid.mods.sodium.client.platform.windows.api.version;

import java.io.Closeable;
import java.nio.ByteBuffer;
import java.util.Locale;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.MemoryUtil;

public class VersionInfo implements Closeable {

    private final ByteBuffer pBlock;

    VersionInfo(ByteBuffer buffer) {
        this.pBlock = buffer;
    }

    public static VersionInfo allocate(int len) {
        return new VersionInfo(MemoryUtil.memAlignedAlloc(16, len));
    }

    @Nullable
    public String queryValue(String key, LanguageCodePage translation) {
        QueryResult result = Version.query(this.pBlock, getStringFileInfoPath(key, translation));
        return result == null ? null : MemoryUtil.memUTF16(result.address());
    }

    @Nullable
    public LanguageCodePage queryEnglishTranslation() {
        QueryResult result = Version.query(this.pBlock, "\\VarFileInfo\\Translation");
        return result == null ? null : findEnglishTranslationEntry(result);
    }

    @Nullable
    private static LanguageCodePage findEnglishTranslationEntry(QueryResult result) {
        LanguageCodePage translation = null;
        int offset = 0;
        while (offset < result.length()) {
            translation = LanguageCodePage.decode(result.address() + (long) offset);
            offset += 4;
            if (translation.codePage() == 1200 && translation.languageId() == 1033) {
                return translation;
            }
        }
        return translation;
    }

    private static String getStringFileInfoPath(String key, LanguageCodePage translation) {
        return String.format(Locale.ROOT, "\\StringFileInfo\\%04x%04x\\%s", translation.languageId(), translation.codePage(), key);
    }

    public void close() {
        MemoryUtil.memAlignedFree(this.pBlock);
    }

    long address() {
        return MemoryUtil.memAddress(this.pBlock);
    }
}