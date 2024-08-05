package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.mehvahdjukaar.moonlight.api.block.WaterBlock;
import net.mehvahdjukaar.moonlight.api.fluids.BuiltInSoftFluids;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidTank;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.tiles.GobletBlockTile;
import net.mehvahdjukaar.supplementaries.common.utils.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class GobletBlock extends WaterBlock implements EntityBlock {

    protected static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 9.0, 11.0);

    public static final IntegerProperty LIGHT_LEVEL = ModBlockProperties.LIGHT_LEVEL_0_15;

    public GobletBlock(BlockBehaviour.Properties properties) {
        super(properties.lightLevel(state -> (Integer) state.m_61143_(LIGHT_LEVEL)));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(LIGHT_LEVEL, 0)).m_61124_(WATERLOGGED, false));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.getBlockEntity(pos) instanceof GobletBlockTile tile && tile.isAccessibleBy(player) && tile.handleInteraction(player, handIn)) {
            if (!worldIn.isClientSide()) {
                tile.setChanged();
            }
            return InteractionResult.sidedSuccess(worldIn.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIGHT_LEVEL);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new GobletBlockTile(pPos, pState);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof GobletBlockTile tile) {
            return tile.fluidHolder.isEmpty() ? 0 : 15;
        } else {
            return 0;
        }
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (0.05 > (double) random.nextFloat() && world.getBlockEntity(pos) instanceof GobletBlockTile tile) {
            SoftFluidTank tank = tile.getSoftFluidTank();
            if (tank.getFluid().is(BuiltInSoftFluids.POTION.get())) {
                int i = tank.getCachedStillColor(world, pos);
                double d0 = (double) (i >> 16 & 0xFF) / 255.0;
                double d1 = (double) (i >> 8 & 0xFF) / 255.0;
                double d2 = (double) (i & 0xFF) / 255.0;
                world.addParticle(ParticleTypes.ENTITY_EFFECT, (double) pos.m_123341_() + 0.3125 + (double) random.nextFloat() * 0.375, (double) pos.m_123342_() + 0.5625, (double) pos.m_123343_() + 0.3125 + (double) random.nextFloat() * 0.375, d0, d1, d2);
            }
        }
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BlockUtil.addOptionalOwnership(placer, world, pos);
    }
}