package net.minecraftforge.client.textures;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ForgeTextureMetadata {

    public static final ForgeTextureMetadata EMPTY = new ForgeTextureMetadata(null);

    public static final MetadataSectionSerializer<ForgeTextureMetadata> SERIALIZER = new ForgeTextureMetadata.Serializer();

    @Nullable
    private final ITextureAtlasSpriteLoader loader;

    public static ForgeTextureMetadata forResource(Resource resource) throws IOException {
        Optional<ForgeTextureMetadata> metadata = resource.metadata().getSection(SERIALIZER);
        return metadata.isEmpty() ? EMPTY : (ForgeTextureMetadata) metadata.get();
    }

    public ForgeTextureMetadata(@Nullable ITextureAtlasSpriteLoader loader) {
        this.loader = loader;
    }

    @Nullable
    public ITextureAtlasSpriteLoader getLoader() {
        return this.loader;
    }

    private static final class Serializer implements MetadataSectionSerializer<ForgeTextureMetadata> {

        @NotNull
        @Override
        public String getMetadataSectionName() {
            return "forge";
        }

        @NotNull
        public ForgeTextureMetadata fromJson(JsonObject json) {
            ITextureAtlasSpriteLoader loader;
            if (json.has("loader")) {
                ResourceLocation loaderName = new ResourceLocation(GsonHelper.getAsString(json, "loader"));
                loader = TextureAtlasSpriteLoaderManager.get(loaderName);
                if (loader == null) {
                    throw new JsonSyntaxException("Unknown TextureAtlasSpriteLoader " + loaderName);
                }
            } else {
                loader = null;
            }
            return new ForgeTextureMetadata(loader);
        }
    }
}