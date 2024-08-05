package io.github.edwinmindcraft.origins.common.power.configuration;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import java.util.Optional;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public record NoSlowdownConfiguration(@Nullable TagKey<Block> blocks) implements IDynamicFeatureConfiguration {

    public static final Codec<NoSlowdownConfiguration> CODEC = TagKey.hashedCodec(Registries.BLOCK).optionalFieldOf("tag").xmap(x -> new NoSlowdownConfiguration((TagKey<Block>) x.orElse(null)), x -> Optional.ofNullable(x.blocks())).codec();

    public boolean test(BlockState state) {
        return this.blocks() == null || state.m_204336_(this.blocks());
    }
}