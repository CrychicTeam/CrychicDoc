package snownee.kiwi.customization.block.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;
import snownee.kiwi.customization.block.KBlockSettings;
import snownee.kiwi.customization.block.KBlockUtils;
import snownee.kiwi.customization.block.loader.KBlockComponents;

public record StackableComponent(IntegerProperty property) implements KBlockComponent, LayeredComponent {

    public static final Codec<StackableComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(ExtraCodecs.intRange(0, 1).optionalFieldOf("min", 1).forGetter(StackableComponent::minValue), ExtraCodecs.POSITIVE_INT.fieldOf("max").forGetter(StackableComponent::maxValue)).apply(instance, StackableComponent::create));

    public static StackableComponent create(int max) {
        return create(1, max);
    }

    public static StackableComponent create(int min, int max) {
        return new StackableComponent(KBlockUtils.internProperty(IntegerProperty.create("c", min, max)));
    }

    @Override
    public KBlockComponent.Type<?> type() {
        return KBlockComponents.STACKABLE.getOrCreate();
    }

    @Override
    public void injectProperties(Block block, StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(this.property);
    }

    @Override
    public BlockState registerDefaultState(BlockState state) {
        return (BlockState) state.m_61124_(this.property, this.getDefaultLayer());
    }

    public int minValue() {
        return this.property.min;
    }

    public int maxValue() {
        return this.property.max;
    }

    @Override
    public boolean hasAnalogOutputSignal() {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state) {
        return Math.min((Integer) state.m_61143_(this.property) - this.minValue() + 1, 15);
    }

    @Override
    public BlockState getStateForPlacement(KBlockSettings settings, BlockState state, BlockPlaceContext context) {
        BlockState blockState = context.m_43725_().getBlockState(context.getClickedPos());
        return blockState.m_60713_(state.m_60734_()) ? (BlockState) blockState.m_61124_(this.property, Math.min(this.maxValue(), (Integer) blockState.m_61143_(this.property) + 1)) : state;
    }

    @Nullable
    @Override
    public Boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return !context.m_7078_() && context.m_43722_().is(state.m_60734_().asItem()) && state.m_61143_(this.property) < this.maxValue() ? Boolean.TRUE : null;
    }

    @Override
    public IntegerProperty getLayerProperty() {
        return this.property;
    }

    @Override
    public int getDefaultLayer() {
        return this.minValue();
    }
}