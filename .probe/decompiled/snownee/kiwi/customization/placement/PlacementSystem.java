package snownee.kiwi.customization.placement;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import snownee.kiwi.Kiwi;
import snownee.kiwi.customization.CustomizationHooks;
import snownee.kiwi.customization.block.KBlockSettings;
import snownee.kiwi.customization.duck.KPlayer;
import snownee.kiwi.customization.network.SSyncPlaceCountPacket;
import snownee.kiwi.util.Util;

public class PlacementSystem {

    private static final Cache<BlockPlaceContext, PlaceMatchResult> RESULT_CONTEXT = CacheBuilder.newBuilder().weakKeys().expireAfterWrite(100L, TimeUnit.MILLISECONDS).build();

    public static boolean isDebugEnabled(Player player) {
        return player != null && player.isCreative() && player.m_21206_().is(Items.CHAINMAIL_HELMET);
    }

    public static void removeDebugBlocks(Level level, BlockPos start) {
        BlockPos.MutableBlockPos pos = start.mutable();
        pos.move(Direction.UP, 2);
        while (isBlockToRemove(level.getBlockState(pos))) {
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            pos.move(Direction.UP);
        }
    }

    private static boolean isBlockToRemove(BlockState blockState) {
        if (blockState.m_60713_(Blocks.BEDROCK)) {
            return true;
        } else {
            String namespace = BuiltInRegistries.BLOCK.getKey(blockState.m_60734_()).getNamespace();
            return CustomizationHooks.getBlockNamespaces().contains(namespace);
        }
    }

    public static BlockState onPlace(BlockItem blockItem, BlockState blockState, BlockPlaceContext context) {
        PlaceChoices choices = null;
        KBlockSettings settings = KBlockSettings.of(blockState.m_60734_());
        if (settings != null) {
            choices = settings.placeChoices;
        }
        if (choices != null && !choices.alter().isEmpty()) {
            for (PlaceChoices.Alter alter : choices.alter()) {
                BlockState altered = alter.alter(blockItem, context);
                if (altered != null) {
                    return onPlace(blockItem, altered, context);
                }
            }
        }
        if (PlaceSlot.hasNoSlots(blockState.m_60734_())) {
            return blockState;
        } else if (!context.m_7078_() || choices != null && !choices.skippable()) {
            Level level = context.m_43725_();
            BlockPos pos = context.getClickedPos();
            BlockPos.MutableBlockPos mutable = pos.mutable();
            Map<Direction, Collection<PlaceSlot>> neighborSlots = (Map<Direction, Collection<PlaceSlot>>) Direction.stream().map($ -> PlaceSlot.find(level.getBlockState(mutable.setWithOffset(pos, $)), $.getOpposite())).filter(Predicate.not(Collection::isEmpty)).collect(Collectors.toUnmodifiableMap($ -> ((PlaceSlot) $.iterator().next()).side().getOpposite(), Function.identity()));
            if (neighborSlots.isEmpty()) {
                return blockState;
            } else {
                boolean debug = isDebugEnabled(context.m_43723_());
                List<PlaceMatchResult> results = Lists.newArrayList();
                boolean waterLoggable = blockState.m_61138_(BlockStateProperties.WATERLOGGED);
                boolean hasWater = waterLoggable && (Boolean) blockState.m_61143_(BlockStateProperties.WATERLOGGED);
                BlockState noWaterBlockState = hasWater ? (BlockState) blockState.m_61124_(BlockStateProperties.WATERLOGGED, false) : blockState;
                PlaceMatchResult originalResult = null;
                UnmodifiableIterator resultIndex = blockState.m_60734_().getStateDefinition().getPossibleStates().iterator();
                while (resultIndex.hasNext()) {
                    BlockState possibleState = (BlockState) resultIndex.next();
                    if (!waterLoggable || hasWater == (Boolean) possibleState.m_61143_(BlockStateProperties.WATERLOGGED)) {
                        int bonusInterest = 0;
                        if (choices != null) {
                            bonusInterest = choices.test(blockState, possibleState);
                            if (bonusInterest == Integer.MIN_VALUE) {
                                continue;
                            }
                        }
                        PlaceMatchResult result = getPlaceMatchResultAt(possibleState, neighborSlots, bonusInterest);
                        if (result != null) {
                            results.add(result);
                            if (possibleState == noWaterBlockState) {
                                originalResult = result;
                            }
                        }
                    }
                }
                if (results.isEmpty()) {
                    if (debug && !level.isClientSide) {
                        Kiwi.LOGGER.info("No match");
                        level.setBlockAndUpdate(mutable.move(Direction.UP), Blocks.BEDROCK.defaultBlockState());
                    }
                    return blockState;
                } else {
                    results.sort(null);
                    int resultIndexx = 0;
                    int maxInterest = ((PlaceMatchResult) results.get(0)).interest();
                    if (maxInterest > 0 && results.size() > 1 && context.m_43723_() instanceof KPlayer player) {
                        int i = 1;
                        while (i < results.size() && ((PlaceMatchResult) results.get(i)).interest() >= maxInterest) {
                            resultIndexx = i++;
                        }
                        if (resultIndexx > 0) {
                            resultIndexx = player.kiwi$getPlaceCount() % (resultIndexx + 1);
                        }
                    }
                    PlaceMatchResult result = maxInterest == 0 ? originalResult : (PlaceMatchResult) results.get(resultIndexx);
                    if (result == null) {
                        return blockState;
                    } else {
                        if (debug && maxInterest > 0 && !level.isClientSide) {
                            mutable.setWithOffset(pos, Direction.UP);
                            Kiwi.LOGGER.info("Interest: %d".formatted(result.interest()));
                            results.forEach($ -> {
                                if ($ != result) {
                                    level.setBlockAndUpdate(mutable.move(Direction.UP), $.blockState());
                                    Kiwi.LOGGER.info("Alt Interest: %d : %s".formatted($.interest(), $.blockState()));
                                }
                            });
                        }
                        BlockState resultState = result.blockState();
                        for (SlotLink.MatchResult link : result.links()) {
                            resultState = link.onLinkFrom().apply(level, pos, resultState);
                        }
                        RESULT_CONTEXT.put(context, result);
                        return resultState;
                    }
                }
            }
        } else {
            return blockState;
        }
    }

