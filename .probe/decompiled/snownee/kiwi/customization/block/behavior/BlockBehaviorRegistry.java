package snownee.kiwi.customization.block.behavior;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Objects;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public final class BlockBehaviorRegistry {

    private static final BlockBehaviorRegistry INSTANCE = new BlockBehaviorRegistry();

    private Block context;

    private final Map<Block, UseHandler> useHandlers = Maps.newIdentityHashMap();

    public static BlockBehaviorRegistry getInstance() {
        return INSTANCE;
    }

    private BlockBehaviorRegistry() {
    }

    public void addUseHandler(UseHandler handler) {
        Objects.requireNonNull(this.context, "Context not set");
        this.useHandlers.put(this.context, handler);
    }

    public void setContext(Block block) {
        this.context = block;
    }

    public InteractionResult onUseBlock(Player entity, Level level, InteractionHand hand, BlockHitResult hitVec) {
        BlockState blockState = level.getBlockState(hitVec.getBlockPos());
        return this.useHandlers.containsKey(blockState.m_60734_()) ? ((UseHandler) this.useHandlers.get(blockState.m_60734_())).use(blockState, entity, level, hand, hitVec) : InteractionResult.PASS;
    }
}