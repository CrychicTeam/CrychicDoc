package fuzs.puzzleslib.api.resources.v1;

import fuzs.puzzleslib.api.core.v1.CommonAbstractions;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.BuiltInMetadata;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.world.flag.FeatureFlagSet;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class AbstractModPackResources implements PackResources {

    protected final String modLogoPath;

    private ResourceLocation id;

    private BuiltInMetadata metadata;

    private PackType packType;

    protected AbstractModPackResources() {
        this("mod_logo.png");
    }

    protected AbstractModPackResources(String modLogoPath) {
        Objects.requireNonNull(modLogoPath, "mod logo path is null");
        this.modLogoPath = modLogoPath;
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(String... elements) {
        String path = String.join("/", elements);
        return "pack.png".equals(path) ? (IoSupplier) ModLoaderEnvironment.INSTANCE.getModContainer(this.getNamespace()).flatMap(container -> container.findResource(this.modLogoPath)).map(modResource -> () -> Files.newInputStream(modResource)).orElse(null) : null;
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getResource(PackType packType, ResourceLocation location) {
        return null;
    }

    @Override
    public void listResources(PackType packType, String namespace, String path, PackResources.ResourceOutput resourceOutput) {
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        Objects.requireNonNull(this.packType, "pack type is null");
        return this.packType == type ? Collections.singleton(this.getNamespace()) : Collections.emptySet();
    }

    @Nullable
    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> deserializer) {
        Objects.requireNonNull(this.metadata, "metadata is null");
        return this.metadata.get(deserializer);
    }

    @Override
    public String packId() {
        return this.id.toString();
    }

    @Override
    public boolean isBuiltin() {
        return true;
    }

    @Override
    public void close() {
    }

    protected final String getNamespace() {
        Objects.requireNonNull(this.id, "id is null");
        return this.id.getNamespace();
    }

    @Internal
    void setup() {
    }

    @Internal
    static Pack buildPack(PackType packType, ResourceLocation id, Supplier<AbstractModPackResources> factory, Component title, Component description, boolean required, boolean fixedPosition, boolean hidden, FeatureFlagSet features) {
        PackMetadataSection metadataSection = new PackMetadataSection(description, SharedConstants.getCurrentVersion().getPackVersion(packType));
        BuiltInMetadata metadata = BuiltInMetadata.of(PackMetadataSection.TYPE, metadataSection);
        Pack.Info info = CommonAbstractions.INSTANCE.createPackInfo(id, description, metadataSection.getPackFormat(), features, hidden);
        return Pack.create(id.toString(), title, required, $ -> {
            AbstractModPackResources packResources = (AbstractModPackResources) factory.get();
            packResources.id = id;
            packResources.metadata = metadata;
            packResources.packType = packType;
            packResources.setup();
            return packResources;
        }, info, packType, Pack.Position.TOP, fixedPosition, PackSource.BUILT_IN);
    }
}