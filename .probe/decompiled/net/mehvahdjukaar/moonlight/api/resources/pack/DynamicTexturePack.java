package net.mehvahdjukaar.moonlight.api.resources.pack;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.nio.file.Files;
import java.nio.file.Path;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.assets.LangBuilder;
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureImage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;

public class DynamicTexturePack extends DynamicResourcePack {

    public DynamicTexturePack(ResourceLocation name, Pack.Position position, boolean fixed, boolean hidden) {
        super(name, PackType.CLIENT_RESOURCES, position, fixed, hidden);
    }

    public DynamicTexturePack(ResourceLocation name) {
        super(name, PackType.CLIENT_RESOURCES);
    }

    void addPackLogo() {
        Path logoPath = ClientHelper.getModIcon(this.mainNamespace);
        if (logoPath != null) {
            try {
                this.addRootResource("pack.png", Files.readAllBytes(logoPath));
            } catch (Exception var3) {
            }
        }
    }

    public void addAndCloseTexture(ResourceLocation path, TextureImage image) {
        this.addAndCloseTexture(path, image, true);
    }

    public void addAndCloseTexture(ResourceLocation path, TextureImage image, boolean isOnAtlas) {
        try {
            TextureImage e = image;
            try {
                this.addBytes(path, image.getImage().asByteArray(), ResType.TEXTURES);
                if (!isOnAtlas) {
                    this.markNotClearable(ResType.TEXTURES.getPath(path));
                }
                JsonObject mcmeta = image.serializeMcMeta();
                if (mcmeta != null) {
                    this.addJson(path, mcmeta, ResType.MCMETA);
                }
            } catch (Throwable var8) {
                if (image != null) {
                    try {
                        e.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }
                throw var8;
            }
            if (image != null) {
                image.close();
            }
        } catch (Exception var9) {
            LOGGER.warn("Failed to add image {} to resource pack {}.", path, this, var9);
        }
    }

    public void addBlockModel(ResourceLocation modelLocation, JsonElement model) {
        this.addJson(modelLocation, model, ResType.BLOCK_MODELS);
    }

    public void addItemModel(ResourceLocation modelLocation, JsonElement model) {
        this.addJson(modelLocation, model, ResType.ITEM_MODELS);
    }

    public void addBlockState(ResourceLocation modelLocation, JsonElement model) {
        this.addJson(modelLocation, model, ResType.BLOCKSTATES);
    }

    public void addLang(ResourceLocation langName, JsonElement language) {
        this.addJson(langName, language, ResType.LANG);
    }

    public void addLang(ResourceLocation langName, LangBuilder builder) {
        this.addJson(langName, builder.build(), ResType.LANG);
    }
}