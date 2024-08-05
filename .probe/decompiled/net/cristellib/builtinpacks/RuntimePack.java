package net.cristellib.builtinpacks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.cristellib.CristelLib;
import net.cristellib.config.ConfigUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.FeatureFlagsMetadataSection;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.flag.FeatureFlags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RuntimePack implements PackResources {

    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    private final Lock waiting = new ReentrantLock();

    private final Map<ResourceLocation, Supplier<byte[]>> data = new ConcurrentHashMap();

    private final Map<List<String>, Supplier<byte[]>> root = new ConcurrentHashMap();

    public final int packVersion;

    private final String name;

    public RuntimePack(String name, int version, @Nullable Path imageFile) {
        this.packVersion = version;
        this.name = name;
        if (imageFile != null) {
            byte[] image = extractImageBytes(imageFile);
            if (image != null) {
                this.addRootResource("pack.png", image);
            }
        }
    }

    public byte[] addStructureSet(ResourceLocation identifier, JsonObject set) {
        return this.addDataForJsonLocation("worldgen/structure_set", identifier, set);
    }

    public byte[] addBiome(ResourceLocation identifier, JsonObject biome) {
        return this.addDataForJsonLocation("worldgen/biome", identifier, biome);
    }

    public byte[] addStructure(ResourceLocation identifier, JsonObject structure) {
        return this.addDataForJsonLocation("worldgen/structure", identifier, structure);
    }

    public byte[] addLootTable(ResourceLocation identifier, JsonObject table) {
        return this.addDataForJsonLocation("loot_tables", identifier, table);
    }

    @Nullable
    public byte[] addDataForJsonLocationFromPath(String prefix, ResourceLocation identifier, String fromSubPath, String fromModID) {
        return ConfigUtil.getElement(fromModID, fromSubPath) instanceof JsonObject object ? this.addDataForJsonLocation(prefix, identifier, object) : null;
    }

    public byte[] addDataForJsonLocation(String prefix, ResourceLocation identifier, JsonObject object) {
        return this.addAndSerializeDataForLocation(prefix, "json", identifier, object);
    }

    public byte[] addAndSerializeDataForLocation(String prefix, String end, ResourceLocation identifier, JsonObject object) {
        return this.addData(new ResourceLocation(identifier.getNamespace(), prefix + "/" + identifier.getPath() + "." + end), serializeJson(object));
    }

    public byte[] addData(ResourceLocation path, byte[] data) {
        this.data.put(path, (Supplier) () -> data);
        return data;
    }

    public void removeData(ResourceLocation path) {
        this.data.remove(path);
    }

    @Nullable
    public static byte[] extractImageBytes(Path imageName) {
        try {
            InputStream stream = Files.newInputStream(imageName.toAbsolutePath());
            return stream.readAllBytes();
        } catch (IOException var3) {
            CristelLib.LOGGER.warn("Couldn't get image for path: " + imageName, var3);
            return null;
        }
    }

    public static byte[] serializeJson(JsonObject object) {
        UnsafeByteArrayOutputStream ubaos = new UnsafeByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(ubaos, StandardCharsets.UTF_8);
        GSON.toJson(object, writer);
        try {
            writer.close();
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        }
        return ubaos.getBytes();
    }

    public byte[] addRootResource(String path, byte[] data) {
        this.root.put(Arrays.asList(path.split("/")), (Supplier) () -> data);
        return data;
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(@NotNull String... strings) {
        this.lock();
        Supplier<byte[]> supplier = (Supplier<byte[]>) this.root.get(Arrays.asList(strings));
        if (supplier == null) {
            this.waiting.unlock();
            return null;
        } else {
            this.waiting.unlock();
            return () -> new ByteArrayInputStream((byte[]) supplier.get());
        }
    }

    private void lock() {
        if (!this.waiting.tryLock()) {
            this.waiting.lock();
        }
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getResource(@NotNull PackType packType, @NotNull ResourceLocation id) {
        this.lock();
        Supplier<byte[]> supplier = (Supplier<byte[]>) this.data.get(id);
        if (supplier == null) {
            this.waiting.unlock();
            return null;
        } else {
            this.waiting.unlock();
            return () -> new ByteArrayInputStream((byte[]) supplier.get());
        }
    }

    @Override
    public void listResources(@NotNull PackType packType, @NotNull String namespace, @NotNull String prefix, @NotNull PackResources.ResourceOutput resourceOutput) {
        this.lock();
        for (ResourceLocation identifier : this.data.keySet()) {
            Supplier<byte[]> supplier = (Supplier<byte[]>) this.data.get(identifier);
            if (supplier == null) {
                this.waiting.unlock();
            } else if (identifier.getNamespace().equals(namespace) && identifier.getPath().contains(prefix + "/")) {
                IoSupplier<InputStream> inputSupplier = () -> new ByteArrayInputStream((byte[]) supplier.get());
                resourceOutput.accept(identifier, inputSupplier);
            }
        }
        this.waiting.unlock();
    }

    @NotNull
    @Override
    public Set<String> getNamespaces(@NotNull PackType packType) {
        this.lock();
        Set<String> namespaces = new HashSet();
        for (ResourceLocation identifier : this.data.keySet()) {
            namespaces.add(identifier.getNamespace());
        }
        this.waiting.unlock();
        return namespaces;
    }

    @Nullable
    @Override
    public <T> T getMetadataSection(@NotNull MetadataSectionSerializer<T> metadataSectionSerializer) {
        InputStream stream = null;
        try {
            IoSupplier<InputStream> supplier = this.getRootResource("pack.mcmeta");
            if (supplier != null) {
                stream = supplier.get();
            }
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        }
        if (stream != null) {
            return (T) FilePackResources.m_10214_(metadataSectionSerializer, stream);
        } else if (metadataSectionSerializer.getMetadataSectionName().equals("pack")) {
            JsonObject object = new JsonObject();
            object.addProperty("pack_format", this.packVersion);
            object.addProperty("description", this.name);
            return metadataSectionSerializer.fromJson(object);
        } else if (metadataSectionSerializer.getMetadataSectionName().equals("features")) {
            return metadataSectionSerializer.fromJson(FeatureFlagsMetadataSection.TYPE.toJson(new FeatureFlagsMetadataSection(FeatureFlags.DEFAULT_FLAGS)));
        } else {
            CristelLib.LOGGER.debug("'" + metadataSectionSerializer.getMetadataSectionName() + "' is an unsupported metadata key");
            return null;
        }
    }

    public boolean hasResource(ResourceLocation location) {
        return this.data.containsKey(location);
    }

    @Override
    public boolean isBuiltin() {
        return true;
    }

    @NotNull
    @Override
    public String packId() {
        return this.name;
    }

    @Override
    public void close() {
        CristelLib.LOGGER.debug("Closing RDP: " + this.name);
    }

    public void load(Path dir) throws IOException {
        Stream<Path> stream = Files.walk(dir);
        for (Path file : () -> stream.filter(x$0 -> Files.isRegularFile(x$0, new LinkOption[0])).map(dir::relativize).iterator()) {
            String s = file.toString();
            if (s.startsWith("data")) {
                String path = s.substring("data".length() + 1);
                this.load(path, this.data, Files.readAllBytes(file));
            } else if (!s.startsWith("assets")) {
                byte[] data = Files.readAllBytes(file);
                this.root.put(Arrays.asList(s.split("/")), (Supplier) () -> data);
            }
        }
    }

    public void load(ZipInputStream stream) throws IOException {
        ZipEntry entry;
        while ((entry = stream.getNextEntry()) != null) {
            String s = entry.toString();
            if (s.startsWith("data")) {
                String path = s.substring("data".length() + 1);
                this.load(path, this.data, this.read(entry, stream));
            } else if (!s.startsWith("assets")) {
                byte[] data = this.read(entry, stream);
                this.root.put(Arrays.asList(s.split("/")), (Supplier) () -> data);
            }
        }
    }

    protected byte[] read(ZipEntry entry, InputStream stream) throws IOException {
        byte[] data = new byte[Math.toIntExact(entry.getSize())];
        if (stream.read(data) != data.length) {
            throw new IOException("Zip stream was cut off! (maybe incorrect zip entry length? maybe u didn't flush your stream?)");
        } else {
            return data;
        }
    }

    protected void load(String fullPath, Map<ResourceLocation, Supplier<byte[]>> map, byte[] data) {
        int sep = fullPath.indexOf(47);
        String namespace = fullPath.substring(0, sep);
        String path = fullPath.substring(sep + 1);
        map.put(new ResourceLocation(namespace, path), (Supplier) () -> data);
    }

    @Nullable
    public JsonObject getResource(ResourceLocation location) {
        IoSupplier<InputStream> stream = this.getResource(PackType.SERVER_DATA, location);
        try {
            return GsonHelper.parse(new BufferedReader(new InputStreamReader(stream.get())));
        } catch (NullPointerException | IOException var5) {
            CristelLib.LOGGER.error("Couldn't get JsonObject from location: " + location, var5);
            return null;
        }
    }
}