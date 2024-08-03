package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.tiles.TurnTableBlockTile;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModParticles;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModSounds;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class TurnTableBlock extends Block implements EntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public static final IntegerProperty POWER = BlockStateProperties.POWER;

    public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;

    public static final BooleanProperty ROTATING = ModBlockProperties.ROTATING;

    public TurnTableBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.UP)).m_61124_(POWER, 0)).m_61124_(INVERTED, false)).m_61124_(ROTATING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWER, INVERTED, ROTATING);
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
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.m_49966_().m_61124_(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (this.updatePower(state, world, pos) && (Integer) world.getBlockState(pos).m_61143_(POWER) != 0) {
            this.tryRotate(world, pos);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        Direction face = hit.getDirection();
        Direction myDir = (Direction) state.m_61143_(FACING);
        if (face == myDir || face == myDir.getOpposite()) {
            return InteractionResult.PASS;
        } else if (!Utils.mayPerformBlockAction(player, pos, player.m_21120_(handIn))) {
            return InteractionResult.PASS;
        } else {
            state = (BlockState) state.m_61122_(INVERTED);
            float f = state.m_61143_(INVERTED) ? 0.55F : 0.5F;
            worldIn.playSound(player, pos, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS, 0.3F, f);
            worldIn.setBlock(pos, state, 6);
            return InteractionResult.sidedSuccess(worldIn.isClientSide);
        }
    }

    public boolean updatePower(BlockState state, Level world, BlockPos pos) {
        int bestNeighborSignal = world.m_277086_(pos);
        int currentPower = (Integer) state.m_61143_(POWER);
        if (bestNeighborSignal != currentPower) {
            world.setBlock(pos, (BlockState) ((BlockState) state.m_61124_(POWER, bestNeighborSignal)).m_61124_(ROTATING, bestNeighborSignal != 0), 6);
            return true;
        } else {
            return false;
        }
    }

    private void tryRotate(Level world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof TurnTableBlockTile te) {
            te.tryRotate();
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block neighborBlock, BlockPos fromPos, boolean moving) {
        super.m_6861_(state, world, pos, neighborBlock, fromPos, moving);
        boolean powerChanged = this.updatePower(state, world, pos);
        if ((Integer) world.getBlockState(pos).m_61143_(POWER) != 0 && (powerChanged || fromPos.equals(pos.relative((Direction) state.m_61143_(FACING))))) {
            this.tryRotate(world, pos);
        }
    }

    private static Vec3 rotateY(Vec3 vec, float deg) {
        if (deg == 0.0F) {
            return vec;
        } else if (vec == Vec3.ZERO) {
            return vec;
        } else {
            double x = vec.x;
            double y = vec.y;
            double z = vec.z;
            float angle = deg * (float) (Math.PI / 180.0);
            double s = Math.sin((double) angle);
            double c = Math.cos((double) angle);
            return new Vec3(x * c + z * s, y, z * c - x * s);
        }
    }

    public static int getPeriod(BlockState state) {
        return 60 - (Integer) state.m_61143_(POWER) * 4 + 4;
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity e) {
        super.stepOn(world, pos, state, e);
        if ((Boolean) CommonConfigs.Redstone.TURN_TABLE_ROTATE_ENTITIES.get()) {
            if (e.onGround()) {
                if ((Integer) state.m_61143_(POWER) != 0 && state.m_61143_(FACING) == Direction.UP) {
                    float period = (float) (getPeriod(state) + 1);
                    float angleIncrement = 90.0F / period;
                    float increment = state.m_61143_(INVERTED) ? angleIncrement : -1.0F * angleIncrement;
                    Vec3 origin = new Vec3((double) pos.m_123341_() + 0.5, (double) pos.m_123342_(), (double) pos.m_123343_() + 0.5);
                    Vec3 oldPos = e.position();
                    Vec3 oldOffset = oldPos.subtract(origin);
                    Vec3 newOffset = rotateY(oldOffset, increment);
                    Vec3 posDiff = origin.add(newOffset).subtract(oldPos);
                    e.move(MoverType.SHULKER_BOX, posDiff);
                    e.hurtMarked = true;
                    if (e instanceof LivingEntity entity) {
                        if (e instanceof ServerPlayer player) {
                            Advancement advancement = world.getServer().getAdvancements().getAdvancement(Supplementaries.res("husbandry/turn_table"));
                            if (advancement != null && !player.getAdvancements().getOrStartProgress(advancement).isDone()) {
                                player.getAdvancements().award(advancement, "unlock");
                            }
                        }
                        e.setOnGround(false);
                        float diff = e.getYHeadRot() - increment;
                        e.setYBodyRot(diff);
                        e.setYHeadRot(diff);
                        entity.yHeadRotO = ((LivingEntity) e).yHeadRot;
                        entity.setNoActionTime(20);
                        if (e instanceof Cat cat && cat.m_21827_() && !world.isClientSide && world.getBlockEntity(pos) instanceof TurnTableBlockTile tile) {
                            int catTimer = tile.getCatTimer();
                            if (catTimer == 0) {
                                tile.setCat();
                                world.playSound(null, (double) pos.m_123341_() + 0.5, (double) ((float) pos.m_123342_() + 1.0F), (double) pos.m_123343_() + 0.5, (SoundEvent) ModSounds.TOM.get(), SoundSource.BLOCKS, 0.85F, 1.0F);
                            }
                        }
                    }
                    e.yRotO = e.getYRot();
                    e.setYRot(e.getYRot() - increment);
                }
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TurnTableBlockTile(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return Utils.getTicker(pBlockEntityType, (BlockEntityType) ModRegistry.TURN_TABLE_TILE.get(), !pLevel.isClientSide ? TurnTableBlockTile::tick : null);
    }

    @Override
    public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int eventID, int eventParam) {
        if (eventID == 0) {
            if (world.isClientSide && (Boolean) ClientConfigs.Blocks.TURN_TABLE_PARTICLES.get()) {
                Direction dir = (Direction) state.m_61143_(FACING);
                BlockPos front = pos.relative(dir);
                world.addParticle((ParticleOptions) ModParticles.ROTATION_TRAIL_EMITTER.get(), (double) front.m_123341_() + 0.5, (double) front.m_123342_() + 0.5, (double) front.m_123343_() + 0.5, (double) dir.get3DDataValue(), 0.71, (double) (state.m_61143_(INVERTED) ? 1 : -1));
            }
            return true;
        } else {
            return super.m_8133_(state, world, pos, eventID, eventParam);
        }
    }
}