package net.mehvahdjukaar.amendments.integration.forge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.blueprint.common.world.modification.structure.StructureRepaletter;
import com.teamabnormals.blueprint.common.world.modification.structure.StructureRepalleterManager;
import net.mehvahdjukaar.amendments.Amendments;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlueprintIntegration {

    public static void init() {
        StructureRepalleterManager.registerSerializer(Amendments.res("blockstate_replace"), BlueprintIntegration.BlockStateRepaletter.CODEC);
    }

    public static record BlockStateRepaletter(Block replacesBlock, BlockState replacesWith, float chance) implements StructureRepaletter {

        public static final Codec<BlueprintIntegration.BlockStateRepaletter> CODEC = RecordCodecBuilder.create(i -> i.group(BuiltInRegistries.BLOCK.m_194605_().fieldOf("replaces_block").forGetter(BlueprintIntegration.BlockStateRepaletter::replacesBlock), BlockState.CODEC.fieldOf("replaces_with").forGetter(BlueprintIntegration.BlockStateRepaletter::replacesWith), Codec.FLOAT.optionalFieldOf("chance", 1.0F).forGetter(BlueprintIntegration.BlockStateRepaletter::chance)).apply(i, BlueprintIntegration.BlockStateRepaletter::new));

        @Nullable
        public BlockState getReplacement(ServerLevelAccessor serverLevelAccessor, BlockState state, RandomSource randomSource) {
            return state.m_60713_(this.replacesBlock) && randomSource.nextFloat() < this.chance ? this.replacesWith : null;
        }

        public Codec<? extends StructureRepaletter> codec() {
            return CODEC;
        }
    }
}