package net.minecraft.client.resources;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.BuiltInMetadata;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.VanillaPackResources;
import net.minecraft.server.packs.VanillaPackResourcesBuilder;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;

public class ClientPackSource extends BuiltInPackSource {

    private static final PackMetadataSection VERSION_METADATA_SECTION = new PackMetadataSection(Component.translatable("resourcePack.vanilla.description"), SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES));

    private static final BuiltInMetadata BUILT_IN_METADATA = BuiltInMetadata.of(PackMetadataSection.TYPE, VERSION_METADATA_SECTION);

    private static final Component VANILLA_NAME = Component.translatable("resourcePack.vanilla.name");

    public static final String HIGH_CONTRAST_PACK = "high_contrast";

    private static final Map<String, Component> SPECIAL_PACK_NAMES = Map.of("programmer_art", Component.translatable("resourcePack.programmer_art.name"), "high_contrast", Component.translatable("resourcePack.high_contrast.name"));

    private static final ResourceLocation PACKS_DIR = new ResourceLocation("minecraft", "resourcepacks");

    @Nullable
    private final Path externalAssetDir;

    public ClientPackSource(Path path0) {
        super(PackType.CLIENT_RESOURCES, createVanillaPackSource(path0), PACKS_DIR);
        this.externalAssetDir = this.findExplodedAssetPacks(path0);
    }

    @Nullable
    private Path findExplodedAssetPacks(Path path0) {
        if (SharedConstants.IS_RUNNING_IN_IDE && path0.getFileSystem() == FileSystems.getDefault()) {
            Path $$1 = path0.getParent().resolve("resourcepacks");
            if (Files.isDirectory($$1, new LinkOption[0])) {
                return $$1;
            }
        }
        return null;
    }

    private static VanillaPackResources createVanillaPackSource(Path path0) {
        VanillaPackResourcesBuilder $$1 = new VanillaPackResourcesBuilder().setMetadata(BUILT_IN_METADATA).exposeNamespace("minecraft", "realms");
        return $$1.applyDevelopmentConfig().pushJarResources().pushAssetPath(PackType.CLIENT_RESOURCES, path0).build();
    }

    @Override
    protected Component getPackTitle(String string0) {
        Component $$1 = (Component) SPECIAL_PACK_NAMES.get(string0);
        return (Component) ($$1 != null ? $$1 : Component.literal(string0));
    }

    @Nullable
    @Override
    protected Pack createVanillaPack(PackResources packResources0) {
        return Pack.readMetaAndCreate("vanilla", VANILLA_NAME, true, p_247953_ -> packResources0, PackType.CLIENT_RESOURCES, Pack.Position.BOTTOM, PackSource.BUILT_IN);
    }

    @Nullable
    @Override
    protected Pack createBuiltinPack(String string0, Pack.ResourcesSupplier packResourcesSupplier1, Component component2) {
        return Pack.readMetaAndCreate(string0, component2, false, packResourcesSupplier1, PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.BUILT_IN);
    }

    @Override
    protected void populatePackList(BiConsumer<String, Function<String, Pack>> biConsumerStringFunctionStringPack0) {
        super.populatePackList(biConsumerStringFunctionStringPack0);
        if (this.externalAssetDir != null) {
            this.m_245111_(this.externalAssetDir, biConsumerStringFunctionStringPack0);
        }
    }
}