package net.minecraft.world.level.levelgen.feature.rootplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record MangroveRootPlacement(HolderSet<Block> f_225773_, HolderSet<Block> f_225774_, BlockStateProvider f_225775_, int f_225776_, int f_225777_, float f_225778_) {

    private final HolderSet<Block> canGrowThrough;

    private final HolderSet<Block> muddyRootsIn;

    private final BlockStateProvider muddyRootsProvider;

    private final int maxRootWidth;

    private final int maxRootLength;

    private final float randomSkewChance;

    public static final Codec<MangroveRootPlacement> CODEC = RecordCodecBuilder.create(p_225789_ -> p_225789_.group(RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("can_grow_through").forGetter(p_225808_ -> p_225808_.canGrowThrough), RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("muddy_roots_in").forGetter(p_225803_ -> p_225803_.muddyRootsIn), BlockStateProvider.CODEC.fieldOf("muddy_roots_provider").forGetter(p_225800_ -> p_225800_.muddyRootsProvider), Codec.intRange(1, 12).fieldOf("max_root_width").forGetter(p_225797_ -> p_225797_.maxRootWidth), Codec.intRange(1, 64).fieldOf("max_root_length").forGetter(p_225794_ -> p_225794_.maxRootLength), Codec.floatRange(0.0F, 1.0F).fieldOf("random_skew_chance").forGetter(p_225791_ -> p_225791_.randomSkewChance)).apply(p_225789_, MangroveRootPlacement::new));

    public MangroveRootPlacement(HolderSet<Block> f_225773_, HolderSet<Block> f_225774_, BlockStateProvider f_225775_, int f_225776_, int f_225777_, float f_225778_) {
        this.canGrowThrough = f_225773_;
        this.muddyRootsIn = f_225774_;
        this.muddyRootsProvider = f_225775_;
        this.maxRootWidth = f_225776_;
        this.maxRootLength = f_225777_;
        this.randomSkewChance = f_225778_;
    }
}