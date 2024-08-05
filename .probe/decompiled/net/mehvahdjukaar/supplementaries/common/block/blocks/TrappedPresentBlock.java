package net.mehvahdjukaar.supplementaries.common.block.blocks;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.client.util.ParticleUtil;
import net.mehvahdjukaar.supplementaries.common.block.present.IPresentItemBehavior;
import net.mehvahdjukaar.supplementaries.common.block.tiles.TrappedPresentBlockTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

public class TrappedPresentBlock extends AbstractPresentBlock {

    private static final Map<Item, IPresentItemBehavior> TRAPPED_PRESENT_INTERACTIONS_REGISTRY = new IdentityHashMap();

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final BooleanProperty ON_COOLDOWN = BlockStateProperties.TRIGGERED;

    public TrappedPresentBlock(DyeColor color, BlockBehaviour.Properties properties) {
        super(color, properties);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, Direction.NORTH)).m_61124_(ON_COOLDOWN, false));
    }

    public static void registerBehavior(ItemLike pItem, IPresentItemBehavior pBehavior) {
        TRAPPED_PRESENT_INTERACTIONS_REGISTRY.put(pItem.asItem(), pBehavior);
    }

    public static IPresentItemBehavior getPresentBehavior(ItemStack pStack) {
        return (IPresentItemBehavior) TRAPPED_PRESENT_INTERACTIONS_REGISTRY.getOrDefault(pStack.getItem(), (IPresentItemBehavior) (source, stack) -> Optional.empty());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, ON_COOLDOWN);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TrappedPresentBlockTile(pPos, pState);
    }

    @Override
    public boolean triggerEvent(BlockState pState, Level pLevel, BlockPos pPos, int pId, int pParam) {
        if (pId == 0 && (Boolean) pState.m_61143_(ON_COOLDOWN)) {
            if (pLevel.isClientSide) {
                RandomSource random = pLevel.random;
                double cx = (double) pPos.m_123341_() + 0.5;
                double cy = (double) pPos.m_123342_() + 0.5 + 0.4;
                double cz = (double) pPos.m_123343_() + 0.5;
                for (int i = 0; i < 10; i++) {
                    double speed = random.nextDouble() * 0.15 + 0.015;
                    double py = cy + 0.02 + (random.nextDouble() - 0.5) * 0.3;
                    double dx = random.nextGaussian() * 0.01;
                    double dy = speed + random.nextGaussian() * 0.01;
                    double dz = random.nextGaussian() * 0.01;
                    pLevel.addParticle(ParticleTypes.CLOUD, cx, py, cz, dx, dy, dz);
                }
                ParticleUtil.spawnBreakParticles(PresentBlock.SHAPE_LID, pPos, pState, pLevel);
            }
            return true;
        } else {
            return super.m_8133_(pState, pLevel, pPos, pId, pParam);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);
        if ((Boolean) state.m_61143_(ON_COOLDOWN)) {
            level.m_46597_(pos, (BlockState) state.m_61124_(ON_COOLDOWN, false));
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.m_61124_(FACING, rot.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.m_60717_(mirrorIn.getRotation((Direction) state.m_61143_(FACING)));
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.m_6861_(state, world, pos, block, fromPos, isMoving);
        boolean isPowered = world.m_276867_(pos);
        if (world instanceof ServerLevel serverLevel && isPowered && (Boolean) state.m_61143_(PACKED) && world.getBlockEntity(pos) instanceof TrappedPresentBlockTile tile) {
            tile.detonate(serverLevel, pos);
        }
    }
}