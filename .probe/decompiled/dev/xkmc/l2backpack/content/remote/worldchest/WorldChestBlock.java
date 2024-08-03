package dev.xkmc.l2backpack.content.remote.worldchest;

import dev.xkmc.l2backpack.content.capability.PickupConfig;
import dev.xkmc.l2backpack.content.common.ContentTransfer;
import dev.xkmc.l2backpack.content.tool.TweakerTool;
import dev.xkmc.l2backpack.init.registrate.BackpackBlocks;
import dev.xkmc.l2backpack.init.registrate.BackpackItems;
import dev.xkmc.l2modularblock.mult.CreateBlockStateBlockMethod;
import dev.xkmc.l2modularblock.mult.DefaultStateBlockMethod;
import dev.xkmc.l2modularblock.mult.OnClickBlockMethod;
import dev.xkmc.l2modularblock.mult.PlacementBlockMethod;
import dev.xkmc.l2modularblock.mult.SetPlacedByBlockMethod;
import dev.xkmc.l2modularblock.one.BlockEntityBlockMethod;
import dev.xkmc.l2modularblock.one.GetBlockItemBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import dev.xkmc.l2modularblock.one.SpecialDropBlockMethod;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WorldChestBlock implements CreateBlockStateBlockMethod, DefaultStateBlockMethod, PlacementBlockMethod, OnClickBlockMethod, GetBlockItemBlockMethod, SpecialDropBlockMethod, SetPlacedByBlockMethod, ShapeBlockMethod {

    public static final WorldChestBlock INSTANCE = new WorldChestBlock();

    protected static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);

    public static final BlockEntityBlockMethod<WorldChestBlockEntity> TILE_ENTITY_SUPPLIER_BUILDER = new WorldChestAnalogBlockEntity<WorldChestBlockEntity>(BackpackBlocks.TE_WORLD_CHEST, WorldChestBlockEntity.class);

    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COLOR);
    }

    public BlockState getDefaultState(BlockState state) {
        return (BlockState) state.m_61124_(COLOR, DyeColor.WHITE);
    }

    public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (level.getBlockEntity(pos) instanceof WorldChestBlockEntity chest) {
            ItemStack stack = player.m_21120_(hand);
            if (stack.getItem() instanceof TweakerTool) {
                return InteractionResult.PASS;
            } else if (stack.getItem() instanceof DyeItem dye) {
                if (!level.isClientSide()) {
                    level.setBlockAndUpdate(pos, (BlockState) state.m_61124_(COLOR, dye.getDyeColor()));
                    chest.setColor(dye.getDyeColor().getId());
                } else {
                    ContentTransfer.playSound(player);
                }
                return InteractionResult.SUCCESS;
            } else {
                BlockPos blockpos = pos.above();
                if (level.getBlockState(blockpos).m_60796_(level, blockpos)) {
                    return InteractionResult.sidedSuccess(level.isClientSide);
                } else if (level.isClientSide) {
                    ContentTransfer.playSound(player);
                    return InteractionResult.SUCCESS;
                } else {
                    player.openMenu(chest);
                    PiglinAi.angerNearbyPiglins(player, true);
                    return InteractionResult.CONSUME;
                }
            }
        } else {
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
        return world.getBlockEntity(pos) instanceof WorldChestBlockEntity chest ? buildStack(state, chest) : BackpackItems.DIMENSIONAL_STORAGE[((DyeColor) state.m_61143_(COLOR)).getId()].asStack();
    }

    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity blockentity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        return blockentity instanceof WorldChestBlockEntity chest ? List.of(buildStack(state, chest)) : List.of(BackpackItems.DIMENSIONAL_STORAGE[((DyeColor) state.m_61143_(COLOR)).getId()].asStack());
    }

    public static ItemStack buildStack(BlockState state, WorldChestBlockEntity chest) {
        ItemStack stack = BackpackItems.DIMENSIONAL_STORAGE[((DyeColor) state.m_61143_(COLOR)).getId()].asStack();
        if (chest.owner_id != null) {
            stack.getOrCreateTag().putUUID("owner_id", chest.owner_id);
            stack.getOrCreateTag().putString("owner_name", chest.owner_name);
            stack.getOrCreateTag().putLong("password", chest.password);
            PickupConfig.setConfig(stack, chest.config);
        }
        return stack;
    }

    public BlockState getStateForPlacement(BlockState def, BlockPlaceContext context) {
        return (BlockState) def.m_61124_(COLOR, ((WorldChestItem) context.m_43722_().getItem()).color);
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        UUID id = stack.getOrCreateTag().getUUID("owner_id");
        String name = stack.getOrCreateTag().getString("owner_name");
        long pwd = stack.getOrCreateTag().getLong("password");
        PickupConfig config = PickupConfig.getPickupMode(stack);
        if (blockentity instanceof WorldChestBlockEntity chest) {
            chest.owner_id = id;
            chest.owner_name = name;
            chest.password = pwd;
            chest.config = config;
            chest.setColor(((DyeColor) state.m_61143_(COLOR)).getId());
            chest.addToListener();
        }
    }

    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}