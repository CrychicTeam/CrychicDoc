package net.minecraft.server.packs.resources;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.util.ResourceLocationPattern;

public class ResourceFilterSection {

    private static final Codec<ResourceFilterSection> CODEC = RecordCodecBuilder.create(p_261431_ -> p_261431_.group(Codec.list(ResourceLocationPattern.CODEC).fieldOf("block").forGetter(p_215520_ -> p_215520_.blockList)).apply(p_261431_, ResourceFilterSection::new));

    public static final MetadataSectionType<ResourceFilterSection> TYPE = MetadataSectionType.fromCodec("filter", CODEC);

    private final List<ResourceLocationPattern> blockList;

    public ResourceFilterSection(List<ResourceLocationPattern> listResourceLocationPattern0) {
        this.blockList = List.copyOf(listResourceLocationPattern0);
    }

    public boolean isNamespaceFiltered(String string0) {
        return this.blockList.stream().anyMatch(p_261433_ -> p_261433_.namespacePredicate().test(string0));
    }

    public boolean isPathFiltered(String string0) {
        return this.blockList.stream().anyMatch(p_261430_ -> p_261430_.pathPredicate().test(string0));
    }
}