    @Nullable
    public static PlaceMatchResult getPlaceMatchResultAt(BlockState blockState, Map<Direction, Collection<PlaceSlot>> theirSlotsMap, int bonusInterest) {
        int interest = 0;
        List<SlotLink.MatchResult> results = List.of();
        List<Vec3i> offsets = List.of();
        for (Direction side : Util.DIRECTIONS) {
            Collection<PlaceSlot> theirSlots = (Collection<PlaceSlot>) theirSlotsMap.get(side);
            if (theirSlots != null) {
                Collection<PlaceSlot> ourSlots = PlaceSlot.find(blockState, side);
                SlotLink.MatchResult result = SlotLink.find(ourSlots, theirSlots);
                if (result != null) {
                    SlotLink link = result.link();
                    interest += link.interest();
                    if (results.isEmpty()) {
                        results = Lists.newArrayListWithExpectedSize(theirSlotsMap.size());
                        offsets = Lists.newArrayListWithExpectedSize(theirSlotsMap.size());
                    }
                    results.add(result);
                    offsets.add(side.getNormal());
                }
            }
        }
        return interest < 0 ? null : new PlaceMatchResult(blockState, interest + bonusInterest, results, offsets);
    }

    public static void onBlockPlaced(BlockPlaceContext context) {
        PlaceMatchResult result = (PlaceMatchResult) RESULT_CONTEXT.getIfPresent(context);
        if (result != null) {
            RESULT_CONTEXT.invalidate(context);
            BlockPos.MutableBlockPos mutable = context.getClickedPos().mutable();
            for (int i = 0; i < result.links().size(); i++) {
                BlockPos theirPos = mutable.setWithOffset(context.getClickedPos(), (Vec3i) result.offsets().get(i));
                BlockState theirState = context.m_43725_().getBlockState(theirPos);
                SlotLink.MatchResult link = (SlotLink.MatchResult) result.links().get(i);
                theirState = link.onLinkTo().apply(context.m_43725_(), theirPos, theirState);
                context.m_43725_().setBlock(theirPos, theirState, 11);
            }
            Player player = context.m_43723_();
            if (player != null) {
                ((KPlayer) player).kiwi$incrementPlaceCount();
                if (player instanceof ServerPlayer serverPlayer) {
                    SSyncPlaceCountPacket.sync(serverPlayer);
                }
            }
        }
    }

    public static void onBlockRemoved(Level level, BlockPos pos, BlockState oldState, BlockState newState) {
        if (!PlaceSlot.hasNoSlots(oldState.m_60734_())) {
            BlockPos.MutableBlockPos mutable = pos.mutable();
            for (Direction direction : Util.DIRECTIONS) {
                BlockState neighborState = level.getBlockState(mutable.setWithOffset(pos, direction));
                if (!PlaceSlot.hasNoSlots(neighborState.m_60734_())) {
                    SlotLink.MatchResult result = SlotLink.find(oldState, neighborState, direction);
                    if (result != null) {
                        neighborState = result.onUnlinkTo().apply(level, mutable, neighborState);
                        level.setBlockAndUpdate(mutable, neighborState);
                    }
                }
            }
        }
    }
}