package net.minecraft.client.renderer.texture.atlas.sources;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSourceType;
import net.minecraft.client.renderer.texture.atlas.SpriteSources;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class DirectoryLister implements SpriteSource {

    public static final Codec<DirectoryLister> CODEC = RecordCodecBuilder.create(p_262096_ -> p_262096_.group(Codec.STRING.fieldOf("source").forGetter(p_261592_ -> p_261592_.sourcePath), Codec.STRING.fieldOf("prefix").forGetter(p_262146_ -> p_262146_.idPrefix)).apply(p_262096_, DirectoryLister::new));

    private final String sourcePath;

    private final String idPrefix;

    public DirectoryLister(String string0, String string1) {
        this.sourcePath = string0;
        this.idPrefix = string1;
    }

    @Override
    public void run(ResourceManager resourceManager0, SpriteSource.Output spriteSourceOutput1) {
        FileToIdConverter $$2 = new FileToIdConverter("textures/" + this.sourcePath, ".png");
        $$2.listMatchingResources(resourceManager0).forEach((p_261906_, p_261635_) -> {
            ResourceLocation $$4 = $$2.fileToId(p_261906_).withPrefix(this.idPrefix);
            spriteSourceOutput1.add($$4, p_261635_);
        });
    }

    @Override
    public SpriteSourceType type() {
        return SpriteSources.DIRECTORY;
    }
}