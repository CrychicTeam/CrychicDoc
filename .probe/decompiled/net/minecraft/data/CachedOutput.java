package net.minecraft.data;

import com.google.common.hash.HashCode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

public interface CachedOutput {

    CachedOutput NO_CACHE = (p_236019_, p_236020_, p_236021_) -> {
        Files.createDirectories(p_236019_.getParent());
        Files.write(p_236019_, p_236020_, new OpenOption[0]);
    };

    void writeIfNeeded(Path var1, byte[] var2, HashCode var3) throws IOException;
}