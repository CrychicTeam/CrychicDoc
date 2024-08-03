package net.minecraft.client.renderer.texture.atlas.sources;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSourceType;
import net.minecraft.client.renderer.texture.atlas.SpriteSources;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.Logger;

public class SingleFile implements SpriteSource {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Codec<SingleFile> CODEC = RecordCodecBuilder.create(p_261903_ -> p_261903_.group(ResourceLocation.CODEC.fieldOf("resource").forGetter(p_261913_ -> p_261913_.resourceId), ResourceLocation.CODEC.optionalFieldOf("sprite").forGetter(p_261615_ -> p_261615_.spriteId)).apply(p_261903_, SingleFile::new));

    private final ResourceLocation resourceId;

    private final Optional<ResourceLocation> spriteId;

    public SingleFile(ResourceLocation resourceLocation0, Optional<ResourceLocation> optionalResourceLocation1) {
        this.resourceId = resourceLocation0;
        this.spriteId = optionalResourceLocation1;
    }

    @Override
    public void run(ResourceManager resourceManager0, SpriteSource.Output spriteSourceOutput1) {
        ResourceLocation $$2 = f_266012_.idToFile(this.resourceId);
        Optional<Resource> $$3 = resourceManager0.m_213713_($$2);
        if ($$3.isPresent()) {
            spriteSourceOutput1.add((ResourceLocation) this.spriteId.orElse(this.resourceId), (Resource) $$3.get());
        } else {
            LOGGER.warn("Missing sprite: {}", $$2);
        }
    }

    @Override
    public SpriteSourceType type() {
        return SpriteSources.SINGLE_FILE;
    }
}