package net.minecraft.world.level.levelgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class GeodeBlockSettings {

    public final BlockStateProvider fillingProvider;

    public final BlockStateProvider innerLayerProvider;

    public final BlockStateProvider alternateInnerLayerProvider;

    public final BlockStateProvider middleLayerProvider;

    public final BlockStateProvider outerLayerProvider;

    public final List<BlockState> innerPlacements;

    public final TagKey<Block> cannotReplace;

    public final TagKey<Block> invalidBlocks;

    public static final Codec<GeodeBlockSettings> CODEC = RecordCodecBuilder.create(p_158307_ -> p_158307_.group(BlockStateProvider.CODEC.fieldOf("filling_provider").forGetter(p_158323_ -> p_158323_.fillingProvider), BlockStateProvider.CODEC.fieldOf("inner_layer_provider").forGetter(p_158321_ -> p_158321_.innerLayerProvider), BlockStateProvider.CODEC.fieldOf("alternate_inner_layer_provider").forGetter(p_158319_ -> p_158319_.alternateInnerLayerProvider), BlockStateProvider.CODEC.fieldOf("middle_layer_provider").forGetter(p_158317_ -> p_158317_.middleLayerProvider), BlockStateProvider.CODEC.fieldOf("outer_layer_provider").forGetter(p_158315_ -> p_158315_.outerLayerProvider), ExtraCodecs.nonEmptyList(BlockState.CODEC.listOf()).fieldOf("inner_placements").forGetter(p_158313_ -> p_158313_.innerPlacements), TagKey.hashedCodec(Registries.BLOCK).fieldOf("cannot_replace").forGetter(p_204566_ -> p_204566_.cannotReplace), TagKey.hashedCodec(Registries.BLOCK).fieldOf("invalid_blocks").forGetter(p_204564_ -> p_204564_.invalidBlocks)).apply(p_158307_, GeodeBlockSettings::new));

    public GeodeBlockSettings(BlockStateProvider blockStateProvider0, BlockStateProvider blockStateProvider1, BlockStateProvider blockStateProvider2, BlockStateProvider blockStateProvider3, BlockStateProvider blockStateProvider4, List<BlockState> listBlockState5, TagKey<Block> tagKeyBlock6, TagKey<Block> tagKeyBlock7) {
        this.fillingProvider = blockStateProvider0;
        this.innerLayerProvider = blockStateProvider1;
        this.alternateInnerLayerProvider = blockStateProvider2;
        this.middleLayerProvider = blockStateProvider3;
        this.outerLayerProvider = blockStateProvider4;
        this.innerPlacements = listBlockState5;
        this.cannotReplace = tagKeyBlock6;
        this.invalidBlocks = tagKeyBlock7;
    }
}