package com.mna.blocks.sorcery;

import com.mna.api.blocks.interfaces.ITranslucentBlock;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.blocks.WaterloggableBlock;
import com.mna.blocks.tileentities.ManaCrystalTile;
import com.mna.blocks.tileentities.init.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ManaCrystalBlock extends WaterloggableBlock implements ITranslucentBlock, EntityBlock {

    protected static final VoxelShape SHAPE_NORMAL = Block.box(0.0, 0.0, 0.0, 16.0, 25.5, 16.0);

    protected static final VoxelShape SHAPE_HANGING = Block.box(0.0, -9.5, 0.0, 16.0, 16.0, 16.0);

    public static final BooleanProperty HANGING = BooleanProperty.create("hanging");

    public ManaCrystalBlock() {
        super(BlockBehaviour.Properties.of().strength(2.0F).noOcclusion().sound(SoundType.GLASS).dynamicShape(), false);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(HANGING, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean hanging = false;
        if (context.m_43719_() == Direction.DOWN) {
            hanging = true;
        }
        return (BlockState) super.getStateForPlacement(context).m_61124_(HANGING, hanging);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HANGING);
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        if (worldIn.isClientSide && rand.nextBoolean()) {
            Vec3 origin = this.getRandomPointAroundBlock(pos, stateIn, true);
            Vec3 dest = this.getRandomPointAroundBlock(pos, stateIn, false);
            worldIn.addParticle(new MAParticleType(ParticleInit.LIGHTNING_BOLT.get()), origin.x, origin.y, origin.z, dest.x, dest.y, dest.z);
        }
    }

    private Vec3 getRandomPointAroundBlock(BlockPos pos, BlockState state, boolean touch) {
        return new Vec3(touch ? (double) pos.m_123341_() + 0.15 + Math.random() * 0.7 : (double) pos.m_123341_() + Math.random(), (double) (pos.m_123342_() + (state.m_61143_(HANGING) ? 0 : 1)) - 0.5 + Math.random(), touch ? (double) pos.m_123343_() + 0.15 + Math.random() * 0.7 : (double) pos.m_123343_() + Math.random());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ManaCrystalTile(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == TileEntityInit.MANA_CRYSTAL.get() && !level.isClientSide() ? (lvl, pos, state1, be) -> ManaCrystalTile.ServerTick(lvl, pos, state1, (ManaCrystalTile) be) : null;
    }

    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return 10;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return state.m_61143_(HANGING) ? SHAPE_HANGING : SHAPE_NORMAL;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}