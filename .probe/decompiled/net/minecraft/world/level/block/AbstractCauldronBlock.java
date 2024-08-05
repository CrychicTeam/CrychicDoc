package net.minecraft.world.level.block;

import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class AbstractCauldronBlock extends Block {

    private static final int SIDE_THICKNESS = 2;

    private static final int LEG_WIDTH = 4;

    private static final int LEG_HEIGHT = 3;

    private static final int LEG_DEPTH = 2;

    protected static final int FLOOR_LEVEL = 4;

    private static final VoxelShape INSIDE = m_49796_(2.0, 4.0, 2.0, 14.0, 16.0, 14.0);

    protected static final VoxelShape SHAPE = Shapes.join(Shapes.block(), Shapes.or(m_49796_(0.0, 0.0, 4.0, 16.0, 3.0, 12.0), m_49796_(4.0, 0.0, 0.0, 12.0, 3.0, 16.0), m_49796_(2.0, 0.0, 2.0, 14.0, 3.0, 14.0), INSIDE), BooleanOp.ONLY_FIRST);

    private final Map<Item, CauldronInteraction> interactions;

    public AbstractCauldronBlock(BlockBehaviour.Properties blockBehaviourProperties0, Map<Item, CauldronInteraction> mapItemCauldronInteraction1) {
        super(blockBehaviourProperties0);
        this.interactions = mapItemCauldronInteraction1;
    }

    protected double getContentHeight(BlockState blockState0) {
        return 0.0;
    }

    protected boolean isEntityInsideContent(BlockState blockState0, BlockPos blockPos1, Entity entity2) {
        return entity2.getY() < (double) blockPos1.m_123342_() + this.getContentHeight(blockState0) && entity2.getBoundingBox().maxY > (double) blockPos1.m_123342_() + 0.25;
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        ItemStack $$6 = player3.m_21120_(interactionHand4);
        CauldronInteraction $$7 = (CauldronInteraction) this.interactions.get($$6.getItem());
        return $$7.interact(blockState0, level1, blockPos2, player3, interactionHand4, $$6);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    public VoxelShape getInteractionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return INSIDE;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState0) {
        return true;
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }

    public abstract boolean isFull(BlockState var1);

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        BlockPos $$4 = PointedDripstoneBlock.findStalactiteTipAboveCauldron(serverLevel1, blockPos2);
        if ($$4 != null) {
            Fluid $$5 = PointedDripstoneBlock.getCauldronFillFluidType(serverLevel1, $$4);
            if ($$5 != Fluids.EMPTY && this.canReceiveStalactiteDrip($$5)) {
                this.receiveStalactiteDrip(blockState0, serverLevel1, blockPos2, $$5);
            }
        }
    }

    protected boolean canReceiveStalactiteDrip(Fluid fluid0) {
        return false;
    }

    protected void receiveStalactiteDrip(BlockState blockState0, Level level1, BlockPos blockPos2, Fluid fluid3) {
    }
}