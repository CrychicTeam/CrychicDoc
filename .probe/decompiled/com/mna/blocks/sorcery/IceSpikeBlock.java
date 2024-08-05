package com.mna.blocks.sorcery;

import com.mna.api.blocks.interfaces.IDontCreateBlockItem;
import com.mna.api.blocks.interfaces.ITranslucentBlock;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.blocks.FacingBlock;
import com.mna.blocks.tileentities.IceSpikeTile;
import com.mna.entities.boss.Odin;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
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
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.FakePlayerFactory;
import org.jetbrains.annotations.Nullable;

public class IceSpikeBlock extends FacingBlock implements ITranslucentBlock, IDontCreateBlockItem, EntityBlock {

    private static final VoxelShape SHAPE_UP = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

    private static final VoxelShape SHAPE_EAST = Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);

    private static final VoxelShape SHAPE_SOUTH = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);

    private static final VoxelShape SHAPE_NORTH = Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);

    private static final VoxelShape SHAPE_WEST = Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    private static final VoxelShape SHAPE_DOWN = Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);

    public IceSpikeBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.ICE).instabreak().noCollission().noLootTable().noOcclusion());
    }

    protected boolean isAir(BlockState state) {
        return false;
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pRandom.nextBoolean()) {
            Vec3 center = Vec3.atLowerCornerOf(pPos).add(pRandom.nextDouble(), pRandom.nextDouble(), pRandom.nextDouble());
            if (pRandom.nextBoolean()) {
                pLevel.addParticle(new MAParticleType(ParticleInit.FROST.get()).setGravity(0.01F), center.x, center.y, center.z, 0.0, 0.0, 0.0);
            } else {
                pLevel.addParticle(ParticleTypes.SNOWFLAKE, center.x, center.y, center.z, 0.0, 0.0, 0.0);
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

    @Nullable
    public BlockPathTypes getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
        return mob instanceof Odin ? BlockPathTypes.OPEN : BlockPathTypes.DANGER_POWDER_SNOW;
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (!(pEntity instanceof Odin)) {
            if (pEntity instanceof LivingEntity && (pEntity.xOld != pEntity.getX() || pEntity.zOld != pEntity.getZ())) {
                LivingEntity living = (LivingEntity) pEntity;
                living.m_7601_(pState, new Vec3(0.8F, 0.75, 0.8F));
                if (!pLevel.isClientSide() && !living.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                    living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1));
                }
                living.m_146917_(living.m_146888_() + 1);
                double d0 = Math.abs(pEntity.getX() - pEntity.xOld);
                double d1 = Math.abs(pEntity.getZ() - pEntity.zOld);
                if (d0 >= 0.003F || d1 >= 0.003F) {
                    pEntity.hurt(pEntity.damageSources().freeze(), 1.0F);
                }
            }
        }
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
        return new IceSpikeTile(pPos, pState);
    }
}