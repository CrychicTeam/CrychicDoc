package net.mehvahdjukaar.supplementaries.common.block.blocks;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.Arrays;
import java.util.stream.Collectors;
import net.mehvahdjukaar.moonlight.api.block.IPistonMotionReact;
import net.mehvahdjukaar.supplementaries.common.network.ClientBoundParticlePacket;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.QuarkCompat;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class FlintBlock extends Block implements IPistonMotionReact {

    private static final Long2ObjectMap<Direction> BY_NORMAL = (Long2ObjectMap<Direction>) Arrays.stream(Direction.values()).collect(Collectors.toMap(direction -> new BlockPos(direction.getNormal()).asLong(), direction -> direction, (direction, direction2) -> {
        throw new IllegalArgumentException("Duplicate keys");
    }, Long2ObjectOpenHashMap::new));

    public FlintBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void onMoved(BlockState movedState, Level level, BlockPos pos, Direction direction, boolean extending, PistonMovingBlockEntity tile) {
        if (!extending && !level.isClientSide) {
            BlockPos firePos = pos.relative(direction);
            if (level.getBlockState(firePos).m_60795_()) {
                for (Direction ironDir : Direction.values()) {
                    if (ironDir.getAxis() != direction.getAxis()) {
                        BlockPos ironPos = firePos.relative(ironDir);
                        BlockState facingState = level.getBlockState(ironPos);
                        if (canBlockCreateSpark(facingState, level, ironPos, ironDir.getOpposite())) {
                            this.ignitePosition(level, firePos, false);
                            return;
                        }
                    }
                }
            }
        }
    }

    private void ignitePosition(Level level, BlockPos firePos, boolean isIronMoving) {
        ModNetwork.CHANNEL.sendToAllClientPlayersInRange(level, firePos, 64.0, new ClientBoundParticlePacket(Vec3.atCenterOf(firePos), ClientBoundParticlePacket.EventType.FLINT_BLOCK_IGNITE, isIronMoving ? 1 : 0));
        for (Direction dir : Direction.values()) {
            if (BaseFireBlock.canBePlacedAt(level, firePos, dir)) {
                level.setBlockAndUpdate(firePos, BaseFireBlock.getState(level, firePos));
                level.m_142346_(null, GameEvent.BLOCK_PLACE, firePos);
                break;
            }
        }
        this.playSound(level, firePos);
    }

    public static boolean canBlockCreateSpark(BlockState state, Level level, BlockPos pos, Direction face) {
        return state.m_204336_(ModTags.FLINT_METALS) && state.m_60783_(level, pos, face);
    }

    private void playSound(Level level, BlockPos pos) {
        RandomSource randomSource = level.getRandom();
        level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, (randomSource.nextFloat() - randomSource.nextFloat()) * 0.2F + 1.0F);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!level.isClientSide && entity instanceof LivingEntity le && !le.m_20161_() && le.m_20142_() && le.f_19797_ % 2 == 0 && le.getItemBySlot(EquipmentSlot.FEET).is(Items.IRON_BOOTS) && level.getBlockState(pos.above()).m_60795_()) {
            this.ignitePosition(level, pos.above(), true);
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block oldBlock, BlockPos targetPos, boolean isMoving) {
        super.m_6861_(state, level, pos, oldBlock, targetPos, isMoving);
        BlockState newState = level.getBlockState(targetPos);
        if (newState.m_60795_() && oldBlock.builtInRegistryHolder().is(ModTags.FLINT_METALS)) {
            Direction dir = (Direction) BY_NORMAL.get(pos.subtract(targetPos).asLong());
            for (Direction pistonDir : Direction.values()) {
                if (dir.getAxis() != pistonDir.getAxis()) {
                    BlockPos tilePos = targetPos.relative(pistonDir);
                    BlockEntity be = level.getBlockEntity(tilePos);
                    if (be instanceof PistonMovingBlockEntity) {
                        PistonMovingBlockEntity piston = (PistonMovingBlockEntity) be;
                        if (piston.getDirection() == pistonDir.getOpposite() && piston.getMovedState().m_60713_(oldBlock) && canBlockCreateSpark(piston.getMovedState(), level, tilePos, dir)) {
                            this.ignitePosition(level, targetPos, true);
                        }
                    } else if (be != null && CompatHandler.QUARK) {
                        BlockState magnetState = QuarkCompat.getMagnetStateForFlintBlock(be, pistonDir);
                        if (magnetState != null && magnetState.m_60713_(oldBlock) && canBlockCreateSpark(magnetState, level, tilePos, dir)) {
                            this.ignitePosition(level, targetPos, true);
                        }
                    }
                }
            }
        }
    }
}