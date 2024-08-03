package net.mehvahdjukaar.supplementaries.common.block.blocks;

import java.util.ArrayList;
import java.util.List;
import net.mehvahdjukaar.moonlight.api.block.ItemDisplayTile;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.tiles.UrnBlockTile;
import net.mehvahdjukaar.supplementaries.common.entities.FallingUrnEntity;
import net.mehvahdjukaar.supplementaries.common.utils.BlockUtil;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModCreativeTabs;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class UrnBlock extends FallingBlock implements EntityBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final BooleanProperty TREASURE = ModBlockProperties.TREASURE;

    private static final VoxelShape SHAPE = Shapes.or(m_49796_(4.0, 0.0, 4.0, 12.0, 10.0, 12.0), m_49796_(5.0, 10.0, 5.0, 11.0, 12.0, 11.0), m_49796_(4.0, 12.0, 4.0, 12.0, 14.0, 12.0));

    public UrnBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(WATERLOGGED, false)).m_61124_(TREASURE, false));
    }

    @Override
    public int getDustColor(BlockState state, BlockGetter reader, BlockPos pos) {
        return 6173722;
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (state.m_60734_() != oldState.m_60734_()) {
            worldIn.m_186460_(pos, this, this.m_7198_());
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, TREASURE);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean flag = context.m_43725_().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return (BlockState) this.m_49966_().m_61124_(WATERLOGGED, flag);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType pathType) {
        return false;
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean) stateIn.m_61143_(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(worldIn));
        }
        worldIn.scheduleTick(currentPos, this, this.m_7198_());
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public void tick(BlockState state, ServerLevel pLevel, BlockPos pos, RandomSource pRand) {
        if (m_53241_(pLevel.m_8055_(pos.below())) && pos.m_123342_() >= pLevel.m_141937_()) {
            CompoundTag tag = null;
            if (pLevel.m_7702_(pos) instanceof UrnBlockTile tile) {
                tag = tile.m_187482_();
                tile.m_6211_();
                tile.m_7651_();
            }
            FallingBlockEntity fallingblockentity = FallingUrnEntity.fall(pLevel, pos, state);
            fallingblockentity.blockData = tag;
            this.m_6788_(fallingblockentity);
        }
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return !pState.m_61143_(TREASURE) ? new UrnBlockTile(pPos, pState) : null;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.getBlockEntity(pos) instanceof ItemDisplayTile tile && tile.m_7983_()) {
            return tile.interact(player, handIn);
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() != newState.m_60734_()) {
            if (world.getBlockEntity(pos) instanceof UrnBlockTile tile) {
                Containers.dropContents(world, pos, tile);
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.m_6810_(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos pos) {
        if ((Boolean) blockState.m_61143_(TREASURE)) {
            return 15;
        } else {
            if (world.getBlockEntity(pos) instanceof Container tile && !tile.isEmpty()) {
                return 15;
            }
            return 0;
        }
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        super.m_6402_(world, pos, state, entity, stack);
        BlockUtil.addOptionalOwnership(entity, world, pos);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        if (builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof UrnBlockTile tile) {
            if (!tile.m_58898_()) {
                tile.m_142339_(builder.getLevel());
            }
            List<ItemStack> l = super.m_49635_(state, builder);
            for (int i = 0; i < tile.m_6643_(); i++) {
                l.add(tile.m_8020_(i));
            }
            return l;
        } else {
            ResourceLocation resourcelocation = this.m_60589_();
            if (resourcelocation == BuiltInLootTables.EMPTY) {
                return super.m_49635_(state, builder);
            } else {
                float oldLuck = builder.luck;
                ItemStack stack = builder.getOptionalParameter(LootContextParams.TOOL);
                int f = stack == null ? 0 : EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, stack);
                builder.withLuck(oldLuck + 0.25F * (float) f);
                LootParams lootContext = builder.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
                ServerLevel serverlevel = lootContext.getLevel();
                LootTable loottable = serverlevel.getServer().getLootData().m_278676_(resourcelocation);
                List<ItemStack> selectedLoot;
                do {
                    selectedLoot = loottable.getRandomItems(lootContext);
                    if (selectedLoot.isEmpty()) {
                        break;
                    }
                    selectedLoot = selectedLoot.stream().filter(e -> !ModCreativeTabs.isHidden(e.getItem())).toList();
                } while (selectedLoot.isEmpty());
                return selectedLoot;
            }
        }
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        super.m_5707_(pLevel, pPos, pState, pPlayer);
        if (pLevel.isClientSide && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, pPlayer.m_21211_()) == 0) {
            spawnExtraBrokenParticles(pState, pPos, pLevel);
        }
    }

    public static void spawnExtraBrokenParticles(BlockState state, BlockPos pos, Level level) {
        if (level.isClientSide && (Boolean) state.m_61143_(TREASURE)) {
            level.addDestroyBlockEffect(pos, state);
            if (level.random.nextInt(20) == 0) {
                double x = (double) pos.m_123341_() + 0.5;
                double y = (double) pos.m_123342_() + 0.3125;
                double z = (double) pos.m_123343_() + 0.5;
                level.addParticle(ParticleTypes.SOUL, x, y, z, 0.0, 0.05, 0.0);
                float f = level.random.nextFloat() * 0.4F + level.random.nextFloat() > 0.9F ? 0.6F : 0.0F;
                level.playSound(null, x, y, z, SoundEvents.SOUL_ESCAPE, SoundSource.BLOCKS, f, 0.6F + level.random.nextFloat() * 0.4F);
            }
        }
    }

    @Override
    public void onProjectileHit(Level pLevel, BlockState pState, BlockHitResult pHit, Projectile pProjectile) {
        super.m_5581_(pLevel, pState, pHit, pProjectile);
        BlockPos pos = pHit.getBlockPos();
        pLevel.m_46961_(pos, true);
        spawnExtraBrokenParticles(pState, pos, pLevel);
    }

    @Override
    public void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack, boolean bl) {
        super.m_213646_(state, level, pos, stack, bl);
        if ((double) level.f_46441_.nextFloat() < (Double) CommonConfigs.Functional.URN_ENTITY_SPAWN_CHANCE.get() && level.m_46469_().getBoolean(GameRules.RULE_DOBLOCKDROPS) && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0) {
            List<EntityType<?>> list = new ArrayList();
            for (Holder<EntityType<?>> e : BuiltInRegistries.ENTITY_TYPE.m_206058_(ModTags.URN_SPAWN)) {
                list.add(e.value());
            }
            if (!list.isEmpty()) {
                EntityType<?> e = (EntityType<?>) list.get(level.m_213780_().nextInt(list.size()));
                Entity entity = e.create(level);
                if (entity != null) {
                    if (entity instanceof Slime slime) {
                        slime.setSize(0, true);
                    }
                    entity.moveTo((double) pos.m_123341_() + 0.5, (double) pos.m_123342_(), (double) pos.m_123343_() + 0.5, 0.0F, 0.0F);
                    level.addFreshEntity(entity);
                }
            }
        }
    }
}