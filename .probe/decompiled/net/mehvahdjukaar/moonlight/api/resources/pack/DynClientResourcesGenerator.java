package net.mehvahdjukaar.moonlight.api.resources.pack;

import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.events.AfterLanguageLoadEvent;
import net.mehvahdjukaar.moonlight.api.events.MoonlightEventsHelper;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureImage;
import net.mehvahdjukaar.moonlight.core.MoonlightClient;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.resources.ResourceManager;

public abstract class DynClientResourcesGenerator extends DynResourceGenerator<DynamicTexturePack> {

    private static Integer index = 0;

    protected DynClientResourcesGenerator(DynamicTexturePack pack) {
        super(MoonlightClient.maybeMergePack(pack), pack.mainNamespace);
    }

    @Override
    public void register() {
        super.register();
        if (!PlatHelper.isData()) {
            Supplier var10000 = () -> this;
            String var10003 = this.modId;
            Integer var1 = index;
            index = index + 1;
            ClientHelper.addClientReloadListener(var10000, new ResourceLocation(var10003, "dyn_resources_generator_" + var1));
        }
        MoonlightEventsHelper.addListener(this::addDynamicTranslations, AfterLanguageLoadEvent.class);
    }

    @Override
    protected PackRepository getRepository() {
        return Minecraft.getInstance().getResourcePackRepository();
    }

    public boolean alreadyHasTextureAtLocation(ResourceManager manager, ResourceLocation res) {
        return this.alreadyHasAssetAtLocation(manager, res, ResType.TEXTURES);
    }

    public void addTextureIfNotPresent(ResourceManager manager, String relativePath, Supplier<TextureImage> textureSupplier) {
        this.addTextureIfNotPresent(manager, relativePath, textureSupplier, true);
    }

    public void addTextureIfNotPresent(ResourceManager manager, String relativePath, Supplier<TextureImage> textureSupplier, boolean isOnAtlas) {
        ResourceLocation res = relativePath.contains(":") ? new ResourceLocation(relativePath) : new ResourceLocation(this.modId, relativePath);
        if (!this.alreadyHasTextureAtLocation(manager, res)) {
            TextureImage textureImage = null;
            try {
                textureImage = (TextureImage) textureSupplier.get();
            } catch (Exception var8) {
                this.getLogger().error("Failed to generate texture {}: {}", res, var8);
            }
            if (textureImage == null) {
                this.getLogger().warn("Could not generate texture {}", res);
            } else {
                this.dynamicPack.addAndCloseTexture(res, textureImage, isOnAtlas);
            }
        }
    }

    public void addDynamicTranslations(AfterLanguageLoadEvent languageEvent) {
    }
}