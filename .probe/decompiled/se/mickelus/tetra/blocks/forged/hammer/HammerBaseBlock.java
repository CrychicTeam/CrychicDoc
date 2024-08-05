package se.mickelus.tetra.blocks.forged.hammer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.mutil.util.TileEntityOptional;
import se.mickelus.tetra.TetraToolActions;
import se.mickelus.tetra.advancements.BlockUseCriterion;
import se.mickelus.tetra.blocks.TetraBlock;
import se.mickelus.tetra.blocks.forged.ForgedBlockCommon;
import se.mickelus.tetra.blocks.salvage.BlockInteraction;
import se.mickelus.tetra.blocks.salvage.IInteractiveBlock;
import se.mickelus.tetra.blocks.salvage.InteractiveBlockOverlay;
import se.mickelus.tetra.blocks.salvage.TileBlockInteraction;
import se.mickelus.tetra.items.cell.ThermalCellItem;
import se.mickelus.tetra.module.ItemModuleMajor;

@ParametersAreNonnullByDefault
public class HammerBaseBlock extends TetraBlock implements IInteractiveBlock, EntityBlock {

    public static final String identifier = "hammer_base";

    public static final DirectionProperty facingProp = HorizontalDirectionalBlock.FACING;

    public static final String qualityImprovementKey = "quality";

    public static final BlockInteraction[] interactions = new BlockInteraction[] { new TileBlockInteraction(TetraToolActions.pry, 1, Direction.EAST, 5.0F, 11.0F, 10.0F, 12.0F, HammerBaseBlockEntity.class, tile -> tile.getEffect(true) != null, (world, pos, blockState, player, hand, hitFace) -> removeModule(world, pos, blockState, player, hand, hitFace, true)), new TileBlockInteraction(TetraToolActions.pry, 1, Direction.WEST, 5.0F, 11.0F, 10.0F, 12.0F, HammerBaseBlockEntity.class, tile -> tile.getEffect(false) != null, (world, pos, blockState, player, hand, hitFace) -> removeModule(world, pos, blockState, player, hand, hitFace, false)) };

    @ObjectHolder(registryName = "block", value = "tetra:hammer_base")
    public static HammerBaseBlock instance;

    public HammerBaseBlock() {
        super(ForgedBlockCommon.propertiesNotSolid);
    }

