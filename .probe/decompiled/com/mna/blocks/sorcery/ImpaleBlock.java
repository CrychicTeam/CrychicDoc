package com.mna.blocks.sorcery;

import com.mna.api.blocks.interfaces.IDontCreateBlockItem;
import com.mna.api.blocks.interfaces.ITranslucentBlock;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.blocks.FacingBlock;
import com.mna.blocks.tileentities.ImpaleSpikeTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.FakePlayerFactory;

public class ImpaleBlock extends FacingBlock implements ITranslucentBlock, IDontCreateBlockItem, EntityBlock {

    private static final VoxelShape SHAPE_UP = Shapes.or(Block.box(2.0, 0.0, 2.0, 14.0, 3.0, 14.0), Block.box(4.0, 3.0, 4.0, 12.0, 6.0, 12.0), Block.box(6.0, 6.0, 6.0, 10.0, 12.0, 10.0), Block.box(7.0, 12.0, 7.0, 9.0, 18.0, 9.0));

    private static final VoxelShape SHAPE_EAST = Shapes.or(Block.box(0.0, 2.0, 2.0, 3.0, 14.0, 14.0), Block.box(3.0, 4.0, 4.0, 6.0, 12.0, 12.0), Block.box(6.0, 6.0, 6.0, 12.0, 10.0, 10.0), Block.box(12.0, 7.0, 7.0, 18.0, 9.0, 9.0));

    private static final VoxelShape SHAPE_SOUTH = Shapes.or(Block.box(2.0, 2.0, 0.0, 14.0, 14.0, 3.0), Block.box(4.0, 4.0, 3.0, 12.0, 12.0, 6.0), Block.box(6.0, 6.0, 6.0, 10.0, 10.0, 12.0), Block.box(7.0, 7.0, 12.0, 9.0, 9.0, 18.0));

    private static final VoxelShape SHAPE_NORTH = Shapes.or(Block.box(2.0, 2.0, 13.0, 14.0, 14.0, 16.0), Block.box(4.0, 4.0, 10.0, 12.0, 12.0, 13.0), Block.box(6.0, 6.0, 4.0, 10.0, 10.0, 10.0), Block.box(7.0, 7.0, -2.0, 9.0, 9.0, 4.0));

    private static final VoxelShape SHAPE_WEST = Shapes.or(Block.box(13.0, 2.0, 2.0, 16.0, 14.0, 14.0), Block.box(10.0, 4.0, 4.0, 13.0, 12.0, 12.0), Block.box(4.0, 6.0, 6.0, 10.0, 10.0, 10.0), Block.box(-2.0, 7.0, 7.0, 4.0, 9.0, 9.0));

    private static final VoxelShape SHAPE_DOWN = Shapes.or(Block.box(2.0, 13.0, 2.0, 14.0, 16.0, 14.0), Block.box(4.0, 10.0, 4.0, 12.0, 13.0, 12.0), Block.box(6.0, 4.0, 6.0, 10.0, 10.0, 10.0), Block.box(7.0, -2.0, 7.0, 9.0, 4.0, 9.0));

    public ImpaleBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).instabreak().noLootTable().noOcclusion());
    }

    protected boolean isAir(BlockState state) {
        return false;
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pRandom.nextBoolean()) {
            Vec3 center = Vec3.atLowerCornerOf(pPos).add(pRandom.nextDouble(), pRandom.nextDouble(), pRandom.nextDouble());
            if (pRandom.nextBoolean()) {
                pLevel.addParticle(new MAParticleType(ParticleInit.EARTH.get()).setGravity(0.01F), center.x, center.y, center.z, 0.0, 0.0, 0.0);
            } else {
                pLevel.addParticle(new MAParticleType(ParticleInit.DUST.get()).setGravity(0.01F), center.x, center.y, center.z, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        int face = (Integer) blockState0.m_61143_(FacingBlock.SURFACE_TYPE);
        switch(face) {
            case 2:
                return SHAPE_DOWN;
            case 3:
                Direction dir = (Direction) blockState0.m_61143_(FacingBlock.FACING);
                switch(dir) {
                    case SOUTH:
                        return SHAPE_SOUTH;
                    case WEST:
                        return SHAPE_WEST;
                    case EAST:
                        return SHAPE_EAST;
                    default:
                        return SHAPE_NORTH;
                }
            default:
                return SHAPE_UP;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.m_43725_().getBlockState(context.getClickedPos().relative(context.m_43719_().getOpposite()));
        if (context.m_43725_() instanceof ServerLevel) {
            Player fp = FakePlayerFactory.getMinecraft((ServerLevel) context.m_43725_());
            if (!Block.isFaceFull(blockstate.m_60734_().m_5939_(blockstate, context.m_43725_(), context.getClickedPos(), CollisionContext.of(fp)), context.m_43719_())) {
                return null;
            }
        }
        Direction[] random_values = new Direction[] { Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH };
        BlockState state = super.getStateForPlacement(context);
        if ((Integer) state.m_61143_(FacingBlock.SURFACE_TYPE) != 3) {
            state = (BlockState) state.m_61124_(FACING, random_values[(int) (Math.random() * (double) random_values.length)]);
        }
        return state;
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        pLevel.m_7731_(pPos, Blocks.AIR.defaultBlockState(), 3);
    }

    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 0;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ImpaleSpikeTile(pPos, pState);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        entity.causeFallDamage(fallDistance * 2.0F, 3.0F, entity.damageSources().fall());
    }
}