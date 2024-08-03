package com.simibubi.create.content.contraptions.piston;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.Tags;

public class MechanicalPistonBlock extends DirectionalAxisKineticBlock implements IBE<MechanicalPistonBlockEntity> {

    public static final EnumProperty<MechanicalPistonBlock.PistonState> STATE = EnumProperty.create("state", MechanicalPistonBlock.PistonState.class);

    protected boolean isSticky;

    public static MechanicalPistonBlock normal(BlockBehaviour.Properties properties) {
        return new MechanicalPistonBlock(properties, false);
    }

    public static MechanicalPistonBlock sticky(BlockBehaviour.Properties properties) {
        return new MechanicalPistonBlock(properties, true);
    }

    protected MechanicalPistonBlock(BlockBehaviour.Properties properties, boolean sticky) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, Direction.NORTH)).m_61124_(STATE, MechanicalPistonBlock.PistonState.RETRACTED));
        this.isSticky = sticky;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STATE);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!player.mayBuild()) {
            return InteractionResult.PASS;
        } else if (player.m_6144_()) {
            return InteractionResult.PASS;
        } else if (!player.m_21120_(handIn).is(Tags.Items.SLIMEBALLS)) {
            if (player.m_21120_(handIn).isEmpty()) {
                this.withBlockEntityDo(worldIn, pos, be -> be.assembleNextTick = true);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        } else if (state.m_61143_(STATE) != MechanicalPistonBlock.PistonState.RETRACTED) {
            return InteractionResult.PASS;
        } else {
            Direction direction = (Direction) state.m_61143_(FACING);
            if (hit.getDirection() != direction) {
                return InteractionResult.PASS;
            } else if (((MechanicalPistonBlock) state.m_60734_()).isSticky) {
                return InteractionResult.PASS;
            } else if (worldIn.isClientSide) {
                Vec3 vec = hit.m_82450_();
                worldIn.addParticle(ParticleTypes.ITEM_SLIME, vec.x, vec.y, vec.z, 0.0, 0.0, 0.0);
                return InteractionResult.SUCCESS;
            } else {
                AllSoundEvents.SLIME_ADDED.playOnServer(worldIn, pos, 0.5F, 1.0F);
                if (!player.isCreative()) {
                    player.m_21120_(handIn).shrink(1);
                }
                worldIn.setBlockAndUpdate(pos, (BlockState) ((BlockState) AllBlocks.STICKY_MECHANICAL_PISTON.getDefaultState().m_61124_(FACING, direction)).m_61124_(AXIS_ALONG_FIRST_COORDINATE, (Boolean) state.m_61143_(AXIS_ALONG_FIRST_COORDINATE)));
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block p_220069_4_, BlockPos fromPos, boolean p_220069_6_) {
        Direction direction = (Direction) state.m_61143_(FACING);
        if (fromPos.equals(pos.relative(direction.getOpposite()))) {
            if (!world.isClientSide && !world.m_183326_().willTickThisTick(pos, this)) {
                world.m_186460_(pos, this, 0);
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource r) {
        Direction direction = (Direction) state.m_61143_(FACING);
        BlockState pole = worldIn.m_8055_(pos.relative(direction.getOpposite()));
        if (AllBlocks.PISTON_EXTENSION_POLE.has(pole)) {
            if (((Direction) pole.m_61143_(PistonExtensionPoleBlock.f_52588_)).getAxis() == direction.getAxis()) {
                this.withBlockEntityDo(worldIn, pos, be -> {
                    if (be.lastException != null) {
                        be.lastException = null;
                        be.sendData();
                    }
                });
            }
        }
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return state.m_61143_(STATE) != MechanicalPistonBlock.PistonState.RETRACTED ? InteractionResult.PASS : super.onWrenched(state, context);
    }

    @Override
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        Direction direction = (Direction) state.m_61143_(FACING);
        BlockPos pistonHead = null;
        boolean dropBlocks = player == null || !player.isCreative();
        Integer maxPoles = maxAllowedPistonPoles();
        for (int offset = 1; offset < maxPoles; offset++) {
            BlockPos currentPos = pos.relative(direction, offset);
            BlockState block = worldIn.getBlockState(currentPos);
            if (!isExtensionPole(block) || direction.getAxis() != ((Direction) block.m_61143_(BlockStateProperties.FACING)).getAxis()) {
                if (isPistonHead(block) && block.m_61143_(BlockStateProperties.FACING) == direction) {
                    pistonHead = currentPos;
                }
                break;
            }
        }
        if (pistonHead != null && pos != null) {
            BlockPos.betweenClosedStream(pos, pistonHead).filter(p -> !p.equals(pos)).forEach(p -> worldIn.m_46961_(p, dropBlocks));
        }
        for (int offsetx = 1; offsetx < maxPoles; offsetx++) {
            BlockPos currentPos = pos.relative(direction.getOpposite(), offsetx);
            BlockState block = worldIn.getBlockState(currentPos);
            if (!isExtensionPole(block) || direction.getAxis() != ((Direction) block.m_61143_(BlockStateProperties.FACING)).getAxis()) {
                break;
            }
            worldIn.m_46961_(currentPos, dropBlocks);
        }
        super.m_5707_(worldIn, pos, state, player);
    }

    public static int maxAllowedPistonPoles() {
        return AllConfigs.server().kinetics.maxPistonPoles.get();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        if (state.m_61143_(STATE) == MechanicalPistonBlock.PistonState.EXTENDED) {
            return AllShapes.MECHANICAL_PISTON_EXTENDED.get((Direction) state.m_61143_(FACING));
        } else {
            return state.m_61143_(STATE) == MechanicalPistonBlock.PistonState.MOVING ? AllShapes.MECHANICAL_PISTON.get((Direction) state.m_61143_(FACING)) : Shapes.block();
        }
    }

    @Override
    public Class<MechanicalPistonBlockEntity> getBlockEntityClass() {
        return MechanicalPistonBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MechanicalPistonBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends MechanicalPistonBlockEntity>) AllBlockEntityTypes.MECHANICAL_PISTON.get();
    }

    public static boolean isPiston(BlockState state) {
        return AllBlocks.MECHANICAL_PISTON.has(state) || isStickyPiston(state);
    }

    public static boolean isStickyPiston(BlockState state) {
        return AllBlocks.STICKY_MECHANICAL_PISTON.has(state);
    }

    public static boolean isExtensionPole(BlockState state) {
        return AllBlocks.PISTON_EXTENSION_POLE.has(state);
    }

    public static boolean isPistonHead(BlockState state) {
        return AllBlocks.MECHANICAL_PISTON_HEAD.has(state);
    }

    public static enum PistonState implements StringRepresentable {

        RETRACTED, MOVING, EXTENDED;

        @Override
        public String getSerializedName() {
            return Lang.asId(this.name());
        }
    }
}