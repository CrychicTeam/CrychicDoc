package net.minecraft.world.level.validation;

import com.mojang.logging.LogUtils;
import java.io.BufferedReader;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;

public class PathAllowList implements PathMatcher {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String COMMENT_PREFIX = "#";

    private final List<PathAllowList.ConfigEntry> entries;

    private final Map<String, PathMatcher> compiledPaths = new ConcurrentHashMap();

    public PathAllowList(List<PathAllowList.ConfigEntry> listPathAllowListConfigEntry0) {
        this.entries = listPathAllowListConfigEntry0;
    }

    public PathMatcher getForFileSystem(FileSystem fileSystem0) {
        return (PathMatcher) this.compiledPaths.computeIfAbsent(fileSystem0.provider().getScheme(), p_289958_ -> {
            List<PathMatcher> $$2;
            try {
                $$2 = this.entries.stream().map(p_289937_ -> p_289937_.compile(fileSystem0)).toList();
            } catch (Exception var5) {
                LOGGER.error("Failed to compile file pattern list", var5);
                return p_289987_ -> false;
            }
            return switch($$2.size()) {
                case 0 ->
                    p_289982_ -> false;
                case 1 ->
                    (PathMatcher) $$2.get(0);
                default ->
                    p_289927_ -> {
                        for (PathMatcher $$2 : $$2) {
                            if ($$2.matches(p_289927_)) {
                                return true;
                            }
                        }
                        return false;
                    };
            };
        });
    }

    public boolean matches(Path path0) {
        return this.getForFileSystem(path0.getFileSystem()).matches(path0);
    }

    public static PathAllowList readPlain(BufferedReader bufferedReader0) {
        return new PathAllowList(bufferedReader0.lines().flatMap(p_289962_ -> PathAllowList.ConfigEntry.parse(p_289962_).stream()).toList());
    }

    public static record ConfigEntry(PathAllowList.EntryType f_289830_, String f_289839_) {

        private final PathAllowList.EntryType type;

        private final String pattern;

        public ConfigEntry(PathAllowList.EntryType f_289830_, String f_289839_) {
            this.type = f_289830_;
            this.pattern = f_289839_;
        }

        public PathMatcher compile(FileSystem p_289936_) {
            return this.type().compile(p_289936_, this.pattern);
        }

        static Optional<PathAllowList.ConfigEntry> parse(String p_289947_) {
            if (p_289947_.isBlank() || p_289947_.startsWith("#")) {
                return Optional.empty();
            } else if (!p_289947_.startsWith("[")) {
                return Optional.of(new PathAllowList.ConfigEntry(PathAllowList.EntryType.PREFIX, p_289947_));
            } else {
                int $$1 = p_289947_.indexOf(93, 1);
                if ($$1 == -1) {
                    throw new IllegalArgumentException("Unterminated type in line '" + p_289947_ + "'");
                } else {
                    String $$2 = p_289947_.substring(1, $$1);
                    String $$3 = p_289947_.substring($$1 + 1);
                    return switch($$2) {
                        case "glob", "regex" ->
                            Optional.of(new PathAllowList.ConfigEntry(PathAllowList.EntryType.FILESYSTEM, $$2 + ":" + $$3));
                        case "prefix" ->
                            Optional.of(new PathAllowList.ConfigEntry(PathAllowList.EntryType.PREFIX, $$3));
                        default ->
                            throw new IllegalArgumentException("Unsupported definition type in line '" + p_289947_ + "'");
                    };
                }
            }
        }

        static PathAllowList.ConfigEntry glob(String p_289983_) {
            return new PathAllowList.ConfigEntry(PathAllowList.EntryType.FILESYSTEM, "glob:" + p_289983_);
        }

        static PathAllowList.ConfigEntry regex(String p_289944_) {
            return new PathAllowList.ConfigEntry(PathAllowList.EntryType.FILESYSTEM, "regex:" + p_289944_);
        }

        static PathAllowList.ConfigEntry prefix(String p_289918_) {
            return new PathAllowList.ConfigEntry(PathAllowList.EntryType.PREFIX, p_289918_);
        }
    }

    @FunctionalInterface
    public interface EntryType {

        PathAllowList.EntryType FILESYSTEM = FileSystem::getPathMatcher;

        PathAllowList.EntryType PREFIX = (p_289949_, p_289938_) -> p_289955_ -> p_289955_.toString().startsWith(p_289938_);

        PathMatcher compile(FileSystem var1, String var2);
    }
}