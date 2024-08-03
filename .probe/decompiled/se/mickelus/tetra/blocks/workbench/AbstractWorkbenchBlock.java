package se.mickelus.tetra.blocks.workbench;

import com.mojang.datafixers.util.Pair;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkHooks;
import se.mickelus.mutil.util.TileEntityOptional;
import se.mickelus.tetra.blocks.ICraftingEffectProviderBlock;
import se.mickelus.tetra.blocks.ISchematicProviderBlock;
import se.mickelus.tetra.blocks.IToolProviderBlock;
import se.mickelus.tetra.blocks.TetraBlock;
import se.mickelus.tetra.blocks.salvage.BlockInteraction;
import se.mickelus.tetra.blocks.salvage.IInteractiveBlock;
import se.mickelus.tetra.data.DataManager;

public abstract class AbstractWorkbenchBlock extends TetraBlock implements IInteractiveBlock, EntityBlock {

    public AbstractWorkbenchBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        InteractionResult interactionResult = BlockInteraction.attemptInteraction(world, state, pos, player, hand, hit);
        if (interactionResult == InteractionResult.PASS && hand != InteractionHand.OFF_HAND) {
            if (!world.isClientSide) {
                TileEntityOptional.from(world, pos, WorkbenchTile.class).ifPresent(te -> NetworkHooks.openScreen((ServerPlayer) player, te, pos));
            }
            return InteractionResult.SUCCESS;
        } else {
            return interactionResult;
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!this.equals(newState.m_60734_())) {
            ((LazyOptional) TileEntityOptional.from(world, pos, WorkbenchTile.class).map(te -> te.getCapability(ForgeCapabilities.ITEM_HANDLER)).orElse(LazyOptional.empty())).ifPresent(cap -> {
                for (int i = 0; i < cap.getSlots(); i++) {
                    ItemStack itemStack = cap.getStackInSlot(i);
                    if (!itemStack.isEmpty()) {
                        Containers.dropItemStack(world, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), itemStack.copy());
                    }
                }
            });
            TileEntityOptional.from(world, pos, WorkbenchTile.class).ifPresent(BlockEntity::m_7651_);
        }
    }

    protected Stream<Pair<BlockPos, BlockState>> getToolProviderBlockStream(Level world, BlockPos pos) {
        return BlockPos.betweenClosedStream(pos.offset(-2, 0, -2), pos.offset(2, 4, 2)).map(offsetPos -> new Pair(offsetPos, world.getBlockState(offsetPos))).filter(pair -> ((BlockState) pair.getSecond()).m_60734_() instanceof IToolProviderBlock).filter(pair -> ((IToolProviderBlock) ((BlockState) pair.getSecond()).m_60734_()).canProvideTools(world, (BlockPos) pair.getFirst(), pos));
    }

    public Collection<ToolAction> getTools(Level world, BlockPos pos, BlockState blockState) {
        return (Collection<ToolAction>) this.getToolProviderBlockStream(world, pos).map(pair -> ((IToolProviderBlock) ((BlockState) pair.getSecond()).m_60734_()).getTools(world, (BlockPos) pair.getFirst(), (BlockState) pair.getSecond())).flatMap(Collection::stream).collect(Collectors.toSet());
    }

    public int getToolLevel(Level world, BlockPos pos, BlockState blockState, ToolAction toolAction) {
        return (Integer) this.getToolProviderBlockStream(world, pos).map(pair -> ((IToolProviderBlock) ((BlockState) pair.getSecond()).m_60734_()).getToolLevel(world, (BlockPos) pair.getFirst(), (BlockState) pair.getSecond(), toolAction)).max(Integer::compare).orElse(-1);
    }

    public Map<ToolAction, Integer> getToolLevels(Level world, BlockPos pos, BlockState blockState) {
        return (Map<ToolAction, Integer>) this.getToolProviderBlockStream(world, pos).map(pair -> ((IToolProviderBlock) ((BlockState) pair.getSecond()).m_60734_()).getToolLevels(world, (BlockPos) pair.getFirst(), (BlockState) pair.getSecond())).map(Map::entrySet).flatMap(Collection::stream).collect(Collectors.toMap(Entry::getKey, Entry::getValue, Integer::max));
    }

    private Pair<BlockPos, BlockState> getProvidingBlockstate(Level world, BlockPos pos, BlockState blockState, ItemStack targetStack, ToolAction toolAction, int level) {
        return (Pair<BlockPos, BlockState>) this.getToolProviderBlockStream(world, pos).filter(pair -> ((IToolProviderBlock) ((BlockState) pair.getSecond()).m_60734_()).getToolLevel(world, (BlockPos) pair.getFirst(), (BlockState) pair.getSecond(), toolAction) >= level).findFirst().orElse(null);
    }

    public ItemStack onCraftConsumeTool(Level world, BlockPos pos, BlockState blockState, ItemStack targetStack, String slot, boolean isReplacing, Player player, ToolAction requiredTool, int requiredLevel, boolean consumeResources) {
        Pair<BlockPos, BlockState> provider = this.getProvidingBlockstate(world, pos, blockState, targetStack, requiredTool, requiredLevel);
        if (provider != null) {
            IToolProviderBlock block = (IToolProviderBlock) ((BlockState) provider.getSecond()).m_60734_();
            return block.onCraftConsumeTool(world, (BlockPos) provider.getFirst(), (BlockState) provider.getSecond(), targetStack, slot, isReplacing, player, requiredTool, requiredLevel, consumeResources);
        } else {
            return null;
        }
    }

    public ItemStack onActionConsumeTool(Level world, BlockPos pos, BlockState blockState, ItemStack targetStack, Player player, ToolAction requiredTool, int requiredLevel, boolean consumeResources) {
        Pair<BlockPos, BlockState> provider = this.getProvidingBlockstate(world, pos, blockState, targetStack, requiredTool, requiredLevel);
        if (provider != null) {
            IToolProviderBlock block = (IToolProviderBlock) ((BlockState) provider.getSecond()).m_60734_();
            return block.onActionConsumeTool(world, (BlockPos) provider.getFirst(), (BlockState) provider.getSecond(), targetStack, player, requiredTool, requiredLevel, consumeResources);
        } else {
            return null;
        }
    }

    public ResourceLocation[] getSchematics(Level world, BlockPos pos, BlockState blockState) {
        return (ResourceLocation[]) Stream.concat(DataManager.instance.unlockData.getData().values().stream().filter(unlock -> unlock.block != null && unlock.schematics != null && unlock.schematics.length > 0).filter(unlock -> BlockPos.betweenClosedStream(unlock.bounds.move(pos)).anyMatch(offsetPos -> unlock.block.test(world.getBlockState(offsetPos)))).map(unlock -> unlock.schematics), BlockPos.betweenClosedStream(pos.offset(-2, 0, -2), pos.offset(2, 4, 2)).map(offsetPos -> new Pair(offsetPos, world.getBlockState(offsetPos))).filter(pair -> ((BlockState) pair.getSecond()).m_60734_() instanceof ISchematicProviderBlock).filter(pair -> ((ISchematicProviderBlock) ((BlockState) pair.getSecond()).m_60734_()).canUnlockSchematics(world, (BlockPos) pair.getFirst(), pos)).map(pair -> ((ISchematicProviderBlock) ((BlockState) pair.getSecond()).m_60734_()).getSchematics(world, (BlockPos) pair.getFirst(), blockState))).flatMap(Stream::of).toArray(ResourceLocation[]::new);
    }

    public ResourceLocation[] getCraftingEffects(Level world, BlockPos pos, BlockState blockState) {
        return (ResourceLocation[]) Stream.concat(DataManager.instance.unlockData.getData().values().stream().filter(unlock -> unlock.block != null && unlock.effects != null && unlock.effects.length > 0).filter(unlock -> BlockPos.betweenClosedStream(unlock.bounds.move(pos)).anyMatch(offsetPos -> unlock.block.test(world.getBlockState(offsetPos)))).map(unlock -> unlock.effects), BlockPos.betweenClosedStream(pos.offset(-2, 0, -2), pos.offset(2, 4, 2)).map(offsetPos -> new Pair(offsetPos, world.getBlockState(offsetPos))).filter(pair -> ((BlockState) pair.getSecond()).m_60734_() instanceof ICraftingEffectProviderBlock).filter(pair -> ((ICraftingEffectProviderBlock) ((BlockState) pair.getSecond()).m_60734_()).canUnlockCraftingEffects(world, (BlockPos) pair.getFirst(), pos)).map(pair -> ((ICraftingEffectProviderBlock) ((BlockState) pair.getSecond()).m_60734_()).getCraftingEffects(world, (BlockPos) pair.getFirst(), blockState))).flatMap(Stream::of).toArray(ResourceLocation[]::new);
    }

    @Override
    public BlockInteraction[] getPotentialInteractions(Level world, BlockPos pos, BlockState blockState, Direction face, Collection<ToolAction> tools) {
        return face == Direction.UP ? (BlockInteraction[]) TileEntityOptional.from(world, pos, WorkbenchTile.class).map(WorkbenchTile::getInteractions).orElse(new BlockInteraction[0]) : new BlockInteraction[0];
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new WorkbenchTile(blockPos0, blockState1);
    }
}