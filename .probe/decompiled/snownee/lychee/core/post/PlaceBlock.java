package snownee.lychee.core.post;

import com.google.common.collect.UnmodifiableIterator;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.LycheeLootContextParams;
import snownee.lychee.PostActionTypes;
import snownee.lychee.block_crushing.BlockCrushingRecipe;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.def.BlockPredicateHelper;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.mixin.BlockPredicateAccess;
import snownee.lychee.mixin.NbtPredicateAccess;
import snownee.lychee.mixin.StatePropertiesPredicateAccess;
import snownee.lychee.util.CommonProxy;

public class PlaceBlock extends PostAction {

    public final BlockPredicate block;

    public final BlockPos offset;

    public PlaceBlock(BlockPredicate block, BlockPos offset) {
        this.block = block;
        this.offset = offset;
    }

    @Override
    public PostActionType<?> getType() {
        return PostActionTypes.PLACE;
    }

    @Override
    protected void apply(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        BlockPos pos = ctx.getParamOrNull(LycheeLootContextParams.BLOCK_POS);
        if (pos == null) {
            pos = BlockPos.containing(ctx.getParam(LootContextParams.ORIGIN));
        }
        pos = pos.offset(this.offset);
        ServerLevel level = ctx.getServerLevel();
        BlockState oldState = level.m_8055_(pos);
        BlockState state = this.getNewState(oldState);
        if (state != null) {
            if (state.m_60795_()) {
                destroyBlock(level, pos, false);
            } else {
                if (recipe instanceof BlockCrushingRecipe && !oldState.m_60795_()) {
                    level.m_46796_(2001, pos, Block.getId(oldState));
                }
                BlockPredicateAccess access = (BlockPredicateAccess) this.block;
                if (this.getType() == PostActionTypes.PLACE) {
                    Set<String> properties = (Set<String>) ((StatePropertiesPredicateAccess) access.getProperties()).getProperties().stream().map($ -> $.getName()).collect(Collectors.toSet());
                    UnmodifiableIterator blockentity = oldState.m_61148_().entrySet().iterator();
                    while (blockentity.hasNext()) {
                        Entry<Property<?>, Comparable<?>> entry = (Entry<Property<?>, Comparable<?>>) blockentity.next();
                        Property property = (Property) entry.getKey();
                        if (!properties.contains(property.getName()) && state.m_61138_(property)) {
                            state = (BlockState) state.m_61124_(property, (Comparable) entry.getValue());
                        }
                    }
                    if (state.m_61138_(BlockStateProperties.WATERLOGGED) && oldState.m_60819_().isSourceOfType(Fluids.WATER)) {
                        state = (BlockState) state.m_61124_(BlockStateProperties.WATERLOGGED, true);
                    }
                }
                if (level.m_46597_(pos, state)) {
                    NbtPredicate nbtPredicate = access.getNbt();
                    if (this.getType() == PostActionTypes.PLACE && nbtPredicate != NbtPredicate.ANY) {
                        BlockEntity blockentity = level.m_7702_(pos);
                        if (blockentity != null) {
                            CompoundTag compoundtag1 = blockentity.saveWithoutMetadata();
                            CompoundTag compoundtag2 = compoundtag1.copy();
                            compoundtag1.merge(((NbtPredicateAccess) nbtPredicate).getTag());
                            if (!compoundtag1.equals(compoundtag2)) {
                                blockentity.load(compoundtag1);
                                blockentity.setChanged();
                            }
                        }
                    }
                    level.m_220407_(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
                }
            }
        }
    }

    @Nullable
    protected BlockState getNewState(BlockState oldState) {
        return BlockPredicateHelper.anyBlockState(this.block);
    }

    private static boolean destroyBlock(Level level, BlockPos pos, boolean drop) {
        BlockState blockstate = level.getBlockState(pos);
        if (blockstate.m_60795_()) {
            return false;
        } else {
            FluidState fluidstate = level.getFluidState(pos);
            if (!(blockstate.m_60734_() instanceof BaseFireBlock)) {
                level.m_46796_(2001, pos, Block.getId(blockstate));
            }
            if (drop) {
                BlockEntity blockentity = blockstate.m_155947_() ? level.getBlockEntity(pos) : null;
                Block.dropResources(blockstate, level, pos, blockentity, null, ItemStack.EMPTY);
            }
            BlockState legacy = fluidstate.createLegacyBlock();
            if (legacy == blockstate) {
                legacy = Blocks.AIR.defaultBlockState();
            }
            boolean flag = level.setBlock(pos, legacy, 3, 512);
            if (flag) {
                level.m_142346_(null, GameEvent.BLOCK_DESTROY, pos);
            }
            return flag;
        }
    }

    @Override
    public Component getDisplayName() {
        BlockState state = BlockPredicateHelper.anyBlockState(this.block);
        String key = CommonProxy.makeDescriptionId("postAction", PostActionTypes.PLACE.getRegistryName());
        return state.m_60795_() ? Component.translatable(key + ".consume") : Component.translatable(key, state.m_60734_().getName());
    }

    @Override
    public List<ItemStack> getItemOutputs() {
        return BlockPredicateHelper.getMatchedItemStacks(this.block);
    }

    @Override
    public List<BlockPredicate> getBlockOutputs() {
        return List.of(this.block);
    }

    @Override
    public boolean canRepeat() {
        return false;
    }

    public static class Type extends PostActionType<PlaceBlock> {

        public PlaceBlock fromJson(JsonObject o) {
            return new PlaceBlock(BlockPredicateHelper.fromJson(o.get("block")), CommonProxy.parseOffset(o));
        }

        public void toJson(PlaceBlock action, JsonObject o) {
            BlockPos offset = action.offset;
            if (offset.m_123341_() != 0) {
                o.addProperty("offsetX", offset.m_123341_());
            }
            if (offset.m_123342_() != 0) {
                o.addProperty("offsetY", offset.m_123342_());
            }
            if (offset.m_123343_() != 0) {
                o.addProperty("offsetZ", offset.m_123341_());
            }
            o.add("block", BlockPredicateHelper.toJson(action.block));
        }

        public PlaceBlock fromNetwork(FriendlyByteBuf buf) {
            return new PlaceBlock(BlockPredicateHelper.fromNetwork(buf), buf.readBlockPos());
        }

        public void toNetwork(PlaceBlock action, FriendlyByteBuf buf) {
            BlockPredicateHelper.toNetwork(action.block, buf);
            buf.writeBlockPos(action.offset);
        }
    }
}