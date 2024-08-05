package net.minecraft.client.renderer.texture.atlas.sources;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSourceType;
import net.minecraft.client.renderer.texture.atlas.SpriteSources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.ResourceLocationPattern;

public class SourceFilter implements SpriteSource {

    public static final Codec<SourceFilter> CODEC = RecordCodecBuilder.create(p_261830_ -> p_261830_.group(ResourceLocationPattern.CODEC.fieldOf("pattern").forGetter(p_262094_ -> p_262094_.filter)).apply(p_261830_, SourceFilter::new));

    private final ResourceLocationPattern filter;

    public SourceFilter(ResourceLocationPattern resourceLocationPattern0) {
        this.filter = resourceLocationPattern0;
    }

    @Override
    public void run(ResourceManager resourceManager0, SpriteSource.Output spriteSourceOutput1) {
        spriteSourceOutput1.removeAll(this.filter.locationPredicate());
    }

    @Override
    public SpriteSourceType type() {
        return SpriteSources.FILTER;
    }
}