package se.mickelus.tetra.blocks.forged;

import com.google.common.base.Predicates;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.tetra.TetraToolActions;
import se.mickelus.tetra.blocks.PropertyMatcher;
import se.mickelus.tetra.blocks.TetraWaterloggedBlock;
import se.mickelus.tetra.blocks.salvage.BlockInteraction;
import se.mickelus.tetra.blocks.salvage.IInteractiveBlock;

@ParametersAreNonnullByDefault
public class ForgedVentBlock extends TetraWaterloggedBlock implements IInteractiveBlock {

    public static final IntegerProperty propRotation = IntegerProperty.create("rotation", 0, 3);

    public static final BooleanProperty propX = BooleanProperty.create("x");

    public static final BooleanProperty propBroken = BooleanProperty.create("broken");

    public static final String identifier = "forged_vent";

    private static final ResourceLocation boltLootTable = new ResourceLocation("tetra", "forged/bolt_break");

    private static final ResourceLocation ventLootTable = new ResourceLocation("tetra", "forged/vent_break");

    public static final BlockInteraction[] interactions = new BlockInteraction[] { new BlockInteraction(TetraToolActions.hammer, 3, Direction.EAST, 1.0F, 4.0F, 12.0F, 15.0F, new PropertyMatcher().where(propBroken, Predicates.equalTo(false)).where(propRotation, Predicates.equalTo(0)), ForgedVentBlock::breakBolt), new BlockInteraction(TetraToolActions.hammer, 3, Direction.EAST, 1.0F, 4.0F, 1.0F, 4.0F, new PropertyMatcher().where(propBroken, Predicates.equalTo(false)).where(propRotation, Predicates.equalTo(1)), ForgedVentBlock::breakBolt), new BlockInteraction(TetraToolActions.hammer, 3, Direction.EAST, 12.0F, 15.0F, 12.0F, 15.0F, new PropertyMatcher().where(propBroken, Predicates.equalTo(false)).where(propRotation, Predicates.equalTo(2)), ForgedVentBlock::breakBolt), new BlockInteraction(TetraToolActions.hammer, 3, Direction.EAST, 12.0F, 15.0F, 1.0F, 4.0F, new PropertyMatcher().where(propBroken, Predicates.equalTo(false)).where(propRotation, Predicates.equalTo(3)), ForgedVentBlock::breakBolt), new BlockInteraction(TetraToolActions.hammer, 3, Direction.WEST, 12.0F, 15.0F, 12.0F, 15.0F, new PropertyMatcher().where(propBroken, Predicates.equalTo(false)).where(propRotation, Predicates.equalTo(0)), ForgedVentBlock::breakBolt), new BlockInteraction(TetraToolActions.hammer, 3, Direction.WEST, 12.0F, 15.0F, 1.0F, 4.0F, new PropertyMatcher().where(propBroken, Predicates.equalTo(false)).where(propRotation, Predicates.equalTo(1)), ForgedVentBlock::breakBolt), new BlockInteraction(TetraToolActions.hammer, 3, Direction.WEST, 1.0F, 4.0F, 12.0F, 15.0F, new PropertyMatcher().where(propBroken, Predicates.equalTo(false)).where(propRotation, Predicates.equalTo(2)), ForgedVentBlock::breakBolt), new BlockInteraction(TetraToolActions.hammer, 3, Direction.WEST, 1.0F, 4.0F, 1.0F, 4.0F, new PropertyMatcher().where(propBroken, Predicates.equalTo(false)).where(propRotation, Predicates.equalTo(3)), ForgedVentBlock::breakBolt), new BlockInteraction(TetraToolActions.pry, 1, Direction.EAST, 7.0F, 11.0F, 8.0F, 12.0F, new PropertyMatcher().where(propBroken, Predicates.equalTo(true)), ForgedVentBlock::breakBeam), new BlockInteraction(TetraToolActions.pry, 1, Direction.WEST, 7.0F, 11.0F, 8.0F, 12.0F, new PropertyMatcher().where(propBroken, Predicates.equalTo(true)), ForgedVentBlock::breakBeam) };

    @ObjectHolder(registryName = "block", value = "tetra:forged_vent")
    public static ForgedVentBlock instance;

    public ForgedVentBlock() {
        super(ForgedBlockCommon.propertiesNotSolid);
    }

    private static boolean breakBolt(Level world, BlockPos pos, BlockState blockState, Player player, InteractionHand hand, Direction hitFace) {
        world.setBlock(pos, (BlockState) world.getBlockState(pos).m_61124_(propBroken, true), 2);
        if (!world.isClientSide) {
            ServerLevel serverWorld = (ServerLevel) world;
            if (player != null) {
                BlockInteraction.dropLoot(ventLootTable, player, hand, serverWorld, blockState);
            } else {
                BlockInteraction.dropLoot(ventLootTable, serverWorld, pos, blockState);
            }
            serverWorld.m_5594_(null, pos, SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.PLAYERS, 0.4F, 0.5F);
        }
        return true;
    }

