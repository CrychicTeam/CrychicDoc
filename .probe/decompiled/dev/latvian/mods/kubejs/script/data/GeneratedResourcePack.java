package dev.latvian.mods.kubejs.script.data;

import dev.latvian.mods.kubejs.DevProperties;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.KubeJSPaths;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.Lazy;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class GeneratedResourcePack implements ExportablePackResources {

    private final PackType packType;

    private Map<ResourceLocation, GeneratedData> generated;

    private Set<String> generatedNamespaces;

    private static Stream<Path> tryWalk(Path path) {
        try {
            return Files.walk(path);
        } catch (Exception var2) {
            return Stream.empty();
        }
    }

    public static void scanForInvalidFiles(String pathName, Path path) throws IOException {
        for (Path p : Files.list(path).filter(x$0 -> Files.isDirectory(x$0, new LinkOption[0])).flatMap(GeneratedResourcePack::tryWalk).filter(x$0 -> Files.isRegularFile(x$0, new LinkOption[0])).filter(Files::isReadable).toList()) {
            try {
                String fileName = p.getFileName().toString();
                String fileNameLC = fileName.toLowerCase();
                if (!fileNameLC.endsWith(".zip") && !fileNameLC.equals(".ds_store") && !fileNameLC.equals("thumbs.db") && !fileNameLC.equals("desktop.ini")) {
                    if (Files.isHidden(path)) {
                        ConsoleJS.STARTUP.error("Invisible file found: " + pathName + path.relativize(p).toString().replace('\\', '/')).withExternalFile(p);
                    } else {
                        char[] chars = fileName.toCharArray();
                        for (char c : chars) {
                            if (c >= 'A' && c <= 'Z') {
                                ConsoleJS.STARTUP.error("Invalid file name: Uppercase '" + c + "' in " + pathName + path.relativize(p).toString().replace('\\', '/')).withExternalFile(p);
                                break;
                            }
                            if (c != '_' && c != '-' && (c < 'a' || c > 'z') && (c < '0' || c > '9') && c != '/' && c != '.') {
                                ConsoleJS.STARTUP.error("Invalid file name: Invalid character '" + c + "' in " + pathName + path.relativize(p).toString().replace('\\', '/')).withExternalFile(p);
                                break;
                            }
                        }
                    }
                }
            } catch (Exception var11) {
                ConsoleJS.STARTUP.error("Invalid file name: " + pathName + path.relativize(p).toString().replace('\\', '/'), var11).withExternalFile(p);
            }
        }
    }

    public GeneratedResourcePack(PackType t) {
        this.packType = t;
    }

    @Nullable
    public GeneratedData getRootResource(String... path) {
        String var2 = path.length == 1 ? path[0] : "";
        return switch(var2) {
            case "pack.mcmeta" ->
                GeneratedData.PACK_META;
            case "pack.png" ->
                GeneratedData.PACK_ICON;
            default ->
                null;
        };
    }

    public Map<ResourceLocation, GeneratedData> getGenerated() {
        if (this.generated == null) {
            this.generated = new HashMap();
            this.generate(this.generated);
            boolean debug = DevProperties.get().logGeneratedData || DevProperties.get().debugInfo;
            try {
                Path root = KubeJSPaths.get(this.packType);
                for (Path dir : Files.list(root).filter(x$0 -> Files.isDirectory(x$0, new LinkOption[0])).toList()) {
                    String ns = dir.getFileName().toString();
                    if (debug) {
                        KubeJS.LOGGER.info("# Walking namespace '" + ns + "'");
                    }
                    for (Path path : Files.walk(dir).filter(x$0 -> Files.isRegularFile(x$0, new LinkOption[0])).filter(Files::isReadable).toList()) {
                        String pathStr = dir.relativize(path).toString().replace('\\', '/').toLowerCase();
                        int sindex = pathStr.lastIndexOf(47);
                        String fileNameLC = sindex == -1 ? pathStr : pathStr.substring(sindex + 1);
                        if (!fileNameLC.endsWith(".zip") && !fileNameLC.equals(".ds_store") && !fileNameLC.equals("thumbs.db") && !fileNameLC.equals("desktop.ini") && !Files.isHidden(path)) {
                            GeneratedData data = new GeneratedData(new ResourceLocation(ns, pathStr), Lazy.of(() -> {
                                try {
                                    return Files.readAllBytes(path);
                                } catch (Exception var2x) {
                                    var2x.printStackTrace();
                                    return new byte[0];
                                }
                            }), this.forgetFile(pathStr));
                            if (debug) {
                                KubeJS.LOGGER.info("- File found: '" + data.id() + "' (" + data.data().get().length + " bytes)");
                            }
                            if (this.skipFile(data)) {
                                if (debug) {
                                    KubeJS.LOGGER.info("- Skipping '" + data.id() + "'");
                                }
                            } else {
                                this.generated.put(data.id(), data);
                            }
                        }
                    }
                }
            } catch (Exception var12) {
                KubeJS.LOGGER.error("Failed to load files from kubejs/" + this.packType.getDirectory(), var12);
            }
            this.generated.put(GeneratedData.INTERNAL_RELOAD.id(), GeneratedData.INTERNAL_RELOAD);
            this.generated = Map.copyOf(this.generated);
            if (debug) {
                KubeJS.LOGGER.info("Generated " + this.packType + " data (" + this.generated.size() + " files)");
            }
        }
        return this.generated;
    }

    protected boolean forgetFile(String path) {
        return true;
    }

    protected boolean skipFile(GeneratedData data) {
        return false;
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getResource(PackType type, ResourceLocation location) {
        GeneratedData r = type == this.packType ? (GeneratedData) this.getGenerated().get(location) : null;
        if (r == GeneratedData.INTERNAL_RELOAD) {
            this.close();
        }
        return r;
    }

    public void generate(Map<ResourceLocation, GeneratedData> map) {
    }

    @Override
    public void listResources(PackType type, String namespace, String path, PackResources.ResourceOutput visitor) {
        if (type == this.packType) {
            if (!path.endsWith("/")) {
                path = path + "/";
            }
            for (Entry<ResourceLocation, GeneratedData> r : this.getGenerated().entrySet()) {
                if (((ResourceLocation) r.getKey()).getNamespace().equals(namespace) && ((ResourceLocation) r.getKey()).getPath().startsWith(path)) {
                    visitor.accept((ResourceLocation) r.getKey(), (IoSupplier) r.getValue());
                }
            }
        }
    }

    @NotNull
    @Override
    public Set<String> getNamespaces(PackType type) {
        if (type != this.packType) {
            return Collections.emptySet();
        } else {
            if (this.generatedNamespaces == null) {
                this.generatedNamespaces = new HashSet();
                for (Entry<ResourceLocation, GeneratedData> s : this.getGenerated().entrySet()) {
                    this.generatedNamespaces.add(((ResourceLocation) s.getKey()).getNamespace());
                }
            }
            return this.generatedNamespaces;
        }
    }

    @Nullable
    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> serializer) throws IOException {
        GeneratedData inputSupplier = this.getRootResource("pack.mcmeta");
        if (inputSupplier != null) {
            InputStream input = inputSupplier.get();
            Object var4;
            try {
                var4 = AbstractPackResources.getMetadataFromStream(serializer, input);
            } catch (Throwable var7) {
                if (input != null) {
                    try {
                        input.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }
                throw var7;
            }
            if (input != null) {
                input.close();
            }
            return (T) var4;
        } else {
            return null;
        }
    }

    @NotNull
    @Override
    public String packId() {
        return "KubeJS Resource Pack [" + this.packType.getDirectory() + "]";
    }

    @Override
    public void close() {
        this.generated = null;
        this.generatedNamespaces = null;
    }

    @Override
    public boolean isBuiltin() {
        return true;
    }

    @Override
    public void export(Path root) throws IOException {
        for (Entry<ResourceLocation, GeneratedData> file : this.getGenerated().entrySet()) {
            Path path = root.resolve(this.packType.getDirectory() + "/" + ((ResourceLocation) file.getKey()).getNamespace() + "/" + ((ResourceLocation) file.getKey()).getPath());
            Files.createDirectories(path.getParent());
            Files.write(path, ((GeneratedData) file.getValue()).data().get(), new OpenOption[0]);
        }
        Files.write(root.resolve("pack.mcmeta"), GeneratedData.PACK_META.data().get(), new OpenOption[0]);
        Files.write(root.resolve("pack.png"), GeneratedData.PACK_ICON.data().get(), new OpenOption[0]);
    }
}