    public static boolean removeModule(Level world, BlockPos pos, BlockState blockState, @Nullable Player player, @Nullable InteractionHand hand, Direction hitFace, boolean isA) {
        ItemStack moduleStack = (ItemStack) TileEntityOptional.from(world, pos, HammerBaseBlockEntity.class).map(te -> te.removeModule(isA)).map(ItemStack::new).orElse(null);
        if (moduleStack != null && !world.isClientSide) {
            if (player != null && player.getInventory().add(moduleStack)) {
                player.playSound(SoundEvents.ITEM_PICKUP, 1.0F, 1.0F);
            } else {
                m_49840_(world, pos.relative(hitFace), moduleStack);
            }
        }
        world.playSound(player, pos, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.PLAYERS, 0.5F, 0.6F);
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(facingProp);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void clientInit() {
        BlockEntityRenderers.register(HammerBaseBlockEntity.type.get(), HammerBaseRenderer::new);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag advanced) {
        tooltip.add(ForgedBlockCommon.locationTooltip);
        tooltip.add(Component.literal(" "));
        tooltip.add(Component.translatable("block.multiblock_hint.1x2x1").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }

    public boolean isFunctional(Level world, BlockPos pos) {
        return (Boolean) TileEntityOptional.from(world, pos, HammerBaseBlockEntity.class).map(HammerBaseBlockEntity::isFunctional).orElse(false);
    }

    public void consumeFuel(Level world, BlockPos pos) {
        TileEntityOptional.from(world, pos, HammerBaseBlockEntity.class).ifPresent(HammerBaseBlockEntity::consumeFuel);
    }

    public int getHammerLevel(Level world, BlockPos pos) {
        return (Integer) TileEntityOptional.from(world, pos, HammerBaseBlockEntity.class).map(HammerBaseBlockEntity::getHammerLevel).orElse(0);
    }

    public ItemStack applyCraftEffects(Level world, BlockPos pos, BlockState blockState, ItemStack targetStack, String slot, boolean isReplacing, Player player, ToolAction requiredTool, int requiredLevel, boolean consumeResources) {
        if (consumeResources) {
            this.consumeFuel(world, pos);
        }
        if (isReplacing) {
            int preciseLevel = (Integer) TileEntityOptional.from(world, pos, HammerBaseBlockEntity.class).map(te -> te.getEffectLevel(HammerEffect.precise)).orElse(0);
            if (preciseLevel > 0) {
                ItemStack upgradedStack = targetStack.copy();
                ItemModuleMajor.addImprovement(upgradedStack, slot, "quality", preciseLevel);
                return upgradedStack;
            }
        }
        return targetStack;
    }

    public ItemStack applyActionEffects(Level world, BlockPos pos, BlockState blockState, ItemStack targetStack, Player player, ToolAction requiredTool, int requiredLevel, boolean consumeResources) {
        if (consumeResources) {
            this.consumeFuel(world, pos);
        }
        return targetStack;
    }

    private Map<String, String> getAdvancementData(Level world, BlockPos pos) {
        return (Map<String, String>) TileEntityOptional.from(world, pos, HammerBaseBlockEntity.class).map(tile -> {
            Map<String, String> result = new HashMap();
            result.put("functional", String.valueOf(tile.isFunctional()));
            Optional.ofNullable(tile.getEffect(true)).ifPresent(module -> result.put("moduleA", module.toString()));
            Optional.ofNullable(tile.getEffect(false)).ifPresent(module -> result.put("moduleB", module.toString()));
            return result;
        }).orElseGet(Collections::emptyMap);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        Direction blockFacing = (Direction) blockState.m_61143_(facingProp);
        HammerBaseBlockEntity te = (HammerBaseBlockEntity) TileEntityOptional.from(world, pos, HammerBaseBlockEntity.class).orElse(null);
        ItemStack heldStack = player.m_21120_(hand);
        Direction facing = rayTraceResult.getDirection();
        if (te == null) {
            return InteractionResult.FAIL;
        } else {
            if (blockFacing.getAxis().equals(facing.getAxis())) {
                int slotIndex = blockFacing.equals(facing) ? 0 : 1;
                if (te.hasCellInSlot(slotIndex)) {
                    ItemStack cell = te.removeCellFromSlot(slotIndex);
                    if (player.getInventory().add(cell)) {
                        player.playSound(SoundEvents.ITEM_PICKUP, 1.0F, 1.0F);
                    } else {
                        m_49840_(world, pos.relative(facing), cell);
                    }
                    world.playSound(player, pos, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.PLAYERS, 0.5F, 0.6F);
                    if (!player.m_9236_().isClientSide) {
                        BlockUseCriterion.trigger((ServerPlayer) player, world.getBlockState(pos), ItemStack.EMPTY, this.getAdvancementData(world, pos));
                    }
                    return InteractionResult.sidedSuccess(player.m_9236_().isClientSide);
                }
                if (heldStack.getItem() instanceof ThermalCellItem) {
                    te.putCellInSlot(heldStack, slotIndex);
                    player.m_21008_(hand, ItemStack.EMPTY);
                    world.playSound(player, pos, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.PLAYERS, 0.5F, 0.5F);
                    if (!player.m_9236_().isClientSide) {
                        BlockUseCriterion.trigger((ServerPlayer) player, world.getBlockState(pos), heldStack, this.getAdvancementData(world, pos));
                    }
                    return InteractionResult.sidedSuccess(player.m_9236_().isClientSide);
                }
            } else {
                boolean isA = Rotation.CLOCKWISE_90.rotate(blockFacing).equals(facing);
                if (te.getEffect(isA) == null) {
                    boolean success = te.setModule(isA, heldStack.getItem());
                    if (success) {
                        if (!player.m_9236_().isClientSide) {
                            BlockUseCriterion.trigger((ServerPlayer) player, world.getBlockState(pos), heldStack, this.getAdvancementData(world, pos));
                        }
                        world.playSound(player, pos, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.PLAYERS, 0.5F, 0.5F);
                        heldStack.shrink(1);
                        if (world.isClientSide) {
                            InteractiveBlockOverlay.markDirty();
                        }
                        return InteractionResult.sidedSuccess(player.m_9236_().isClientSide);
                    }
                }
            }
            return BlockInteraction.attemptInteraction(world, world.getBlockState(pos), pos, player, hand, rayTraceResult);
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!this.equals(newState.m_60734_())) {
            TileEntityOptional.from(world, pos, HammerBaseBlockEntity.class).ifPresent(tile -> {
                for (int i = 0; i < 2; i++) {
                    if (tile.hasCellInSlot(i)) {
                        Containers.dropItemStack(world, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), tile.getStackInSlot(i).copy());
                    }
                }
                Stream.of(tile.getEffect(true), tile.getEffect(false)).filter(Objects::nonNull).map(HammerEffect::getItem).map(ItemStack::new).forEach(stack -> Containers.dropItemStack(world, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), stack));
            });
            TileEntityOptional.from(world, pos, HammerBaseBlockEntity.class).ifPresent(BlockEntity::m_7651_);
        }
    }

    @Override
    public BlockInteraction[] getPotentialInteractions(Level world, BlockPos pos, BlockState state, Direction face, Collection<ToolAction> tools) {
        return (BlockInteraction[]) Arrays.stream(interactions).filter(interaction -> interaction.isPotentialInteraction(world, pos, state, (Direction) state.m_61143_(facingProp), face, tools)).toArray(BlockInteraction[]::new);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        TileEntityOptional.from(world, currentPos, HammerBaseBlockEntity.class).ifPresent(HammerBaseBlockEntity::updateRedstonePower);
        return Direction.DOWN.equals(facing) && !HammerHeadBlock.instance.equals(facingState.m_60734_()) ? Blocks.AIR.defaultBlockState() : super.m_7417_(state, facing, facingState, world, currentPos, facingPos);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BlockState headState = (BlockState) HammerHeadBlock.instance.m_49966_().m_61124_(BlockStateProperties.WATERLOGGED, world.getFluidState(pos.below()).getType() == Fluids.WATER);
        world.setBlock(pos.below(), headState, 3);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return context.m_43725_().getBlockState(context.getClickedPos().below()).m_60629_(context) ? (BlockState) this.m_49966_().m_61124_(facingProp, context.m_8125_().getOpposite()) : null;
    }

    public void onNeighborChange(BlockState state, LevelReader world, BlockPos pos, BlockPos neighbor) {
        TileEntityOptional.from(world, pos, HammerBaseBlockEntity.class).ifPresent(HammerBaseBlockEntity::updateRedstonePower);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) state.m_61124_(facingProp, rotation.rotate((Direction) state.m_61143_(facingProp)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.m_60717_(mirror.getRotation((Direction) state.m_61143_(facingProp)));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HammerBaseBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        return getTicker(entityType, HammerBaseBlockEntity.type.get(), (lvl, pos, blockState, tile) -> tile.tick(lvl, pos, blockState));
    }
}