    private static boolean breakBeam(Level world, BlockPos pos, BlockState blockState, @Nullable Player player, @Nullable InteractionHand hand, Direction hitFace) {
        List<BlockPos> connectedVents = getConnectedBlocks(world, pos, new LinkedList(), (Boolean) blockState.m_61143_(propX));
        if (connectedVents.stream().anyMatch(blockPos -> !(Boolean) world.getBlockState(blockPos).m_61143_(propBroken))) {
            if (!world.isClientSide) {
                world.playSound(null, pos, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.PLAYERS, 0.4F, 2.0F);
            }
            return false;
        } else {
            connectedVents.forEach(blockPos -> {
                world.m_5898_(null, 2001, blockPos, Block.getId(world.getBlockState(blockPos)));
                world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
            });
            if (!world.isClientSide) {
                ServerLevel serverWorld = (ServerLevel) world;
                if (player != null) {
                    BlockInteraction.dropLoot(ventLootTable, player, hand, serverWorld, blockState);
                } else {
                    BlockInteraction.dropLoot(ventLootTable, serverWorld, pos, blockState);
                }
            }
            return true;
        }
    }

    private static List<BlockPos> getConnectedBlocks(Level world, BlockPos pos, List<BlockPos> visited, boolean isXAxis) {
        if (!visited.contains(pos) && world.getBlockState(pos).m_60734_() instanceof ForgedVentBlock) {
            visited.add(pos);
            getConnectedBlocks(world, pos.above(), visited, isXAxis);
            getConnectedBlocks(world, pos.below(), visited, isXAxis);
            if (isXAxis) {
                getConnectedBlocks(world, pos.east(), visited, isXAxis);
                getConnectedBlocks(world, pos.west(), visited, isXAxis);
            } else {
                getConnectedBlocks(world, pos.north(), visited, isXAxis);
                getConnectedBlocks(world, pos.south(), visited, isXAxis);
            }
        }
        return visited;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(propRotation, propX, propBroken);
    }

    @Override
    public BlockInteraction[] getPotentialInteractions(Level world, BlockPos pos, BlockState state, Direction face, Collection<ToolAction> tools) {
        return (BlockInteraction[]) Arrays.stream(interactions).filter(interaction -> interaction.isPotentialInteraction(world, pos, state, state.m_61143_(propX) ? Direction.EAST : Direction.SOUTH, face, tools)).toArray(BlockInteraction[]::new);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTrace) {
        return BlockInteraction.attemptInteraction(world, state, pos, player, hand, rayTrace);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag advanced) {
        tooltip.add(ForgedBlockCommon.locationTooltip);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState = super.getStateForPlacement(context);
        Direction playerFacing = context.m_43723_() != null ? context.m_43723_().m_6350_() : Direction.NORTH;
        blockState = blockState != null ? blockState : this.m_49966_();
        blockState = (BlockState) blockState.m_61124_(propX, Direction.Axis.X.equals(playerFacing.getAxis()));
        int rotation = 0;
        if (Direction.EAST.equals(playerFacing) || Direction.SOUTH.equals(playerFacing)) {
            rotation = 2;
        }
        if (context.m_43719_() != Direction.UP && (context.m_43719_() == Direction.DOWN || context.m_43720_().y - (double) context.getClickedPos().m_123342_() > 0.5)) {
            rotation++;
        }
        return (BlockState) ((BlockState) blockState.m_61124_(propRotation, rotation)).m_61124_(propBroken, false);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        boolean isXAxis = (Boolean) state.m_61143_(propX);
        if (rot.equals(Rotation.CLOCKWISE_90) || rot.equals(Rotation.COUNTERCLOCKWISE_90)) {
            state = (BlockState) state.m_61124_(propX, !isXAxis);
        }
        return !rot.equals(Rotation.CLOCKWISE_180) && (isXAxis || !rot.equals(Rotation.CLOCKWISE_90)) && (!isXAxis || !rot.equals(Rotation.COUNTERCLOCKWISE_90)) ? (BlockState) state.m_61124_(propRotation, (Integer) state.m_61143_(propRotation)) : (BlockState) state.m_61124_(propRotation, (Integer) state.m_61143_(propRotation) ^ 2);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext contex) {
        return state.m_61143_(propX) ? m_49796_(0.0, 0.0, 7.0, 16.0, 16.0, 9.0) : m_49796_(7.0, 0.0, 0.0, 9.0, 16.0, 16.0);
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter world, BlockPos pos) {
        return 0;
    }
}