package net.minecraftforge.client.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.util.ExtraCodecs;

public record ForgeFaceData(int color, int blockLight, int skyLight, boolean ambientOcclusion, boolean calculateNormals) {

    public static final ForgeFaceData DEFAULT = new ForgeFaceData(-1, 0, 0, true, false);

    public static final Codec<Integer> COLOR = new ExtraCodecs.EitherCodec(Codec.INT, Codec.STRING).xmap(either -> (Integer) either.map(Function.identity(), str -> (int) Long.parseLong(str, 16)), color -> Either.right(Integer.toHexString(color)));

    public static final Codec<ForgeFaceData> CODEC = RecordCodecBuilder.create(builder -> builder.group(COLOR.optionalFieldOf("color", -1).forGetter(ForgeFaceData::color), Codec.intRange(0, 15).optionalFieldOf("block_light", 0).forGetter(ForgeFaceData::blockLight), Codec.intRange(0, 15).optionalFieldOf("sky_light", 0).forGetter(ForgeFaceData::skyLight), Codec.BOOL.optionalFieldOf("ambient_occlusion", true).forGetter(ForgeFaceData::ambientOcclusion), Codec.BOOL.optionalFieldOf("calculate_normals", false).forGetter(ForgeFaceData::calculateNormals)).apply(builder, ForgeFaceData::new));

    public ForgeFaceData(int color, int blockLight, int skyLight, boolean ambientOcclusion) {
        this(color, blockLight, skyLight, ambientOcclusion, false);
    }

    @Nullable
    public static ForgeFaceData read(@Nullable JsonElement obj, @Nullable ForgeFaceData fallback) throws JsonParseException {
        return obj == null ? fallback : (ForgeFaceData) CODEC.parse(JsonOps.INSTANCE, obj).getOrThrow(false, JsonParseException::new);
    }
}