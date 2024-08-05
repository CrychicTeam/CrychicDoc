package me.jellysquid.mods.sodium.client.compatibility.checks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.server.packs.metadata.MetadataSectionType;

public record SodiumResourcePackMetadata(List<String> ignoredShaders) {

    public static final Codec<SodiumResourcePackMetadata> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.STRING.listOf().fieldOf("ignored_shaders").forGetter(SodiumResourcePackMetadata::ignoredShaders)).apply(instance, SodiumResourcePackMetadata::new));

    public static final MetadataSectionType<SodiumResourcePackMetadata> SERIALIZER = MetadataSectionType.fromCodec("sodium", CODEC);
}