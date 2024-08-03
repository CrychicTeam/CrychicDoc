package se.mickelus.tetra.blocks.multischematic;

import java.util.Collection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolAction;
import org.jetbrains.annotations.Nullable;
import se.mickelus.tetra.TetraToolActions;
import se.mickelus.tetra.blocks.salvage.BlockInteraction;
import se.mickelus.tetra.blocks.salvage.IInteractiveBlock;
import se.mickelus.tetra.effect.EffectHelper;

public class RuinedMultiblockSchematicBlock extends HorizontalDirectionalBlock implements IInteractiveBlock {

    public static final DirectionProperty facingProp = BlockStateProperties.HORIZONTAL_FACING;

    protected ResourceLocation pryTable;

    protected BlockInteraction[] pryAction = new BlockInteraction[] { new BlockInteraction(TetraToolActions.pry, 1, Direction.EAST, 6.0F, 10.0F, 7.0F, 10.0F, BlockStatePredicate.ANY, this::pryBlock) };

    public RuinedMultiblockSchematicBlock(BlockBehaviour.Properties properties, ResourceLocation pryTable) {
        super(properties);
        this.pryTable = pryTable;
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(facingProp, Direction.EAST));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(facingProp);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) super.m_5573_(context).m_61124_(facingProp, context.m_8125_().getOpposite());
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return this.pryTable != null ? BlockInteraction.attemptInteraction(world, state, pos, player, hand, hit) : super.m_6227_(state, world, pos, player, hand, hit);
    }

    @Override
    public BlockInteraction[] getPotentialInteractions(Level world, BlockPos pos, BlockState blockState, Direction face, Collection<ToolAction> tools) {
        return this.pryTable != null && face.getOpposite().equals(blockState.m_61143_(facingProp)) ? this.pryAction : new BlockInteraction[0];
    }

    protected boolean pryBlock(Level world, BlockPos pos, BlockState blockState, Player player, InteractionHand hand, Direction facing) {
        boolean didBreak = EffectHelper.breakBlock(world, player, player.m_21120_(hand), pos, blockState, false, false);
        if (didBreak && world instanceof ServerLevel) {
            BlockInteraction.getLoot(this.pryTable, player, hand, (ServerLevel) world, blockState).forEach(lootStack -> m_49840_(world, pos, lootStack));
        }
        return true;
    }
}