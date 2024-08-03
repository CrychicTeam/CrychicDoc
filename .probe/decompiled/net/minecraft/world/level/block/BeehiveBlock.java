package net.minecraft.world.level.block;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BeehiveBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final IntegerProperty HONEY_LEVEL = BlockStateProperties.LEVEL_HONEY;

    public static final int MAX_HONEY_LEVELS = 5;

    private static final int SHEARED_HONEYCOMB_COUNT = 3;

    public BeehiveBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(HONEY_LEVEL, 0)).m_61124_(FACING, Direction.NORTH));
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState0) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState0, Level level1, BlockPos blockPos2) {
        return (Integer) blockState0.m_61143_(HONEY_LEVEL);
    }

    @Override
    public void playerDestroy(Level level0, Player player1, BlockPos blockPos2, BlockState blockState3, @Nullable BlockEntity blockEntity4, ItemStack itemStack5) {
        super.m_6240_(level0, player1, blockPos2, blockState3, blockEntity4, itemStack5);
        if (!level0.isClientSide && blockEntity4 instanceof BeehiveBlockEntity $$6) {
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack5) == 0) {
                $$6.emptyAllLivingFromHive(player1, blockState3, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
                level0.updateNeighbourForOutputSignal(blockPos2, this);
                this.angerNearbyBees(level0, blockPos2);
            }
            CriteriaTriggers.BEE_NEST_DESTROYED.trigger((ServerPlayer) player1, blockState3, itemStack5, $$6.getOccupantCount());
        }
    }

    private void angerNearbyBees(Level level0, BlockPos blockPos1) {
        List<Bee> $$2 = level0.m_45976_(Bee.class, new AABB(blockPos1).inflate(8.0, 6.0, 8.0));
        if (!$$2.isEmpty()) {
            List<Player> $$3 = level0.m_45976_(Player.class, new AABB(blockPos1).inflate(8.0, 6.0, 8.0));
            int $$4 = $$3.size();
            for (Bee $$5 : $$2) {
                if ($$5.m_5448_() == null) {
                    $$5.m_6710_((LivingEntity) $$3.get(level0.random.nextInt($$4)));
                }
            }
        }
    }

    public static void dropHoneycomb(Level level0, BlockPos blockPos1) {
        m_49840_(level0, blockPos1, new ItemStack(Items.HONEYCOMB, 3));
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        ItemStack $$6 = player3.m_21120_(interactionHand4);
        int $$7 = (Integer) blockState0.m_61143_(HONEY_LEVEL);
        boolean $$8 = false;
        if ($$7 >= 5) {
            Item $$9 = $$6.getItem();
            if ($$6.is(Items.SHEARS)) {
                level1.playSound(player3, player3.m_20185_(), player3.m_20186_(), player3.m_20189_(), SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
                dropHoneycomb(level1, blockPos2);
                $$6.hurtAndBreak(1, player3, p_49571_ -> p_49571_.m_21190_(interactionHand4));
                $$8 = true;
                level1.m_142346_(player3, GameEvent.SHEAR, blockPos2);
            } else if ($$6.is(Items.GLASS_BOTTLE)) {
                $$6.shrink(1);
                level1.playSound(player3, player3.m_20185_(), player3.m_20186_(), player3.m_20189_(), SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                if ($$6.isEmpty()) {
                    player3.m_21008_(interactionHand4, new ItemStack(Items.HONEY_BOTTLE));
                } else if (!player3.getInventory().add(new ItemStack(Items.HONEY_BOTTLE))) {
                    player3.drop(new ItemStack(Items.HONEY_BOTTLE), false);
                }
                $$8 = true;
                level1.m_142346_(player3, GameEvent.FLUID_PICKUP, blockPos2);
            }
            if (!level1.isClientSide() && $$8) {
                player3.awardStat(Stats.ITEM_USED.get($$9));
            }
        }
        if ($$8) {
            if (!CampfireBlock.isSmokeyPos(level1, blockPos2)) {
                if (this.hiveContainsBees(level1, blockPos2)) {
                    this.angerNearbyBees(level1, blockPos2);
                }
                this.releaseBeesAndResetHoneyLevel(level1, blockState0, blockPos2, player3, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
            } else {
                this.resetHoneyLevel(level1, blockState0, blockPos2);
            }
            return InteractionResult.sidedSuccess(level1.isClientSide);
        } else {
            return super.m_6227_(blockState0, level1, blockPos2, player3, interactionHand4, blockHitResult5);
        }
    }

    private boolean hiveContainsBees(Level level0, BlockPos blockPos1) {
        return level0.getBlockEntity(blockPos1) instanceof BeehiveBlockEntity $$3 ? !$$3.isEmpty() : false;
    }

    public void releaseBeesAndResetHoneyLevel(Level level0, BlockState blockState1, BlockPos blockPos2, @Nullable Player player3, BeehiveBlockEntity.BeeReleaseStatus beehiveBlockEntityBeeReleaseStatus4) {
        this.resetHoneyLevel(level0, blockState1, blockPos2);
        if (level0.getBlockEntity(blockPos2) instanceof BeehiveBlockEntity $$6) {
            $$6.emptyAllLivingFromHive(player3, blockState1, beehiveBlockEntityBeeReleaseStatus4);
        }
    }

    public void resetHoneyLevel(Level level0, BlockState blockState1, BlockPos blockPos2) {
        level0.setBlock(blockPos2, (BlockState) blockState1.m_61124_(HONEY_LEVEL, 0), 3);
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        if ((Integer) blockState0.m_61143_(HONEY_LEVEL) >= 5) {
            for (int $$4 = 0; $$4 < randomSource3.nextInt(1) + 1; $$4++) {
                this.trySpawnDripParticles(level1, blockPos2, blockState0);
            }
        }
    }

    private void trySpawnDripParticles(Level level0, BlockPos blockPos1, BlockState blockState2) {
        if (blockState2.m_60819_().isEmpty() && !(level0.random.nextFloat() < 0.3F)) {
            VoxelShape $$3 = blockState2.m_60812_(level0, blockPos1);
            double $$4 = $$3.max(Direction.Axis.Y);
            if ($$4 >= 1.0 && !blockState2.m_204336_(BlockTags.IMPERMEABLE)) {
                double $$5 = $$3.min(Direction.Axis.Y);
                if ($$5 > 0.0) {
                    this.spawnParticle(level0, blockPos1, $$3, (double) blockPos1.m_123342_() + $$5 - 0.05);
                } else {
                    BlockPos $$6 = blockPos1.below();
                    BlockState $$7 = level0.getBlockState($$6);
                    VoxelShape $$8 = $$7.m_60812_(level0, $$6);
                    double $$9 = $$8.max(Direction.Axis.Y);
                    if (($$9 < 1.0 || !$$7.m_60838_(level0, $$6)) && $$7.m_60819_().isEmpty()) {
                        this.spawnParticle(level0, blockPos1, $$3, (double) blockPos1.m_123342_() - 0.05);
                    }
                }
            }
        }
    }

    private void spawnParticle(Level level0, BlockPos blockPos1, VoxelShape voxelShape2, double double3) {
        this.spawnFluidParticle(level0, (double) blockPos1.m_123341_() + voxelShape2.min(Direction.Axis.X), (double) blockPos1.m_123341_() + voxelShape2.max(Direction.Axis.X), (double) blockPos1.m_123343_() + voxelShape2.min(Direction.Axis.Z), (double) blockPos1.m_123343_() + voxelShape2.max(Direction.Axis.Z), double3);
    }

    private void spawnFluidParticle(Level level0, double double1, double double2, double double3, double double4, double double5) {
        level0.addParticle(ParticleTypes.DRIPPING_HONEY, Mth.lerp(level0.random.nextDouble(), double1, double2), double5, Mth.lerp(level0.random.nextDouble(), double3, double4), 0.0, 0.0, 0.0);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return (BlockState) this.m_49966_().m_61124_(FACING, blockPlaceContext0.m_8125_().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(HONEY_LEVEL, FACING);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new BeehiveBlockEntity(blockPos0, blockState1);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        return level0.isClientSide ? null : m_152132_(blockEntityTypeT2, BlockEntityType.BEEHIVE, BeehiveBlockEntity::m_155144_);
    }

    @Override
    public void playerWillDestroy(Level level0, BlockPos blockPos1, BlockState blockState2, Player player3) {
        if (!level0.isClientSide && player3.isCreative() && level0.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && level0.getBlockEntity(blockPos1) instanceof BeehiveBlockEntity $$5) {
            ItemStack $$6 = new ItemStack(this);
            int $$7 = (Integer) blockState2.m_61143_(HONEY_LEVEL);
            boolean $$8 = !$$5.isEmpty();
            if ($$8 || $$7 > 0) {
                if ($$8) {
                    CompoundTag $$9 = new CompoundTag();
                    $$9.put("Bees", $$5.writeBees());
                    BlockItem.setBlockEntityData($$6, BlockEntityType.BEEHIVE, $$9);
                }
                CompoundTag $$10 = new CompoundTag();
                $$10.putInt("honey_level", $$7);
                $$6.addTagElement("BlockStateTag", $$10);
                ItemEntity $$11 = new ItemEntity(level0, (double) blockPos1.m_123341_(), (double) blockPos1.m_123342_(), (double) blockPos1.m_123343_(), $$6);
                $$11.setDefaultPickUpDelay();
                level0.m_7967_($$11);
            }
        }
        super.m_5707_(level0, blockPos1, blockState2, player3);
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState0, LootParams.Builder lootParamsBuilder1) {
        Entity $$2 = lootParamsBuilder1.getOptionalParameter(LootContextParams.THIS_ENTITY);
        if ($$2 instanceof PrimedTnt || $$2 instanceof Creeper || $$2 instanceof WitherSkull || $$2 instanceof WitherBoss || $$2 instanceof MinecartTNT) {
            BlockEntity $$3 = lootParamsBuilder1.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
            if ($$3 instanceof BeehiveBlockEntity $$4) {
                $$4.emptyAllLivingFromHive(null, blockState0, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
            }
        }
        return super.m_49635_(blockState0, lootParamsBuilder1);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if (levelAccessor3.m_8055_(blockPos5).m_60734_() instanceof FireBlock && levelAccessor3.m_7702_(blockPos4) instanceof BeehiveBlockEntity $$7) {
            $$7.emptyAllLivingFromHive(null, blockState0, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
        }
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }
}