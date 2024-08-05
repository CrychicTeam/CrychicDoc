package me.jellysquid.mods.lithium.mixin.ai.pathing;

import me.jellysquid.mods.lithium.common.ai.pathing.BlockStatePathingCache;
import me.jellysquid.mods.lithium.common.world.blockview.SingleBlockBlockView;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.apache.commons.lang3.Validate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ BlockBehaviour.BlockStateBase.class })
public abstract class AbstractBlockStateMixin implements BlockStatePathingCache {

    private BlockPathTypes pathNodeType = null;

    private BlockPathTypes pathNodeTypeNeighbor = null;

    @Inject(method = { "initShapeCache()V" }, at = { @At("RETURN") })
    private void init(CallbackInfo ci) {
        this.pathNodeType = null;
        this.pathNodeTypeNeighbor = null;
        BlockState state = this.asState();
        SingleBlockBlockView blockView = SingleBlockBlockView.of(state, BlockPos.ZERO);
        try {
            this.pathNodeType = (BlockPathTypes) Validate.notNull(WalkNodeEvaluator.getBlockPathTypeRaw(blockView, BlockPos.ZERO));
        } catch (ClassCastException | SingleBlockBlockView.SingleBlockViewException var6) {
            this.pathNodeType = null;
        }
        try {
            this.pathNodeTypeNeighbor = WalkNodeEvaluator.checkNeighbourBlocks(blockView, BlockPos.ZERO.mutable(), null);
            if (this.pathNodeTypeNeighbor == null) {
                this.pathNodeTypeNeighbor = BlockPathTypes.OPEN;
            }
        } catch (ClassCastException | SingleBlockBlockView.SingleBlockViewException var5) {
            this.pathNodeTypeNeighbor = null;
        }
    }

    @Override
    public BlockPathTypes getPathNodeType() {
        return this.pathNodeType;
    }

    @Override
    public BlockPathTypes getNeighborPathNodeType() {
        return this.pathNodeTypeNeighbor;
    }

    @Shadow
    protected abstract BlockState asState();

    @Shadow
    public abstract Block getBlock();
}