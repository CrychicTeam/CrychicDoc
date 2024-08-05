package net.minecraft.client.renderer.block.model.multipart;

import com.google.common.collect.Streams;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class OrCondition implements Condition {

    public static final String TOKEN = "OR";

    private final Iterable<? extends Condition> conditions;

    public OrCondition(Iterable<? extends Condition> iterableExtendsCondition0) {
        this.conditions = iterableExtendsCondition0;
    }

    @Override
    public Predicate<BlockState> getPredicate(StateDefinition<Block, BlockState> stateDefinitionBlockBlockState0) {
        List<Predicate<BlockState>> $$1 = (List<Predicate<BlockState>>) Streams.stream(this.conditions).map(p_112009_ -> p_112009_.getPredicate(stateDefinitionBlockBlockState0)).collect(Collectors.toList());
        return p_112012_ -> $$1.stream().anyMatch(p_173513_ -> p_173513_.test(p_112012_));
    }
}