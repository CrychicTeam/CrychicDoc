package com.simibubi.create.content.processing.burner;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Lang;
import java.util.Random;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlazeBurnerBlock extends HorizontalDirectionalBlock implements IBE<BlazeBurnerBlockEntity>, IWrenchable {

    public static final EnumProperty<BlazeBurnerBlock.HeatLevel> HEAT_LEVEL = EnumProperty.create("blaze", BlazeBurnerBlock.HeatLevel.class);

    public BlazeBurnerBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(HEAT_LEVEL, BlazeBurnerBlock.HeatLevel.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(HEAT_LEVEL, f_54117_);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState p_220082_4_, boolean p_220082_5_) {
        if (!world.isClientSide) {
            if (world.getBlockEntity(pos.above()) instanceof BasinBlockEntity basin) {
                basin.notifyChangeOfContents();
            }
        }
    }

    @Override
    public Class<BlazeBurnerBlockEntity> getBlockEntityClass() {
        return BlazeBurnerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BlazeBurnerBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends BlazeBurnerBlockEntity>) AllBlockEntityTypes.HEATER.get();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.m_61143_(HEAT_LEVEL) == BlazeBurnerBlock.HeatLevel.NONE ? null : IBE.super.newBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockRayTraceResult) {
        ItemStack heldItem = player.m_21120_(hand);
        BlazeBurnerBlock.HeatLevel heat = (BlazeBurnerBlock.HeatLevel) state.m_61143_(HEAT_LEVEL);
        if (AllItems.GOGGLES.isIn(heldItem) && heat != BlazeBurnerBlock.HeatLevel.NONE) {
            return this.onBlockEntityUse(world, pos, bbte -> {
                if (bbte.goggles) {
                    return InteractionResult.PASS;
                } else {
                    bbte.goggles = true;
                    bbte.notifyUpdate();
                    return InteractionResult.SUCCESS;
                }
            });
        } else if (heldItem.isEmpty() && heat != BlazeBurnerBlock.HeatLevel.NONE) {
            return this.onBlockEntityUse(world, pos, bbte -> {
                if (!bbte.goggles) {
                    return InteractionResult.PASS;
                } else {
                    bbte.goggles = false;
                    bbte.notifyUpdate();
                    return InteractionResult.SUCCESS;
                }
            });
        } else if (heat == BlazeBurnerBlock.HeatLevel.NONE) {
            if (heldItem.getItem() instanceof FlintAndSteelItem) {
                world.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, world.random.nextFloat() * 0.4F + 0.8F);
                if (world.isClientSide) {
                    return InteractionResult.SUCCESS;
                } else {
                    heldItem.hurtAndBreak(1, player, p -> p.m_21190_(hand));
                    world.setBlockAndUpdate(pos, AllBlocks.LIT_BLAZE_BURNER.getDefaultState());
                    return InteractionResult.SUCCESS;
                }
            } else {
                return InteractionResult.PASS;
            }
        } else {
            boolean doNotConsume = player.isCreative();
            boolean forceOverflow = !(player instanceof FakePlayer);
            InteractionResultHolder<ItemStack> res = tryInsert(state, world, pos, heldItem, doNotConsume, forceOverflow, false);
            ItemStack leftover = res.getObject();
            if (!world.isClientSide && !doNotConsume && !leftover.isEmpty()) {
                if (heldItem.isEmpty()) {
                    player.m_21008_(hand, leftover);
                } else if (!player.getInventory().add(leftover)) {
                    player.drop(leftover, false);
                }
            }
            return res.getResult() == InteractionResult.SUCCESS ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
    }

    public static InteractionResultHolder<ItemStack> tryInsert(BlockState state, Level world, BlockPos pos, ItemStack stack, boolean doNotConsume, boolean forceOverflow, boolean simulate) {
        if (!state.m_155947_()) {
            return InteractionResultHolder.fail(ItemStack.EMPTY);
        } else if (!(world.getBlockEntity(pos) instanceof BlazeBurnerBlockEntity burnerBE)) {
            return InteractionResultHolder.fail(ItemStack.EMPTY);
        } else if (burnerBE.isCreativeFuel(stack)) {
            if (!simulate) {
                burnerBE.applyCreativeFuel();
            }
            return InteractionResultHolder.success(ItemStack.EMPTY);
        } else if (!burnerBE.tryUpdateFuel(stack, forceOverflow, simulate)) {
            return InteractionResultHolder.fail(ItemStack.EMPTY);
        } else if (!doNotConsume) {
            ItemStack container = stack.hasCraftingRemainingItem() ? stack.getCraftingRemainingItem() : ItemStack.EMPTY;
            if (!world.isClientSide) {
                stack.shrink(1);
            }
            return InteractionResultHolder.success(container);
        } else {
            return InteractionResultHolder.success(ItemStack.EMPTY);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        ItemStack stack = context.m_43722_();
        Item item = stack.getItem();
        BlockState defaultState = this.m_49966_();
        if (!(item instanceof BlazeBurnerBlockItem)) {
            return defaultState;
        } else {
            BlazeBurnerBlock.HeatLevel initialHeat = ((BlazeBurnerBlockItem) item).hasCapturedBlaze() ? BlazeBurnerBlock.HeatLevel.SMOULDERING : BlazeBurnerBlock.HeatLevel.NONE;
            return (BlockState) ((BlockState) defaultState.m_61124_(HEAT_LEVEL, initialHeat)).m_61124_(f_54117_, context.m_8125_().getOpposite());
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        return AllShapes.HEATER_BLOCK_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, BlockGetter p_220071_2_, BlockPos p_220071_3_, CollisionContext p_220071_4_) {
        return p_220071_4_ == CollisionContext.empty() ? AllShapes.HEATER_BLOCK_SPECIAL_COLLISION_SHAPE : this.getShape(p_220071_1_, p_220071_2_, p_220071_3_, p_220071_4_);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState p_149740_1_) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level p_180641_2_, BlockPos p_180641_3_) {
        return Math.max(0, ((BlazeBurnerBlock.HeatLevel) state.m_61143_(HEAT_LEVEL)).ordinal() - 1);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
        if (random.nextInt(10) == 0) {
            if (((BlazeBurnerBlock.HeatLevel) state.m_61143_(HEAT_LEVEL)).isAtLeast(BlazeBurnerBlock.HeatLevel.SMOULDERING)) {
                world.playLocalSound((double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 0.5F), (double) ((float) pos.m_123343_() + 0.5F), SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F, false);
            }
        }
    }

    public static BlazeBurnerBlock.HeatLevel getHeatLevelOf(BlockState blockState) {
        return blockState.m_61138_(HEAT_LEVEL) ? (BlazeBurnerBlock.HeatLevel) blockState.m_61143_(HEAT_LEVEL) : BlazeBurnerBlock.HeatLevel.NONE;
    }

    public static int getLight(BlockState state) {
        BlazeBurnerBlock.HeatLevel level = (BlazeBurnerBlock.HeatLevel) state.m_61143_(HEAT_LEVEL);
        return switch(level) {
            case NONE ->
                0;
            case SMOULDERING ->
                8;
            default ->
                15;
        };
    }

    public static LootTable.Builder buildLootTable() {
        LootItemCondition.Builder survivesExplosion = ExplosionCondition.survivesExplosion();
        BlazeBurnerBlock block = (BlazeBurnerBlock) AllBlocks.BLAZE_BURNER.get();
        LootTable.Builder builder = LootTable.lootTable();
        LootPool.Builder poolBuilder = LootPool.lootPool();
        for (BlazeBurnerBlock.HeatLevel level : BlazeBurnerBlock.HeatLevel.values()) {
            ItemLike drop = level == BlazeBurnerBlock.HeatLevel.NONE ? (ItemLike) AllItems.EMPTY_BLAZE_BURNER.get() : (ItemLike) AllBlocks.BLAZE_BURNER.get();
            poolBuilder.add(((LootPoolSingletonContainer.Builder) LootItem.lootTableItem(drop).m_79080_(survivesExplosion)).m_79080_(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HEAT_LEVEL, level))));
        }
        builder.withPool(poolBuilder.setRolls(ConstantValue.exactly(1.0F)));
        return builder;
    }

    public static enum HeatLevel implements StringRepresentable {

        NONE, SMOULDERING, FADING, KINDLED, SEETHING;

        public static BlazeBurnerBlock.HeatLevel byIndex(int index) {
            return values()[index];
        }

        public BlazeBurnerBlock.HeatLevel nextActiveLevel() {
            return byIndex(this.ordinal() % (values().length - 1) + 1);
        }

        public boolean isAtLeast(BlazeBurnerBlock.HeatLevel heatLevel) {
            return this.ordinal() >= heatLevel.ordinal();
        }

        @Override
        public String getSerializedName() {
            return Lang.asId(this.name());
        }
    }
}