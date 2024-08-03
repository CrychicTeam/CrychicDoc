package net.minecraft.world.level.block;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PowderSnowBlock extends Block implements BucketPickup {

    private static final float HORIZONTAL_PARTICLE_MOMENTUM_FACTOR = 0.083333336F;

    private static final float IN_BLOCK_HORIZONTAL_SPEED_MULTIPLIER = 0.9F;

    private static final float IN_BLOCK_VERTICAL_SPEED_MULTIPLIER = 1.5F;

    private static final float NUM_BLOCKS_TO_FALL_INTO_BLOCK = 2.5F;

    private static final VoxelShape FALLING_COLLISION_SHAPE = Shapes.box(0.0, 0.0, 0.0, 1.0, 0.9F, 1.0);

    private static final double MINIMUM_FALL_DISTANCE_FOR_SOUND = 4.0;

    private static final double MINIMUM_FALL_DISTANCE_FOR_BIG_SOUND = 7.0;

    public PowderSnowBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public boolean skipRendering(BlockState blockState0, BlockState blockState1, Direction direction2) {
        return blockState1.m_60713_(this) ? true : super.m_6104_(blockState0, blockState1, direction2);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return Shapes.empty();
    }

    @Override
    public void entityInside(BlockState blockState0, Level level1, BlockPos blockPos2, Entity entity3) {
        if (!(entity3 instanceof LivingEntity) || entity3.getFeetBlockState().m_60713_(this)) {
            entity3.makeStuckInBlock(blockState0, new Vec3(0.9F, 1.5, 0.9F));
            if (level1.isClientSide) {
                RandomSource $$4 = level1.getRandom();
                boolean $$5 = entity3.xOld != entity3.getX() || entity3.zOld != entity3.getZ();
                if ($$5 && $$4.nextBoolean()) {
                    level1.addParticle(ParticleTypes.SNOWFLAKE, entity3.getX(), (double) (blockPos2.m_123342_() + 1), entity3.getZ(), (double) (Mth.randomBetween($$4, -1.0F, 1.0F) * 0.083333336F), 0.05F, (double) (Mth.randomBetween($$4, -1.0F, 1.0F) * 0.083333336F));
                }
            }
        }
        entity3.setIsInPowderSnow(true);
        if (!level1.isClientSide) {
            if (entity3.isOnFire() && (level1.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) || entity3 instanceof Player) && entity3.mayInteract(level1, blockPos2)) {
                level1.m_46961_(blockPos2, false);
            }
            entity3.setSharedFlagOnFire(false);
        }
    }

    @Override
    public void fallOn(Level level0, BlockState blockState1, BlockPos blockPos2, Entity entity3, float float4) {
        if (!((double) float4 < 4.0) && entity3 instanceof LivingEntity $$5) {
            LivingEntity.Fallsounds $$7 = $$5.getFallSounds();
            SoundEvent $$8 = (double) float4 < 7.0 ? $$7.small() : $$7.big();
            entity3.playSound($$8, 1.0F, 1.0F);
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        if (collisionContext3 instanceof EntityCollisionContext $$4) {
            Entity $$5 = $$4.getEntity();
            if ($$5 != null) {
                if ($$5.fallDistance > 2.5F) {
                    return FALLING_COLLISION_SHAPE;
                }
                boolean $$6 = $$5 instanceof FallingBlockEntity;
                if ($$6 || canEntityWalkOnPowderSnow($$5) && collisionContext3.isAbove(Shapes.block(), blockPos2, false) && !collisionContext3.isDescending()) {
                    return super.m_5939_(blockState0, blockGetter1, blockPos2, collisionContext3);
                }
            }
        }
        return Shapes.empty();
    }

    @Override
    public VoxelShape getVisualShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return Shapes.empty();
    }

    public static boolean canEntityWalkOnPowderSnow(Entity entity0) {
        if (entity0.getType().is(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)) {
            return true;
        } else {
            return entity0 instanceof LivingEntity ? ((LivingEntity) entity0).getItemBySlot(EquipmentSlot.FEET).is(Items.LEATHER_BOOTS) : false;
        }
    }

    @Override
    public ItemStack pickupBlock(LevelAccessor levelAccessor0, BlockPos blockPos1, BlockState blockState2) {
        levelAccessor0.m_7731_(blockPos1, Blocks.AIR.defaultBlockState(), 11);
        if (!levelAccessor0.m_5776_()) {
            levelAccessor0.levelEvent(2001, blockPos1, Block.getId(blockState2));
        }
        return new ItemStack(Items.POWDER_SNOW_BUCKET);
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL_POWDER_SNOW);
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return true;
    }
}