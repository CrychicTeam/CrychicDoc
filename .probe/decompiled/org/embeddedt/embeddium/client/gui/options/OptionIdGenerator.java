package org.embeddedt.embeddium.client.gui.options;

import java.lang.StackWalker.StackFrame;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;

public class OptionIdGenerator {

    private static final List<String> BLACKLISTED_PREFIXES = List.of("me.jellysquid.mods.sodium", "org.embeddedt.embeddium", "net.minecraft", "net.neoforged");

    private static final Map<Path, String> MOD_ID_FROM_URL_CACHE = new HashMap();

    private static boolean isAllowedClass(String name) {
        for (String prefix : BLACKLISTED_PREFIXES) {
            if (name.startsWith(prefix)) {
                return false;
            }
        }
        return true;
    }

    private static Stream<Class<?>> streamFromName(String name) {
        try {
            return Stream.of(Class.forName(name));
        } catch (Throwable var2) {
            return Stream.empty();
        }
    }

    public static <T> OptionIdentifier<T> generateId(String path) {
        Optional<String> modId = (Optional<String>) StackWalker.getInstance().walk(frameStream -> frameStream.map(StackFrame::getClassName).filter(OptionIdGenerator::isAllowedClass).flatMap(OptionIdGenerator::streamFromName).map(clz -> {
            try {
                CodeSource source = clz.getProtectionDomain().getCodeSource();
                if (source != null) {
                    URL url = source.getLocation();
                    if (url != null) {
                        return (String) MOD_ID_FROM_URL_CACHE.get(Paths.get(url.toURI()));
                    }
                }
            } catch (URISyntaxException var3) {
            }
            return null;
        }).filter(Objects::nonNull).findFirst());
        return (OptionIdentifier<T>) modId.filter(id -> !id.equals("minecraft")).map(s -> OptionIdentifier.create(s, path)).orElse(null);
    }

    static {
        for (IModFileInfo info : ModList.get().getModFiles()) {
            if (!info.getMods().isEmpty()) {
                Path rootPath;
                try {
                    rootPath = info.getFile().findResource(new String[] { "/" });
                } catch (Throwable var4) {
                    continue;
                }
                MOD_ID_FROM_URL_CACHE.put(rootPath, ((IModInfo) info.getMods().get(0)).getModId());
            }
        }
    }
}