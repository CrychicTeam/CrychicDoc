package net.minecraftforge.common.data;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import net.minecraft.client.resources.ClientPackSource;
import net.minecraft.client.resources.IndexedAssetSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.resource.ResourcePackLoader;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

public class ExistingFileHelper {

    private final MultiPackResourceManager clientResources;

    private final MultiPackResourceManager serverData;

    private final boolean enable;

    private final Multimap<PackType, ResourceLocation> generated = HashMultimap.create();

    public ExistingFileHelper(Collection<Path> existingPacks, Set<String> existingMods, boolean enable, @Nullable String assetIndex, @Nullable File assetsDir) {
        List<PackResources> candidateClientResources = new ArrayList();
        List<PackResources> candidateServerResources = new ArrayList();
        if (assetIndex != null && assetsDir != null && assetsDir.exists()) {
            candidateClientResources.add(ClientPackSource.createVanillaPackSource(IndexedAssetSource.createIndexFs(assetsDir.toPath(), assetIndex)));
        }
        candidateServerResources.add(ServerPacksSource.createVanillaPackSource());
        for (Path existing : existingPacks) {
            File file = existing.toFile();
            PackResources pack = (PackResources) (file.isDirectory() ? new PathPackResources(file.getName(), file.toPath(), false) : new FilePackResources(file.getName(), file, false));
            candidateClientResources.add(pack);
            candidateServerResources.add(pack);
        }
        for (String existingMod : existingMods) {
            IModFileInfo modFileInfo = ModList.get().getModFileById(existingMod);
            if (modFileInfo != null) {
                PackResources pack = ResourcePackLoader.createPackForMod(modFileInfo);
                candidateClientResources.add(pack);
                candidateServerResources.add(pack);
            }
        }
        this.clientResources = new MultiPackResourceManager(PackType.CLIENT_RESOURCES, candidateClientResources);
        this.serverData = new MultiPackResourceManager(PackType.SERVER_DATA, candidateServerResources);
        this.enable = enable;
    }

    private ResourceManager getManager(PackType packType) {
        return packType == PackType.CLIENT_RESOURCES ? this.clientResources : this.serverData;
    }

    private ResourceLocation getLocation(ResourceLocation base, String suffix, String prefix) {
        return new ResourceLocation(base.getNamespace(), prefix + "/" + base.getPath() + suffix);
    }

    public boolean exists(ResourceLocation loc, PackType packType) {
        return !this.enable ? true : this.generated.get(packType).contains(loc) || this.getManager(packType).m_213713_(loc).isPresent();
    }

    public boolean exists(ResourceLocation loc, ExistingFileHelper.IResourceType type) {
        return this.exists(this.getLocation(loc, type.getSuffix(), type.getPrefix()), type.getPackType());
    }

    public boolean exists(ResourceLocation loc, PackType packType, String pathSuffix, String pathPrefix) {
        return this.exists(this.getLocation(loc, pathSuffix, pathPrefix), packType);
    }

    public void trackGenerated(ResourceLocation loc, ExistingFileHelper.IResourceType type) {
        this.generated.put(type.getPackType(), this.getLocation(loc, type.getSuffix(), type.getPrefix()));
    }

    public void trackGenerated(ResourceLocation loc, PackType packType, String pathSuffix, String pathPrefix) {
        this.generated.put(packType, this.getLocation(loc, pathSuffix, pathPrefix));
    }

    @VisibleForTesting
    public Resource getResource(ResourceLocation loc, PackType packType, String pathSuffix, String pathPrefix) throws FileNotFoundException {
        return this.getResource(this.getLocation(loc, pathSuffix, pathPrefix), packType);
    }

    @VisibleForTesting
    public Resource getResource(ResourceLocation loc, PackType packType) throws FileNotFoundException {
        return this.getManager(packType).m_215593_(loc);
    }

    @VisibleForTesting
    public List<Resource> getResourceStack(ResourceLocation loc, PackType packType) {
        return this.getManager(packType).getResourceStack(loc);
    }

    public boolean isEnabled() {
        return this.enable;
    }

    public interface IResourceType {

        PackType getPackType();

        String getSuffix();

        String getPrefix();
    }

    public static class ResourceType implements ExistingFileHelper.IResourceType {

        final PackType packType;

        final String suffix;

        final String prefix;

        public ResourceType(PackType type, String suffix, String prefix) {
            this.packType = type;
            this.suffix = suffix;
            this.prefix = prefix;
        }

        @Override
        public PackType getPackType() {
            return this.packType;
        }

        @Override
        public String getSuffix() {
            return this.suffix;
        }

        @Override
        public String getPrefix() {
            return this.prefix;
        }
    }
}