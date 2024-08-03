package malte0811.ferritecore.mixin.predicates;

import com.google.common.base.Splitter;
import java.util.function.Predicate;
import malte0811.ferritecore.impl.KeyValueConditionImpl;
import net.minecraft.client.renderer.block.model.multipart.KeyValueCondition;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = { KeyValueCondition.class }, priority = 2000)
public class KeyValueConditionMixin {

    @Shadow
    @Final
    private String key;

    @Shadow
    @Final
    private String value;

    @Shadow
    @Final
    private static Splitter PIPE_SPLITTER;

    @Overwrite
    public Predicate<BlockState> getPredicate(StateDefinition<Block, BlockState> stateContainer) {
        return KeyValueConditionImpl.getPredicate(stateContainer, this.key, this.value, PIPE_SPLITTER);
    }
}