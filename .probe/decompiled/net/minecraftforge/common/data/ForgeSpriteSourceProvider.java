package net.minecraftforge.common.data;

import java.util.Optional;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

public class ForgeSpriteSourceProvider extends SpriteSourceProvider {

    public ForgeSpriteSourceProvider(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, fileHelper, "forge");
    }

    @Override
    protected void addSources() {
        this.atlas(SpriteSourceProvider.BLOCKS_ATLAS).addSource(new SingleFile(new ResourceLocation("forge:white"), Optional.empty()));
    }
}