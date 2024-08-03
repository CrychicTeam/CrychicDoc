package com.mna.blocks.decoration;

import com.mna.api.blocks.ISpellInteractibleBlock;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.collections.Components;
import com.mna.blocks.WaterloggableBlock;
import com.mna.blocks.tileentities.BrazierTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BrazierBlock extends WaterloggableBlock implements EntityBlock, ISpellInteractibleBlock<BrazierBlock> {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public static final BooleanProperty AUTO_ACTIVATE = BooleanProperty.create("auto_activate");

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.values());

    protected static final VoxelShape SHAPE = Block.box(0.0, 1.0, 0.0, 16.0, 8.0, 16.0);

    public BrazierBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.0F, 10.0F).noOcclusion().randomTicks().sound(SoundType.LANTERN), false);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(ACTIVE, false)).m_61124_(AUTO_ACTIVATE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ACTIVE, AUTO_ACTIVATE, FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource rand) {
        if ((Boolean) state.m_61143_(AUTO_ACTIVATE)) {
            BlockState newstate = state;
            if (worldIn.m_46462_() && !(Boolean) state.m_61143_(ACTIVE)) {
                newstate = (BlockState) state.m_61124_(ACTIVE, true);
            } else if (worldIn.m_46461_() && (Boolean) state.m_61143_(ACTIVE)) {
                newstate = (BlockState) state.m_61124_(ACTIVE, false);
            }
            worldIn.m_7731_(pos, newstate, 1);
            worldIn.sendBlockUpdated(pos, state, newstate, 2);
            worldIn.m_186460_(pos, this, 20);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult p_225533_6_) {
        if (!worldIn.isClientSide) {
            BlockState newstate;
            if (player.m_6047_()) {
                newstate = (BlockState) state.m_61124_(AUTO_ACTIVATE, !(Boolean) state.m_61143_(AUTO_ACTIVATE));
                if ((Boolean) newstate.m_61143_(AUTO_ACTIVATE)) {
                    player.m_213846_(Component.translatable("block.mna.brazier.auto_on"));
                } else {
                    player.m_213846_(Component.translatable("block.mna.brazier.auto_off"));
                }
                worldIn.m_186460_(pos, this, 20);
            } else {
                newstate = (BlockState) state.m_61124_(ACTIVE, !(Boolean) state.m_61143_(ACTIVE));
            }
            worldIn.setBlock(pos, newstate, 1);
            worldIn.sendBlockUpdated(pos, state, newstate, 2);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        if ((Boolean) stateIn.m_61143_(ACTIVE)) {
            int color = -1;
            BlockEntity be = worldIn.getBlockEntity(pos);
            if (be != null && be instanceof BrazierTile) {
                color = ((BrazierTile) be).getColor();
            }
            for (int i = 0; i < 20; i++) {
                MAParticleType pfx = new MAParticleType(ParticleInit.FLAME.get()).setMaxAge((int) (20.0 + 20.0 * Math.random())).setScale(0.03F);
                if (color == -1) {
                    pfx.setColor(30, 172, 255);
                } else {
                    pfx.setColor(FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color), FastColor.ARGB32.alpha(color));
                }
                worldIn.addParticle(pfx, (double) ((float) pos.m_123341_() + 0.5F + -0.175F + worldIn.getRandom().nextFloat() * 0.35F), (double) pos.m_123342_() + 0.2, (double) ((float) pos.m_123343_() + 0.5F + -0.175F + worldIn.getRandom().nextFloat() * 0.35F), 0.0, 0.02F, 0.0);
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        BlockState superState = super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        Direction myFacing = (Direction) stateIn.m_61143_(FACING);
        if (!this.isDirectionValidAnchor(worldIn, currentPos, myFacing)) {
            Direction newFacing = null;
            for (Direction dir : Direction.values()) {
                if (this.isDirectionValidAnchor(worldIn, currentPos, dir)) {
                    newFacing = dir;
                    break;
                }
            }
            if (newFacing == null) {
                newFacing = Direction.DOWN;
            }
            superState = (BlockState) superState.m_61124_(FACING, newFacing);
        }
        return superState;
    }

    private boolean isDirectionValidAnchor(LevelReader world, BlockPos pos, Direction facing) {
        BlockPos facingPos = pos.relative(facing);
        BlockState facingBlockState = world.m_8055_(facingPos);
        return facingBlockState.m_60783_(world, facingPos, facing.getOpposite());
    }

    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return state.m_61143_(ACTIVE) ? 14 : 0;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.m_49966_().m_61124_(FACING, context.m_43719_().getOpposite());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BrazierTile(pPos, pState);
    }

    @Override
    public boolean onHitBySpell(Level world, BlockPos pos, ISpellDefinition spell) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be != null && be instanceof BrazierTile && spell.containsPart(Components.LIGHT.getRegistryName())) {
            ((BrazierTile) be).setColor(spell.getParticleColorOverride());
            return true;
        } else {
            return false;
        }
    }
}