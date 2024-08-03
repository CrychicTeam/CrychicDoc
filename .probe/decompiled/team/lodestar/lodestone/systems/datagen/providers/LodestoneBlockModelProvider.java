package team.lodestar.lodestone.systems.datagen.providers;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public final class LodestoneBlockModelProvider extends BlockModelProvider {

    final Function<ResourceLocation, LodestoneBlockModelProvider.LodestoneBlockModelBuilder> factory;

    public static final HashMap<String, ResourceLocation> BLOCK_TEXTURE_CACHE = new HashMap();

    public LodestoneBlockModelProvider(LodestoneBlockStateProvider provider, PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
        this.factory = l -> new LodestoneBlockModelProvider.LodestoneBlockModelBuilder(provider, l, existingFileHelper);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return super.m_213708_(cache);
    }

    @Override
    protected void registerModels() {
    }

    public BlockModelBuilder getBuilder(String path) {
        Preconditions.checkNotNull(path, "Path must not be null");
        ResourceLocation outputLoc = this.extendWithFolder(path.contains(":") ? new ResourceLocation(path) : new ResourceLocation(this.modid, path));
        this.existingFileHelper.trackGenerated(outputLoc, MODEL);
        return (BlockModelBuilder) this.generatedModels.computeIfAbsent(outputLoc, this.factory);
    }

    public BlockModelBuilder nested() {
        return (BlockModelBuilder) this.factory.apply(new ResourceLocation("dummy:dummy"));
    }

    @Override
    public ResourceLocation extendWithFolder(ResourceLocation rl) {
        return rl.getPath().contains("/") ? rl : new ResourceLocation(rl.getNamespace(), this.folder + "/" + rl.getPath());
    }

    private static class LodestoneBlockModelBuilder extends BlockModelBuilder {

        public final LodestoneBlockStateProvider provider;

        public LodestoneBlockModelBuilder(LodestoneBlockStateProvider provider, ResourceLocation outputLocation, ExistingFileHelper existingFileHelper) {
            super(outputLocation, existingFileHelper);
            this.provider = provider;
            LodestoneBlockModelProvider.BLOCK_TEXTURE_CACHE.clear();
        }

        public BlockModelBuilder texture(String key, ResourceLocation texture) {
            ResourceLocation actualLocation = texture;
            if (!texture.getNamespace().equals("minecraft") && !this.provider.staticTextures.contains(texture)) {
                String actualPath = texture.getPath().replace("block/", "block/" + LodestoneBlockStateProvider.getTexturePath());
                actualLocation = new ResourceLocation(texture.getNamespace(), actualPath);
            }
            LodestoneBlockModelProvider.BLOCK_TEXTURE_CACHE.put(key, actualLocation);
            return super.texture(key, actualLocation);
        }
    }
}