package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.LuxtructosaurusEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FissurePrimalMagmaBlock extends Block {

    public static final IntegerProperty REGEN_HEIGHT = IntegerProperty.create("regen_height", 0, 4);

    public FissurePrimalMagmaBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.NETHER).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().lightLevel(state -> 5).strength(0.5F).isValidSpawn((state, getter, pos, entityType) -> entityType.fireImmune()).hasPostProcess((state, getter, pos) -> true).emissiveRendering((state, getter, pos) -> true).sound(ACSoundTypes.FLOOD_BASALT).randomTicks());
        this.m_49959_((BlockState) this.m_49966_().m_61124_(REGEN_HEIGHT, 0));
    }

    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        if (!entity.isSteppingCarefully() && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entity)) {
            entity.hurt(level.damageSources().hotFloor(), 1.0F);
            entity.setSecondsOnFire(6);
        }
        super.stepOn(level, blockPos, blockState, entity);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos blockPos, Entity entity) {
        if (!(entity instanceof LuxtructosaurusEntity)) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.9, 0.1, 0.9));
            entity.hurt(level.damageSources().hotFloor(), 1.0F);
            entity.setSecondsOnFire(6);
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos blockPos, CollisionContext context) {
        if (context instanceof EntityCollisionContext entityCollisionContext && !(entityCollisionContext.getEntity() instanceof LuxtructosaurusEntity)) {
            return entityCollisionContext.getEntity() instanceof ItemEntity ? Shapes.empty() : PrimalMagmaBlock.SINK_SHAPE;
        }
        return super.m_5939_(state, level, blockPos, context);
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos blockPos) {
        return Shapes.block();
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos blockPos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        if (!PrimalMagmaBlock.isBossActive(serverLevel)) {
            int regenHeight = (Integer) blockState.m_61143_(REGEN_HEIGHT);
            for (int i = 0; i < regenHeight + 1; i++) {
                BlockState regenState = ACBlockRegistry.FLOOD_BASALT.get().defaultBlockState();
                BlockPos regenPos = blockPos.above(i);
                List<BlockState> neighbors = new ArrayList();
                if (i == 0 || serverLevel.m_8055_(regenPos).m_60795_()) {
                    for (Direction direction : Direction.values()) {
                        BlockState offsetState = serverLevel.m_8055_(regenPos.relative(direction));
                        if (offsetState.m_204336_(ACTagRegistry.REGENERATES_AFTER_PRIMORDIAL_BOSS_FIGHT) && !offsetState.m_60713_(this)) {
                            neighbors.add(offsetState);
                        }
                    }
                    if (!neighbors.isEmpty()) {
                        if (neighbors.stream().anyMatch(state -> state.m_60713_(Blocks.GRASS_BLOCK))) {
                            regenState = Blocks.GRASS_BLOCK.defaultBlockState();
                        } else if (neighbors.stream().anyMatch(state -> state.m_60713_(Blocks.MOSS_BLOCK))) {
                            regenState = Blocks.MOSS_BLOCK.defaultBlockState();
                        } else if (neighbors.stream().anyMatch(state -> state.m_60713_(Blocks.DIRT))) {
                            regenState = Blocks.DIRT.defaultBlockState();
                        } else {
                            regenState = Util.getRandom(neighbors, randomSource);
                        }
                    } else {
                        BlockState lowestNonMagma = this.findNonMagmaBlockBeneath(serverLevel, blockPos);
                        if (lowestNonMagma.m_204336_(ACTagRegistry.REGENERATES_AFTER_PRIMORDIAL_BOSS_FIGHT)) {
                            regenState = lowestNonMagma;
                        }
                    }
                    serverLevel.m_46597_(regenPos, regenState);
                }
            }
            if (serverLevel.f_46441_.nextInt(2) == 0) {
                serverLevel.m_247517_((Player) null, blockPos, ACSoundRegistry.PRIMAL_MAGMA_FISSURE_CLOSE.get(), SoundSource.BLOCKS);
            }
        }
    }

    private BlockState findNonMagmaBlockBeneath(Level level, BlockPos blockPos) {
        while (blockPos.m_123342_() > level.m_141937_() && level.getBlockState(blockPos).m_60713_(this)) {
            blockPos = blockPos.below();
        }
        return level.getBlockState(blockPos);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        BlockState newState = super.m_7417_(state, direction, state1, levelAccessor, blockPos, blockPos1);
        levelAccessor.scheduleTick(blockPos, this, 2);
        return newState;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
        Vec3 center = Vec3.upFromBottomCenterOf(pos, 1.0).add((double) (randomSource.nextFloat() - 0.5F), 0.0, (double) (randomSource.nextFloat() - 0.5F));
        Vec3 delta = new Vec3((double) (randomSource.nextFloat() - 0.5F), (double) (randomSource.nextFloat() - 0.5F), (double) (randomSource.nextFloat() - 0.5F));
        if (randomSource.nextFloat() <= 0.33F) {
            level.addParticle(ACParticleRegistry.RED_VENT_SMOKE.get(), center.x, center.y, center.z, delta.x * 0.3F, 0.15F, delta.z * 0.3F);
        }
        if (randomSource.nextFloat() < 0.1F) {
            level.addParticle(ParticleTypes.LAVA, center.x, center.y, center.z, delta.x, 0.7F + delta.y, delta.z);
            level.playLocalSound(center.x, center.y, center.z, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.2F + randomSource.nextFloat() * 0.2F, 0.75F + randomSource.nextFloat() * 0.25F, false);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        return new ItemStack(ACBlockRegistry.PRIMAL_MAGMA.get());
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter getter, BlockPos blockPos, PathComputationType computationType) {
        return false;
    }

    public boolean isBurning(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    public BlockPathTypes getAdjacentBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob, BlockPathTypes originalType) {
        return BlockPathTypes.DANGER_FIRE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(REGEN_HEIGHT);
    }
}