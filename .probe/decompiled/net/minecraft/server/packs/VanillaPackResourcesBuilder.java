package net.minecraft.server.packs;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.Util;
import org.slf4j.Logger;

public class VanillaPackResourcesBuilder {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static Consumer<VanillaPackResourcesBuilder> developmentConfig = p_251787_ -> {
    };

    private static final Map<PackType, Path> ROOT_DIR_BY_TYPE = Util.make(() -> {
        synchronized (VanillaPackResources.class) {
            Builder<PackType, Path> $$0 = ImmutableMap.builder();
            for (PackType $$1 : PackType.values()) {
                String $$2 = "/" + $$1.getDirectory() + "/.mcassetsroot";
                URL $$3 = VanillaPackResources.class.getResource($$2);
                if ($$3 == null) {
                    LOGGER.error("File {} does not exist in classpath", $$2);
                } else {
                    try {
                        URI $$4 = $$3.toURI();
                        String $$5 = $$4.getScheme();
                        if (!"jar".equals($$5) && !"file".equals($$5)) {
                            LOGGER.warn("Assets URL '{}' uses unexpected schema", $$4);
                        }
                        Path $$6 = safeGetPath($$4);
                        $$0.put($$1, $$6.getParent());
                    } catch (Exception var12) {
                        LOGGER.error("Couldn't resolve path to vanilla assets", var12);
                    }
                }
            }
            return $$0.build();
        }
    });

    private final Set<Path> rootPaths = new LinkedHashSet();

    private final Map<PackType, Set<Path>> pathsForType = new EnumMap(PackType.class);

    private BuiltInMetadata metadata = BuiltInMetadata.of();

    private final Set<String> namespaces = new HashSet();

    private static Path safeGetPath(URI uRI0) throws IOException {
        try {
            return Paths.get(uRI0);
        } catch (FileSystemNotFoundException var3) {
        } catch (Throwable var4) {
            LOGGER.warn("Unable to get path for: {}", uRI0, var4);
        }
        try {
            FileSystems.newFileSystem(uRI0, Collections.emptyMap());
        } catch (FileSystemAlreadyExistsException var2) {
        }
        return Paths.get(uRI0);
    }

    private boolean validateDirPath(Path path0) {
        if (!Files.exists(path0, new LinkOption[0])) {
            return false;
        } else if (!Files.isDirectory(path0, new LinkOption[0])) {
            throw new IllegalArgumentException("Path " + path0.toAbsolutePath() + " is not directory");
        } else {
            return true;
        }
    }

    private void pushRootPath(Path path0) {
        if (this.validateDirPath(path0)) {
            this.rootPaths.add(path0);
        }
    }

    private void pushPathForType(PackType packType0, Path path1) {
        if (this.validateDirPath(path1)) {
            ((Set) this.pathsForType.computeIfAbsent(packType0, p_250639_ -> new LinkedHashSet())).add(path1);
        }
    }

    public VanillaPackResourcesBuilder pushJarResources() {
        ROOT_DIR_BY_TYPE.forEach((p_251514_, p_251979_) -> {
            this.pushRootPath(p_251979_.getParent());
            this.pushPathForType(p_251514_, p_251979_);
        });
        return this;
    }

    public VanillaPackResourcesBuilder pushClasspathResources(PackType packType0, Class<?> class1) {
        Enumeration<URL> $$2 = null;
        try {
            $$2 = class1.getClassLoader().getResources(packType0.getDirectory() + "/");
        } catch (IOException var8) {
        }
        while ($$2 != null && $$2.hasMoreElements()) {
            URL $$3 = (URL) $$2.nextElement();
            try {
                URI $$4 = $$3.toURI();
                if ("file".equals($$4.getScheme())) {
                    Path $$5 = Paths.get($$4);
                    this.pushRootPath($$5.getParent());
                    this.pushPathForType(packType0, $$5);
                }
            } catch (Exception var7) {
                LOGGER.error("Failed to extract path from {}", $$3, var7);
            }
        }
        return this;
    }

    public VanillaPackResourcesBuilder applyDevelopmentConfig() {
        developmentConfig.accept(this);
        return this;
    }

    public VanillaPackResourcesBuilder pushUniversalPath(Path path0) {
        this.pushRootPath(path0);
        for (PackType $$1 : PackType.values()) {
            this.pushPathForType($$1, path0.resolve($$1.getDirectory()));
        }
        return this;
    }

    public VanillaPackResourcesBuilder pushAssetPath(PackType packType0, Path path1) {
        this.pushRootPath(path1);
        this.pushPathForType(packType0, path1);
        return this;
    }

    public VanillaPackResourcesBuilder setMetadata(BuiltInMetadata builtInMetadata0) {
        this.metadata = builtInMetadata0;
        return this;
    }

    public VanillaPackResourcesBuilder exposeNamespace(String... string0) {
        this.namespaces.addAll(Arrays.asList(string0));
        return this;
    }

    public VanillaPackResources build() {
        Map<PackType, List<Path>> $$0 = new EnumMap(PackType.class);
        for (PackType $$1 : PackType.values()) {
            List<Path> $$2 = copyAndReverse((Collection<Path>) this.pathsForType.getOrDefault($$1, Set.of()));
            $$0.put($$1, $$2);
        }
        return new VanillaPackResources(this.metadata, Set.copyOf(this.namespaces), copyAndReverse(this.rootPaths), $$0);
    }

    private static List<Path> copyAndReverse(Collection<Path> collectionPath0) {
        List<Path> $$1 = new ArrayList(collectionPath0);
        Collections.reverse($$1);
        return List.copyOf($$1);
    }
}