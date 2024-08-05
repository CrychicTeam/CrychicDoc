package net.minecraft.world.level.levelgen.feature.stateproviders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Collection;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class RandomizedIntStateProvider extends BlockStateProvider {

    public static final Codec<RandomizedIntStateProvider> CODEC = RecordCodecBuilder.create(p_161576_ -> p_161576_.group(BlockStateProvider.CODEC.fieldOf("source").forGetter(p_161592_ -> p_161592_.source), Codec.STRING.fieldOf("property").forGetter(p_161590_ -> p_161590_.propertyName), IntProvider.CODEC.fieldOf("values").forGetter(p_161578_ -> p_161578_.values)).apply(p_161576_, RandomizedIntStateProvider::new));

    private final BlockStateProvider source;

    private final String propertyName;

    @Nullable
    private IntegerProperty property;

    private final IntProvider values;

    public RandomizedIntStateProvider(BlockStateProvider blockStateProvider0, IntegerProperty integerProperty1, IntProvider intProvider2) {
        this.source = blockStateProvider0;
        this.property = integerProperty1;
        this.propertyName = integerProperty1.m_61708_();
        this.values = intProvider2;
        Collection<Integer> $$3 = integerProperty1.getPossibleValues();
        for (int $$4 = intProvider2.getMinValue(); $$4 <= intProvider2.getMaxValue(); $$4++) {
            if (!$$3.contains($$4)) {
                throw new IllegalArgumentException("Property value out of range: " + integerProperty1.m_61708_() + ": " + $$4);
            }
        }
    }

    public RandomizedIntStateProvider(BlockStateProvider blockStateProvider0, String string1, IntProvider intProvider2) {
        this.source = blockStateProvider0;
        this.propertyName = string1;
        this.values = intProvider2;
    }

    @Override
    protected BlockStateProviderType<?> type() {
        return BlockStateProviderType.RANDOMIZED_INT_STATE_PROVIDER;
    }

    @Override
    public BlockState getState(RandomSource randomSource0, BlockPos blockPos1) {
        BlockState $$2 = this.source.getState(randomSource0, blockPos1);
        if (this.property == null || !$$2.m_61138_(this.property)) {
            this.property = findProperty($$2, this.propertyName);
        }
        return (BlockState) $$2.m_61124_(this.property, this.values.sample(randomSource0));
    }

    private static IntegerProperty findProperty(BlockState blockState0, String string1) {
        Collection<Property<?>> $$2 = blockState0.m_61147_();
        Optional<IntegerProperty> $$3 = $$2.stream().filter(p_161583_ -> p_161583_.getName().equals(string1)).filter(p_161588_ -> p_161588_ instanceof IntegerProperty).map(p_161574_ -> (IntegerProperty) p_161574_).findAny();
        return (IntegerProperty) $$3.orElseThrow(() -> new IllegalArgumentException("Illegal property: " + string1));
    }
}